import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  password_check = '';
  passwordCheckClass = '';
  passwordClass = '';
  RegButtonCheck = false
  passwordCheckError = '';
  passwordError = '';

  constructor(private authService: AuthService, private router: Router) {
    this.RegButtonCheck = true;
  }

  CheckPassword() {
    if (this.password.length < 8) {
      this.passwordClass = 'pass-to-short';
      this.RegButtonCheck = true;
      this.passwordError = 'Password must be at least 8 characters long';
    } else {
      this.passwordClass = '';
      this.RegButtonCheck = false;
      this.passwordError = '';
      this.CheckPasswordMatch();
    }
  }

  CheckPasswordMatch() {
    if (this.password !== this.password_check) {
      this.passwordCheckClass = 'pass-not-match';
      this.RegButtonCheck = true;
      this.passwordCheckError = 'Passwords do not match';
    } else {
      this.passwordCheckClass = '';
      this.RegButtonCheck = false;
      this.passwordCheckError = '';
      this.CheckPassword();
    }
  }

  register() {
    const user = { username: this.username, email: this.email, password: this.password, role: 'USER' };

    this.authService.register(user).subscribe({
      next: response => {
        console.log('Registration complete', response);
        this.router.navigate(['/login']);
      },
      error: err => console.error('Registration error', err)
    });
  }
}
