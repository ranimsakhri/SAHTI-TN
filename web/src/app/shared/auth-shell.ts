import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-auth-shell',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="shell">
      <aside class="brand" aria-label="SA7TI, Manger et Bouger">
        <div class="decoration plate" aria-hidden="true">
          <svg viewBox="0 0 220 220">
            <circle cx="110" cy="110" r="105" fill="#fffefa"/>
            <circle cx="110" cy="110" r="91" fill="none" stroke="#c83c32" stroke-width="5"/>
            <circle cx="110" cy="110" r="80" fill="none" stroke="#c83c32" stroke-width="2"/>
            <path d="M52 115c18-41 38-51 58-31 19-31 48-25 58 10 22 21 9 58-17 68-22 25-65 12-73-11-28 2-42-14-26-36z" fill="#687d3d"/>
            <circle cx="108" cy="108" r="31" fill="#c93e32" stroke="#f5d781" stroke-width="6"/>
            <g fill="#f5d781">
              <ellipse cx="99" cy="97" rx="2" ry="4"/><ellipse cx="117" cy="102" rx="2" ry="4"/>
              <ellipse cx="105" cy="117" rx="2" ry="4"/><ellipse cx="122" cy="117" rx="2" ry="4"/>
            </g>
            <g fill="#718447">
              <circle cx="156" cy="83" r="17"/><circle cx="177" cy="100" r="14"/><circle cx="155" cy="111" r="15"/>
            </g>
            <g fill="none" stroke="#44592e" stroke-width="2">
              <circle cx="156" cy="83" r="10"/><circle cx="177" cy="100" r="8"/><circle cx="155" cy="111" r="9"/>
              <path d="M156 74v18m-9-9h18m12 9v16m-8-8h16m-24 5v18m-9-9h18"/>
            </g>
          </svg>
        </div>
        <div class="decoration melon" aria-hidden="true">
          <svg viewBox="0 0 190 150">
            <path d="M17 16c-8 40 16 89 68 118 35-32 59-70 77-111C116-4 55-4 17 16z" fill="#ee9940"/>
            <path d="M27 21c-6 34 15 75 59 100 30-28 51-61 67-96C116 5 61 5 27 21z" fill="#f6cf60"/>
            <path d="M43 28c-4 27 12 58 44 77 23-22 39-47 52-73-33-17-68-17-96-4z" fill="#de4939"/>
            <g fill="#302b25">
              <ellipse cx="70" cy="51" rx="2" ry="5" transform="rotate(-24 70 51)"/>
              <ellipse cx="93" cy="66" rx="2" ry="5" transform="rotate(-18 93 66)"/>
              <ellipse cx="119" cy="40" rx="2" ry="5" transform="rotate(15 119 40)"/>
              <ellipse cx="109" cy="91" rx="2" ry="5" transform="rotate(18 109 91)"/>
              <ellipse cx="76" cy="86" rx="2" ry="5" transform="rotate(-18 76 86)"/>
            </g>
          </svg>
        </div>
        <div class="decoration citrus" aria-hidden="true">
          <svg viewBox="0 0 160 90">
            <path d="M15 62c8-31 25-48 52-50 3 29-11 47-52 50z" fill="#f4a331"/>
            <path d="M60 64c8-31 25-48 52-50 3 29-11 47-52 50z" fill="#f8ba45"/>
            <path d="M91 67c8-30 23-44 47-46 2 27-10 43-47 46z" fill="#ee9630"/>
          </svg>
        </div>
        <div class="decoration olive-branch" aria-hidden="true">
          <svg viewBox="0 0 160 180">
            <path d="M20 168C59 123 84 79 134 13" fill="none" stroke="#315b41" stroke-width="7" stroke-linecap="round"/>
            <g fill="#5b7439">
              <ellipse cx="44" cy="130" rx="12" ry="27" transform="rotate(-48 44 130)"/>
              <ellipse cx="57" cy="144" rx="11" ry="25" transform="rotate(42 57 144)"/>
              <ellipse cx="66" cy="100" rx="12" ry="26" transform="rotate(-48 66 100)"/>
              <ellipse cx="83" cy="112" rx="11" ry="25" transform="rotate(42 83 112)"/>
              <ellipse cx="91" cy="69" rx="11" ry="25" transform="rotate(-48 91 69)"/>
              <ellipse cx="108" cy="79" rx="10" ry="23" transform="rotate(42 108 79)"/>
              <ellipse cx="116" cy="39" rx="10" ry="22" transform="rotate(-48 116 39)"/>
            </g>
            <g fill="#779343">
              <ellipse cx="37" cy="158" rx="7" ry="11" transform="rotate(-48 37 158)"/>
              <ellipse cx="76" cy="127" rx="7" ry="11" transform="rotate(42 76 127)"/>
              <ellipse cx="101" cy="91" rx="7" ry="11" transform="rotate(-48 101 91)"/>
              <ellipse cx="127" cy="56" rx="7" ry="11" transform="rotate(42 127 56)"/>
            </g>
          </svg>
        </div>
        <div class="decoration tomato" aria-hidden="true">
          <svg viewBox="0 0 150 135">
            <path d="M76 28c-31-15-66 5-65 44 1 36 31 57 65 57 35 0 63-21 64-57 1-37-32-59-64-44z" fill="#df4638"/>
            <path d="M74 30c-5-15-1-24 8-29 7 12 8 20 3 29 12-10 22-11 31-4-5 12-14 17-28 15 8 7 11 14 7 22-13-4-20-11-21-23-4 11-11 17-21 17-1-12 4-21 17-27-15 3-25-1-29-11 10-7 21-6 33 2z" fill="#3d653d"/>
            <g fill="#f5b83f">
              <ellipse cx="51" cy="63" rx="3" ry="7" transform="rotate(-32 51 63)"/>
              <ellipse cx="89" cy="57" rx="3" ry="7" transform="rotate(28 89 57)"/>
              <ellipse cx="107" cy="83" rx="3" ry="7" transform="rotate(35 107 83)"/>
              <ellipse cx="74" cy="96" rx="3" ry="7" transform="rotate(-8 74 96)"/>
              <ellipse cx="43" cy="88" rx="3" ry="7" transform="rotate(-35 43 88)"/>
            </g>
          </svg>
        </div>
        <div class="decoration wave wave-top" aria-hidden="true">
          <svg viewBox="0 0 130 70"><path d="M5 48 23 18l18 30 18-30 18 30 18-30 18 30" fill="none" stroke="#fffefa" stroke-width="8" stroke-linecap="round" stroke-linejoin="round"/></svg>
        </div>
        <div class="decoration wave wave-bottom" aria-hidden="true">
          <svg viewBox="0 0 130 70"><path d="M5 48 23 18l18 30 18-30 18 30 18-30 18 30" fill="none" stroke="#fffefa" stroke-width="8" stroke-linecap="round" stroke-linejoin="round"/></svg>
        </div>
        <div class="decoration spoon" aria-hidden="true">
          <svg viewBox="0 0 210 150">
            <ellipse cx="50" cy="45" rx="39" ry="24" transform="rotate(-30 50 45)" fill="#fff"/>
            <path d="m80 61 105 72" stroke="#fff" stroke-width="13" stroke-linecap="round"/>
            <path d="m89 68 98 67" stroke="#d9dde3" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </div>
        <div class="decoration cook" aria-hidden="true">
          <svg viewBox="0 0 220 310">
            <path d="M65 105c-13-65 9-94 66-91 53 3 73 38 61 100l-5 75H65z" fill="#102c26"/>
            <path d="M91 52c14-21 51-24 72-3 15 15 16 45 6 68l-14 28H91L77 104c-9-23-3-42 14-52z" fill="#f5a277"/>
            <path d="M83 71c10 4 17-5 20-18 22 15 53 11 73-4 13 15 13 28 10 46 21-42 4-76-24-87-35-14-75-3-88 30-6 15-3 34 3 48z" fill="#102c26"/>
            <path d="M72 157c-26 17-40 40-45 81l-9 68h187l-12-80c-6-36-21-59-49-70l-13 19H99z" fill="#dc493c"/>
            <path d="M90 151c8 20 30 28 46 0" fill="none" stroke="#db795c" stroke-width="5" stroke-linecap="round"/>
            <path d="M65 197 31 222m113-37 37 32" fill="none" stroke="#f5a277" stroke-width="17" stroke-linecap="round"/>
            <path d="M48 209c14-13 27-18 41-16m48-17c16 2 28 8 41 22" fill="none" stroke="#f5d15d" stroke-width="5"/>
            <path d="M66 256c11-31 29-44 61-43 31 1 49 16 58 43-29 21-91 22-119 0z" fill="#c93632"/>
            <path d="M67 252c24 11 88 12 116 0" fill="none" stroke="#fff4e1" stroke-width="7"/>
            <g fill="#e4543e">
              <circle cx="94" cy="230" r="10"/><circle cx="122" cy="222" r="11"/><circle cx="151" cy="231" r="10"/>
            </g>
            <g fill="#f4c85c">
              <circle cx="103" cy="225" r="2"/><circle cx="127" cy="219" r="2"/><circle cx="148" cy="226" r="2"/>
            </g>
            <path d="M101 90h8m31 0h8" stroke="#17332b" stroke-width="5" stroke-linecap="round"/>
            <path d="M113 116c8 6 17 6 24-1" fill="none" stroke="#b85046" stroke-width="3" stroke-linecap="round"/>
          </svg>
        </div>

        <div class="brand-inner">
          <a class="logo" routerLink="/" aria-label="SA7TI Tunisie, accueil">
            <span class="logo-country">TN</span>
            <span class="logo-name">SA7TI</span>
            <span class="fez" aria-hidden="true">
              <svg viewBox="0 0 120 82" role="presentation">
                <path d="M63 20c8-15 20-20 31-12 8 6 12 14 18 23l6 8-8 6-12-15c-8-10-17-12-26-4l-9 10-8-6z" fill="#171916"/>
                <path d="M16 30c4-12 22-22 47-24 23-2 39 4 43 15l-6 35c-2 12-12 18-34 20l-38-2C14 72 7 66 8 54z" fill="#ba1f35"/>
                <path d="M16 30c5 10 23 15 45 14 19 0 36-5 45-13" fill="none" stroke="#d44451" stroke-width="3" opacity=".7"/>
                <path d="M91 17c13 5 18 18 21 31 3 14 0 24-7 29-8 6-14-1-10-8 4-7 8-17 4-26-3-8-9-13-18-16z" fill="#171916"/>
                <path d="M21 62c16 7 46 11 74 2" fill="none" stroke="#8d1729" stroke-width="2" opacity=".75"/>
              </svg>
            </span>
          </a>
          <div class="tagline">Manger et Bouger</div>
        </div>
      </aside>

      <main class="content">
        <div class="panel"><ng-content /></div>
      </main>
    </div>
  `,
  styles: [`
    :host { display: block; min-height: 100vh; }
    .shell {
      position: relative; display: grid; grid-template-columns: 1fr minmax(390px, 43%);
      min-height: 100vh; overflow: hidden;
      background-color: #70a575;
      background-image: repeating-conic-gradient(rgba(255,255,255,.055) 0% 25%, rgba(31,89,54,.035) 0% 50%);
      background-size: 64px 64px;
    }
    .brand { position: relative; z-index: 1; display: grid; place-items: center; }
    .brand-inner { position: relative; text-align: center; padding: 70px 35px 40px; }
    .logo {
      position: relative; display: inline-block; color: #fff; text-decoration: none;
      font-family: 'Lilita One', 'Poppins', sans-serif; font-weight: 400; font-size: clamp(105px, 28vw, 300px);
      line-height: .82; letter-spacing: -1px; transform: rotate(-2deg);
    }
    .logo-country { position: absolute; top: -42px; right: -42px; font-family: 'Lilita One', sans-serif; font-size: .34em; }
    .fez {
      position: absolute; z-index: 1; top: -.2em; left: -.16em;
      width: .48em; height: .34em; transform: rotate(-13deg);
    }
    .fez svg { display: block; width: 100%; height: 100%; overflow: visible; filter: drop-shadow(0 2px 1px rgba(24,32,23,.2)); }
    .tagline {
      width: min(480px, 44vw); margin: 24px auto 0; padding: 13px 18px;
      border-radius: 13px; background: #ffe17a; color: #141b13;
      font-size: clamp(18px, 2vw, 27px); font-weight: 700; letter-spacing: -.4px;
    }
    .decoration { position: absolute; z-index: 0; pointer-events: none; }
    .decoration svg { display: block; width: 100%; height: 100%; }
    .plate { width: clamp(150px, 21vw, 250px); top: -34px; left: -42px; transform: rotate(-13deg); }
    .melon { width: clamp(140px, 18vw, 218px); top: -21px; right: 1%; transform: rotate(13deg); }
    .citrus { width: 180px; top: -22px; left: 57%; transform: rotate(9deg); }
    .olive-branch { width: clamp(100px, 14vw, 165px); top: 19%; left: -52px; transform: rotate(-18deg); }
    .tomato { width: clamp(85px, 12vw, 140px); left: -24px; bottom: -23px; transform: rotate(-12deg); }
    .wave { width: clamp(90px, 12vw, 135px); }
    .wave-top { top: 8%; left: 43%; transform: rotate(-8deg); }
    .wave-bottom { right: 27%; bottom: 3%; transform: rotate(7deg); }
    .spoon { width: clamp(100px, 15vw, 210px); bottom: 5%; left: -25px; transform: rotate(-14deg); }
    .cook { width: clamp(155px, 20vw, 235px); bottom: -39px; right: -15px; }
    .content {
      position: relative; z-index: 2; display: flex; align-items: center; justify-content: center;
      min-width: 0; padding: 38px clamp(22px, 4vw, 54px);
    }
    .panel {
      width: 100%; max-width: 500px; padding: clamp(25px, 3vw, 38px);
      border-radius: 22px; background: rgba(255,254,250,.96);
    }
    :host(.landing) .shell { display: block; }
    :host(.landing) .brand { min-height: 100vh; }
    :host(.landing) .brand-inner { position: absolute; top: 42%; left: 50%; transform: translate(-50%, -50%); padding: 0; }
    :host(.landing) .content {
      position: absolute; inset: auto 0 5%; padding: 0 18px;
      pointer-events: none;
    }
    :host(.landing) .panel {
      padding: 0; background: transparent; pointer-events: auto;
    }
    @media (max-width: 920px) {
      .shell { grid-template-columns: minmax(0, 1fr) minmax(330px, 46%); }
      .logo { font-size: clamp(140px, 23vw, 230px); letter-spacing: -1px; }
      .logo-country { top: -32px; right: -31px; font-size: 34px; }
    }
    @media (max-width: 680px) {
      .shell { display: flex; flex-direction: column; min-height: 100svh; overflow: hidden; }
      .brand { min-height: 245px; height: 245px; flex: 0 0 245px; }
      .brand-inner { padding: 30px 20px 15px; }
      .logo { font-size: 82px; letter-spacing: 0; }
      .logo-country { top: -24px; right: -27px; font-size: 27px; }
      .tagline { width: min(360px, 82vw); margin-top: 17px; padding: 9px 14px; font-size: 18px; border-radius: 10px; }
      .plate { width: 120px; top: -35px; left: -49px; }
      .melon { width: 112px; top: -23px; right: -22px; }
      .citrus { width: 105px; top: -22px; left: 56%; }
      .cook { width: 116px; right: -25px; bottom: -24px; }
      .olive-branch { width: 75px; top: 30%; left: -35px; }
      .tomato { width: 75px; bottom: -20px; left: -20px; }
      .wave { width: 75px; }
      .wave-top { top: 16%; left: 38%; }
      .wave-bottom { right: 31%; bottom: 1%; }
      .content { flex: 1; align-items: flex-start; padding: 14px 15px 28px; }
      .panel { max-width: 560px; padding: 23px 20px; border-radius: 16px; }
      :host(.landing) .brand-inner { top: 53%; }
      :host(.landing) .brand { min-height: 57vh; height: 57vh; flex-basis: 57vh; }
      :host(.landing) .content { position: relative; inset: auto; padding: 0 16px 24px; }
    }
  `],
})
export class AuthShell {}
