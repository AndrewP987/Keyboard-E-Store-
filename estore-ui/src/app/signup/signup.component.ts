
import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalService } from '../local.service';
import { MessageService } from '../messages.service';
import { User } from '../user';
import { UserService } from '../userservice.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  username: string = '';
  password: string = '';

  constructor(
    private userService: UserService,
    private _router: Router,
    private messageService: MessageService,
    private localService: LocalService
  ) {}

  ngOnInit(): void {}

  signup(): void {
    if (this.username != '' && this.password != '') {
      console.log(this.username);
      console.log(this.password);
      this.userService
        .addUser(this.username, this.password)
        .subscribe((data) => this.handleSignupResponse(data));

      this.localService.saveData('username', this.username);
      this.localService.saveData('password', this.password);
    } else {
      alert("Please enter a username and password to sign up")
    }
  }

  handleSignupResponse(user: User) {
    if (user) {
      this.messageService.add('UserService: Signed up ');
      this.userService.getUsers();
      this.localService.saveData('username', user.username);
      this.localService.saveData('password', user.password);
      if(user.username === "admin"){
        this._router.navigate(["/keyboards"]);
      }else{
        this._router.navigate(["/keyboards"])
      }
    } else {
      alert("Username already exists. Please enter a different username.")
    }
  }

  userUsernameCreation(username: string) {
    this.username = username.replace(/[^a-zA-Z0-9 ]/g, '').substring(0, 14);
  }

  userPassCreation(password: string) {
    this.password = password.replace(/[^a-zA-Z0-9 ]/g, '').substring(0, 14);
  }

}
