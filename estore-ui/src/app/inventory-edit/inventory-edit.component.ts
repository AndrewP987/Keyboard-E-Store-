import { Component } from '@angular/core';

import { KeyboardService } from '../keyboard.service';
import { Keyboard } from '../keyboard';
import { MessageService } from '../messages.service';
import { User } from '../user';
import { LocalService } from '../local.service';
import { UserService } from '../userservice.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inventory-edit',
  templateUrl: './inventory-edit.component.html',
  styleUrls: ['./inventory-edit.component.css']
})
export class InventoryEditComponent {
  constructor(
    private keyboardService: KeyboardService,
    private localService: LocalService,
    private userService: UserService,
    private router: Router
    ) { }

  keyboards: Keyboard[] = [];
  user!: User;

  ngOnInit(): void {
    this.getKeyboards();
    this.getUser(this.localService.getData("username")!);
    

    const productGrid = document.querySelector('.product-grid');
    if (productGrid) {
      productGrid.addEventListener('click', (event) => {
      const target = event.target as HTMLElement;
      if (target.classList.contains('circle')) {
        const color = target.classList[1];
        this.changeKeyColor(color);
       }
      });
    }
  }

  getUser(username: string): void{
    this.userService.getUser(username).subscribe(user => this.user = user);
  }

  getKeyboards(): void {
    this.keyboardService.getKeyboards().subscribe(keyboards => this.keyboards = keyboards);
  }

  delete(keyboard: Keyboard): void {
    this.keyboards = this.keyboards.filter(h => h !== keyboard);
    this.keyboardService.deleteKeyboard(keyboard.keyboardId).subscribe();
  }

  changeKeyColor(color: string): void {
    const keys = document.querySelectorAll('.key');
    keys.forEach((key) => {
      (key as HTMLElement).style.backgroundColor = color;
    });
  }
}
