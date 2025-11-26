package View;

import Controller.*;
import Model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class ATMPanel extends JPanel {
    private JTextField amountField;
    private JButton withdrawButton;
    private JButton cancelButton;
    private double currentBalance;
    private Runnable onWithdraw;
    private Runnable onCancel;

    public ATMPanel(double currentBalance, Runnable onWithdraw, Runnable onCancel) {
        this.currentBalance = currentBalance;
        this.onWithdraw = onWithdraw;
        this.onCancel = onCancel;
        
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 180));

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.anchor = GridBagConstraints.CENTER;

        // Main ATM panel with border
        JPanel atmPanel = new JPanel();
        atmPanel.setLayout(new GridBagLayout());
        atmPanel.setBackground(new Color(30, 30, 50));
        atmPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 180, 0), 3),
            new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("ATM MACHINE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(255, 215, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        atmPanel.add(titleLabel, gbc);

        // Separator line
        JSeparator separator1 = new JSeparator();
        separator1.setBackground(new Color(100, 100, 120));
        separator1.setForeground(new Color(100, 100, 120));
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 0, 12, 0);
        atmPanel.add(separator1, gbc);

        // Current balance section header
        gbc.insets = new Insets(12, 15, 8, 15);
        JLabel balanceHeaderLabel = new JLabel("Available Balance:");
        balanceHeaderLabel.setForeground(new Color(150, 200, 255));
        balanceHeaderLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        atmPanel.add(balanceHeaderLabel, gbc);

        // Balance amount - large display
        JLabel balanceLabel = new JLabel("₱ " + String.format("%,.2f", currentBalance));
        balanceLabel.setForeground(new Color(100, 255, 100));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 15, 15, 15);
        atmPanel.add(balanceLabel, gbc);

        // Separator line
        JSeparator separator2 = new JSeparator();
        separator2.setBackground(new Color(100, 100, 120));
        separator2.setForeground(new Color(100, 100, 120));
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 12, 0);
        atmPanel.add(separator2, gbc);

        // Withdrawal amount section header
        gbc.insets = new Insets(12, 15, 8, 15);
        JLabel amountLabel = new JLabel("Withdrawal Amount:");
        amountLabel.setForeground(new Color(220, 220, 240));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        atmPanel.add(amountLabel, gbc);

        // Amount input field with styling
        amountField = new JTextField(16);
        amountField.setFont(new Font("Arial", Font.PLAIN, 18));
        amountField.setBackground(new Color(50, 50, 70));
        amountField.setForeground(Color.WHITE);
        amountField.setCaretColor(new Color(255, 215, 0));
        amountField.setBorder(new LineBorder(new Color(100, 100, 140), 2));
        gbc.gridy = 6;
        gbc.insets = new Insets(8, 15, 12, 15);
        atmPanel.add(amountField, gbc);

        // Currency note
        JLabel currencyNote = new JLabel("(Enter amount in Philippine Pesos)");
        currencyNote.setForeground(new Color(150, 150, 170));
        currencyNote.setFont(new Font("Arial", Font.ITALIC, 10));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 15, 12, 15);
        atmPanel.add(currencyNote, gbc);

        // Separator line
        JSeparator separator3 = new JSeparator();
        separator3.setBackground(new Color(100, 100, 120));
        separator3.setForeground(new Color(100, 100, 120));
        gbc.gridy = 8;
        gbc.insets = new Insets(5, 0, 15, 0);
        atmPanel.add(separator3, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(30, 30, 50));

        // Withdraw button
        withdrawButton = new JButton("WITHDRAW");
        withdrawButton.setFont(new Font("Arial", Font.BOLD, 14));
        withdrawButton.setBackground(new Color(0, 180, 100));
        withdrawButton.setForeground(Color.WHITE);
        withdrawButton.setFocusPainted(false);
        withdrawButton.setBorder(new EmptyBorder(12, 35, 12, 35));
        withdrawButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        withdrawButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                withdrawButton.setBackground(new Color(0, 210, 120));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                withdrawButton.setBackground(new Color(0, 180, 100));
            }
        });
        withdrawButton.addActionListener(e -> handleWithdraw());
        buttonPanel.add(withdrawButton);

        // Cancel button
        cancelButton = new JButton("CANCEL");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(200, 80, 80));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorder(new EmptyBorder(12, 35, 12, 35));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(230, 100, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(200, 80, 80));
            }
        });
        cancelButton.addActionListener(e -> handleCancel());
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        atmPanel.add(buttonPanel, gbc);

        // Add main panel to center
        add(atmPanel, mainGbc);

        setFocusable(true);
        requestFocusInWindow();
        amountField.requestFocusInWindow();
    }

    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a positive amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (amount > currentBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (onWithdraw != null) {
                onWithdraw.run();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancel() {
        if (onCancel != null) {
            onCancel.run();
        }
    }

    public double getWithdrawalAmount() {
        try {
            return Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void handleConfirm() {
        handleWithdraw();
    }
}
