import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * UI for selecting quantity of a product
 */
public class QuantityInputPanel extends JPanel {
    private String productName;
    private double pricePerUnit;
    private int maxAvailable;  // Maximum quantity available in the store
    private int maxCanCarry;   // Maximum based on inventory capacity
    private int selectedQuantity = 1;
    private Runnable onConfirm;
    private Runnable onCancel;
    private String errorMessage = "";

    public QuantityInputPanel(String productName, double pricePerUnit, int maxAvailable, 
                             int maxCanCarry, Runnable onConfirm, Runnable onCancel) {
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.maxAvailable = maxAvailable;
        this.maxCanCarry = maxCanCarry;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
        
        setOpaque(false);
        setFocusable(true);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selectedQuantity = Math.min(selectedQuantity + 1, Math.min(maxAvailable, maxCanCarry));
                    errorMessage = "";
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selectedQuantity = Math.max(selectedQuantity - 1, 1);
                    errorMessage = "";
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (selectedQuantity > maxCanCarry) {
                        errorMessage = "You can only carry " + maxCanCarry + " of this product!";
                        repaint();
                    } else {
                        onConfirm.run();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    onCancel.run();
                }
            }
        });
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void increaseQuantity() {
        int maxQty = Math.min(maxAvailable, maxCanCarry);
        if (selectedQuantity < maxQty) {
            selectedQuantity++;
            errorMessage = "";
        }
    }

    public void decreaseQuantity() {
        if (selectedQuantity > 1) {
            selectedQuantity--;
            errorMessage = "";
        }
    }

    public void confirmQuantity(Runnable onConfirmCallback) {
        if (selectedQuantity > maxCanCarry) {
            errorMessage = "You can only carry " + maxCanCarry + " of this product!";
        } else {
            onConfirmCallback.run();
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

        int menuWidth = 450;
        int menuHeight = 320;
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
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String title = "Select Quantity";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (menuWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 35);

        // Draw product info
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 13));
        g.drawString("Product: " + productName, x + 20, y + 70);
        g.drawString("Price per unit: ₱" + String.format("%.2f", pricePerUnit), x + 20, y + 95);
        g.drawString("Max available: " + maxAvailable, x + 20, y + 120);
        g.drawString("Max you can carry: " + maxCanCarry, x + 20, y + 145);

        // Draw quantity display with visual bar
        g.setColor(new Color(100, 150, 255));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String qtyStr = "Quantity: " + selectedQuantity;
        g.drawString(qtyStr, x + 20, y + 185);

        // Draw quantity bar
        int barWidth = 300;
        int barHeight = 30;
        int barX = x + 20;
        int barY = y + 195;
        
        // Background bar
        g.setColor(new Color(60, 60, 60));
        g.fillRect(barX, barY, barWidth, barHeight);
        
        // Filled bar
        int maxQty = Math.min(maxAvailable, maxCanCarry);
        int fillWidth = (int) (barWidth * ((double) selectedQuantity / maxQty));
        g.setColor(new Color(100, 200, 100));
        g.fillRect(barX, barY, fillWidth, barHeight);
        
        // Border
        g.setColor(new Color(200, 200, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRect(barX, barY, barWidth, barHeight);

        // Draw total price
        double totalPrice = selectedQuantity * pricePerUnit;
        g.setColor(new Color(100, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String totalStr = "Total: ₱" + String.format("%.2f", totalPrice);
        g.drawString(totalStr, x + 20, y + 250);

        // Draw error message if any
        if (!errorMessage.isEmpty()) {
            g.setColor(new Color(255, 100, 100));
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(errorMessage, x + 20, y + 275);
        }

        // Draw instructions
        g.setColor(new Color(200, 200, 200));
        g.setFont(new Font("Arial", Font.ITALIC, 11));
        g.drawString("UP/DOWN: Change quantity | ENTER: Confirm | ESC: Cancel", x + 20, y + menuHeight - 15);

        g.dispose();
    }
}
