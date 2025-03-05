import { Component, OnInit } from '@angular/core';
import { TmApiService } from '../../services/tm-api.service';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-comp-num-1',
  imports: [NgFor, NgIf, FormsModule],
  templateUrl: './comp-num-1.component.html',
  styleUrl: './comp-num-1.component.css'
})
export class CompNum1Component {
  message = 'Witaj w Angularze!';

  changeMessage() {
    this.message = 'Tekst został zmieniony!';
  }


  questions: any[] = [];
  subjects: any[] = [];
  sections: any[] = [];
  
  newQuestion: string = '';
  selectedSubject: string = '';
  selectedSection: string = '';
  
  constructor(private apiService: TmApiService) {}

  ngOnInit() {
    this.loadQuestions();
    this.loadSections();
    this.loadSubjects();
  }

  // Pobranie pytań z backendu
  loadQuestions() {
    this.apiService.getQuestions().subscribe((data) => {
      this.questions = data;
    });
  }
  // Pobranie pytań z backendu
  loadSubjects() {
    this.apiService.getSubjects().subscribe((data) => {
      this.subjects = data;
    });
  }
  // Pobranie pytań z backendu
  loadSections() {
    this.apiService.getSections().subscribe((data) => {
      this.sections = data;
    });
  }

  // Dodanie nowego pytania
  addQuestion() {
    if (!this.newQuestion.trim() || !this.selectedSubject || !this.selectedSection) return;

    const question = {
      description: this.newQuestion,
      subject: this.selectedSubject,
      section: this.selectedSection
    };

    console.log(question);

    this.apiService.addQuestion(question).subscribe((response) => {
      this.questions.push(response);
      this.newQuestion = '';
      this.selectedSubject = '';
      this.selectedSection = '';
    });
  }
}
