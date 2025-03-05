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

  // Pobranie pytania z backendu
  getQuestion(id: number): Observable<any> {
    return this.http.get(this.apiTask + '/' + id);
  }

  // Pobranie pytań z backendu
  getQuestions(): Observable<any> {
    return this.http.get(this.apiTask);
  }

  // Pobranie pytań z backendu dla danej sekcji
  getQuestionsBySection(section: String): Observable<any> {
    return this.http.get(this.apiTaskSection+"/"+section);
  }
  
  // Pobranie subjects z backendu
  getSubjects(): Observable<any> {
    return this.http.get(this.apiSubject);
  }
  
  // Pobranie sections z backendu dla danego subject
  getSectionsForSubject(subject: String): Observable<any> {
    return this.http.get(this.apiSection+"/"+subject);
  }

  // Pobranie solutions z backendu dla danego pytania
  getSolutionsForQuestion(id: number): Observable<any> {
    return this.http.get(this.apiTask + '/' + id + '/solutions');
  }

  // Dodawanie nowego pytania
  addQuestion(question: { description: string, subject: string, section: string, creator_username: string}): Observable<any> {
    return this.http.post<any>(this.apiTask, question);
  }

  // Dodawanie nowego rozwiązania
  addSolution(solution: { task_id: number, solution: string, creator_username: string}): Observable<any> {
    return this.http.post<any>(this.apiSolutions, solution);
  }

  // Usuwanie pytania
  deleteQuestion(id: number): Observable<any> {
    return this.http.delete(this.apiTask + '/' + id);
  }

  // Usuwanie rozwiązania
  deleteSolution(id: number): Observable<any> {
    return this.http.delete(this.apiSolutions + '/' + id);
  }

  // Edytowanie pytania
  updateQuestion(id: number, question: { description: string }): Observable<any> {
    return this.http.put(this.apiTask + '/' + id, question);
  }

  // Edytowanie rozwiązania
  updateSolution(id: number, solution: { solution: string }): Observable<any> {
    return this.http.put(this.apiSolutions + '/' + id, solution);
  }
}
