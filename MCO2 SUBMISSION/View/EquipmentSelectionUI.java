package View;

import Controller.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * UI for selecting equipment (Hands, Basket, or Cart)
 */
public class EquipmentSelectionUI extends JPanel {
    private ShopperInventory inventory;
    private Runnable onClose;
    private int selectedIndex = 0;
    private static final int HANDS = 0;
    private static final int BASKET = 1;
    private static final int CART = 2;
    private static final int RETURN = 3;  // New option to return equipment

    /**
     * Constructs a new EquipmentSelectionUI for selecting equipment types.
     *
     * @param inventory the shopper's inventory
     * @param onClose callback runnable when equipment selection is complete
     */
    public EquipmentSelectionUI(ShopperInventory inventory, Runnable onClose) {
        this.inventory = inventory;
        this.onClose = onClose;
        setOpaque(false);
        setFocusable(true);
        
        // Set current equipment as selected
        switch (inventory.getEquipment()) {
            case HANDS:
                selectedIndex = HANDS;
                break;
            case BASKET:
                selectedIndex = BASKET;
                break;
            case CART:
                selectedIndex = CART;
                break;
        }
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selectedIndex = (selectedIndex - 1 + 4) % 4;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selectedIndex = (selectedIndex + 1) % 4;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    selectEquipment();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onClose.run();
                }
            }
        });
    }

    private void selectEquipment() {
        switch (selectedIndex) {
            case HANDS:
                inventory.setEquipment(ShopperInventory.EquipmentType.HANDS);
                break;
            case BASKET:
                inventory.setEquipment(ShopperInventory.EquipmentType.BASKET);
                inventory.equipCurrentEquipment();
                break;
            case CART:
                inventory.setEquipment(ShopperInventory.EquipmentType.CART);
                inventory.equipCurrentEquipment();
                break;
            case RETURN:
                // Return equipment and clear inventory
                inventory.returnEquipment();
                inventory.clear();
                break;
        }
        onClose.run();
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

        int menuWidth = 400;
        int menuHeight = 280;
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
        String title = "⚙️ SELECT EQUIPMENT";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (menuWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 35);

        // Draw instructions
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 11));
        g.drawString("UP/DOWN to select • ENTER to confirm • ESC to cancel", x + 15, y + 60);

        // Draw equipment options
        String[] options = {
            "👐 Hands (Capacity: 2)", 
            "🧺 Basket (Capacity: 15)", 
            "🛒 Cart (Capacity: 30)",
            "❌ Return Equipment (Forfeit Items)"
        };
        int optionY = y + 100;

        for (int i = 0; i < options.length; i++) {
            if (i == selectedIndex) {
                g.setColor(new Color(100, 150, 255));
                g.fillRect(x + 20, optionY - 18, menuWidth - 40, 28);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 14));
            } else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 14));
            }

            g.drawString("> " + options[i], x + 30, optionY);
            optionY += 45;
        }

        g.dispose();
    }
}
