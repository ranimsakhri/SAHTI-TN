import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, errMsg } from '../../services/auth';
import { AuthShell } from '../../shared/auth-shell';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink, AuthShell],
  template: `
    <app-auth-shell>
      <h1 class="titre">Créer mon compte</h1>
      <p class="sous">Espace client : repas, activité et suivi au quotidien.</p>

      <form (ngSubmit)="submit()" #f="ngForm">
        <div class="row">
          <label class="field"><span>Nom</span><input name="nom" [(ngModel)]="nom" required></label>
          <label class="field"><span>Prénom</span><input name="prenom" [(ngModel)]="prenom" required></label>
        </div>
        <label class="field"><span>Email</span>
          <div class="email-input">
            <input name="email" type="text" [(ngModel)]="email" required pattern="[^\\s@]+"
                   autocomplete="username" inputmode="email" aria-label="Adresse Gmail">
            <span aria-hidden="true">@gmail.com</span>
          </div>
        </label>
        <label class="field"><span>Mot de passe</span>
          <input name="password" type="password" [(ngModel)]="password" required minlength="8"
                 pattern="(?=.*[A-Za-z])(?=.*[0-9]).*" #passwordField="ngModel">
          <small class="hint" [class.hint-error]="passwordField.invalid && (passwordField.dirty || passwordField.touched)"
                 aria-live="polite">
            {{ passwordField.invalid && (passwordField.dirty || passwordField.touched)
              ? 'Le mot de passe doit contenir au moins 8 caractères, avec des lettres et des chiffres.'
              : '8 caractères minimum, avec lettres et chiffres' }}
          </small>
        </label>
        <label class="field"><span>Confirmer le mot de passe</span>
          <input name="confirm" type="password" [(ngModel)]="confirmPassword" required>
        </label>

        <label class="check">
          <input type="checkbox" name="cgu" [(ngModel)]="cgu">
          <span>J'accepte les CGU et la politique de confidentialité</span>
        </label>

        @if (error()) { <div class="alert alert-err">{{ error() }}</div> }

        <button class="btn btn-primary" type="submit" [disabled]="f.invalid || !cgu || loading()">
          {{ loading() ? 'Création...' : 'Créer mon compte' }}
        </button>
      </form>

      <div class="centre">Déjà inscrit ? <a class="lien" routerLink="/login">Se connecter</a></div>
      <div class="centre" style="margin-top:6px">
        Vous êtes nutritionniste ou coach ? <a class="lien" routerLink="/pro/register">Espace professionnels</a>
      </div>
    </app-auth-shell>
  `,
})
export class Register {
  private auth = inject(AuthService);
  private router = inject(Router);

  nom = ''; prenom = ''; email = '';
  password = ''; confirmPassword = '';
  cgu = false;
  error = signal('');
  loading = signal(false);

  submit() {
    this.error.set('');
    if (!this.isPasswordValid()) {
      this.error.set('Le mot de passe doit contenir au moins 8 caractères, avec des lettres et des chiffres.');
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.error.set('Les mots de passe ne correspondent pas');
      return;
    }
    this.loading.set(true);
    this.auth.register({
      nom: this.nom, prenom: this.prenom, email: `${this.email.trim()}@gmail.com`,
      password: this.password, confirmPassword: this.confirmPassword,
    }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (e) => {
        this.loading.set(false);
        this.error.set(errMsg(e, "Erreur lors de l'inscription"));
      },
    });
  }

  private isPasswordValid(): boolean {
    return this.password.length >= 8 && /[A-Za-z]/.test(this.password) && /[0-9]/.test(this.password);
  }
}
