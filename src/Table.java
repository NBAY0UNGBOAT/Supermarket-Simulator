

import java.util.*;
import java.util.Random;

public class Table {
    private final List<Item> items = new ArrayList<>();

    public Table() {
        randomizedTable();
    }

    public void addItem(String name, String category, int qty) {
        addItem(name, category, qty, 1.0);
    }

    public void addItem(String name, String category, int qty, double price) {
        // items.add(new Item(name, category, qty));
        boolean check = false;

        for (Item item : items) {
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

    public void addItem(Item item) {
        // items.add(new Item(item.getName(), item.getCategory(), item.getQty()));
        boolean check = false;

        for (Item item2 : items) {
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

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void randomizedTable() {
        items.clear();

        int number;
        Random num = new Random();
        number = num.nextInt(4);
      
        if (number == 0) {
            addItem("Mango", "FRU00001", 10, 48.00);
            addItem("Apple", "FRU00002", 10, 65.00);
            addItem("Banana", "FRU00003", 10, 35.00);
            addItem("Orange", "FRU00004", 10, 45.00);
        } else if (number == 1) {
            addItem("Cabbage", "Veg00001", 10, 28.00);
            addItem("Lettuce", "Veg00002", 10, 32.00);
            addItem("Broccoli", "Veg00003", 10, 55.00);
            addItem("Carrot", "Veg00004", 10, 25.00);
        } else if (number == 2) {
            addItem("Baguette", "BRD00001", 10, 35.00);
            addItem("Croissant", "BRD00002", 10, 42.00);
            addItem("Bagel", "BRD00003", 10, 38.00);
            addItem("Pandesal", "BRD00004", 10, 12.00);
        } else {
            addItem("Brown", "EGG00001", 10, 180.00);
            addItem("Quail", "EGG00002", 10, 150.00);
            addItem("Free-range", "EGG00003", 10, 220.00);
            addItem("Egg White", "EGG00004", 10, 85.00);
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