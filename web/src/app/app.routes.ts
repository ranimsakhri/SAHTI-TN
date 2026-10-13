import { Routes } from '@angular/router';
import { Welcome } from './pages/welcome/welcome';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { ProRegister } from './pages/pro-register/pro-register';
import { ProPending } from './pages/pro-pending/pro-pending';
import { ForgotPassword } from './pages/forgot-password/forgot-password';
import { Dashboard } from './pages/dashboard/dashboard';
import { AdminProfessionals } from './pages/admin-professionals/admin-professionals';
import { authGuard, roleGuard } from './services/auth.guard';

export const routes: Routes = [
  { path: '', component: Welcome, pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'pro/register', component: ProRegister },
  { path: 'pro/pending', component: ProPending },
  { path: 'forgot-password', component: ForgotPassword },
  { path: 'dashboard', component: Dashboard, canActivate: [authGuard] },
  { path: 'admin/professionals', component: AdminProfessionals, canActivate: [authGuard, roleGuard('ADMIN')] },
  { path: '**', redirectTo: '' },
];
