import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';
import { Keyboard } from '../keyboard';
import { KeyboardService } from '../keyboard.service';
import { LocalService } from '../local.service';


@Component({
  selector: 'app-keyboard-search',
  templateUrl: './keyboard-search.component.html',
  styleUrls: [ './keyboard-search.component.css' ]
})
export class KeyboardSearchComponent implements OnInit {

  constructor(
    private keyboardService: KeyboardService,
    private localService: LocalService
    ) {}

  keyboards$!: Observable<Keyboard[]>;
  private searchTerms = new Subject<string>();

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.keyboards$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.keyboardService.searchKeyboards(term)),
    );
  }
}

