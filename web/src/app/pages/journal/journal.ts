import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ActivityEntry, Food, MealEntry, WellnessService } from '../../services/wellness';
import { errMsg } from '../../services/auth';

const localDateString = () => {
  const date = new Date();
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

@Component({
  selector: 'app-journal',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './journal.html',
  styleUrl: './journal.scss',
})
export class Journal implements OnInit {
  private readonly wellness = inject(WellnessService);

  readonly date = signal(localDateString());
  readonly foods = signal<Food[]>([]);
  readonly meals = signal<MealEntry[]>([]);
  readonly activities = signal<ActivityEntry[]>([]);
  readonly caloriesEstimated = signal(0);
  readonly activeMinutes = signal(0);
  readonly loading = signal(false);
  readonly saving = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly foodSearch = signal('');
  readonly filteredFoods = computed(() => {
    const query = this.foodSearch().trim().toLocaleLowerCase('fr');
    return this.foods().filter((food) =>
      !query || `${food.name} ${food.category}`.toLocaleLowerCase('fr').includes(query),
    );
  });

  selectedFoodId = 0;
  mealType = 'PETIT_DEJEUNER';
  quantityGrams = 100;
  activityType = 'Marche';
  durationMinutes = 30;
  intensity = 'MODEREE';
  activityNotes = '';

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading.set(true);
    this.error.set('');
    this.wellness.getFoods().subscribe({
      next: (foods) => this.foods.set(foods),
      error: (error) => this.error.set(errMsg(error, 'Impossible de charger les aliments.')),
    });
    this.loadDay();
  }

  setDate(date: string) {
    this.date.set(date);
    this.loadDay();
  }

  addMeal() {
    this.error.set('');
    this.success.set('');
    if (!this.selectedFoodId || !Number.isFinite(this.quantityGrams) || this.quantityGrams < 1) {
      this.error.set('Choisissez un aliment et une quantité valide.');
      return;
    }
    this.saving.set(true);
    this.wellness.addMeal({
      foodId: this.selectedFoodId,
      type: this.mealType,
      date: this.date(),
      quantityGrams: this.quantityGrams,
    }).subscribe({
      next: () => {
        this.success.set('Repas ajouté à votre journal.');
        this.loadDay();
        this.saving.set(false);
      },
      error: (error) => {
        this.error.set(errMsg(error, 'Impossible d’ajouter ce repas.'));
        this.saving.set(false);
      },
    });
  }

  removeMeal(id: number) {
    this.error.set('');
    this.wellness.deleteMeal(id).subscribe({
      next: () => this.loadDay(),
      error: (error) => this.error.set(errMsg(error, 'Impossible de supprimer ce repas.')),
    });
  }

  addActivity() {
    this.error.set('');
    this.success.set('');
    if (!this.activityType.trim() || !Number.isInteger(this.durationMinutes)
      || this.durationMinutes < 1 || this.durationMinutes > 1440) {
      this.error.set('Saisissez un exercice et une durée comprise entre 1 et 1 440 minutes.');
      return;
    }
    this.saving.set(true);
    this.wellness.addActivity({
      type: this.activityType.trim(),
      date: this.date(),
      durationMinutes: this.durationMinutes,
      intensity: this.intensity,
      notes: this.activityNotes.trim(),
    }).subscribe({
      next: () => {
        this.success.set('Activité ajoutée à votre suivi.');
        this.activityNotes = '';
        this.loadDay();
        this.saving.set(false);
      },
      error: (error) => {
        this.error.set(errMsg(error, 'Impossible d’ajouter cette activité.'));
        this.saving.set(false);
      },
    });
  }

  removeActivity(id: number) {
    this.error.set('');
    this.wellness.deleteActivity(id).subscribe({
      next: () => this.loadDay(),
      error: (error) => this.error.set(errMsg(error, 'Impossible de supprimer cette activité.')),
    });
  }

  private loadDay() {
    this.loading.set(true);
    this.wellness.getMeals(this.date()).subscribe({
      next: (meals) => this.meals.set(meals),
      error: (error) => {
        this.error.set(errMsg(error, 'Impossible de charger le journal alimentaire.'));
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
    this.wellness.getActivities(this.date()).subscribe({
      next: (activities) => this.activities.set(activities),
      error: (error) => this.error.set(errMsg(error, 'Impossible de charger les activités.')),
    });
    this.wellness.getDailyProgress(this.date()).subscribe({
      next: (progress) => {
        this.caloriesEstimated.set(progress.caloriesEstimated);
        this.activeMinutes.set(progress.activeMinutes);
      },
      error: (error) => this.error.set(errMsg(error, 'Impossible de charger votre progression.')),
    });
  }
}
