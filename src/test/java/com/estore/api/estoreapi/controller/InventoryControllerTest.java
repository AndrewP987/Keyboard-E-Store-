package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.estore.api.estoreapi.model.Keyboard;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("Controller-tier")
public class InventoryControllerTest {

    private InventoryController inventoryController;
    private InventoryDAO mockInventoryDAO;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setUpInventoryController() {
        mockInventoryDAO = mock(InventoryDAO.class);
        inventoryController = new InventoryController(mockInventoryDAO);
    }

    @Test
    public void testGetKeyboardbyID() throws IOException {

        Keyboard keyboard = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
     
        when(mockInventoryDAO.getKeyboard(keyboard.getKeyboardId())).thenReturn(keyboard);

        ResponseEntity<Keyboard> response = inventoryController.getKeyboard(keyboard.getKeyboardId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(keyboard, response.getBody());
    }

    @Test
    public void testGetKeyboardbyIDNot() throws IOException {

        Keyboard keyboard = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        

        when(mockInventoryDAO.getKeyboard(13)).thenReturn(keyboard);

        ResponseEntity<Keyboard> response = inventoryController.getKeyboard(keyboard.getKeyboardId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // assertEquals(keyboard, response.getBody());
    }

    @Test
    public void testGetKeyboards() throws IOException {
        Keyboard[] keys = new Keyboard[2];
        keys[0] = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        keys[1] = new Keyboard(13, "test2", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        when(mockInventoryDAO.getKeyboard(keys[0].getKeyboardId())).thenReturn(keys[0]);
        when(mockInventoryDAO.getKeyboard(keys[1].getKeyboardId())).thenReturn(keys[1]);
        when(mockInventoryDAO.getKeyboards()).thenReturn(keys);

        ResponseEntity<Keyboard[]> response = inventoryController.getKeyboards();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new Keyboard[] { keys[0], keys[1] }, response.getBody());
    }

    @Test
    public void testGetKeyboardsNot() throws IOException {
        Keyboard[] keys = new Keyboard[0];
        // keys[0] = new Keyboard(12, "test1", 10, 10);
        // keys[1] = new Keyboard(13, "test2", 10, 10);
        // when(mockInventoryDAO.getKeyboard(keys[0].getKeyboardId())).thenReturn(keys[0]);
        // when(mockInventoryDAO.getKeyboard(keys[1].getKeyboardId())).thenReturn(keys[1]);
        when(mockInventoryDAO.getKeyboards()).thenReturn(keys);

        ResponseEntity<Keyboard[]> response = inventoryController.getKeyboards();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // assertArrayEquals(new Keyboard[] {keys[0], keys[1]}, response.getBody());
    }

    @Test
    public void testSearchKeyboards() throws IOException {

        String searchString = "test";

        Keyboard[] keys = new Keyboard[2];
        keys[0] = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        keys[1] = new Keyboard(13, "test2", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        when(mockInventoryDAO.getKeyboard(keys[0].getKeyboardId())).thenReturn(keys[0]);
        when(mockInventoryDAO.getKeyboard(keys[1].getKeyboardId())).thenReturn(keys[1]);
        when(mockInventoryDAO.findKeyboard(searchString)).thenReturn(keys);

        ResponseEntity<Keyboard[]> response = inventoryController.searchKeyboards(searchString);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(new Keyboard[] { keys[0], keys[1] }, response.getBody());
    }

    @Test
    public void testSearchKeyboardsNot() throws IOException {

        String searchString = "test";

        Keyboard[] keys = new Keyboard[2];
        keys[0] = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        keys[1] = new Keyboard(13, "test2", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        when(mockInventoryDAO.getKeyboard(keys[0].getKeyboardId())).thenReturn(keys[0]);
        when(mockInventoryDAO.getKeyboard(keys[1].getKeyboardId())).thenReturn(keys[1]);
        when(mockInventoryDAO.findKeyboard("k")).thenReturn(keys);

        ResponseEntity<Keyboard[]> response = inventoryController.searchKeyboards(searchString);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testCreateKeyboard() throws IOException {

        Keyboard keyboard = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);
        

        when(mockInventoryDAO.createKeyboard(keyboard)).thenReturn(keyboard);

        ResponseEntity<Keyboard> response = inventoryController.createKeyboard(keyboard);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(keyboard, response.getBody());
    }

    @Test
    public void testUpdateKeyboard() throws IOException {
        Keyboard keyboard = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);

        when(mockInventoryDAO.createKeyboard(keyboard)).thenReturn(keyboard);
        keyboard = inventoryController.createKeyboard(keyboard).getBody();

        when(mockInventoryDAO.updateKeyboard(keyboard)).thenReturn(keyboard);

        ResponseEntity<Keyboard> response = inventoryController.updateKeyboard(keyboard);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(keyboard, response.getBody());

    }

    @Test
    public void testDeleteKeyboard() throws IOException {
        Keyboard keyboard = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);

        when(mockInventoryDAO.createKeyboard(keyboard)).thenReturn(keyboard);
        keyboard = inventoryController.createKeyboard(keyboard).getBody();

        when(mockInventoryDAO.deleteKeyboard(keyboard.getKeyboardId())).thenReturn(true);

        ResponseEntity<Keyboard> response = inventoryController.deleteKeyboard(keyboard.getKeyboardId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteKeyboardNot() throws IOException {
        Keyboard keyboard = new Keyboard(12, "test", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 10, 10);

        // when(mockInventoryDAO.createKeyboard(keyboard)).thenReturn(keyboard);
        // keyboard = inventoryController.createKeyboard(keyboard).getBody();

        when(mockInventoryDAO.deleteKeyboard(keyboard.getKeyboardId())).thenReturn(false);

        ResponseEntity<Keyboard> response = inventoryController.deleteKeyboard(keyboard.getKeyboardId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }
}
/*
 * @Test
 * public void testDeleteUserNotFound() throws IOException { // deleteUser may
 * throw IOException
 * // Setup with the username just deleted
 * String username = "My summer co-op (Haha get it?  It doesn't exist)";
 * // when deleteUser is called return false, simulating failed deletion
 * when(mockUserDAO.deleteAccount(username)).thenReturn(false);
 * 
 * // Invoke
 * ResponseEntity<User> response = userController.deleteUser(username);
 * 
 * // Analyze
 * assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
 * }
 */