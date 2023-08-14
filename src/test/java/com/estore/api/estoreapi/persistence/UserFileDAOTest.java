package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Keyboard;

@Tag("Persistence-tier")
public class UserFileDAOTest {
    // Create test data
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;
    ArrayList<Keyboard> history = new ArrayList<Keyboard>();

    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);

        testUsers = new User[2];
        testUsers[0] = new User("test_user1", "test_user1");
        testUsers[1] = new User("test_user2", "test_user2");

        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"), User[].class)).thenReturn(testUsers);

        userFileDAO = new UserFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testLogin() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        userFileDAO.logout(retrievedUser);
        userFileDAO.login(retrievedUser);
        
        assertTrue(retrievedUser.getLoginStatus());
    }

    @Test
    public void testAddToUserCart() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        boolean x = userFileDAO.addToUserCart(retrievedUser, keyboard, 1);

        assertTrue(x);
        
    }

    @Test
    public void testRemoveFromUserCart() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        boolean x = userFileDAO.addToUserCart(retrievedUser, keyboard, 1);
        boolean y = userFileDAO.removeFromCart(retrievedUser, keyboard);

        assertTrue(y);
        
    }

    @Test
    public void testAddToOrderHistory() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        

        assertTrue(userFileDAO.addToUserOrderHistory(retrievedUser, keyboard, 1));
        
    }

    @Test
    public void testIncreaseQuantity() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        userFileDAO.addToUserCart(retrievedUser, keyboard, 1);

        boolean x = userFileDAO.increaseQuantity(retrievedUser, keyboard);

        assertTrue(x);
        
    }

    @Test
    public void testDecreaseQuantity() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        userFileDAO.addToUserCart(retrievedUser, keyboard, 1);

        boolean x = userFileDAO.decreaseQuantity(retrievedUser, keyboard);

        assertTrue(x);
        
    }

    @Test
    public void testClearCart() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        userFileDAO.addToUserCart(retrievedUser, keyboard, 1);

        boolean x = userFileDAO.clearUserCart(retrievedUser);

        assertTrue(x);
        
    }

    @Test
    public void testGetCart() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        userFileDAO.addToUserCart(retrievedUser, keyboard, 1);

        assertEquals(retrievedUser.getUserCart(), userFileDAO.getUserShoppingCart(retrievedUser));
        
    }
   


    @Test
    public void testGetUser() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        // Check that the retrieved user is not null and has the correct username
        assertNotNull(retrievedUser);
        assertEquals(testUsers[0].getUsername(), retrievedUser.getUsername());
        
    }


    @Test
    public void testGetKeyboardFromCart() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        userFileDAO.addToUserCart(retrievedUser, keyboard, 1);

        assertEquals(keyboard, userFileDAO.getKeyboardFromCart(retrievedUser, keyboard));
        
    }

    @Test
    public void testGetUserOrderHistory() throws IOException {
        // Retrieve the user from the data store using the getUser method of the User
        // class
        User retrievedUser = userFileDAO.getUser(testUsers[0].getUsername());
        
        Keyboard keyboard = new Keyboard(1, "bur", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 10);

        userFileDAO.addToUserCart(retrievedUser, keyboard, 1);
        userFileDAO.addToUserOrderHistory(retrievedUser, keyboard, 1);
        assertEquals(retrievedUser.getUserOrderHistory(), userFileDAO.getUserOrderHistory(retrievedUser.getUsername()));
    }


    @Test
    public void testGetUsers() throws IOException {
        // Retrieve all users from the data store using the getUsers method of the User
        // class
        User[] retrievedUserList = userFileDAO.getUsers();

        // Check that the user list is not null and contains both users
        assertNotNull(retrievedUserList);
        assertEquals(2, retrievedUserList.length);
        boolean found_user1 = false;
        boolean found_user2 = false;
        for (User user : retrievedUserList) {
            if (user.getUsername().equals(testUsers[0].getUsername()))
                found_user1 = true;
            if (user.getUsername().equals(testUsers[1].getUsername()))
                found_user2 = true;
        }
        assertTrue(found_user1 && found_user2);
    }

    @Test
    public void testCreateUserWithName() throws IOException {
        User user = new User("testUser", "test_user1");
        User user_created = userFileDAO.createUser(user);

        // Retrieve all users from the data store using the getUsers method of the User
        // class
        User user_found = userFileDAO.getUser(user_created.getUsername());

        // Check that the user found is not null and contains the new user
        assertNotNull(user_created);
        assertEquals(user_found, user_created);
    }

    @Test
    public void testCreateUserAlreadyExists() throws IOException {
        User user = new User("testUser","test_user1");
        User user_created = userFileDAO.createUser(user);
        User user2 = new User("testUser","test_user1");
        User user_created2 = userFileDAO.createUser(user2);
        // Check that the user created is null because test_user3 was previously created
        assertNull(user_created2);
    }

    @Test
    public void testUpdateUser() throws IOException {

        User user1 = new User("testUserUpdate","test_user1");

        User create = userFileDAO.createUser(user1);
        User update = userFileDAO.updateUser(user1);
        assertEquals(user1, update);
    }

    @Test
    public void testDeleteUser() throws IOException {
        // Invoke
        boolean result = userFileDAO.deleteAccount(testUsers[1].getUsername());

        // Analzye
        assertEquals(result, true);
        // We check the internal tree map size against the length
        // of the test users array - 1 (because of the delete)
        assertEquals(userFileDAO.getUsers().length, testUsers.length - 1);
    }

    // @Test
    // public void testAddToHistory() throws IOException {
    // // Create a test user and add it
    // User user = new User("test_user", history, cart);
    // userFileDAO.createUser("test_user");

    // // Retrieve all users from the data store using the getUsers method of the
    // User class
    // User[] userList = userFileDAO.getUsers();

    // // Check that the user list is not null and contains the new user
    // assertNotNull(userList);
    // assertEquals(1, userList.length);
    // //assertTrue(userList.contains(user));

    // // Create a test order and add it to history
    // Keyboard keyboard = new Keyboard(95, "Test Keyboard", 99, 25);
    // ArrayList<Keyboard> order = new ArrayList<Keyboard>();
    // order.add(keyboard);
    // // userFileDAO.addToHistory("test_user", order);
    // }

    @Test
    public void testGetUserNotFound() {
        // try and get a non existent user and make sure the result is null
        User nonExistentUser = userFileDAO.getUser("test_user99");
        assertNull(nonExistentUser);
    }

    @Test
    public void testDeleteUserNotFound() throws IOException {
        // try and get a non existent user and make sure the result is null
        boolean result = userFileDAO.deleteAccount("test_user99");
        assertFalse(result);

        // assert that the userFileDAO's user list was not decreased by a failed delete
        assertEquals(userFileDAO.getUsers().length, testUsers.length);
    }

    /*
     * @Test
     * public void testUpdateUserNotFound() throws IOException {
     * User nonExistentUser = userFileDAO.getUser("test_user99");
     * // assert that get user failed
     * assertNull(nonExistentUser);
     * 
     * 
     * User updatedUser = new User("test_user99");
     * // assert that updating a non existent user is null
     * User update = userFileDAO.updateUser(updatedUser);
     * assertNull(update);
     * }
     * 
     * 
     * @Test
     * public void testUpdateUserNotExistent() throws IOException {
     * User nonExistentUser = userFileDAO.getUser("test_user99");
     * // assert that get user failed
     * assertNull(nonExistentUser);
     * 
     * User userToUpdate = new User("test_user99", history);
     * // assert that updating a non existent user is null
     * assertNull(userFileDAO.updateUser(userToUpdate, nonExistentUser));
     * }
     * 
     * 
     * @Test
     * public void testUpdateUserDifferentUsernames() throws IOException {
     * // make sure that trying to update a user with a different user returns null
     * assertNull(userFileDAO.updateUser(testUsers[0], testUsers[1]));
     * }
     */

    @Test
    public void testUpdateUserNotInUserMap() throws IOException {
        User userToUpdate = new User("test_user99","test_user1");
        // assert that user is not in userMap
        assertFalse(userFileDAO.userMap.containsKey(userToUpdate.getUsername()));

        Keyboard keyboard = new Keyboard(16, "NonFoldable Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 50, 10);
        history.add(keyboard);
        User newUser = new User(userToUpdate.getUsername(), userToUpdate.getPassword());

        // assert update user returns null with a user not in the map
        assertNull(userFileDAO.updateUser(userToUpdate));

    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        doThrow(new IOException()).when(mockObjectMapper).readValue(new File("doesnt_matter.txt"), User[].class);

        // Invoke and Analyze
        assertThrows(IOException.class, () -> new UserFileDAO("doesnt_matter.txt", mockObjectMapper),
                "IOException not thrown");
    }
}
