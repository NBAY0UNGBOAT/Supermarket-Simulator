package View;

import Controller.*;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ReturnProductPanel extends JPanel {
    private List<String> returnableProducts;
    private ShopperInventory inventory;
    private int selectedIndex = 0;
    private Runnable onConfirm;
    private Runnable onCancel;
    private String selectedProductName = null;

    public ReturnProductPanel(List<String> returnableProducts, ShopperInventory inventory, Runnable onConfirm, Runnable onCancel) {
        this.returnableProducts = returnableProducts;
        this.inventory = inventory;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
        setOpaque(false);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = getWidth() / 4;
        int y = getHeight() / 4;
        int w = getWidth() / 2;
        int h = getHeight() / 2;

        // Draw semi-transparent background
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect(x, y, w, h, 15, 15);
        
        // Draw border
        g.setColor(new Color(100, 200, 100));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x, y, w, h, 15, 15);

        // Draw title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Return Product", x + 20, y + 35);

        // Draw products list
        int startY = y + 60;
        int itemHeight = 35;
        
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        
        for (int i = 0; i < returnableProducts.size(); i++) {
            String productName = returnableProducts.get(i);
            int quantity = inventory.getProductQuantity(productName);
            
            if (quantity > 0) {
                int itemY = startY + (i * itemHeight);
                
                // Highlight selected item
                if (i == selectedIndex) {
                    g.setColor(new Color(100, 200, 100, 100));
                    g.fillRect(x + 10, itemY - 25, w - 20, 30);
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(200, 200, 200));
                }
                
                g.drawString("► " + productName + " (x" + quantity + ")", x + 20, itemY);
            }
        }

        // Draw instructions
        g.setColor(new Color(150, 150, 150));
        g.setFont(new Font("Arial", Font.ITALIC, 12));
        g.drawString("UP/DOWN: Select  |  ENTER: Return  |  ESC: Cancel", x + 20, y + h - 30);

        g.dispose();
    }

    public void handleKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_UP) {
            selectedIndex = (selectedIndex - 1 + returnableProducts.size()) % returnableProducts.size();
            repaint();
        } else if (keyCode == KeyEvent.VK_DOWN) {
            selectedIndex = (selectedIndex + 1) % returnableProducts.size();
            repaint();
        } else if (keyCode == KeyEvent.VK_ENTER) {
            selectedProductName = returnableProducts.get(selectedIndex);
            if (onConfirm != null) {
                onConfirm.run();
            }
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            if (onCancel != null) {
                onCancel.run();
            }
        }
    }

    public String getSelectedProductName() {
        return selectedProductName;
    }

    public int getInventoryQuantity() {
        if (selectedProductName != null) {
            return inventory.getProductQuantity(selectedProductName);
        }
        return 0;
    }
}
