
import { Component } from '@angular/core';

@Component({
  selector: 'app-auth-shell',
  standalone: true,
  imports: [],
  template: `
    <div class="auth-background">
      <main class="auth-content">
        <div class="auth-panel">
          <ng-content></ng-content>
        </div>
      </main>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      min-height: 100vh;
      width: 100%;
      color: #153528;
    }

    * {
      box-sizing: border-box;
    }

    .auth-background {
      min-height: 100vh;
      width: 100%;
      background-color: #faf9f5;
      background-image: url('/assets/images/sa7ti-nutrition.png');
      background-position: center center;
      background-size: cover;
      background-repeat: no-repeat;
    }

    .auth-content {
      display: flex;
      min-height: 100vh;
      width: 100%;
      align-items: center;
      justify-content: center;
      padding: 40px 24px;
    }

    .auth-panel {
  width: 100%;
  max-width: 720px;
  padding: clamp(42px, 5vw, 65px);

  border: 1px solid rgba(255, 255, 255, .35);
  border-radius: 26px;

  /* Formulaire transparent */
  background: rgba(250, 249, 245, .22);

  /* Effet verre */
  box-shadow:
    0 20px 60px rgba(21, 53, 40, .12),
    inset 0 1px 1px rgba(255, 255, 255, .25);

  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}

    @media (max-width: 600px) {
      .auth-content {
        padding: 20px 14px;
      }

      .auth-panel {
        max-width: 100%;
        padding: 30px 22px;
        border-radius: 20px;
      }
    }
  `]
})
export class AuthShell {}
