import { Injectable } from '@angular/core';

import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  display: BehaviorSubject<'open' | 'close'> =  
  new BehaviorSubject<'open' | 'close'>('close');

  /* this function observes the changes of the modal */
  watch(): Observable<'open' | 'close'> {
    return this.display.asObservable();
  }

  /* this function is used to open the modal */
  open() {
    this.display.next('open');
  }

  /* this function is used to close the modal */
  close() {
    this.display.next('close');
  }
}
