import { Component, ElementRef, ViewChild } from '@angular/core';
import { Remarkable } from 'remarkable';
import { SharedService } from '../services/shared.service';

@Component({
  selector: 'app-markdown',
  templateUrl: './markdown.component.html',
  styleUrl: './markdown.component.scss',
})
export class MarkdownComponent {
  @ViewChild('markdownContainer') markdownContainer!: ElementRef;
  constructor(private sharedService: SharedService) {}
  ngAfterViewInit() {
    this.sharedService.getData$().subscribe({
      next: (response) => {
        var md = new Remarkable();
        let markdownContent: string = md.render(response);

        if (this.markdownContainer) {
          this.markdownContainer.nativeElement.innerHTML = markdownContent;
        }
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
