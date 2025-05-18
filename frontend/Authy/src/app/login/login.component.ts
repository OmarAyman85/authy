import { Component, inject } from '@angular/core';
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
    console.log('THIS IS THE BODY WILL BE SENT TO THE API::');
    console.log(this.loginReq.value);
    this.http
      .post('http://localhost:8081/api/login', this.loginReq.value, {
        observe: 'response',
      })
      .subscribe(
        (response: HttpResponse<any>) => {
          // if (response.status === 200 && response.body?.mfaEnabled) {
          //   // Store MFA data and navigate to the MFA setup page
          //   const secretImageUri = response.body.secretImageUri;
          //   const userName = response.body.userName;
          //   this.router.navigate(['/mfa-setup'], {
          //     state: { secretImageUri, userName },
          //   });
          // } else
          if (response.status === 200) {
            this.router.navigate(['/']);
          }
        },
        (error) => {
          console.error('Registration failed:', error);
          alert('User registration failed due to a server error');
        }
      );
  }
}
