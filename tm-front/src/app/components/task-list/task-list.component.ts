import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TmApiService } from '../../services/tm-api.service';
import { NgFor, NgIf, Location } from '@angular/common';

@Component({
  selector: 'app-task-list',
  imports: [NgFor, NgIf],
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  section: string = '';
  TaskList: any = [];
  constructor(private tmService: TmApiService, private router: Router, private route: ActivatedRoute, private location: Location) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.section = params.get('section') || '';
      console.log('Wybrana sekcja:', this.section);
      // Tutaj możesz pobrać zadania dla danej sekcji z API
    });
    this.getTasks();
  }

  getTasks() {
    this.tmService.getQuestionsBySection(this.section).subscribe((data) => {
      console.log('Dane z API', data);
      this.TaskList = data;
      console.log('Zadania dla sekcji', this.section, this.TaskList);
    });
  }

  goBack() {
    this.location.back(); // Cofnij użytkownika do poprzedniej strony
  }
  goToTask(taskId: number) {
    this.router.navigate(['/task', taskId]); // Przekierowanie na stronę zadania
  }

}
