// src/app/services/auth.service.ts
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, Observable, Subject, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient);
  private tokenRefreshedSubject = new Subject<boolean>();
  tokenRefreshed$: Observable<boolean> =
    this.tokenRefreshedSubject.asObservable();

  private accessToken: string | null = null;

  setAccessToken(token: string): void {
    this.accessToken = token;
  }

  getAccessToken(): string | null {
    return this.accessToken;
  }

  clearAccessToken(): void {
    this.accessToken = null;
  }

  refreshToken() {
    return this.http
      .get<{ access_token: string }>(
        'http://localhost:8081/api/refresh-token',
        { withCredentials: true }
      )
      .pipe(
        tap((res) => {
          this.setAccessToken(res.access_token);
          this.tokenRefreshedSubject.next(true);
        }),
        catchError(() => {
      this.tokenRefreshedSubject.error(new Error('Refresh failed'));
          return throwError(() => new Error('Failed to refresh token'));
        })
      );
  }
}
