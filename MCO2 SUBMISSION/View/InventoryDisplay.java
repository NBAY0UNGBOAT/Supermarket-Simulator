package View;

import Controller.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Displays the shopper's inventory/equipment contents
 */
public class InventoryDisplay extends JPanel {
    private ShopperInventory inventory;
    private boolean showReceiptMode = false;
    private int receiptScrollOffset = 0;

    /**
     * Constructs a new InventoryDisplay showing inventory items.
     *
     * @param inventory the shopper's inventory to display
     * @param onClose callback runnable when display is closed
     */
    public InventoryDisplay(ShopperInventory inventory, Runnable onClose) {
        this.inventory = inventory;
        this.showReceiptMode = false;
        this.receiptScrollOffset = 0;
        setOpaque(false);
        setFocusable(true);
        setupKeyListener();
    }
    
    /**
     * Constructs a new InventoryDisplay with optional receipt mode.
     *
     * @param inventory the shopper's inventory to display
     * @param onClose callback runnable when display is closed
     * @param showReceipt whether to show receipt mode instead of inventory
     */
    public InventoryDisplay(ShopperInventory inventory, Runnable onClose, boolean showReceipt) {
        this.inventory = inventory;
        this.showReceiptMode = showReceipt;
        this.receiptScrollOffset = 0;
        setOpaque(false);
        setFocusable(true);
        setupKeyListener();
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (showReceiptMode && inventory.getLastReceipt() != null) {
                    String[] lines = inventory.getLastReceipt().split("\n");
                    int maxLines = 20;
                    int totalLines = lines.length;
                    int maxScroll = Math.max(0, totalLines - maxLines);

                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        receiptScrollOffset = Math.max(0, receiptScrollOffset - 1);
                        repaint();
                        e.consume();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        receiptScrollOffset = Math.min(maxScroll, receiptScrollOffset + 1);
                        repaint();
                        e.consume();
                    }
                }
            }
        });
    }

    /**
     * Sets the scroll offset for receipt display.
     *
     * @param offset the scroll offset in lines
     */
    public void setReceiptScrollOffset(int offset) {
        this.receiptScrollOffset = offset;
    }

    /**
     * Gets the current receipt scroll offset.
     *
     * @return the scroll offset in lines
     */
    public int getReceiptScrollOffset() {
        return receiptScrollOffset;
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

        int menuWidth = 300;
        int menuHeight = 500;
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
        String title = (showReceiptMode && inventory.getLastReceipt() != null) ? "🧾 RECEIPT" : "🛒 INVENTORY";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (menuWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 30);

        // If receipt mode and receipt exists, show it
        if (showReceiptMode && inventory.getLastReceipt() != null) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.PLAIN, 11));
            int lineY = y + 60;
            String[] lines = inventory.getLastReceipt().split("\n");
            int maxLines = 20;
            int lineCount = 0;
            
            // Display lines starting from scroll offset
            for (int i = receiptScrollOffset; i < lines.length && lineCount < maxLines; i++) {
                g.drawString(lines[i], x + 15, lineY);
                lineY += 16;
                lineCount++;
            }
            
            // Show scroll indicator
            int totalLines = lines.length;
            if (totalLines > maxLines) {
                g.setColor(new Color(100, 100, 100));
                g.setFont(new Font("Arial", Font.ITALIC, 9));
                g.drawString(String.format("(↑↓ Scroll: %d/%d)", receiptScrollOffset, totalLines - maxLines), 
                            x + 15, lineY + 10);
            }
        } else {
            // Show regular inventory
            g.setColor(new Color(100, 255, 100));
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Equipment: " + inventory.getEquipment().getDisplayName(), x + 20, y + 60);

            // Draw summary
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            int lineY = y + 85;
            g.drawString("Total Items: " + inventory.getTotalQuantity() + "/" + inventory.getEquipment().getCapacity(), 
                         x + 20, lineY);
            lineY += 20;
            g.drawString("Unique Products: " + inventory.getUniqueProductCount(), x + 20, lineY);
            lineY += 20;
            g.drawString("Total Price: ₱" + String.format("%.2f", inventory.getTotalPrice()), x + 20, lineY);
            lineY += 30;

            // Draw items list
            g.setColor(new Color(200, 200, 200));
            g.setFont(new Font("Arial", Font.ITALIC, 11));
            g.drawString("--- Items ---", x + 20, lineY);
            lineY += 20;

            if (inventory.isEmpty()) {
                g.setColor(Color.GRAY);
                g.drawString("(Empty)", x + 20, lineY);
            } else {
                g.setColor(Color.WHITE);
                int maxItems = 15; // Show max 15 items
                int itemCount = 0;
                for (InventoryItem item : inventory.getItems()) {
                    if (itemCount >= maxItems) {
                        g.drawString("... and more", x + 20, lineY);
                        break;
                    }
                    String itemStr = String.format("• %s (x%d) - ₱%.2f", 
                        item.getName(), item.getQuantity(), item.getTotalPrice());
                    g.drawString(itemStr, x + 20, lineY);
                    lineY += 18;
                    itemCount++;
                }
            }
        }

        // Draw instructions
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 11));
        g.drawString("Press ESC or V to close", x + 20, y + menuHeight - 20);

        g.dispose();
    }
}
