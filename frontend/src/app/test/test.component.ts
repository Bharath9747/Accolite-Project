import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterConfigOptions } from '@angular/router';
import { Question } from '../model/question.model.';
import { QuestionService } from '../services/question.service';
import { SharedService } from '../services/shared.service';
import { ExplorerItem } from '../model/explorer-item.model';
import { ExplorerService } from '../services/explorer.service';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrl: './test.component.scss',
})
export class TestComponent {
  fileNames: ExplorerItem[] = [];
  type: string = '';
  explorerData: ExplorerItem[] = [];
  selectedLanguage: string = '';
  hideArea: boolean = true;
  id!: number;
  email: string = '';
  questions: Question[] = [];
  code: string = '';
  hideNav = true;
  hideConsole = false;
  status: string = 'close';
  statusConsole: string = 'open';

  onClose() {
    this.hideNav = !this.hideNav;
    this.status = this.hideNav ? 'close' : 'open';
  }
  onCloseConsole() {
    this.hideConsole = !this.hideConsole;
    this.statusConsole = this.hideConsole ? 'close' : 'open';
  }

  editorOptions = {
    theme: 'vs-dark',
    language: 'cpp',
    fontSize: 18,
    minimap: { enabled: false },
    scrollBeyondLastLine: false,
    wordWrap: true,
    readOnly: false,
    automaticLayout: true,
  };
  constructor(
    private questionService: QuestionService,
    private explorerService: ExplorerService,
    private sharedService: SharedService
  ) {}

  onChange(index: number) {
    let question: Question = this.questions[index];
    this.hideNav = true;
    this.status = 'close';
    if (question.id) this.id = question.id;
    this.explorerService
      .getExplorerData(this.id, 'Test', this.email)
      .subscribe({
        next: (data) => {
          this.explorerData = data;
          if (this.explorerData.length === 2) this.type = 'DB';
          else this.type = 'CD';

          for (let index = 0; index < this.explorerData.length; index++) {
            const element = this.explorerData[index];
            if (!element.children && element.name.includes('.md'))
              this.handleItemClick(element);
          }
        },
        error: (error) => {
          console.error('Error fetching explorer data:', error);
        },
      });
  }
  onStart() {
    this.questionService.getQuestionByEmail(this.email).subscribe({
      next: (response) => {
        this.hideArea = false;
        this.questions = response;
        this.questions = this.questions.sort((a, b) => {
          if (a.id !== undefined && b.id !== undefined) {
            return a.id - b.id;
          } else if (a.id !== undefined) {
            return -1;
          } else if (b.id !== undefined) {
            return 1;
          } else {
            return 0;
          }
        });
        this.onChange(0);
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
  changeCode() {}
  runCode() {
    this.questionService
      .runCode({
        id: this.id,
        code: this.code,
        language: this.selectedLanguage,
      })
      .subscribe({
        next: (response) => {
          console.log(response);
        },
        error: (error) => {
          console.log(error);
        },
      });
  }
  handleItemClick(item: ExplorerItem) {
    if (item.type === 'file') {
      this.explorerService.getFileContent(item.absolutePath).subscribe({
        next: (data) => {
          this.code = data.content;

          if (
            item.name.includes('.cpp') ||
            item.name.includes('.java') ||
            item.name.includes('.py') ||
            item.name.includes('.sql') ||
            item.name.includes('.txt')
          ) {
            this.selectedLanguage = this.getLanguageFromFileName(item.name);
            this.editorOptions = {
              theme: 'vs-dark',
              language: this.selectedLanguage.toLowerCase(),
              fontSize: 18,
              minimap: { enabled: false },
              scrollBeyondLastLine: false,
              wordWrap: true,
              readOnly: false,
              automaticLayout: true,
            };
          } else if (item.name.includes('.md')) {
            this.selectedLanguage = 'markdown';
            this.sharedService.setData(this.code);
          }
        },
        error: (error) => {
          console.error('Error fetching file content:', error);
        },
      });
    } else {
      item.expanded = !item.expanded;
    }
  }
  getLanguageFromFileName(fileName: string): string {
    if (fileName.includes('.cpp')) {
      return 'Cpp';
    } else if (fileName.includes('.java')) {
      return 'Java';
    } else if (fileName.includes('.py')) {
      return 'Python';
    } else {
      return '';
    }
  }
}
