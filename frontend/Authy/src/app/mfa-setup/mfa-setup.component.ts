import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-mfa-setup',
  imports: [FormsModule, HttpClientModule],
  templateUrl: './mfa-setup.component.html',
  styleUrl: './mfa-setup.component.css',
})
export class MfaSetupComponent {
  secretImageUri: string | null = null;
  username: string = '';
  code: string = '';
  errorMessage: string = '';

  constructor(private router: Router, private http: HttpClient) {
    const navigation = this.router.getCurrentNavigation();
    console.log(navigation?.extras?.state);
    this.secretImageUri = navigation?.extras?.state?.['secretImageUri'] || null;
    this.username = navigation?.extras?.state?.['userName'] || '';
    console.log('USERNAME::::' + this.username);
    console.log('SecretImageUri::::' + this.secretImageUri);
  }

  verifyCode() {
    this.http
      .post<any>('http://localhost:8081/api/verify-code', {
        username: this.username,
        code: this.code,
      })
      .subscribe({
        next: (response) => {
          const token = response.token;
          if (token) {
            localStorage.setItem('jwtToken', token);
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
