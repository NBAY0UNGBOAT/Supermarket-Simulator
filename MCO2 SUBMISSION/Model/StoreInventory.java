package Model;

import Controller.*;
import View.*;

import java.util.*;

/**
 * Manages the supermarket's inventory of products
 * Tracks available quantities and generates unique serial numbers for each product instance
 */
public class StoreInventory {
    // Default initial quantity for all products
    private static final int INITIAL_QUANTITY = 999;
    
    // Map of base product ID -> current quantity available
    private Map<String, Integer> productQuantities = new HashMap<>();
    
    // Map of base product ID -> next serial number to assign
    private Map<String, Integer> serialNumberCounters = new HashMap<>();
    
    // Map of base product ID -> product name
    private Map<String, String> productNames = new HashMap<>();
    
    // Map of base product ID -> product price
    private Map<String, Double> productPrices = new HashMap<>();

    /**
     * Constructs a new StoreInventory with all products initialized.
     */
    public StoreInventory() {
        // Initialize store inventory with products
        initializeInventory();
    }

    /**
     * Initialize the store inventory with all products
     */
    private void initializeInventory() {
        // All product codes from TileInventory
        String[][] productCategories = {
            // Chilled
            {"CHK00001", "Chicken Thigh Fillet", "280.00"},
            {"CHK00002", "Chicken Breast Fillet", "320.00"},
            {"CHK00003", "Ground Chicken", "250.00"},
            {"BEF00001", "Beef Rib", "450.00"},
            {"BEF00002", "Beef Shank", "380.00"},
            {"BEF00003", "Ground Beef", "420.00"},
            {"SEA00001", "Tilapia", "320.00"},
            {"SEA00002", "Sugpo", "580.00"},
            {"SEA00003", "Squid", "420.00"},
            // Shelf - Alcohol
            {"ALC00001", "Beer", "50.00"},
            {"ALC00002", "Wine", "450.00"},
            {"ALC00003", "Whiskey", "850.00"},
            {"ALC00004", "Vodka", "750.00"},
            {"ALC00005", "Brandy", "650.00"},
            {"ALC00006", "Champagne", "1200.00"},
            {"ALC00007", "Rum", "580.00"},
            {"ALC00008", "Gin", "520.00"},
            // Shelf - Condiments
            {"CON00001", "Salt", "25.00"},
            {"CON00002", "Pepper", "35.00"},
            {"CON00003", "Soy Sauce", "65.00"},
            {"CON00004", "Vinegar", "48.00"},
            {"CON00005", "Butter", "185.00"},
            {"CON00006", "Cooking Oil", "95.00"},
            {"CON00007", "Honey", "125.00"},
            {"CON00008", "Corn Syrup", "75.00"},
            // Shelf - Soft Drinks
            {"SFT00001", "Sparkling Water", "55.00"},
            {"SFT00002", "Coke", "45.00"},
            {"SFT00003", "Sprite", "45.00"},
            {"SFT00004", "Mountain Dew", "50.00"},
            {"SFT00005", "Royal", "40.00"},
            {"SFT00006", "7-Up", "45.00"},
            {"SFT00007", "Gatorade", "65.00"},
            {"SFT00008", "Lemonade", "35.00"},
            // Shelf - Juice
            {"JUC00001", "Orange Juice", "65.00"},
            {"JUC00002", "Pineapple Juice", "55.00"},
            {"JUC00003", "Mango Juice", "60.00"},
            {"JUC00004", "Apple Juice", "58.00"},
            {"JUC00005", "Grape Juice", "62.00"},
            {"JUC00006", "Tomato Juice", "48.00"},
            {"JUC00007", "Carrot Juice", "52.00"},
            {"JUC00008", "Mixed Fruit Juice", "75.00"},
            // Shelf - Cereal
            {"CER00001", "Oatmeal", "95.00"},
            {"CER00002", "Corn Flakes", "125.00"},
            {"CER00003", "Honey Bunches", "115.00"},
            {"CER00004", "Wheat Bran", "85.00"},
            {"CER00005", "Rice Krispies", "105.00"},
            {"CER00006", "Frosted Flakes", "110.00"},
            {"CER00007", "Granola", "145.00"},
            {"CER00008", "Bran Flakes", "95.00"},
            // Shelf - Noodles
            {"NDL00001", "Instant Noodles", "8.50"},
            {"NDL00002", "Ramen", "25.00"},
            {"NDL00003", "Lomi", "12.00"},
            {"NDL00004", "Pancit Canton", "15.00"},
            {"NDL00005", "Spaghetti", "35.00"},
            {"NDL00006", "Pasta", "42.00"},
            {"NDL00007", "Udon", "48.00"},
            {"NDL00008", "Glass Noodles", "38.00"},
            // Shelf - Canned
            {"CAN00001", "Canned Tuna", "42.00"},
            {"CAN00002", "Canned Sardines", "28.00"},
            {"CAN00003", "Canned Beans", "35.00"},
            {"CAN00004", "Canned Corn", "32.00"},
            {"CAN00005", "Canned Peas", "30.00"},
            {"CAN00006", "Canned Mushroom", "45.00"},
            {"CAN00007", "Canned Coconut Milk", "55.00"},
            {"CAN00008", "Canned Tomato", "38.00"},
            // Shelf - Snacks
            {"SNK00001", "Candies", "45.00"},
            {"SNK00002", "Cookies", "65.00"},
            {"SNK00003", "Crackers", "55.00"},
            {"SNK00004", "Chips", "48.00"},
            {"SNK00005", "Peanuts", "75.00"},
            {"SNK00006", "Chocolate", "85.00"},
            {"SNK00007", "Wafers", "62.00"},
            {"SNK00008", "Pretzels", "58.00"},
            // Table - Fruits
            {"FRU00001", "Apples", "65.00"},
            {"FRU00002", "Bananas", "45.00"},
            {"FRU00003", "Oranges", "55.00"},
            {"FRU00004", "Grapes", "125.00"},
            // Fridge - Milk
            {"MLK00001", "Whole Milk", "85.00"},
            {"MLK00002", "Skim Milk", "80.00"},
            {"MLK00003", "Almond Milk", "95.00"},
            {"MLK00004", "Oat Milk", "100.00"},
            // Fridge - Frozen
            {"FRZ00001", "Frozen Pizza", "240.00"},
            {"FRZ00002", "Frozen Veggies", "120.00"},
            {"FRZ00003", "Ice Cream", "180.00"},
            {"FRZ00004", "Frozen Berries", "150.00"},
            // Fridge - Cheese
            {"CHE00001", "Cheddar Cheese", "220.00"},
            {"CHE00002", "Mozzarella Cheese", "200.00"},
            {"CHE00003", "Blue Cheese", "280.00"},
            // Shelf - Pet Food
            {"PET00001", "Dog Food", "320.00"},
            {"PET00002", "Cat Food", "280.00"},
            {"PET00003", "Bird Food", "150.00"},
            {"PET00004", "Fish Food", "95.00"},
            // Shelf - Stationery
            {"STA00001", "Notebook", "85.00"},
            {"STA00002", "Pen Pack", "45.00"},
            {"STA00003", "Pencil Pack", "35.00"},
            {"STA00004", "Eraser", "15.00"},
            // Shelf - Clothes
            {"CLO00001", "T-Shirt", "350.00"},
            {"CLO00002", "Jeans", "850.00"},
            {"CLO00003", "Socks", "120.00"},
            {"CLO00004", "Cap", "250.00"},
            // Shelf - Dental Care
            {"DEN00001", "Toothpaste", "125.00"},
            {"DEN00002", "Toothbrush", "85.00"},
            {"DEN00003", "Mouthwash", "180.00"},
            {"DEN00004", "Dental Floss", "95.00"}
        };
        
        for (String[] product : productCategories) {
            String productId = product[0];
            String productName = product[1];
            double price = Double.parseDouble(product[2]);
            
            productQuantities.put(productId, INITIAL_QUANTITY);
            serialNumberCounters.put(productId, 1);
            productNames.put(productId, productName);
            productPrices.put(productId, price);
        }
    }

    /**
     * Check if a product is available in the store
     */
    public boolean isAvailable(String productId) {
        return productQuantities.getOrDefault(productId, 0) > 0;
    }

    /**
     * Get available quantity for a product
     */
    public int getAvailableQuantity(String productId) {
        return productQuantities.getOrDefault(productId, 0);
    }

    /**
     * Take products from store and generate unique serial numbers
     * @param productId Base product ID
     * @param quantity Number of items to take
     * @return List of unique serial numbers, or empty list if not enough available
     */
    public List<String> takeProducts(String productId, int quantity) {
        if (!productQuantities.containsKey(productId)) {
            return new ArrayList<>();
        }
        
        int available = productQuantities.get(productId);
        if (available < quantity) {
            return new ArrayList<>();  // Not enough available
        }
        
        // Generate unique serial numbers
        List<String> serialNumbers = new ArrayList<>();
        int currentSerial = serialNumberCounters.get(productId);
        
        for (int i = 0; i < quantity; i++) {
            String serialNumber = productId.substring(0, 3) + String.format("%05d", currentSerial + i);
            serialNumbers.add(serialNumber);
        }
        
        // Update counters and quantities
        serialNumberCounters.put(productId, currentSerial + quantity);
        productQuantities.put(productId, available - quantity);
        
        return serialNumbers;
    }

    /**
     * Return products to store
     * @param productId Base product ID
     * @param quantity Number of items to return
     */
    public void returnProducts(String productId, int quantity) {
        if (productQuantities.containsKey(productId)) {
            int current = productQuantities.get(productId);
            productQuantities.put(productId, current + quantity);
        }
    }

    /**
     * Get product name from base product ID
     */
    public String getProductName(String productId) {
        return productNames.getOrDefault(productId, "Unknown Product");
    }

    /**
     * Get product price from base product ID
     */
    public double getProductPrice(String productId) {
        return productPrices.getOrDefault(productId, 0.0);
    }
}
