import { Component, Inject, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  HttpClient,
  HttpClientModule,
  HttpResponse,
} from '@angular/common/http';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, HttpClientModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  http = inject(HttpClient);
  router = inject(Router);
  authService = inject(AuthService);

  loginReq: FormGroup = new FormGroup({
    userName: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(20),
      Validators.pattern('^[a-zA-Z0-9_]+$'),
    ]),
    password: new FormControl('', [Validators.required]),
    rememberMe: new FormControl(false),
  });

  onSubmit(): void {
    this.http
      .post('http://localhost:8081/api/login', this.loginReq.value, {
        observe: 'response',
        withCredentials: true,
      })
      .subscribe(
        (response: HttpResponse<any>) => {
          if (response.status === 200) {
            const token = response.body?.access_token;
            if (token) {
              this.authService.setAccessToken(token);
              console.log('THE ACCESS TOKEN : ', token);
              this.router.navigate(['/user-details']);
            }
          }
        },
        (error) => {
          console.error('Registration failed:', error);
          alert('User registration failed due to a server error');
        }
      );
  }
}
