package View;

import Controller.*;
import Model.*;

import javax.swing.*;
import java.awt.*;

public class BalanceInquiryPanel extends JPanel {
    private double displayBalance;

    public BalanceInquiryPanel(double playerBankBalance) {
        // Display the exact bank balance (not random)
        this.displayBalance = playerBankBalance;
        setOpaque(false);
        setFocusable(true);
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

        // Display box dimensions
        int boxWidth = 350;
        int boxHeight = 200;
        int x = (panelWidth - boxWidth) / 2;
        int y = (panelHeight - boxHeight) / 2;

        // Draw box background
        g.setColor(new Color(30, 30, 50));
        g.fillRect(x, y, boxWidth, boxHeight);
        g.setColor(new Color(100, 200, 100));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, boxWidth, boxHeight);

        // Draw title
        g.setColor(new Color(100, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String title = "BALANCE INQUIRY";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (boxWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 40);

        // Draw separator
        g.setColor(new Color(100, 100, 120));
        g.drawLine(x + 20, y + 55, x + boxWidth - 20, y + 55);

        // Draw balance label
        g.setColor(new Color(200, 200, 220));
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String label = "Your Current Balance:";
        int labelX = x + (boxWidth - fm.stringWidth(label)) / 2;
        g.drawString(label, labelX, y + 85);

        // Draw balance amount
        g.setColor(new Color(100, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 32));
        String balance = "₱ " + String.format("%,.2f", displayBalance);
        int balanceX = x + (boxWidth - fm.stringWidth(balance)) / 2;
        g.drawString(balance, balanceX, y + 130);

        // Draw instructions
        g.setColor(new Color(150, 150, 170));
        g.setFont(new Font("Arial", Font.ITALIC, 10));
        String instructions = "Press ENTER to return to menu or ESC to exit";
        int instructionsX = x + (boxWidth - fm.stringWidth(instructions)) / 2;
        g.drawString(instructions, instructionsX, y + boxHeight - 15);

        g.dispose();
    }

    public double getDisplayBalance() {
        return displayBalance;
    }
}
