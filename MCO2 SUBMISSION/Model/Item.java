package Model;

import Controller.*;
import View.*;


class Item {
    String name;
    String category;
    int qty;
    double price;

    /**
     * Constructs a new Item with the specified name, category and quantity.
     *
     * @param name the display name of the item
     * @param category an identifier/category for the item (e.g. "ALC" prefix for alcohol)
     * @param qty the initial quantity available (non-negative expected)
     */
    public Item(String name, String category, int qty) {
        this.name = name;
        this.category = category;
        this.qty = qty;
        this.price = 1.0;
    }

    /**
     * Constructs a new Item with the specified name, category, quantity and price.
     *
     * @param name the display name of the item
     * @param category an identifier/category for the item
     * @param qty the initial quantity available (non-negative expected)
     * @param price the unit price of the item
     */
    public Item(String name, String category, int qty, double price) {
        this.name = name;
        this.category = category;
        this.qty = qty;
        this.price = price;
    }

    /**
     * Returns the item name.
     *
     * @return the display name of the item
     */
    public String getName() { 
        return name; 
    }

    /**
     * Returns the item category/ID.
     *
     * @return the category identifier for the item
     */
    public String getCategory() { 
        return category; 
    }

    /**
     * Returns the available quantity of the item.
     *
     * @return the current quantity (units) of this item
     */
    public int getQty() { 
        return qty; 
    }

    /**
     * Returns the unit price of the item.
     *
     * @return the price per unit
     */
    public double getPrice() {
        return price;
    }

    /**
     * Adds the specified quantity to this item.
     *
     * @param qty the number of units to add (must be >= 0)
     */
    public void addQty(int qty) {
        if (qty < 0) {
            System.out.println("Quantity must be more than 0.");
        } else {
            this.qty += qty;
        }
    }

    /**
     * Returns a string representation of this item.
     *
     * @return formatted string containing name, category and quantity
     */
    @Override
    public String toString() {
        return String.format("%s (%s) x%d", name, category, qty);
    }

    /**
     * Decreases the quantity of this item by the specified amount.
     *
     * @param qty the number of units to remove (must be >= 0)
     */
    public void decreaseQty(int qty) {
        if (qty < 0) {
            System.out.println("Quantity must be more than 0.");
        } else if (qty >= this.qty) {
            this.qty = 0;
        } else {
            this.qty -= qty;
        }
    }
}