import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MainLayoutComponent } from './components/main-layout/main-layout.component';

@Component({
  selector: 'app-root',
  imports: [MainLayoutComponent, RouterModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'tm-front';
}
