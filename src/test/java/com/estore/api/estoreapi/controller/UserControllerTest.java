package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.persistence.UserDAO;
import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Keyboard;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;

@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;
    private InventoryDAO mockInventoryDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        mockInventoryDAO = mock(InventoryDAO.class);
        userController = new UserController(mockUserDAO, mockInventoryDAO);
    }

    @Test
    public void testGetUser() throws IOException {
        // Setup
        User user = new User("Bob", "Bob");
        // When the same id is passed in, our mock UserDAO will return the User object
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserNotFound() throws Exception { // createUser may throw IOException
        // Setup
        String username = "Waluigi";
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no user found
        when(mockUserDAO.getUser(username)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all UserController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateUser() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("batman", "batman");

        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("batman", "batman");

        // when createUser is called, return false simulating failed
        when(mockUserDAO.createUser(user)).thenReturn(null);

        // Invoke
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        ResponseEntity<User> response = userController.createUser(user);
        // response = userController.createUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("Birdman", "Super");


        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).createUser(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException { // updateUser may throw IOException
        // Setup
        // To test updateUser, we have to use a username that is already in storage
        ArrayList<Keyboard> history = new ArrayList<>();

        history.add(new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 50, 10));
        User user = new User("Pac-man", "Pac-man");


        // To get a user in storage, lets create it
        when(mockUserDAO.createUser(user)).thenReturn(user);
        user = userController.createUser(user).getBody();

        // update the user to its different than the one in storage
        user.setUserOrderHistory(history);

        when(mockUserDAO.updateUser(user)).thenReturn(user);
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUserFailed() throws IOException { // updateUser may throw IOException
        // Setup
        ArrayList<Keyboard> history = new ArrayList<>();

        history.add(new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 50, 10));
        User user = new User("Birdman", "test");
        User user1 = new User("Birdman2", "test");

        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user1);
        user = userController.getUser(user.getUsername()).getBody();
        doThrow(new IOException()).when(mockUserDAO).updateUser(user);
        // invoke

        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        // assertEquals("response", response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws IOException { // updateUser may throw IOException
        // Setup
        // To test updateUser, we have to use a username that is already in storage
        ArrayList<Keyboard> history = new ArrayList<>();
        User user = new User("bird", "bird");

        // To get a user in storage, lets create it
        when(mockUserDAO.createUser(user)).thenReturn(new User(user.getUsername(), user.getPassword()));
        user = userController.createUser(user).getBody();

        // update the user to its different than the one in storage
        history.add(new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 50, 10));
        user.setUserOrderHistory(history);

        doThrow(new IOException()).when(mockUserDAO).updateUser(user);
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetUsers() {
        // Setup
        User[] users = new User[2];
        users[0] = new User("Bob", "Bob");
        ArrayList<Keyboard> testHistory = new ArrayList<>();

        testHistory.add(new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 50, 10));
        users[1] = new User("Batman", "test");

        // When getUsers is called return the users created above
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(users, response.getBody());
    }

    @Test
    public void testDeleteUser() throws IOException { // deleteUser may throw IOException
        // Setup
        String username = "Batman";
        // when deleteUser is called return true, simulating successful deletion
        when(mockUserDAO.deleteAccount(username)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(username);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserNotFound() throws IOException { // deleteUser may throw IOException
        // Setup with the username just deleted
        String username = "My summer co-op (Haha get it?  It doesn't exist)";
        // when deleteUser is called return false, simulating failed deletion
        when(mockUserDAO.deleteAccount(username)).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(username);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserHandleException() throws IOException { // deleteUser may throw IOException
        // Setup
        String username = "Birdman";
        // When deleteUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteAccount(username);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(username);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLogin() throws IOException {
        // Setup
        User user = new User("login man", "login man");

        // To get a user in storage, lets create it
        when(mockUserDAO.createUser(user)).thenReturn(user);
        user = userController.createUser(user).getBody();

        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // invoke

        ResponseEntity<User> response = userController.login(user.getUsername(), user.getPassword());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testLoginFailed() throws IOException { // updateUser may throw IOException
        // Setup

        User user = new User("login man", "login");
        User user1 = new User("login man2", "login");

        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user1);
        user = userController.getUser(user.getUsername()).getBody();


        ResponseEntity<User> response;
        // ResponseEntity.status(HttpStatus.NOT_FOUND);

        // invoke
        response = userController.login(user.getUsername(), user.getPassword());


        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testLoginUnauthorized() throws IOException { // updateUser may throw IOException
        // Setup

        User user = new User("login man", "login");
        User userToFind = new User("not login man", "not login");

        assertNotEquals(user.getUsername(), userToFind.getUsername());

        when(mockUserDAO.getUser(user.getUsername())).thenReturn(userToFind);

        // invoke
        ResponseEntity<User> response = userController.login(user.getUsername(), user.getPassword());

        // Analyze
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testLoginHandleException() throws IOException { // updateUser may throw IOException
        // Setup

        User user = new User("login man", "lo");



        // To get a user in storage, lets create it
        when(mockUserDAO.createUser(user)).thenReturn(user);
        user = userController.createUser(user).getBody();

        doThrow(new IOException()).when(mockUserDAO).login(user);
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // invoke
        ResponseEntity<User> response = userController.login(user.getUsername(), user.getPassword());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLogout() throws IOException {
        User user = new User("login man", "lo");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        assertEquals(HttpStatus.OK, userController.logout(user.getUsername(), user.getPassword()).getStatusCode());
    }

    @Test
    public void testLogoutNotFound() throws IOException {
        User user = new User("login man", "lo");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, userController.logout(user.getUsername(), user.getPassword()).getStatusCode());
    }

    @Test
    public void testLogoutHandleException() throws IOException {
        User user = new User("login man", "lo");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        doThrow(new IOException()).when(mockUserDAO).logout(user);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, userController.logout(user.getUsername(), user.getPassword()).getStatusCode());
    }

    @Test
    public void testAddToCart() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("Batman", "super");
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);


        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        // when(mockUserDAO.addToUserCart(user, keyboard, 1));

        // Invoke
        ResponseEntity<User> response = userController.addToCart(keyboard, user.getUsername());

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testAddToCartNotFound() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("Batman", "super");
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);

        // when createUser is called, return false simulating failed
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.addToCart(keyboard, user.getUsername());
        // response = userController.createUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartHandleException() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("Birdman", "Manbird");
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);


        // When createUser is called on the Mock User DAO, throw an IOException
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        doThrow(new IOException()).when(mockUserDAO).addToUserCart(user, keyboard, 1);

        // Invoke
        ResponseEntity<User> response = userController.addToCart(keyboard, user.getUsername());

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // @Test
    // public void testRemoveFromCart() throws IOException { // createUser may throw IOException
    //     // Setup


    //     User user = new User("Batman", "super");
    //     Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);


    //     // when createUser is called, return true simulating successful
    //     // creation and save
    //     when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
    //     // when(mockUserDAO.addToUserCart(user, keyboard, 1));
    //     user.getUserCart().add(keyboard);
    //     //userController.addToCart(keyboard, user.getUsername());
    //     System.out.println(user.getUserCart().toString());
    //     // when(mockUserDAO.getUser(user.getUsername()).addKeyboardToCart(keyboard,
    //     // 3)).thenReturn(true);

    //     // Invoke
    //     ResponseEntity<User> response = userController.removeFromCart(user.getUsername(), keyboard);
    //     System.out.println(user.getUserCart().toString());
    //     System.out.println(response);

    //     // Analyze
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals(user, response.getBody());
    // }

    @Test
    public void testRemoveFromCartNotFound() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("Batman", "super");
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);


        // when createUser is called, return false simulating failed
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.removeFromCart(user.getUsername(), keyboard);
        // response = userController.createUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
     * @Test
     * public void testRemoveFromCartHandleException() throws IOException { //
     * createUser may throw IOException
     * // Setup
     * User user = new User("Birdman");
     * Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", 50, 10);
     * 
     * // When createUser is called on the Mock User DAO, throw an IOException
     * when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
     * doThrow(new IOException()).when(mockUserDAO).removeFromUserCart(user,
     * keyboard, 1);
     * 
     * // Invoke
     * ResponseEntity<User> response = userController.removeFromCart(keyboard,
     * user.getUsername());
     * 
     * // Analyze
     * assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
     * }
     */

    // @Test
    // public void testClearUserCart() throws IOException { // createUser may throw IOException
    //     // Setup

    //     User user = new User("Batman", "homie");
    //     // when createUser is called, return true simulating successful
    //     // creation and save
    //     when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
    //     Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 50, 10);
    //     // when createUser is called, return true simulating successful
    //     // creation and save
    //     // when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
    //     // when(mockUserDAO.addToUserCart(user, keyboard, 1));

    //     //userController.addToCart(keyboard, user.getUsername());
    //     mockUserDAO.addToUserCart(user, keyboard, 1);
    //     // Invoke
        
    //     System.out.println(user.getUserCart());
    //     // Invoke
    //     ResponseEntity<User> response = userController.clearCartAfterPurchase(user.getUsername());

    //     // Analyze
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    // }

    @Test
    public void testClearUserCartNotFound() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("Batman", "super");

        // when createUser is called, return false simulating failed
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.clearCartAfterPurchase(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testClearUserCartHandleException() throws IOException { // createUser may throw IOException
        // Setup

        User user = new User("Birdman", "super");


        // When createUser is called on the Mock User DAO, throw an IOException
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        doThrow(new IOException()).when(mockUserDAO).clearUserCart(user);

        // Invoke
        ResponseEntity<User> response = userController.clearCartAfterPurchase(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testIncreaseQuantity() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);

        keyboard.setQuantity(2);
        userController.clearCartAfterPurchase(user.getUsername());
        assertEquals(HttpStatus.CREATED, userController.increaseQuantity(keyboard, "Batman").getStatusCode());
        
        assertEquals(2, keyboard.getQuantity());
    }

    @Test
    public void testIncreaseQuantity_userNotFound() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        doThrow(new IOException()).when(mockUserDAO).clearUserCart(user);
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);

        keyboard.setQuantity(2);
        userController.clearCartAfterPurchase(user.getUsername());
        assertEquals(HttpStatus.CREATED, userController.increaseQuantity(keyboard, "Batman").getStatusCode());
        assertEquals(2, keyboard.getQuantity());
    }

    @Test
    public void testDecreaseQuantity() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);

        keyboard.setQuantity(2);
        userController.clearCartAfterPurchase(user.getUsername());
        assertEquals(HttpStatus.CREATED, userController.decreaseQuantity(keyboard, "Batman").getStatusCode());
        
        assertEquals(2, keyboard.getQuantity());
    }

    //@Test
    // public void testDecreaseQuantityHandleException() throws IOException {
    //     User user = new User("Batman", "super");


    //     // When createUser is called on the Mock User DAO, throw an IOException
    //     when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        

    //     Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);
    //     keyboard.setQuantity(2);
    //     doThrow(new IOException()).when(mockUserDAO).decreaseQuantity(user, keyboard);

    //     // Invoke
    //     ResponseEntity<User> response = userController.decreaseQuantity(keyboard, user.getUsername());

    //     // Analyze
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    // }

    @Test
    public void testGetUserOrderHistory() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);

        keyboard.setQuantity(2);
        userController.clearCartAfterPurchase(user.getUsername());
        assertEquals(HttpStatus.OK, userController.getUserOrderHistory("Batman").getStatusCode());
        
        assertEquals(2, keyboard.getQuantity());
    }

    @Test public void testGetUserNotFoundShoppingCart() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, userController.getUserShoppingCart(user.getUsername()).getStatusCode());
    }

    @Test public void testGetUserShoppingCartNotFound() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        when(mockUserDAO.getUser(user.getUsername()).getUserCart()).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, userController.getUserShoppingCart(user.getUsername()).getStatusCode());
    }

    @Test
    public void testGetUserOrderHistoryNotFound() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        when(mockUserDAO.getUserOrderHistory(user.getUsername())).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, userController.getUserOrderHistory(user.getUsername()).getStatusCode());
    }

    @Test
    public void testGetUserNotFoundOrderHistory() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, userController.getUserOrderHistory(user.getUsername()).getStatusCode());
    }

    @Test
    public void testPushToOrderHistory() throws IOException {
        User user = new User("Batman", "super");
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);
        
        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);

        keyboard.setQuantity(2);
        userController.clearCartAfterPurchase(user.getUsername());
        assertEquals(HttpStatus.CREATED, userController.pushToOrderHistory(user.getUserOrderHistory(), "Batman").getStatusCode());
        
        assertEquals(2, keyboard.getQuantity());
    }

    // @Test
    // public void testPushToOrderHistoryUserNotFound() throws IOException {
    //     User user = new User("Batman", "super");
    //     when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);
    //     Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);
    //     keyboard.setQuantity(2);
    //     ArrayList<Keyboard> history = new ArrayList<>();
    //     history.add(keyboard);

    //     assertEquals(HttpStatus.NOT_FOUND, userController.pushToOrderHistory(history, user.getUsername()).getStatusCode());
    // }

    // @Test
    // public void testPushToOrderHistoryHandleException() throws IOException {
    //     User user = new User("Batman", "super");
    //     doThrow(new IOException()).when(mockUserDAO).getUser(user.getUsername());
    //     assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, 
    //         userController.pushToOrderHistory(new ArrayList<Keyboard>(), user.getUsername()).getStatusCode());
    // }

    
}