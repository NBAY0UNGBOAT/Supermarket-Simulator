import javax.swing.*;
import java.awt.*;

public class WithdrawalSuccessPanel extends JPanel {
    private double withdrawnAmount;

    public WithdrawalSuccessPanel(double amount) {
        this.withdrawnAmount = amount;
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
        g.setColor(new Color(0, 200, 100));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, boxWidth, boxHeight);

        // Draw title
        g.setColor(new Color(100, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String title = "WITHDRAWAL SUCCESS!";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (boxWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 45);

        // Draw separator
        g.setColor(new Color(100, 100, 120));
        g.drawLine(x + 20, y + 60, x + boxWidth - 20, y + 60);

        // Draw amount withdrawn
        g.setColor(new Color(200, 200, 220));
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String amountLabel = "Amount Withdrawn:";
        int labelX = x + (boxWidth - fm.stringWidth(amountLabel)) / 2;
        g.drawString(amountLabel, labelX, y + 95);

        // Draw amount value
        g.setColor(new Color(100, 255, 100));
        g.setFont(new Font("Arial", Font.BOLD, 28));
        String amount = "₱ " + String.format("%,.2f", withdrawnAmount);
        fm = g.getFontMetrics();
        int amountX = x + (boxWidth - fm.stringWidth(amount)) / 2;
        g.drawString(amount, amountX, y + 140);

        // Draw instructions
        g.setColor(new Color(150, 150, 170));
        g.setFont(new Font("Arial", Font.ITALIC, 10));
        String instructions = "Press ESC to return to menu";
        fm = g.getFontMetrics();
        int instructionsX = x + (boxWidth - fm.stringWidth(instructions)) / 2;
        g.drawString(instructions, instructionsX, y + boxHeight - 15);

        g.dispose();
    }

    public double getWithdrawnAmount() {
        return withdrawnAmount;
    }
}
