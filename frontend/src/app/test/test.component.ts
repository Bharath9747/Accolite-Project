import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterConfigOptions } from '@angular/router';
import { Question } from '../model/question.model.';
import { QuestionService } from '../services/question.service';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrl: './test.component.scss',
})
export class TestComponent {
  hideArea: boolean = true;

  email: string = '';
  questions: Question[] = [];
  constructor(private questionService: QuestionService) {}

  onStart() {
    this.questionService.getQuestionByEmail(this.email).subscribe({
      next: (response) => {
        this.hideArea = false;
        this.questions = response;
      },
      error: (error) => {
        console.log(error);
      },
    });
  }
}
