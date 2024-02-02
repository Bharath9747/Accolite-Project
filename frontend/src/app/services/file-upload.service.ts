// file-upload.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FileUploadService {
  constructor(private http: HttpClient) {}
  api: string = 'http://localhost:8080/api';
  uploadFiles(formData: FormData): Observable<{ [key: string]: string }> {
    return this.http.post<{ [key: string]: string }>(
      `${this.api}/upload`,
      formData
    );
  }
}
