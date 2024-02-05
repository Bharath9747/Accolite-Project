import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { Remarkable } from 'remarkable';

@Component({
  selector: 'app-dummy',
  templateUrl: './dummy.component.html',
  styleUrl: './dummy.component.scss',
})
export class DummyComponent implements AfterViewInit {
  @ViewChild('markdownContainer') markdownContainer!: ElementRef;
  selectedLanguage: string = 'markdown'; // Assuming you have this variable defined

  ngAfterViewInit() {
    var md = new Remarkable();
    let markdownContent: string = md.render('');
    if (this.markdownContainer) {
      // Directly set the innerHTML of the element
      this.markdownContainer.nativeElement.innerHTML = markdownContent;
    }
  }
  // ngAfterViewInit() {
  //   // Access the element in the ngAfterViewInit lifecycle hook
  //   if (this.markdownContainer) {
  //     var md = new Remarkable();
  //     const htmlResult: string = md.render('# Hello, Remarkable!');
  //     this.markdownContainer.nativeElement.innerHtml = htmlResult;
  //     console.log(this.markdownContainer);
  //   }
  // }
}
