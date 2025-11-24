public class TileGridInitializer {
    private static final int SIZE = 22;

    public static void initFloor0(TileType[][] grid) {
        // Initialize all tiles to FLOOR first
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = TileType.FLOOR;
            }
        }

        // Set walls (borders and internal walls)
        for (int i = 0; i < SIZE; i++) {
            grid[0][i] = TileType.WALL;
            grid[SIZE - 1][i] = TileType.WALL;
            grid[i][0] = TileType.WALL;
            grid[i][SIZE - 1] = TileType.WALL;
        }

        // Internal walls at rows 17-18, cols 10-11
        for (int r = 17; r <= 18; r++) {
            grid[r][10] = TileType.WALL;
            grid[r][11] = TileType.WALL;
        }

        // Wall column at row 18
        int[] wallCols = {1, 3, 5, 7, 14, 16, 18, 20};
        for (int col : wallCols) {
            grid[18][col] = TileType.WALL;
        }

        // Refrigerator at row 1 (except cols 7 and 14)
        for (int c = 1; c < SIZE - 1; c++) {
            if (c != 7 && c != 14) {
                grid[1][c] = TileType.CHILLED;
            }
        }

        // Shelves: rows 4-7 and 10-13, cols 2-3, 6-7, 14-15, 18-19
        int[] shelfRows = {4, 5, 6, 7, 10, 11, 12, 13};
        int[] shelfCols = {2, 3, 6, 7, 14, 15, 18, 19};
        for (int r : shelfRows) {
            for (int c : shelfCols) {
                grid[r][c] = TileType.SHELF;
            }
        }

        // Tables: rows 4-7 and 10-13, cols 10-11
        int[] tableCols = {10, 11};
        for (int r : shelfRows) {
            for (int c : tableCols) {
                grid[r][c] = TileType.TABLE;
            }
        }

        // Ladders at row 15, cols 1 and 20
        grid[15][1] = TileType.STAIRS_UP;
        grid[15][20] = TileType.STAIRS_UP;

        // Cashier (Counter) at row 18, cols 2, 4, 6, 8, 13, 15, 17, 19
        int[] counterCols = {2, 4, 6, 8, 13, 15, 17, 19};
        for (int c : counterCols) {
            grid[18][c] = TileType.CASHIER;
        }

        // Search at row 15, cols 8 and 13
        grid[15][8] = TileType.SEARCH;
        grid[15][13] = TileType.SEARCH;

        // Basket at row 20, col 1
        grid[20][1] = TileType.BASKET;

        // Cart at row 20, col 20
        grid[20][20] = TileType.CART;

        // Entrance/Exit at row 21, col 10 (exit) and col 11 (entrance/spawn)
        grid[21][10] = TileType.EXIT;
        grid[21][11] = TileType.DOOR;
    }

    public static void initFloor1(TileType[][] grid) {
        // Initialize all tiles to FLOOR first
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = TileType.FLOOR;
            }
        }

        // Set walls (borders)
        for (int i = 0; i < SIZE; i++) {
            grid[0][i] = TileType.WALL;
            grid[SIZE - 1][i] = TileType.WALL;
            grid[i][0] = TileType.WALL;
            grid[i][SIZE - 1] = TileType.WALL;
        }

        // Row 1: Refrigerators, Basket, and Cart
        // Refrigerator at row 1, columns 3-6
        for (int c = 3; c <= 6; c++) {
            grid[1][c] = TileType.FRIDGE;
        }
        // Refrigerator at row 1, columns 9-12
        for (int c = 9; c <= 12; c++) {
            grid[1][c] = TileType.FRIDGE;
        }
        // Refrigerator at row 1, columns 15-18
        for (int c = 15; c <= 18; c++) {
            grid[1][c] = TileType.FRIDGE;
        }
        // Basket at row 1, column 1
        grid[1][1] = TileType.BASKET;
        // Cart at row 1, column 20
        grid[1][20] = TileType.CART;

        // Shelves
        // Row 4-7, column 2
        for (int r = 4; r <= 7; r++) {
            grid[r][2] = TileType.SHELF;
        }
        // Row 4-7, column 3
        for (int r = 4; r <= 7; r++) {
            grid[r][3] = TileType.SHELF;
        }
        // Row 10-13, column 3
        for (int r = 10; r <= 13; r++) {
            grid[r][3] = TileType.SHELF;
        }
        // Row 10-13, column 2
        for (int r = 10; r <= 13; r++) {
            grid[r][2] = TileType.SHELF;
        }
        // Row 4-7, column 6
        for (int r = 4; r <= 7; r++) {
            grid[r][6] = TileType.SHELF;
        }
        // Row 10-13, column 6
        for (int r = 10; r <= 13; r++) {
            grid[r][6] = TileType.SHELF;
        }
        // Row 4-7, column 7
        for (int r = 4; r <= 7; r++) {
            grid[r][7] = TileType.SHELF;
        }
        // Row 10-13, column 7
        for (int r = 10; r <= 13; r++) {
            grid[r][7] = TileType.SHELF;
        }
        // Row 4-7, column 14
        for (int r = 4; r <= 7; r++) {
            grid[r][14] = TileType.SHELF;
        }
        // Row 10-13, column 14
        for (int r = 10; r <= 13; r++) {
            grid[r][14] = TileType.SHELF;
        }
        // Row 4-7, column 15
        for (int r = 4; r <= 7; r++) {
            grid[r][15] = TileType.SHELF;
        }
        // Row 10-13, column 15
        for (int r = 10; r <= 13; r++) {
            grid[r][15] = TileType.SHELF;
        }
        // Row 4-7, column 18
        for (int r = 4; r <= 7; r++) {
            grid[r][18] = TileType.SHELF;
        }
        // Row 10-13, column 18
        for (int r = 10; r <= 13; r++) {
            grid[r][18] = TileType.SHELF;
        }
        // Row 4-7, column 19
        for (int r = 4; r <= 7; r++) {
            grid[r][19] = TileType.SHELF;
        }
        // Row 10-13, column 19
        for (int r = 10; r <= 13; r++) {
            grid[r][19] = TileType.SHELF;
        }

        // Tables
        // Row 4-7, column 10
        for (int r = 4; r <= 7; r++) {
            grid[r][10] = TileType.TABLE;
        }
        // Row 10-13, column 10
        for (int r = 10; r <= 13; r++) {
            grid[r][10] = TileType.TABLE;
        }
        // Row 4-7, column 11
        for (int r = 4; r <= 7; r++) {
            grid[r][11] = TileType.TABLE;
        }
        // Row 10-13, column 11
        for (int r = 10; r <= 13; r++) {
            grid[r][11] = TileType.TABLE;
        }
        // Row 20, columns 3-7
        for (int c = 3; c <= 7; c++) {
            grid[20][c] = TileType.TABLE;
        }
        // Row 20, columns 9-12
        for (int c = 9; c <= 12; c++) {
            grid[20][c] = TileType.TABLE;
        }
        // Row 20, columns 14-18
        for (int c = 14; c <= 18; c++) {
            grid[20][c] = TileType.TABLE;
        }

        // Search kiosks
        // Row 20, column 1
        grid[20][1] = TileType.SEARCH;
        // Row 20, column 20
        grid[20][20] = TileType.SEARCH;

        // ATM machines at row 16, cols 1 and 20
        grid[16][1] = TileType.ATM;
        grid[16][20] = TileType.ATM;

        // Walls
        // Row 16-17, columns 4-5
        for (int r = 16; r <= 17; r++) {
            for (int c = 4; c <= 5; c++) {
                grid[r][c] = TileType.WALL;
            }
        }
        // Row 16-17, columns 10-11
        for (int r = 16; r <= 17; r++) {
            for (int c = 10; c <= 11; c++) {
                grid[r][c] = TileType.WALL;
            }
        }
        // Row 16-17, columns 16-17
        for (int r = 16; r <= 17; r++) {
            for (int c = 16; c <= 17; c++) {
                grid[r][c] = TileType.WALL;
            }
        }

        // Stairs DOWN at row 15 (connecting to Floor 1)
        grid[15][1] = TileType.STAIRS_DOWN;
        grid[15][20] = TileType.STAIRS_DOWN;
    }
}
