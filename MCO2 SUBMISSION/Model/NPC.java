package Model;

import Controller.*;
import View.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;

public class NPC {
    private BufferedImage spriteSheet;
    private BufferedImage staticFrame;
    private int frameW = 1;
    private int frameH = 1;
    private int framesPerRow = 1;
    private int totalRows = 1;
    private double scale = 2.5;
    private int x = 0;
    private int y = 0;
    private String name;
    private int appearFloor = 0;  // Which floor this NPC appears on (0 for Floor 1, 1 for Floor 2)

    /**
     * Create an NPC with a static frame from a sprite sheet
     * @param spritePath Path to the sprite sheet
     * @param frameRow Row index of the frame (0-based)
     * @param frameCol Column index of the frame (0-based)
     * @param framesPerRow Total frames per row in the sprite sheet
     * @param totalRows Total rows in the sprite sheet
     * @param name NPC name for identification
     */
    public NPC(String spritePath, int frameRow, int frameCol, int framesPerRow, int totalRows, String name) {
        // Initialize NPC with sprite sheet
        this.name = name;
        this.framesPerRow = framesPerRow;
        this.totalRows = totalRows;
        // Load sprite sheet from file
        loadSpriteSheet(spritePath);
        // Extract specific frame for static display
        if (spriteSheet != null) {
            extractFrame(frameRow, frameCol);
        }
    }

    /**
     * Load the sprite sheet image
     */
    private void loadSpriteSheet(String path) {
        try {
            // Try loading from classpath first
            InputStream is = NPC.class.getResourceAsStream(path);
            if (is != null) {
                spriteSheet = ImageIO.read(is);
            } else {
                // Fall back to file system
                spriteSheet = ImageIO.read(new File(path));
            }

            if (spriteSheet != null) {
                // Auto-detect frame dimensions based on framesPerRow and totalRows
                frameW = spriteSheet.getWidth() / framesPerRow;
                frameH = spriteSheet.getHeight() / totalRows;
            }
        } catch (IOException e) {
            System.err.println("NPC: failed to load sprite sheet: " + path + " -> " + e.getMessage());
        }
    }

    /**
     * Extract a single frame from the sprite sheet
     */
    private void extractFrame(int frameRow, int frameCol) {
        // Extract frame data from sprite sheet
        if (spriteSheet == null) return;

        // Calculate source coordinates
        int sx = frameCol * frameW;
        int sy = frameRow * frameH;
        // Calculate source dimensions
        int sw = Math.min(frameW, spriteSheet.getWidth() - sx);
        int sh = Math.min(frameH, spriteSheet.getHeight() - sy);

        // Create subimage if coordinates are valid
        if (sx >= 0 && sy >= 0 && sw > 0 && sh > 0) {
            staticFrame = spriteSheet.getSubimage(sx, sy, sw, sh);
        }
    }

    /**
     * Set position in grid coordinates (row, col will be converted to pixels)
     */
    public void setGridPosition(int gridRow, int gridCol, int cellSize) {
        this.x = gridCol * cellSize + cellSize / 2;
        this.y = gridRow * cellSize + cellSize / 2;
    }

    /**
     * Set pixel position directly
     */
    public void setPixelPosition(int pixelX, int pixelY) {
        this.x = pixelX;
        this.y = pixelY;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
    public int getAppearFloor() { return appearFloor; }
    public void setAppearFloor(int floor) { this.appearFloor = floor; }

    public void setScale(double s) {
        if (s > 0) this.scale = s;
    }

    /**
     * Draw the NPC at its current position
     * Anchored at the bottom of the sprite (feet position) to preserve depth perception
     */
    public void draw(Graphics2D g, int tileSize) {
        if (staticFrame == null) {
            // Fallback: draw a colored circle
            int pad = Math.max(2, tileSize / 6);
            g.setColor(new java.awt.Color(100, 150, 200));
            g.fillOval(x - (tileSize - pad) / 2, y - (tileSize - pad) / 2, tileSize - pad, tileSize - pad);
            return;
        }

        int dw = (int) Math.round(tileSize * scale);
        int maxDw = tileSize * 4;
        if (dw > maxDw) dw = maxDw;
        if (dw < 1) dw = tileSize;

        int dh = (int) Math.round(((double) staticFrame.getHeight() / staticFrame.getWidth()) * dw);
        int dx = x - dw / 2;
        int dy = y - (dh * 3 / 4);  // Anchor lower - feet at 3/4 down instead of all the way down

        g.drawImage(staticFrame, dx, dy, dw, dh, null);
    }

    /**
     * Alternative: Draw centered at provided coordinates
     * Anchored at the bottom of the sprite (feet position) to preserve depth perception
     */
    public void draw(Graphics2D g, int centerX, int centerY, int tileSize) {
        if (staticFrame == null) {
            int pad = Math.max(2, tileSize / 6);
            g.setColor(new java.awt.Color(100, 150, 200));
            g.fillOval(centerX - (tileSize - pad) / 2, centerY - (tileSize - pad) / 2, tileSize - pad, tileSize - pad);
            return;
        }

        int dw = (int) Math.round(tileSize * scale);
        int maxDw = tileSize * 4;
        if (dw > maxDw) dw = maxDw;
        if (dw < 1) dw = tileSize;

        int dh = (int) Math.round(((double) staticFrame.getHeight() / staticFrame.getWidth()) * dw);
        int dx = centerX - dw / 2;
        int dy = centerY - (dh * 3 / 4);  // Anchor lower - feet at 3/4 down instead of all the way down

        g.drawImage(staticFrame, dx, dy, dw, dh, null);
    }
}
