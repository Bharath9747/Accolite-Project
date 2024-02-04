import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { HomeComponent } from './home/home.component';
import { AssignQuestionComponent } from './assign-question/assign-question.component';
import { TestComponent } from './test/test.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'editor',
    component: CodeEditorComponent,
  },
  {
    path: 'test',
    component: TestComponent,
  },

  {
    path: 'assign',
    component: AssignQuestionComponent,
  },
  {
    path: 'editor/:id',
    component: CodeEditorComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
