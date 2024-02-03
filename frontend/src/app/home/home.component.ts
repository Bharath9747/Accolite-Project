import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FileUploadService } from '../services/file-upload.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  questionZip!: File;

  onFileChange(event: any) {
    const file = event.target.files[0];
    this.questionZip = file;
    const formData = new FormData();
    formData.append('question', this.questionZip);

    this.fileUploadService.uploadFiles(formData).subscribe({
      next: (response) => {
        alert(response['result']);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }
  constructor(
    private fileUploadService: FileUploadService,
    private route: Router
  ) {}
  navigate() {
    this.route.navigate(['/editor']);
  }
}
