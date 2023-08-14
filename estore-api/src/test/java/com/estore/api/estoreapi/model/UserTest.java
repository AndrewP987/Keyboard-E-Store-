package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

@Tag("Model-tier")
public class UserTest {

    @Test
    public void getUsername() {
        User user = new User("testuser", "testuser");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);

        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void setUsername() {
        User user = new User("test", "test");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);

        user.setUsername("test2");

        assertEquals("test2", user.getUsername());
    }

    @Test
    public void getPassword() {
        User user = new User("testuser", "testuser");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);

        assertEquals("testuser", user.getPassword());
    }

    @Test
    public void setPassword(){
        User user = new User("test", "test");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);
        user.setPassword("test2");
        assertEquals("test2", user.getPassword());
    }

    @Test
    public void setandgetOrderHistory() {

        User user = new User("testuser", "test");
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);


        ArrayList<Keyboard> userOrderHistory = new ArrayList<>();
        userOrderHistory.add(keyboard);

        user.setUserOrderHistory(userOrderHistory);

        assertEquals(userOrderHistory, user.getUserOrderHistory());

    }

    @Test
    public void setandgetCart() {

        User user = new User("testuser", "test");
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);


        ArrayList<Keyboard> cart = new ArrayList<>();
        cart.add(keyboard);

        user.setUserCart(cart);

        assertEquals(cart, user.getUserCart());
    }

    @Test
    public void testgetLoginStatus() {
        User user = new User("test", "test");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);

        assertTrue(user.getLoginStatus());
    }

    @Test
    public void testisLoginStatus() {
        User user = new User("test", "test");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);

        assertTrue(user.getLoginStatus());
    }

    @Test
    public void testsetLoginStatus() {
        User user = new User("test", "test");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);

        user.setLoginStatus(false);
        assertFalse(user.getLoginStatus());
    }

    @Test
    public void testTostring() {

        User user = new User("testuser", "test");
        // Keyboard keyboard = new Keyboard(2, "testkeyboard", 10, 2);

        String expString = "User [username=testuser, password=test, orderHistory=[], cart=[], login_status=true]";

        assertEquals(String.format(User.STRING_FORMAT, user.getUsername(), user.getPassword(),
                user.getUserOrderHistory(), user.getUserCart(), user.getLoginStatus()), expString);
    }

   
    @Test
    public void testIsLoginStatus() {
        User user = new User("test", "test");
        // user login status always generates as true
        assertTrue(user.isLoginStatus());
    }

    @Test
    public void testNotEquals() {

        Keyboard first = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);
        Keyboard second = new Keyboard(13, "Foldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);
        assertFalse(first.equals(second));

    }

}
