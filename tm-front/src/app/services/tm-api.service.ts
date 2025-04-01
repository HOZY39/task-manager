import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TmApiService {

  private apiTask = 'http://localhost:8080/api/tasks';
  private apiSolutions = 'http://localhost:8080/api/solutions';
  private apiTaskSection = 'http://localhost:8080/api/tasks/section';
  private apiSubject = 'http://localhost:8080/api/subject';
  private apiSection = 'http://localhost:8080/api/section';

  constructor(private http: HttpClient) { }

  getQuestion(id: number): Observable<any> {
    return this.http.get(this.apiTask + '/' + id);
  }

  getQuestions(): Observable<any> {
    return this.http.get(this.apiTask);
  }

  getQuestionsBySection(section: String): Observable<any> {
    return this.http.get(this.apiTaskSection+"/"+section);
  }
  
  getSubjects(): Observable<any> {
    return this.http.get(this.apiSubject);
  }
  
  getSectionsForSubject(subject: String): Observable<any> {
    return this.http.get(this.apiSection+"/"+subject);
  }

  getSolutionsForQuestion(id: number): Observable<any> {
    return this.http.get(this.apiTask + '/' + id + '/solutions');
  }

  addQuestion(question: { description: string, subject: string, section: string, creator_username: string}): Observable<any> {
    return this.http.post<any>(this.apiTask, question);
  }

  AddImageQue(file: File, queId: String): Observable<any> {
    const formData = new FormData();
    formData.append('image', file);
    console.log(this.apiTask+'/'+queId+'/image');
    return this.http.post<any>(this.apiTask+'/'+queId+'/image', formData);
  }

  addSolution(solution: { task_id: number, solution: string, creator_username: string}): Observable<any> {
    return this.http.post<any>(this.apiSolutions, solution);
  }

  AddImageSol(file: File, solId: String): Observable<any> {
    const formData = new FormData();
    formData.append('image', file);

    return this.http.post<any>(this.apiSolutions+'/'+solId+'/image', formData);
  }

  deleteQuestion(id: number): Observable<any> {
    return this.http.delete(this.apiTask + '/' + id);
  }

  deleteSolution(id: number): Observable<any> {
    return this.http.delete(this.apiSolutions + '/' + id);
  }

  updateQuestion(id: number, question: { description: string }): Observable<any> {
    return this.http.put(this.apiTask + '/' + id, question);
  }

  updateSolution(id: number, solution: { solution: string }): Observable<any> {
    return this.http.put(this.apiSolutions + '/' + id, solution);
  }

  getQuestionsByText(text: string): Observable<any> {
    return this.http.get(this.apiTask + '/search/' + text);
  }
}
