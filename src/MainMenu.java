

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class MainMenu extends JPanel {
    private BufferedImage backgroundImage;
    private JButton playButton;
    private JButton quitButton;
    private Runnable onPlayGame;
    private Runnable onQuitGame;

    public MainMenu(Runnable onPlay, Runnable onQuit) {
        this.onPlayGame = onPlay;
        this.onQuitGame = onQuit;

        setLayout(null);
        setBackground(new Color(20, 20, 20));

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("Graphics/Tileset/Menu.jpg"));
        } catch (Exception e) {
            System.err.println("Failed to load menu background: " + e.getMessage());
        }

        // Create Play Game button - Gangsta style
        playButton = new JButton("PLAY");
        playButton.setFont(new Font("Arial Black", Font.BOLD, 22));
        playButton.setBackground(new Color(255, 215, 0));
        playButton.setForeground(new Color(20, 20, 20));
        playButton.setFocusPainted(false);
        playButton.setBorder(BorderFactory.createLineBorder(new Color(255, 165, 0), 3));
        playButton.setBounds(208, 280, 200, 60);
        playButton.addActionListener(e -> {
            if (onPlayGame != null) onPlayGame.run();
        });
        add(playButton);

        // Create Quit Game button - Gangsta style
        quitButton = new JButton("EXIT");
        quitButton.setFont(new Font("Arial Black", Font.BOLD, 22));
        quitButton.setBackground(new Color(220, 20, 60));
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 52), 3));
        quitButton.setBounds(208, 360, 200, 60);
        quitButton.addActionListener(e -> {
            if (onQuitGame != null) onQuitGame.run();
        });
        add(quitButton);

        setPreferredSize(new Dimension(616, 616));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background image if available
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw dark semi-transparent overlay
        g2d.setColor(new Color(0, 0, 0, 140));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw main title with gangsta style
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 56));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "SUPERMARKET";
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        
        // Add shadow effect
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.drawString(title, titleX + 3, 120 + 3);
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString(title, titleX, 120);
        
        // Draw subtitle
        g2d.setFont(new Font("Arial Black", Font.BOLD, 36));
        String subtitle = "SIMULATOR";
        fm = g2d.getFontMetrics();
        int subtitleX = (getWidth() - fm.stringWidth(subtitle)) / 2;
        
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.drawString(subtitle, subtitleX + 3, 170 + 3);
        
        g2d.setColor(new Color(220, 20, 60));
        g2d.drawString(subtitle, subtitleX, 170);
    }
}
