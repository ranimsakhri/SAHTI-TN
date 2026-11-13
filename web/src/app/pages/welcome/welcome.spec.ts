import { provideRouter } from '@angular/router';
import { TestBed } from '@angular/core/testing';
import { Welcome } from './welcome';

describe('Welcome', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Welcome],
      providers: [provideRouter([])],
    }).compileComponents();
  });

  it('presents SA7TI as a nutrition and fitness platform', () => {
    const fixture = TestBed.createComponent(Welcome);
    fixture.detectChanges();

    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('h1')?.textContent).toContain('À votre rythme');
    expect(page.querySelector('#nutrition h3')?.textContent).toContain('Mieux composer vos repas');
    expect(page.querySelector('.movement-card h3')?.textContent).toContain('Bouger à votre façon');
  });

  it('offers client and professional access after the information sections', () => {
    const fixture = TestBed.createComponent(Welcome);
    fixture.detectChanges();

    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('#approche')).toBeTruthy();
    expect(page.querySelector('#acces')).toBeTruthy();
    expect(page.querySelector('.client-access a[href="/login"]')).toBeTruthy();
    expect(page.querySelector('.pro-access a[href="/login"]')).toBeTruthy();
    expect(page.querySelector('.client-access a[href="/register"]')).toBeTruthy();
    expect(page.querySelector('.pro-access a[href="/pro/register"]')).toBeTruthy();
  });

  it('introduces the 2026 commitments and the Zitouna guide', () => {
    const fixture = TestBed.createComponent(Welcome);
    fixture.detectChanges();

    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('#cap-2026')?.textContent).toContain('CAP 2026');
    expect(page.querySelector('#zitouna')?.textContent).toContain('Zitouna');
    expect(page.querySelector('.zitouna-launcher')).toBeTruthy();

    (page.querySelector('.zitouna-launcher') as HTMLButtonElement).click();
    fixture.detectChanges();
    expect(page.querySelector('.zitouna-panel')?.textContent).toContain('repères nutritionnels');
    expect(page.querySelector('.assistant-disclaimer')?.textContent).toContain('service d’analyse IA');
  });

  it('invites visitors to use SA7TI on mobile and shows nutrition with fitness', () => {
    const fixture = TestBed.createComponent(Welcome);
    fixture.detectChanges();

    const page = fixture.nativeElement as HTMLElement;
    expect(page.querySelector('#mobile h2')?.textContent).toContain('dans votre poche');
    expect(page.querySelector('#mobile a[href="/register"]')?.textContent).toContain('sur mobile');
    expect(page.querySelector('.home-brand img')?.getAttribute('src')).toBe('/assets/sa7ti-logo-original.png');
    expect(page.querySelector('.nutrition-card .card-illustration')?.getAttribute('src')).toBe('/assets/sa7ti-chef.png');
    expect(page.querySelector('.movement-card .card-illustration')?.getAttribute('src')).toBe('/assets/sa7ti-coach.png');
    expect(page.querySelector('.coaching-card .card-illustration')?.getAttribute('src')).toBe('/assets/sa7ti-eater.png');
    expect(page.querySelector('.brand-girl')?.getAttribute('src')).toBe('/assets/sa7ti-girl.png');
    expect(page.querySelector('.brand-girl')?.getAttribute('alt')).toContain('Zitouna');
    expect(page.querySelector('.doodle-tomato')?.getAttribute('src')).toBe('/assets/sa7ti-tomatoes.png');
    expect(page.querySelector('.doodle-citrus')?.getAttribute('src')).toBe('/assets/sa7ti-lemons.png');
    expect(page.querySelector('.mascot-caption')?.textContent).toContain('nutrition & fitness');
    expect(page.querySelector('.phone-pair')?.textContent).toContain('Mouvement');
  });
});
