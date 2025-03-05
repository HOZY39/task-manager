import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    const user = { username: this.username, email: this.email, password: this.password, role: 'USER' };

    this.authService.register(user).subscribe({
      next: response => {
        console.log('Rejestracja udana', response);
        this.router.navigate(['/login']); // Po rejestracji przekierowanie do logowania
      },
      error: err => console.error('Błąd rejestracji', err)
    });
  }
}
