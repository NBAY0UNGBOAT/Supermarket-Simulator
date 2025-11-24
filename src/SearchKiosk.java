import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SearchKiosk extends JPanel {
    private Runnable onClose;
    private Component parentComponent = null;  // Reference to parent for repainting

    // Waypoint fields - now supports multiple waypoints
    private java.util.List<int[]> waypoints = new java.util.ArrayList<>();
    private boolean hasWaypoint = false;
    
    // Key press tracking to prevent duplicate X key presses
    private int lastKeyCode = -1;
    private long lastKeyTime = 0;

    // Product type categories
    private static final String[] PRODUCT_TYPES = {
        "Milk", "Frozen Food", "Cheese", "Chicken", "Beef", "Seafood",
        "Bread", "Cereal", "Noodles", "Snacks", "Canned Goods", "Condiments",
        "Soft Drinks", "Juice", "Alcohol", "Cleaning Agents", "Home Essentials",
        "Hair Care", "Body Care", "Dental Care", "Clothes", "Stationery", "Pet Food",
        "Fruit", "Vegetable", "Eggs"
    };

    private static final Map<String, String[]> PRODUCT_EXAMPLES = new HashMap<>();
    private static final Map<String, String> PRODUCT_LOCATIONS = new HashMap<>();
    private static final Map<String, String> PRODUCT_COORDINATES = new HashMap<>();

    static {
        // Initialize product examples and locations
        // ============ FLOOR 0: GROUND FLOOR ============
        
        // CHILLED COUNTER (Row 1)
        PRODUCT_EXAMPLES.put("Chicken", new String[]{"🐔 Chicken Thigh Fillet (CHK00001) - ₱280.00", "🐔 Chicken Breast Fillet (CHK00002) - ₱320.00", "🐔 Ground Chicken (CHK00003) - ₱250.00"});
        PRODUCT_LOCATIONS.put("Chicken", "Chilled Counter");
        PRODUCT_COORDINATES.put("Chicken", "Floor 0, Row 1, Col 1-6");

        PRODUCT_EXAMPLES.put("Beef", new String[]{"🐄 Beef Rib (BEF00001) - ₱450.00", "🐄 Beef Shank (BEF00002) - ₱380.00", "🐄 Ground Beef (BEF00003) - ₱420.00"});
        PRODUCT_LOCATIONS.put("Beef", "Chilled Counter");
        PRODUCT_COORDINATES.put("Beef", "Floor 0, Row 1, Col 8-13");

        PRODUCT_EXAMPLES.put("Seafood", new String[]{"🐟 Tilapia (SEA00001) - ₱320.00", "🐟 Sugpo (SEA00002) - ₱580.00", "🐟 Squid (SEA00003) - ₱420.00"});
        PRODUCT_LOCATIONS.put("Seafood", "Chilled Counter");
        PRODUCT_COORDINATES.put("Seafood", "Floor 0, Row 1, Col 15-20");
        
        // SHELVES - Ground Floor
        // Shelf Aisle 1
        PRODUCT_EXAMPLES.put("Alcohol", new String[]{"🍺 Beer (ALC00001) - ₱50.00", "🍷 Wine (ALC00002) - ₱450.00", "🥃 Whiskey (ALC00003) - ₱850.00", "🍸 Vodka (ALC00004) - ₱750.00", "🍻 Brandy (ALC00005) - ₱650.00", "🍾 Champagne (ALC00006) - ₱1200.00", "🥂 Rum (ALC00007) - ₱580.00", "🍹 Gin (ALC00008) - ₱520.00"});
        PRODUCT_LOCATIONS.put("Alcohol", "Shelf Aisle 1");
        PRODUCT_COORDINATES.put("Alcohol", "Floor 0, Row 4-7, Col 2-3");

        PRODUCT_EXAMPLES.put("Condiments", new String[]{"🧂 Salt (CON00001) - ₱25.00", "🌶️ Pepper (CON00002) - ₱35.00", "🍶 Soy Sauce (CON00003) - ₱65.00", "🍯 Vinegar (CON00004) - ₱48.00", "🧈 Butter (CON00005) - ₱185.00", "🛢️ Cooking Oil (CON00006) - ₱95.00", "🍯 Honey (CON00007) - ₱125.00", "🌽 Corn Syrup (CON00008) - ₱75.00"});
        PRODUCT_LOCATIONS.put("Condiments", "Shelf Aisle 1");
        PRODUCT_COORDINATES.put("Condiments", "Floor 0, Row 10-13, Col 2-3");
        
        // Shelf Aisle 2
        PRODUCT_EXAMPLES.put("Soft Drinks", new String[]{"🥤 Sparkling Water (SFT00001) - ₱55.00", "🥤 Coke (SFT00002) - ₱45.00", "🥤 Sprite (SFT00003) - ₱45.00", "🥤 Mountain Dew (SFT00004) - ₱50.00", "🥤 Royal (SFT00005) - ₱40.00", "🥤 7-Up (SFT00006) - ₱45.00", "🥤 Gatorade (SFT00007) - ₱65.00", "🥤 Lemonade (SFT00008) - ₱35.00"});
        PRODUCT_LOCATIONS.put("Soft Drinks", "Shelf Aisle 2");
        PRODUCT_COORDINATES.put("Soft Drinks", "Floor 0, Row 4-7, Col 6-7");

        PRODUCT_EXAMPLES.put("Juice", new String[]{"🧃 Orange Juice (JUC00001) - ₱65.00", "🧃 Pineapple Juice (JUC00002) - ₱55.00", "🧃 Mango Juice (JUC00003) - ₱60.00", "🧃 Apple Juice (JUC00004) - ₱58.00", "🧃 Grape Juice (JUC00005) - ₱62.00", "🧃 Tomato Juice (JUC00006) - ₱48.00", "🧃 Carrot Juice (JUC00007) - ₱52.00", "🧃 Mixed Fruit Juice (JUC00008) - ₱75.00"});
        PRODUCT_LOCATIONS.put("Juice", "Shelf Aisle 2");
        PRODUCT_COORDINATES.put("Juice", "Floor 0, Row 10-13, Col 6-7");
        
        // Shelf Aisle 3
        PRODUCT_EXAMPLES.put("Cereal", new String[]{"🥣 Oatmeal (CER00001) - ₱95.00", "🥣 Corn Flakes (CER00002) - ₱125.00", "🥣 Honey Bunches (CER00003) - ₱115.00", "🥣 Wheat Bran (CER00004) - ₱85.00", "🥣 Rice Krispies (CER00005) - ₱105.00", "🥣 Frosted Flakes (CER00006) - ₱110.00", "🥣 Granola (CER00007) - ₱145.00", "🥣 Bran Flakes (CER00008) - ₱95.00"});
        PRODUCT_LOCATIONS.put("Cereal", "Shelf Aisle 3");
        PRODUCT_COORDINATES.put("Cereal", "Floor 0, Row 4-7, Col 14-15");

        PRODUCT_EXAMPLES.put("Noodles", new String[]{"🍜 Instant Noodles (NDL00001) - ₱8.50", "🍜 Ramen (NDL00002) - ₱25.00", "🍜 Lomi (NDL00003) - ₱12.00", "🍜 Pancit Canton (NDL00004) - ₱15.00", "🍝 Spaghetti (NDL00005) - ₱35.00", "🍝 Pasta (NDL00006) - ₱42.00", "🍜 Udon (NDL00007) - ₱48.00", "🍜 Glass Noodles (NDL00008) - ₱38.00"});
        PRODUCT_LOCATIONS.put("Noodles", "Shelf Aisle 3");
        PRODUCT_COORDINATES.put("Noodles", "Floor 0, Row 10-13, Col 14-15");
        
        // Shelf Aisle 4
        PRODUCT_EXAMPLES.put("Canned Goods", new String[]{"🥫 Canned Tuna (CAN00001) - ₱42.00", "🥫 Canned Sardines (CAN00002) - ₱28.00", "🥫 Canned Beans (CAN00003) - ₱35.00", "🥫 Canned Corn (CAN00004) - ₱32.00", "🥫 Canned Peas (CAN00005) - ₱30.00", "🥫 Canned Mushroom (CAN00006) - ₱45.00", "🥫 Canned Coconut Milk (CAN00007) - ₱55.00", "🥫 Canned Tomato (CAN00008) - ₱38.00"});
        PRODUCT_LOCATIONS.put("Canned Goods", "Shelf Aisle 4");
        PRODUCT_COORDINATES.put("Canned Goods", "Floor 0, Row 4-7, Col 18-19");

        PRODUCT_EXAMPLES.put("Snacks", new String[]{"🍪 Candies (SNK00001) - ₱45.00", "🍪 Cookies (SNK00002) - ₱65.00", "🍪 Crackers (SNK00003) - ₱55.00", "🍪 Chips (SNK00004) - ₱48.00", "🥜 Peanuts (SNK00005) - ₱75.00", "🍫 Chocolate (SNK00006) - ₱85.00", "🍪 Wafers (SNK00007) - ₱62.00", "🥨 Pretzels (SNK00008) - ₱58.00"});
        PRODUCT_LOCATIONS.put("Snacks", "Shelf Aisle 4");
        PRODUCT_COORDINATES.put("Snacks", "Floor 0, Row 10-13, Col 18-19");
        
        // TABLES - Ground Floor (All show Fruits)
        PRODUCT_EXAMPLES.put("Fruit", new String[]{"🍎 Apples (FRU00001) - ₱65.00", "🍌 Bananas (FRU00002) - ₱45.00", "🍊 Oranges (FRU00003) - ₱55.00", "🍇 Grapes (FRU00004) - ₱125.00"});
        PRODUCT_LOCATIONS.put("Fruit", "Table");
        PRODUCT_COORDINATES.put("Fruit", "Floor 0, Row 4-7 & 10-13, Col 10-11");

        // ============ FLOOR 1: UPPER FLOOR ============
        
        // FRIDGE (Row 1)
        PRODUCT_EXAMPLES.put("Milk", new String[]{"🥛 Fresh Milk (MLK00001) - ₱68.00", "🥛 Soy Milk (MLK00002) - ₱65.00", "🥛 Almond Milk (MLK00003) - ₱95.00"});
        PRODUCT_LOCATIONS.put("Milk", "Fridge Unit 1");
        PRODUCT_COORDINATES.put("Milk", "Floor 1, Row 1, Col 3-6");

        PRODUCT_EXAMPLES.put("Frozen Food", new String[]{"❄️ Hotdog (FRZ00001) - ₱85.00", "❄️ Chicken Nuggets (FRZ00002) - ₱125.00", "❄️ Tocino (FRZ00003) - ₱155.00"});
        PRODUCT_LOCATIONS.put("Frozen Food", "Fridge Unit 2");
        PRODUCT_COORDINATES.put("Frozen Food", "Floor 1, Row 1, Col 9-12");

        PRODUCT_EXAMPLES.put("Cheese", new String[]{"🧀 Sliced Cheese (CHS00001) - ₱145.00", "🧀 Keso de Bola (CHS00002) - ₱175.00", "🧀 Mozzarella (CHS00003) - ₱185.00"});
        PRODUCT_LOCATIONS.put("Cheese", "Fridge Unit 3");
        PRODUCT_COORDINATES.put("Cheese", "Floor 1, Row 1, Col 15-18");
        
        // SHELVES - Upper Floor
        // Shelf Aisle 1
        PRODUCT_EXAMPLES.put("Pet Food", new String[]{"😺 Cat Food (PET00001) - ₱125.00", "🐕 Dog Food (PET00002) - ₱145.00", "🐠 Fish Food (PET00003) - ₱65.00", "🦜 Bird Food (PET00004) - ₱85.00", "😺 Cat Treats (PET00005) - ₱95.00", "🐕 Dog Treats (PET00006) - ₱105.00", "🐹 Hamster Food (PET00007) - ₱55.00", "🦎 Reptile Food (PET00008) - ₱125.00"});
        PRODUCT_LOCATIONS.put("Pet Food", "Shelf Aisle 1");
        PRODUCT_COORDINATES.put("Pet Food", "Floor 1, Row 4-7, Col 2-3");

        PRODUCT_EXAMPLES.put("Stationery", new String[]{"📝 Paper (STN00001) - ₱65.00", "✏️ Pencil (STN00002) - ₱25.00", "🖊️ Ballpoint Pen (STN00003) - ₱8.00", "🖍️ Marker (STN00004) - ₱15.00", "📌 Pushpins (STN00005) - ₱12.00", "📎 Paper Clip (STN00006) - ₱10.00", "✂️ Scissors (STN00007) - ₱45.00", "📏 Ruler (STN00008) - ₱20.00"});
        PRODUCT_LOCATIONS.put("Stationery", "Shelf Aisle 1");
        PRODUCT_COORDINATES.put("Stationery", "Floor 1, Row 10-13, Col 2-3");
        
        // Shelf Aisle 2
        PRODUCT_EXAMPLES.put("Clothes", new String[]{"👚 Shirts (CLO00001) - ₱299.00", "👖 Jeans (CLO00002) - ₱599.00", "👗 Dresses (CLO00003) - ₱449.00", "👔 Polo (CLO00004) - ₱349.00", "👕 T-Shirts (CLO00005) - ₱199.00", "🧥 Jackets (CLO00006) - ₱799.00", "👗 Skirts (CLO00007) - ₱399.00", "👚 Blouse (CLO00008) - ₱349.00"});
        PRODUCT_LOCATIONS.put("Clothes", "Shelf Aisle 2");
        PRODUCT_COORDINATES.put("Clothes", "Floor 1, Row 4-7, Col 6-7");

        PRODUCT_EXAMPLES.put("Dental Care", new String[]{"🦷 Toothpaste (DEN00001) - ₱68.00", "🪥 Toothbrush (DEN00002) - ₱45.00", "🧵 Dental Floss (DEN00003) - ₱55.00", "🌿 Mouthwash (DEN00004) - ₱85.00", "😁 Whitening Strip (DEN00005) - ₱125.00", "🦷 Sensitive Toothpaste (DEN00006) - ₱95.00", "🪥 Electric Toothbrush (DEN00007) - ₱599.00", "🌿 Natural Mouthwash (DEN00008) - ₱95.00"});
        PRODUCT_LOCATIONS.put("Dental Care", "Shelf Aisle 2");
        PRODUCT_COORDINATES.put("Dental Care", "Floor 1, Row 10-13, Col 6-7");
        
        // Shelf Aisle 3
        PRODUCT_EXAMPLES.put("Cleaning Agents", new String[]{"🫧 Detergent (CLE00001) - ₱85.00", "🧼 Bleach (CLE00002) - ₱75.00", "🧽 Sponge (CLE00003) - ₱25.00", "🧹 Brush (CLE00004) - ₱35.00", "🪣 Bucket (CLE00005) - ₱95.00", "🧻 Tissue Paper (CLE00006) - ₱45.00", "🧴 Liquid Soap (CLE00007) - ₱65.00", "🧹 Broom (CLE00008) - ₱125.00"});
        PRODUCT_LOCATIONS.put("Cleaning Agents", "Shelf Aisle 3");
        PRODUCT_COORDINATES.put("Cleaning Agents", "Floor 1, Row 4-7, Col 14-15");

        PRODUCT_EXAMPLES.put("Hair Care", new String[]{"🧴 Shampoo (HAR00001) - ₱120.00", "🧴 Conditioner (HAR00002) - ₱130.00", "🧴 Hair Oil (HAR00003) - ₱95.00", "💇 Hair Cream (HAR00004) - ₱110.00", "🧴 Gel (HAR00005) - ₱85.00", "🧴 Hair Spray (HAR00006) - ₱75.00", "🧴 Hair Mask (HAR00007) - ₱140.00", "🧴 Hair Serum (HAR00008) - ₱165.00"});
        PRODUCT_LOCATIONS.put("Hair Care", "Shelf Aisle 3");
        PRODUCT_COORDINATES.put("Hair Care", "Floor 1, Row 10-13, Col 14-15");
        
        // Shelf Aisle 4
        PRODUCT_EXAMPLES.put("Home Essentials", new String[]{"🏠 Broom (HOM00001) - ₱125.00", "🏠 Dustpan (HOM00002) - ₱45.00", "🏠 Mop (HOM00003) - ₱185.00", "🏠 Bucket (HOM00004) - ₱95.00", "🏠 Cloth (HOM00005) - ₱15.00", "🏠 Towel (HOM00006) - ₱125.00", "🏠 Mat (HOM00007) - ₱95.00", "🏠 Curtain (HOM00008) - ₱299.00"});
        PRODUCT_LOCATIONS.put("Home Essentials", "Shelf Aisle 4");
        PRODUCT_COORDINATES.put("Home Essentials", "Floor 1, Row 4-7, Col 18-19");

        PRODUCT_EXAMPLES.put("Body Care", new String[]{"🧼 Soap (BOD00001) - ₱45.00", "🧴 Body Wash (BOD00002) - ₱95.00", "🧴 Lotion (BOD00003) - ₱105.00", "🧴 Deodorant (BOD00004) - ₱75.00", "🧴 Body Oil (BOD00005) - ₱125.00", "🧴 Shaving Cream (BOD00006) - ₱85.00", "🧴 Face Wash (BOD00007) - ₱65.00", "🧴 Face Moisturizer (BOD00008) - ₱145.00"});
        PRODUCT_LOCATIONS.put("Body Care", "Shelf Aisle 4");
        PRODUCT_COORDINATES.put("Body Care", "Floor 1, Row 10-13, Col 18-19");
        
        // TABLES - Upper Floor
        PRODUCT_EXAMPLES.put("Vegetable", new String[]{"🥬 Cabbage (VEG00001) - ₱28.00", "🥕 Carrot (VEG00002) - ₱35.00", "🥒 Cucumber (VEG00003) - ₱25.00", "🧅 Onion (VEG00004) - ₱20.00"});
        PRODUCT_LOCATIONS.put("Vegetable", "Table Aisle 1");
        PRODUCT_COORDINATES.put("Vegetable", "Floor 1, Row 4-7 & 10-13, Col 10-11");

        PRODUCT_EXAMPLES.put("Bread", new String[]{"🥖 Baguette (BRD00001) - ₱35.00", "🍞 Sandwich Bread (BRD00002) - ₱45.00", "🥐 Croissant (BRD00003) - ₱55.00", "🧈 Toast (BRD00004) - ₱25.00"});
        PRODUCT_LOCATIONS.put("Bread", "Table Dining Areas");
        PRODUCT_COORDINATES.put("Bread", "Floor 1, Row 20, Col 3-7 & 14-18");

        PRODUCT_EXAMPLES.put("Eggs", new String[]{"🥚 Brown Eggs (EGG00001) - ₱180.00", "🥚 White Eggs (EGG00002) - ₱175.00", "🥚 Duck Eggs (EGG00003) - ₱220.00", "🥚 Quail Eggs (EGG00004) - ₱90.00"});
        PRODUCT_LOCATIONS.put("Eggs", "Table Dining Area 2");
        PRODUCT_COORDINATES.put("Eggs", "Floor 1, Row 20, Col 9-12");
    }

    private int selectedTypeIndex = 0;
    private int selectedItemIndex = 0;
    private String selectedType = null;
    private String searchResult = null;
    private int stage = 0;  // 0 = select type, 1 = select item, 2 = show result

    public SearchKiosk(Runnable onClose) {
        this.onClose = onClose;
        setOpaque(false);
        setFocusable(false);  // Don't take focus - let parent TileGrid handle key events
    }

    public void setParentComponent(Component parent) {
        this.parentComponent = parent;
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Draw semi-transparent background
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, panelWidth, panelHeight);

        int menuWidth = 450;
        int menuHeight = 400;
        int x = (panelWidth - menuWidth) / 2;
        int y = (panelHeight - menuHeight) / 2;

        // Draw menu background
        g.setColor(new Color(40, 40, 40));
        g.fillRect(x, y, menuWidth, menuHeight);
        g.setColor(new Color(200, 200, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, menuWidth, menuHeight);

        // Draw title
        g.setColor(new Color(255, 200, 0));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String title = "🔍 SEARCH KIOSK";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (menuWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 30);

        if (stage == 0) {
            drawSelectTypeStage(g, x, y, menuWidth, menuHeight, fm);
        } else if (stage == 1) {
            drawSelectItemStage(g, x, y, menuWidth, menuHeight, fm);
        } else if (stage == 2) {
            drawResultStage(g, x, y, menuWidth, menuHeight, fm);
        }

        g.dispose();
    }

    private void drawSelectTypeStage(Graphics2D g, int x, int y, int menuWidth, int menuHeight, FontMetrics fm) {
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 12));
        g.drawString("Select Product Type (UP/DOWN • ENTER)", x + 20, y + 50);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 13));

        int visibleItems = 10;
        int startIdx = Math.max(0, selectedTypeIndex - 4);
        if (startIdx + visibleItems > PRODUCT_TYPES.length) {
            startIdx = Math.max(0, PRODUCT_TYPES.length - visibleItems);
        }

        for (int i = 0; i < visibleItems && startIdx + i < PRODUCT_TYPES.length; i++) {
            int idx = startIdx + i;
            int itemY = y + 75 + (i * 25);

            if (idx == selectedTypeIndex) {
                g.setColor(new Color(100, 150, 255));
                g.fillRect(x + 10, itemY - 18, menuWidth - 20, 22);
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.WHITE);
            }

            g.drawString("> " + PRODUCT_TYPES[idx], x + 20, itemY);
        }
    }

    private void drawSelectItemStage(Graphics2D g, int x, int y, int menuWidth, int menuHeight, FontMetrics fm) {
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 12));
        g.drawString("Select Item (UP/DOWN • ENTER to Search)", x + 20, y + 50);

        g.setColor(new Color(255, 200, 100));
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("Category: " + selectedType, x + 20, y + 75);

        String[] items = PRODUCT_EXAMPLES.get(selectedType);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        for (int i = 0; i < items.length; i++) {
            int itemY = y + 110 + (i * 25);

            if (i == selectedItemIndex) {
                g.setColor(new Color(100, 150, 255));
                g.fillRect(x + 10, itemY - 18, menuWidth - 20, 22);
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.WHITE);
            }

            g.drawString("> " + items[i], x + 20, itemY);
        }
    }

    private void drawResultStage(Graphics2D g, int x, int y, int menuWidth, int menuHeight, FontMetrics fm) {
        g.setColor(new Color(100, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String found = "✓ ITEM FOUND";
        int resultX = x + (menuWidth - fm.stringWidth(found)) / 2;
        g.drawString(found, resultX, y + 80);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString("Item: " + searchResult, x + 20, y + 130);

        g.setColor(new Color(200, 200, 100));
        g.setFont(new Font("Arial", Font.BOLD, 11));
        
        // Display exact tile locations from waypoints in 3x3 matrix
        if (!waypoints.isEmpty()) {
            int startY = y + 155;
            int colWidth = (menuWidth - 40) / 3;  // 3 columns
            int rowHeight = 18;
            int count = 0;
            
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (count >= waypoints.size()) break;
                    
                    int[] waypoint = waypoints.get(count);
                    int floor = waypoint[0];
                    int tileRow = waypoint[1];
                    int tileCol = waypoint[2];
                    String floorName = (floor == 0) ? "1F" : "2F";
                    String locStr = floorName + " " + tileRow + "," + tileCol;
                    
                    int drawX = x + 20 + (col * colWidth);
                    int drawY = startY + (row * rowHeight);
                    g.drawString(locStr, drawX, drawY);
                    count++;
                }
                if (count >= waypoints.size()) break;
            }
        }

        // Display waypoint status - moved down to avoid overlap
        if (hasWaypoint) {
            g.setColor(new Color(255, 100, 100));
            g.setFont(new Font("Arial", Font.BOLD, 11));
            g.drawString("✓ WAYPOINT SET", x + 20, y + 310);
        }

        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 11));
        int instructY = hasWaypoint ? y + 330 : y + 310;
        g.drawString("ENTER: search another  |  X: toggle waypoint  |  ESC: close", x + 20, instructY);
    }

    public void handleKeyPress(int keyCode) {
        // Debounce: ignore duplicate key presses within 200ms
        long currentTime = System.currentTimeMillis();
        if (keyCode == lastKeyCode && (currentTime - lastKeyTime) < 200) {
            return;
        }
        lastKeyCode = keyCode;
        lastKeyTime = currentTime;
        
        if (stage == 0) {
            handleTypeSelection(keyCode);
        } else if (stage == 1) {
            handleItemSelection(keyCode);
        } else if (stage == 2) {
            handleResultNavigation(keyCode);
        }
    }

    private void handleTypeSelection(int keyCode) {
        if (keyCode == KeyEvent.VK_UP) {
            selectedTypeIndex = (selectedTypeIndex - 1 + PRODUCT_TYPES.length) % PRODUCT_TYPES.length;
            repaint();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selectedTypeIndex = (selectedTypeIndex + 1) % PRODUCT_TYPES.length;
            repaint();
        } else if (keyCode == KeyEvent.VK_ENTER) {
            selectedType = PRODUCT_TYPES[selectedTypeIndex];
            selectedItemIndex = 0;
            stage = 1;
            repaint();
        }
    }

    private void handleItemSelection(int keyCode) {
        String[] items = PRODUCT_EXAMPLES.get(selectedType);
        if (keyCode == KeyEvent.VK_UP) {
            selectedItemIndex = (selectedItemIndex - 1 + items.length) % items.length;
            repaint();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selectedItemIndex = (selectedItemIndex + 1) % items.length;
            repaint();
        } else if (keyCode == KeyEvent.VK_ENTER) {
            searchResult = items[selectedItemIndex];
            stage = 2;
            // Automatically load waypoints when item is found
            java.util.List<int[]> allTiles = findAllTilesWithProduct(selectedType);
            if (allTiles != null && !allTiles.isEmpty()) {
                waypoints = allTiles;
                System.out.println("Waypoints found: " + waypoints.size() + " tiles for " + selectedType);
                for (int[] tile : waypoints) {
                    System.out.println("  - Waypoint at: floor=" + tile[0] + ", row=" + tile[1] + ", col=" + tile[2]);
                }
            }
            repaint();
        }
    }

    private void handleResultNavigation(int keyCode) {
        if (keyCode == KeyEvent.VK_ENTER) {
            selectedItemIndex = 0;
            stage = 1;
            waypoints.clear();
            repaint();
        } else if (keyCode == KeyEvent.VK_X) {
            System.out.println("X key pressed - toggling waypoint highlighting");
            // Toggle waypoint highlighting on/off
            hasWaypoint = !hasWaypoint;
            System.out.println("Waypoint highlighting: " + (hasWaypoint ? "ON" : "OFF"));
            repaint();
            // Repaint parent (TileGrid) to show/hide waypoint highlighting on tiles
            if (parentComponent != null) {
                parentComponent.repaint();
            }
        }
    }

    private int[] findTileWithProduct(String productType) {
        // Map product types to tile types and their locations
        // First, determine which tile type contains this product
        String tileType = getTileTypeForProduct(productType);
        if (tileType == null) {
            return null;
        }
        
        // Now search through all tiles of that type to find which one has this product
        // Based on TileGridInitializer layout
        switch(tileType) {
            case "ref":
                // Refrigerator at row 1, search cols 1-20
                return new int[]{1, 5};  // Return middle of refrigerator
            case "chilled":
                // Chilled counter at rows 4-7 and 10-13, cols 10-11
                return new int[]{5, 10};  // Return from first group
            case "table":
                // Tables at rows 4-7, 10-13, cols 10-11
                // Need to find which specific table has this product
                return findTableWithProduct(productType);
            case "shelf":
                // Shelves at various locations
                return findShelfWithProduct(productType);
        }
        return null;
    }

    private String getTileTypeForProduct(String productType) {
        // Determine which tile type contains this product based on PRODUCT_LOCATIONS
        String location = PRODUCT_LOCATIONS.get(productType);
        if (location == null) return null;
        
        // Map location strings to tile types
        if (location.contains("Chilled Counter")) return "chilled";
        if (location.contains("Fridge Unit")) return "ref";
        if (location.contains("Table")) return "table";
        if (location.contains("Shelf Aisle")) return "shelf";
        if (location.equals("Refrigerator")) return "ref";  // Legacy support
        if (location.equals("Table")) return "table";  // Legacy support
        if (location.equals("Shelf")) return "shelf";  // Legacy support
        return null;
    }

    private int[] findTableWithProduct(String productType) {
        // Table items: Bread, Fruit, Vegetable, Eggs
        // Tables at rows 4-7, 10-13, cols 10-11
        // Using TileInventory formula: (row * 7 + col) % items.length
        // TABLE_ITEMS has 4 categories: Fruit, Vegetable, Bread, Eggs
        
        int[] tableRows = {4, 5, 6, 7, 10, 11, 12, 13};
        int[] tableCols = {10, 11};
        
        for (int row : tableRows) {
            for (int col : tableCols) {
                int categoryIndex = (row * 7 + col) % 4;
                if (categoryIndex == 0 && productType.equals("Fruit")) return new int[]{row, col};
                if (categoryIndex == 1 && productType.equals("Vegetable")) return new int[]{row, col};
                if (categoryIndex == 2 && productType.equals("Bread")) return new int[]{row, col};
                if (categoryIndex == 3 && productType.equals("Eggs")) return new int[]{row, col};
            }
        }
        return null;
    }

    private java.util.List<int[]> findAllTilesWithProduct(String productType) {
        // Return ALL tiles that contain this product with floor info
        // Format: [floor, row, col]
        java.util.List<int[]> results = new java.util.ArrayList<>();
        
        // FLOOR 0: GROUND FLOOR
        if ("Chicken".equals(productType)) {
            // Chilled Row 1, Cols 1-6
            for (int col = 1; col <= 6; col++) results.add(new int[]{0, 1, col});
        } else if ("Beef".equals(productType)) {
            // Chilled Row 1, Cols 8-13
            for (int col = 8; col <= 13; col++) results.add(new int[]{0, 1, col});
        } else if ("Seafood".equals(productType)) {
            // Chilled Row 1, Cols 15-20
            for (int col = 15; col <= 20; col++) results.add(new int[]{0, 1, col});
        } else if ("Alcohol".equals(productType)) {
            // Shelf Aisle 1 Left: Rows 4-7, Cols 2-3
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{0, row, 2});
                results.add(new int[]{0, row, 3});
            }
        } else if ("Condiments".equals(productType)) {
            // Shelf Aisle 1 Back: Rows 10-13, Cols 2-3
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{0, row, 2});
                results.add(new int[]{0, row, 3});
            }
        } else if ("Soft Drinks".equals(productType)) {
            // Shelf Aisle 2 Left: Rows 4-7, Cols 6-7
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{0, row, 6});
                results.add(new int[]{0, row, 7});
            }
        } else if ("Juice".equals(productType)) {
            // Shelf Aisle 2 Back: Rows 10-13, Cols 6-7
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{0, row, 6});
                results.add(new int[]{0, row, 7});
            }
        } else if ("Cereal".equals(productType)) {
            // Shelf Aisle 3 Left: Rows 4-7, Cols 14-15
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{0, row, 14});
                results.add(new int[]{0, row, 15});
            }
        } else if ("Noodles".equals(productType)) {
            // Shelf Aisle 3 Back: Rows 10-13, Cols 14-15
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{0, row, 14});
                results.add(new int[]{0, row, 15});
            }
        } else if ("Canned Goods".equals(productType)) {
            // Shelf Aisle 4 Left: Rows 4-7, Cols 18-19
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{0, row, 18});
                results.add(new int[]{0, row, 19});
            }
        } else if ("Snacks".equals(productType)) {
            // Shelf Aisle 4 Back: Rows 10-13, Cols 18-19
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{0, row, 18});
                results.add(new int[]{0, row, 19});
            }
        } else if ("Fruit".equals(productType)) {
            // Tables all rows: Rows 4-7 & 10-13, Cols 10-11
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{0, row, 10});
                results.add(new int[]{0, row, 11});
            }
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{0, row, 10});
                results.add(new int[]{0, row, 11});
            }
        }
        
        // FLOOR 1: UPPER FLOOR
        if ("Milk".equals(productType)) {
            // Fridge Unit 1: Row 1, Cols 3-6
            for (int col = 3; col <= 6; col++) results.add(new int[]{1, 1, col});
        } else if ("Frozen Food".equals(productType)) {
            // Fridge Unit 2: Row 1, Cols 9-12
            for (int col = 9; col <= 12; col++) results.add(new int[]{1, 1, col});
        } else if ("Cheese".equals(productType)) {
            // Fridge Unit 3: Row 1, Cols 15-18
            for (int col = 15; col <= 18; col++) results.add(new int[]{1, 1, col});
        } else if ("Pet Food".equals(productType)) {
            // Shelf Aisle 1 Left: Rows 4-7, Cols 2-3
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{1, row, 2});
                results.add(new int[]{1, row, 3});
            }
        } else if ("Stationery".equals(productType)) {
            // Shelf Aisle 1 Back: Rows 10-13, Cols 2-3
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{1, row, 2});
                results.add(new int[]{1, row, 3});
            }
        } else if ("Clothes".equals(productType)) {
            // Shelf Aisle 2 Left: Rows 4-7, Cols 6-7
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{1, row, 6});
                results.add(new int[]{1, row, 7});
            }
        } else if ("Dental Care".equals(productType)) {
            // Shelf Aisle 2 Back: Rows 10-13, Cols 6-7
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{1, row, 6});
                results.add(new int[]{1, row, 7});
            }
        } else if ("Cleaning Agents".equals(productType)) {
            // Shelf Aisle 3 Left: Rows 4-7, Cols 14-15
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{1, row, 14});
                results.add(new int[]{1, row, 15});
            }
        } else if ("Hair Care".equals(productType)) {
            // Shelf Aisle 3 Back: Rows 10-13, Cols 14-15
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{1, row, 14});
                results.add(new int[]{1, row, 15});
            }
        } else if ("Home Essentials".equals(productType)) {
            // Shelf Aisle 4 Left: Rows 4-7, Cols 18-19
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{1, row, 18});
                results.add(new int[]{1, row, 19});
            }
        } else if ("Body Care".equals(productType)) {
            // Shelf Aisle 4 Back: Rows 10-13, Cols 18-19
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{1, row, 18});
                results.add(new int[]{1, row, 19});
            }
        } else if ("Vegetable".equals(productType)) {
            // Table Aisle 1: Rows 4-7 & 10-13, Cols 10-11
            for (int row = 4; row <= 7; row++) {
                results.add(new int[]{1, row, 10});
                results.add(new int[]{1, row, 11});
            }
            for (int row = 10; row <= 13; row++) {
                results.add(new int[]{1, row, 10});
                results.add(new int[]{1, row, 11});
            }
        } else if ("Bread".equals(productType)) {
            // Table Dining Areas 1 & 3: Row 20, Cols 3-7 & 14-18
            for (int col = 3; col <= 7; col++) results.add(new int[]{1, 20, col});
            for (int col = 14; col <= 18; col++) results.add(new int[]{1, 20, col});
        } else if ("Eggs".equals(productType)) {
            // Table Dining Area 2: Row 20, Cols 9-12
            for (int col = 9; col <= 12; col++) results.add(new int[]{1, 20, col});
        }
        
        return results;
    }

    private int[] findShelfWithProduct(String productType) {
        // Shelves have 16 categories at rows 4-7, 10-13, cols 2-3, 6-7, 14-15, 18-19
        int[] shelfRows = {4, 5, 6, 7, 10, 11, 12, 13};
        int[] shelfCols = {2, 3, 6, 7, 14, 15, 18, 19};
        
        // Map product types to their indices in SHELF_ITEMS
        int productIndex = getShelfProductIndex(productType);
        if (productIndex == -1) return null;
        
        for (int row : shelfRows) {
            for (int col : shelfCols) {
                int categoryIndex = (row * 7 + col) % 16;
                if (categoryIndex == productIndex) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    private int getShelfProductIndex(String productType) {
        // Shelf items in order: Cereal, Noodles, Snacks, Canned, Condiments, SoftDrinks, Juice, Alcohol,
        //                        Cleaning, HomeEssentials, HairCare, BodyCare, DentalCare, Clothes, Stationery, PetFood
        if (productType.equals("Cereal")) return 0;
        if (productType.equals("Noodles")) return 1;
        if (productType.equals("Snacks")) return 2;
        if (productType.equals("Canned Goods")) return 3;
        if (productType.equals("Condiments")) return 4;
        if (productType.equals("Soft Drinks")) return 5;
        if (productType.equals("Juice")) return 6;
        if (productType.equals("Alcohol")) return 7;
        if (productType.equals("Cleaning Agents")) return 8;
        if (productType.equals("Home Essentials")) return 9;
        if (productType.equals("Hair Care")) return 10;
        if (productType.equals("Body Care")) return 11;
        if (productType.equals("Dental Care")) return 12;
        if (productType.equals("Clothes")) return 13;
        if (productType.equals("Stationery")) return 14;
        if (productType.equals("Pet Food")) return 15;
        return -1;
    }

    private int[] parseCoordinates(String coords) {
        // Parse strings like "Row 1, Col 1" or "Row 4-7, Col 10-11"
        // Return the first coordinate [row, col]
        try {
            String[] parts = coords.split(",");
            String rowPart = parts[0].trim().substring(4).trim();  // Remove "Row "
            String colPart = parts[1].trim().substring(4).trim();  // Remove "Col "
            
            int row, col;
            if (rowPart.contains("-")) {
                row = Integer.parseInt(rowPart.split("-")[0]);
            } else {
                row = Integer.parseInt(rowPart);
            }
            
            if (colPart.contains("-")) {
                col = Integer.parseInt(colPart.split("-")[0]);
            } else {
                col = Integer.parseInt(colPart);
            }
            
            return new int[]{row, col};
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasActiveWaypoint() {
        return hasWaypoint;
    }

    public java.util.List<int[]> getWaypoints() {
        return waypoints;
    }

    public void clearWaypoint() {
        hasWaypoint = false;
        waypoints.clear();
    }

    private String getTileTypeName(String tileType) {
        if (tileType == null) return "Unknown";
        if (tileType.equals("ref")) return "Fridge";
        if (tileType.equals("chilled")) return "ChilledCounter";
        if (tileType.equals("table")) return "Table";
        if (tileType.equals("shelf")) return "Shelf";
        return "Unknown";
    }

    public void close() {
        if (onClose != null) {
            onClose.run();
        }
    }
}
