public class SecretHallwayInitializer {
    private static final int SIZE = 22;

    /**
     * Initialize the secret hallway map with evil boss theme
     * Layout: short 5-tile hallway leading to a large boss room
     */
    public static void initSecretHallway(TileType[][] grid) {
        // Initialize all tiles to WALL first
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = TileType.WALL;
            }
        }

        // Entrance: Player spawns on floor at (18, 11)
        grid[18][11] = TileType.SECRETFLOOR;
        
        // Short hallway: column 11, from row 14 to row 18 (5 tiles going UP)
        for (int r = 14; r <= 18; r++) {
            grid[r][11] = TileType.SECRETFLOOR;
        }
        
        // Boss room: large chamber from rows 3-13, columns 5-17
        for (int r = 3; r <= 13; r++) {
            for (int c = 5; c <= 17; c++) {
                grid[r][c] = TileType.SECRETFLOOR;
            }
        }
        
        // Create wall obstacles in the boss room with all adjacent tiles including diagonals
        // Wall at (4,6) and all 8 adjacent tiles
        for (int r = 3; r <= 5; r++) {
            for (int c = 5; c <= 7; c++) {
                grid[r][c] = TileType.WALL;
            }
        }
        
        // Wall at (4,16) and all 8 adjacent tiles
        for (int r = 3; r <= 5; r++) {
            for (int c = 15; c <= 17; c++) {
                grid[r][c] = TileType.WALL;
            }
        }
        
        // Wall at (12,6) and all 8 adjacent tiles
        for (int r = 11; r <= 13; r++) {
            for (int c = 5; c <= 7; c++) {
                grid[r][c] = TileType.WALL;
            }
        }
        
        // Wall at (12,16) and all 8 adjacent tiles
        for (int r = 11; r <= 13; r++) {
            for (int c = 15; c <= 17; c++) {
                grid[r][c] = TileType.WALL;
            }
        }
        
        // Black tiles for teleportation back to first floor
        grid[8][6] = TileType.BLACK_TILE;
        grid[8][16] = TileType.BLACK_TILE;
        
        // Connection zone from hallway to room (rows 13-14)
        for (int c = 10; c <= 12; c++) {
            grid[13][c] = TileType.SECRETFLOOR;
            grid[14][c] = TileType.SECRETFLOOR;
        }
    }
}

