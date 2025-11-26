package Controller;

import View.*;
import Model.*;

import javax.swing.*;

/**
 * Main entry point for the Supermarket Simulator game.
 * Sets up the application window, manages UI state transitions between menu, registration, and game.
 */
public class Driver {
    /**
     * Starts the Supermarket Simulator application.
     * Initializes the main window and displays the game menu with options to play or quit.
     * Handles transitions between menu, player registration, and game screens.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize main game window
            JFrame frame = new JFrame("Supermarket Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            // Create main content panel that will switch between menu, registration, and game
            JPanel contentPanel = new JPanel(new java.awt.CardLayout());
            
            // Create menu with callbacks for Play and Quit
            MainMenu menu = new MainMenu(
                () -> {
                    // Play Game action - show registration panel in same window
                    java.awt.CardLayout cl = (java.awt.CardLayout) contentPanel.getLayout();
                    // Show the registration panel
                    
                    final GameStartPanel[] startPanelHolder = new GameStartPanel[1];
                    startPanelHolder[0] = new GameStartPanel(() -> {
                        String username = startPanelHolder[0].getUsername();
                        int age = startPanelHolder[0].getAge();
                        double startingBalance = startPanelHolder[0].getBalance();
                        
                        // Now start the actual game
                        // Get the game wrapper and update the game with user data
                        JLabel status = new JLabel("Loading...");
                        TileGrid gamePanel = new TileGrid(status);
                        gamePanel.setPlayerData(username, age, startingBalance);
                        
                        // Create a wrapper for the game with status bar
                        JPanel gameWrapper = new JPanel(new java.awt.BorderLayout());
                        gameWrapper.add(gamePanel, java.awt.BorderLayout.CENTER);
                        status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                        gameWrapper.add(status, java.awt.BorderLayout.SOUTH);
                        
                        // Replace game panel in card layout
                        contentPanel.remove(2);  // Remove old game panel if exists
                        contentPanel.add(gameWrapper, "game", 2);
                        
                        cl.show(contentPanel, "game");
                    });
                    
                    // Replace registration panel in card layout
                    contentPanel.remove(1);  // Remove old registration panel if exists
                    contentPanel.add(startPanelHolder[0], "registration", 1);
                    
                    cl.show(contentPanel, "registration");
                },
                () -> {
                    // Quit Game action
                    System.exit(0);
                }
            );
            
            // Create initial game panel (will be replaced)
            JLabel status = new JLabel("Loading...");
            TileGrid gamePanel = new TileGrid(status);
            
            // Create a wrapper for the game with status bar
            JPanel gameWrapper = new JPanel(new java.awt.BorderLayout());
            gameWrapper.add(gamePanel, java.awt.BorderLayout.CENTER);
            status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            gameWrapper.add(status, java.awt.BorderLayout.SOUTH);
            
            // Create dummy registration panel initially
            GameStartPanel initialRegPanel = new GameStartPanel(() -> {});
            
            // Add menu, registration and game to card panel
            contentPanel.add(menu, "menu");
            contentPanel.add(initialRegPanel, "registration");
            contentPanel.add(gameWrapper, "game");
            
            frame.getContentPane().add(contentPanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            // Show menu first
            java.awt.CardLayout cl = (java.awt.CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, "menu");
        });
    }
}

