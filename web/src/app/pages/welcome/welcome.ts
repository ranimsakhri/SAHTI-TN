import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="home-page">
      <header class="home-header">
        <a class="home-brand" routerLink="/" aria-label="SA7TI, accueil">
          <img class="brand-wordmark" src="/assets/sa7ti-logo-original.png" alt="" />
        </a>
        <nav class="home-nav" aria-label="Navigation principale">
          <a href="#approche">La méthode</a>
          <a href="#nutrition">Nutrition &amp; forme</a>
          <a href="#cap-2026">Nos engagements</a>
          <a href="#zitouna">Zitouna IA</a>
          <a class="nav-login" routerLink="/login">Se connecter <span aria-hidden="true">↗</span></a>
        </nav>
      </header>

      <main>
        <section class="home-hero" aria-labelledby="home-title">
          <div class="hero-copy">
            <p class="home-eyebrow"><span></span> VOTRE BIEN-ÊTRE, AU QUOTIDIEN</p>
            <h1 id="home-title">Les bons choix.<br /><span>À votre rythme.</span></h1>
            <p class="hero-description">
              Votre espace web et mobile pour mieux manger, bouger davantage et suivre des objectifs
              adaptés à votre quotidien. Avec Zitouna, découvrez SA7TI pas à pas.
            </p>
            <div class="hero-actions">
              <a class="button button-yellow" href="#approche">Découvrir SA7TI <span aria-hidden="true">↓</span></a>
              <a class="hero-login" routerLink="/login">J’ai déjà un compte <span aria-hidden="true">→</span></a>
            </div>
            <div class="hero-note"><span class="note-check" aria-hidden="true">✓</span> Nutrition et activité physique réunies dans un seul espace.</div>
          </div>

          <div class="hero-visual" aria-label="L’illustration de la guide SA7TI, avec des produits frais et une assiette équilibrée">
            <div class="visual-orbit orbit-one"></div>
            <div class="visual-orbit orbit-two"></div>
            <img class="food-doodle doodle-tomato" src="/assets/sa7ti-tomatoes.png" alt="" />
            <img class="food-doodle doodle-citrus" src="/assets/sa7ti-lemons.png" alt="" />
            <img class="brand-girl" src="/assets/sa7ti-girl.png" alt="Zitouna, la guide SA7TI, avec une assiette de cuisine tunisienne" />
            <div class="mascot-caption"><span class="mascot-caption-dot"></span><span><strong>Zitouna</strong><small>Votre guide nutrition &amp; fitness</small></span><span class="mascot-caption-spark">✳</span></div>
          </div>
          <a class="scroll-cue" href="#approche"><span></span> DÉFILER POUR DÉCOUVRIR</a>
        </section>

        <section class="achievement-section" id="cap-2026" aria-labelledby="achievement-title">
          <div class="achievement-heading">
            <p class="section-eyebrow">CAP 2026 · NOS ENGAGEMENTS</p>
            <h2 id="achievement-title">Des progrès qui se construisent, un jour à la fois.</h2>
          </div>
          <div class="achievement-grid">
            <article><span class="achievement-number">01</span><div><h3>Mieux manger</h3><p>Des repères accessibles pour des repas variés et équilibrés.</p></div><span class="achievement-line"></span></article>
            <article><span class="achievement-number">02</span><div><h3>Plus de mouvement</h3><p>Des objectifs d’activité adaptés à votre rythme et votre quotidien.</p></div><span class="achievement-line"></span></article>
            <article><span class="achievement-number">03</span><div><h3>Un suivi qui vous ressemble</h3><p>Votre parcours, vos étapes et un accompagnement si vous le souhaitez.</p></div><span class="achievement-line"></span></article>
          </div>
          <p class="achievement-footnote">Notre cap 2026 : rendre le bien-être plus accessible et durable.</p>
        </section>

        <section class="approach-section" id="approche" aria-labelledby="approach-title">
          <div class="section-intro">
            <p class="section-eyebrow">UNE APPROCHE QUI VOUS RESSEMBLE</p>
            <h2 id="approach-title">Le bien-être, sans formule toute faite.</h2>
            <p>SA7TI vous aide à construire des habitudes réalistes, avec des outils accessibles sur le web et sur mobile.</p>
          </div>
          <div class="approach-grid">
            <article class="approach-card nutrition-card" id="nutrition">
              <img class="card-illustration" src="/assets/sa7ti-chef.png" alt="" aria-hidden="true" />
              <span class="card-number">01 / NUTRITION</span>
              <span class="card-symbol" aria-hidden="true">
                <svg viewBox="0 0 48 48"><circle cx="24" cy="24" r="17" fill="none" stroke="currentColor" stroke-width="1.7"/><path d="M24 7v34M7 24h34" stroke="currentColor" stroke-width="1.5"/><path d="M24 7a17 17 0 0 0 0 34Z" fill="currentColor" opacity=".14"/><circle cx="24" cy="24" r="5" fill="#ffe17a"/></svg>
              </span>
              <h3>Mieux composer vos repas</h3>
              <p>Explorez des idées inspirées de la cuisine tunisienne et des repères simples pour varier les aliments et équilibrer votre assiette.</p>
              <span class="card-tag">Des idées, pas des interdits</span>
            </article>
            <article class="approach-card movement-card">
              <img class="card-illustration" src="/assets/sa7ti-coach.png" alt="" aria-hidden="true" />
              <span class="card-number">02 / ACTIVITÉ</span>
              <span class="card-symbol" aria-hidden="true">
                <svg viewBox="0 0 48 48"><path d="M8 33c5-12 10-18 17-18 6 0 9 5 15 2M8 38h32" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/><circle cx="25" cy="12" r="4" fill="currentColor"/><path d="m20 21-7 8 8 4 5-9 7 6 7-5" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
              </span>
              <h3>Bouger à votre façon</h3>
              <p>Faites une place au mouvement dans votre quotidien et avancez vers des objectifs adaptés à votre rythme et à vos possibilités.</p>
              <span class="card-tag">Chaque pas compte</span>
            </article>
            <article class="approach-card coaching-card">
              <img class="card-illustration" src="/assets/sa7ti-eater.png" alt="" aria-hidden="true" />
              <span class="card-number">03 / ACCOMPAGNEMENT</span>
              <span class="card-symbol" aria-hidden="true">
                <svg viewBox="0 0 48 48"><path d="M8 11h32v22H23l-9 7v-7H8z" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linejoin="round"/><path d="M16 22h16m-16 6h10" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/><circle cx="36" cy="13" r="5" fill="#ffe17a"/></svg>
              </span>
              <h3>Avancer bien entouré</h3>
              <p>Si vous le souhaitez, adressez une demande à un·e nutritionniste ou à un·e coach sportif pour un suivi plus personnalisé.</p>
              <span class="card-tag">Avec des professionnels</span>
            </article>
          </div>
        </section>

        <section class="zitouna-section" id="zitouna" aria-labelledby="zitouna-title">
          <div class="zitouna-orbit" aria-hidden="true"><span>زيتونة</span></div>
          <div class="zitouna-copy">
            <p class="section-eyebrow">VOTRE GUIDE NUMÉRIQUE</p>
            <h2 id="zitouna-title">Voici Zitouna.<br /><span>Votre alliée pour explorer SA7TI.</span></h2>
            <p>Zitouna vous accompagne dans la découverte des espaces de l’application. Le scan de repas est présenté comme une fonctionnalité à connecter : aucune analyse nutritionnelle n’est simulée sur cette page.</p>
            <div class="zitouna-actions">
              <button class="button button-yellow" type="button" (click)="zitounaOpen.set(!zitounaOpen())">
                {{ zitounaOpen() ? 'Fermer le guide' : 'Rencontrer Zitouna' }} <span aria-hidden="true">→</span>
              </button>
              <a class="zitouna-secondary" href="#nutrition">Voir les repères nutritionnels</a>
            </div>
          </div>
          <aside class="zitouna-preview" aria-label="Exemple de conseils de Zitouna">
            <div class="preview-head"><span class="zitouna-avatar" aria-hidden="true"><img src="/assets/sa7ti-zitouna-avatar.png" alt="" /></span><span><strong>Zitouna</strong><small>Guide de découverte SA7TI</small></span><i>Guide</i></div>
            <p class="preview-message">Salem ! Je peux vous orienter vers les objectifs, les conseils nutrition et l’espace d’accompagnement.</p>
            <div class="preview-options"><a href="#approche">Découvrir la méthode <span>→</span></a><a href="#nutrition">Explorer la nutrition <span>→</span></a><a href="#acces">Trouver mon espace <span>→</span></a></div>
            <div class="preview-foot"><span></span> Guide interactif · Scan IA à connecter</div>
          </aside>
        </section>

        <section class="promise-section" aria-labelledby="promise-title">
          <div class="promise-copy">
            <p class="section-eyebrow">FAIT POUR LA VRAIE VIE</p>
            <h2 id="promise-title">Pas besoin de tout changer.<br /><span>On commence par un pas.</span></h2>
            <p>Un repas, une marche, un nouvel objectif. SA7TI rassemble nutrition et activité physique dans un espace clair pour vous aider à suivre votre parcours, sans pression.</p>
            <a class="text-link" href="#acces">Choisir mon espace <span aria-hidden="true">→</span></a>
          </div>
          <div class="promise-list">
            <div><span>01</span><p><strong>À votre rythme</strong><small>Des objectifs personnels, évolutifs et réalistes.</small></p><span class="list-check" aria-hidden="true">✓</span></div>
            <div><span>02</span><p><strong>Inspiré de votre quotidien</strong><small>Des repères accessibles et des saveurs familières.</small></p><span class="list-check" aria-hidden="true">✓</span></div>
            <div><span>03</span><p><strong>Sur vos écrans</strong><small>Retrouvez votre espace sur le web et le mobile.</small></p><span class="list-check" aria-hidden="true">✓</span></div>
          </div>
        </section>

        <section class="mobile-section" id="mobile" aria-labelledby="mobile-title">
          <div class="mobile-copy">
            <p class="section-eyebrow">SA7TI VOUS ACCOMPAGNE PARTOUT</p>
            <h2 id="mobile-title">Votre équilibre,<br /><span>dans votre poche.</span></h2>
            <p>Consultez votre espace SA7TI depuis votre téléphone : gardez vos objectifs nutrition et activité à portée de main, à la maison comme en déplacement.</p>
            <a class="button button-dark" routerLink="/register">Découvrir SA7TI sur mobile <span aria-hidden="true">→</span></a>
            <small class="mobile-availability">Version web adaptée au mobile · Liens de téléchargement natif à ajouter lors de la publication.</small>
          </div>
          <div class="phone-scene" aria-label="Aperçu de SA7TI sur téléphone">
            <div class="phone-shadow"></div>
            <div class="phone-device">
              <div class="phone-notch"></div>
              <div class="phone-screen">
                <div class="phone-top"><span>SA7TI</span><span class="phone-avatar">S</span></div>
                <div class="phone-greeting">BONJOUR</div>
                <h3>On avance<br />à votre rythme.</h3>
                <div class="phone-goal"><span>VOTRE OBJECTIF</span><strong>Un peu plus d’équilibre</strong><div><i></i></div></div>
                <div class="phone-pair"><span><i class="phone-nutrition">✳</i><b>Nutrition</b><small>Des choix variés</small></span><span><i class="phone-fitness">↗</i><b>Mouvement</b><small>À votre rythme</small></span></div>
                <div class="phone-tab"><span>⌂<small>Accueil</small></span><span>◷<small>Suivi</small></span><span>♡<small>Profil</small></span></div>
              </div>
            </div>
            <span class="phone-note"><i></i> Un seul espace, où que vous soyez</span>
          </div>
        </section>

        <section class="access-section" id="acces" aria-labelledby="access-title">
          <div class="access-heading">
            <p class="section-eyebrow">BIENVENUE DANS VOTRE ESPACE</p>
            <h2 id="access-title">Comment souhaitez-vous continuer ?</h2>
            <p>Connectez-vous ou créez l’espace qui correspond à votre parcours.</p>
          </div>
          <div class="access-grid">
            <article class="access-card client-access">
              <span class="access-kicker">POUR VOTRE PARCOURS</span>
              <h3>Espace client</h3>
              <p>Retrouvez votre profil, vos objectifs et les ressources pour prendre soin de vous au quotidien.</p>
              <div class="access-actions">
                <a class="button button-dark" routerLink="/login">Connexion client <span aria-hidden="true">→</span></a>
                <a class="access-secondary" routerLink="/register">Créer un compte</a>
              </div>
            </article>
            <article class="access-card pro-access">
              <span class="access-kicker">POUR LES PROFESSIONNELS</span>
              <h3>Nutritionniste ou coach ?</h3>
              <p>Rejoignez SA7TI et recevez les demandes des personnes qui recherchent un accompagnement.</p>
              <div class="access-actions">
                <a class="button button-yellow" routerLink="/login">Connexion pro <span aria-hidden="true">→</span></a>
                <a class="access-secondary" routerLink="/pro/register">Rejoindre SA7TI</a>
              </div>
            </article>
          </div>
          <p class="access-note">Déjà membre ? <a routerLink="/login">Se connecter à mon compte</a></p>
        </section>
      </main>

      <footer class="home-footer">
        <a class="footer-brand" routerLink="/" aria-label="SA7TI, accueil">
          <img class="footer-wordmark" src="/assets/sa7ti-logo-original.png" alt="" />
        </a>
        <p>Nutrition et mouvement, à votre rythme.</p>
        <a href="#home-title">Retour en haut ↑</a>
      </footer>

      @if (zitounaOpen()) {
        <aside class="zitouna-panel" aria-label="Guide interactif Zitouna">
          <div class="zitouna-panel-head"><span class="zitouna-avatar" aria-hidden="true"><img src="/assets/sa7ti-zitouna-avatar.png" alt="" /></span><span><strong>Zitouna</strong><small>Guide de découverte</small></span><button type="button" aria-label="Fermer le guide Zitouna" (click)="zitounaOpen.set(false)">×</button></div>
          <p>Bienvenue ! Que souhaitez-vous découvrir ?</p>
          <a href="#nutrition" (click)="zitounaOpen.set(false)">Les repères nutritionnels <span>→</span></a>
          <a href="#cap-2026" (click)="zitounaOpen.set(false)">Les objectifs SA7TI 2026 <span>→</span></a>
          <a href="#acces" (click)="zitounaOpen.set(false)">Choisir mon espace <span>→</span></a>
          <small class="assistant-disclaimer">Le scan photo nécessite une connexion au service d’analyse IA.</small>
        </aside>
      }
      <button class="zitouna-launcher" type="button" [attr.aria-expanded]="zitounaOpen()" (click)="zitounaOpen.set(!zitounaOpen())">
        <span class="launcher-icon" aria-hidden="true"><img src="/assets/sa7ti-zitouna-avatar.png" alt="" /></span><span><strong>Besoin d’aide ?</strong><small>Parler à Zitouna</small></span><span class="launcher-arrow" aria-hidden="true">{{ zitounaOpen() ? '×' : '↗' }}</span>
      </button>
    </div>
  `,
})
export class Welcome {
  readonly zitounaOpen = signal(false);
}
