import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { QuestionService } from '../services/question.service';
import { Question } from '../model/question.model.';
import { Template } from '../model/template.model';

@Component({
  selector: 'app-code-editor',
  templateUrl: './code-editor.component.html',
  styleUrl: './code-editor.component.scss',
})
export class CodeEditorComponent implements OnInit {
  question!: Question;
  templates: Template[] = [];
  languages: string[] = [];
  selectedLanguage: string = 'Cpp';
  ngOnInit(): void {
    this.questionService.getQuestion(1).subscribe({
      next: (response) => {
        this.question = response;
        if (this.question.templates) {
          this.templates = this.question.templates;
          this.changeCode();
          for (let index = 0; index < this.templates.length; index++) {
            const element = this.templates[index];
            this.languages.push(element.language);
          }
        }
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  editorOptions = {
    theme: 'vs-dark',
    language: 'cpp',
    minimap: { enabled: false },
  };
  code: string = '';
  constructor(private questionService: QuestionService) {}
  displayQuestion() {
    this.editorOptions.language = 'markdown';

    if (this.question.description) {
      this.code = this.question.description;
    }
    console.log(this.editorOptions);

    this.selectedLanguage = '';
  }
  changeCode() {
    for (let index = 0; index < this.templates.length; index++) {
      const element = this.templates[index];
      if (element.language === this.selectedLanguage) {
        this.editorOptions.language = this.selectedLanguage.toLowerCase();
        this.code = element.code;
        break;
      }
    }
  }
}
