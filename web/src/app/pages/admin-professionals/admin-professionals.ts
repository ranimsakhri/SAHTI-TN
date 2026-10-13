import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AdminService } from '../../services/admin';
import { errMsg } from '../../services/auth';

@Component({
  selector: 'app-admin-professionals',
  imports: [RouterLink],
  template: `
    <header class="top">
      <span class="logo">SA7TI<sup>TN</sup></span>
      <a routerLink="/dashboard" class="back">← Tableau de bord</a>
    </header>

    <main>
      <h1>Validation des professionnels</h1>

      <div class="tabs">
        @for (t of tabs; track t.key) {
          <button [class.active]="filtre() === t.key" (click)="setFiltre(t.key)">{{ t.label }}</button>
        }
      </div>

      @if (error()) { <div class="err">{{ error() }}</div> }
      @if (items().length === 0) { <p class="vide">Aucun dossier dans cette catégorie.</p> }

      @for (it of items(); track it.id) {
        <article class="dossier">
          <div class="head">
            <div>
              <h3>{{ it.prenom }} {{ it.nom }}</h3>
              <small>{{ it.email }} · {{ it.type === 'COACH' ? 'Coach sportif' : 'Nutritionniste' }}</small>
            </div>
            <span class="st" [class]="'st ' + it.status">{{ statut(it.status) }}</span>
          </div>
          <p><b>Spécialité :</b> {{ it.specialite }}</p>
          @if (it.bio) { <p class="bio">{{ it.bio }}</p> }
          @if (it.motifRefus) { <p class="motif">Motif du refus : {{ it.motifRefus }}</p> }

          <div class="docs">
            <button (click)="open(it, 'cv')">CV</button>
            <button (click)="open(it, 'lettre')">Lettre</button>
            <button (click)="open(it, 'diplome')">Diplôme</button>
          </div>

          @if (it.status === 'EN_ATTENTE') {
            <div class="actions">
              <button class="ok" (click)="approve(it)">Approuver</button>
              <button class="ko" (click)="reject(it)">Refuser</button>
            </div>
          }
        </article>
      }
    </main>
  `,
  styles: [`
    .top { display:flex; justify-content:space-between; align-items:center; padding:14px 24px; background:#5f9e68; color:#fff; }
    .logo { font-family:'Lilita One','Poppins',sans-serif; font-weight:400; font-size:30px; }
    .logo sup { font-family:'Lilita One','Poppins',sans-serif; font-size:12px; }
    .back { color:#fff; text-decoration:none; font-weight:600; }
    main { max-width:860px; margin:0 auto; padding:28px 20px; }
    h1 { color:#143d2b; }
    .tabs { display:flex; gap:8px; flex-wrap:wrap; margin:16px 0; }
    .tabs button { padding:9px 18px; border:1.5px solid #e2dccb; background:#fffdf6; border-radius:999px; cursor:pointer; font:inherit; }
    .tabs button.active { background:#143d2b; color:#f7f1e1; border-color:#143d2b; }
    .dossier { background:#fffdf6; border:2px solid #e2dccb; border-radius:22px; padding:20px; margin-bottom:14px; }
    .head { display:flex; justify-content:space-between; gap:10px; align-items:flex-start; }
    .head h3 { margin:0; color:#143d2b; }
    small { color:#6b7b70; }
    .st { padding:4px 12px; border-radius:999px; font-size:12px; font-weight:600; white-space:nowrap; }
    .EN_ATTENTE { background:#fff6d6; color:#6a5300; }
    .APPROUVE { background:#e6f4e8; color:#1f5a3e; }
    .REFUSE { background:#fde8ea; color:#9b1c28; }
    .bio { color:#555; font-size:14px; }
    .motif { color:#9b1c28; font-size:14px; }
    .docs, .actions { display:flex; gap:10px; flex-wrap:wrap; margin-top:12px; }
    .docs button { padding:8px 16px; border:1.5px solid #1f5fa6; color:#1f5fa6; background:#fff; border-radius:999px; cursor:pointer; font:inherit; }
    .actions button { padding:10px 22px; border:0; border-radius:999px; color:#fff; cursor:pointer; font:inherit; font-weight:600; }
    .ok { background:#1f5a3e; } .ko { background:#b3202a; }
    .err { background:#fde8ea; color:#9b1c28; padding:10px 14px; border-radius:12px; }
    .vide { color:#6b7b70; }
  `],
})
export class AdminProfessionals implements OnInit {
  private admin = inject(AdminService);

  tabs = [
    { key: 'EN_ATTENTE', label: 'En attente' },
    { key: 'APPROUVE', label: 'Approuvés' },
    { key: 'REFUSE', label: 'Refusés' },
    { key: 'TOUS', label: 'Tous' },
  ];
  filtre = signal('EN_ATTENTE');
  items = signal<any[]>([]);
  error = signal('');

  ngOnInit() { this.load(); }

  setFiltre(f: string) { this.filtre.set(f); this.load(); }

  load() {
    this.error.set('');
    const status = this.filtre() === 'TOUS' ? undefined : this.filtre();
    this.admin.list(status).subscribe({
      next: (l) => this.items.set(l),
      error: (e) => this.error.set(errMsg(e)),
    });
  }

  statut(s: string) {
    return s === 'EN_ATTENTE' ? 'En attente' : s === 'APPROUVE' ? 'Approuvé' : 'Refusé';
  }

  open(it: any, doc: string) {
    this.admin.document(it.id, doc).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${doc}-${it.nom}-${it.prenom}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: (e) => this.error.set(errMsg(e)),
    });
  }

  approve(it: any) {
    this.admin.approve(it.id).subscribe({
      next: () => this.load(),
      error: (e) => this.error.set(errMsg(e)),
    });
  }

  reject(it: any) {
    const motif = window.prompt('Motif du refus (visible par le candidat) :');
    if (motif === null) return;
    this.admin.reject(it.id, motif.trim()).subscribe({
      next: () => this.load(),
      error: (e) => this.error.set(errMsg(e)),
    });
  }
}
