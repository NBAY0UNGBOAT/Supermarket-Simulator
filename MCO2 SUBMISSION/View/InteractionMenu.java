package View;

import Controller.*;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class InteractionMenu extends JPanel {
    // Product list and selection tracking
    private String[] items;
    private java.util.List<String> returnableProducts;
    private String tileType;
    private int selectedIndex = 0;
    // Parent for repaint notifications
    private JComponent parentComponent;
    private Runnable onClose;
    // Track if in return mode
    private boolean inReturnMode = false;
    // Target tile position
    private int targetRow;
    private int targetCol;
    
    // Grid dimensions based on display type
    private int gridCols;
    private int gridRows;
    private static final int SQUARE_SIZE = 100;
    private static final int PADDING = 20;
    private static final int TITLE_HEIGHT = 40;

    /**
     * Constructs a new InteractionMenu for the given tile type and position.
     * Loads products available at the tile and sets up the display grid.
     *
     * @param tileType the type of tile (ref, chilled, shelf, table)
     * @param row the row position of the tile
     * @param col the column position of the tile
     * @param floor the current floor number
     * @param onClose callback runnable when menu is closed
     */
    public InteractionMenu(String tileType, int row, int col, int floor, Runnable onClose) {
        // Initialize interaction menu for tile
        this.tileType = tileType;
        this.onClose = onClose;
        this.targetRow = row;
        this.targetCol = col;
        // Get products for this tile type
        this.items = TileInventory.getItemsForTile(tileType, row, col, floor);
        setOpaque(false);
        setFocusable(true);
        
        // Set grid dimensions based on display type and product count
        setupGridDimensions();
    }
    
    /**
     * Sets the list of returnable products and switches to return mode.
     *
     * @param products list of product strings the shopper can return
     */
    public void setReturnProducts(java.util.List<String> products) {
        this.returnableProducts = products;
        if (products != null) {
            // Switch to return mode
            this.inReturnMode = true;
            if (!products.isEmpty()) {
                this.items = products.toArray(new String[0]);
            } else {
                this.items = new String[0];
            }
            this.selectedIndex = 0;
            setupGridDimensions();
        }
    }
    
    /**
     * Exits return mode and returns to the default product buying display.
     */
    public void exitReturnMode() {
        this.inReturnMode = false;
        this.returnableProducts = null;
        this.items = TileInventory.getItemsForTile(tileType, targetRow, targetCol, 0);
        this.selectedIndex = 0;
        setupGridDimensions();
    }
    
    /**
     * Sets the parent component for triggering repaints.
     *
     * @param parent the parent JComponent
     */
    public void setParentComponent(JComponent parent) {
        this.parentComponent = parent;
    }
    
    /**
     * Checks if the menu is currently in return mode.
     *
     * @return true if in return mode, false if in buying mode
     */
    public boolean isInReturnMode() {
        return inReturnMode;
    }
    
    private void setupGridDimensions() {
        // Determine grid layout based on display type
        if ("ref".equals(tileType)) {
            // Refrigerator: 3 tiers x 3 products = 9 total, shown as 3x3
            gridCols = 3;
            gridRows = 3;
        } else if ("chilled".equals(tileType)) {
            // Chilled counter: 3 products in single row
            gridCols = 3;
            gridRows = 1;
        } else if ("shelf".equals(tileType)) {
            // Shelf: 2 tiers x 4 products = 8 total, shown as 4x2
            gridCols = 4;
            gridRows = 2;
        } else if ("table".equals(tileType)) {
            // Table: 4 products in single row
            gridCols = 4;
            gridRows = 1;
        } else {
            // Default fallback
            gridCols = 3;
            gridRows = 1;
        }
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

        // Calculate menu dimensions
        int gridWidth = gridCols * SQUARE_SIZE + PADDING * 2;
        int gridHeight = gridRows * SQUARE_SIZE + PADDING * 2 + TITLE_HEIGHT;
        int x = (panelWidth - gridWidth) / 2;
        int y = (panelHeight - gridHeight) / 2;

        // Draw menu background
        g.setColor(new Color(40, 40, 40));
        g.fillRect(x, y, gridWidth, gridHeight);
        g.setColor(new Color(200, 200, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, gridWidth, gridHeight);

        // Draw title
        g.setColor(new Color(255, 200, 0));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String title = inReturnMode ? "Return Products" : formatTileType(tileType);
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (gridWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 25);

        // Draw product squares in grid
        int itemIndex = 0;
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                if (itemIndex >= items.length) break;
                
                int squareX = x + PADDING + col * SQUARE_SIZE;
                int squareY = y + TITLE_HEIGHT + PADDING + row * SQUARE_SIZE;
                
                // Highlight selected item
                if (itemIndex == selectedIndex) {
                    g.setColor(new Color(100, 150, 255));
                    g.fillRect(squareX, squareY, SQUARE_SIZE, SQUARE_SIZE);
                    g.setColor(new Color(255, 200, 0));
                    g.setStroke(new BasicStroke(3));
                } else {
                    g.setColor(new Color(70, 70, 70));
                    g.fillRect(squareX, squareY, SQUARE_SIZE, SQUARE_SIZE);
                    g.setColor(new Color(150, 150, 150));
                    g.setStroke(new BasicStroke(1));
                }
                
                g.drawRect(squareX, squareY, SQUARE_SIZE, SQUARE_SIZE);
                
                // Extract product name from full string
                String fullText = items[itemIndex];
                String productName = extractProductName(fullText);
                
                // Draw product name in square
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 10));
                
                // Word wrap for longer names
                String[] words = productName.split(" ");
                int lineY = squareY + SQUARE_SIZE / 2 - 10;
                
                for (String word : words) {
                    fm = g.getFontMetrics();
                    int wordX = squareX + (SQUARE_SIZE - fm.stringWidth(word)) / 2;
                    g.drawString(word, wordX, lineY);
                    lineY += 12;
                }
                
                itemIndex++;
            }
        }

        // Draw instructions
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 11));
        String instruction = "ARROW KEYS to navigate • ENTER to select • R to return • ESC to close";
        fm = g.getFontMetrics();
        int instructionX = x + (gridWidth - fm.stringWidth(instruction)) / 2;
        g.drawString(instruction, instructionX, y + gridHeight + 20);

        g.dispose();
    }
    
    private String extractProductName(String fullText) {
        // Extract just the product name from "emoji Name (ID) - Price"
        int startIdx = fullText.indexOf(" ") + 1; // Skip emoji
        int endIdx = fullText.indexOf("(");
        if (endIdx > startIdx) {
            return fullText.substring(startIdx, endIdx).trim();
        }
        return fullText;
    }

    private String formatTileType(String type) {
        return Character.toUpperCase(type.charAt(0)) + type.substring(1);
    }

    public void moveSelection(int direction) {
        // Convert direction to grid movement (UP=-cols, DOWN=+cols, LEFT=-1, RIGHT=+1)
        if (direction == -1) {  // UP
            selectedIndex = Math.max(0, selectedIndex - gridCols);
        } else if (direction == 1) {  // DOWN
            selectedIndex = Math.min(items.length - 1, selectedIndex + gridCols);
        } else if (direction == -2) {  // LEFT
            if (selectedIndex % gridCols > 0) {
                selectedIndex--;
            }
        } else if (direction == 2) {  // RIGHT
            if ((selectedIndex + 1) % gridCols != 0 && selectedIndex + 1 < items.length) {
                selectedIndex++;
            }
        }
        
        repaint();
    }
    
    public void moveUp() {
        moveSelection(-1);
    }
    
    public void moveDown() {
        moveSelection(1);
    }
    
    public void moveLeft() {
        moveSelection(-2);
    }
    
    public void moveRight() {
        moveSelection(2);
    }

    public void close() {
        if (onClose != null) {
            onClose.run();
        }
    }

    /**
     * Gets the currently selected product item string.
     *
     * @return the selected product string
     */
    public String getSelectedItem() {
        return items[selectedIndex];
    }

    /**
     * Parse product string to extract name, ID, and price
     * Format: "🥛 Fresh Milk (MLK00001) - ₱68.00"
     * Returns: [productName, productId, priceAsString]
     */
    public String[] parseProductInfo(String productString) {
        try {
            // Remove emoji at the start (if present)
            String cleaned = productString.replaceAll("^[^A-Za-z0-9]+", "").trim();
            
            // Find product name (everything before the parenthesis)
            int openParen = cleaned.indexOf('(');
            if (openParen == -1) return null;
            
            String productName = cleaned.substring(0, openParen).trim();
            
            // Find product ID (inside parentheses)
            int closeParen = cleaned.indexOf(')');
            if (closeParen == -1) return null;
            
            String productId = cleaned.substring(openParen + 1, closeParen).trim();
            
            // Find price (after the dash and peso sign)
            int dashIndex = cleaned.indexOf('-', closeParen);
            if (dashIndex == -1) return null;
            
            String priceStr = cleaned.substring(dashIndex + 1).trim();
            priceStr = priceStr.replaceAll("[^0-9.]", "");
            
            return new String[]{productName, productId, priceStr};
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Gets the name of the selected product.
     *
     * @return the product name
     */
    public String getSelectedProductName() {
        // In return mode, return the full product name directly
        if (inReturnMode) {
            return getSelectedItem();
        }
        // In buy mode, parse the product info
        String[] info = parseProductInfo(getSelectedItem());
        return info != null ? info[0] : null;
    }

    /**
     * Gets the ID of the selected product.
     *
     * @return the product ID
     */
    public String getSelectedProductId() {
        String[] info = parseProductInfo(getSelectedItem());
        return info != null ? info[1] : null;
    }

    /**
     * Gets the price of the selected product.
     *
     * @return the product price
     */
    public double getSelectedProductPrice() {
        String[] info = parseProductInfo(getSelectedItem());
        if (info != null) {
            try {
                return Double.parseDouble(info[2]);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }
}
