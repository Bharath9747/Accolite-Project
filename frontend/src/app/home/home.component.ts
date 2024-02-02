import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FileUploadService } from '../services/file-upload.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  readmeFile!: File;
  javaZip!: File;
  cppZip!: File;
  pythonZip!: File;
  onUpload() {
    const formData = new FormData();
    if (this.readmeFile && (this.javaZip || this.cppZip || this.pythonZip)) {
      formData.append('readme', this.readmeFile);
      formData.append('java', this.javaZip);
      formData.append('cpp', this.cppZip);
      formData.append('python', this.pythonZip);

      this.fileUploadService.uploadFiles(formData).subscribe({
        next: (response) => {
          alert(response['result']);
        },
        error: (error) => {
          console.error(error);
        },
      });
    } else alert('Upload Data first');
  }
  onFileChange(event: any, fileType: string) {
    const file = event.target.files[0];
    if (file) {
      switch (fileType) {
        case 'readme':
          this.readmeFile = file;
          break;
        case 'java':
          this.javaZip = file;
          break;
        case 'cpp':
          this.cppZip = file;
          break;
        case 'python':
          this.pythonZip = file;
          break;
        default:
          break;
      }
    }
  }
  constructor(
    private fileUploadService: FileUploadService,
    private route: Router
  ) {}
  navigate() {
    this.route.navigate(['/editor']);
  }
}
