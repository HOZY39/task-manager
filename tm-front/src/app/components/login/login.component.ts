import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router,  } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {
    if (this.authService.isLoggedIn()) {
    this.router.navigate(['/home']);
  }}

  login() {
    const user = { username: this.username, password: this.password };

    this.authService.login(user).subscribe({
      next: response => {
        console.log('Zalogowano!', response);
        localStorage.setItem('token', response.token); // Zapis tokena
        this.router.navigate(['/home']); // Przekierowanie po zalogowaniu
      },
      error: err => console.error('Błąd logowania', err)
    });
  }
}
