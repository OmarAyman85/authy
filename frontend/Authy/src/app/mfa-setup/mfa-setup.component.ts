import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-mfa-setup',
  imports: [FormsModule, HttpClientModule],
  templateUrl: './mfa-setup.component.html',
  styleUrl: './mfa-setup.component.css',
})
export class MfaSetupComponent {
  secretImageUri: string | null = null;
  userName: string = '';
  code: string = '';
  errorMessage: string = '';

  constructor(
    private router: Router,
    private http: HttpClient,
    private authService: AuthService
  ) {
    const navigation = this.router.getCurrentNavigation();
    this.secretImageUri = navigation?.extras?.state?.['secretImageUri'] || null;
    this.userName = navigation?.extras?.state?.['userName'] || '';
  }

  verifyCode() {
    this.http
      .post<any>('http://localhost:8081/api/verify-code', {
        username: this.userName,
        code: this.code,
      })
      .subscribe({
        next: (response) => {
          const token = response.access_token;
          if (token) {
            this.authService.setAccessToken(token);
            this.router.navigate(['/']);
          } else {
            this.errorMessage = 'Verification failed: No token received.';
          }
        },
        error: () => {
          this.errorMessage = 'Invalid code, please try again.';
        },
      });
  }
}
