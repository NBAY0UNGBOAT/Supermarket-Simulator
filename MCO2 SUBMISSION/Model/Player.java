package Model;

import Controller.*;
import View.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

public class Player {
    private BufferedImage sheet;
    private int frameW = 1, frameH = 1;
    private int framesPerRow = 1;    // number of columns (frames) per row
    private int dirRow = 0;          // 0 = right, 1 = down, 2 = up (per your sheet)
    private int frameIndex = 0;
    private boolean moving = false;
    private boolean facingLeft = false; // when true we flip the right-facing row

    private long lastFrameAdvance = 0;
    private long lastMoveTime = 0;

    private final int frameTimeMs = 120; // ms per animation frame
    private final int stopAfterMs = 300; // ms without movement to stop walk animation

    private double scale = 2.5; // default sprite scale (changeable) — increased for larger sprite

    // Movement / position
    private int x = 0;
    private int y = 0;
    private int speed = 4;
    private long lastUpdateTime = 0;
    private double balance = 1000.0; // Player balance (money)
    private double bankBalance = 50000.0; // Bank balance (separate from current balance)

    public Player(String spritePath) {
        // Load sprite sheet and initialize animation timers
        loadSheet(spritePath);
        long now = System.currentTimeMillis();
        lastMoveTime = now;
        lastFrameAdvance = now;
        lastUpdateTime = now;
    }

    /**
     * Sets the visual scale of the player sprite.
     *
     * @param s the scale factor (1.0 = tile size)
     */
    public void setScale(double s) { if (s > 0) this.scale = s; }

    /**
     * Makes the player face a direction without starting walking animation.
     *
     * @param dRow row direction (down=+1, up=-1)
     * @param dCol column direction (right=+1, left=-1)
     */
    public void face(int dRow, int dCol) {
        // Set facing direction without movement animation
        facingLeft = false;
        if (dRow > 0) { dirRow = 1; }       // down
        else if (dRow < 0) { dirRow = 2; }  // up
        else if (dCol > 0) { dirRow = 0; }  // right
        else if (dCol < 0) { dirRow = 0; facingLeft = true; } // left uses right-row flipped
        moving = false;
        frameIndex = 0;
    }

    /**
     * Starts a walking animation in the specified direction.
     *
     * @param dRow row direction (down=+1, up=-1)
     * @param dCol column direction (right=+1, left=-1)
     */
    public void startWalking(int dRow, int dCol) {
        // Start walking animation in specified direction
        facingLeft = false;
        if (dRow > 0) { dirRow = 1; }
        else if (dRow < 0) { dirRow = 2; }
        else if (dCol > 0) { dirRow = 0; }
        else if (dCol < 0) { dirRow = 0; facingLeft = true; }
        moving = true;
        lastMoveTime = System.currentTimeMillis();
        lastUpdateTime = lastMoveTime;
    }

    /**
     * Stops the walking animation.
     */
    // Stop walking animation and reset frame
    public void stopWalking() { moving = false; frameIndex = 0; }

    /**
     * Sets the player position.
     *
     * @param px the x coordinate
     * @param py the y coordinate
     */
    public void setPosition(int px, int py) { x = px; y = py; }

    /**
     * Gets the player's X coordinate.
     *
     * @return the x position
     */
    public int getX() { return x; }

    /**
     * Gets the player's Y coordinate.
     *
     * @return the y position
     */
    public int getY() { return y; }

    /**
     * Sets the player movement speed.
     *
     * @param sp the speed value (pixels per frame)
     */
    public void setSpeed(int sp) { if (sp > 0) speed = sp; }

    /**
     * Gets the player movement speed.
     *
     * @return the current speed
     */
    public int getSpeed() { return speed; }

    /**
     * Gets the player's current balance.
     *
     * @return the current balance (money)
     */
    public double getBalance() { return balance; }

    /**
     * Sets the player's balance.
     *
     * @param b the new balance
     */
    public void setBalance(double b) { this.balance = b; }

    /**
     * Adds to the player's balance.
     *
     * @param amount the amount to add
     */
    public void addBalance(double amount) { this.balance += amount; }

    /**
     * Subtracts from the player's balance.
     *
     * @param amount the amount to subtract
     */
    public void subtractBalance(double amount) { this.balance -= amount; }
    
    /**
     * Gets the player's bank balance.
     *
     * @return the bank balance
     */
    public double getBankBalance() { return bankBalance; }

    /**
     * Sets the player's bank balance.
     *
     * @param b the new bank balance
     */
    public void setBankBalance(double b) { this.bankBalance = b; }

    /**
     * Subtracts from the bank balance.
     *
     * @param amount the amount to withdraw
     */
    public void subtractFromBankBalance(double amount) { this.bankBalance -= amount; }

    // Load sprite sheet: tries classpath resource then file system.
    // Assumes sheet layout: 3 rows x 8 columns by default (per your run.png info).
    /**
     * Loads the sprite sheet for player animation.
     *
     * @param path the path to the sprite sheet image
     */
    private void loadSheet(String path) {
        try {
            // Load sprite sheet from classpath or file system
            InputStream is = Player.class.getResourceAsStream(path);
            if (is != null) {
                sheet = ImageIO.read(is);
            } else {
                sheet = ImageIO.read(new File(path));
            }

            if (sheet != null) {
                // Parse sprite sheet layout: 3 rows x 8 cols
                if (sheet.getHeight() % 3 == 0 && sheet.getWidth() % 8 == 0) {
                    frameH = sheet.getHeight() / 3;
                    framesPerRow = 8;
                    frameW = sheet.getWidth() / framesPerRow;
                } else {
                    // fallback: attempt reasonable detection
                    int rows = 3;
                    frameH = Math.max(1, sheet.getHeight() / rows);
                    framesPerRow = Math.max(1, sheet.getWidth() / frameH);
                    frameW = Math.max(1, sheet.getWidth() / framesPerRow);
                }
            } else {
                System.err.println("Player: sheet==null after load attempt: " + path);
            }
        } catch (IOException e) {
            sheet = null;
            System.err.println("Player: failed to load sprite sheet: " + path + " -> " + e.getMessage());
        }
    }

    // Update movement & animation. Call from your game loop with System.currentTimeMillis()
    /**
     * Updates the player position and animation state.
     *
     * @param now the current time in milliseconds
     */
    public void update(long now) {
        // Update player position and animation frame
        if (sheet == null) {
            lastUpdateTime = now;
            return;
        }

        long elapsed = Math.max(0L, now - lastUpdateTime);

        if (moving) {
            // Scale movement to elapsed milliseconds (approx 60 fps baseline)
            double factor = (double) elapsed / 16.6667;
            int movePixels = (int) Math.round(speed * factor);
            if (movePixels > 0) {
                if (dirRow == 1) { // down
                    y += movePixels;
                } else if (dirRow == 2) { // up
                    y -= movePixels;
                } else { // horizontal (row 0)
                    if (facingLeft) x -= movePixels; else x += movePixels;
                }
            }
        }

        if (moving) {
            // Advance animation frame based on elapsed time
            if (now - lastFrameAdvance >= frameTimeMs) {
                frameIndex = (frameIndex + 1) % Math.max(1, framesPerRow);
                lastFrameAdvance = now;
            }
            // Stop walking if no input for threshold duration
            if (now - lastMoveTime > stopAfterMs) {
                moving = false;
                frameIndex = 0;
            }
        } else {
            frameIndex = 0;
        }

        lastUpdateTime = now;
    }

    // Draw centered at (centerX, centerY) in pixels; tileSize used as reference for desired render size.
    /**
     * Renders the player sprite at the specified screen position.
     *
     * @param g the Graphics2D context
     * @param centerX the center X coordinate on screen
     * @param centerY the center Y coordinate on screen
     * @param tileSize the size of a tile for scaling reference
     */
    public void draw(Graphics2D g, int centerX, int centerY, int tileSize) {
        // Render player sprite at specified location
        if (sheet == null) {
            // Fallback: draw red circle if sprite not loaded
            int pad = Math.max(2, tileSize / 6);
            g.setColor(new java.awt.Color(220, 40, 40));
            g.fillOval(centerX - (tileSize - pad)/2, centerY - (tileSize - pad)/2, tileSize - pad, tileSize - pad);
            return;
        }

        // Calculate sprite dimensions and position
        int dw = (int) Math.round(tileSize * scale);
        int maxDw = tileSize * 4;
        if (dw > maxDw) dw = maxDw;
        if (dw < 1) dw = tileSize;

        int dh = (int) Math.round(((double) frameH / Math.max(1, frameW)) * dw);
        int dx = centerX - dw / 2;
        int dy = centerY - dh / 2;

        // Calculate sprite sheet coordinates
        int cols = Math.max(1, framesPerRow);
        int fi = Math.max(0, frameIndex % cols);

        int sx1 = fi * frameW;
        int sy1 = dirRow * frameH;
        int sx2 = Math.min(sheet.getWidth(), sx1 + frameW);
        int sy2 = Math.min(sheet.getHeight(), sy1 + frameH);

        if (facingLeft) {
            // Flip horizontally by swapping destination x coordinates
            g.drawImage(sheet, dx + dw, dy, dx, dy + dh, sx1, sy1, sx2, sy2, null);
        } else {
            // Draw sprite normally
            g.drawImage(sheet, dx, dy, dx + dw, dy + dh, sx1, sy1, sx2, sy2, null);
        }
    }
}