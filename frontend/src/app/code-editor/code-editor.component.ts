import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { QuestionService } from '../services/question.service';
import { ActivatedRoute } from '@angular/router';
import { ExplorerItem } from '../model/explorer-item.model';
import { ExplorerService } from '../services/explorer.service';

@Component({
  selector: 'app-code-editor',
  templateUrl: './code-editor.component.html',
  styleUrl: './code-editor.component.scss',
})
export class CodeEditorComponent implements OnInit {
  fileNames: ExplorerItem[] = [];
  explorerData: ExplorerItem[] = [];
  selectedLanguage: string = '';
  id!: number;
  ngOnInit(): void {
    this.router.queryParams.subscribe((params) => {
      this.id = params['id'];
    });
    this.explorerService.getExplorerData(this.id).subscribe({
      next: (data) => {
        this.explorerData = data;
        for (let index = 0; index < this.explorerData.length; index++) {
          const element = this.explorerData[index];
          if (!element.children) this.handleItemClick(element);
        }
      },
      error: (error) => {
        console.error('Error fetching explorer data:', error);
      },
    });
  }

  editorOptions = {
    theme: 'vs-dark',
    language: 'cpp',
    fontSize: 18,
    minimap: { enabled: false },
    wordWrap: true,
    readOnly: true,
  };
  code: string = '';
  constructor(
    private questionService: QuestionService,
    private router: ActivatedRoute,
    private explorerService: ExplorerService
  ) {}
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
          if (
            item.name.includes('.cpp') ||
            item.name.includes('.java') ||
            item.name.includes('.py')
          ) {
            this.selectedLanguage = this.getLanguageFromFileName(item.name);
            this.editorOptions = {
              theme: 'vs-dark',
              language: this.selectedLanguage.toLowerCase(),
              fontSize: 18,
              minimap: { enabled: false },
              wordWrap: true,
              readOnly: false,
            };
          } else if (item.name.includes('.md')) {
            this.selectedLanguage = 'markdown';
            this.editorOptions = {
              theme: 'vs-dark',
              language: this.selectedLanguage,
              fontSize: 18,
              minimap: { enabled: false },
              wordWrap: true,
              readOnly: true,
            };
          }

          this.code = data.content;
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
