

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserRegistrationPanel extends JPanel {
    private JTextField usernameField;
    private JSpinner ageSpinner;
    private JButton confirmButton;
    private Runnable onConfirm;
    private String username = "";
    private int age = 18;

    public UserRegistrationPanel(Runnable onConfirm) {
        this.onConfirm = onConfirm;
        setLayout(new GridBagLayout());
        setBackground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        // Age label and spinner
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(ageLabel, gbc);

        ageSpinner = new JSpinner(new SpinnerNumberModel(18, 13, 120, 1));
        ageSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(ageSpinner, gbc);

        // Confirm button
        confirmButton = new JButton("Continue");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(100, 200, 100));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addActionListener(e -> handleConfirm());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(confirmButton, gbc);

        setFocusable(true);
        requestFocusInWindow();
        usernameField.requestFocusInWindow();
    }

    private void handleConfirm() {
        username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        age = (Integer) ageSpinner.getValue();
        if (onConfirm != null) {
            onConfirm.run();
        }
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }
}
