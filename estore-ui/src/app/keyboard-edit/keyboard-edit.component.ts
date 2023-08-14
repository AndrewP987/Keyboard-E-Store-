import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Keyboard } from '../keyboard';
import { KeyboardService } from '../keyboard.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-keyboard-edit',
  templateUrl: './keyboard-edit.component.html',
  styleUrls: [ './keyboard-edit.component.css' ]
})
export class KeyboardEditComponent implements OnInit {

  keyboard: Keyboard | undefined;

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
    const confirmNavigation = confirm("Are you done making changes to the listing?");
    if (confirmNavigation) {
      this.router.navigate(['/inventory-edit'])
    }
  }

  save(): void {
    if (this.keyboard) {
      this.keyboardService.updateKeyboard(this.keyboard)
        .subscribe(() => this.goBack());
    }
  }

}
