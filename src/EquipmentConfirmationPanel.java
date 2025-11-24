import javax.swing.*;
import java.awt.*;

/**
 * Confirmation panel for equipment actions (equip, return)
 */
public class EquipmentConfirmationPanel extends JPanel {
    private String message;
    private boolean isQuestion;  // true = question (yes/no), false = confirmation (ok)
    private Runnable onConfirm;
    private Runnable onCancel;

    public EquipmentConfirmationPanel(String message, boolean isQuestion, Runnable onConfirm, Runnable onCancel) {
        this.message = message;
        this.isQuestion = isQuestion;
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;

        setOpaque(false);
        setFocusable(true);

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (isQuestion) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        onConfirm.run();
                    } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                        onCancel.run();
                    }
                } else {
                    // For simple confirmation, any key closes it
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                        onCancel.run();
                    }
                }
            }
        });
    }

    public void handleKeyPress(int keyCode) {
        if (isQuestion) {
            if (keyCode == java.awt.event.KeyEvent.VK_ENTER) {
                onConfirm.run();
            } else if (keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
                onCancel.run();
            }
        } else {
            if (keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
                onCancel.run();
            }
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
        int menuHeight = 200;
        int x = (panelWidth - menuWidth) / 2;
        int y = (panelHeight - menuHeight) / 2;

        // Draw menu background
        g.setColor(new Color(40, 40, 40));
        g.fillRect(x, y, menuWidth, menuHeight);
        g.setColor(new Color(200, 200, 200));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, menuWidth, menuHeight);

        // Draw title
        g.setColor(new Color(255, 215, 0));
        g.setFont(new Font("Segoe UI", Font.BOLD, 20));
        String title = isQuestion ? "Confirm Action" : "Equipment";
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (menuWidth - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 42);

        // Draw message
        g.setColor(new Color(230, 230, 240));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        int messageY = y + 90;
        
        // Split message into multiple lines if needed
        String[] lines = message.split("\n");
        for (String line : lines) {
            int lineX = x + (menuWidth - fm.stringWidth(line)) / 2;
            g.drawString(line, lineX, messageY);
            messageY += 25;
        }

        // Draw instructions
        g.setColor(new Color(170, 190, 210));
        g.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        String instruction = isQuestion ? "ENTER: Yes | ESC: No" : "ESC: Close";
        fm = g.getFontMetrics();
        int instructionX = x + (menuWidth - fm.stringWidth(instruction)) / 2;
        g.drawString(instruction, instructionX, y + menuHeight - 18);

        g.dispose();
    }
}
