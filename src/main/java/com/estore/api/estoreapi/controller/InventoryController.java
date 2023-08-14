package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Keyboard;
import com.estore.api.estoreapi.persistence.InventoryDAO;

/**
 * Constructor that initializes the InventoryController with a inventoryDAO.
 * is a REST API controller class that implements CRUD
 * operations for Keyboard objects.
 * It uses the inventoryDAO class to interact with the persistence layer and
 * provides the API endpoint for keyboard operations.
 */
@RestController
@RequestMapping("keyboards")
public class InventoryController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDAO;

    /**
     * Constructor that initializes the InventoryController with a inventoryDAO.
     * 
     * @param inventoryDAO the inventoryDAO instance to be used by the
     *                     * Constructor that initializes the InventoryController
     *                     with a inventoryDAO.
     *                     .
     */
    public InventoryController(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    /**
     * A GET API endpoint that returns a Keyboard object with the specified id.
     * 
     * @param id the id of the keyboard to be retrieved.
     * @return a ResponseEntity with a status code of 200 (OK) if the keyboard is
     *         found,
     *         404 (NOT_FOUND) if the keyboard with the specified id does not exist,
     *         or 500 (INTERNAL_SERVER_ERROR) if an IOException occurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Keyboard> getKeyboard(@PathVariable int id) {
        LOG.info("GET /keyboards/" + id);
        try {
            Keyboard keyboard = inventoryDAO.getKeyboard(id);
            if (keyboard != null) {
                return new ResponseEntity<Keyboard>(keyboard, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A GET API endpoint that returns an array of all Keyboard objects.
     * 
     * @return a ResponseEntity with a status code of 200 (OK) if the keyboards are
     *         found,
     *         500 (INTERNAL_SERVER_ERROR) if an IOException occurs.
     */
    @GetMapping("")
    public ResponseEntity<Keyboard[]> getKeyboards() {
        LOG.info("GET /keyboards");

        try {
            Keyboard[] keyboards = inventoryDAO.getKeyboards();
            if (keyboards.length > 0) {
                return new ResponseEntity<Keyboard[]>(keyboards, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A GET API endpoint that returns an array of Keyboard objects with the
     * specified name.
     * 
     * @param name the name of the keyboards to be retrieved.
     * @return a ResponseEntity with a status code of 200 (OK) if the keyboards are
     *         found,
     *         500 (INTERNAL_SERVER_ERROR) if an IOException occurs.
     */
    @GetMapping("/")
    public ResponseEntity<Keyboard[]> searchKeyboards(@RequestParam String name) {
        LOG.info("GET /keyboards/?name=" + name);

        try {
            Keyboard[] userNames = inventoryDAO.findKeyboard(name);
            if (userNames != null) {
                return new ResponseEntity<Keyboard[]>(userNames, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A POST API endpoint that creates a new Keyboard object.
     * 
     * @param keyboard the Keyboard object to be created.
     * @return a ResponseEntity with a status code of 201 (CREATED) if the keyboard
     *         is successfully created,
     *         409 (CONFLICT) if a keyboard with the same id already exists,
     *         or 500 (INTERNAL_SERVER_ERROR) if an IOException occurs.
     */
    @PostMapping("")
    public ResponseEntity<Keyboard> createKeyboard(@RequestBody Keyboard keyboard) {
        LOG.info("POST /keyboards " + keyboard);

        try {
            Keyboard createdKeyboard = inventoryDAO.createKeyboard(keyboard);
            if (createdKeyboard == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<Keyboard>(createdKeyboard, HttpStatus.CREATED);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A PUT API endpoint that updates a Keyboard object.
     * 
     * @param keyboard the Keyboard object to be updated.
     * @return a ResponseEntity with a status code of 201 (UPDATED) if the keyboard
     *         is successfully updated. Otherwise,
     *         409 (CONFLICT), if the keybaord does not exist, or 500
     *         (INTERNAL_SERVER_ERROR) if an IOException occurs.
     */
    @PutMapping("")
    public ResponseEntity<Keyboard> updateKeyboard(@RequestBody Keyboard keyboard) {
        LOG.info("PUT /keyboards " + keyboard);

        try {
            Keyboard keyboardUpdated = inventoryDAO.updateKeyboard(keyboard);
            if (keyboardUpdated != null) {
                return new ResponseEntity<>(keyboardUpdated, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * A DELETE API endpoint that deletes a Keyboard object.
     * 
     * @param keyboard the Keyboard object to be deleted.
     * @return a ResponseEntity with a status code of 201 (DELETED) if the keyboard
     *         is successfully deleted,
     *         409 (CONFLICT) if the keyboard does not exist,
     *         or 500 (INTERNAL_SERVER_ERROR) if an IOException occurs.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Keyboard> deleteKeyboard(@PathVariable int id) {
        LOG.info("DELETE /keyboards/" + id);

        try {
            boolean isDeleted = inventoryDAO.deleteKeyboard(id);
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
     * 
     * A GET API endpoint that filters Keyboard objects by price range.
     * 
     * @param fromPrice the lower limit of the price range.
     * @param toPrice   the upper limit of the price range.
     * @return a ResponseEntity with a status code of 200 (OK) and an array of
     *         Keyboard objects
     *         filtered by price range if the operation is successful,
     *         or a ResponseEntity with a status code of 500 (INTERNAL_SERVER_ERROR)
     *         if an IOException occurs.
     * @throws IOException if an IOException occurs while accessing the
     *                     inventoryDAO.
     */
    @GetMapping("/filter/fromPrice={fromPrice}&toPrice={toPrice}")
    public ResponseEntity<Keyboard[]> filterKeyboards(@PathVariable String fromPrice,
            @PathVariable String toPrice) throws IOException {

        try {
            Keyboard[] keyboards = inventoryDAO.getFilteredKeyboards(fromPrice, toPrice);

            if (keyboards != null) {
                return new ResponseEntity<Keyboard[]>(keyboards, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
