package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.*;
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of user methods to interact with storage in json files
 * 
 * @author Justin Ronca & Andrew Photinakis
 */
@Component
public class UserFileDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(UserFileDAO.class.getName());
    private ObjectMapper objectMapper;
    private String filename;
    HashMap<String, User> userMap;

    /**
     * creates a UserFileDAO instance and json storage
     * 
     * @param filename     name of the file to store info in
     * @param objectMapper object mapper to write to file with
     * @throws IOException
     */
    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        loadFromJSON();
    }

    /**
     * 
     * Loads user data from a JSON file and updates the user map.
     * 
     * @throws IOException if an I/O error occurs while reading the JSON file
     * 
     * @return true if the user data was successfully loaded and updated, false
     *         otherwise
     */
    private boolean loadFromJSON() throws IOException {
        userMap = new HashMap<>();
        User[] userArray = objectMapper.readValue(new File(filename), User[].class);
        for (User user : userArray) {
            userMap.put(user.getUsername(), user);
        }
        return true;
    }

    /**
     * 
     * Saves the current user data to a JSON file.
     * 
     * @throws IOException if an I/O error occurs while writing to the JSON file
     * 
     * @return true if the user data was successfully saved to the JSON file, false
     *         otherwise
     */
    private boolean saveToJSON() throws IOException {
        User[] userArray = getUsers();
        objectMapper.writeValue(new File(filename), userArray);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String username) {
        synchronized (userMap) {
            if (userMap.containsKey(username))
                return userMap.get(username);
            else
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public User[] getUsers() {
        synchronized (userMap) {
            int num_users = userMap.size();
            User[] usersArray = new User[num_users];
            int i = 0;
            for (User user : userMap.values()) {
                usersArray[i] = user;
                i++;
            }
            return usersArray;
        }
    }

    /**
     * {@inheritDoc}
     */
    public User createUser(User user) throws IOException {
        synchronized (userMap) {
            User newUser = new User(user.getUsername(), user.getPassword());
            if (userMap.containsValue(newUser))
                return null;
            userMap.put(newUser.getUsername(), newUser);
            saveToJSON();
            return newUser;
        }
    }

    /**
     * {@inheritDoc}
     */
    public User updateUser(User userToUpdate) throws IOException {
        synchronized (userMap) {
            if (!userMap.containsKey(userToUpdate.getUsername()) || !userMap.containsValue(userToUpdate)) {
                return null;
            }
            userMap.put(userToUpdate.getUsername(), userToUpdate);
            saveToJSON();
            return userToUpdate;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean deleteAccount(String username) throws IOException {
        synchronized (userMap) {
            if (userMap.containsKey(username)) {
                userMap.remove(username);
                saveToJSON();
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */
    @Override
    public void login(User user) throws IOException {
        synchronized (userMap) {
            user.setLoginStatus(true);
            saveToJSON();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(User user) throws IOException {
        synchronized (userMap) {
            user.setLoginStatus(false);
            saveToJSON();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */
    @Override
    public boolean addToUserCart(User user, Keyboard keyboard, int quantity) throws IOException {
        synchronized (userMap) {
            try {
                if (!user.getUserCart().contains(keyboard)) {
                    ArrayList<Keyboard> currCart = user.getUserCart();
                    currCart.add(keyboard);
                    user.setUserCart(currCart);
                    saveToJSON();
                    return true;
                } else {
                    LOG.info("Already contains keyboard");
                    return false;
                }
            } catch (IOException e) {
                throw new IOException();
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */

    @Override
    public boolean removeFromCart(User user, Keyboard keyboard) throws IOException {
        synchronized (userMap) {
            try {
                if (user.getUserCart().contains(keyboard)) {
                    System.out.println("Containes keyboard");
                    ArrayList<Keyboard> currCart = user.getUserCart();
                    currCart.remove(keyboard);
                    user.setUserCart(currCart);
                    saveToJSON();
                    return true;
                } else {
                    System.out.println("Hello");
                    LOG.info("Does not contain keyboard");
                    return false;
                }
            } catch (IOException e) {
                System.out.println("Fucl");
                throw new IOException();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addToUserOrderHistory(User user, Keyboard keyboard, int quantity) throws IOException {
        synchronized (userMap) {
            try {
                if (keyboard.getQuantity() != 0) {
                    if (user.getUserOrderHistory().contains(keyboard)) {
                        Keyboard keyboardIndex = user.getUserOrderHistory().get(user.getUserCart().indexOf(keyboard));
                        keyboardIndex.setQuantity(keyboardIndex.getQuantity() + quantity);
                    } else {
                        user.getUserOrderHistory().add(new Keyboard(keyboard, quantity));
                    }
                    saveToJSON();
                    return true;
                } else {
                    LOG.info("No keyboards to add");
                }
                return true;
            } catch (IOException e) {
                throw new IOException();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean increaseQuantity(User user, Keyboard keyboard) throws IOException {
        synchronized (userMap) {
            try {
                if (keyboard.getQuantity() == 0) {
                    return false;
                }
                if (user.getUserCart().contains(keyboard)) {
                    Keyboard keyboardIndex = user.getUserCart().get(user.getUserCart().indexOf(keyboard));
                    keyboardIndex.setQuantity(keyboardIndex.getQuantity() + 1);
                    ArrayList<Keyboard> keys = user.getUserCart();
                    keys.set(user.getUserCart().indexOf(keyboard), keyboardIndex);
                    user.setUserCart(keys);
                    saveToJSON();
                    return true;
                }
                return false;
            } catch (Exception e) {
                throw new IOException();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean decreaseQuantity(User user, Keyboard keyboard) throws IOException {
        synchronized (userMap) {
            try {
                if (keyboard.getQuantity() == 0) {
                    return false;
                }
                if (user.getUserCart().contains(keyboard)) {
                    Keyboard keyboardIndex = user.getUserCart().get(user.getUserCart().indexOf(keyboard));
                    keyboardIndex.setQuantity(keyboardIndex.getQuantity() - 1);
                    ArrayList<Keyboard> keys = user.getUserCart();
                    keys.set(user.getUserCart().indexOf(keyboard), keyboardIndex);
                    user.setUserCart(keys);
                    saveToJSON();
                    return true;
                }
                return false;
            } catch (Exception e) {
                throw new IOException();
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IOException
     */
    @Override
    public boolean clearUserCart(User user) throws IOException {
        synchronized (userMap) {
            if (user.getUserCart() != null) {
                user.setUserCart(new ArrayList<Keyboard>());
                saveToJSON();
                return true;
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Keyboard> getUserShoppingCart(User user) {
        synchronized (userMap) {
            return user.getUserCart();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Keyboard getKeyboardFromCart(User user, Keyboard keyboard) throws IOException {
        synchronized (userMap) {
            if (!user.getUserCart().contains(keyboard)) {
                return null;
            } else {
                return user.getUserCart().get(user.getUserCart().indexOf(keyboard));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<Keyboard> getUserOrderHistory(String username) throws IOException {
        synchronized (userMap) {
            if (userMap.containsKey(username))
                return userMap.get(username).getUserOrderHistory();
            else
                return null;
        }
    }

}