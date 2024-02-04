import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Question } from '../model/question.model.';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Code } from '../model/code.model';

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
  getAllQuestion(): Observable<Question[]> {
    return this.http.get<Question[]>(`${this.api}/question/all`);
  }
  runCode(code: Code): Observable<string[]> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    const options = { headers: headers };

    return this.http.post<string[]>(`${this.api}/run`, code, options);
  }
  uploadData(
    file: File,
    selectedQuestion: Question[]
  ): Observable<{ [key: string]: string }> {
    const formData = new FormData();
    formData.append('file', file);

    // Append each selected question to the formData
    for (const question of selectedQuestion) {
      formData.append('selectedQuestions', JSON.stringify(question));
    }

    const headers = new HttpHeaders(); // Let Angular set the content type automatically
    const options = { headers: headers };

    return this.http.post<{ [key: string]: string }>(
      `${this.api}/assign`,
      formData,
      options
    );
  }
}
