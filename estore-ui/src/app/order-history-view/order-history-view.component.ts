import { Component } from '@angular/core';
import { LocalService } from '../local.service';
import { User } from '../user';
import { UserService } from '../userservice.service';
import { Keyboard } from '../keyboard';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-history-view',
  templateUrl: './order-history-view.component.html',
  styleUrls: ['./order-history-view.component.css']
})
export class OrderHistoryViewComponent {

  constructor(
    private localService: LocalService,
    private userService: UserService,
    private router: Router
  ){}

  user!: User;

  ngOnInit(): void {
    this.getUser(this.localService.getData("username")!);

  }

  getUser(username: string): void{
    this.userService.getUser(username).subscribe(user => this.user = user);
  }




}
