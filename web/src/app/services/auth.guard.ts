import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.isLoggedIn() ? true : router.createUrlTree(['/login']);
};

export const roleGuard = (role: string): CanActivateFn => () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.user?.role === role ? true : router.createUrlTree(['/dashboard']);
};
