package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.model.Keyboard;
import com.estore.api.estoreapi.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class to handle http methods GET, POST, and DELETE for users
 */
@RestController
@RequestMapping("user")
public class UserController {
    private static final Logger LOG = Logger.getLogger(UserController.class.getName());
    private UserDAO userDAO;
    private InventoryDAO inventoryDAO;

    /**
     * Constructor to initialize the userController with its DAO
     * 
     * @param userDAO      the userDAO instance to be used in the UserController
     * @param inventoryDAO the inventoryDAO instance to be used in the
     *                     UserController
     */
    public UserController(UserDAO userDAO, InventoryDAO inventoryDAO) {
        this.userDAO = userDAO;
        this.inventoryDAO = inventoryDAO;
    }

    /**
     * gets a user from the json file as requested by http
     * 
     * @param username user to get
     * @return a ResponseEntity with a status code of 200 (OK) if the user is found,
     *         or 404 (NOT_FOUND) if the keyboard with the specified id does not
     *         exist.
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) throws IOException {
        LOG.info("GET /user/" + username);
        User user = userDAO.getUser(username);
        if (user != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * gets all users from the json file as requested by http
     * 
     * @return a ResponseEntity with a status code of 200 (OK) if the user is found
     */
    @GetMapping("")
    public ResponseEntity<User[]> getUsers() {
        LOG.info("GET /users");
        User[] users = userDAO.getUsers();
        return new ResponseEntity<User[]>(users, HttpStatus.OK);
    }

    /**
     * uses the UserDAO to create a user from the username requested
     * 
     * @param user of the user to create
     * @return a ResponseEntity with a status code of 201 (CREATED) if the user
     *         is successfully created,
     *         409 (CONFLICT) if a user with the same username already exists,
     *         or 500 (INTERNAL_SERVER_ERROR) if an IOException occurs.
     */
    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.info("POST /user " + user);
        try {
            User userToCreate = userDAO.createUser(user);
            if (userToCreate != null) {
                return new ResponseEntity<>(userToCreate, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing User with the information contained in the provided User
     * object.
     *
     * @param newUser a User object containing the updated information
     * @return a ResponseEntity containing the updated User object and an HTTP
     *         status code
     *         of OK (200) if the update was successful, or NOT_FOUND (404) if no
     *         User object
     *         with the specified username exists
     *         or INTERNAL_SERVER_ERROR (500) if an I/O error occurs while updating
     *         the user
     * @throws IOException if an I/O error occurs while retrieving or updating the
     *                     User object
     */

    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User newUser) {
        LOG.info("PUT /user/" + newUser.getUsername());

        try {
            User userToUpdate = userDAO.getUser(newUser.getUsername());
            if (userToUpdate == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                userToUpdate = userDAO.updateUser(userToUpdate);
                return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * deletes a user from the website's storage based on http request
     * 
     * @param username user to delete
     * @return http status of delete
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        LOG.info("DELETE /user/" + username);

        try {
            boolean isDeleted = userDAO.deleteAccount(username);
            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves a User object with the specified username and logs the user in.
     *
     * @param username the username of the User object to retrieve and log in
     * @param password the password of the User object to retrieve and log in
     * @return a ResponseEntity containing the retrieved User object and an HTTP
     *         status code
     *         of OK (200) if the login was successful and the User object exists,
     *         UNAUTHORIZED (401) if the username does not match the provided
     *         username,
     *         or NOT_FOUND (404) if no User object with the specified username
     *         exists
     *         or INTERNAL_SERVER_ERROR (500) if an I/O error occurs while
     *         retrieving or updating the user
     * @throws IOException if an I/O error occurs while retrieving or updating the
     *                     User object
     */
    @PutMapping("/login/username={username}&password={password}")
    public ResponseEntity<User> login(@PathVariable String username, @PathVariable String password) {
        LOG.info("GET /login/username=" + username + "&password=" + password);

        try {
            User user = userDAO.getUser(username);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                userDAO.login(user);
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A PUT API endpoint that logs out a user with the given username and password.
     * 
     * @param username the username of the user to be logged out.
     * @param password the password of the user to be logged out.
     * 
     * @return a ResponseEntity with a status code of 200 (OK) if the user is
     *         successfully logged out,
     *         or a ResponseEntity with a status code of 404 (NOT_FOUND) if the user
     *         is not found,
     *         or a ResponseEntity with a status code of 500 (INTERNAL_SERVER_ERROR)
     *         if an IOException occurs.
     */
    @PutMapping("/logout/username={username}&password={password}")
    public ResponseEntity<User> logout(@PathVariable String username, @PathVariable String password) {
        LOG.info("GET /logout/username=" + username + "?password=" + password);

        try {
            User user = userDAO.getUser(username);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                userDAO.logout(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves the shopping cart of the User with the specified username.
     *
     * @param username the username of the User object to retrieve the shopping cart
     *                 for
     * @return a ResponseEntity containing an ArrayList of Keyboard objects
     *         representing the User's shopping cart
     *         and an HTTP status code of OK (200) if the User object exists and
     *         contains a non-empty cart,
     *         or NOT_FOUND (404) if no User object with the specified username
     *         exists or the User object's cart is empty
     */
    @GetMapping("/{username}/cart")
    public ResponseEntity<ArrayList<Keyboard>> getUserShoppingCart(@PathVariable String username) {
        LOG.info("GET /" + username + "/cart");

        User currUser = userDAO.getUser(username);
        if (currUser != null) {
            ArrayList<Keyboard> currUserCart = userDAO.getUser(currUser.getUsername()).getUserCart();
            if (currUserCart != null) {
                return new ResponseEntity<ArrayList<Keyboard>>(currUserCart, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 
     * PUT mapping for updating user shopping cart. Adds a new keyboard to the
     * user's shopping cart and updates the quantity of the keyboard in the
     * inventory.
     * 
     * @param keyboard the keyboard to be added to the user's cart.
     * @param username the username of the user whose cart is being updated.
     * @return ResponseEntity<User> containing the updated user object and
     *         HttpStatus.CREATED if the cart was updated successfully.
     *         ResponseEntity with HttpStatus.NOT_FOUND if the user was not found.
     */
    @PutMapping("/{username}/cart")
    public ResponseEntity<User> addToCart(@RequestBody Keyboard keyboard, @PathVariable String username) {
        LOG.info("PUT /" + username + ": " + keyboard);

        try {
            User currUser = userDAO.getUser(username);
            if (currUser != null) {
                if (userDAO.addToUserCart(currUser, keyboard, keyboard.getQuantity())) {
                    Keyboard keyIndex = inventoryDAO.getKeyboard(keyboard.getKeyboardId());
                    if (keyboard.equals(keyIndex)) {
                        keyIndex.setQuantity(keyIndex.getQuantity() - 1);
                        inventoryDAO.updateKeyboard(keyIndex);
                    }
                }
                return new ResponseEntity<User>(currUser, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * DELETE mapping for clearing a user's shopping cart after purchase.
     * 
     * @param username the username of the user whose cart is being cleared.
     * @return ResponseEntity<User> containing the updated user object and
     *         HttpStatus.OK if the cart was cleared successfully. ResponseEntity
     *         with HttpStatus.NOT_FOUND if the user was not found.
     */
    @DeleteMapping("/{username}/clearCart")
    public ResponseEntity<User> clearCartAfterPurchase(@PathVariable String username) {
        LOG.info("DELETE /" + username + "/clearCart");

        try {
            User user = userDAO.getUser(username);

            if (user == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else {
                if (userDAO.clearUserCart(user)) {
                    return new ResponseEntity<User>(user, HttpStatus.OK);
                } else
                    throw new IOException();
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * A DELETE API endpoint that removes a Keyboard object from a user's cart.
     * 
     * @param username the username of the user whose cart the Keyboard object will
     *                 be removed from.
     * @param keyboard the Keyboard object to be removed from the user's cart.
     * @return a ResponseEntity with a status code of 200 (OK) and the updated User
     *         object if the operation is successful,
     *         css
     *         Copy code
     *         or a ResponseEntity with a status code of 404 (NOT_FOUND) if the user
     *         or the Keyboard object is not found,
     *         css
     *         Copy code
     *         or a ResponseEntity with a status code of 500 (INTERNAL_SERVER_ERROR)
     *         if an IOException occurs.
     */
    @DeleteMapping("/{username}/removeFromCart")
    public ResponseEntity<User> removeFromCart(@PathVariable String username, @RequestBody Keyboard keyboard) {
        LOG.info("DELETE /" + username + "/removeFromCart/keyboard" + ":" + keyboard);

        try {
            User currUser = userDAO.getUser(username);
            if (currUser != null) {
                if (userDAO.removeFromCart(currUser, keyboard)) {
                    return new ResponseEntity<User>(currUser, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * PUT mapping for increasing the quantity of a keyboard in a user's shopping
     * cart.
     * 
     * @param keyboard the keyboard whose quantity is being increased.
     * @param username the username of the user whose cart is being updated.
     * @return ResponseEntity<User> containing the updated user object and
     *         HttpStatus.CREATED if the cart was updated successfully.
     *         ResponseEntity with HttpStatus.NOT_FOUND if the user was not found.
     */
    @PutMapping("/{username}/cart/increaseQuantity")
    public ResponseEntity<User> increaseQuantity(@RequestBody Keyboard keyboard, @PathVariable String username) {
        LOG.info("PUT /" + username + "/cart/increaseQuantity/" + keyboard);

        try {
            User currUser = userDAO.getUser(username);
            if (currUser != null) {
                Keyboard keyboardUpdated = userDAO.getKeyboardFromCart(currUser, keyboard);
                if (currUser.getUserCart().contains(keyboard)) {
                    userDAO.increaseQuantity(currUser, keyboardUpdated);
                }
                return new ResponseEntity<User>(currUser, HttpStatus.NOT_ACCEPTABLE);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * Decreases the quantity of a Keyboard in the cart of a given user.
     * 
     * @param keyboard the Keyboard to decrease the quantity of
     * @param username the username of the user whose cart will have the Keyboard
     *                 quantity decreased
     * @return a ResponseEntity containing the updated User object if successful, or
     *         an error status if not
     */
    @PutMapping("/{username}/cart/decreaseQuantity")
    public ResponseEntity<User> decreaseQuantity(@RequestBody Keyboard keyboard, @PathVariable String username) {
        LOG.info("PUT /" + username + "/cart/decreaseQuantity/" + keyboard);

        try {
            User currUser = userDAO.getUser(username);
            if (currUser != null) {
                Keyboard keyboardUpdated = userDAO.getKeyboardFromCart(currUser, keyboard);
                if (currUser.getUserCart().contains(keyboard)) {
                    userDAO.decreaseQuantity(currUser, keyboardUpdated);
                }
                return new ResponseEntity<User>(currUser, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 
     * Gets the order history for a given user.
     * 
     * @param username the username of the user whose order history to retrieve
     * @return a ResponseEntity containing an ArrayList of Keyboard objects
     *         representing the user's order history if successful, or an error
     *         status if not
     * @throws IOException if there is an error accessing the data source
     */
    @GetMapping("/{username}/orders")
    public ResponseEntity<ArrayList<Keyboard>> getUserOrderHistory(@PathVariable String username) throws IOException {
        LOG.info("GET /" + username + "/orders");

        User currUser = userDAO.getUser(username);
        if (currUser != null) {
            ArrayList<Keyboard> currUserOrderHistory = userDAO.getUserOrderHistory(currUser.getUsername());
            if (currUserOrderHistory != null) {
                return new ResponseEntity<ArrayList<Keyboard>>(currUserOrderHistory, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 
     * Adds a list of purchased keyboards to a user's order history and updates the
     * inventory accordingly.
     * 
     * @param keyboardsPurchased an ArrayList of Keyboard objects representing the
     *                           keyboards purchased
     * @param username           the username of the user who made the purchase
     * @return a ResponseEntity containing the updated User object if successful, or
     *         an error status if not
     */
    @PostMapping("/{username}/orders/addToOrderHistory")
    public ResponseEntity<User> pushToOrderHistory(@RequestBody ArrayList<Keyboard> keyboardsPurchased,
            @PathVariable String username) {
        LOG.info("POST /" + username + ": " + keyboardsPurchased);

        try {
            User currUser = userDAO.getUser(username);
            if (currUser != null) {
                for (Keyboard currPurchased : keyboardsPurchased) {
                    if (userDAO.addToUserOrderHistory(currUser, currPurchased, currPurchased.getQuantity())) {
                        Keyboard fromInventory = inventoryDAO.getKeyboard(currPurchased.getKeyboardId());
                        fromInventory.setQuantity(fromInventory.getQuantity() - currPurchased.getQuantity());
                        inventoryDAO.updateKeyboard(fromInventory);
                    }
                }
                return new ResponseEntity<User>(currUser, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}