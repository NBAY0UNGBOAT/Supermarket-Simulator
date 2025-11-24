

class Item {
    String name;
    String category;
    int qty;
    double price;

    public Item(String name, String category, int qty) {
        this.name = name;
        this.category = category;
        this.qty = qty;
        this.price = 1.0;
    }

    public Item(String name, String category, int qty, double price) {
        this.name = name;
        this.category = category;
        this.qty = qty;
        this.price = price;
    }

    public String getName() { 
        return name; 
    }

    public String getCategory() { 
        return category; 
    }

    public int getQty() { 
        return qty; 
    }

    public double getPrice() {
        return price;
    }

    public void addQty(int qty) {
        if (qty < 0) {
            System.out.println("Quantity must be more than 0.");
        } else {
            this.qty += qty;
        }
    }

    public String toString() {
        return String.format("%s (%s) x%d", name, category, qty);
    }

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