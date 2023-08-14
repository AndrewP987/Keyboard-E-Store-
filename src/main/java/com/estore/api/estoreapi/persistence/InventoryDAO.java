package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Keyboard;
import com.estore.api.estoreapi.model.Keyboard.Size;
import com.estore.api.estoreapi.model.Keyboard.SwitchColor;

public interface InventoryDAO {
    /**
     * Retrieves all {@linkplain Keyboard keyboard}
     * 
     * @return An array of {@link Keyboard keyboard} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Keyboard[] getKeyboards() throws IOException;

    /**
     * Finds all {@linkplain Keyboard keyboards} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Keyboard keyboards} whose nemes contains the given
     *         text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Keyboard[] findKeyboard(String containsText) throws IOException;

    /**
     * Retrieves a {@linkplain Keyboard keyboard} with the given id
     * 
     * @param id The id of the {@link Keyboard keyboards} to get
     * 
     * @return a {@link Keyboard keyboard} object with the matching id
     *         <br>
     *         null if no {@link Keyboard keyboard} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Keyboard getKeyboard(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Keyboard keyboard}
     * 
     * @param keyboard {@linkplain Keyboard keyboard} object to be created and saved
     *                 <br>
     *                 The id of the keyboard object is ignored and a new unique id
     *                 is assigned
     *
     * @return new {@link Keyboard keyboard} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    Keyboard createKeyboard(Keyboard keyboard) throws IOException;

    /**
     * Updates and saves a {@linkplain Keyboard keyboard}
     * 
     * @param {@link Keyboard keyboard} object to be updated and saved
     * 
     * @return updated {@link Keyboard keyboard} if successful, null if
     *         {@link Keyboard keyboard} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Keyboard updateKeyboard(Keyboard keyboard) throws IOException;

    /**
     * Deletes a {@linkplain Keyboard keyboard} with the given id
     * 
     * @param id The id of the {@link Keyboard keyboard}
     * 
     * @return true if the {@link Keyboard keyboard} was deleted
     *         <br>
     *         false if keyboard with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteKeyboard(int id) throws IOException;

    /**
     * 
     * Returns an array of Keyboard objects filtered by their price, based on the
     * given range.
     * 
     * @param fromPrice a String representation of the lower limit of the price
     *                  range.
     * @param toPrice   a String representation of the upper limit of the price
     *                  range.
     * @return a Keyboard array that contains all Keyboard objects whose price falls
     *         within the given range.
     */
    Keyboard[] getFilteredKeyboards(String fromPrice, String toPrice)
            throws IOException;

}
