import java.awt.*;

public enum TileType {
    WALL(new Color(250, 245, 230)),
    FLOOR(Color.WHITE),
    SECRETFLOOR(Color.WHITE),
    TABLE(new Color(80, 160, 60)),
    FRIDGE(new Color(30, 90, 200)),
    CHILLED(new Color(150, 210, 255)),
    SHELF(new Color(245, 175, 35)),
    STAIRS_UP(new Color(200, 200, 200)),
    STAIRS_DOWN(new Color(200, 200, 200)),
    SEARCH(new Color(200, 150, 100)),
    BASKET(new Color(200, 160, 120)),
    CART(new Color(160, 120, 200)),
    CASHIER(new Color(180, 140, 200)),
    DOOR(new Color(180, 180, 250)),
    ATM(new Color(100, 100, 100)),
    EXIT(new Color(180, 180, 250)),
    BLACK_TILE(new Color(20, 20, 20));

    public final Color color;

    TileType(Color c) {
        this.color = c;
    }
}
