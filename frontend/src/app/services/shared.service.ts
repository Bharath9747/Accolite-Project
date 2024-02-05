import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SharedService {
  private code: ReplaySubject<string> = new ReplaySubject<string>(1);

  setData(data: string): void {
    this.code.next(data);
  }

  getData$(): Observable<string> {
    return this.code.asObservable();
  }
}
