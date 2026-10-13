import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, errMsg } from '../../services/auth';
import { AuthShell } from '../../shared/auth-shell';

@Component({
  selector: 'app-pro-register',
  imports: [FormsModule, RouterLink, AuthShell],
  template: `
    <app-auth-shell>
      <h1 class="titre">Espace professionnels</h1>
      <p class="sous">Déposez votre dossier : un administrateur le vérifiera avant d'activer votre compte.</p>

      <form (ngSubmit)="submit()" #f="ngForm">
        <div class="field"><span>Je suis</span>
          <div class="choices">
            <button type="button" class="choice" [class.active]="type() === 'NUTRITIONNISTE'"
                    (click)="type.set('NUTRITIONNISTE')">Nutritionniste</button>
            <button type="button" class="choice" [class.active]="type() === 'COACH'"
                    (click)="type.set('COACH')">Coach sportif</button>
          </div>
        </div>

        <div class="row">
          <label class="field"><span>Nom</span><input name="nom" [(ngModel)]="nom" required></label>
          <label class="field"><span>Prénom</span><input name="prenom" [(ngModel)]="prenom" required></label>
        </div>
        <label class="field"><span>Email</span>
          <input name="email" type="email" [(ngModel)]="email" required>
        </label>
        <label class="field"><span>Spécialité</span>
          <input name="specialite" [(ngModel)]="specialite" required
                 [placeholder]="type() === 'COACH' ? 'Ex. : musculation, remise en forme' : 'Ex. : nutrition sportive, diabète'">
        </label>
        <label class="field"><span>Présentation (facultatif)</span>
          <textarea name="bio" rows="3" maxlength="1000" [(ngModel)]="bio"></textarea>
        </label>

        <div class="field"><span>Documents obligatoires (PDF, 5 Mo max)</span>
          <label class="file" [class.done]="cv()">
            <input type="file" accept="application/pdf" (change)="pick('cv', $event)">
            <span class="file-mark" aria-hidden="true"><svg viewBox="0 0 20 22" fill="none"><path d="M4 1.5h8l4 4V20H4z" stroke="currentColor" stroke-width="1.5"/><path d="M12 1.5v4h4M7 11h6M7 14h6" stroke="currentColor" stroke-width="1.5"/></svg></span>
            <span><b>Curriculum vitae</b><small>{{ cv()?.name || 'Choisir un fichier PDF' }}</small></span>
          </label>
          <label class="file" [class.done]="lettre()">
            <input type="file" accept="application/pdf" (change)="pick('lettre', $event)">
            <span class="file-mark" aria-hidden="true"><svg viewBox="0 0 20 22" fill="none"><path d="M4 1.5h8l4 4V20H4z" stroke="currentColor" stroke-width="1.5"/><path d="M12 1.5v4h4M7 11h6M7 14h4" stroke="currentColor" stroke-width="1.5"/></svg></span>
            <span><b>Lettre de motivation</b><small>{{ lettre()?.name || 'Choisir un fichier PDF' }}</small></span>
          </label>
          <label class="file" [class.done]="diplome()">
            <input type="file" accept="application/pdf" (change)="pick('diplome', $event)">
            <span class="file-mark" aria-hidden="true"><svg viewBox="0 0 20 22" fill="none"><path d="M4 1.5h8l4 4V20H4z" stroke="currentColor" stroke-width="1.5"/><path d="M12 1.5v4h4M7 11h6M7 14h6" stroke="currentColor" stroke-width="1.5"/></svg></span>
            <span><b>Diplôme</b><small>{{ diplome()?.name || 'Choisir un fichier PDF' }}</small></span>
          </label>
        </div>

        <label class="field"><span>Mot de passe</span>
          <input name="password" type="password" [(ngModel)]="password" required>
          <small class="hint">8 caractères minimum, avec lettres et chiffres</small>
        </label>
        <label class="field"><span>Confirmer le mot de passe</span>
          <input name="confirm" type="password" [(ngModel)]="confirmPassword" required>
        </label>

        <label class="check">
          <input type="checkbox" name="cgu" [(ngModel)]="cgu">
          <span>Je certifie l'exactitude de mes documents et j'accepte les CGU</span>
        </label>

        @if (error()) { <div class="alert alert-err">{{ error() }}</div> }

        <button class="btn btn-primary" type="submit"
                [disabled]="f.invalid || !cgu || !cv() || !lettre() || !diplome() || loading()">
          {{ loading() ? 'Envoi du dossier...' : 'Envoyer mon dossier' }}
        </button>
      </form>

      <div class="centre">Déjà validé ? <a class="lien" routerLink="/login">Se connecter</a></div>
      <div class="centre" style="margin-top:6px">Vous êtes client ? <a class="lien" routerLink="/register">Créer un compte client</a></div>
    </app-auth-shell>
  `,
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
    if (this.password !== this.confirmPassword) {
      this.error.set('Les mots de passe ne correspondent pas');
      return;
    }
    const fd = new FormData();
    fd.append('type', this.type());
    fd.append('nom', this.nom);
    fd.append('prenom', this.prenom);
    fd.append('email', this.email);
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
}
