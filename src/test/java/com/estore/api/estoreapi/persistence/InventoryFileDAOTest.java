package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.estore.api.estoreapi.model.Keyboard;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Persistence-tier")
public class InventoryFileDAOTest {

    InventoryFileDAO inventoryFileDAO;
    Keyboard[] testKeyboards;
    ObjectMapper mockObjectMapper;

    @BeforeEach
    public void setUpKeyboardFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testKeyboards = new Keyboard[5];
        testKeyboards[0] = new Keyboard(99, "First", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 30, 20);
        testKeyboards[1] = new Keyboard(100, "Second", Keyboard.Size.SIXTY, Keyboard.SwitchColor.RED, 31, 21);
        testKeyboards[2] = new Keyboard(101, "Third", Keyboard.Size.FULL, Keyboard.SwitchColor.BROWN, 32, 22);
        testKeyboards[3] = new Keyboard(102, "Fourth", Keyboard.Size.TKL, Keyboard.SwitchColor.BLUE, 33, 23);
        testKeyboards[4] = new Keyboard(103, "Fifth", Keyboard.Size.FULL, Keyboard.SwitchColor.RED, 34, 24);

        when(mockObjectMapper.readValue(new File("testing_doesnt_matter.txt"), Keyboard[].class))
                .thenReturn(testKeyboards);

        inventoryFileDAO = new InventoryFileDAO("testing_doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetFilteredKeyboardsForGettingPrice() throws Exception {
        Keyboard[] keyboards = inventoryFileDAO.getFilteredKeyboards(String.valueOf(30),
                String.valueOf(32));
        assertEquals(3, keyboards.length);

        for (int i = 0; i < keyboards.length; i++) {
            int currPrice = keyboards[i].getPrice();
            boolean between;

            if (currPrice >= 30 && currPrice <= 32) {
                between = true;
            } else {
                between = false;
            }
            assertTrue(between);

        }

    }


    @Test
    public void testGetKeyboards() throws IOException {

        Keyboard[] keyboards = inventoryFileDAO.getKeyboards();

        assertEquals(keyboards.length, testKeyboards.length);
        for (int i = 0; i < testKeyboards.length; ++i)
            assertEquals(keyboards[i], testKeyboards[i]);
    }

    @Test
    public void testFindKeyboards() throws IOException {

        Keyboard[] keyboards = inventoryFileDAO.findKeyboard("i");

        assertEquals(keyboards.length, 3);
        assertEquals(keyboards[0], testKeyboards[0]);
        assertEquals(keyboards[1], testKeyboards[2]);
        assertEquals(keyboards[2], testKeyboards[4]);

    }

    @Test
    public void testGetKeyboard() throws IOException {

        Keyboard keyboard = inventoryFileDAO.getKeyboard(103);

        assertEquals(keyboard, testKeyboards[4]);
    }

    @Test
    public void testDeleteKeyboard() {

        boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteKeyboard(103),
                "Unexpected exception thrown");

        assertEquals(result, true);
        assertEquals(inventoryFileDAO.keyboardMap.size(), testKeyboards.length - 1);
    }

    @Test
    public void testCreateKeyboard() throws IOException {

        Keyboard keyboard = new Keyboard(105, "New Keyboard", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 20, 200);

        Keyboard result = assertDoesNotThrow(() -> inventoryFileDAO.createKeyboard(keyboard),
                "Unexpected exception thrown");

        assertNotNull(result);
        Keyboard actual = inventoryFileDAO.getKeyboard(keyboard.getKeyboardId());
        assertEquals(actual.getKeyboardId(), result.getKeyboardId());
        assertEquals(actual.getKeyboardName(), keyboard.getKeyboardName());
    }

    @Test
    public void testUpdateKeyboard() throws IOException {

        Keyboard keyboard = testKeyboards[4];

        // Invoke
        Keyboard result = assertDoesNotThrow(() -> inventoryFileDAO.updateKeyboard(keyboard),
                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Keyboard actual = inventoryFileDAO.getKeyboard(keyboard.getKeyboardId());
        assertEquals(actual, keyboard);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
                .when(mockObjectMapper)
                .writeValue(any(File.class), any(Keyboard[].class));

        Keyboard keyboard = testKeyboards[1];

        assertThrows(IOException.class,
                () -> inventoryFileDAO.createKeyboard(keyboard),
                "IOException not thrown");
    }

    @Test
    public void testGetKeyboardNotFound() throws IOException {
        // Invoke
        Keyboard keyboard = inventoryFileDAO.getKeyboard(10000);

        // Analyze
        assertEquals(keyboard, null);
    }

    @Test
    public void testDeleteKeyboardNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteKeyboard(10000),
                "Unexpected exception thrown");

        // Analyze
        assertEquals(result, false);
        assertEquals(inventoryFileDAO.keyboardMap.size(), testKeyboards.length);
    }

    @Test
    public void testUpdateKeyboardNotFound() {
        // Setup
        Keyboard keyboard = new Keyboard(1, "ss", Keyboard.Size.FULL, Keyboard.SwitchColor.BLUE, 100, 2);
        // Invoke
        Keyboard result = assertDoesNotThrow(() -> inventoryFileDAO.updateKeyboard(keyboard),
                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the HeroFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
                .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"), Keyboard[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                () -> new InventoryFileDAO("doesnt_matter.txt", mockObjectMapper),
                "IOException not thrown");
    }

}
