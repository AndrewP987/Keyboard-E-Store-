import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { KeyboardDetailComponent } from './keyboard-detail/keyboard-detail.component';
import { FormsModule } from '@angular/forms';
import { KeyboardSearchComponent } from './keyboard-search/keyboard-search.component';
import { NavbarComponent } from './navbar/navbar.component';
import { InventoryViewComponent } from './inventory-view/inventory-view.component';
import { KeyboardEditComponent } from './keyboard-edit/keyboard-edit.component';
import { KeyboardAddComponent } from './keyboard-add/keyboard-add.component';
import { ShoppingcartComponent } from './shoppingcart/shoppingcart.component';
import { LoginComponent } from './login/login.component';
import { OrderHistoryViewComponent } from './order-history-view/order-history-view.component';
import { KeyboardService } from './keyboard.service';
import { LocalService } from './local.service';
import { UserService } from './userservice.service';
import { SignupComponent } from './signup/signup.component';
import { FirstpageComponent } from './firstpage/firstpage.component';
import { InventoryEditComponent } from './inventory-edit/inventory-edit.component';


@NgModule({
  declarations: [
    AppComponent,
    InventoryViewComponent,
    KeyboardDetailComponent,
    KeyboardSearchComponent,
    NavbarComponent,
    KeyboardAddComponent,
    KeyboardEditComponent,
    ShoppingcartComponent,
    LoginComponent,
    OrderHistoryViewComponent,
    SignupComponent,
    FirstpageComponent,
    InventoryEditComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [KeyboardService,
  UserService, LocalService],
  bootstrap: [AppComponent]
})
export class AppModule { }

