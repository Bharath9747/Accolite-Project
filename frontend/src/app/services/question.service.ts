import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Question } from '../model/question.model.';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Template } from '../model/template.model';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  api: string = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}
  getQuestion(id: number): Observable<Question> {
    const params = new HttpParams().set('id', id);
    return this.http.get<Question>(`${this.api}/question`, { params });
  }
  runCode(template: Template): Observable<string[]> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    const options = { headers: headers };

    return this.http.post<string[]>(`${this.api}/run`, template, options);
  }
}
