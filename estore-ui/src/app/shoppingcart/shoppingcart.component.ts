import { Component, OnInit} from '@angular/core';
import { Keyboard } from '../keyboard';
import { CurrencyPipe } from '@angular/common';
import { User } from '../user';
import { UserService } from '../userservice.service';
import { KeyboardService } from '../keyboard.service';
import { Observable, map, switchMap } from 'rxjs';
import { LocalService } from '../local.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-shoppingcart',
  templateUrl: './shoppingcart.component.html',
  styleUrls: ['./shoppingcart.component.css'],
  providers: [CurrencyPipe]
})
export class ShoppingcartComponent implements OnInit {
  showCheckoutForm = false;
  user!: User;

  alerts: string[] = [];

  constructor(private userService: UserService,
              private keyboardSerivce: KeyboardService,
              private router: Router,
              private localService: LocalService) { }


  ngOnInit(): void {
    const username = this.localService.getData("username");
    if (username) {
      this.getUser(username);
    }
  }

  getUser(username: string): void{
    this.userService.getUser(username).subscribe(user => this.user = user);
  }

  getTotalPrice(): number {
    let total = 0;
    if (this.user?.userCart) {
      for (const keyboard of this.user?.userCart) {
        total += keyboard.price * keyboard.quantity;
      }
    }
    return total;
  }

  placeOrder() {
    this.userService.pushToOrderHistory(this.user?.username, this.user?.userCart).subscribe();
    this.userService.clearCartAfterPurchase(this.user.username).subscribe();
    this.router.navigate(["/keyboards"])
  }


  removeFromCart(keyboard: Keyboard): void {
    if (keyboard && this.user.username && 1 <= keyboard.quantity) {
      const item = {
        size: keyboard.size,
        switchColor: keyboard.switchColor,
        keyboardId: keyboard.keyboardId,
        keyboardName: keyboard.keyboardName,
        price: keyboard.price,
        quantity: keyboard.quantity,
      };
      this.userService.removeFromCart(item, this.user.username).subscribe();
      location.reload();
    }
  }

  increaseQuantity(keyboard: Keyboard) {
    const keyboardQuantity = this.keyboardSerivce.getKeyboard(keyboard.keyboardId);
    this.userService.increaseQuantity(keyboard, this.user.username).subscribe();
    keyboard.quantity += 1;
  }

  decreaseQuantity(keyboard: Keyboard) {
    if (keyboard.quantity > 1) {
      const keyboardQuantity = this.keyboardSerivce.getKeyboard(keyboard.keyboardId);
      this.userService.decreaseQuantity(keyboard, this.user.username).subscribe();
      keyboard.quantity -= 1;
    }
  }

}
