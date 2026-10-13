import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, errMsg } from '../../services/auth';
import { AuthShell } from '../../shared/auth-shell';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, AuthShell],
  template: `
    <app-auth-shell>
      <h1 class="titre">Connexion</h1>
      <p class="sous">Heureux de vous revoir sur SA7TI.</p>

      <form (ngSubmit)="submit()" #f="ngForm">
        <label class="field"><span>Email</span>
          <input name="email" type="email" [(ngModel)]="email" required placeholder="vous@exemple.com">
        </label>
        <label class="field"><span>Mot de passe</span>
          <input name="password" type="password" [(ngModel)]="password" required placeholder="••••••••">
        </label>

        <div class="centre" style="text-align:right;margin-top:8px">
          <a class="lien" routerLink="/forgot-password">Mot de passe oublié ?</a>
        </div>

        @if (error()) { <div class="alert alert-err">{{ error() }}</div> }

        <button class="btn btn-primary" type="submit" [disabled]="f.invalid || loading()">
          {{ loading() ? 'Connexion...' : 'Se connecter' }}
        </button>
      </form>

      <div class="centre">Nouveau ? <a class="lien" routerLink="/register">Créer un compte client</a></div>
      <div class="centre" style="margin-top:6px">
        Nutritionniste ou coach ? <a class="lien" routerLink="/pro/register">Espace professionnels</a>
      </div>
    </app-auth-shell>
  `,
})
export class Login {
  private auth = inject(AuthService);
  private router = inject(Router);

  email = '';
  password = '';
  error = signal('');
  loading = signal(false);

  submit() {
    this.error.set('');
    this.loading.set(true);
    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (e) => {
        this.loading.set(false);
        this.error.set(errMsg(e, 'Connexion impossible'));
      },
    });
  }
}
