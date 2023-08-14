package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The interface for UserFileDAO which interacts with json storage
 * of User accounts, their names, order histories, and shopping carts
 * 
 * @author Justin Ronca
 */
public interface UserDAO {
    /**
     * gets the user of a certain username, if one exists
     * 
     * @param username the user to get
     * @return the user found, or null
     */
    User getUser(String username);

    /**
     * gets all users signed up for the website
     * 
     * @return an array of all users
     */
    User[] getUsers();

    /**
     * creates a user with the provided username
     * 
     * @param username username of the user to create
     * @param password password of the user to create
     * @return the user created, or null if username already exists
     * @throws IOException
     */
    User createUser(User user) throws IOException;

    /**
     * updates a user in the json file from the provided user
     * 
     * @param userToUpdate the User to change
     * @param newUser      to set userToUpdate to
     * @return the user updated
     * @throws IOException
     */
    User updateUser(User userToUpdate) throws IOException;

    /**
     * deletes an account off of the e-store website
     * 
     * @param username the username of the user to delete
     * @return true upon successful deletion, and false upon user not found
     * @throws IOException
     */
    boolean deleteAccount(String username) throws IOException;

    /**
     * Logs in a User by turning a loginstatus to true
     * 
     * @param user the user to log in
     * 
     * @throws IOException if an error occurs
     * 
     */
    void login(User user) throws IOException;

    /**
     * Logs out a User by turning a loginstatus to false
     * 
     * @param user the user to log out
     * 
     * @throws IOException if an error occurs
     * 
     */
    void logout(User user) throws IOException;

    /**
     * 
     * Adds a specified quantity of a keyboard to a user's cart and saves the
     * updated user data to a JSON file.
     * 
     * @param user     the User object for whom the keyboard is being added to the
     *                 cart
     * @param keyboard the Keyboard object to be added to the cart
     * @param quantity the amount of the specified keyboard to be added to the cart
     * @throws IOException if an I/O error occurs while saving the updated user data
     *                     to the JSON file
     * @return true if the keyboard was successfully added to the user's cart and
     *         the updated user data was saved to the JSON file, false otherwise
     */
    boolean addToUserCart(User user, Keyboard keyboard, int quantity) throws IOException;

    /**
     * 
     * Clears a user's cart and saves the updated user data to a JSON file.
     * 
     * @param user the User object whose cart is being cleared
     * @throws IOException if an I/O error occurs while saving the updated user data
     *                     to the JSON file
     * @return true if the user's cart was successfully cleared and the updated user
     *         data was saved to the JSON file, false otherwise
     */
    boolean clearUserCart(User user) throws IOException;

    /**
     *
     * Returns a user's shopping cart
     * 
     * @param user the User object whose cart is being returned
     * @return ArrayList conssiting of the keyboards in the user's shopping cart
     */
    ArrayList<Keyboard> getUserShoppingCart(User user);

    /**
     * 
     * Increases the quantity of a Keyboard in a User's cart by one and saves the
     * updated user data to a JSON file.
     * 
     * @param user     the User object whose cart is being modified
     * @param keyboard the Keyboard object being modified in the cart
     * @return true if the Keyboard's quantity was successfully increased and the
     *         updated user data was saved to the JSON file, false otherwise
     * @throws IOException if an I/O error occurs while saving the updated user data
     *                     to the JSON file
     */
    boolean increaseQuantity(User user, Keyboard keyboard) throws IOException;

    /**
     * 
     * Decreases the quantity of a Keyboard in a User's cart by one and saves the
     * updated user data to a JSON file.
     * 
     * @param user     the User object whose cart is being modified
     * @param keyboard the Keyboard object being modified in the cart
     * @return true if the Keyboard's quantity was successfully decreased and the
     *         updated user data was saved to the JSON file, false otherwise
     * @throws IOException if an I/O error occurs while saving the updated user data
     *                     to the JSON file
     */
    boolean decreaseQuantity(User user, Keyboard keyboard) throws IOException;

    /**
     * 
     * Returns a Keyboard object from a User's cart.
     * 
     * @param user     the User object whose cart is being queried
     * @param keyboard the Keyboard object being queried in the cart
     * @return the Keyboard object from the cart if it exists, null otherwise
     * @throws IOException if an I/O error occurs while accessing the User's cart
     */
    Keyboard getKeyboardFromCart(User user, Keyboard keyboard) throws IOException;

    /**
     * 
     * Returns an ArrayList of Keyboard objects representing a User's order history.
     * 
     * @param username the username of the User whose order history is being queried
     * @return an ArrayList of Keyboard objects representing the User's order
     *         history, null if the User does not exist
     * @throws IOException if an I/O error occurs while accessing the User's order
     *                     history
     */
    ArrayList<Keyboard> getUserOrderHistory(String username) throws IOException;

    /**
     * 
     * Adds a Keyboard object to a User's order history with the given quantity and
     * saves the updated user data to a JSON file.
     * 
     * @param user     the User object whose order history is being modified
     * @param keyboard the Keyboard object being added to the order history
     * @param quantity the quantity of the Keyboard being added to the order history
     * @return true if the Keyboard was successfully added to the order history and
     *         the updated user data was saved to the JSON file, false otherwise
     * @throws IOException if an I/O error occurs while saving the updated user data
     *                     to the JSON file
     */
    boolean addToUserOrderHistory(User user, Keyboard keyboard, int quantity) throws IOException;

    /**
     * 
     * Removes a specified quantity of a keyboard from a user's cart and saves the
     * updated user data to a JSON file.
     * 
     * @param user     the User object for whom the keyboard is being removed from
     *                 the cart
     * @param keyboard the Keyboard object to be removed from the cart
     * @param quantity the amount of the specified keyboard to be removed from the
     *                 cart
     * @throws IOException if an I/O error occurs while saving the updated user data
     *                     to the JSON file
     * @return true if the keyboard was successfully removed from the user's cart
     *         and the updated user data was saved to the JSON file, false otherwise
     */
    boolean removeFromCart(User user, Keyboard keyboard) throws IOException;

}