import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { HomeComponent } from './home/home.component';
import { TestComponent } from './test/test.component';
import { DummyComponent } from './dummy/dummy.component';
import { CreateTestComponent } from './create-test/create-test.component';
import { AssignTestComponent } from './assign-test/assign-test.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'create-test',
    component: CreateTestComponent,
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
    component: AssignTestComponent,
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
