import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Keyboard } from '../keyboard';
import { KeyboardService } from '../keyboard.service';
import { User } from '../user';
import { Router } from '@angular/router';
import { UserService } from '../userservice.service';
import { Size } from '../size'
import { SwitchColor } from '../switch-color';
import { LocalService } from '../local.service';
@Component({
  selector: 'app-keyboard-detail',
  templateUrl: './keyboard-detail.component.html',
  styleUrls: [ './keyboard-detail.component.css' ]
})
export class KeyboardDetailComponent implements OnInit {

  keyboard: Keyboard | undefined;
  duplicateKeyboard: Keyboard | undefined;
  user!: User;

  size: string = '';
  keyBoardSwitchColor: string = '';
  price: number = 0;
  quantity: number = 0;
  id: number = 0;

  constructor(
    private route: ActivatedRoute,
    private keyboardService: KeyboardService,
    private userService: UserService,
    private localService: LocalService,
    private router: Router

  ) {}

  ngOnInit(): void {
    this.getUser(this.localService.getData("username")!);
    if(this.user == null) {
      this.router.navigate(["/home"]);
    }
    this.getKeyboard();
    this.duplicateKeyboard = {
      size: this.keyboard!.size,
      switchColor: this.keyboard!.switchColor,
      price: this.keyboard!.price,
      quantity: this.keyboard!.quantity,
      keyboardName: this.keyboard!.keyboardName,
      keyboardId: this.keyboard!.keyboardId
    }
    console.log("this.duplicate keyboard" + this.duplicateKeyboard)
    //set duplicateKeyboard

  }

  getUser(username: string): void{
    this.userService.getUser(username).subscribe(user => this.user = user);
    console.log("User: " + this.user)

  }

  getKeyboard(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.keyboardService.getKeyboard(id).subscribe(keyboard => {
      this.keyboard = keyboard
    });

    // duplicate the keyboard so we aren't editing the json inventory file
    this.keyboard = {
      size: this.localService.getData("size") as any as Size,
      switchColor: this.localService.getData("switchColor")as any as SwitchColor,
      price: Number(this.localService.getData("keyboardPrice")),
      quantity: Number(this.localService.getData("keyboardQuantity")),
      keyboardName: this.localService.getData("keyboardName") + "",
      keyboardId: Number(this.localService.getData("keyboardId"))
    }

  }

  checkValuesOfBoth(){
    if(this.duplicateKeyboard!.keyboardName != this.keyboard!.keyboardName){
      this.duplicateKeyboard!.keyboardName = this.keyboard!.keyboardName;
    }
    if(this.duplicateKeyboard!.keyboardId != this.keyboard!.keyboardId){
      this.duplicateKeyboard!.keyboardName = this.keyboard!.keyboardName;
    }
    if(this.duplicateKeyboard!.price != this.keyboard!.price){
      this.duplicateKeyboard!.price = this.keyboard!.price;
    }
    if(this.duplicateKeyboard!.quantity != this.keyboard!.quantity){
      this.duplicateKeyboard!.quantity = this.keyboard!.quantity;
    }

    console.log("call inside check " + this.duplicateKeyboard);
  }


  changeSelectedSize(size: string){
    if(size == "small") {
      this.duplicateKeyboard!.size = Size.SIXTY
    } else if (size == "medium") {
      this.duplicateKeyboard!.size= Size.TKL
    } else if (size == "large") {
      this.duplicateKeyboard!.size = Size.FULL
    }
    this.checkValuesOfBoth();

    console.log(this.duplicateKeyboard)

  }


  changeKeyboardSwitchColor(keyboardSwitchColor: string){
    if(keyboardSwitchColor == "brown") {
      this.duplicateKeyboard!.switchColor = SwitchColor.BROWN;
    }else if(keyboardSwitchColor == "red") {
      this.duplicateKeyboard!.switchColor = SwitchColor.RED;
    } else if (keyboardSwitchColor == "blue") {
      this.duplicateKeyboard!.switchColor = SwitchColor.BLUE;
    }

    this.checkValuesOfBoth();

    console.log(this.duplicateKeyboard)
  }

  // changeQuantity

  addToCart(): void {
    if (this.duplicateKeyboard && this.keyBoardSwitchColor != "" && this.duplicateKeyboard.size) {
      this.duplicateKeyboard.quantity = 1;
      console.log(this.duplicateKeyboard);
      console.log(this.user.username)
      this.userService.addToCart(this.duplicateKeyboard, this.user.username).subscribe();
      alert(this.duplicateKeyboard.keyboardName + " has been added to the cart.")
    } else {
      alert("Cannot add to cart without a customization")
    }
  }

  goBack(): void {
    this.router.navigate(['/keyboards'])
  }

}
