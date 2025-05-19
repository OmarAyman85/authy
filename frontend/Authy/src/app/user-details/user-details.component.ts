import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  providers: [],
})
export class UserDetailsComponent implements OnInit {
  user: any;

  constructor(private http: HttpClient, private authService: AuthService) {}

  ngOnInit(): void {
    const token = this.authService.getAccessToken();

    if (token) {
      // ✅ Token already available – fetch user immediately
      this.fetchUserDetails();
    } else {
      // 🔁 Wait for token to be refreshed
      this.authService.tokenRefreshed$.subscribe({
        next: () => this.fetchUserDetails(),
        error: (err) => console.error('Token refresh failed', err),
      });
    }
  }

  private fetchUserDetails() {
    this.http.get('http://localhost:8081/api/user/me').subscribe({
      next: (data) => (this.user = data),
      error: (err) => console.error('Failed to fetch user details', err),
    });
  }
}
