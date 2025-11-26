package Model;

import Controller.*;
import View.*;


public class Tile {
	
	private String type;
    private String symbol;
	private String color;
    private boolean walkable;

    private Table table;
    private Ref ref;
    private Shelf shelf;

    /**
     * Constructs a new Tile with the specified type, walkability, symbol and color.
     *
     * @param type the tile type (e.g. "wall", "empty", "table", "shelf")
     * @param walkable whether the tile is walkable
     * @param symbol the display symbol for this tile
     * @param color the ANSI color code for rendering
     */
    public Tile(String type, boolean walkable, String symbol, String color) {
        // Initialize tile with properties
        this.type = type;
        this.walkable = walkable;
        this.symbol = symbol;
		this.color = color;
        // Create table/ref/shelf if applicable
        if (type.equals("table")) {
            // Create produce table
            this.table = new Table();
        } else if (type.equals("ref")) {
            // Create refrigerated unit
            this.ref = new Ref();
        } else if (type.equals("shelf")) {
            // Create product shelf
            this.shelf = new Shelf();
        }
    }

    /**
     * Returns the tile type.
     *
     * @return the type string
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if this tile is walkable.
     *
     * @return true if the tile can be walked on, false otherwise
     */
    public boolean isWalkable() {
        return walkable;
    }

    /**
     * Returns the symbol representing this tile.
     *
     * @return the display symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol for this tile.
     *
     * @param symbol the new display symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

	/**
	 * Returns the ANSI color code for this tile.
	 *
	 * @return the color string
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the ANSI color code for this tile.
	 *
	 * @param color the new color code
	 */
	public void setColor(String color) {
        this.color = color;
    } 

	/**
	 * Returns the combined colored symbol for console display.
	 *
	 * @return colored symbol with reset code
	 */
	public String getCombinedContent() {
		return color + symbol + Colors.RESET;
	}

    /**
     * Returns the Table object if this tile contains one.
     *
     * @return the Table, or null if not applicable
     */
    public Table getTable() { 
        return table; 
    }

    /**
     * Returns the Ref (refrigerated) object if this tile contains one.
     *
     * @return the Ref, or null if not applicable
     */
    public Ref getRef() { 
        return ref; 
    }

    /**
     * Returns the Shelf object if this tile contains one.
     *
     * @return the Shelf, or null if not applicable
     */
    public Shelf getShelf() { 
        return shelf; 
    }

}