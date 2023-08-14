import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalService } from '../local.service';
import { MessageService } from '../messages.service';
import { User } from '../user';
import { UserService } from '../userservice.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  ngOnInit(): void {}
  constructor(
    private userService: UserService,
    private _router: Router,
    private messageService: MessageService,
    private localService: LocalService
  ) {}


  username: string = '';
  password: string = '';

  login(): void {
    if (this.username != '' && this.password != '') {

      this.userService
        .login(this.username, this.password)
        .subscribe((data) => this.handleLoginResponse(data));

      this.localService.saveData('username', this.username);
      this.localService.saveData('password', this.password);
    } else {
      alert('Please enter a username and password');
    }
  }

  handleLoginResponse(user: User) {
    if (user) {
      this.localService.saveData('username', this.username);
      this.localService.saveData('password', this.password);
      localStorage.setItem('isLoggedIn', 'true');
      this._router.navigate(["/keyboards"]);
    } else {
      if (this.localService.getData('error_status') == '401') {
        alert("Incorrect password for this user. Please try again")
      } else {
        alert('Account does not exist, please sign up on the right');
      }
    }
  }

  userLoginChange(username: string) {
    this.username = username.replace(/[^a-zA-Z0-9 ]/g, '').substring(0, 14);
  }

  userPassChange(password: string) {
    this.password = password.replace(/[^a-zA-Z0-9 ]/g, '').substring(0, 14);
  }

}
