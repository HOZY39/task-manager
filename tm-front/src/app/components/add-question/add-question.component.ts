import { Component } from '@angular/core';
import { NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TmApiService } from '../../services/tm-api.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-question',
  imports: [NgFor, FormsModule],
  templateUrl: './add-question.component.html',
  styleUrl: './add-question.component.css'
})
export class AddQuestionComponent {
  questionText: string = '';
  selectedSection: string = '';
  selectedSubject: string = '';

  username: string | null = null;

  subjects: any[] = [];
  sections: any[] = [];

  selectedFile: File | null = null;

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

    if (!this.questionText.trim()) return;
    this.getUsername();
    const newQuestion: any = {
      description: this.questionText,
      section: this.selectedSection,
      subject: this.selectedSubject,
      creator_username: this.username
    };

    this.tmService.addQuestion(newQuestion).subscribe((questionId) => {
      if (this.selectedFile) {
        this.tmService.AddImageQue(this.selectedFile, questionId).subscribe(() => {
          console.log('Image uploaded successfully');
        });
      }
      this.router.navigate(['/']);
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }
}
