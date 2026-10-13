import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthShell } from '../../shared/auth-shell';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [AuthShell, RouterLink],
  template: `
    <app-auth-shell class="landing">
      <section class="welcome-content">
        <h1 class="titre">Mar7ba bik</h1>
        <p class="sous">Choisissez votre espace pour commencer.</p>

        <div class="roles">
          <a class="role" routerLink="/register">
            <span class="role-index">01</span>
            <span class="role-content"><b>Je suis un client</b>
              <small>Suivez vos repas, votre activité et votre progression.</small></span>
            <span class="arrow" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M4 12h15m-6-6 6 6-6 6" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/></svg></span>
          </a>

          <a class="role pro" routerLink="/pro/register">
            <span class="role-index">02</span>
            <span class="role-content"><b>Espace professionnel</b>
              <small>Nutritionniste ou coach sportif : rejoignez SA7TI.</small></span>
            <span class="arrow" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M4 12h15m-6-6 6 6-6 6" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/></svg></span>
          </a>
        </div>

        <div class="centre">Déjà un compte ? <a class="lien" routerLink="/login">Se connecter</a></div>
      </section>
    </app-auth-shell>
  `,
  styles: [`
    .welcome-content { text-align: center; }
    .titre { color: #fff; text-shadow: 0 2px 8px rgba(34,78,42,.26); font-size: 25px; }
    .sous { margin: 5px 0 15px; color: rgba(255,255,255,.94); }
    .roles { display: flex; justify-content: center; gap: 10px; }
    .role {
      display: flex; gap: 10px; align-items: center; flex: 1; padding: 12px 14px;
      border: 2px solid rgba(255,255,255,.75); border-radius: 13px; background: rgba(255,254,250,.96);
      text-align: left; text-decoration: none; color: #23332a; transition: .15s;
    }
    .role:hover { border-color: #ffe17a; transform: translateY(-2px); box-shadow: 0 8px 20px rgba(31,73,42,.2); }
    .role.pro { background: #ffe17a; border-color: #ffe17a; color: #23332a; }
    .role-index { display: grid; place-items: center; width: 32px; height: 32px; flex: 0 0 32px; border-radius: 50%; background: #e5efdc; color: #365d43; font-size: 11px; font-weight: 700; }
    .role.pro .role-index { background: #fff3bd; color: #8c5632; }
    .role-content { flex: 1; min-width: 0; }
    b { display: block; font-size: 13px; font-weight: 600; margin-bottom: 3px; }
    small { color: #647269; font-size: 10px; line-height: 1.4; display: block; }
    .arrow { width: 18px; height: 18px; color: #648d62; flex: 0 0 18px; }
    .arrow svg { display: block; width: 100%; height: 100%; }
    .role.pro .arrow { color: #8c5632; }
    .centre { color: #fff; margin-top: 10px; font-size: 12px; }
    .lien { color: #fff4c8; }
    @media (max-width: 680px) {
      .titre { font-size: 22px; }
      .sous { margin-bottom: 11px; font-size: 12px; }
      .roles { flex-direction: column; gap: 8px; }
      .role { padding: 10px 12px; }
      .centre { margin-top: 7px; }
    }
  `],
})
export class Welcome {}
