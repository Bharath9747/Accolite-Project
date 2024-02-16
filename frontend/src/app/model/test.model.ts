import { Question } from './question.model.';

export interface Test {
  id: number;
  questions: Question[];
}
