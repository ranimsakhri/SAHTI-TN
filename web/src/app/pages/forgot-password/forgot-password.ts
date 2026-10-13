import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService, errMsg } from '../../services/auth';
import { AuthShell } from '../../shared/auth-shell';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [FormsModule, RouterLink, AuthShell],
  template: `
    <app-auth-shell>
      <h1 class="titre">Mot de passe oublié</h1>

      @if (step() === 1) {
        <p class="sous">Entrez votre email, nous vous enverrons un code.</p>
        <form (ngSubmit)="sendCode()" #f1="ngForm">
          <label class="field"><span>Email</span>
            <input name="email" type="email" [(ngModel)]="email" required>
          </label>
          @if (error()) { <div class="alert alert-err">{{ error() }}</div> }
          <button class="btn btn-primary" type="submit" [disabled]="f1.invalid || loading()">Recevoir le code</button>
        </form>
      } @else if (step() === 2) {
        <p class="sous">Entrez le code reçu et choisissez un nouveau mot de passe.</p>
        <div class="alert alert-info">
          Version démo : le code s'affiche dans le terminal du serveur (ligne « === DEMO === »).
        </div>
        <form (ngSubmit)="changePassword()" #f2="ngForm">
          <label class="field"><span>Code</span>
            <input name="token" [(ngModel)]="token" required placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx">
          </label>
          <label class="field"><span>Nouveau mot de passe</span>
            <input name="pwd" type="password" [(ngModel)]="newPassword" required>
            <small class="hint">8 caractères minimum, avec lettres et chiffres</small>
          </label>
          @if (error()) { <div class="alert alert-err">{{ error() }}</div> }
          <button class="btn btn-primary" type="submit" [disabled]="f2.invalid || loading()">Changer le mot de passe</button>
        </form>
      } @else {
        <div class="alert alert-ok">Mot de passe modifié avec succès.</div>
        <a class="btn btn-primary" routerLink="/login" style="display:block;text-align:center;text-decoration:none">
          Se connecter
        </a>
      }

      <div class="centre"><a class="lien" routerLink="/login">← Retour à la connexion</a></div>
    </app-auth-shell>
  `,
})
export class ForgotPassword {
  private auth = inject(AuthService);

  step = signal(1);
  email = '';
  token = '';
  newPassword = '';
  error = signal('');
  loading = signal(false);

  sendCode() {
    this.error.set('');
    this.loading.set(true);
    this.auth.forgot(this.email).subscribe({
      next: () => { this.loading.set(false); this.step.set(2); },
      error: (e) => { this.loading.set(false); this.error.set(errMsg(e)); },
    });
  }

  changePassword() {
    this.error.set('');
    this.loading.set(true);
    this.auth.reset(this.token.trim(), this.newPassword).subscribe({
      next: () => { this.loading.set(false); this.step.set(3); },
      error: (e) => { this.loading.set(false); this.error.set(errMsg(e)); },
    });
  }
}
