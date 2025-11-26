package Model;

import Controller.*;
import View.*;


import java.util.*;
import java.util.Random;

public class Shelf {
    private final List<Item> items = new ArrayList<>();

    /**
     * Constructs a new Shelf with default items.
     */
    public Shelf() {
        // Initialize shelf with default items
        initializeShelf();
    }

    /**
     * Adds an item to the shelf with default price.
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
     * Adds an item to the shelf with custom price.
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
     * Adds an existing Item object to the shelf.
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
     * Removes an item from the shelf.
     *
     * @param item the Item to remove
     * @return true if the item was removed, false otherwise
     */
    // Remove item from inventory
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Returns all items on the shelf.
     *
     * @return list of all items
     */
    // Get all items on shelf
    public List<Item> getItems() {
        return items;
    }

    public void initializeShelf() {
        items.clear();

        int number;
        Random num = new Random();
        number = num.nextInt(8);
      
        if (number == 0) {
            addItem("Oatmeal", "CER00001", 10, 95.00);
            addItem("Barley", "CER00002", 10, 85.00);
            addItem("Quinoa", "CER00003", 10, 120.00);
            addItem("Cereal", "CER00004", 10, 75.00);

            addItem("Instant", "NDL00001", 10, 8.50);
            addItem("Ramen", "NDL00002", 10, 9.50);
            addItem("Miswa", "NDL00003", 10, 15.00);
            addItem("Udon", "NDL00004", 10, 12.50);
        } else if (number == 1) {
            addItem("Candies", "SNK00001", 10, 45.00);
            addItem("Junkfood", "SNK00002", 10, 55.00);
            addItem("Cookies", "SNK00003", 10, 35.00);
            addItem("Skyflakes", "SNK00004", 10, 18.00);

            addItem("Canned Tuna", "CAN00001", 10, 42.00);
            addItem("Sardines", "CAN00002", 10, 35.00);
            addItem("Soup", "CAN00003", 10, 32.00);
            addItem("San Marino", "CAN00004", 10, 48.00);
        } else if (number == 2) {
            addItem("Salt", "CON00001", 10, 25.00);
            addItem("Pepper", "CON00002", 10, 65.00);
            addItem("Paprika", "CON00003", 10, 75.00);
            addItem("Spice", "CON00004", 10, 55.00);

            addItem("Sparkling", "SFT00001", 10, 55.00);
            addItem("Coke", "SFT00002", 10, 45.00);
            addItem("Sprite", "SFT00003", 10, 45.00);
            addItem("Dew", "SFT00004", 10, 40.00);
        } else if (number == 3) {
            addItem("Orange", "JUC00001", 10, 65.00);
            addItem("Apple", "JUC00002", 10, 72.00);
            addItem("Watermelon", "JUC00003", 10, 68.00);
            addItem("Mango", "JUC00004", 10, 78.00);

            addItem("Tequila", "ALC00001", 10, 800.00);
            addItem("Beer", "ALC00002", 10, 50.00);
            addItem("Vodka", "ALC00003", 10, 950.00);
            addItem("Soju", "ALC00004", 10, 400.00);
        } else if (number == 4) {
            addItem("Detergent", "CLE00001", 10, 85.00);
            addItem("Bleach", "CLE00002", 10, 45.00);
            addItem("Dish Soap", "CLE00003", 10, 35.00);
            addItem("Cleanex", "CLE00004", 10, 55.00);

            addItem("Broom", "HOM00001", 10, 125.00);
            addItem("Mop", "HOM00002", 10, 145.00);
            addItem("Plunger", "HOM00003", 10, 165.00);
            addItem("Walis tingting", "HOM00004", 10, 95.00);
        } else if (number == 5) {
            addItem("Shampoo", "HAR00001", 10, 120.00);
            addItem("Conditioner", "HAR00002", 10, 130.00);
            addItem("Hair Wax", "HAR00003", 10, 150.00);
            addItem("Hair Gel", "HAR00004", 10, 110.00);

            addItem("Body Soap", "BOD00001", 10, 45.00);
            addItem("Body Wash", "BOD00002", 10, 95.00);
            addItem("Shower Gel", "BOD00003", 10, 85.00);
            addItem("Body Scrubber", "BOD00004", 10, 35.00);
        } else if (number == 6) {
            addItem("Toothbrush", "DEN00001", 10, 28.00);
            addItem("Toothpaste", "DEN00002", 10, 68.00);
            addItem("Dental Floss", "DEN00003", 10, 45.00);
            addItem("Mouthwash", "DEN00004", 10, 75.00);

            addItem("Shirt", "CLO00001", 10, 299.00);
            addItem("Short", "CLO00002", 10, 249.00);
            addItem("Pants", "CLO00003", 10, 399.00);
            addItem("Jacket", "CLO00004", 10, 899.00);
        } else {
            addItem("Paper", "STN00001", 10, 65.00);
            addItem("Tape", "STN00002", 10, 35.00);
            addItem("Pencil", "STN00003", 10, 12.00);
            addItem("Ballpen", "STN00004", 10, 8.00);

            addItem("Cat Food", "PET00001", 10, 125.00);
            addItem("Dog Food", "PET00002", 10, 135.00);
            addItem("Bird Food", "PET00003", 10, 85.00);
            addItem("Tiger Food", "PET00004", 10, 450.00);
        }
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