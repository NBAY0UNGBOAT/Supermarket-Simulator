

public class Tile {
	
	private String type;
    private String symbol;
	private String color;
    private boolean walkable;

    private Table table;
    private Ref ref;
    private Shelf shelf;

    public Tile(String type, boolean walkable, String symbol, String color) {
        this.type = type;
        this.walkable = walkable;
        this.symbol = symbol;
		this.color = color;
        if (type.equals("table")) {
            this.table = new Table();
        } else if (type.equals("ref")) {
            this.ref = new Ref();
        } else if (type.equals("shelf")) {
            this.shelf = new Shelf();
        }
    }

    public String getType() {
        return type;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
        this.color = color;
    } 

	public String getCombinedContent() {
		return color + symbol + Colors.RESET;
	}

    public Table getTable() { 
        return table; 
    }

    public Ref getRef() { 
        return ref; 
    }

    public Shelf getShelf() { 
        return shelf; 
    }

}