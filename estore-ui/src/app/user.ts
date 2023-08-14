import { Keyboard } from "./keyboard";

export interface User {
  username: string;
  password: string;
  userOrderHistory: Keyboard[],
  userCart: Keyboard[],
  loginStatus: boolean;
}
