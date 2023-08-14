package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Keyboard;
import com.estore.api.estoreapi.model.Keyboard.Size;
import com.estore.api.estoreapi.model.Keyboard.SwitchColor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Implements the functionality for JSON file-based peristance for Keyboards
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of
 * this
 * class and injects the instance into other classes as needed
 * 
 * @author Team 2
 */
@Component
public class InventoryFileDAO implements InventoryDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    HashMap<Integer, Keyboard> keyboardMap; // Provides a local cache of the keyboard objects
    // so that we don't need to read from the file
    // each time
    private ObjectMapper objectMapper; // Provides conversion between Keyboard
                                       // objects and JSON text format written
                                       // to the file
    private static int nextId; // The next Id to assign to a new keyboard
    private String filename; // Filename to read from and write to

    public static final int MAX_KEYBOARD_PRICE = 999;

    /**
     * Creates a Keyboard File Data Access Object
     * 
     * @param filename     Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization
     *                     and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public InventoryFileDAO(@Value("${inventory.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Generates the next id for a new {@linkplain Keyboard keyboard}
     * 
     * @return The next id
     */
    private synchronized static int nextKeyboardId() throws IOException {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Keyboard keyboards} from the hashmap
     * 
     * @return The array of {@link Keyboard keyboards}, may be empty
     */
    private Keyboard[] getKeyboardArray() {
        return getKeyboardArray(null);
    }

    /**
     * 
     * Returns an ArrayList of Keyboard objects filtered by their price, based on
     * the given range.
     * 
     * @param fromPrice the lower limit of the price range.
     * @param toPrice   the upper limit of the price range.
     * @return an ArrayList of Keyboard objects whose price falls within the given
     *         range.
     */
    public ArrayList<Keyboard> filteredByFromAndTwo(int fromPrice, int toPrice) {
        ArrayList<Keyboard> filtered = new ArrayList<Keyboard>();
        for (Keyboard cuKeyboard : this.keyboardMap.values()) {
            if (cuKeyboard.getPrice() >= fromPrice && cuKeyboard.getPrice() <= toPrice) {
                filtered.add(cuKeyboard);
            }
        }
        return filtered;
    }

    /**
     * Generates an array of {@linkplain Keyboard keyboards} from the tree map for
     * any
     * {@linkplain Keyboard keyboards} that contains the text specified by
     * containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Keyboard
     * keyboards}
     * in the hash map
     * 
     * @return The array of {@linkplain Keyboard keyboard} , may be empty
     */
    private Keyboard[] getKeyboardArray(String containsText) { // if containsText == null, no filter
        ArrayList<Keyboard> keyboardArrayList = new ArrayList<>();

        for (Keyboard keyboard : keyboardMap.values()) {

            if (containsText == null || keyboard.getKeyboardName().contains(containsText)) {
                keyboardArrayList.add(keyboard);
            }
        }

        Keyboard[] keyboardArray = new Keyboard[keyboardArrayList.size()];
        keyboardArrayList.toArray(keyboardArray);
        return keyboardArray;
    }

    /**
     * Saves the {@linkplain Keyboard keyboard} from the map into the file as an
     * array of JSON objects
     * 
     * @return true if the {@linkplain Keyboard keyboards} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Keyboard[] keyboardArray = getKeyboardArray();
        objectMapper.writeValue(new File(filename), keyboardArray);
        return true;
    }

    /**
     * Loads {@linkplain Keyboard keyboards} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        keyboardMap = new HashMap<>();
        nextId = 0;

        Keyboard[] keyboardArray = objectMapper.readValue(new File(filename), Keyboard[].class);
        for (Keyboard currKeyboard : keyboardArray) {
            keyboardMap.put(currKeyboard.getKeyboardId(), currKeyboard);
            if (currKeyboard.getKeyboardId() > nextId) {
                nextId = currKeyboard.getKeyboardId();
            }
        }
        ++nextId;
        return true;
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Keyboard[] getKeyboards() throws IOException {
        synchronized (keyboardMap) {
            return getKeyboardArray();
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Keyboard[] findKeyboard(String containsText) throws IOException {
        synchronized (keyboardMap) {
            return getKeyboardArray(containsText);
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Keyboard getKeyboard(int id) throws IOException {
        synchronized (keyboardMap) {
            if (keyboardMap.containsKey(id)) {
                return keyboardMap.get(id);
            } else {
                return null;
            }

        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Keyboard createKeyboard(Keyboard keyboard) throws IOException {
        synchronized (keyboardMap) {
            Keyboard newKeyboard = new Keyboard(nextKeyboardId(),
                    keyboard.getKeyboardName(), keyboard.getSize(), keyboard.getSwitchColor(),
                    keyboard.getPrice(), keyboard.getQuantity());
            if (keyboardMap.containsValue(newKeyboard)) {
                return null;
            }
            newKeyboard.setKeyboardId(nextId);
            keyboardMap.put(newKeyboard.getKeyboardId(), newKeyboard);
            save();
            return newKeyboard;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public Keyboard updateKeyboard(Keyboard keyboard) throws IOException {
        synchronized (keyboardMap) {
            if (!keyboardMap.containsKey(keyboard.getKeyboardId())) {
                return null;
            }
            keyboardMap.put(keyboard.getKeyboardId(), keyboard);
            save();
            return keyboard;
        }
    }

    /**
     ** {@inheritDoc}
     */
    @Override
    public boolean deleteKeyboard(int id) throws IOException {
        synchronized (keyboardMap) {
            if (keyboardMap.containsKey(id)) {
                keyboardMap.remove(id);
                return save();
            } else {
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Keyboard[] getFilteredKeyboards(String fromPrice, String toPrice) {
        ArrayList<Keyboard> res = new ArrayList<>();
        res.addAll(filteredByFromAndTwo(Integer.valueOf(fromPrice), Integer.valueOf(toPrice)));
        Keyboard[] keyboardArray = res.toArray(new Keyboard[0]);
        return keyboardArray;
    }

}
