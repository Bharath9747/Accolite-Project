import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { CodeEditorComponent } from './code-editor/code-editor.component';
import { HomeComponent } from './home/home.component';
import { MonacoEditorModule } from 'ngx-monaco-editor-v2';
import { FormsModule } from '@angular/forms';

import { TestComponent } from './test/test.component';
import { DummyComponent } from './dummy/dummy.component';
import { MarkdownComponent } from './markdown/markdown.component';
import { CreateTestComponent } from './create-test/create-test.component';
import { AssignTestComponent } from './assign-test/assign-test.component';

@NgModule({
  declarations: [
    AppComponent,
    CodeEditorComponent,
    HomeComponent,

    TestComponent,
    DummyComponent,
    MarkdownComponent,
    CreateTestComponent,
    AssignTestComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MonacoEditorModule.forRoot(),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
