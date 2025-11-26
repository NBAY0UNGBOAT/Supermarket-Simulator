package View;

import Controller.*;
import Model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Displays the main ATM menu with options for balance inquiry and withdrawal.
 */
public class ATMMenu extends JPanel {
    private String[] options = { "Balance Inquiry", "Withdrawal" };
    private int selectedIndex = 0;
    private String username;
    private double currentBalance;
    private Runnable onClose;

    /**
     * Constructs a new ATMMenu with player information.
     *
     * @param username the player's username
     * @param currentBalance the player's current bank balance
     * @param onClose callback runnable when menu is closed
     */
    public ATMMenu(String username, double currentBalance, Runnable onClose) {
        this.username = username;
        this.currentBalance = currentBalance;
        this.onClose = onClose;
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

        // Menu dimensions
        int menuWidth = 400;
        int menuHeight = 200;
        int x = (panelWidth - menuWidth) / 2;
        int y = (panelHeight - menuHeight) / 2;

        // Draw menu background with border
        g.setColor(new Color(30, 30, 50));
        g.fillRect(x, y, menuWidth, menuHeight);
        g.setColor(new Color(200, 180, 0));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, menuWidth, menuHeight);

        // Draw title
        g.setColor(new Color(255, 215, 0));
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String title = "Welcome " + username + "!";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (menuWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 30);

        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(new Color(200, 200, 220));
        String subtitle = "What would you like to do?";
        fm = g.getFontMetrics();
        int subtitleX = x + (menuWidth - fm.stringWidth(subtitle)) / 2;
        g.drawString(subtitle, subtitleX, y + 50);

        // Draw separator
        g.setColor(new Color(100, 100, 120));
        g.drawLine(x + 20, y + 60, x + menuWidth - 20, y + 60);

        // Draw menu options
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        int optionY = y + 90;
        int optionHeight = 35;

        for (int i = 0; i < options.length; i++) {
            if (i == selectedIndex) {
                g.setColor(new Color(100, 150, 255));
                g.fillRect(x + 30, optionY - 20, menuWidth - 60, optionHeight);
                g.setColor(Color.WHITE);
            } else {
                g.setColor(new Color(200, 200, 220));
            }
            
            String option = options[i];
            FontMetrics fm2 = g.getFontMetrics();
            int optionX = x + (menuWidth - fm2.stringWidth(option)) / 2;
            if (i == selectedIndex) {
                g.drawString("> " + option + " <", optionX - fm2.stringWidth("> "), optionY);
            } else {
                g.drawString(option, optionX, optionY);
            }
            optionY += optionHeight;
        }

        // Draw instructions
        g.setColor(new Color(150, 150, 170));
        g.setFont(new Font("Arial", Font.ITALIC, 10));
        String instructions = "UP/DOWN: Navigate | ENTER: Select | ESC: Exit";
        int instructionsX = x + (menuWidth - fm.stringWidth(instructions)) / 2;
        g.drawString(instructions, instructionsX, y + menuHeight - 10);

        g.dispose();
    }

    /**
     * Moves selection up in the menu options.
     */
    public void moveUp() {
        selectedIndex = (selectedIndex - 1 + options.length) % options.length;
    }

    /**
     * Moves selection down in the menu options.
     */
    public void moveDown() {
        selectedIndex = (selectedIndex + 1) % options.length;
    }

    /**
     * Gets the currently selected option index.
     *
     * @return the selected option index
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Gets the currently selected menu option.
     *
     * @return the selected option string
     */
    public String getSelectedOption() {
        return options[selectedIndex];
    }

    /**
     * Gets the player's current bank balance.
     *
     * @return the current balance
     */
    public double getCurrentBalance() {
        return currentBalance;
    }
}
