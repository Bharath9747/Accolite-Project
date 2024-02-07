import { Component } from '@angular/core';
import { Question } from '../model/question.model.';
import { QuestionService } from '../services/question.service';

@Component({
  selector: 'app-create-test',
  templateUrl: './create-test.component.html',
  styleUrl: './create-test.component.scss',
})
export class CreateTestComponent {
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
  onSubmit() {
    if (this.selectedQuestions && this.selectedQuestions.length != 0) {
      this.questionService.createTest(this.selectedQuestions).subscribe({
        next: (response) => {
          alert(response['result']);
        },
        error: (err) => {
          console.log(err);
        },
      });
    } else alert('Select a Question First');
  }
}
