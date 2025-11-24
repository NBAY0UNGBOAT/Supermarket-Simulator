import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TileImageLoader {
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

    private static final String BASE_PATH = "Graphics/Tileset/";

    public TileImageLoader() {
        loadAllImages();
    }

    private void loadAllImages() {
        loadImage("Floor.png", "floor", img -> floorImage = img);
        loadImage("SecretFloor.png", "secretFloor", img -> secretFloorImage = img);
        loadImage("Table.png", "table", img -> tableImage = img);
        loadImage("Ref.png", "fridge", img -> fridgeImage = img);
        loadImage("Chilled.png", "chilled", img -> chilledImage = img);
        loadImage("ShelfLeft.png", "shelf", img -> shelfImage = img);
        loadImage("Stairs.png", "stairs", img -> stairsImage = img);
        loadImage("Search.png", "search", img -> searchImage = img);
        loadImage("Basket.png", "basket", img -> basketImage = img);
        loadImage("Cart.png", "cart", img -> cartImage = img);
        loadImage("Cashier.png", "cashier", img -> cashierImage = img);
        loadImage("ATM.png", "atm", img -> atmImage = img);
    }

    private void loadImage(String filename, String name, java.util.function.Consumer<BufferedImage> setter) {
        try {
            BufferedImage img = ImageIO.read(new File(BASE_PATH + filename));
            setter.accept(img);
        } catch (IOException ex) {
            System.err.println("Failed to load " + name + " image: " + ex.getMessage());
        }
    }

    public BufferedImage getFloorImage() {
        return floorImage;
    }

    public BufferedImage getSecretFloorImage() {
        return secretFloorImage;
    }

    public BufferedImage getTableImage() {
        return tableImage;
    }

    public BufferedImage getFridgeImage() {
        return fridgeImage;
    }

    public BufferedImage getChilledImage() {
        return chilledImage;
    }

    public BufferedImage getShelfImage() {
        return shelfImage;
    }

    public BufferedImage getStairsImage() {
        return stairsImage;
    }

    public BufferedImage getSearchImage() {
        return searchImage;
    }

    public BufferedImage getBasketImage() {
        return basketImage;
    }

    public BufferedImage getCartImage() {
        return cartImage;
    }

    public BufferedImage getCashierImage() {
        return cashierImage;
    }

    public BufferedImage getAtmImage() {
        return atmImage;
    }
}
