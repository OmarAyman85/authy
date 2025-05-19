import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Authy';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    this.authService.refreshToken().subscribe({
      next: (res) => {
        this.authService.setAccessToken(res.access_token);
      },
      error: (error) => {
        // this.authService.logout();
        this.router.navigate(['/login']);
      },
    });
  }
}
