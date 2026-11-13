import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth';
import { Login } from './login';

describe('Login', () => {
  const auth = { login: vi.fn().mockReturnValue(of({})) };

  beforeEach(async () => {
    vi.clearAllMocks();
    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        { provide: AuthService, useValue: auth },
        provideRouter([{ path: 'dashboard', children: [] }]),
      ],
    }).compileComponents();
  });

  it('adds @gmail.com when the user enters only an email name', () => {
    const fixture = TestBed.createComponent(Login);
    fixture.componentInstance.email = 'ranim';
    fixture.componentInstance.password = 'password';

    fixture.componentInstance.submit();

    expect(auth.login).toHaveBeenCalledWith({ email: 'ranim@gmail.com', password: 'password' });
  });

  it('keeps an email address entered in full', () => {
    const fixture = TestBed.createComponent(Login);
    fixture.componentInstance.email = 'ranim@example.com';
    fixture.componentInstance.password = 'password';

    fixture.componentInstance.submit();

    expect(auth.login).toHaveBeenCalledWith({ email: 'ranim@example.com', password: 'password' });
  });
});
