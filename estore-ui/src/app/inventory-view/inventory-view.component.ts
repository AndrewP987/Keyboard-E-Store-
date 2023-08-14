import { Component, OnInit } from '@angular/core';

import { KeyboardService } from '../keyboard.service';
import { Keyboard } from '../keyboard';
import { MessageService } from '../messages.service';
import { User } from '../user';
import { LocalService } from '../local.service';
import { UserService } from '../userservice.service';
import { Size } from '../size';
import { SwitchColor } from '../switch-color';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inventory-view',
  templateUrl: './inventory-view.component.html',
  styleUrls: ['./inventory-view.component.css'],
})
export class InventoryViewComponent implements OnInit {
  constructor(
    private keyboardService: KeyboardService,
    private localService: LocalService,
    private userService: UserService,
    private router: Router
  ) {}

  keyboards: Keyboard[] = [];
  fromPrice: number = 0;
  toPrice: number = 0;
  user!: User;
  ngOnInit(): void {
    this.getUser(this.localService.getData('username')!);
    this.getKeyboards();
    const productGrid = document.querySelector('.product-grid');
    if (productGrid) {
      productGrid.addEventListener('click', (event) => {
        const target = event.target as HTMLElement;
      });
    }
  }

  getUser(username: string): void {
    this.userService.getUser(username).subscribe((user) => (this.user = user));
  }

  getKeyboards(): void {
    this.keyboardService.getKeyboards().subscribe((keyboards) => (this.keyboards = keyboards));
  }

  addToCart(keyboard: Keyboard): void {
    if (keyboard && keyboard.quantity >= 1) {
      const keyboardDupe = {
        price: keyboard.price,
        quantity: 1,
        size: keyboard.size,
        switchColor: keyboard.switchColor,
        keyboardName: keyboard.keyboardName,
        keyboardId: keyboard.keyboardId
    }
      this.userService.addToCart(keyboardDupe, this.user.username).subscribe();
    }
  }

  delete(keyboard: Keyboard): void {
    this.keyboards = this.keyboards.filter((h) => h !== keyboard);
    this.keyboardService.deleteKeyboard(keyboard.keyboardId).subscribe();
  }

  changeFromPrice(fromPrice: number) {
    this.fromPrice = fromPrice;
  }

  changeToPrice(toPrice: number) {
    this.toPrice = toPrice;
  }

  getFiltered(fromPrice: string, toPrice: string) {
    const from = Number(fromPrice);
    const to = Number(toPrice);

    if(from < 1 || to < 1){
      alert("Can't be less than 1");
      return;
    }

    if(to > 999 || from > 999){
      alert("Can't be greater than 999");
      return;
    }

    if (from > to) {
      alert('From price must be less than To price.');
      return;
    }

    this.keyboardService.filterKeyboards(fromPrice, toPrice).subscribe((keyboards) => {
      this.keyboards = keyboards;
      if (this.keyboards.length === 0) {
        alert('No keyboards match the selected price range.');
      }
    });
  }

}
