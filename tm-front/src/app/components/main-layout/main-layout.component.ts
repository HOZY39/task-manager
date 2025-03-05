import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-main-layout',
  imports: [RouterModule, NgIf],
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent {
  username: string | null = null;
  constructor(private authService: AuthService, private router: Router) {
    this.username = this.authService.getUsernameFromToken();}

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
