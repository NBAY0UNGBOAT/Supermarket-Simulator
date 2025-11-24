

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Single unified panel for player registration, age confirmation, and starting balance input
 */
public class GameStartPanel extends JPanel {
    private JTextField usernameField;
    private JSpinner ageSpinner;
    private JSpinner balanceSpinner;
    private JButton startButton;
    private Runnable onStart;
    private String username = "";
    private int age = 18;
    private double balance = 100.0;
    private JLabel warningLabel;

    public GameStartPanel(Runnable onStart) {
        this.onStart = onStart;
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(25, 25, 35));
        setPreferredSize(new Dimension(600, 500));

        // Create main content panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(25, 25, 35));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Header section with title
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(25, 25, 35));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Welcome to Supermarket Simulator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(255, 180, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Create Your Shopping Profile");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        subtitleLabel.setForeground(new Color(180, 180, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(new EmptyBorder(2, 0, 10, 0));
        headerPanel.add(subtitleLabel);

        mainPanel.add(headerPanel);

        // Input section - organized with visual separation
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(35, 35, 45));
        inputPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(100, 100, 120), 1),
            new EmptyBorder(12, 20, 12, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username row
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(new Color(220, 220, 240));
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(18);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(new Color(50, 50, 65));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(new Color(255, 180, 0));
        usernameField.setBorder(new LineBorder(new Color(100, 100, 130), 1));
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(usernameField, gbc);

        // Age row
        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setForeground(new Color(220, 220, 240));
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(ageLabel, gbc);

        SpinnerModel ageModel = new SpinnerNumberModel(18, 10, 120, 1);
        ageSpinner = new JSpinner(ageModel);
        ageSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        ageSpinner.addChangeListener(e -> {
            age = (Integer) ageSpinner.getValue();
            updateWarning();
        });

        JComponent ageEditor = ageSpinner.getEditor();
        JFormattedTextField ageTextField = ((JSpinner.DefaultEditor) ageEditor).getTextField();
        ageTextField.setBackground(new Color(50, 50, 65));
        ageTextField.setForeground(Color.WHITE);
        ageTextField.setCaretColor(new Color(255, 180, 0));
        ageTextField.setEditable(true);
        ageTextField.setBorder(new LineBorder(new Color(100, 100, 130), 1));

        JPanel agePanel = new JPanel(new BorderLayout());
        agePanel.setBackground(new Color(35, 35, 45));
        agePanel.add(ageSpinner, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(agePanel, gbc);

        // Starting Balance row
        JLabel balanceLabel = new JLabel("Starting Balance:");
        balanceLabel.setForeground(new Color(220, 220, 240));
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(balanceLabel, gbc);

        SpinnerModel balanceModel = new SpinnerNumberModel(100.0, 0.0, 100000.0, 100.0);
        balanceSpinner = new JSpinner(balanceModel);
        balanceSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        balanceSpinner.addChangeListener(e -> {
            balance = (Double) balanceSpinner.getValue();
        });

        JComponent balanceEditor = balanceSpinner.getEditor();
        JFormattedTextField balanceTextField = ((JSpinner.DefaultEditor) balanceEditor).getTextField();
        balanceTextField.setBackground(new Color(50, 50, 65));
        balanceTextField.setForeground(Color.WHITE);
        balanceTextField.setCaretColor(new Color(255, 180, 0));
        balanceTextField.setEditable(true);
        balanceTextField.setBorder(new LineBorder(new Color(100, 100, 130), 1));

        JPanel balancePanel = new JPanel(new BorderLayout());
        balancePanel.setBackground(new Color(35, 35, 45));
        balancePanel.add(balanceSpinner, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(balancePanel, gbc);

        // Currency indicator
        JLabel currencyLabel = new JLabel("₱ (Philippine Pesos)");
        currencyLabel.setForeground(new Color(150, 150, 170));
        currencyLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(currencyLabel, gbc);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Warning/Info label
        warningLabel = new JLabel("");
        warningLabel.setForeground(new Color(255, 180, 0));
        warningLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(warningLabel);

        // Info section
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(45, 45, 60));
        infoPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(80, 120, 180), 2),
            new EmptyBorder(12, 20, 12, 20)
        ));

        JLabel infoTitleLabel = new JLabel("Store Policy");
        infoTitleLabel.setFont(new Font("Arial", Font.BOLD, 13));
        infoTitleLabel.setForeground(new Color(100, 180, 255));
        infoTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(infoTitleLabel);

        infoPanel.add(Box.createVerticalStrut(8));

        JLabel infoLine1 = new JLabel("• Minors will not be sold Alcohol and Cleaning Agents");
        infoLine1.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLine1.setForeground(new Color(200, 200, 220));
        infoLine1.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(infoLine1);

        infoPanel.add(Box.createVerticalStrut(5));

        JLabel infoLine2 = new JLabel("• Senior customers are entitled to discounts: 20% off food, 10% off beverages");
        infoLine2.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLine2.setForeground(new Color(200, 200, 220));
        infoLine2.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(infoLine2);

        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createVerticalStrut(12));

        // Button section
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBackground(new Color(25, 25, 35));

        startButton = new JButton("START SHOPPING");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(0, 150, 80));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(new EmptyBorder(12, 40, 12, 40));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(0, 180, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(0, 150, 80));
            }
        });
        startButton.addActionListener(e -> handleStart());

        buttonPanel.add(startButton);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);

        updateWarning();
    }

    private void updateWarning() {
        if (age < 10) {
            warningLabel.setText("⚠ Age must be 10 or older");
            warningLabel.setForeground(new Color(255, 100, 100));
            startButton.setEnabled(false);
        } else if (age < 18) {
            warningLabel.setText("");
            startButton.setEnabled(true);
        } else if (age >= 60) {
            warningLabel.setText("✓ You qualify for senior citizen discounts!");
            warningLabel.setForeground(new Color(100, 220, 100));
            startButton.setEnabled(true);
        } else {
            warningLabel.setText("");
            startButton.setEnabled(true);
        }
    }

    private void handleStart() {
        String user = usernameField.getText().trim();
        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (age < 10) {
            JOptionPane.showMessageDialog(this, "Age must be at least 10", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        username = user;
        if (onStart != null) {
            onStart.run();
        }
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public double getBalance() {
        return balance;
    }
}
