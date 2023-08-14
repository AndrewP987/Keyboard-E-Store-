import { Size } from "./size";
import { SwitchColor } from "./switch-color";

export interface Keyboard{
  size: Size;
  switchColor: SwitchColor;
  keyboardId: number;
  keyboardName: string;
  price: number;
  quantity: number;
}
