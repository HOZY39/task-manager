import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router,  } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterModule, NgIf],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  loginError = '';

  constructor(private authService: AuthService, private router: Router) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/home']);
    }
  }

  login() {
    const user = { username: this.username, password: this.password };

    this.authService.login(user).subscribe({
      next: response => {
        console.log('Logged in!', response);
        localStorage.setItem('token', response.token); // Save token
        this.loginError = '';
        window.location.reload();
        window.location.href = '/home';
      },
      error: err => {
        console.error('Login error', err)
        this.loginError = 'Invalid username or password';
      }
    });
  }
}
