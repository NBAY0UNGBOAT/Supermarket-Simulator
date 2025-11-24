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
    private double originalPrice;  // Track original price before discounts

    public InventoryItem(String name, String baseProductId, double pricePerUnit, List<String> serialNumbers) {
        this.name = name;
        this.baseProductId = baseProductId;
        this.serialNumbers = new ArrayList<>(serialNumbers);
        this.pricePerUnit = pricePerUnit;
        this.originalPrice = pricePerUnit;
    }

    public InventoryItem(String name, String baseProductId, double pricePerUnit, List<String> serialNumbers, double originalPrice) {
        this.name = name;
        this.baseProductId = baseProductId;
        this.serialNumbers = new ArrayList<>(serialNumbers);
        this.pricePerUnit = pricePerUnit;
        this.originalPrice = originalPrice;
    }

    public void addSerialNumbers(List<String> newSerials) {
        this.serialNumbers.addAll(newSerials);
    }

    public void removeSerialNumber(int index) {
        if (index >= 0 && index < serialNumbers.size()) {
            serialNumbers.remove(index);
        }
    }

    public String getName() {
        return name;
    }

    public String getBaseProductId() {
        return baseProductId;
    }

    public String getProductId() {
        return baseProductId;  // For backward compatibility
    }

    public int getQuantity() {
        return serialNumbers.size();
    }

    public List<String> getSerialNumbers() {
        return new ArrayList<>(serialNumbers);
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getTotalPrice() {
        return getQuantity() * pricePerUnit;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) x%d - ₱%.2f (Total: ₱%.2f)", 
            name, baseProductId, getQuantity(), pricePerUnit, getTotalPrice());
    }
}
