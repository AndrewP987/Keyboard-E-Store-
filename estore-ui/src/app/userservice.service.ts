import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ComponentFactoryResolver, Injectable } from '@angular/core';
import { MessageService } from './messages.service';
import { Observable, catchError, filter, map, of, tap } from 'rxjs';
import { User } from './user';
import { Element } from '@angular/compiler';
import { Keyboard } from './keyboard';
import { LocalService } from './local.service';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userURL = 'http://localhost:8080/user'

  httpOptions = {
    headers: new HttpHeaders({'Content-Type':'application/json'})
  }

  constructor(private http: HttpClient, private messageService: MessageService,
    private localService: LocalService) {}

  private log(message: string){
    this.messageService.add(`KeyboardService: ${message}`);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      if (error.status === 401) {
        this.localService.saveData('error_status', '401')
        console.error(error);
        this.log(`${operation} failed: ${error.message}`);
      } else {
        this.localService.saveData('error_status', 'other')
        console.error(error);
        this.log(`${operation} failed: ${error.message}`);
      }
      return of(result as T);
    }
  }

  updateUser(user: User): Observable<any> {
    return this.http.put(this.userURL, user, this.httpOptions).pipe(
      tap(_ => this.log(`updated user userName=${user.username}`)),
      catchError(this.handleError<any>('updateUser'))
    );
  }

  addUser(username: string, password: string): Observable<User> {
    const user: User =
    {
      username: username,
      password: password,
      userOrderHistory: [],
      userCart: [],
      loginStatus: true
    }
    return this.http.post<User>(this.userURL, user, this.httpOptions).pipe(
      tap((newUser: User) => this.log(`added User w/ username=${newUser.username}&password=${newUser.password}`)),
      catchError(this.handleError<User>('addUser'))
    );
  }

  deleteAccount(username: string): Observable<User> {
    const url = `${this.userURL}/${username}`;
    return this.http.delete<User>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted deleteAccount username=${username}`)),
      catchError(this.handleError<User>('deleteAccount'))
    );
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.userURL)
      .pipe(
        tap(_ => this.log('fetched users')),
        catchError(this.handleError<User[]>('getUsers', []))
      );
  }

  getUserNo404<Data>(username: string): Observable<User>{
    const url = `${this.userURL}/${username}`;
    return this.http.get<User[]>(url).pipe(
      map(users => users[0]),
      tap(h => {
        const outcome = h ? 'fetched' : 'did not find';
        this.log(`${outcome} keyboard id=${username}`);
      }),
      catchError(this.handleError<User>(`getKeyboard id=${username}`))
    );
  }

  getUser(username: string): Observable<User> {
    const url = `${this.userURL}/${username}`;
    return this.http.get<User>(url).pipe(
      tap(_ => this.log(`fetched User username=${username}`)),
      catchError(this.handleError<User>(`getUser username=${username}`))
    );
  }

  addToCart(keyboard: Keyboard, username: string): Observable<User> {
    const url = `${this.userURL}/${username}/cart`;
    return this.http.put<User>(url, keyboard, this.httpOptions).pipe(
      tap(user => this.log('added to '+user.username)), //_ => this.log(`added ${item.type} to cart`)
      catchError(this.handleError<any>('addToCart'))
    );
  }

  removeFromCart(keyboard: Keyboard, username: string): Observable<User> {
    const url = `${this.userURL}/${username}/removeFromCart`;
    const httpOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      body: keyboard // include keyboard in the request body
    };
    return this.http.delete<User>(url, httpOptions).pipe(
      tap(_ => this.log(`removed from ${_.username}`)),
      catchError(this.handleError<any>('removeFromCart'))
    );
  }


  increaseQuantity(keyboard: Keyboard, username: string): Observable<User> {
    const url = `${this.userURL}/${username}/cart/increaseQuantity`;
    return this.http.put<User>(url, keyboard, this.httpOptions).pipe(
      tap(user => this.log('increased keyboard quantity to  '+ keyboard.quantity)),
      catchError(this.handleError<any>('addToCart'))
    );
  }

  decreaseQuantity(keyboard: Keyboard, username: string): Observable<User> {
    const url = `${this.userURL}/${username}/cart/decreaseQuantity`;
    return this.http.put<User>(url, keyboard, this.httpOptions).pipe(
      tap(user => this.log('increased keyboard quantity to  '+ keyboard.quantity)),
      catchError(this.handleError<any>('addToCart'))
    );
  }

  login(username: string, password: string ): Observable<User> {
    const url = `${this.userURL}/login/username=${username}&password=${password}`;
    return this.http.put<User>(url, username).pipe(
      tap((_) => this.log(`login ${username}, ${password}`)),
      catchError(this.handleError<any>('login'))
    );
  }

  logout(username: string, password: string ): Observable<User> {
    const url = `${this.userURL}/logout/username=${username}&password=${password}`;
    return this.http.put<User>(url, username).pipe(
      tap((_) => this.log(`login ${username}, ${password}`)),
      catchError(this.handleError<any>('login'))
    );
  }

  clearCart(username: string): Observable<User> {
    const url = `${this.userURL}/${username}/order`;
    return this.http.delete<User>(url).pipe(
      tap(_ => this.log(`cleared ${_.username}'s cart`)),
      catchError(this.handleError<any>('clearCart'))
    );
  }

  getEntireCart(username: string): Observable<Keyboard[]> {
    const url = `${this.userURL}/${username}/cart`;
    return this.http.get<User>(url).pipe(
      map(user => user.userCart),
      tap(_ => this.log(`retrieved ${username}'s cart`)),
      catchError(this.handleError<any>('retrieved user cart'))
    );
  }

  getOrderHistory(username: string): Observable<Keyboard[]> {
    const url = `${this.userURL}/${username}/orders`;
    return this.http.get<User>(url).pipe(
      map(user => user.userCart),
      tap(_ => this.log(`retrieved ${username}'s cart`)),
      catchError(this.handleError<any>('retrieved user cart'))
    );
  }

  pushToOrderHistory(username: string, keyboards: Keyboard[]): Observable<User> {
    const url = `${this.userURL}/${username}/orders/addToOrderHistory`;
    return this.http.post<User>(url, keyboards, this.httpOptions).pipe(
      tap((user: User) => this.log(`updated User w/ username=${user.username}`)),
      catchError(this.handleError<User>('pushToOrderHistory'))
    );
  }

  clearCartAfterPurchase(username: string): Observable<User> {
    const url = `${this.userURL}/${username}/clearCart`;
    return this.http.delete<User>(url, this.httpOptions).pipe(
      tap(_ => this.log(`cleared the cart of username=${username}`)),
      catchError(this.handleError<User>('deleteAccount'))
    );
  }




}
