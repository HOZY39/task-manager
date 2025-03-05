import { Component } from '@angular/core';
import { TmApiService } from '../../services/tm-api.service';
import { NgIf, NgFor } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  imports: [NgIf, NgFor, RouterModule, FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  subjects: any[] = [];
  sections: any[] = [];
  searchQuery: string = '';
  chosenSubject: string = '';
  chosenSection: string = '';
  showSubjects: boolean = true;
  constructor(private tmService: TmApiService, private router: Router) {
    this.tmService.getSubjects().subscribe(data => {
      this.subjects = data;
      console.log(data);
    });
  }
  getSections(subject: any) {
    this.tmService.getSectionsForSubject(subject).subscribe(data => {
      this.sections = data;
      console.log(data);
    });
  }
  searchTask() {
    if (this.searchQuery.trim()) {
      this.router.navigate(['/search'], { queryParams: { q: this.searchQuery } });
    }
  }

  goToTopics(subject: any) {
    this.chosenSubject = subject;
    this.getSections(this.chosenSubject);
    this.showSubjects = false;
  }
  goToList(section: any) {
    this.chosenSection = section;
    this.router.navigate(['/task'], { queryParams: { q: this.searchQuery } });
  }

  backToSubjects() {
    this.showSubjects = true;
  }
}
