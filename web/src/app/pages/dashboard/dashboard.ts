import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, errMsg } from '../../services/auth';
import { CoachingRequest, CoachingRequestsService, ProfessionalType } from '../../services/coaching-requests';
import { WellnessService } from '../../services/wellness';
import { forkJoin } from 'rxjs';

type Gender = 'FEMME' | 'HOMME' | 'AUTRE' | 'FILLE' | 'GARCON';
type DashboardPage = 'dashboard' | 'profile' | 'support' | 'inbox' | 'child-bmi' | 'recipes';
type BillingCycle = 'MONTHLY' | 'ANNUAL';

const localDateString = (date = new Date()) =>
  `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;

interface ClientProfile {
  firstName: string;
  lastName: string;
  gender: Gender;
  ageYears?: number;
  birthDate?: string;
  heightCm: number;
  weightKg: number;
  goal: string;
  activityLevel: string;
  dietaryPreferences: string;
  allergies: string;
  targetWeightKg: number | null;
  lastPeriodStart: string;
  cycleLength: number;
  periodDuration: number;
  avatar: {
    skin: string;
    hairColor: string;
    hair: string;
    eyes: string;
    nose: string;
    mouth: string;
  };
}

interface Recipe {
  id: string;
  name: string;
  calories: number | null;
  time: number;
  category: 'Plat' | 'Entrée' | 'Petit-déjeuner';
  description: string;
  ingredients: string;
  symbol: string;
  forChildren?: boolean;
}

interface ChildProfile {
  age: number;
  heightCm: number;
  weightKg: number;
  gender: 'FILLE' | 'GARCON';
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DatePipe, DecimalPipe, FormsModule, RouterLink],
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly coachingRequests = inject(CoachingRequestsService);
  private readonly wellness = inject(WellnessService);

  readonly isProfessional = ['COACH', 'NUTRITIONNISTE'].includes(this.auth.user?.role);
  readonly page = signal<DashboardPage>(this.isProfessional ? 'inbox' : 'dashboard');
  readonly profile = signal<ClientProfile | null>(this.readProfile());
  readonly profileSaving = signal(false);
  readonly profileError = signal('');
  readonly savedMessage = signal('');
  readonly requestMessage = signal('');
  readonly requestError = signal('');
  readonly savingRequest = signal(false);
  readonly profileStep = signal(1);
  readonly todayDate = localDateString();
  readonly oldestBirthDate = (() => {
    const date = new Date();
    date.setFullYear(date.getFullYear() - 110);
    return localDateString(date);
  })();
  readonly bmiInfoOpen = signal(false);
  readonly childProfile = signal<ChildProfile | null>(this.readChildProfile());
  readonly childProfileError = signal('');
  readonly inboxRequests = signal<CoachingRequest[]>([]);
  readonly inboxLoading = signal(false);
  readonly inboxError = signal('');
  readonly todayLabel = new Date().toLocaleDateString('fr-FR', { weekday: 'long' }).toLocaleUpperCase('fr-FR');
  readonly recipeCategory = signal('Tout');
  readonly recipeSearch = signal('');
  readonly childAge = signal(this.profile()?.ageYears ?? this.childProfile()?.age ?? 8);
  readonly childHeight = signal(this.profile()?.heightCm ?? this.childProfile()?.heightCm ?? 128);
  readonly childWeight = signal(this.profile()?.weightKg ?? this.childProfile()?.weightKg ?? 27);
  readonly childGender = signal<'FILLE' | 'GARCON'>(
    this.profile()?.gender === 'GARCON'
      ? 'GARCON'
      : this.profile()?.gender === 'FILLE'
        ? 'FILLE'
        : this.childProfile()?.gender ?? 'FILLE',
  );
  readonly billingCycle = signal<BillingCycle>('MONTHLY');
  readonly serviceSelection = { nutrition: true, coach: false };
  readonly recipeCategories = ['Tout', 'Petit-déjeuner', 'Entrée', 'Plat', 'Enfants'];
  readonly recipes: Recipe[] = [
    { id: 'ojja-legumes', name: 'Ojja aux légumes & œufs', calories: 340, time: 25, category: 'Plat', description: 'Tomates mijotées, poivrons et œufs, avec juste ce qu’il faut d’huile d’olive.', ingredients: 'Tomates · poivrons · œufs · ail · épices', symbol: '🍳' },
    { id: 'lablabi-leger', name: 'Lablabi tout doux', calories: 390, time: 35, category: 'Plat', description: 'Pois chiches, cumin et citron dans une version équilibrée et généreuse en fibres.', ingredients: 'Pois chiches · pain complet · cumin · citron', symbol: '🥣' },
    { id: 'couscous-vert', name: 'Couscous aux légumes', calories: 465, time: 50, category: 'Plat', description: 'Semoule complète, légumes de saison et pois chiches pour un repas complet.', ingredients: 'Semoule complète · courge · carotte · pois chiches', symbol: '🥕' },
    { id: 'mechouia', name: 'Slata mechouia fraîche', calories: 185, time: 30, category: 'Entrée', description: 'Légumes grillés, herbes fraîches et un filet d’huile d’olive.', ingredients: 'Poivrons · tomates · ail · huile d’olive', symbol: '🥗' },
    { id: 'brik-four', name: 'Brik au thon au four', calories: 285, time: 25, category: 'Entrée', description: 'Le croustillant de la feuille malsouka, dorée au four plutôt que frite.', ingredients: 'Malsouka · thon · œuf · persil · câpres', symbol: '🥟' },
    { id: 'chorba-frik', name: 'Chorba frik maison', calories: 260, time: 40, category: 'Entrée', description: 'Une soupe réconfortante au blé vert concassé et aux herbes.', ingredients: 'Frik · tomate · céleri · coriandre', symbol: '🍲' },
    { id: 'chakchouka', name: 'Chakchouka du matin', calories: 310, time: 20, category: 'Petit-déjeuner', description: 'Un petit-déjeuner salé et coloré, accompagné de pain complet.', ingredients: 'Œufs · tomate · poivron · pain complet', symbol: '🍅' },
    { id: 'yaourt-dattes', name: 'Yaourt, dattes & bsissa', calories: 245, time: 5, category: 'Petit-déjeuner', description: 'Une pause nourrissante avec yaourt nature, bsissa et quelques dattes.', ingredients: 'Yaourt nature · bsissa · dattes · amandes', symbol: '🌴' },
    { id: 'couscous-enfant', name: 'Couscous doux aux légumes', calories: null, time: 40, category: 'Plat', description: 'Une version familiale aux légumes fondants, avec des épices douces et une texture facile à adapter.', ingredients: 'Semoule · carotte · courgette · pois chiches · huile d’olive', symbol: '🥕', forChildren: true },
    { id: 'ojja-enfant', name: 'Ojja douce aux œufs', calories: null, time: 25, category: 'Plat', description: 'Tomate et poivron bien mijotés, sans piment, avec un œuf et du pain pour accompagner.', ingredients: 'Tomates · poivron doux · œuf · pain', symbol: '🍳', forChildren: true },
    { id: 'bsissa-enfant', name: 'Crème de bsissa au yaourt', calories: null, time: 5, category: 'Petit-déjeuner', description: 'Une collation tunisienne onctueuse, à préparer avec une texture adaptée à l’enfant.', ingredients: 'Bsissa · yaourt nature · dattes finement coupées', symbol: '🥣', forChildren: true },
  ];
  firstName = this.profile()?.firstName ?? this.auth.user?.prenom ?? '';
  lastName = this.profile()?.lastName ?? this.auth.user?.nom ?? '';
  ageYears = this.profile()?.ageYears ?? 30;
  birthDate = this.profile()?.birthDate ?? '';
  gender: Gender = this.profile()?.gender ?? 'FEMME';
  heightCm = this.profile()?.heightCm ?? 165;
  weightKg = this.profile()?.weightKg ?? 70;
  goal = this.profile()?.goal ?? 'PERDRE';
  activityLevel = this.profile()?.activityLevel ?? 'SEDENTAIRE';
  dietaryPreferences = this.profile()?.dietaryPreferences ?? '';
  allergies = this.profile()?.allergies ?? '';
  targetWeightKg = this.profile()?.targetWeightKg ?? null;
  lastPeriodStart = this.profile()?.lastPeriodStart ?? '';
  cycleLength = this.profile()?.cycleLength ?? 28;
  periodDuration = this.profile()?.periodDuration ?? 5;
  readonly avatar = {
    skin: this.profile()?.avatar.skin ?? '#d99a72',
    hairColor: this.profile()?.avatar.hairColor ?? '#38281f',
    hair: this.profile()?.avatar.hair ?? 'boucles',
    eyes: this.profile()?.avatar.eyes ?? 'marron',
    nose: this.profile()?.avatar.nose ?? 'fin',
    mouth: this.profile()?.avatar.mouth ?? 'sourire',
  };

  readonly bmi = computed(() => {
    const profile = this.profile();
    if (!profile || Number(profile.ageYears ?? 18) < 18 || !profile.heightCm || !profile.weightKg) return null;
    const heightInMeters = profile.heightCm / 100;
    return profile.weightKg / (heightInMeters * heightInMeters);
  });

  readonly bmiStatus = computed(() => {
    const value = this.bmi();
    if (value === null) return { label: 'À calculer', tone: 'neutral' };
    if (value < 18.5) return { label: 'Insuffisance pondérale', tone: 'warning' };
    if (value < 25) return { label: 'Corpulence normale', tone: 'good' };
    if (value < 30) return { label: 'Surpoids', tone: 'warning' };
    return { label: 'Obésité', tone: 'warning' };
  });

  readonly cycleEstimate = computed(() => {
    const profile = this.profile();
    if (!profile || profile.gender !== 'FEMME' || !profile.lastPeriodStart) return null;

    const start = new Date(`${profile.lastPeriodStart}T12:00:00`);
    if (Number.isNaN(start.getTime())) return null;
    const nextPeriod = new Date(start);
    nextPeriod.setDate(nextPeriod.getDate() + profile.cycleLength);
    const ovulation = new Date(nextPeriod);
    ovulation.setDate(ovulation.getDate() - 14);
    const fertileStart = new Date(ovulation);
    fertileStart.setDate(fertileStart.getDate() - 5);
    const fertileEnd = new Date(ovulation);
    fertileEnd.setDate(fertileEnd.getDate() + 1);

    return {
      nextPeriod: this.formatDate(nextPeriod),
      ovulation: this.formatDate(ovulation),
      fertileWindow: `${this.formatDate(fertileStart)} – ${this.formatDate(fertileEnd)}`,
      periodEnd: this.formatDate(
        new Date(start.getFullYear(), start.getMonth(), start.getDate() + profile.periodDuration - 1),
      ),
    };
  });

  requestNote = '';
  requestConsent = false;

  get isChildAccount(): boolean {
    return Number(this.profile()?.ageYears ?? 18) < 18;
  }

  get isChildDraft(): boolean {
    return Number(this.ageYears) < 18;
  }

  ngOnInit() {
    if (this.isProfessional) this.loadInbox();
    else this.loadSavedProfile();
  }

  private loadSavedProfile() {
    this.wellness.getProfile().subscribe({
      next: (saved) => {
        if (!saved.birthDate || !saved.gender || !saved.heightCm || !saved.weightKg || !saved.goal) return;
        const ageYears = this.ageFromBirthDate(saved.birthDate);
        const profile: ClientProfile = {
          firstName: saved.firstName,
          lastName: saved.lastName,
          gender: saved.gender as Gender,
          ageYears,
          birthDate: saved.birthDate,
          heightCm: saved.heightCm,
          weightKg: saved.weightKg,
          goal: saved.goal,
          activityLevel: saved.activityLevel ?? 'SEDENTAIRE',
          dietaryPreferences: saved.dietaryPreferences ?? '',
          allergies: saved.allergies ?? '',
          targetWeightKg: null,
          lastPeriodStart: '',
          cycleLength: 28,
          periodDuration: 5,
          avatar: { ...this.avatar },
        };
        this.profile.set(profile);
        this.firstName = profile.firstName;
        this.lastName = profile.lastName;
        this.ageYears = ageYears;
        this.birthDate = profile.birthDate ?? '';
        this.gender = profile.gender;
        this.heightCm = profile.heightCm;
        this.weightKg = profile.weightKg;
        this.goal = profile.goal;
        this.activityLevel = profile.activityLevel;
        this.dietaryPreferences = profile.dietaryPreferences;
        this.allergies = profile.allergies;
      },
      error: (error) => this.profileError.set(errMsg(error, 'Impossible de charger votre profil.')),
    });
  }

  private readProfile(): ClientProfile | null {
    const value = localStorage.getItem(this.profileStorageKey);
    return value ? JSON.parse(value) as ClientProfile : null;
  }

  private get profileStorageKey(): string {
    const email = String(this.auth.user?.email ?? 'anonymous').toLowerCase();
    return `sa7ti:client-profile:${encodeURIComponent(email)}`;
  }

  private get childProfileStorageKey(): string {
    const email = String(this.auth.user?.email ?? 'anonymous').toLowerCase();
    return `sa7ti:child-profile:${encodeURIComponent(email)}`;
  }

  private readChildProfile(): ChildProfile | null {
    const value = localStorage.getItem(this.childProfileStorageKey);
    return value ? JSON.parse(value) as ChildProfile : null;
  }

  setPage(page: DashboardPage) {
    this.savedMessage.set('');
    this.page.set(page);
  }

  toggleBmiInfo() {
    this.bmiInfoOpen.update((open) => !open);
  }

  nextProfileStep() {
    this.profileStep.update((step) => Math.min(step + 1, 3));
  }

  previousProfileStep() {
    this.profileStep.update((step) => Math.max(step - 1, 1));
  }

  setRecipeCategory(category: string) {
    this.recipeCategory.set(category);
  }

  saveChildProfile() {
    this.childProfileError.set('');
    const profile: ChildProfile = {
      age: Number(this.childAge()),
      heightCm: Number(this.childHeight()),
      weightKg: Number(this.childWeight()),
      gender: this.childGender(),
    };
    if (
      !Number.isFinite(profile.age) || profile.age < 2 || profile.age >= 18
      || !Number.isFinite(profile.heightCm) || profile.heightCm < 70 || profile.heightCm > 220
      || !Number.isFinite(profile.weightKg) || profile.weightKg < 5 || profile.weightKg > 200
    ) {
      this.childProfileError.set('Vérifiez l’âge, la taille et le poids saisis avant d’enregistrer.');
      return;
    }

    localStorage.setItem(this.childProfileStorageKey, JSON.stringify(profile));
    this.childProfile.set(profile);
    const clientProfile = this.profile();
    if (clientProfile) {
      const updatedProfile: ClientProfile = {
        ...clientProfile,
        ageYears: profile.age,
        heightCm: profile.heightCm,
        weightKg: profile.weightKg,
        gender: profile.gender,
        goal: 'MAINTENIR',
        targetWeightKg: null,
        lastPeriodStart: '',
      };
      localStorage.setItem(this.profileStorageKey, JSON.stringify(updatedProfile));
      this.profile.set(updatedProfile);
      this.ageYears = profile.age;
      this.heightCm = profile.heightCm;
      this.weightKg = profile.weightKg;
      this.gender = profile.gender;
    }
    this.savedMessage.set('Votre repère de croissance est enregistré sur cet appareil.');
    this.page.set('dashboard');
  }

  openChildRecipes() {
    this.setRecipeCategory('Enfants');
    this.setPage('recipes');
  }

  get filteredRecipes(): Recipe[] {
    const category = this.recipeCategory();
    const query = this.recipeSearch().trim().toLocaleLowerCase('fr');
    return this.recipes.filter((recipe) =>
      (category === 'Tout'
        || (category === 'Enfants' ? recipe.forChildren === true : recipe.category === category))
      && (!query || `${recipe.name} ${recipe.ingredients}`.toLocaleLowerCase('fr').includes(query)),
    );
  }

  get childBmi(): number | null {
    const age = this.childAge();
    const heightCm = this.childHeight();
    const weightKg = this.childWeight();
    const height = heightCm / 100;
    if (
      !Number.isFinite(age) || age < 2 || age >= 18
      || !Number.isFinite(heightCm)
      || height < 0.7 || height > 2.2
      || !Number.isFinite(weightKg) || weightKg < 5 || weightKg > 200
    ) return null;
    return weightKg / (height * height);
  }

  get selectedServices(): ProfessionalType[] {
    const services: ProfessionalType[] = [];
    if (this.serviceSelection.nutrition) services.push('NUTRITIONNISTE');
    if (this.serviceSelection.coach) services.push('COACH');
    return services;
  }

  get subscriptionPrice(): number {
    if (this.selectedServices.length === 2) return this.billingCycle() === 'ANNUAL' ? 400 : 35;
    return this.selectedServices.length === 1 ? 20 : 0;
  }

  get annualSavings(): number {
    return this.billingCycle() === 'ANNUAL' && this.selectedServices.length === 2 ? 20 : 0;
  }

  chooseBillingCycle(cycle: BillingCycle) {
    if (cycle === 'ANNUAL' && this.selectedServices.length !== 2) return;
    this.billingCycle.set(cycle);
  }

  toggleService(type: ProfessionalType, enabled: boolean) {
    if (type === 'COACH') this.serviceSelection.coach = enabled;
    else this.serviceSelection.nutrition = enabled;
    if (this.selectedServices.length !== 2) this.billingCycle.set('MONTHLY');
  }

  get avatarShadow(): string {
    return ({ '#f1c7a5': '#dba882', '#d99a72': '#bd7955', '#a96648': '#8d4e38', '#704531': '#583626' }[this.avatar.skin] ?? '#bd7955');
  }

  get avatarHighlight(): string {
    return ({ '#38281f': '#725140', '#171916': '#4d5049', '#95603e': '#c18b65', '#c9904d': '#e5bd75' }[this.avatar.hairColor] ?? '#725140');
  }

  get avatarHairShadow(): string {
    return ({ '#38281f': '#211914', '#171916': '#111210', '#95603e': '#70432e', '#c9904d': '#a46c37' }[this.avatar.hairColor] ?? '#211914');
  }

  get avatarEyeColor(): string {
    return ({ marron: '#49312a', verts: '#66835b', bleus: '#5e8098' }[this.avatar.eyes] ?? '#49312a');
  }

  get hairBackPath(): string {
    if (this.avatar.hair === 'court') return 'M91 141V99c0-55 24-81 59-81s59 26 59 81v42l-16 30-12-47 4-47-35-22-39 21 3 49-11 46z';
    if (this.avatar.hair === 'long') return 'M88 137V98c0-56 26-80 62-80s62 24 62 80v126l-22 18-9-110-19 20-22-18-25 20-10-19-6 108-21-16z';
    return 'M88 126V98c0-53 27-80 62-80s62 27 62 80v43l-15 24-10-62-17 19-24-21-26 20-18-17-9 59z';
  }

  get hairFrontPath(): string {
    if (this.avatar.hair === 'court') return 'M93 112c-3-47 18-71 57-71 40 0 61 24 57 70l-17-13-10-18-26 18-20-18-25 22-12-11z';
    if (this.avatar.hair === 'long') return 'M90 105c-1-51 21-77 60-77s61 26 60 77l-16-15-16-20-26 19-24-19-23 24-12-12z';
    return 'M91 108c-4-48 18-73 59-73 42 0 64 26 60 73l-16-13-12-18-20 17-18-18-21 17-15-10-13 22z';
  }

  openInbox() {
    this.setPage('inbox');
    this.loadInbox();
  }

  loadInbox() {
    this.inboxLoading.set(true);
    this.inboxError.set('');
    this.coachingRequests.inbox().subscribe({
      next: (requests) => {
        this.inboxRequests.set(requests);
        this.inboxLoading.set(false);
      },
      error: (error) => {
        this.inboxError.set(errMsg(error, 'Impossible de charger les demandes.'));
        this.inboxLoading.set(false);
      },
    });
  }

  saveProfile() {
    this.profileError.set('');
    this.savedMessage.set('');
    const current = this.profile();
    const profileAge = Number(this.ageYears);
    const savedBirthDate = this.birthDate || this.dateForAge(profileAge);
    const profile: ClientProfile = {
      firstName: this.firstName.trim(),
      lastName: this.lastName.trim(),
      gender: this.gender,
      ageYears: Number(this.ageYears),
      birthDate: savedBirthDate,
      heightCm: Number(this.heightCm),
      weightKg: Number(this.weightKg),
      goal: Number(this.ageYears) < 18 ? 'MAINTENIR' : this.goal,
      activityLevel: this.activityLevel,
      dietaryPreferences: this.dietaryPreferences.trim(),
      allergies: this.allergies.trim(),
      targetWeightKg: Number(this.ageYears) >= 18 && (this.goal === 'PERDRE' || this.goal === 'PRENDRE')
        ? Number(this.targetWeightKg) || null
        : null,
      lastPeriodStart: this.gender === 'FEMME' ? this.lastPeriodStart : '',
      cycleLength: Number(this.cycleLength),
      periodDuration: Number(this.periodDuration),
      avatar: { ...this.avatar },
    };

    if (profileAge < 18 && profile.gender !== 'FILLE' && profile.gender !== 'GARCON') {
      profile.gender = 'FILLE';
    } else if (profileAge >= 18 && ['FILLE', 'GARCON'].includes(profile.gender)) {
      profile.gender = 'FEMME';
    }
    profile.lastPeriodStart = profile.gender === 'FEMME' ? this.lastPeriodStart : '';

    localStorage.setItem(this.profileStorageKey, JSON.stringify(profile));
    if (current) {
      const user = this.auth.user;
      if (user) {
        localStorage.setItem('user', JSON.stringify({
          ...user,
          prenom: profile.firstName,
          nom: profile.lastName,
        }));
      }
    }
    this.profile.set(profile);
    if (profileAge < 18) {
      this.childAge.set(profileAge);
      this.childHeight.set(profile.heightCm);
      this.childWeight.set(profile.weightKg);
      this.childGender.set(profile.gender === 'GARCON' ? 'GARCON' : 'FILLE');
    }
    this.profileSaving.set(true);
    this.wellness.saveProfile({
      gender: profile.gender,
      birthDate: savedBirthDate,
      heightCm: profile.heightCm,
      weightKg: profile.weightKg,
      goal: profile.goal,
      activityLevel: profile.activityLevel,
      dietaryPreferences: profile.dietaryPreferences,
      allergies: profile.allergies,
    }).subscribe({
      next: () => {
        this.savedMessage.set('Votre profil est enregistré sur votre compte SA7TI.');
        this.profileSaving.set(false);
        this.page.set('dashboard');
      },
      error: (error) => {
        this.profileError.set(errMsg(error, 'Le profil n’a pas pu être synchronisé avec le serveur.'));
        this.savedMessage.set('Votre saisie reste enregistrée sur cet appareil, mais elle n’est pas synchronisée.');
        this.profileSaving.set(false);
      },
    });
  }

  private ageFromBirthDate(date: string): number {
    const birthDate = new Date(`${date}T12:00:00`);
    if (Number.isNaN(birthDate.getTime())) return 0;
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    if (
      today.getMonth() < birthDate.getMonth()
      || (today.getMonth() === birthDate.getMonth() && today.getDate() < birthDate.getDate())
    ) age--;
    return age;
  }

  private dateForAge(age: number): string {
    const date = new Date();
    date.setFullYear(date.getFullYear() - age);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${date.getFullYear()}-${month}-${day}`;
  }

  onAgeChange(age: number) {
    const wasChildDraft = this.isChildDraft;
    this.ageYears = Number(age);
    if (wasChildDraft !== this.isChildDraft) {
      this.heightCm = 0;
      this.weightKg = 0;
      this.targetWeightKg = null;
    }
    if (this.isChildDraft && this.gender !== 'FILLE' && this.gender !== 'GARCON') {
      this.gender = 'FILLE';
    } else if (!this.isChildDraft && (this.gender === 'FILLE' || this.gender === 'GARCON')) {
      this.gender = 'FEMME';
    }
  }

  onBirthDateChange(date: string) {
    this.birthDate = date;
    if (date) this.onAgeChange(this.ageFromBirthDate(date));
  }

  submitRequest() {
    this.requestMessage.set('');
    this.requestError.set('');
    if (!this.requestConsent) {
      this.requestError.set('Veuillez confirmer le partage de vos coordonnées pour recevoir une réponse.');
      return;
    }
    if (!this.selectedServices.length) {
      this.requestError.set('Choisissez au moins un accompagnement.');
      return;
    }
    this.savingRequest.set(true);
    const planLabel = this.selectedServices.length === 2
      ? 'Formule Duo'
      : this.selectedServices[0] === 'COACH' ? 'Formule Coach' : 'Formule Nutrition';
    const periodLabel = this.billingCycle() === 'ANNUAL' ? 'annuelle' : 'mensuelle';
    const message = `${planLabel} ${periodLabel} — ${this.subscriptionPrice} DT${this.annualSavings ? ` (économie de ${this.annualSavings} DT)` : ''}. ${this.requestNote.trim()}`.trim();
    forkJoin(this.selectedServices.map((type) => this.coachingRequests.create({ type, message }))).subscribe({
      next: () => {
        this.requestMessage.set('Votre demande est en cours de traitement par le professionnel. Après confirmation, vous pourrez passer à l’étape suivante et effectuer votre paiement de manière sécurisée. Aucun paiement n’a été effectué pour le moment.');
        this.requestNote = '';
        this.requestConsent = false;
        this.savingRequest.set(false);
      },
      error: (error) => {
        this.requestError.set(errMsg(error, 'Impossible d’envoyer votre demande.'));
        this.savingRequest.set(false);
      },
    });
  }

  get displayName(): string {
    const profile = this.profile();
    return profile?.firstName || this.firstName || 'Bienvenue';
  }

  get authRoleLabel(): string {
    return this.auth.user?.role === 'COACH' ? 'votre espace de coaching' : 'votre espace nutrition';
  }

  get initials(): string {
    const profile = this.profile();
    const first = profile?.firstName || this.firstName;
    const last = profile?.lastName || this.lastName;
    return `${first.charAt(0)}${last.charAt(0)}`.toUpperCase() || 'S';
  }

  get bmiMarker(): number {
    const value = this.bmi() ?? 22;
    return Math.max(4, Math.min(96, ((value - 15) / 25) * 100));
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  private formatDate(date: Date): string {
    return date.toLocaleDateString('fr-FR', { day: 'numeric', month: 'long' });
  }
}
