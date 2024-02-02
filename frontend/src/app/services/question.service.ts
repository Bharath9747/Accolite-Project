import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Question } from '../model/question.model.';
import { HttpClient, HttpParams } from '@angular/common/http';

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
}
