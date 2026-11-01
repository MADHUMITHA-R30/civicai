import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  authService = inject(AuthService);
  router = inject(Router);

  name = '';
  email = '';
  mobile = '';
  password = '';
  errorMessage = '';
  isLoading = false;

  register() {
    this.errorMessage = '';
    this.isLoading = true;

    this.authService.register({
      name: this.name,
      email: this.email,
      mobile: this.mobile,
      password: this.password,
      role: 'ROLE_CITIZEN'
    }).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/citizen-home']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Registration failed.';
      }
    });
  }
}
