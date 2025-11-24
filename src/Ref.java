

import java.util.*;
import java.util.Random;

public class Ref {
    private final List<Item> items = new ArrayList<>();

    public Ref() {
        initializeRef();
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

    public void initializeRef() {
        items.clear();

        // MLKXXXXX 
        addItem("Fresh", "MLK00001", 10, 68.00);
        addItem("Soy", "MLK00002", 10, 65.00);
        addItem("Almond", "MLK00003", 10, 95.00);

        // FRZXXXXX 
        addItem("Hotdog", "FRZ00001", 10, 85.00);
        addItem("Nuggets", "FRZ00002", 10, 125.00);
        addItem("Tocino", "FRZ00003", 10, 155.00);

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