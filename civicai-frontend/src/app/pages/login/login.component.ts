import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  authService = inject(AuthService);
  router = inject(Router);

  identifier = '';
  password = '';
  errorMessage = '';
  isLoading = false;

  login() {
    this.errorMessage = '';
    this.isLoading = true;

    this.authService.login(this.identifier, this.password).subscribe({
      next: (user) => {
        this.isLoading = false;
        if (user.role === 'ROLE_CITIZEN') {
          this.router.navigate(['/citizen-home']);
        } else {
          this.router.navigate(['/admin/dashboard']);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Invalid credentials or blocked account.';
      }
    });
  }

  quickLogin(email: string, pass: string) {
    this.identifier = email;
    this.password = pass;
    this.login();
  }
}
