package Model;

import Controller.*;
import View.*;


import java.util.*;
import java.util.Random;

public class Ref {
    private final List<Item> items = new ArrayList<>();

    /**
     * Constructs a new Ref (refrigerated unit) with default items.
     */
    public Ref() {
        // Initialize refrigerated unit with default items
        initializeRef();
    }

    /**
     * Adds an item to the refrigerated unit with default price.
     *
     * @param name the item name
     * @param category the item category/ID
     * @param qty the quantity to add
     */
    // Add item with default price
    public void addItem(String name, String category, int qty) {
        addItem(name, category, qty, 1.0);
    }

    /**
     * Adds an item to the refrigerated unit with custom price.
     *
     * @param name the item name
     * @param category the item category/ID
     * @param qty the quantity to add
     * @param price the unit price
     */
    // Add item with custom price
    public void addItem(String name, String category, int qty, double price) {
        // Check if item already exists
        boolean check = false;

        for (Item item : items) {
            // If item exists, increase quantity
            if (item.getName().equalsIgnoreCase(name)) {
                item.addQty(qty);
                check = true;
            }
        }

        // If not found, add as a new item
        if (!check) {
            items.add(new Item(name, category, qty, price));
        }
    }

    /**
     * Adds an existing Item object to the refrigerated unit.
     *
     * @param item the Item to add
     */
    // Add existing item object
    public void addItem(Item item) {
        // Check if item already exists in inventory
        boolean check = false;

        for (Item item2 : items) {
            // If found, increase quantity
            if (item2.getName().equalsIgnoreCase(item.getName())) {
                item2.addQty(item.getQty());
                check = true;
            }
        }

        // If not found, add as a new item
        if (!check) {
            items.add(new Item(item.getName(), item.getCategory(), item.getQty()));
        }
    }

    /**
     * Removes an item from the refrigerated unit.
     *
     * @param item the Item to remove
     * @return true if the item was removed, false otherwise
     */
    // Remove item from inventory
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Returns all items in the refrigerated unit.
     *
     * @return list of all items
     */
    // Get all items in refrigerated unit
    public List<Item> getItems() {
        return items;
    }

    /**
     * Initializes the refrigerated unit with default chilled products.
     */
    // Initialize refrigerated unit with default products
    public void initializeRef() {
        items.clear();

        // Add milk products (MLK)
        // MLKXXXXX 
        addItem("Fresh", "MLK00001", 10, 68.00);
        addItem("Soy", "MLK00002", 10, 65.00);
        addItem("Almond", "MLK00003", 10, 95.00);

        // Add frozen products (FRZ)
        // FRZXXXXX 
        addItem("Hotdog", "FRZ00001", 10, 85.00);
        addItem("Nuggets", "FRZ00002", 10, 125.00);
        addItem("Tocino", "FRZ00003", 10, 155.00);

        // Add cheese products (CHS)
        // CHSXXXXX 
        addItem("Sliced", "CHS00001", 10, 145.00);
        addItem("Mozzarella", "CHS00002", 10, 185.00);
        addItem("Keso de Bola", "CHS00003", 10, 175.00);
    }

    public Item takeItemUnits(int index, int qtyRequested) {
        if (qtyRequested <= 0) {
            return null;
        }

        if (index < 0 || index >= items.size()) {
            return null;
        }

        Item it = items.get(index);
        int take = Math.min(qtyRequested, it.getQty());
        if (take <= 0) {
            return null;
        }

        Item taken = new Item(it.getName(), it.getCategory(), take);

        if (take == it.getQty()) {
            items.remove(index);
        } else {
            it.decreaseQty(take);
        }

        return taken;
    }
}