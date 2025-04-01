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
  userRole: string | null = null;

  editingAnswerId: number | null = null;
  editedAnswerText: string = '';

  editingTaskId: number | null = null;
  editedTaskText: string = '';

  selectedFile: File | null = null;

  constructor(private route: ActivatedRoute, private tmService: TmApiService, private authService: AuthService) {
    this.getUsername();
    if (!this.username) {
      window.location.href = '/login';
    }else {
      this.authService.getUserRole(this.username).subscribe((role) => {
        this.userRole = role;
      });
    }
    this.taskId = Number(this.route.snapshot.paramMap.get('id'));
    this.getTask(this.taskId);
    this.getSolutions(this.taskId);
  }


  getTask(taskId: number) {
    this.tmService.getQuestion(taskId).subscribe((data) => {
      this.task = {
        ...data,
        images: data.images ? data.images.split(', ') : []
      };
      console.log(this.task);
    });
  }

  getSolutions(taskId: number) { 
    this.tmService.getSolutionsForQuestion(taskId).subscribe((data) => {
      this.solutions = data.map((sol: any) => ({
        ...sol,
        images: sol.images ? sol.images.split(', ') : []
      }));
    });
  }

  getUsername() {
    this.username = this.authService.getUsernameFromToken();
  }

  canEditOrDeleteTask(): boolean {
    return this.username === this.task?.creator_username || this.userRole === 'ADMIN';
  }

  canEditOrDeleteSolution(creatorUsername: string): boolean {
    return this.username === creatorUsername || this.userRole === 'ADMIN';
  }

  addAnswer() {
    if (!this.newAnswerText.trim()) return;
    this.getUsername();
    const newAnswer: any = {
      task_id: this.taskId,
      solution: this.newAnswerText,
      creator_username: this.username
    };

    this.tmService.addSolution(newAnswer).subscribe((solutionId) => {
      this.getSolutions(this.taskId!);
      this.newAnswerText = '';

    if (this.selectedFile) {
      this.tmService.AddImageSol(this.selectedFile, solutionId).subscribe(() => {
        this.getSolutions(this.taskId!);
      });
      window.location.reload();
    }
  });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  deleteTask() {
    if (confirm('Are you sure you want to delete this question?')) {
      this.tmService.deleteQuestion(this.taskId!).subscribe(() => {
        alert('Question deleted successfully');
        window.history.back();
      });
    }
  }

  deleteAnswer(answerId: number) {
    if (confirm('Are you sure you want to delete this answer?')) {
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


  showModal = false;
  selectedImage: string = '';

  openModal(image: string) {
    this.selectedImage = image;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
}
}
