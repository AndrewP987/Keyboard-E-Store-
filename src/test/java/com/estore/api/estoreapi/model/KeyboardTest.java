package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class KeyboardTest {

    @Test
    public void Ctor() {
        int expected_id = 20020;
        String expected_name = "Random Name";
        int expected_price = 234;
        int expected_quantity = 23;
        Keyboard.SwitchColor expected_SwitchColor = Keyboard.SwitchColor.BLUE;
        Keyboard.Size expected_Size = Keyboard.Size.FULL;


        Keyboard keyboard = new Keyboard(expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);

        assertEquals(expected_id, keyboard.getKeyboardId());
        assertEquals(expected_name, keyboard.getKeyboardName());
        assertEquals(expected_price, keyboard.getPrice());
        assertEquals(expected_quantity, keyboard.getQuantity());
    }

    @Test
    public void testName() {
        int expected_id = 20020;
        String expected_name = "Random Name";
        int expected_price = 234;
        int expected_quantity = 23;
        Keyboard.SwitchColor expected_SwitchColor = Keyboard.SwitchColor.BLUE;
        Keyboard.Size expected_Size = Keyboard.Size.FULL;


        Keyboard keyboard = new Keyboard(expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);


        String name = "Non-Foldable keyboard";

        keyboard.setKeyboardName(name);

        assertEquals(name, keyboard.getKeyboardName());

    }

    @Test
    public void testSize() {
        int expected_id = 20020;
        String expected_name = "Random Name";
        int expected_price = 234;
        int expected_quantity = 23;
        Keyboard.SwitchColor expected_SwitchColor = Keyboard.SwitchColor.BLUE;
        Keyboard.Size expected_Size = Keyboard.Size.FULL;


        Keyboard keyboard = new Keyboard(expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);


        Keyboard.Size size = Keyboard.Size.TKL;

        keyboard.setSize(size);

        assertEquals(size, keyboard.getSize());

    }

    @Test
    public void testSwitchColor() {
        int expected_id = 20020;
        String expected_name = "Random Name";
        int expected_price = 234;
        int expected_quantity = 23;
        Keyboard.SwitchColor expected_SwitchColor = Keyboard.SwitchColor.BLUE;
        Keyboard.Size expected_Size = Keyboard.Size.FULL;


        Keyboard keyboard = new Keyboard(expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);


        Keyboard.SwitchColor switchColor = Keyboard.SwitchColor.RED;

        keyboard.setSwitchColor(switchColor);

        assertEquals(switchColor, keyboard.getSwitchColor());

    }


    @Test
    public void testPrice() {
        int expected_id = 20020;
        String expected_name = "Random Name";
        int expected_price = 234;
        int expected_quantity = 23;
        Keyboard.SwitchColor expected_SwitchColor = Keyboard.SwitchColor.BLUE;
        Keyboard.Size expected_Size = Keyboard.Size.FULL;


        Keyboard keyboard = new Keyboard(expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);


        int price = 34453;

        keyboard.setPrice(price);

        assertEquals(price, keyboard.getPrice());

    }

    @Test
    public void testQuantity() {
        int expected_id = 20020;
        String expected_name = "Random Name";
        int expected_price = 234;
        int expected_quantity = 23;
        Keyboard.SwitchColor expected_SwitchColor = Keyboard.SwitchColor.BLUE;
        Keyboard.Size expected_Size = Keyboard.Size.FULL;


        Keyboard keyboard = new Keyboard(expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);


        int quantity = 3983;

        keyboard.setQuantity(quantity);

        assertEquals(quantity, keyboard.getQuantity());

    }

    @Test
    public void testToString() {
        int expected_id = 20020;
        String expected_name = "Random Name";
        int expected_price = 234;
        int expected_quantity = 23;
        Keyboard.SwitchColor expected_SwitchColor = Keyboard.SwitchColor.BLUE;
        Keyboard.Size expected_Size = Keyboard.Size.FULL;


        Keyboard keyboard = new Keyboard(expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);

        String expected_String = String.format(Keyboard.STRING_FORMAT, expected_id, expected_name, expected_Size, expected_SwitchColor, expected_price, expected_quantity);

        String actual_string = keyboard.toString();

        assertEquals(actual_string, expected_String);

    }

}