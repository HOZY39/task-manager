import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TmApiService } from '../../services/tm-api.service';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-task-page',
  imports: [NgFor, NgIf, FormsModule],
  templateUrl: './task-page.component.html',
  styleUrl: './task-page.component.css'
})
export class TaskPageComponent {
  taskId: number | null = null;
  task: any = null;
  solutions: any = [];
  newAnswerText: string = '';
  username: string | null = null;

  editingAnswerId: number | null = null;
  editedAnswerText: string = '';

  editingTaskId: number | null = null;
  editedTaskText: string = '';

  constructor(private route: ActivatedRoute, private tmService: TmApiService, private authService: AuthService) {
    this.getUsername();
    this.taskId = Number(this.route.snapshot.paramMap.get('id'));
    this.getTask(this.taskId);
    this.getSolutions(this.taskId);
  }


  getTask(taskId: number) {
    this.tmService.getQuestion(taskId).subscribe((data) => {
      this.task = data;
    });
  }

  getSolutions(taskId: number) { 
    this.tmService.getSolutionsForQuestion(taskId).subscribe((data) => {
      this.solutions = data;
    });
  }

  getUsername() {
    this.username = this.authService.getUsernameFromToken();
  }

  canEditOrDeleteTask(): boolean {
    return this.username === this.task?.creator_username;
  }

  canEditOrDeleteSolution(creatorUsername: string): boolean {
    //console.log(creatorUsername);
    //console.log(this.username);
    return this.username === creatorUsername;
  }

  addAnswer() {
    if (!this.newAnswerText.trim()) return;
    this.getUsername();
    const newAnswer: any = {
      task_id: this.taskId,
      solution: this.newAnswerText,
      creator_username: this.username
    };

    this.tmService.addSolution(newAnswer).subscribe((response) => {
      //this.solutions.push(response); // Aktualizacja listy odpowiedzi
      this.getSolutions(this.taskId!); // Aktualizacja listy odpowiedzi
      this.newAnswerText = ''; // Czyszczenie pola
    });
  }




  deleteTask() {
    if (confirm('Czy na pewno chcesz usunąć to pytanie?')) {
      this.tmService.deleteQuestion(this.taskId!).subscribe(() => {
        alert('Pytanie zostało usunięte');
        window.history.back(); // Powrót do poprzedniej strony
      });
    }
  }

  deleteAnswer(answerId: number) {
    if (confirm('Czy na pewno chcesz usunąć tę odpowiedź?')) {
      this.tmService.deleteSolution(answerId).subscribe(() => {
        this.getSolutions(this.taskId!);
      });
    }
  }

  startEditing(answer: any) {
    this.editingAnswerId = answer.id;
    this.editedAnswerText = answer.solution;
  }

  cancelEditing() {
    this.editingAnswerId = null;
    this.editedAnswerText = '';
  }

  saveEditedAnswer(answerId: number) {
    if (!this.editedAnswerText.trim()) return;

    this.tmService.updateSolution(answerId, { solution: this.editedAnswerText }).subscribe(() => {
      this.getSolutions(this.taskId!);
      this.editingAnswerId = null;
      this.editedAnswerText = '';
    });
  }

  saveEditedTask() {
    this.tmService.updateQuestion(this.taskId!, { description: this.editedTaskText }).subscribe(() => {
      this.getTask(this.taskId!);
    });
  }

  goBack() {
    window.history.back();
  }
}
