import { Injectable } from '@angular/core';
import { Keyboard } from './keyboard';
import { Observable, of } from 'rxjs';
import { MessageService } from './messages.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClientModule } from '@angular/common/http';
import { Size } from './size';
import { SwitchColor } from './switch-color';

@Injectable({
  providedIn: 'root'
})
export class KeyboardService{
  private keyboardURL = 'http://localhost:8080/keyboards';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type':'application/json'})
  }

  constructor(private http: HttpClient, private messageService: MessageService) {}

  private log(message: string){
    this.messageService.add(`KeyboardService: ${message}`);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      this.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    }
  }

  searchKeyboards(term: string): Observable<Keyboard[]> {
    if(!term.trim()){
      return of([]);
    }
    return this.http.get<Keyboard[]>(`${this.keyboardURL}/?name=${term}`).pipe(
      tap(x => x.length ?
        this.log(`found keyboards matching "${term}"`):
        this.log(`no keyboards matching "${term}"`)),
      catchError(this.handleError<Keyboard[]>('searchKeyboards', []))
    );
  }

  deleteKeyboard(id: number): Observable<Keyboard> {
    const url = `${this.keyboardURL}/${id}`;

    return this.http.delete<Keyboard>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted keyboard id=${id}`)),
      catchError(this.handleError<Keyboard>('deleteKeyboard'))
    );
  }


  addKeyboard(keyboard: Keyboard): Observable<Keyboard> {
    return this.http.post<Keyboard>(this.keyboardURL, keyboard, this.httpOptions).pipe(
      tap((newkeyboard: Keyboard) => this.log(`added keyboard w/ id=${newkeyboard.keyboardId}`)),
      catchError(this.handleError<Keyboard>('addKeyboard'))
    );
  }


  updateKeyboard(keyboard: Keyboard): Observable<any> {
    return this.http.put(this.keyboardURL, keyboard, this.httpOptions).pipe(
      tap(_ => this.log(`updated keyboard id=${keyboard.keyboardId}`)),
      catchError(this.handleError<any>('updatedKeyboard'))
    );
  }


  getKeyboards(): Observable<Keyboard[]> {
    return this.http.get<Keyboard[]>(this.keyboardURL)
      .pipe(
        tap(_ => this.log('fetched keyboards')),
        catchError(this.handleError<Keyboard[]>('getKeyboards', []))
      );
  }

  getKeyboardNo404<Data>(id: number): Observable<Keyboard>{
    const url = `${this.keyboardURL}/${id}`;
    return this.http.get<Keyboard[]>(url).pipe(
      map(keyboards => keyboards[0]),
      tap(h => {
        const outcome = h ? 'fetched' : 'did not find';
        this.log(`${outcome} keyboard id=${id}`);
      }),
      catchError(this.handleError<Keyboard>(`getKeyboard id=${id}`))
    );
  }

  getKeyboard(id: number): Observable<Keyboard | undefined> {
    const url = `${this.keyboardURL}/${id}`;
    return this.http.get<Keyboard>(url).pipe(
      tap(_ => this.log(`fetched keyboard id=${id}`)),
      catchError(this.handleError<Keyboard>(`getKeyboard id=${id}`))
    );
  }

  filterKeyboards(fromPrice: string, toPrice: string): Observable<Keyboard[]> {
    const url = `${this.keyboardURL}/filter/fromPrice=${fromPrice}&toPrice=${toPrice}`;

    return this.http.get<Keyboard[]>(url).pipe(
      tap(_ => this.log(`filtered keyboards by fromPrice=${fromPrice}, toPrice=${toPrice}`)),
      catchError(this.handleError<Keyboard[]>('filterKeyboards', []))
    );
  }


}
