import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KeyboardDetailComponent } from './keyboard-detail/keyboard-detail.component';
import { InventoryViewComponent } from './inventory-view/inventory-view.component';
import { ShoppingcartComponent } from './shoppingcart/shoppingcart.component';
import { KeyboardAddComponent } from './keyboard-add/keyboard-add.component';
import { OrderHistoryViewComponent } from './order-history-view/order-history-view.component';
import { FirstpageComponent } from './firstpage/firstpage.component';
import { InventoryEditComponent } from './inventory-edit/inventory-edit.component';
import { KeyboardEditComponent } from './keyboard-edit/keyboard-edit.component';
import { AuthGuard } from './auth-service.service';

const routes: Routes = [
  { path: '', component: FirstpageComponent },
  { path: 'keyboards/:id', component: KeyboardDetailComponent },
  { path: 'keyboards', component: InventoryViewComponent, canActivate: [AuthGuard] },
  { path: 'cart', component: ShoppingcartComponent, canActivate: [AuthGuard] },
  { path: 'add' , component: KeyboardAddComponent, canActivate: [AuthGuard] },
  { path: 'orders' , component: OrderHistoryViewComponent, canActivate: [AuthGuard] },
  { path: 'inventory-edit' , component: InventoryEditComponent, canActivate: [AuthGuard] },
  { path: 'edit/:id' , component: KeyboardEditComponent, canActivate: [AuthGuard] },
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
