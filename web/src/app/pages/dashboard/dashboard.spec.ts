import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { EMPTY, of } from 'rxjs';
import { AuthService } from '../../services/auth';
import { CoachingRequestsService } from '../../services/coaching-requests';
import { WellnessService } from '../../services/wellness';
import { Dashboard } from './dashboard';

describe('Dashboard', () => {
  const profileKey = 'sa7ti:client-profile:client%40example.com';
  const childProfileKey = 'sa7ti:child-profile:client%40example.com';
  const requestApi = {
    create: vi.fn().mockReturnValue(of({})),
    inbox: vi.fn().mockReturnValue(of([])),
  };
  const wellnessApi = {
    getProfile: vi.fn().mockReturnValue(EMPTY),
    saveProfile: vi.fn().mockReturnValue(of({})),
  };

  beforeEach(async () => {
    localStorage.removeItem(profileKey);
    localStorage.removeItem(childProfileKey);
    vi.clearAllMocks();
    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        { provide: AuthService, useValue: { user: { email: 'client@example.com', prenom: 'Amel', nom: 'Ben Ali' }, logout: vi.fn() } },
        { provide: CoachingRequestsService, useValue: requestApi },
        { provide: WellnessService, useValue: wellnessApi },
        provideRouter([]),
      ],
    }).compileComponents();
  });

  it('calculates and saves the client BMI from the health profile', () => {
    const fixture = TestBed.createComponent(Dashboard);
    const dashboard = fixture.componentInstance;
    dashboard.heightCm = 170;
    dashboard.weightKg = 65;
    dashboard.gender = 'HOMME';
    dashboard.saveProfile();

    expect(dashboard.bmi()).toBeCloseTo(22.49, 1);
    expect(dashboard.bmiStatus().label).toBe('Corpulence normale');
    expect(JSON.parse(localStorage.getItem(profileKey) ?? '{}').weightKg).toBe(65);
    expect(wellnessApi.saveProfile).toHaveBeenCalledWith(expect.objectContaining({
      birthDate: expect.any(String),
      heightCm: 170,
      weightKg: 65,
      activityLevel: 'SEDENTAIRE',
    }));
  });

  it('shows the child BMI only for a client profile younger than 18', () => {
    const adultFixture = TestBed.createComponent(Dashboard);
    const adultDashboard = adultFixture.componentInstance;
    adultDashboard.saveProfile();
    adultFixture.detectChanges();

    expect(adultDashboard.isChildAccount).toBe(false);
    expect(adultFixture.nativeElement.textContent).not.toContain('Mon IMC enfant');

    const childFixture = TestBed.createComponent(Dashboard);
    const childDashboard = childFixture.componentInstance;
    childDashboard.onAgeChange(12);
    expect(childDashboard.gender).toBe('FILLE');
    childDashboard.gender = 'GARCON';
    childDashboard.heightCm = 140;
    childDashboard.weightKg = 38;
    childDashboard.saveProfile();
    childFixture.detectChanges();

    expect(childDashboard.isChildAccount).toBe(true);
    expect(childDashboard.bmi()).toBeNull();
    expect(childDashboard.childBmi).toBeCloseTo(19.39, 1);
    expect(childFixture.nativeElement.textContent).toContain('Mon IMC enfant');
    expect(childFixture.nativeElement.textContent).not.toContain('MON INDICE DE MASSE CORPORELLE');
  });

  it('toggles the interactive BMI explanation', () => {
    const dashboard = TestBed.createComponent(Dashboard).componentInstance;

    dashboard.toggleBmiInfo();
    expect(dashboard.bmiInfoOpen()).toBe(true);

    dashboard.toggleBmiInfo();
    expect(dashboard.bmiInfoOpen()).toBe(false);
  });

  it('estimates cycle dates when a last-period start date is provided', () => {
    const fixture = TestBed.createComponent(Dashboard);
    const dashboard = fixture.componentInstance;
    dashboard.lastPeriodStart = '2026-10-01';
    dashboard.saveProfile();

    expect(dashboard.cycleEstimate()?.nextPeriod).toBe('29 octobre');
    expect(dashboard.cycleEstimate()?.ovulation).toBe('15 octobre');
  });

  it('sends an accompaniment request to the selected professional', () => {
    const dashboard = TestBed.createComponent(Dashboard).componentInstance;
    dashboard.toggleService('NUTRITIONNISTE', false);
    dashboard.toggleService('COACH', true);
    dashboard.requestNote = '  Reprendre le sport en douceur  ';
    dashboard.requestConsent = true;
    dashboard.submitRequest();

    expect(requestApi.create).toHaveBeenCalledWith({
      type: 'COACH',
      message: expect.stringContaining('Reprendre le sport en douceur'),
    });
    expect(dashboard.requestMessage()).toContain('demande est en cours');
  });

  it('calculates child BMI without applying adult BMI categories', () => {
    const dashboard = TestBed.createComponent(Dashboard).componentInstance;
    dashboard.childAge.set(8);
    dashboard.childHeight.set(128);
    dashboard.childWeight.set(27);

    expect(dashboard.childBmi).toBeCloseTo(16.48, 1);
    expect(dashboard.bmiStatus().label).toBe('À calculer');
  });

  it('saves the child growth measurements for display on the dashboard', () => {
    const dashboard = TestBed.createComponent(Dashboard).componentInstance;
    dashboard.ageYears = 9;
    dashboard.gender = 'GARCON';
    dashboard.heightCm = 132;
    dashboard.weightKg = 30;
    dashboard.saveProfile();
    dashboard.childAge.set(9);
    dashboard.childHeight.set(132);
    dashboard.childWeight.set(30);
    dashboard.childGender.set('GARCON');

    dashboard.saveChildProfile();

    expect(dashboard.page()).toBe('dashboard');
    expect(dashboard.childProfile()).toEqual({ age: 9, heightCm: 132, weightKg: 30, gender: 'GARCON' });
    expect(JSON.parse(localStorage.getItem(childProfileKey) ?? '{}').gender).toBe('GARCON');
  });

  it('offers the requested monthly and annual package pricing', () => {
    const dashboard = TestBed.createComponent(Dashboard).componentInstance;
    expect(dashboard.subscriptionPrice).toBe(20);

    dashboard.toggleService('COACH', true);
    expect(dashboard.subscriptionPrice).toBe(35);
    dashboard.chooseBillingCycle('ANNUAL');
    expect(dashboard.subscriptionPrice).toBe(400);
    expect(dashboard.annualSavings).toBe(20);
  });

  it('creates separate professional requests for the combined annual plan', () => {
    const dashboard = TestBed.createComponent(Dashboard).componentInstance;
    dashboard.toggleService('COACH', true);
    dashboard.chooseBillingCycle('ANNUAL');
    dashboard.requestConsent = true;
    dashboard.requestNote = 'Suivi régulier';
    dashboard.submitRequest();

    expect(requestApi.create).toHaveBeenCalledTimes(2);
    expect(requestApi.create).toHaveBeenCalledWith(expect.objectContaining({
      type: 'NUTRITIONNISTE',
      message: expect.stringContaining('400 DT'),
    }));
    expect(requestApi.create).toHaveBeenCalledWith(expect.objectContaining({
      type: 'COACH',
      message: expect.stringContaining('Formule Duo annuelle'),
    }));
  });

  it('supports progressive onboarding and filtered Tunisian recipes', () => {
    const dashboard = TestBed.createComponent(Dashboard).componentInstance;
    dashboard.nextProfileStep();
    expect(dashboard.profileStep()).toBe(2);
    dashboard.nextProfileStep();
    expect(dashboard.profileStep()).toBe(3);

    dashboard.setRecipeCategory('Entrée');
    expect(dashboard.filteredRecipes.map((recipe) => recipe.category)).toEqual(['Entrée', 'Entrée', 'Entrée']);
    expect(dashboard.filteredRecipes.every((recipe) => recipe.calories !== null && recipe.calories > 0)).toBe(true);

    dashboard.setRecipeCategory('Enfants');
    expect(dashboard.filteredRecipes).toHaveLength(3);
    expect(dashboard.filteredRecipes.every((recipe) => recipe.forChildren)).toBe(true);
  });
});
