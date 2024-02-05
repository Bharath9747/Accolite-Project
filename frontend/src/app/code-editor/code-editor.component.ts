import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { QuestionService } from '../services/question.service';
import { ActivatedRoute } from '@angular/router';
import { ExplorerItem } from '../model/explorer-item.model';
import { ExplorerService } from '../services/explorer.service';
import { Remarkable } from 'remarkable';
import { SharedService } from '../services/shared.service';
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
  type: string = '';

  ngOnInit(): void {
    this.router.queryParams.subscribe((params) => {
      this.id = params['id'];
    });
    this.explorerService.getExplorerData(this.id).subscribe({
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
  code: string = '';

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
    private router: ActivatedRoute,
    private explorerService: ExplorerService,
    private sharedService: SharedService
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
          this.code = data.content;

          if (
            item.name.includes('.cpp') ||
            item.name.includes('.java') ||
            item.name.includes('.py') ||
            item.name.includes('.sql')
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
