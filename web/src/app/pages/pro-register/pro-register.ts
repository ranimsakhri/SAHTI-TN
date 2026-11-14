
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, errMsg } from '../../services/auth';
import { AuthShell } from '../../shared/auth-shell';

@Component({
  selector: 'app-pro-register',
  imports: [FormsModule, RouterLink, AuthShell],
  template: `
    <app-auth-shell class="wide">

      <div class="register-header">
        <h1 class="titre">Espace professionnels</h1>
        <p class="sous">
          Déposez votre dossier : un administrateur le vérifiera
          avant d'activer votre compte.
        </p>
      </div>

      <form (ngSubmit)="submit()" #f="ngForm">

        <div class="field profession-field">
          <span>Je suis</span>
          <div class="choices">
            <button
              type="button"
              class="choice"
              [class.active]="type() === 'NUTRITIONNISTE'"
              (click)="type.set('NUTRITIONNISTE')">
              Nutritionniste
            </button>

            <button
              type="button"
              class="choice"
              [class.active]="type() === 'COACH'"
              (click)="type.set('COACH')">
              Coach sportif
            </button>
          </div>
        </div>

        <div class="row">
          <label class="field">
            <span>Nom</span>
            <input name="nom" [(ngModel)]="nom" required placeholder="Votre nom">
          </label>

          <label class="field">
            <span>Prénom</span>
            <input name="prenom" [(ngModel)]="prenom" required placeholder="Votre prénom">
          </label>
        </div>

        <label class="field">
          <span>Email</span>
          <div class="email-input">
            <input
              name="email"
              type="text"
              [(ngModel)]="email"
              required
              pattern="[^\\s@]+"
              autocomplete="username"
              inputmode="email"
              aria-label="Adresse Gmail"
              placeholder="votre.adresse">
            <span aria-hidden="true">@gmail.com</span>
          </div>
        </label>

        <label class="field">
          <span>Spécialité</span>
          <input
            name="specialite"
            [(ngModel)]="specialite"
            required
            [placeholder]="type() === 'COACH'
              ? 'Ex. : musculation, remise en forme'
              : 'Ex. : nutrition sportive, diabète'">
        </label>

        <label class="field bio-field">
          <span>Présentation (facultatif)</span>
          <textarea
            name="bio"
            rows="2"
            maxlength="1000"
            [(ngModel)]="bio"
            placeholder="Présentez votre parcours et votre expérience...">
          </textarea>
        </label>

        <div class="field documents-field">
          <span>Documents obligatoires <small>(PDF, 5 Mo maximum)</small></span>

          <div class="documents-grid">

            <label class="file" [class.done]="cv()">
              <input type="file" accept="application/pdf" (change)="pick('cv', $event)">
              <span class="file-mark" aria-hidden="true">
                <svg viewBox="0 0 20 22" fill="none">
                  <path d="M4 1.5h8l4 4V20H4z" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M12 1.5v4h4M7 11h6M7 14h6" stroke="currentColor" stroke-width="1.5"/>
                </svg>
              </span>
              <span class="file-info">
                <b>Curriculum vitae</b>
                <small>{{ cv()?.name || 'Choisir un fichier PDF' }}</small>
              </span>
              <span class="file-status">{{ cv() ? '✓' : '+' }}</span>
            </label>

            <label class="file" [class.done]="lettre()">
              <input type="file" accept="application/pdf" (change)="pick('lettre', $event)">
              <span class="file-mark" aria-hidden="true">
                <svg viewBox="0 0 20 22" fill="none">
                  <path d="M4 1.5h8l4 4V20H4z" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M12 1.5v4h4M7 11h6M7 14h4" stroke="currentColor" stroke-width="1.5"/>
                </svg>
              </span>
              <span class="file-info">
                <b>Lettre de motivation</b>
                <small>{{ lettre()?.name || 'Choisir un fichier PDF' }}</small>
              </span>
              <span class="file-status">{{ lettre() ? '✓' : '+' }}</span>
            </label>

            <label class="file" [class.done]="diplome()">
              <input type="file" accept="application/pdf" (change)="pick('diplome', $event)">
              <span class="file-mark" aria-hidden="true">
                <svg viewBox="0 0 20 22" fill="none">
                  <path d="M4 1.5h8l4 4V20H4z" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M12 1.5v4h4M7 11h6M7 14h6" stroke="currentColor" stroke-width="1.5"/>
                </svg>
              </span>
              <span class="file-info">
                <b>Diplôme</b>
                <small>{{ diplome()?.name || 'Choisir un fichier PDF' }}</small>
              </span>
              <span class="file-status">{{ diplome() ? '✓' : '+' }}</span>
            </label>

          </div>
        </div>

        <label class="field">
          <span>Mot de passe</span>
          <input
            name="password"
            type="password"
            [(ngModel)]="password"
            required
            minlength="8"
            pattern="(?=.*[A-Za-z])(?=.*[0-9]).*"
            #passwordField="ngModel"
            placeholder="8 caractères minimum">

          <small
            class="hint"
            [class.hint-error]="passwordField.invalid &&
              (passwordField.dirty || passwordField.touched)"
            aria-live="polite">
            {{ passwordField.invalid && (passwordField.dirty || passwordField.touched)
              ? 'Le mot de passe doit contenir au moins 8 caractères, avec des lettres et des chiffres.'
              : '8 caractères minimum, avec lettres et chiffres' }}
          </small>
        </label>

        <label class="field">
          <span>Confirmer le mot de passe</span>
          <input
            name="confirm"
            type="password"
            [(ngModel)]="confirmPassword"
            required
            placeholder="Confirmer le mot de passe">
        </label>

        <label class="check">
          <input type="checkbox" name="cgu" [(ngModel)]="cgu">
          <span>
            Je certifie l'exactitude de mes documents et j'accepte les CGU
          </span>
        </label>

        @if (error()) {
          <div class="alert alert-err">{{ error() }}</div>
        }

        <button
          class="btn btn-primary"
          type="submit"
          [disabled]="f.invalid || !cgu || !cv() || !lettre() || !diplome() || loading()">
          {{ loading() ? 'Envoi du dossier...' : 'Envoyer mon dossier' }}
        </button>

      </form>

      <div class="centre">
        Déjà validé ?
        <a class="lien" routerLink="/login">Se connecter</a>
      </div>

      <div class="centre second-link">
        Vous êtes client ?
        <a class="lien" routerLink="/register">Créer un compte client</a>
      </div>

    </app-auth-shell>
  `,

  styles: [`
    :host {
      display: block;
      color: #153528;
    }

    .register-header {
      margin-bottom: 22px;
    }

    .titre {
      margin: 0 0 8px;
      color: #153528;
      font-size: clamp(25px, 3vw, 32px);
      font-weight: 750;
      letter-spacing: -1px;
    }

    .sous {
      margin: 0;
      color: #687a70;
      font-size: 13px;
      line-height: 1.6;
    }

    form {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 14px 20px;
    }

    .field {
      display: flex;
      min-width: 0;
      flex-direction: column;
      gap: 7px;
      margin: 0;
    }

    .field > span {
      color: #214b39;
      font-size: 13px;
      font-weight: 700;
    }

    .field input,
    .field textarea {
      width: 100%;
      min-width: 0;
      border: 1px solid rgba(33, 75, 57, .16);
      border-radius: 10px;
      padding: 11px 13px;
      background: rgba(255, 255, 255, .78);
      color: #153528;
      font-family: inherit;
      font-size: 13px;
      outline: none;
      transition: .2s;
    }

    .field input {
      height: 43px;
    }

    .field textarea {
      min-height: 65px;
      resize: vertical;
    }

    .field input:focus,
    .field textarea:focus {
      border-color: #527b63;
      background: rgba(255, 255, 255, .96);
      box-shadow: 0 0 0 3px rgba(82, 123, 99, .12);
    }

    .profession-field,
    .row,
    .bio-field,
    .documents-field {
      grid-column: 1 / -1;
    }

    .row {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 16px;
    }

    .choices {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 10px;
    }

    .choice {
      min-height: 42px;
      border: 1px solid rgba(33, 75, 57, .18);
      border-radius: 10px;
      background: rgba(255, 255, 255, .72);
      color: #527b63;
      font-family: inherit;
      font-size: 13px;
      font-weight: 700;
      cursor: pointer;
      transition: .2s;
    }

    .choice:hover {
      border-color: #527b63;
    }

    .choice.active {
      border-color: #214b39;
      background: #214b39;
      color: #fff;
      box-shadow: 0 4px 12px rgba(33, 75, 57, .14);
    }

    .email-input {
      display: flex;
      align-items: center;
      height: 43px;
      overflow: hidden;
      border: 1px solid rgba(33, 75, 57, .16);
      border-radius: 10px;
      background: rgba(255, 255, 255, .78);
    }

    .email-input:focus-within {
      border-color: #527b63;
      box-shadow: 0 0 0 3px rgba(82, 123, 99, .12);
    }

    .email-input input {
      height: 100%;
      border: none;
      background: transparent;
      box-shadow: none;
    }

    .email-input input:focus {
      box-shadow: none;
    }

    .email-input > span {
      flex-shrink: 0;
      padding: 0 12px 0 4px;
      color: #527b63;
      font-size: 12px;
    }

    .documents-field > span small {
      color: #718078;
      font-size: 11px;
      font-weight: 400;
    }

    .documents-grid {
      display: grid;
      grid-template-columns: repeat(3, minmax(0, 1fr));
      gap: 10px;
    }

    .file {
      display: flex;
      min-width: 0;
      min-height: 62px;
      align-items: center;
      gap: 9px;
      padding: 9px;
      border: 1px dashed rgba(33, 75, 57, .3);
      border-radius: 10px;
      background: rgba(255, 255, 255, .68);
      cursor: pointer;
      transition: .2s;
    }

    .file:hover {
      border-color: #527b63;
      background: rgba(255, 255, 255, .95);
    }

    .file.done {
      border-style: solid;
      border-color: #527b63;
      background: rgba(82, 123, 99, .1);
    }

    .file input {
      display: none;
    }

    .file-mark {
      display: grid;
      flex-shrink: 0;
      width: 33px;
      height: 36px;
      place-items: center;
      border-radius: 8px;
      background: rgba(166, 75, 63, .1);
      color: #a64b3f;
    }

    .file-mark svg {
      width: 18px;
      height: 20px;
    }

    .file-info {
      display: flex;
      min-width: 0;
      flex: 1;
      flex-direction: column;
      gap: 4px;
    }

    .file-info b {
      color: #214b39;
      font-size: 11px;
    }

    .file-info small {
      overflow: hidden;
      color: #718078;
      font-size: 10px;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .file-status {
      flex-shrink: 0;
      color: #527b63;
      font-size: 15px;
      font-weight: 800;
    }

    .hint {
      color: #718078;
      font-size: 10px;
      line-height: 1.4;
    }

    .hint-error {
      color: #a64b3f;
    }

    .check {
      display: flex;
      grid-column: 1 / -1;
      align-items: flex-start;
      gap: 9px;
      color: #52665b;
      font-size: 12px;
      line-height: 1.5;
      cursor: pointer;
    }

    .check input {
      width: 16px;
      height: 16px;
      flex-shrink: 0;
      margin-top: 1px;
      accent-color: #527b63;
    }

    .alert {
      grid-column: 1 / -1;
      padding: 11px 14px;
      border-radius: 10px;
      font-size: 12px;
    }

    .alert-err {
      background: rgba(166, 75, 63, .1);
      color: #a64b3f;
    }

    .btn-primary {
      grid-column: 1 / -1;
      width: 100%;
      min-height: 47px;
      border-radius: 11px;
      font-size: 14px;
      font-weight: 750;
    }

    .btn-primary:disabled {
      opacity: .55;
      cursor: not-allowed;
    }

    .centre {
      margin-top: 13px;
      color: #718078;
      font-size: 12px;
      text-align: center;
    }

    .second-link {
      margin-top: 6px;
    }

    .lien {
      color: #315e67;
      font-weight: 750;
      text-decoration: none;
    }

    .lien:hover {
      color: #214b39;
      text-decoration: underline;
    }

    @media (max-width: 700px) {
      form {
        grid-template-columns: 1fr;
        gap: 14px;
      }

      .profession-field,
      .row,
      .bio-field,
      .documents-field,
      .check,
      .alert,
      .btn-primary {
        grid-column: 1;
      }

      .documents-grid {
        grid-template-columns: 1fr;
      }
    }

    @media (max-width: 420px) {
      .row {
        grid-template-columns: 1fr;
      }

      .choices {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class ProRegister {
  private auth = inject(AuthService);
  private router = inject(Router);

  type = signal<'NUTRITIONNISTE' | 'COACH'>('NUTRITIONNISTE');
  nom = ''; prenom = ''; email = ''; specialite = ''; bio = '';
  password = ''; confirmPassword = '';
  cgu = false;

  cv = signal<File | null>(null);
  lettre = signal<File | null>(null);
  diplome = signal<File | null>(null);

  error = signal('');
  loading = signal(false);

  pick(kind: 'cv' | 'lettre' | 'diplome', ev: Event) {
    const file = (ev.target as HTMLInputElement).files?.[0] ?? null;
    this.error.set('');
    if (file) {
      if (file.type !== 'application/pdf') { this.error.set('Le fichier doit être un PDF.'); return; }
      if (file.size > 5 * 1024 * 1024) { this.error.set('Fichier trop volumineux (5 Mo maximum).'); return; }
    }
    this[kind].set(file);
  }

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
    const fd = new FormData();
    fd.append('type', this.type());
    fd.append('nom', this.nom);
    fd.append('prenom', this.prenom);
    fd.append('email', `${this.email.trim()}@gmail.com`);
    fd.append('specialite', this.specialite);
    fd.append('bio', this.bio);
    fd.append('password', this.password);
    fd.append('confirmPassword', this.confirmPassword);
    fd.append('cv', this.cv()!);
    fd.append('lettre', this.lettre()!);
    fd.append('diplome', this.diplome()!);

    this.loading.set(true);
    this.auth.registerPro(fd).subscribe({
      next: () => this.router.navigate(['/pro/pending']),
      error: (e) => {
        this.loading.set(false);
        this.error.set(errMsg(e, "Erreur lors de l'envoi du dossier"));
      },
    });
  }

  private isPasswordValid(): boolean {
    return this.password.length >= 8 && /[A-Za-z]/.test(this.password) && /[0-9]/.test(this.password);
  }
}
