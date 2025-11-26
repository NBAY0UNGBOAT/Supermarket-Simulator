package Model;

import Controller.*;
import View.*;


public class Board {
	private Tile[][] tiles;

	private final int size = 22;
	private int currentRow;
	private int currentCol;
	private int currentVision;
	
	/**
	 * Constructs a new Board with the specified initial player position and vision range.
	 *
	 * @param currentRow the initial player row position
	 * @param currentCol the initial player column position
	 * @param currentVision the vision range for player sight
	 */
	public Board(int currentRow, int currentCol, int currentVision) {
		// Initialize board with player position
		this.tiles = new Tile[size][size];
		this.currentRow = currentRow;
		this.currentCol = currentCol;
		this.currentVision = currentVision;
		// Create all floor tiles
		initializeTiles();
	}
	
	/**
	 * Returns the 2D array of tiles representing the game board.
	 *
	 * @return the tile grid (22x22)
	 */
	public Tile[][] getTiles() {
		return this.tiles;
	}

	/**
	 * Initializes all tiles on the game board.
	 */
	private void initializeTiles() {
		int row, col;

        // Initialize 22x22 floor grid with different tile types
        for (row = 0; row < size; row++) {
            for (col = 0; col < size; col++) {
                // Define wall positions (borders and structural walls)
                boolean isWall = 
                    (row == 0 || row == size - 1 || col == 0 || col == size - 1) ||
                    (row == 17 || row == 18) && (col == 10 || col == 11) ||
                    (row == 18) && 
                    (col == 1 || col == 3 || col == 5 || col == 7 || 
                    col == 14 || col == 16 || col == 18 || col == 20);
                // Exit/entrance door
                boolean isDoor = (row == 21 && col == 10) || (row == 21 && col == 11);
                // Refrigerated unit positions
                boolean isRef = (row == 1) && (col != 7 && col != 14);
                // Shelf positions
                boolean isShelf = 
                    (row == 4 || row == 5 || row == 6 || row == 7 || 
                    row == 10 || row == 11 || row == 12 || row == 13) && 
                    (col == 2 || col == 3 || col == 6 || col == 7 || 
                    col == 14 || col == 15 || col == 18 || col == 19);
                // Table positions (produce)
                boolean isTable = 
                    (row == 4 || row == 5 || row == 6 || row == 7 || 
                    row == 10 || row == 11 || row == 12 || row == 13) && 
                    (col == 10 || col == 11);
                // Staircase positions for floor transitions
                boolean isLadder = (row == 15) && (col == 1 || col == 20);
                // Search kiosk positions
                boolean isSearch = (row == 15) && (col == 8 || col == 13);
                // Checkout counter positions
                boolean isCounter = (row == 18) && 
                    (col == 2 || col == 4 || col == 6 || col == 8 || 
                    col == 13 || col == 15 || col == 17 || col == 19);
                // Equipment area - basket
                boolean isBasket = (row == 20 && col == 1);
                // Equipment area - cart
                boolean isCart = (row == 20 && col == 20);

				// Create tile at current position
				if (row == currentRow && col == currentCol) {
					// Player tile: UI/sprite handles facing; show neutral marker
					tiles[row][col] = new Tile("player", true, "[PL]", Colors.CYAN);
                } else if (isDoor) {
					// Door tile
					tiles[row][col] = new Tile("door", false, "[  ]", Colors.VIOLET);
                } else if (isWall) {
					// Wall tile - impassable
					tiles[row][col] = new Tile("wall", false, "[##]", Colors.RED);
                } else if (isRef) {
					// Refrigerated display unit
					tiles[row][col] = new Tile("ref", false, "[**]", Colors.BLUE);
                } else if (isShelf) {
					// Shelf display unit
					tiles[row][col] = new Tile("shelf", false, "[**]", Colors.YELLOW);
                } else if (isTable) {
					// Produce table unit
					tiles[row][col] = new Tile("table", false, "[**]", Colors.GREEN);
                } else if (isLadder) {
					// Staircase to next floor
					tiles[row][col] = new Tile("ladder", false, "[//]", Colors.VIOLET);
                } else if (isSearch) {
					// Search kiosk
					tiles[row][col] = new Tile("search", false, "[*i]", Colors.VIOLET);
                } else if (isCounter) {
					// Checkout counter
					tiles[row][col] = new Tile("counter", false, "[$$]", Colors.VIOLET);
                } else if (isBasket) {
					// Equipment basket selector
					tiles[row][col] = new Tile("basket", false, "[BT]", Colors.VIOLET);
                } else if (isCart) {
					// Equipment cart selector
					tiles[row][col] = new Tile("cart", false, "[CT]", Colors.VIOLET);
                } else {
					// Empty walkable floor
					tiles[row][col] = new Tile("empty", true, "[  ]", Colors.WHITE);
                }
            }
        }
	}

	/**
	 * Displays the board to the console with ANSI colors.
	 */
	public void display() {
		// Display board to console
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				// Print each tile
				Tile tile = tiles[row][col];
				System.out.print(tile.getCombinedContent());
				/**
				 * Uncomment this to check what's inside the tables.
				 */
				// if (tile.getType().equals("table")) {
				// 	System.out.printf("Table at (%d, %d):%n", row, col);
				// 	for (Item item : tile.getTable().getItems()) {
				// 		System.out.println("  - " + item.toString());
				// 	}
				// }
			}
			System.out.println();
		}
	}

	/**
	 * Updates the player position on the board.
	 *
	 * @param row the new player row position
	 * @param col the new player column position
	 * @param vision the vision character (unused in tile representation)
	 */
	public void setPlayer(int row, int col, char vision) {
		int i, j;

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                if (tiles[i][j].getType().equals("player")) {
                    tiles[i][j] = new Tile("empty", true, "[  ]", Colors.WHITE);
                }
            }
        }

		// ignore the vision char in tile representation; UI will display sprite facing
		tiles[row][col] = new Tile("player", true, "[PL]", Colors.CYAN);
		this.currentRow = row;
		this.currentCol = col;
    }

	/**
	 * Returns the current player row position.
	 *
	 * @return the player row
	 */
	public int getPlayerRow() { return currentRow; }

	/**
	 * Returns the current player column position.
	 *
	 * @return the player column
	 */
	public int getPlayerCol() { return currentCol; }

	/**
	 * Attempts to move the player in the specified direction.
	 *
	 * @param dr the row direction (-1, 0, or 1)
	 * @param dc the column direction (-1, 0, or 1)
	 * @return true if the move was successful, false if blocked
	 */
	public boolean tryMove(int dr, int dc) {
		int nr = currentRow + dr;
		int nc = currentCol + dc;
		if (nr < 0 || nr >= size || nc < 0 || nc >= size) return false;

		boolean isWall = 
			(nr == 0 || nr == size - 1 || nc == 0 || nc == size - 1) ||
			(nr == 17 || nr == 18) && (nc == 10 || nc == 11) ||
			(nr == 18) && 
			(nc == 1 || nc == 3 || nc == 5 || nc == 7 || 
			nc == 14 || nc == 16 || nc == 18 || nc == 20);
		boolean isDoor = (nr == 21 && nc == 10) || (nr == 21 && nc == 11);
		boolean isRef = (nr == 1) && (nc != 7 && nc != 14);
		boolean isShelf = 
			(nr == 4 || nr == 5 || nr == 6 || nr == 7 || 
			nr == 10 || nr == 11 || nr == 12 || nr == 13) && 
			(nc == 2 || nc == 3 || nc == 6 || nc == 7 || 
			nc == 14 || nc == 15 || nc == 18 || nc == 19);
		boolean isTable = 
			(nr == 4 || nr == 5 || nr == 6 || nr == 7 || 
			nr == 10 || nr == 11 || nr == 12 || nr == 13) && 
			(nc == 10 || nc == 11);
		boolean isLadder = (nr == 15) && (nc == 1 || nc == 20); 
		boolean isSearch = (nr == 15) && (nc == 8 || nc == 13); 
		boolean isCounter = (nr == 18) && 
			(nc == 2 || nc == 4 || nc == 6 || nc == 8 || 
			nc == 13 || nc == 15 || nc == 17 || nc == 19);
		boolean isBasket = (nr == 20 && nc == 1);
		boolean isCart = (nr == 20 && nc == 20);

		boolean isNextEmpty = !isWall && !isRef && !isShelf && !isTable 
			&& !isLadder && !isSearch && !isCounter && !isBasket && !isCart && !isDoor;

		if (isNextEmpty) {
			setPlayer(nr, nc, ' ');
			return true;
		}

		return false;
	}
}