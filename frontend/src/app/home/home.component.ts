import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FileUploadService } from '../services/file-upload.service';
import { types } from '../util/constants';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  questionZip!: File;
  types = types;
  onFileChange(event: any) {
    const file = event.target.files[0];
    let fileName: string = file.name;
    this.questionZip = file;
    const formData = new FormData();
    formData.append('file', this.questionZip);
    if (fileName.includes('Coding')) {
      formData.append('type', 'Coding');

      formData.append('title', fileName.slice(0, fileName.length - 11));
    } else if (fileName.includes('Database')) {
      formData.append('type', 'Database');
      formData.append('title', fileName.slice(0, fileName.length - 13));
    } else {
      alert('Upload Data Correctly');
      return;
    }

    this.fileUploadService.uploadFiles(formData).subscribe({
      next: (response) => {
        alert(response['result']);
      },
      error: (error) => {
        alert(error);
      },
    });
  }
  constructor(
    private fileUploadService: FileUploadService,
    private route: Router
  ) {}
}
