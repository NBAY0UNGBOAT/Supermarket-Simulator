package View;

import Controller.*;
import Model.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Manages loading and caching of tile graphics for the game.
 * Loads all supermarket tile images from the Graphics/Tileset/ directory.
 */
public class TileImageLoader {
    // Game tile images
    private BufferedImage floorImage;
    private BufferedImage secretFloorImage;
    private BufferedImage tableImage;
    private BufferedImage fridgeImage;
    private BufferedImage chilledImage;
    private BufferedImage shelfImage;
    private BufferedImage stairsImage;
    private BufferedImage searchImage;
    private BufferedImage basketImage;
    private BufferedImage cartImage;
    private BufferedImage cashierImage;
    private BufferedImage atmImage;

    // Base path to graphics folder
    private static final String BASE_PATH = "Graphics/Tileset/";

    /**
     * Constructs a new TileImageLoader and loads all game graphics.
     */
    public TileImageLoader() {
        // Load all game graphics
        loadAllImages();
    }

    // Load all tile images from disk
    private void loadAllImages() {
        // Load floor tiles
        loadImage("Floor.png", "floor", img -> floorImage = img);
        loadImage("SecretFloor.png", "secretFloor", img -> secretFloorImage = img);
        // Load display furniture
        loadImage("Table.png", "table", img -> tableImage = img);
        loadImage("Ref.png", "fridge", img -> fridgeImage = img);
        loadImage("Chilled.png", "chilled", img -> chilledImage = img);
        loadImage("ShelfLeft.png", "shelf", img -> shelfImage = img);
        // Load interactive elements
        loadImage("Stairs.png", "stairs", img -> stairsImage = img);
        loadImage("Search.png", "search", img -> searchImage = img);
        loadImage("Basket.png", "basket", img -> basketImage = img);
        loadImage("Cart.png", "cart", img -> cartImage = img);
        loadImage("Cashier.png", "cashier", img -> cashierImage = img);
        loadImage("ATM.png", "atm", img -> atmImage = img);
    }

    // Load individual image from file
    private void loadImage(String filename, String name, java.util.function.Consumer<BufferedImage> setter) {
        try {
            // Read image from file system
            BufferedImage img = ImageIO.read(new File(BASE_PATH + filename));
            setter.accept(img);
        } catch (IOException ex) {
            System.err.println("Failed to load " + name + " image: " + ex.getMessage());
        }
    }

    /**
     * Gets the floor tile image.
     *
     * @return the floor image
     */
    public BufferedImage getFloorImage() {
        return floorImage;
    }

    /**
     * Gets the secret floor tile image.
     *
     * @return the secret floor image
     */
    public BufferedImage getSecretFloorImage() {
        return secretFloorImage;
    }

    /**
     * Gets the table display image.
     *
     * @return the table image
     */
    public BufferedImage getTableImage() {
        return tableImage;
    }

    /**
     * Gets the refrigerator display image.
     *
     * @return the fridge image
     */
    public BufferedImage getFridgeImage() {
        return fridgeImage;
    }

    /**
     * Gets the chilled display image.
     *
     * @return the chilled image
     */
    public BufferedImage getChilledImage() {
        return chilledImage;
    }

    /**
     * Gets the shelf display image.
     *
     * @return the shelf image
     */
    public BufferedImage getShelfImage() {
        return shelfImage;
    }

    /**
     * Gets the stairs image.
     *
     * @return the stairs image
     */
    public BufferedImage getStairsImage() {
        return stairsImage;
    }

    /**
     * Gets the search kiosk image.
     *
     * @return the search image
     */
    public BufferedImage getSearchImage() {
        return searchImage;
    }

    /**
     * Gets the basket equipment image.
     *
     * @return the basket image
     */
    public BufferedImage getBasketImage() {
        return basketImage;
    }

    /**
     * Gets the shopping cart equipment image.
     *
     * @return the cart image
     */
    public BufferedImage getCartImage() {
        return cartImage;
    }

    /**
     * Gets the cashier/checkout counter image.
     *
     * @return the cashier image
     */
    public BufferedImage getCashierImage() {
        return cashierImage;
    }

    /**
     * Gets the ATM machine image.
     *
     * @return the ATM image
     */
    public BufferedImage getAtmImage() {
        return atmImage;
    }
}
