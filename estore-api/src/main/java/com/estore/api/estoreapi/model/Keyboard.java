package com.estore.api.estoreapi.model;

import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Keyboard {
    private static final Logger LOG = Logger.getLogger(Keyboard.class.getName());

    static final String STRING_FORMAT = "Keyboard [id=%d, name=%s, size=%s, switchColor=%s, price=%d, quantity=%d]";

    public enum Size {
        FULL,
        TKL,
        SIXTY
    }

    public enum SwitchColor {
        BLACK,
        RED,
        BLUE,
        BROWN
    }

    @JsonProperty("keyboardId")
    private int id;
    @JsonProperty("keyboardName")
    private String name;
    @JsonProperty("price")
    private int price;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("size")
    public Size size;
    @JsonProperty("switchColor")
    public SwitchColor switchColor;

    /**
     * Create a keyboard with the given id and name
     * 
     * @param id   The id of the keyboard
     * @param name The name of the keyboard
     * 
     *             {@literal @}JsonProperty is used in serialization and
     *             deserialization
     *             of the JSON object to the Java object in mapping the fields. If a
     *             field
     *             is not provided in the JSON object, the Java field gets the
     *             default Java
     *             value, i.e. 0 for int
     */
    public Keyboard(@JsonProperty("id") int id, @JsonProperty("name") String name,
            @JsonProperty("size") Size size, @JsonProperty("switchColor") SwitchColor switchColor,
            @JsonProperty("price") int price, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.switchColor = switchColor;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * 
     * Duplicates an Keyboard with single quantity
     * 
     * @param keyboard Keyboard to duplicate
     */
    public Keyboard(Keyboard keyboard) {
        this.id = keyboard.getKeyboardId();
        this.name = keyboard.getKeyboardName();
        this.size = keyboard.getSize();
        this.switchColor = keyboard.getSwitchColor();
        this.price = keyboard.getPrice();
        this.quantity = 1;
    }

    /**
     * 
     * Creates a new Keyboard based on the given Keyboard and quantity.
     * 
     * @param keyboard Keybaord to create
     */
    public Keyboard(Keyboard keyboard, int quantity) {
        this.id = keyboard.getKeyboardId();
        this.name = keyboard.getKeyboardName();
        this.size = keyboard.getSize();
        this.switchColor = keyboard.getSwitchColor();
        this.price = keyboard.getPrice();
        this.quantity = quantity;
    }

    /**
     * Returns the keyboard's ID.
     * 
     * @return the keyboard's ID
     */
    public int getKeyboardId() {
        return id;
    }

    /**
     * Sets the keyboard's ID.
     * 
     * @param id the new keyboard ID
     */
    public void setKeyboardId(int id) {
        this.id = id;
    }

    /**
     * Returns the keyboard's name.
     * 
     * @return the keyboard's name
     */
    public String getKeyboardName() {
        return name;
    }

    /**
     * Sets the keyboard's name.
     * 
     * @param keyboardName the new keyboard name
     */
    public void setKeyboardName(String name) {
        this.name = name;
    }

    /**
     * Returns the keyboard's name.
     * 
     * @return the keyboard's name
     */
    public SwitchColor getSwitchColor() {
        return switchColor;
    }

    /**
     * Sets the keyboard's name.
     * 
     * @param keyboardName the new keyboard name
     */
    public void setSwitchColor(SwitchColor color) {
        this.switchColor = color;
    }

    /**
     * Returns the keyboard's name.
     * 
     * @return the keyboard's name
     */
    public Size getSize() {
        return size;
    }

    /**
     * Sets the keyboard's name.
     * 
     * @param keyboardName the new keyboard name
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * Returns the keyboard's price.
     * 
     * @return the keyboard's price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the keyboard's price.
     * 
     * @param price the new keyboard price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Returns the keyboard's quantity.
     * 
     * @return the keyboard's quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the keyboard's quantity.
     * 
     * @param quantity the new keyboard quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Checks if the two given Keyboard instances are equal to each other
     * 
     * @param keyboardFirst the first Keyboard to be checked
     * 
     * @param keyboardSecod the second Keyboard to be checked
     *
     * @return boolean true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Keyboard)) {
            return false;
        }
        Keyboard keyboard = (Keyboard) o;
        return id == keyboard.id && Objects.equals(name, keyboard.name) && price == keyboard.price
                && quantity == keyboard.quantity;
    }

    /**
     * Returns a string representation of the Keyboard object.
     * 
     * @return a string representation of the Keyboard object
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, name, size, switchColor, price, quantity);
    }
}
