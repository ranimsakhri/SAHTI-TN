import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { WellnessService } from '../../services/wellness';
import { Journal } from './journal';

describe('Journal', () => {
  const food = {
    id: 4,
    name: 'Lablabi',
    category: 'Plat',
    origin: 'Tunisie',
    serving: '100 g',
    caloriesPer100g: 125,
    proteinPer100g: 8,
    carbohydratesPer100g: 27,
    fatPer100g: 3,
  };
  const wellnessApi = {
    getFoods: vi.fn().mockReturnValue(of([food])),
    getMeals: vi.fn().mockReturnValue(of([])),
    addMeal: vi.fn().mockReturnValue(of({})),
    deleteMeal: vi.fn().mockReturnValue(of(undefined)),
    getActivities: vi.fn().mockReturnValue(of([])),
    addActivity: vi.fn().mockReturnValue(of({})),
    deleteActivity: vi.fn().mockReturnValue(of(undefined)),
    getDailyProgress: vi.fn().mockReturnValue(of({
      date: '2026-11-10',
      caloriesEstimated: 250,
      mealEntries: 1,
      activeMinutes: 30,
      activities: 1,
    })),
  };

  beforeEach(async () => {
    vi.clearAllMocks();
    await TestBed.configureTestingModule({
      imports: [Journal],
      providers: [
        { provide: WellnessService, useValue: wellnessApi },
        provideRouter([]),
      ],
    }).compileComponents();
  });

  it('loads the catalog and daily progress from the shared API', () => {
    const fixture = TestBed.createComponent(Journal);
    fixture.detectChanges();

    expect(wellnessApi.getFoods).toHaveBeenCalledOnce();
    expect(wellnessApi.getMeals).toHaveBeenCalledWith(fixture.componentInstance.date());
    expect(fixture.componentInstance.caloriesEstimated()).toBe(250);
    expect(fixture.componentInstance.activeMinutes()).toBe(30);
  });

  it('sends validated food quantities to the meal journal', () => {
    const component = TestBed.createComponent(Journal).componentInstance;
    component.selectedFoodId = food.id;
    component.quantityGrams = 180;
    component.addMeal();

    expect(wellnessApi.addMeal).toHaveBeenCalledWith({
      foodId: food.id,
      type: 'PETIT_DEJEUNER',
      date: component.date(),
      quantityGrams: 180,
    });
    expect(component.success()).toContain('Repas ajouté');
  });

  it('does not send a meal without a selected food', () => {
    const component = TestBed.createComponent(Journal).componentInstance;
    component.selectedFoodId = 0;
    component.addMeal();

    expect(wellnessApi.addMeal).not.toHaveBeenCalled();
    expect(component.error()).toContain('Choisissez un aliment');
  });
});
