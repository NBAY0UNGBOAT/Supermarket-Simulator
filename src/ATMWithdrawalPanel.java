import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ATMWithdrawalPanel extends JPanel {
    private String inputText = "";
    private static final double MAX_WITHDRAWAL = 20000.0;

    public ATMWithdrawalPanel() {
        setOpaque(false);
        setFocusable(true);
        
        // Add key listener for number input
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Allow digits and decimal point
                if (Character.isDigit(c)) {
                    if (inputText.length() < 8) {  // Limit to 8 characters
                        inputText += c;
                        repaint();
                    }
                    e.consume();
                } else if (c == '.' && !inputText.contains(".")) {
                    if (inputText.length() > 0 && inputText.length() < 8) {
                        inputText += c;
                        repaint();
                    }
                    e.consume();
                } else if (c == KeyEvent.VK_BACK_SPACE) {
                    if (inputText.length() > 0) {
                        inputText = inputText.substring(0, inputText.length() - 1);
                        repaint();
                    }
                    e.consume();
                }
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                // Handle backspace key press
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (inputText.length() > 0) {
                        inputText = inputText.substring(0, inputText.length() - 1);
                        repaint();
                    }
                    e.consume();
                }
            }
        });
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

        // Box dimensions
        int boxWidth = 380;
        int boxHeight = 240;
        int x = (panelWidth - boxWidth) / 2;
        int y = (panelHeight - boxHeight) / 2;

        // Draw box background
        g.setColor(new Color(30, 30, 50));
        g.fillRect(x, y, boxWidth, boxHeight);
        g.setColor(new Color(0, 180, 100));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, boxWidth, boxHeight);

        // Draw title
        g.setColor(new Color(0, 220, 130));
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String title = "WITHDRAWAL";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (boxWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 40);

        // Draw separator
        g.setColor(new Color(100, 100, 120));
        g.drawLine(x + 20, y + 55, x + boxWidth - 20, y + 55);

        // Draw max withdrawal limit
        g.setColor(new Color(255, 180, 0));
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String limitText = "Maximum Withdrawal: ₱ " + String.format("%,.2f", MAX_WITHDRAWAL);
        fm = g.getFontMetrics();
        int limitX = x + (boxWidth - fm.stringWidth(limitText)) / 2;
        g.drawString(limitText, limitX, y + 85);

        // Draw separator
        g.setColor(new Color(100, 100, 120));
        g.drawLine(x + 20, y + 100, x + boxWidth - 20, y + 100);

        // Draw input label
        g.setColor(new Color(200, 200, 220));
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        String inputLabel = "Enter Withdrawal Amount (₱):";
        fm = g.getFontMetrics();
        int labelX = x + (boxWidth - fm.stringWidth(inputLabel)) / 2;
        g.drawString(inputLabel, labelX, y + 130);

        // Draw input field box
        g.setColor(new Color(50, 50, 70));
        g.fillRect(x + 30, y + 145, boxWidth - 60, 35);
        g.setColor(new Color(100, 200, 100));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x + 30, y + 145, boxWidth - 60, 35);

        // Draw entered text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        String displayText = inputText.isEmpty() ? "0.00" : inputText;
        fm = g.getFontMetrics();
        int textX = x + 45;
        g.drawString(displayText, textX, y + 168);

        // Draw instructions
        g.setColor(new Color(150, 150, 170));
        g.setFont(new Font("Arial", Font.ITALIC, 10));
        String instructions = "Type amount | ENTER: Confirm | ESC: Cancel";
        fm = g.getFontMetrics();
        int instructionsX = x + (boxWidth - fm.stringWidth(instructions)) / 2;
        g.drawString(instructions, instructionsX, y + boxHeight - 15);

        g.dispose();
    }

    public void setWithdrawalAmount(double amount) {
        // Method for programmatic setting if needed
    }

    public double getWithdrawalAmount() {
        if (inputText.isEmpty()) {
            return 0.0;
        }
        try {
            double amount = Double.parseDouble(inputText);
            // Cap at MAX_WITHDRAWAL
            return Math.min(amount, MAX_WITHDRAWAL);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getMaxWithdrawal() {
        return MAX_WITHDRAWAL;
    }
    
    public void handleKeyInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (inputText.length() > 0) {
                inputText = inputText.substring(0, inputText.length() - 1);
                repaint();
            }
            e.consume();
        }
    }
    
    public void handleCharInput(KeyEvent e) {
        char c = e.getKeyChar();
        // Allow digits and decimal point
        if (Character.isDigit(c)) {
            if (inputText.length() < 8) {
                inputText += c;
                repaint();
            }
            e.consume();
        } else if (c == '.' && !inputText.contains(".")) {
            if (inputText.length() > 0 && inputText.length() < 8) {
                inputText += c;
                repaint();
            }
            e.consume();
        }
    }
}
