import { Component } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TmApiService } from '../../services/tm-api.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-question',
  imports: [NgFor, NgIf, FormsModule],
  templateUrl: './add-question.component.html',
  styleUrl: './add-question.component.css'
})
export class AddQuestionComponent {
  questionText: string = '';
  answerText: string = '';
  selectedSection: string = '';
  selectedSubject: string = '';

  username: string | null = null;

  subjects: any[] = [];
  sections: any[] = [];

  constructor(private tmService: TmApiService, private authService: AuthService, private router: Router) {
    this.getUsername();

    this.tmService.getSubjects().subscribe(data => {
      this.subjects = data;
      console.log(data);
    });}

  getUsername() {
    this.username = this.authService.getUsernameFromToken();
  }

  updateSections() {
    this.tmService.getSectionsForSubject(this.selectedSubject).subscribe(data => {
      this.sections = data;
      console.log(data);
    });
  }

  addQuestion() {

    const newQuestion: any = {
      description: this.questionText,
      section: this.selectedSection,
      subject: this.selectedSubject,
      creator_username: this.username
    };

    this.tmService.addQuestion(newQuestion).subscribe((data) => {
      console.log(data);

      this.router.navigate(['/']);
    });
  }

}
