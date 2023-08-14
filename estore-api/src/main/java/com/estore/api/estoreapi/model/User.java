package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * class which handles User objects
 * keeps track of username, order history,
 * and shopping cart as variables for each user
 * 
 * 
 * @author Justin Ronca
 */
public class User {

    static final String STRING_FORMAT = "User [username=%s, password=%s, orderHistory=%s, cart=%s, login_status=%s]";

    // declare User variables
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("userOrderHistory")
    private ArrayList<Keyboard> orderHistory;
    @JsonProperty("userCart")
    private ArrayList<Keyboard> cart;
    @JsonProperty("loginStatus")
    private boolean loginStatus;

    /**
     * constructor to create a new user
     * receives properties from a json file storing information
     * 
     * @param username     the input name of the user account
     * @param orderHistory the history of the user's past orders as a list of
     *                     Keyboards
     * @param cart         the user's shopping cart in its current state
     * 
     *                     Important: one attribute of the User object but isn't
     *                     created by the user is loginStatus. This is an
     *                     attribute modified by the system.
     */
    public User(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
        this.orderHistory = new ArrayList<>();
        this.cart = new ArrayList<>();
        this.loginStatus = true;
    }

    /**
     * 
     * Returns the username of this User object.
     * 
     * @return the username of this User object.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * 
     * Sets the username of this User object to the specified String.
     * 
     * @param username the String to set as the username of this User object.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * Returns the password of this User object.
     * 
     * @return the password of this User object.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 
     * Sets the password of this User object to the specified String.
     * 
     * @param username the String to set as the password of this User object.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * Returns an ArrayList of Keyboard objects representing the order history of
     * this User object.
     * 
     * @return an ArrayList of Keyboard objects representing the order history of
     *         this User object.
     */
    public ArrayList<Keyboard> getUserOrderHistory() {
        return this.orderHistory;
    }

    /**
     * 
     * Sets the order history of this User object to the specified ArrayList of
     * Keyboard objects.
     * 
     * @param userOrderHistory the ArrayList of Keyboard objects to set as the order
     *                         history of this User object.
     */
    public void setUserOrderHistory(ArrayList<Keyboard> orderHistory) {
        this.orderHistory = orderHistory;
    }

    /**
     * 
     * Returns an ArrayList of Keyboard objects representing the items in the
     * shopping cart of this User object.
     * 
     * @return an ArrayList of Keyboard objects representing the items in the
     *         shopping cart of this User object.
     */
    public ArrayList<Keyboard> getUserCart() {
        return this.cart;
    }

    /**
     * 
     * Sets the shopping cart of this User object to the specified ArrayList of
     * Keyboard objects.
     * 
     * @param userCart the ArrayList of Keyboard objects to set as the shopping cart
     *                 of this User object.
     */
    public void setUserCart(ArrayList<Keyboard> cart) {
        this.cart = cart;
    }

    /**
     * 
     * Returns a boolean indicating whether this User object is currently logged in
     * or not.
     * 
     * @return true if this User object is currently logged in; false otherwise.
     */
    public boolean isLoginStatus() {
        return this.loginStatus;
    }

    /**
     * 
     * Returns a boolean indicating whether this User object is currently logged in
     * or not.
     * 
     * @return true if this User object is currently logged in; false otherwise.
     */
    public boolean getLoginStatus() {
        return this.loginStatus;
    }

    /**
     * 
     * Sets the login status of this User object to the specified boolean value.
     * 
     * @param loginStatus the boolean value to set as the login status of this User
     *                    object.
     */
    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    /**
     * 
     * Returns a String representation of the User object.
     * The returned String is formatted using the String.format() method,
     * with the format specified by the STRING_FORMAT constant and the username
     * parameter.
     * 
     * @return a String representation of the User object.
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, username, password, orderHistory, cart, loginStatus);
    }

    /**
     * 
     * Compares this User object to the specified Object.
     * The method returns true if the specified Object is also a User object and has
     * the same username as this User object.
     * 
     * @param obj the Object to compare with this User object.
     * @return true if the specified Object is equal to this User object; false
     *         otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return this.getUsername().equals(user.getUsername());
        }
        return false;
    }

}
