package Model;

import Controller.*;
import View.*;
import java.util.*;

/**
 * Manages shopper's inventory with equipment support
 * Equipment types: HANDS (max 2), BASKET (max 15), CART (max 30)
 */
public class ShopperInventory {
    // Equipment types with different capacity limits
    public enum EquipmentType {
        HANDS("Hands"),
        BASKET("Basket"),
        CART("Cart");

        private String displayName;

        EquipmentType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // Get carrying capacity for this equipment type
        public int getCapacity() {
            switch (this) {
                case HANDS:
                    return 2;
                case BASKET:
                    return 15;
                case CART:
                    return 30;
                default:
                    return 0;
            }
        }
    }

    // Current equipment equipped
    private EquipmentType currentEquipment;
    // Inventory of products (Key: baseProductId)
    private Map<String, InventoryItem> inventory;
    // Whether basket/cart is equipped
    private boolean isEquipmentEquipped = false;
    // Store last checkout receipt
    private String lastReceipt = null;
    // Reference to store inventory for product info
    private StoreInventory storeInventory;

    /**
     * Constructs a new ShopperInventory with the specified store inventory reference.
     *
     * @param storeInventory reference to the store's inventory
     */
    public ShopperInventory(StoreInventory storeInventory) {
        // Initialize shopper inventory with default equipment (hands)
        this.currentEquipment = EquipmentType.HANDS;
        this.inventory = new LinkedHashMap<>();
        this.isEquipmentEquipped = false;
        this.lastReceipt = null;
        this.storeInventory = storeInventory;
    }

    /**
     * Adds a product to inventory
     * @return true if successful, false if inventory is full
     */
    public boolean addProduct(String name, String productId, double price) {
        // Add single product to inventory
        return addProduct(productId, name, 1, price);
    }

    /**
     * Adds a product to inventory with specific quantity and original price
     * @param productId Base Product ID
     * @param name Product name
     * @param quantity Quantity to add
     * @param price Price per unit (after discount)
     * @param originalPrice Price before discount
     * @return true if successful, false if inventory is full
     */
    public boolean addProduct(String productId, String name, int quantity, double price, double originalPrice) {
        if (getTotalQuantity() + quantity > currentEquipment.getCapacity()) {
            return false;
        }

        // Try to take products from store
        List<String> serialNumbers = storeInventory.takeProducts(productId, quantity);
        if (serialNumbers.isEmpty()) {
            return false;  // Not enough available in store
        }

        if (inventory.containsKey(productId)) {
            // Add serial numbers to existing item
            inventory.get(productId).addSerialNumbers(serialNumbers);
        } else {
            // Create new item with serial numbers
            inventory.put(productId, new InventoryItem(name, productId, price, serialNumbers, originalPrice));
        }
        return true;
    }

    /**
     * Adds a product to inventory with specific quantity
     * @param productId Base Product ID
     * @param name Product name
     * @param quantity Quantity to add
     * @param price Price per unit
     * @return true if successful, false if inventory is full
     */
    public boolean addProduct(String productId, String name, int quantity, double price) {
        return addProduct(productId, name, quantity, price, price);
    }

    /**
     * Removes a product from inventory
     */
    public boolean removeProduct(String productId, int quantity) {
        if (!inventory.containsKey(productId)) {
            return false;
        }

        InventoryItem item = inventory.get(productId);
        int currentQuantity = item.getQuantity();
        
        if (currentQuantity < quantity) {
            return false;  // Can't remove more than available
        }
        
        // Remove the specified quantity of items
        for (int i = 0; i < quantity && i < currentQuantity; i++) {
            item.removeSerialNumber(currentQuantity - 1 - i);
        }

        if (item.getQuantity() <= 0) {
            inventory.remove(productId);
        }
        
        // Return items to store
        storeInventory.returnProducts(productId, quantity);
        return true;
    }

    /**
     * Removes a product from inventory by product name
     */
    public boolean removeProductByName(String productName, int quantity) {
        for (Map.Entry<String, InventoryItem> entry : inventory.entrySet()) {
            if (entry.getValue().getName().equals(productName)) {
                return removeProduct(entry.getKey(), quantity);
            }
        }
        return false;
    }

    /**
     * Check if inventory is full
     */
    public boolean isFull() {
        return getTotalQuantity() >= currentEquipment.getCapacity();
    }

    /**
     * Get total number of products in inventory
     */
    public int getTotalQuantity() {
        return inventory.values().stream().mapToInt(InventoryItem::getQuantity).sum();
    }

    /**
     * Get number of unique products
     */
    public int getUniqueProductCount() {
        return inventory.size();
    }

    /**
     * Get total price of all products
     */
    public double getTotalPrice() {
        return inventory.values().stream().mapToDouble(InventoryItem::getTotalPrice).sum();
    }

    /**
     * Change equipment (and clear inventory on change)
     */
    public void setEquipment(EquipmentType equipment) {
        this.currentEquipment = equipment;
        // Note: In a real scenario, you might want to handle overflow differently
        // For now, we clear inventory when changing equipment
        if (getTotalQuantity() > equipment.getCapacity()) {
            System.out.println("Warning: Inventory exceeds capacity for " + equipment.name());
        }
    }

    /**
     * Equip the current equipment (basket or cart)
     */
    public void equipCurrentEquipment() {
        if (currentEquipment != EquipmentType.HANDS) {
            isEquipmentEquipped = true;
        }
    }

    /**
     * Return/unequip the current equipment
     */
    public void returnEquipment() {
        if (isEquipmentEquipped) {
            isEquipmentEquipped = false;
            currentEquipment = EquipmentType.HANDS;
        }
    }

    /**
     * Check if equipment is currently equipped
     */
    public boolean isEquipmentEquipped() {
        return isEquipmentEquipped;
    }

    /**
     * Get current equipment
     */
    public EquipmentType getEquipment() {
        return currentEquipment;
    }

    /**
     * Get all inventory items
     */
    public Collection<InventoryItem> getItems() {
        return inventory.values();
    }

    /**
     * Clear inventory
     */
    public void clear() {
        inventory.clear();
    }

    /**
     * Check if inventory is empty
     */
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    /**
     * Get available capacity
     */
    public int getAvailableCapacity() {
        return currentEquipment.getCapacity() - getTotalQuantity();
    }

    /**
     * Get product quantity by product name
     */
    public int getProductQuantity(String productName) {
        for (InventoryItem item : inventory.values()) {
            if (item.getName().equals(productName)) {
                return item.getQuantity();
            }
        }
        return 0;
    }

    /**
     * Get all product names currently in inventory
     */
    public java.util.List<String> getAllProductNames() {
        java.util.List<String> names = new java.util.ArrayList<>();
        for (InventoryItem item : inventory.values()) {
            names.add(item.getName());
        }
        return names;
    }

    /**
     * Generate receipt for checkout
     */
    public String generateReceipt() {
        if (inventory.isEmpty()) {
            return null;  // Cannot checkout with no items
        }
        
        StringBuilder receipt = new StringBuilder();
        receipt.append("ITEMS PURCHASED:\n");
        receipt.append("════════════════════════════════════\n\n");
        
        double totalPrice = 0;
        double totalOriginalPrice = 0;
        
        for (InventoryItem item : inventory.values()) {
            String name = item.getName();
            List<String> serialNumbers = item.getSerialNumbers();
            int qty = item.getQuantity();
            double price = item.getPricePerUnit();
            double originalPrice = item.getOriginalPrice();
            double itemTotal = price * qty;
            double originalItemTotal = originalPrice * qty;
            double discount = originalItemTotal - itemTotal;
            
            receipt.append(String.format("%s\n", name));
            receipt.append(String.format("Quantity: %d\n", qty));
            
            // List all serial numbers
            for (String serial : serialNumbers) {
                receipt.append(String.format("%s\n", serial));
            }
            
            if (discount > 0) {
                receipt.append(String.format("Discount: -₱%.2f\n", discount));
                receipt.append(String.format("Final Price: ₱%.2f × %d = ₱%.2f\n\n", price, qty, itemTotal));
            } else {
                receipt.append(String.format("Price: ₱%.2f × %d = ₱%.2f\n\n", price, qty, itemTotal));
            }
            
            totalPrice += itemTotal;
            totalOriginalPrice += originalItemTotal;
        }
        
        receipt.append("════════════════════════════════════\n");
        receipt.append(String.format("Original Total: ₱%.2f\n", totalOriginalPrice));
        
        double totalDiscount = totalOriginalPrice - totalPrice;
        if (totalDiscount > 0) {
            receipt.append(String.format("Total Discount: -₱%.2f\n", totalDiscount));
        }
        
        receipt.append(String.format("FINAL TOTAL: ₱%.2f\n", totalPrice));
        receipt.append("════════════════════════════════════\n");
        receipt.append("Thank you for shopping!\n");
        
        this.lastReceipt = receipt.toString();
        return lastReceipt;
    }
    
    /**
     * Get the last checkout receipt
     */
    public String getLastReceipt() {
        return lastReceipt;
    }
    
    /**
     * Clear inventory after checkout
     */
    public void checkout() {
        inventory.clear();
        currentEquipment = EquipmentType.HANDS;
        isEquipmentEquipped = false;
    }

    /**
     * Get inventory display string
     */
    public String getInventoryDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTORY ===\n");
        sb.append("Equipment: ").append(currentEquipment.getDisplayName()).append("\n");
        sb.append("Total Items: ").append(getTotalQuantity()).append("/").append(currentEquipment.getCapacity()).append("\n");
        sb.append("Unique Products: ").append(getUniqueProductCount()).append("\n");
        sb.append("Total Price: ₱").append(String.format("%.2f", getTotalPrice())).append("\n");
        sb.append("\n--- Items ---\n");

        if (inventory.isEmpty()) {
            sb.append("(Empty)\n");
        } else {
            for (InventoryItem item : inventory.values()) {
                sb.append(item.toString()).append("\n");
            }
        }

        return sb.toString();
    }
}
