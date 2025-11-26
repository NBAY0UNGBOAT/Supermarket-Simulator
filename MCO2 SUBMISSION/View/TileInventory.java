package View;

import Controller.*;
import Model.*;

/**
 * Manages inventory data for all supermarket tiles across multiple floors.
 * Provides product listings for refrigerators, shelves, tables, and other display types.
 */
public class TileInventory {
    
    // ===================== FLOOR 0: GROUND FLOOR =====================
    
    // CHILLED: Ground Floor
    private static final String[] CHILLED_CHICKEN = {
        "🐔 Chicken Thigh Fillet (CHK00001) - ₱280.00", 
        "🐔 Chicken Breast Fillet (CHK00002) - ₱320.00", 
        "🐔 Ground Chicken (CHK00003) - ₱250.00"
    };
    
    private static final String[] CHILLED_BEEF = {
        "🐄 Beef Rib (BEF00001) - ₱450.00", 
        "🐄 Beef Shank (BEF00002) - ₱380.00", 
        "🐄 Ground Beef (BEF00003) - ₱420.00"
    };
    
    private static final String[] CHILLED_SEAFOOD = {
        "🐟 Tilapia (SEA00001) - ₱320.00", 
        "🐟 Sugpo (SEA00002) - ₱580.00", 
        "🐟 Squid (SEA00003) - ₱420.00"
    };
    
    // SHELF: Ground Floor (2 tiers each)
    private static final String[] SHELF_ALCOHOL_TOP = {
        "🍺 Beer (ALC00001) - ₱50.00", "🍷 Wine (ALC00002) - ₱450.00", "🥃 Whiskey (ALC00003) - ₱850.00", "🍸 Vodka (ALC00004) - ₱750.00"
    };
    
    private static final String[] SHELF_ALCOHOL_BOTTOM = {
        "🍻 Brandy (ALC00005) - ₱650.00", "🍾 Champagne (ALC00006) - ₱1200.00", "🥂 Rum (ALC00007) - ₱580.00", "🍹 Gin (ALC00008) - ₱520.00"
    };
    
    private static final String[] SHELF_CONDIMENTS_TOP = {
        "🧂 Salt (CON00001) - ₱25.00", "🌶️ Pepper (CON00002) - ₱35.00", "🍶 Soy Sauce (CON00003) - ₱65.00", "🍯 Vinegar (CON00004) - ₱48.00"
    };
    
    private static final String[] SHELF_CONDIMENTS_BOTTOM = {
        "🧈 Butter (CON00005) - ₱185.00", "🛢️ Cooking Oil (CON00006) - ₱95.00", "🍯 Honey (CON00007) - ₱125.00", "🌽 Corn Syrup (CON00008) - ₱75.00"
    };
    
    private static final String[] SHELF_SOFTDRINK_TOP = {
        "🥤 Sparkling Water (SFT00001) - ₱55.00", "🥤 Coke (SFT00002) - ₱45.00", "🥤 Sprite (SFT00003) - ₱45.00", "🥤 Mountain Dew (SFT00004) - ₱50.00"
    };
    
    private static final String[] SHELF_SOFTDRINK_BOTTOM = {
        "🥤 Royal (SFT00005) - ₱40.00", "🥤 7-Up (SFT00006) - ₱45.00", "🥤 Gatorade (SFT00007) - ₱65.00", "🥤 Lemonade (SFT00008) - ₱35.00"
    };
    
    private static final String[] SHELF_JUICE_TOP = {
        "🧃 Orange Juice (JUC00001) - ₱65.00", "🧃 Pineapple Juice (JUC00002) - ₱55.00", "🧃 Mango Juice (JUC00003) - ₱60.00", "🧃 Apple Juice (JUC00004) - ₱58.00"
    };
    
    private static final String[] SHELF_JUICE_BOTTOM = {
        "🧃 Grape Juice (JUC00005) - ₱62.00", "🧃 Tomato Juice (JUC00006) - ₱48.00", "🧃 Carrot Juice (JUC00007) - ₱52.00", "🧃 Mixed Fruit Juice (JUC00008) - ₱75.00"
    };
    
    private static final String[] SHELF_CEREAL_TOP = {
        "🥣 Oatmeal (CER00001) - ₱95.00", "🥣 Corn Flakes (CER00002) - ₱125.00", "🥣 Honey Bunches (CER00003) - ₱115.00", "🥣 Wheat Bran (CER00004) - ₱85.00"
    };
    
    private static final String[] SHELF_CEREAL_BOTTOM = {
        "🥣 Rice Krispies (CER00005) - ₱105.00", "🥣 Frosted Flakes (CER00006) - ₱110.00", "🥣 Granola (CER00007) - ₱145.00", "🥣 Bran Flakes (CER00008) - ₱95.00"
    };
    
    private static final String[] SHELF_NOODLES_TOP = {
        "🍜 Instant Noodles (NDL00001) - ₱8.50", "🍜 Ramen (NDL00002) - ₱25.00", "🍜 Lomi (NDL00003) - ₱12.00", "🍜 Pancit Canton (NDL00004) - ₱15.00"
    };
    
    private static final String[] SHELF_NOODLES_BOTTOM = {
        "🍝 Spaghetti (NDL00005) - ₱35.00", "🍝 Pasta (NDL00006) - ₱42.00", "🍜 Udon (NDL00007) - ₱48.00", "🍜 Glass Noodles (NDL00008) - ₱38.00"
    };
    
    private static final String[] SHELF_CANNED_TOP = {
        "🥫 Canned Tuna (CAN00001) - ₱42.00", "🥫 Canned Sardines (CAN00002) - ₱28.00", "🥫 Canned Beans (CAN00003) - ₱35.00", "🥫 Canned Corn (CAN00004) - ₱32.00"
    };
    
    private static final String[] SHELF_CANNED_BOTTOM = {
        "🥫 Canned Peas (CAN00005) - ₱30.00", "🥫 Canned Mushroom (CAN00006) - ₱45.00", "🥫 Canned Coconut Milk (CAN00007) - ₱55.00", "🥫 Canned Tomato (CAN00008) - ₱38.00"
    };
    
    private static final String[] SHELF_SNACKS_TOP = {
        "🍪 Candies (SNK00001) - ₱45.00", "🍪 Cookies (SNK00002) - ₱65.00", "🍪 Crackers (SNK00003) - ₱55.00", "🍪 Chips (SNK00004) - ₱48.00"
    };
    
    private static final String[] SHELF_SNACKS_BOTTOM = {
        "🥜 Peanuts (SNK00005) - ₱75.00", "🍫 Chocolate (SNK00006) - ₱85.00", "🍪 Wafers (SNK00007) - ₱62.00", "🥨 Pretzels (SNK00008) - ₱58.00"
    };
    
    // TABLE: Ground Floor - Fruits only
    private static final String[] TABLE_FRUITS = {
        "🍎 Apples (FRU00001) - ₱65.00", 
        "🍌 Bananas (FRU00002) - ₱45.00", 
        "🍊 Oranges (FRU00003) - ₱55.00", 
        "🍇 Grapes (FRU00004) - ₱125.00"
    };
    
    // ===================== FLOOR 1: UPPER FLOOR =====================
    
    // FRIDGE: Upper Floor
    private static final String[] FRIDGE_MILK = {
        "🥛 Fresh Milk (MLK00001) - ₱68.00", 
        "🥛 Soy Milk (MLK00002) - ₱65.00", 
        "🥛 Almond Milk (MLK00003) - ₱95.00"
    };
    
    private static final String[] FRIDGE_FROZEN_FOOD = {
        "❄️ Hotdog (FRZ00001) - ₱85.00", 
        "❄️ Chicken Nuggets (FRZ00002) - ₱125.00", 
        "❄️ Tocino (FRZ00003) - ₱155.00"
    };
    
    private static final String[] FRIDGE_CHEESE = {
        "🧀 Sliced Cheese (CHS00001) - ₱145.00", 
        "🧀 Keso de Bola (CHS00002) - ₱175.00", 
        "🧀 Mozzarella (CHS00003) - ₱185.00"
    };
    
    // SHELF: Upper Floor
    private static final String[] SHELF_PETFOOD_TOP = {
        "😺 Cat Food (PET00001) - ₱125.00", "🐕 Dog Food (PET00002) - ₱145.00", "🐠 Fish Food (PET00003) - ₱65.00", "🦜 Bird Food (PET00004) - ₱85.00"
    };
    
    private static final String[] SHELF_PETFOOD_BOTTOM = {
        "😺 Cat Treats (PET00005) - ₱95.00", "🐕 Dog Treats (PET00006) - ₱105.00", "🐹 Hamster Food (PET00007) - ₱55.00", "🦎 Reptile Food (PET00008) - ₱125.00"
    };
    
    private static final String[] SHELF_STATIONERY_TOP = {
        "📝 Paper (STN00001) - ₱65.00", "✏️ Pencil (STN00002) - ₱25.00", "🖊️ Ballpoint Pen (STN00003) - ₱8.00", "🖍️ Marker (STN00004) - ₱15.00"
    };
    
    private static final String[] SHELF_STATIONERY_BOTTOM = {
        "📌 Pushpins (STN00005) - ₱12.00", "📎 Paper Clip (STN00006) - ₱10.00", "✂️ Scissors (STN00007) - ₱45.00", "📏 Ruler (STN00008) - ₱20.00"
    };
    
    private static final String[] SHELF_CLOTHES_TOP = {
        "👚 Shirts (CLO00001) - ₱299.00", "👖 Jeans (CLO00002) - ₱599.00", "👗 Dresses (CLO00003) - ₱449.00", "👔 Polo (CLO00004) - ₱349.00"
    };
    
    private static final String[] SHELF_CLOTHES_BOTTOM = {
        "👕 T-Shirts (CLO00005) - ₱199.00", "🧥 Jackets (CLO00006) - ₱799.00", "👗 Skirts (CLO00007) - ₱399.00", "👚 Blouse (CLO00008) - ₱349.00"
    };
    
    private static final String[] SHELF_DENTALCARE_TOP = {
        "🦷 Toothpaste (DEN00001) - ₱68.00", "🪥 Toothbrush (DEN00002) - ₱45.00", "🧵 Dental Floss (DEN00003) - ₱55.00", "🌿 Mouthwash (DEN00004) - ₱85.00"
    };
    
    private static final String[] SHELF_DENTALCARE_BOTTOM = {
        "😁 Whitening Strip (DEN00005) - ₱125.00", "🦷 Sensitive Toothpaste (DEN00006) - ₱95.00", "🪥 Electric Toothbrush (DEN00007) - ₱599.00", "🌿 Natural Mouthwash (DEN00008) - ₱95.00"
    };
    
    private static final String[] SHELF_CLEANING_TOP = {
        "🫧 Detergent (CLE00001) - ₱85.00", "🧼 Bleach (CLE00002) - ₱75.00", "🧽 Sponge (CLE00003) - ₱25.00", "🧹 Brush (CLE00004) - ₱35.00"
    };
    
    private static final String[] SHELF_CLEANING_BOTTOM = {
        "🪣 Bucket (CLE00005) - ₱95.00", "🧻 Tissue Paper (CLE00006) - ₱45.00", "🧴 Liquid Soap (CLE00007) - ₱65.00", "🧹 Broom (CLE00008) - ₱125.00"
    };
    
    private static final String[] SHELF_HAIRCARE_TOP = {
        "🧴 Shampoo (HAR00001) - ₱120.00", "🧴 Conditioner (HAR00002) - ₱130.00", "🧴 Hair Oil (HAR00003) - ₱95.00", "💇 Hair Cream (HAR00004) - ₱110.00"
    };
    
    private static final String[] SHELF_HAIRCARE_BOTTOM = {
        "🧴 Gel (HAR00005) - ₱85.00", "🧴 Hair Spray (HAR00006) - ₱75.00", "🧴 Hair Mask (HAR00007) - ₱140.00", "🧴 Hair Serum (HAR00008) - ₱165.00"
    };
    
    private static final String[] SHELF_HOMEESSENTIALS_TOP = {
        "🏠 Broom (HOM00001) - ₱125.00", "🏠 Dustpan (HOM00002) - ₱45.00", "🏠 Mop (HOM00003) - ₱185.00", "🏠 Bucket (HOM00004) - ₱95.00"
    };
    
    private static final String[] SHELF_HOMEESSENTIALS_BOTTOM = {
        "🏠 Cloth (HOM00005) - ₱15.00", "🏠 Towel (HOM00006) - ₱125.00", "🏠 Mat (HOM00007) - ₱95.00", "🏠 Curtain (HOM00008) - ₱299.00"
    };
    
    private static final String[] SHELF_BODYCARE_TOP = {
        "🧼 Soap (BOD00001) - ₱45.00", "🧴 Body Wash (BOD00002) - ₱95.00", "🧴 Lotion (BOD00003) - ₱105.00", "🧴 Deodorant (BOD00004) - ₱75.00"
    };
    
    private static final String[] SHELF_BODYCARE_BOTTOM = {
        "🧴 Body Oil (BOD00005) - ₱125.00", "🧴 Shaving Cream (BOD00006) - ₱85.00", "🧴 Face Wash (BOD00007) - ₱65.00", "🧴 Face Moisturizer (BOD00008) - ₱145.00"
    };
    
    // TABLE: Upper Floor
    private static final String[] TABLE_VEGETABLES = {
        "🥬 Cabbage (VEG00001) - ₱28.00", 
        "🥕 Carrot (VEG00002) - ₱35.00", 
        "🥒 Cucumber (VEG00003) - ₱25.00", 
        "🧅 Onion (VEG00004) - ₱20.00"
    };
    
    private static final String[] TABLE_BREAD = {
        "🥖 Baguette (BRD00001) - ₱35.00", 
        "🍞 Sandwich Bread (BRD00002) - ₱45.00", 
        "🥐 Croissant (BRD00003) - ₱55.00", 
        "🧈 Toast (BRD00004) - ₱25.00"
    };
    
    private static final String[] TABLE_EGGS = {
        "🥚 Brown Eggs (EGG00001) - ₱180.00", 
        "🥚 White Eggs (EGG00002) - ₱175.00", 
        "🥚 Duck Eggs (EGG00003) - ₱220.00", 
        "🥚 Quail Eggs (EGG00004) - ₱90.00"
    };

    // Helper method to flatten 2D array to 1D
    private static String[] flattenTiers(String[]... tiers) {
        int totalItems = 0;
        for (String[] tier : tiers) {
            totalItems += tier.length;
        }
        
        String[] result = new String[totalItems];
        int index = 0;
        for (String[] tier : tiers) {
            for (String item : tier) {
                result[index++] = item;
            }
        }
        return result;
    }

    /**
     * Gets the items for a tile type using default position and floor.
     *
     * @param tileType the type of tile (ref, chilled, shelf, table)
     * @return array of product strings for the tile
     */
    public static String[] getItemsForTile(String tileType) {
        return getItemsForTile(tileType, 0, 0, 0);
    }

    /**
     * Gets the items for a tile type at the given position on the ground floor.
     *
     * @param tileType the type of tile (ref, chilled, shelf, table)
     * @param row the row position of the tile
     * @param col the column position of the tile
     * @return array of product strings for the tile
     */
    public static String[] getItemsForTile(String tileType, int row, int col) {
        return getItemsForTile(tileType, row, col, 0);
    }

    /**
     * Gets the items for a tile type at the given position and floor.
     * Returns different products depending on floor and exact tile coordinates.
     *
     * @param tileType the type of tile (ref, chilled, shelf, table)
     * @param row the row position of the tile
     * @param col the column position of the tile
     * @param floor the floor number (0=ground floor, 1=upper floor, 2=secret)
     * @return array of product strings available at the tile
     */
    public static String[] getItemsForTile(String tileType, int row, int col, int floor) {
        // FLOOR 0: GROUND FLOOR
        if (floor == 0) {
            if ("chilled".equals(tileType)) {
                // Chilled Row 1: Cols 1-6 (Chicken), 8-13 (Beef), 15-20 (Seafood)
                if (col >= 1 && col <= 6) {
                    return CHILLED_CHICKEN;
                } else if (col >= 8 && col <= 13) {
                    return CHILLED_BEEF;
                } else if (col >= 15 && col <= 20) {
                    return CHILLED_SEAFOOD;
                }
                return CHILLED_CHICKEN; // Default
            } else if ("shelf".equals(tileType)) {
                // Ground Floor Shelves: 4 aisles at cols (2-3), (6-7), (14-15), (18-19)
                if ((col == 2 || col == 3) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 1 Left (rows 4-7)
                    return flattenTiers(SHELF_ALCOHOL_TOP, SHELF_ALCOHOL_BOTTOM);
                } else if ((col == 2 || col == 3) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 1 Back (rows 10-13)
                    return flattenTiers(SHELF_CONDIMENTS_TOP, SHELF_CONDIMENTS_BOTTOM);
                } else if ((col == 6 || col == 7) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 2 Left (rows 4-7)
                    return flattenTiers(SHELF_SOFTDRINK_TOP, SHELF_SOFTDRINK_BOTTOM);
                } else if ((col == 6 || col == 7) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 2 Back (rows 10-13)
                    return flattenTiers(SHELF_JUICE_TOP, SHELF_JUICE_BOTTOM);
                } else if ((col == 14 || col == 15) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 3 Left (rows 4-7)
                    return flattenTiers(SHELF_CEREAL_TOP, SHELF_CEREAL_BOTTOM);
                } else if ((col == 14 || col == 15) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 3 Back (rows 10-13)
                    return flattenTiers(SHELF_NOODLES_TOP, SHELF_NOODLES_BOTTOM);
                } else if ((col == 18 || col == 19) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 4 Left (rows 4-7)
                    return flattenTiers(SHELF_CANNED_TOP, SHELF_CANNED_BOTTOM);
                } else if ((col == 18 || col == 19) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 4 Back (rows 10-13)
                    return flattenTiers(SHELF_SNACKS_TOP, SHELF_SNACKS_BOTTOM);
                }
                return flattenTiers(SHELF_ALCOHOL_TOP, SHELF_ALCOHOL_BOTTOM); // Default
            } else if ("table".equals(tileType)) {
                // All tables on ground floor: Fruits
                return TABLE_FRUITS;
            }
        }
        
        // FLOOR 1: UPPER FLOOR
        if (floor == 1) {
            if ("ref".equals(tileType)) {
                // Fridge Units: Row 1, Cols (3-6), (9-12), (15-18)
                if (col >= 3 && col <= 6) {
                    return FRIDGE_MILK;
                } else if (col >= 9 && col <= 12) {
                    return FRIDGE_FROZEN_FOOD;
                } else if (col >= 15 && col <= 18) {
                    return FRIDGE_CHEESE;
                }
                return FRIDGE_MILK; // Default
            } else if ("shelf".equals(tileType)) {
                // Upper Floor Shelves: 4 aisles at cols (2-3), (6-7), (14-15), (18-19)
                if ((col == 2 || col == 3) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 1 Left (rows 4-7)
                    return flattenTiers(SHELF_PETFOOD_TOP, SHELF_PETFOOD_BOTTOM);
                } else if ((col == 2 || col == 3) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 1 Back (rows 10-13)
                    return flattenTiers(SHELF_STATIONERY_TOP, SHELF_STATIONERY_BOTTOM);
                } else if ((col == 6 || col == 7) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 2 Left (rows 4-7)
                    return flattenTiers(SHELF_CLOTHES_TOP, SHELF_CLOTHES_BOTTOM);
                } else if ((col == 6 || col == 7) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 2 Back (rows 10-13)
                    return flattenTiers(SHELF_DENTALCARE_TOP, SHELF_DENTALCARE_BOTTOM);
                } else if ((col == 14 || col == 15) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 3 Left (rows 4-7)
                    return flattenTiers(SHELF_CLEANING_TOP, SHELF_CLEANING_BOTTOM);
                } else if ((col == 14 || col == 15) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 3 Back (rows 10-13)
                    return flattenTiers(SHELF_HAIRCARE_TOP, SHELF_HAIRCARE_BOTTOM);
                } else if ((col == 18 || col == 19) && (row >= 4 && row <= 7)) {
                    // Shelf Aisle 4 Left (rows 4-7)
                    return flattenTiers(SHELF_HOMEESSENTIALS_TOP, SHELF_HOMEESSENTIALS_BOTTOM);
                } else if ((col == 18 || col == 19) && (row >= 10 && row <= 13)) {
                    // Shelf Aisle 4 Back (rows 10-13)
                    return flattenTiers(SHELF_BODYCARE_TOP, SHELF_BODYCARE_BOTTOM);
                }
                return flattenTiers(SHELF_PETFOOD_TOP, SHELF_PETFOOD_BOTTOM); // Default
            } else if ("table".equals(tileType)) {
                // Table Aisles and Dining Areas
                if ((col == 10 || col == 11) && ((row >= 4 && row <= 7) || (row >= 10 && row <= 13))) {
                    // Table Aisle 1: Vegetables
                    return TABLE_VEGETABLES;
                } else if (col >= 3 && col <= 7 && row == 20) {
                    // Table Dining Area 1: Bread
                    return TABLE_BREAD;
                } else if (col >= 9 && col <= 12 && row == 20) {
                    // Table Dining Area 2: Eggs
                    return TABLE_EGGS;
                } else if (col >= 14 && col <= 18 && row == 20) {
                    // Table Dining Area 3: Bread
                    return TABLE_BREAD;
                }
                return TABLE_VEGETABLES; // Default
            }
        }
        
        return new String[]{"Empty"};
    }

    /**
     * Checks if a tile type is interactable for purchasing items.
     *
     * @param tileType the type of tile
     * @return true if the tile allows item purchasing, false otherwise
     */
    public static boolean isInteractable(String tileType) {
        return "ref".equals(tileType) || "chilled".equals(tileType) || 
               "shelf".equals(tileType) || "table".equals(tileType);
    }
}
