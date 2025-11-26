package Model;

import Controller.*;
import View.*;


import java.util.*;

/**
 * Represents a unique product in inventory with quantity and price info
 * Each instance of a product has its own unique serial number
 */
public class InventoryItem {
    private String name;
    private String baseProductId;  // Base product ID (e.g., CHK00001)
    private List<String> serialNumbers;  // Unique serial number for each instance
    private double pricePerUnit;
    double originalPrice;  // Track original price before discounts

    /**
     * Constructs an InventoryItem with the specified properties.
     *
     * @param name the product name
     * @param baseProductId the base product ID
     * @param pricePerUnit the unit price
     * @param serialNumbers list of unique serial numbers for each unit
     */
    public InventoryItem(String name, String baseProductId, double pricePerUnit, List<String> serialNumbers) {
        // Initialize inventory item
        this.name = name;
        this.baseProductId = baseProductId;
        this.serialNumbers = new ArrayList<>(serialNumbers);
        this.pricePerUnit = pricePerUnit;
        this.originalPrice = pricePerUnit;
    }

    /**
     * Constructs an InventoryItem with original price tracking.
     *
     * @param name the product name
     * @param baseProductId the base product ID
     * @param pricePerUnit the current unit price
     * @param serialNumbers list of unique serial numbers
     * @param originalPrice the original price before discount
     */
    public InventoryItem(String name, String baseProductId, double pricePerUnit, List<String> serialNumbers, double originalPrice) {
        // Initialize inventory item with price history
        this.name = name;
        this.baseProductId = baseProductId;
        this.serialNumbers = new ArrayList<>(serialNumbers);
        this.pricePerUnit = pricePerUnit;
        this.originalPrice = originalPrice;
    }

    /**
     * Adds more serial numbers to this item.
     *
     * @param newSerials list of serial numbers to add
     */
    public void addSerialNumbers(List<String> newSerials) {
        this.serialNumbers.addAll(newSerials);
    }

    /**
     * Removes a serial number at the specified index.
     *
     * @param index the index of the serial number to remove
     */
    public void removeSerialNumber(int index) {
        if (index >= 0 && index < serialNumbers.size()) {
            serialNumbers.remove(index);
        }
    }

    /**
     * Gets the item name.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the base product ID.
     *
     * @return the product ID
     */
    public String getBaseProductId() {
        return baseProductId;
    }

    /**
     * Gets the product ID (backward compatibility).
     *
     * @return the base product ID
     */
    public String getProductId() {
        return baseProductId;  // For backward compatibility
    }

    /**
     * Gets the quantity of this item.
     *
     * @return quantity based on number of serial numbers
     */
    public int getQuantity() {
        return serialNumbers.size();
    }

    /**
     * Gets the list of serial numbers for this item.
     *
     * @return copy of the serial numbers list
     */
    public List<String> getSerialNumbers() {
        return new ArrayList<>(serialNumbers);
    }

    /**
     * Gets the price per unit.
     *
     * @return unit price
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Gets the original price before any discounts.
     *
     * @return original unit price
     */
    public double getOriginalPrice() {
        return originalPrice;
    }

    /**
     * Calculates the total price for all units.
     *
     * @return quantity * price per unit
     */
    public double getTotalPrice() {
        return getQuantity() * pricePerUnit;
    }

    /**
     * Returns a string representation of this inventory item.
     *
     * @return formatted string with name, ID, quantity and total price
     */
    @Override
    public String toString() {
        return String.format("%s (%s) x%d - ₱%.2f (Total: ₱%.2f)", 
            name, baseProductId, getQuantity(), pricePerUnit, getTotalPrice());
    }
}
