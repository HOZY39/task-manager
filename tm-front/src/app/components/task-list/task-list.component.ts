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
  search: string = '';
  TaskList: any = [];
  constructor(private tmService: TmApiService, private router: Router, private route: ActivatedRoute, private location: Location) {
  }
  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      console.log('Params:', params.get('section'), params.get('search'));
      if (params.get('section')) {
        this.section = params.get('section') ?? ''; // ?? -> if null then ''
        this.getTasks();
        console.log('Section:', this.section);
      } else if (params.get('search')) {
        this.search = params.get('search') ?? '';
        this.getTaskByText();
        console.log('Search:', this.search);
      }
    });
  }

  getTasks() {
    this.tmService.getQuestionsBySection(this.section).subscribe((data) => {
      console.log('Data from API', data);
      this.TaskList = data;
      console.log('Tasks for section:', this.section, this.TaskList);
    });
  }
  getTaskByText() {
    this.tmService.getQuestionsByText(this.search).subscribe((data) => {
      console.log('Data from API', data);
      this.TaskList = data;
    });
  }

  goBack() {
    this.location.back(); 
  }
  goToTask(taskId: number) {
    this.router.navigate(['/task', taskId]);
  }

}
