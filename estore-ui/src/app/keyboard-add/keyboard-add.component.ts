import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Keyboard } from '../keyboard';
import { KeyboardService } from '../keyboard.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-keyboard-add',
  templateUrl: './keyboard-add.component.html',
  styleUrls: ['./keyboard-add.component.css']
})

export class KeyboardAddComponent {

  keyboard: Keyboard | undefined;
  keyboards: Keyboard[] = [];

  public selectedColor: string = '';
  public keyCapColor: string = '';
  public keyBoardSwitchColor: string = '';

  constructor(
    private route: ActivatedRoute,
    private keyboardService: KeyboardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getKeyboard();
  }

  getKeyboard(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.keyboardService.getKeyboard(id)
      .subscribe(keyboard => this.keyboard = keyboard);
  }

  goBack(): void {
    const confirmNavigation = confirm("Are you done creating the listing?");
    if (confirmNavigation) {
      this.router.navigate(['/keyboards'])
    }
  }

  save(): void {
    if (this.keyboard) {
      this.keyboardService.updateKeyboard(this.keyboard)
        .subscribe(() => this.goBack());
    }
  }

  add(name: string, price: string, quantity: string): void {

    name = name.trim();
    let numPrice = Number(price);
    let numQuantity = Number(quantity);

    // Validate price
    if (numPrice <= 0 || numPrice >= 300) {
      alert('Price should be between 0 and 300');
      return;
    }

    // Validate quantity
    if (numQuantity <= 0 || numQuantity >= 400) {
      alert('Quantity should be between 0 and 400');
      return;
    }

    // Validate name
    if (!/^[A-Za-z]{2,10}$/.test(name)) {
      alert('Name should consist of only alphabetical characters and be between length 2 and length 14');
      return;
    }

    this.keyboardService.addKeyboard({ keyboardName: name, price: numPrice, quantity: numQuantity } as Keyboard)
      .subscribe(keyboard => {
        this.keyboards.push(keyboard);
      });

    this.goBack();
  }

}
