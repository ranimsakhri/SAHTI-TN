import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

const API = 'http://localhost:8081/api';

export interface ClientProfileData {
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
  gender: string | null;
  birthDate: string | null;
  heightCm: number | null;
  weightKg: number | null;
  goal: string | null;
  activityLevel: string | null;
  dietaryPreferences: string | null;
  allergies: string | null;
}

export interface Food {
  id: number;
  name: string;
  category: string;
  origin: string;
  serving: string;
  caloriesPer100g: number;
  proteinPer100g: number | null;
  carbohydratesPer100g: number | null;
  fatPer100g: number | null;
}

export interface MealEntry {
  id: number;
  foodId: number;
  foodName: string;
  type: string;
  date: string;
  quantityGrams: number;
  caloriesEstimated: number;
}

export interface ActivityEntry {
  id: number;
  type: string;
  date: string;
  durationMinutes: number;
  intensity: string;
  notes: string | null;
}

export interface DailyProgress {
  date: string;
  caloriesEstimated: number;
  mealEntries: number;
  activeMinutes: number;
  activities: number;
}

@Injectable({ providedIn: 'root' })
export class WellnessService {
  private readonly http = inject(HttpClient);

  getProfile() {
    return this.http.get<ClientProfileData>(`${API}/profile`);
  }

  saveProfile(profile: Omit<ClientProfileData, 'email' | 'firstName' | 'lastName' | 'roles'>) {
    return this.http.put<ClientProfileData>(`${API}/profile`, profile);
  }

  getFoods() {
    return this.http.get<Food[]>(`${API}/foods`);
  }

  getMeals(date: string) {
    return this.http.get<MealEntry[]>(`${API}/meals`, { params: { date } });
  }

  addMeal(meal: { foodId: number; type: string; date: string; quantityGrams: number }) {
    return this.http.post<MealEntry>(`${API}/meals`, meal);
  }

  deleteMeal(id: number) {
    return this.http.delete<void>(`${API}/meals/${id}`);
  }

  getActivities(date: string) {
    return this.http.get<ActivityEntry[]>(`${API}/activities`, { params: { date } });
  }

  addActivity(activity: Omit<ActivityEntry, 'id' | 'notes'> & { notes?: string }) {
    return this.http.post<ActivityEntry>(`${API}/activities`, activity);
  }

  deleteActivity(id: number) {
    return this.http.delete<void>(`${API}/activities/${id}`);
  }

  getDailyProgress(date: string) {
    return this.http.get<DailyProgress>(`${API}/progress`, { params: { date } });
  }
}
