import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthShell } from '../../shared/auth-shell';

@Component({
  selector: 'app-pro-pending',
  imports: [AuthShell, RouterLink],
  template: `
    <app-auth-shell>
      <div class="ok" aria-hidden="true"></div>
      <h1 class="titre" style="text-align:center">Dossier envoyé !</h1>
      <p class="sous" style="text-align:center">
        Votre CV, votre lettre de motivation et votre diplôme sont en cours d'examen par
        l'équipe SA7TI. Vous pourrez vous connecter dès que votre dossier sera approuvé.
      </p>
      <div class="alert alert-info">
        Si votre dossier est refusé, le motif s'affichera lors de votre tentative de connexion.
      </div>
      <a class="btn btn-primary" routerLink="/login" style="display:block;text-align:center;text-decoration:none">
        Retour à la connexion
      </a>
    </app-auth-shell>
  `,
  styles: [`
    .ok {
      width: 84px; height: 84px; margin: 0 auto 14px; border-radius: 50%;
      background: #e6f4e8; color: #1f5a3e; display: grid; place-items: center;
    }
    .ok::before {
      content: ''; width: 22px; height: 12px; margin-top: -5px;
      border-left: 3px solid currentColor; border-bottom: 3px solid currentColor;
      transform: rotate(-45deg);
    }
  `],
})
export class ProPending {}
