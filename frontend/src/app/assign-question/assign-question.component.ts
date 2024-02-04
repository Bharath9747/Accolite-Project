import { Component, OnInit } from '@angular/core';
import { QuestionService } from '../services/question.service';
import { Question } from '../model/question.model.';

@Component({
  selector: 'app-assign-question',
  templateUrl: './assign-question.component.html',
  styleUrl: './assign-question.component.scss',
})
export class AssignQuestionComponent implements OnInit {
  constructor(private questionService: QuestionService) {}
  questions: Question[] = [];
  selectedQuestions: Question[] = [];

  ngOnInit(): void {
    this.questionService.getAllQuestion().subscribe({
      next: (response) => {
        this.questions = response;
      },
      error: (error) => {
        alert(error);
      },
    });
  }
  onTableRowSelect(event: any) {
    const clickedRow = event.target.closest('tr');
    if (clickedRow) {
      let index: number = clickedRow.cells[0].textContent;
      let selectedQuestion: Question = this.questions[index - 1];

      let question: Question = this.selectedQuestions.filter(
        (x) => x == selectedQuestion
      )[0];

      if (question) {
        this.selectedQuestions = this.selectedQuestions.filter(
          (x) => x != selectedQuestion
        );
      } else {
        this.selectedQuestions.push(selectedQuestion);
      }
    }
  }
  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (this.selectedQuestions.length != 0) {
      this.questionService.uploadData(file, this.selectedQuestions).subscribe({
        next: (response) => {
          alert(response);
        },
        error: (error) => {
          console.log(error);
        },
      });
    } else alert('select atleast one question');
  }
}
