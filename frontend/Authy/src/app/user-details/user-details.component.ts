import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
})
export class UserDetailsComponent implements OnInit {
  user: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get('http://localhost:8081/api/user/me').subscribe({
      next: (data) => {
        this.user = data;
      },
      error: (error) => {
        console.error('Failed to fetch user details', error);
      },
    });
  }
}
