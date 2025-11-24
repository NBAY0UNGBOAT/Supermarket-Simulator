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
        loadSheet(spritePath);
        long now = System.currentTimeMillis();
        lastMoveTime = now;
        lastFrameAdvance = now;
        lastUpdateTime = now;
    }

    // Set visual scale (1.0 = tile size)
    public void setScale(double s) { if (s > 0) this.scale = s; }

    // Make player face a direction without starting walking animation
    // dRow/dCol same semantics used by TileGrid: down=+1 row, up=-1 row, right=+1 col, left=-1 col
    public void face(int dRow, int dCol) {
        facingLeft = false;
        if (dRow > 0) { dirRow = 1; }       // down
        else if (dRow < 0) { dirRow = 2; }  // up
        else if (dCol > 0) { dirRow = 0; }  // right
        else if (dCol < 0) { dirRow = 0; facingLeft = true; } // left uses right-row flipped
        moving = false;
        frameIndex = 0;
    }

    // Called when a successful move starts (animation + direction). dRow/dCol same as face(...)
    public void startWalking(int dRow, int dCol) {
        facingLeft = false;
        if (dRow > 0) { dirRow = 1; }
        else if (dRow < 0) { dirRow = 2; }
        else if (dCol > 0) { dirRow = 0; }
        else if (dCol < 0) { dirRow = 0; facingLeft = true; }
        moving = true;
        lastMoveTime = System.currentTimeMillis();
        lastUpdateTime = lastMoveTime;
    }

    public void stopWalking() { moving = false; frameIndex = 0; }

    public void setPosition(int px, int py) { x = px; y = py; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setSpeed(int sp) { if (sp > 0) speed = sp; }
    public int getSpeed() { return speed; }

    // Balance (money) methods
    public double getBalance() { return balance; }
    public void setBalance(double b) { this.balance = b; }
    public void addBalance(double amount) { this.balance += amount; }
    public void subtractBalance(double amount) { this.balance -= amount; }
    
    // Bank balance methods
    public double getBankBalance() { return bankBalance; }
    public void setBankBalance(double b) { this.bankBalance = b; }
    public void subtractFromBankBalance(double amount) { this.bankBalance -= amount; }

    // Load sprite sheet: tries classpath resource then file system.
    // Assumes sheet layout: 3 rows x 8 columns by default (per your run.png info).
    private void loadSheet(String path) {
        try {
            InputStream is = Player.class.getResourceAsStream(path);
            if (is != null) {
                sheet = ImageIO.read(is);
            } else {
                sheet = ImageIO.read(new File(path));
            }

            if (sheet != null) {
                // expected layout: 3 rows x 8 cols
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
    public void update(long now) {
        if (sheet == null) {
            lastUpdateTime = now;
            return;
        }

        long elapsed = Math.max(0L, now - lastUpdateTime);

        if (moving) {
            // scale movement to elapsed milliseconds (approx 60 fps baseline)
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
            if (now - lastFrameAdvance >= frameTimeMs) {
                frameIndex = (frameIndex + 1) % Math.max(1, framesPerRow);
                lastFrameAdvance = now;
            }
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
    public void draw(Graphics2D g, int centerX, int centerY, int tileSize) {
        if (sheet == null) {
            int pad = Math.max(2, tileSize / 6);
            g.setColor(new java.awt.Color(220, 40, 40));
            g.fillOval(centerX - (tileSize - pad)/2, centerY - (tileSize - pad)/2, tileSize - pad, tileSize - pad);
            return;
        }

        int dw = (int) Math.round(tileSize * scale);
        int maxDw = tileSize * 4;
        if (dw > maxDw) dw = maxDw;
        if (dw < 1) dw = tileSize;

        int dh = (int) Math.round(((double) frameH / Math.max(1, frameW)) * dw);
        int dx = centerX - dw / 2;
        int dy = centerY - dh / 2;

        int cols = Math.max(1, framesPerRow);
        int fi = Math.max(0, frameIndex % cols);

        int sx1 = fi * frameW;
        int sy1 = dirRow * frameH;
        int sx2 = Math.min(sheet.getWidth(), sx1 + frameW);
        int sy2 = Math.min(sheet.getHeight(), sy1 + frameH);

        if (facingLeft) {
            // flip horizontally by swapping destination x coords
            g.drawImage(sheet, dx + dw, dy, dx, dy + dh, sx1, sy1, sx2, sy2, null);
        } else {
            g.drawImage(sheet, dx, dy, dx + dw, dy + dh, sx1, sy1, sx2, sy2, null);
        }
    }
}