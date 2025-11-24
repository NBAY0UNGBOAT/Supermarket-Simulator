

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BalanceInputPanel extends JPanel {
    private JSpinner balanceSpinner;
    private JButton confirmButton;
    private Runnable onConfirm;
    private double balance = 100.0;

    public BalanceInputPanel(Runnable onConfirm) {
        this.onConfirm = onConfirm;
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Set Starting Balance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Balance label and spinner
        JLabel balanceLabel = new JLabel("Starting Balance (₱):");
        balanceLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(balanceLabel, gbc);

        balanceSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 100000.0, 100.0));
        balanceSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(balanceSpinner, gbc);

        // Confirm button
        confirmButton = new JButton("Start Game");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(100, 150, 255));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addActionListener(e -> handleConfirm());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(confirmButton, gbc);

        setFocusable(true);
        requestFocusInWindow();
    }

    private void handleConfirm() {
        balance = ((Number) balanceSpinner.getValue()).doubleValue();
        if (onConfirm != null) {
            onConfirm.run();
        }
    }

    public double getBalance() {
        return balance;
    }
}
