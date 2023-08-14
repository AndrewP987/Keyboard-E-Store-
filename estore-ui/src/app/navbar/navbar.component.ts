
import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';
import { Keyboard } from '../keyboard';
import { KeyboardService } from '../keyboard.service';
import { Router } from '@angular/router';
import { User } from '../user';
import { UserService } from '../userservice.service';
import { LocalService } from '../local.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  title = 'Keyboard E-Store';
  user!: User;
  id: number = 1;


  keyboards$!: Observable<Keyboard[]>;
  private searchTerms = new Subject<string>();

  constructor(private localService: LocalService, private keyboardService: KeyboardService, private router: Router, private userService: UserService){}

  search(term: string): void{
    this.searchTerms.next(term);
  }

  ngOnInit(): void{
    this.keyboards$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) => this.keyboardService.searchKeyboards(term)),
    )
    this.getUser(this.localService.getData("username")!);

  }

  getUser(username: string): void{
    this.userService.getUser(username).subscribe(user => this.user = user);
  }

  logout(){
    this.userService.logout(this.user.username, this.user.password).subscribe(user => this.user = user);
    this.localService.clearData();
    localStorage.removeItem('isLoggedIn');
    this.router.navigate([""]);
  }

  openInventory(){
    this.router.navigate(["/keyboards"]);
  }

  openOrderHistory(){
    this.router.navigate(["/orders"]);
  }
  openCart(){
    this.router.navigate(["/cart"]);
  }

  displayOrderHistory(): void{
    this.router.navigate(["/orders"]);
  }

  inventoryEdit(): void{
    this.router.navigate(["/inventory-edit"]);
  }

}
