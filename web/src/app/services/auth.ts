import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

const API = 'http://localhost:8081/api/auth';

export function errMsg(e: any, fallback = 'Une erreur est survenue'): string {
  if (e?.status === 0) return 'Serveur injoignable. Vérifiez que le backend est lancé.';
  return e?.error?.message || fallback;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);

  register(data: any) {
    return this.http.post<any>(`${API}/register`, data).pipe(tap(r => this.save(r)));
  }
  registerPro(formData: FormData) {
    return this.http.post<{ message: string }>(`${API}/register-pro`, formData);
  }
  login(data: any) {
    return this.http.post<any>(`${API}/login`, data).pipe(tap(r => this.save(r)));
  }
  forgot(email: string) {
    return this.http.post<any>(`${API}/forgot-password`, { email });
  }
  reset(token: string, newPassword: string) {
    return this.http.post<any>(`${API}/reset-password`, { token, newPassword });
  }

  private save(r: any) {
    localStorage.setItem('token', r.token);
    localStorage.setItem('user', JSON.stringify(r));
  }
  get token(): string | null { return localStorage.getItem('token'); }
  get user(): any { return JSON.parse(localStorage.getItem('user') || 'null'); }
  isLoggedIn(): boolean { return !!this.token; }
  logout() { localStorage.removeItem('token'); localStorage.removeItem('user'); }
}
