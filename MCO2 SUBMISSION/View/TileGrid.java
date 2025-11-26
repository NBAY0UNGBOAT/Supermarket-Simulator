package View;

import Controller.*;
import Model.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;

public class TileGrid extends JPanel {
    private static final int SIZE = 22;
    private static final int CELL = 28;

    private TileType[][] gridFloor0;
    private TileType[][] gridFloor1;
    private TileType[][] gridSecretHallway;
    private int currentFloor = 0;  // 0 = Floor 1, 1 = Floor 2, 2 = Secret Hallway
    private int shopperRow = 21, shopperCol = 11;
    private int blackTileRow = -1, blackTileCol = -1;  // Position of black tile ATM

    private JLabel status;
    private Player player;
    private NPC thugger;
    private Timer repaintTimer;
    private Board board = null;

    private TileImageLoader imageLoader;
    private InteractionMenu menu = null;
    private SearchKiosk searchKiosk = null;
    private int facingDr = -1, facingDc = 0;  // Facing up initially
    
    // Waypoint tracking
    private boolean hasWaypoint = false;
    private java.util.List<int[]> waypoints = new java.util.ArrayList<>();
    private java.util.List<int[]> allWaypoints = new java.util.ArrayList<>();  // Keep full list for floor changes
    
    // Return product tracking
    private int currentTileRow = -1;
    private int currentTileCol = -1;
    private java.util.List<String> currentReturnableProducts = new java.util.ArrayList<>();
    private boolean isProcessingReturn = false;

    // Inventory system
    private StoreInventory storeInventory;
    private ShopperInventory inventory;
    private InventoryDisplay inventoryDisplay = null;
    private int receiptScrollOffset = 0;  // Track scroll position for receipt
    private QuantityInputPanel quantityInputPanel = null;
    private EquipmentConfirmationPanel equipmentConfirmationPanel = null;
    private ReturnProductPanel returnProductPanel = null;
    
    // Product selection state
    private String lastSelectedProductId = null;
    private String lastSelectedProductName = null;
    private double lastSelectedProductPrice = 0.0;
    
    // NPC interaction state
    private boolean playerLocked = false;
    private long lockStartTime = 0;
    private static final long LOCK_DURATION = 3000;  // 3 seconds lock animation
    private static final long THUGGER_ABILITY_DURATION = 26000;  // 26 seconds to buy alcohol
    private long thuggerInteractionTime = 0;
    private String dialogueText = null;
    private long dialogueStartTime = 0;
    private boolean isInteractingWithThugger = false;  // Track if currently interacting with Thugger
    private boolean hasInteractedWithThugger = false;  // Track if normal-aged player has interacted with Thugger (gives 50% discount)
    
    // Discount system
    private double discountMultiplier = 1.0;  // 1.0 = no discount, 0.5 = 50% off
    
    // Player profile data
    private String playerUsername = "Player";
    private int playerAge = 18;
    private double initialBalance = 1000.0;  // Track starting balance for restart
    
    // ATM interaction
    private ATMMenu atmMenu = null;
    private BalanceInquiryPanel balanceInquiryPanel = null;
    private ATMWithdrawalPanel atmWithdrawalPanel = null;
    private WithdrawalSuccessPanel withdrawalSuccessPanel = null;
    private int atmState = 0;  // 0=menu, 1=balance inquiry, 2=withdrawal, 3=success
    private double atmSessionBalance = 0.0;  // Persistent balance per ATM session

    /**
     * Constructs a new TileGrid with the given status label.
     * Initializes game grids, loads player and NPC sprites, and sets up keyboard controls.
     *
     * @param status the status label for displaying game information
     */
    public TileGrid(JLabel status) {
        this.status = status;
        this.imageLoader = new TileImageLoader();
        this.storeInventory = new StoreInventory();
        this.inventory = new ShopperInventory(storeInventory);

        // Set preferred window size
        setPreferredSize(new Dimension(SIZE * CELL, SIZE * CELL));
        // Initialize game grids
        initGrids();

        // Load player sprite
        String spriteFile = "Graphics/Player/run.png";
        player = new Player(spriteFile);
        player.setScale(5.0);
        player.face(-1, 0);  // Face up initially

        // Load Thugger NPC (row 0, col 1 from 4-row, 3-column sprite sheet)
        String thuggerFile = "Graphics/Player/Thugger.png";
        thugger = new NPC(thuggerFile, 0, 1, 3, 4, "Thugger");
        thugger.setScale(1.5);
        thugger.setGridPosition(4, 11, CELL);  // Position at secret room row 4, col 11
        thugger.setAppearFloor(2);  // Appear in secret room (floor 2)

        // Update status bar
        updateStatus();
        // Set up keyboard controls
        setupKeyBindings();
        setFocusable(true);
        requestFocusInWindow();

        // Add KeyListener as fallback for X key and number input for ATM
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Handle return product panel keys
                if (returnProductPanel != null) {
                    returnProductPanel.handleKeyPress(e.getKeyCode());
                    return;
                }
                
                // Handle numeric input and backspace for withdrawal panel
                if (atmWithdrawalPanel != null) {
                    atmWithdrawalPanel.handleKeyInput(e);
                    return;
                }
                
                // Handle R key to toggle between buy and return modes
                if ((e.getKeyCode() == KeyEvent.VK_R || e.getKeyChar() == 'r' || e.getKeyChar() == 'R') && menu != null) {
                    System.out.println("DEBUG R KEY: menu exists, inReturnMode=" + menu.isInReturnMode() + ", returnableProducts size=" + currentReturnableProducts.size());
                    for (String prod : currentReturnableProducts) {
                        System.out.println("  - " + prod);
                    }
                    if (!menu.isInReturnMode() && !currentReturnableProducts.isEmpty()) {
                        // Switch to return mode
                        System.out.println("DEBUG: Switching to return mode with " + currentReturnableProducts.size() + " products");
                        menu.setReturnProducts(currentReturnableProducts);
                        menu.validate();
                        menu.repaint();
                        TileGrid.this.validate();
                        TileGrid.this.repaint();
                        return;
                    } else if (menu.isInReturnMode()) {
                        // Switch back to buy mode
                        System.out.println("DEBUG: Switching back to buy mode");
                        menu.exitReturnMode();
                        menu.validate();
                        menu.repaint();
                        TileGrid.this.validate();
                        TileGrid.this.repaint();
                        return;
                    } else {
                        System.out.println("DEBUG R KEY: CONDITIONS NOT MET - inReturnMode=" + menu.isInReturnMode() + ", isEmpty=" + currentReturnableProducts.isEmpty());
                    }
                }
                
                // Check for X key to interact with search kiosk
                System.out.println("Key pressed: " + e.getKeyCode() + " (" + e.getKeyChar() + ") searchKiosk=" + (searchKiosk != null));
                if ((e.getKeyCode() == KeyEvent.VK_X || e.getKeyChar() == 'x' || e.getKeyChar() == 'X') && searchKiosk != null) {
                    System.out.println("X key detected! Calling searchKiosk.handleKeyPress");
                    searchKiosk.handleKeyPress(KeyEvent.VK_X);
                    repaint();
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {
                // Also handle character input for withdrawal
                if (atmWithdrawalPanel != null) {
                    atmWithdrawalPanel.handleCharInput(e);
                }
            }
        });

        // Create timer for animation and repaint loop
        repaintTimer = new Timer(40, e -> {
            if (player != null) player.update(System.currentTimeMillis());
            repaint();
        });
        repaintTimer.start();
    }

    /**
     * Constructs a new TileGrid with the given status label and board.
     * Initializes game state and restores player position from the provided board.
     *
     * @param status the status label for displaying game information
     * @param board the game board containing player position
     */
    public TileGrid(JLabel status, Board board) {
        // Initialize with board parameter
        this(status);
        this.board = board;
        // Restore player position from board
        if (board != null) {
            this.shopperRow = board.getPlayerRow();
            this.shopperCol = board.getPlayerCol();
        }
    }
    
    /**
     * Sets player profile data from registration.
     * Initializes player balance and generates random bank balance.
     *
     * @param username the player's username
     * @param age the player's age
     * @param startingBalance the player's starting balance
     */
    public void setPlayerData(String username, int age, double startingBalance) {
        this.playerUsername = username;
        this.playerAge = age;
        this.initialBalance = startingBalance;  // Store the initial balance for restart
        if (player != null) {
            player.setBalance(startingBalance);
            // Initialize bank balance with random value between 50000-100000
            double bankBalance = 50000 + Math.random() * 50000;
            player.setBankBalance(bankBalance);
        }
        updateStatus();
    }

    /**
     * Check if an NPC is occupying a grid position
     */
    /**
     * Checks if an NPC is currently at the given grid position on the current floor.
     *
     * @param row the row position
     * @param col the column position
     * @return true if an NPC is at the position, false otherwise
     */
    private boolean isNPCAtPosition(int row, int col) {
        if (thugger == null) return false;
        
        // Get NPC's pixel position
        int npcX = thugger.getX();
        int npcY = thugger.getY();
        
        // Convert grid position to center pixel position
        int gridCenterX = col * CELL + CELL / 2;
        int gridCenterY = row * CELL + CELL / 2;
        
        // Check if NPC is within the grid cell (with some tolerance)
        int tolerance = CELL / 3;
        return Math.abs(npcX - gridCenterX) < tolerance && Math.abs(npcY - gridCenterY) < tolerance;
    }

    /**
     * Initializes the game grids for all floors with tile types.
     * Sets up the basic supermarket layout and secret hallway structure.
     */
    private void initGrids() {
        gridFloor0 = new TileType[SIZE][SIZE];
        gridFloor1 = new TileType[SIZE][SIZE];
        gridSecretHallway = new TileType[SIZE][SIZE];

        TileGridInitializer.initFloor0(gridFloor0);
        TileGridInitializer.initFloor1(gridFloor1);
        SecretHallwayInitializer.initSecretHallway(gridSecretHallway);

        TileType[][] currentGrid = getCurrentGrid();
        if (currentGrid[shopperRow][shopperCol] != TileType.FLOOR &&
            currentGrid[shopperRow][shopperCol] != TileType.STAIRS_UP &&
            currentGrid[shopperRow][shopperCol] != TileType.STAIRS_DOWN &&
            currentGrid[shopperRow][shopperCol] != TileType.DOOR) {
            outer:
            for (int r = SIZE - 2; r >= 1; r--) {
                for (int c = 1; c < SIZE - 1; c++) {
                    if (currentGrid[r][c] == TileType.FLOOR) {
                        shopperRow = r;
                        shopperCol = c;
                        break outer;
                    }
                }
            }
        }
    }

    /**
     * Gets the current floor's tile grid.
     *
     * @return the TileType grid for the current floor
     */
    private TileType[][] getCurrentGrid() {
        if (currentFloor == 0) return gridFloor0;
        else if (currentFloor == 1) return gridFloor1;
        else return gridSecretHallway;
    }

    /**
     * Sets up keyboard bindings for player movement and interaction.
     * Maps arrow keys and WASD keys to movement actions.
     */
    private void setupKeyBindings() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("ENTER"), "useStairs");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "closeMenu");
        im.put(KeyStroke.getKeyStroke('V'), "inventory");
        im.put(KeyStroke.getKeyStroke('v'), "inventory");  // Also handle lowercase
        im.put(KeyStroke.getKeyStroke('B'), "receipt");
        im.put(KeyStroke.getKeyStroke('b'), "receipt");  // Also handle lowercase

        am.put("left", moveAction(0, -1));
        am.put("right", moveAction(0, 1));
        am.put("up", moveAction(-1, 0));
        am.put("down", moveAction(1, 0));
        am.put("useStairs", useStairsAction());
        am.put("closeMenu", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (equipmentConfirmationPanel != null) {
                    equipmentConfirmationPanel = null;
                    repaint();
                } else if (quantityInputPanel != null) {
                    quantityInputPanel = null;
                    repaint();
                } else if (inventoryDisplay != null) {
                    inventoryDisplay = null;
                    receiptScrollOffset = 0;  // Reset scroll offset when closing receipt
                    repaint();
                } else if (balanceInquiryPanel != null) {
                    // ESC from balance inquiry - return to menu
                    atmState = 0;
                    atmMenu = new ATMMenu(playerUsername, atmSessionBalance, () -> {
                        atmMenu = null;
                        atmState = 0;
                        repaint();
                    });
                    balanceInquiryPanel = null;
                    repaint();
                } else if (atmWithdrawalPanel != null) {
                    // ESC from withdrawal - return to menu
                    atmState = 0;
                    atmMenu = new ATMMenu(playerUsername, atmSessionBalance, () -> {
                        atmMenu = null;
                        atmState = 0;
                        repaint();
                    });
                    atmWithdrawalPanel = null;
                    repaint();
                } else if (withdrawalSuccessPanel != null) {
                    // ESC from success panel - return to menu
                    atmState = 0;
                    atmMenu = new ATMMenu(playerUsername, atmSessionBalance, () -> {
                        atmMenu = null;
                        atmState = 0;
                        repaint();
                    });
                    withdrawalSuccessPanel = null;
                    repaint();
                } else if (atmMenu != null) {
                    // ESC from menu - exit ATM
                    atmMenu = null;
                    atmState = 0;
                    repaint();
                } else if (menu != null) {
                    // Clear waypoints when opening a menu (player is interacting with a tile)
                    hasWaypoint = false;
                    waypoints.clear();
                    allWaypoints.clear();
                    menu = null;
                    repaint();
                } else if (searchKiosk != null) {
                    // Copy waypoint data with floor info before closing
                    if (searchKiosk.hasActiveWaypoint()) {
                        hasWaypoint = true;
                        allWaypoints = new java.util.ArrayList<>(searchKiosk.getWaypoints());
                        // Filter waypoints to only show current floor
                        waypoints = new java.util.ArrayList<>();
                        for (int[] wp : allWaypoints) {
                            if (wp[0] == currentFloor) {
                                waypoints.add(wp);
                            }
                        }
                    }
                    searchKiosk = null;
                    repaint();
                }
            }
        });
        am.put("toggleView", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("DEBUG: toggleView called, currentFloor before: " + currentFloor);
                currentFloor = 1 - currentFloor;
                System.out.println("DEBUG: currentFloor after toggle: " + currentFloor);
                // Regenerate black tile when entering Floor 2
                if (currentFloor == 1) {
                    System.out.println("DEBUG: About to call regenerateBlackTile()");
                    regenerateBlackTile();
                }
                // Re-filter waypoints when changing floors to only show products on current floor
                if (hasWaypoint && !allWaypoints.isEmpty()) {
                    waypoints = new java.util.ArrayList<>();
                    for (int[] wp : allWaypoints) {
                        if (wp[0] == currentFloor) {
                            waypoints.add(wp);
                        }
                    }
                }
                repaint();
                updateStatus();
            }
        });
        am.put("inventory", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (inventoryDisplay != null) {
                    inventoryDisplay = null;
                } else {
                    inventoryDisplay = new InventoryDisplay(inventory, () -> {
                        inventoryDisplay = null;
                        requestFocusInWindow();
                        repaint();
                    });
                    inventoryDisplay.setSize(getWidth(), getHeight());
                }
                requestFocusInWindow();
                repaint();
            }
        });
        
        am.put("receipt", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String lastReceipt = inventory.getLastReceipt();
                if (lastReceipt == null) {
                    JOptionPane.showMessageDialog(
                        null,
                        "No receipt available. Please checkout first.",
                        "No Receipt",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    if (inventoryDisplay != null) {
                        inventoryDisplay = null;
                    } else {
                        // Create a receipt display (with showReceipt=true)
                        inventoryDisplay = new InventoryDisplay(inventory, () -> {
                            inventoryDisplay = null;
                            requestFocusInWindow();
                            repaint();
                        }, true);  // true = show receipt mode
                        inventoryDisplay.setSize(getWidth(), getHeight());
                    }
                    repaint();
                    // Transfer focus to the inventory display so arrow keys work
                    SwingUtilities.invokeLater(() -> {
                        inventoryDisplay.requestFocusInWindow();
                    });
                }
            }
        });
    }

    private Action moveAction(int dr, int dc) {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // Check if player is locked from NPC interaction
                if (playerLocked) {
                    long elapsed = System.currentTimeMillis() - lockStartTime;
                    if (elapsed < LOCK_DURATION) {
                        return;  // Still locked, can't move
                    } else {
                        playerLocked = false;  // Unlock after 3 seconds
                    }
                }
                
                // Check if Thugger alcohol ability duration has expired
                if (isInteractingWithThugger) {
                    long thuggerElapsed = System.currentTimeMillis() - thuggerInteractionTime;
                    if (thuggerElapsed >= THUGGER_ABILITY_DURATION) {
                        isInteractingWithThugger = false;  // Disable alcohol purchase after 20 seconds
                    }
                }
                
                // If inventory/receipt display is open, handle receipt scrolling
                if (inventoryDisplay != null) {
                    if (dr < 0 || dr > 0) {  // UP or DOWN arrow
                        String lastReceipt = inventory.getLastReceipt();
                        if (lastReceipt != null) {
                            String[] lines = lastReceipt.split("\n");
                            int maxLines = 20;
                            int totalLines = lines.length;
                            int maxScroll = Math.max(0, totalLines - maxLines);
                            
                            if (dr < 0) {  // UP arrow - scroll up
                                receiptScrollOffset = Math.max(0, receiptScrollOffset - 1);
                            } else if (dr > 0) {  // DOWN arrow - scroll down
                                receiptScrollOffset = Math.min(maxScroll, receiptScrollOffset + 1);
                            }
                            inventoryDisplay.setReceiptScrollOffset(receiptScrollOffset);
                            repaint();
                            return;
                        }
                    }
                }
                
                // If menu is open, handle arrow navigation
                if (menu != null) {
                    if (dc < 0) {  // LEFT arrow
                        menu.moveLeft();
                    } else if (dc > 0) {  // RIGHT arrow
                        menu.moveRight();
                    } else if (dr < 0) {  // UP arrow
                        menu.moveUp();
                    } else if (dr > 0) {  // DOWN arrow
                        menu.moveDown();
                    }
                    repaint();
                    return;
                }
                
                // If equipment confirmation is open, don't allow movement
                if (equipmentConfirmationPanel != null) {
                    return;
                }

                // If quantity input is open, handle it with arrow keys
                if (quantityInputPanel != null) {
                    if (dr < 0) {  // UP arrow
                        quantityInputPanel.increaseQuantity();
                    } else if (dr > 0) {  // DOWN arrow
                        quantityInputPanel.decreaseQuantity();
                    } else if (dc < 0) {  // LEFT arrow
                        quantityInputPanel.decreaseQuantity();  // LEFT decreases quantity
                    } else if (dc > 0) {  // RIGHT arrow
                        quantityInputPanel.increaseQuantity();  // RIGHT increases quantity
                    }
                    repaint();
                    return;
                }

                // If search kiosk is open, use arrow keys to navigate
                if (searchKiosk != null) {
                    if (dr != 0) {  // Up or Down arrow
                        searchKiosk.handleKeyPress(dr < 0 ? KeyEvent.VK_UP : KeyEvent.VK_DOWN);
                    }
                    repaint();
                    return;
                }
                
                // If ATM menu is open, use arrow keys to navigate
                if (atmMenu != null) {
                    if (dr < 0) {  // UP arrow
                        atmMenu.moveUp();
                    } else if (dr > 0) {  // DOWN arrow
                        atmMenu.moveDown();
                    }
                    repaint();
                    return;
                }

                if (player != null) player.face(dr, dc);
                facingDr = dr;
                facingDc = dc;

                if (board != null) {
                    boolean moved = board.tryMove(dr, dc);
                    if (moved) {
                        shopperRow = board.getPlayerRow();
                        shopperCol = board.getPlayerCol();
                        if (player != null) player.startWalking(dr, dc);
                        repaint();
                        updateStatus();
                    }
                    return;
                }

                int nr = shopperRow + dr, nc = shopperCol + dc;
                if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                    // Check NPC collision (only if NPC is on current floor)
                    if (thugger != null && thugger.getAppearFloor() == currentFloor && isNPCAtPosition(nr, nc)) {
                        return;  // Can't move through NPC
                    }
                    
                    TileType target = getCurrentGrid()[nr][nc];
                    
                    // Handle DOOR tiles with directional restrictions
                    if (target == TileType.DOOR) {
                        // Down door (21, 10) only accessible from above (dr = 1 moving down)
                        if (nr == 21 && nc == 10) {
                            if (dr != 1) {
                                // Not moving down - blocked
                                return;
                            }
                        }
                        // Up door (21, 11) only accessible from above (dr = 1 moving down)
                        else if (nr == 21 && nc == 11 && currentFloor != 2) {
                            if (dr != 1) {
                                // Not moving down - blocked
                                return;
                            }
                        }
                    }
                    
                    // Handle EXIT tiles with directional restrictions
                    if (target == TileType.EXIT) {
                        // Exit (21, 10) only accessible from above (dr = 1 moving down)
                        if (dr != 1) {
                            // Not moving down - blocked
                            return;
                        }
                    }
                    
                    if (target == TileType.FLOOR || target == TileType.SECRETFLOOR || target == TileType.STAIRS_UP || target == TileType.STAIRS_DOWN || target == TileType.DOOR || target == TileType.EXIT || target == TileType.BLACK_TILE) {
                        shopperRow = nr;
                        shopperCol = nc;
                        if (player != null) {
                            player.startWalking(dr, dc);
                        }
                        
                        // Check if player stepped on BLACK_TILE in secret hallway - teleport back to ground floor entrance
                        if (target == TileType.BLACK_TILE && currentFloor == 2) {
                            currentFloor = 0;  // Switch back to ground floor (Floor 0)
                            regenerateBlackTile();
                            shopperRow = 21;
                            shopperCol = 11;
                            System.out.println("Exited secret hallway back to ground floor at (21, 11)!");
                            repaint();
                            updateStatus();
                            return;
                        }
                        
                        // Check if player stepped on BLACK_TILE on Floor 1 - teleport to secret hallway
                        if (target == TileType.BLACK_TILE && currentFloor == 1) {
                            currentFloor = 2;  // Switch to secret hallway
                            shopperRow = 18;
                            shopperCol = 11;
                            System.out.println("Teleported to secret hallway!");
                            repaint();
                            updateStatus();
                            return;
                        }
                        
                        // Check if player reached waypoint
                        if (hasWaypoint && !waypoints.isEmpty()) {
                            for (int[] wp : waypoints) {
                                if (shopperRow == wp[0] && shopperCol == wp[1]) {
                                    hasWaypoint = false;
                                    waypoints.clear();
                                    System.out.println("Reached waypoint location");
                                    break;
                                }
                            }
                        }
                        repaint();
                        updateStatus();
                    }
                }
            }
        };
    }

    private Action useStairsAction() {
        return new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // Handle new ATM menu system
                if (atmMenu != null) {
                    // User selected an option from ATM menu
                    String selected = atmMenu.getSelectedOption();
                    if (selected.equals("Balance Inquiry")) {
                        atmState = 1;
                        balanceInquiryPanel = new BalanceInquiryPanel(player.getBankBalance());
                        atmMenu = null;
                        repaint();
                    } else if (selected.equals("Withdrawal")) {
                        atmState = 2;
                        atmWithdrawalPanel = new ATMWithdrawalPanel();
                        atmMenu = null;
                        atmWithdrawalPanel.requestFocus();
                        repaint();
                    }
                    return;
                }
                
                if (balanceInquiryPanel != null) {
                    // Return to menu from balance inquiry
                    atmState = 0;
                    atmMenu = new ATMMenu(playerUsername, atmSessionBalance, () -> {
                        atmMenu = null;
                        atmState = 0;
                        repaint();
                    });
                    balanceInquiryPanel = null;
                    repaint();
                    return;
                }
                
                if (atmWithdrawalPanel != null) {
                    // Handle withdrawal - subtract from bank balance and add to current balance
                    double withdrawAmount = atmWithdrawalPanel.getWithdrawalAmount();
                    if (withdrawAmount > 0 && withdrawAmount <= player.getBankBalance()) {
                        player.subtractFromBankBalance(withdrawAmount);
                        player.addBalance(withdrawAmount);
                        atmSessionBalance = player.getBankBalance();
                        // Show success panel
                        atmState = 3;
                        withdrawalSuccessPanel = new WithdrawalSuccessPanel(withdrawAmount);
                        atmWithdrawalPanel = null;
                        repaint();
                    }
                    return;
                }
                
                if (withdrawalSuccessPanel != null) {
                    // Return to menu from success panel
                    atmState = 0;
                    atmMenu = new ATMMenu(playerUsername, atmSessionBalance, () -> {
                        atmMenu = null;
                        atmState = 0;
                        repaint();
                    });
                    withdrawalSuccessPanel = null;
                    repaint();
                    return;
                }

                // If equipment confirmation is open, handle ENTER/ESC
                if (equipmentConfirmationPanel != null) {
                    // We need to check if it's a question type panel
                    // Since we don't have direct access, we'll create a wrapper to handle this
                    // For now, we'll check by trying to determine context
                    // Actually, let's add a method to EquipmentConfirmationPanel to handle key presses
                    equipmentConfirmationPanel.handleKeyPress(KeyEvent.VK_ENTER);
                    repaint();
                    return;
                }

                // If quantity input is open, handle ENTER to confirm
                if (quantityInputPanel != null) {
                    quantityInputPanel.confirmQuantity(() -> {
                        int quantity = quantityInputPanel.getSelectedQuantity();
                        
                        // Handle return confirmation
                        if (isProcessingReturn) {
                            if (inventory.removeProductByName(lastSelectedProductName, quantity)) {
                                String cleanName = extractProductName(lastSelectedProductName);
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Returned " + quantity + " " + cleanName + "(s).",
                                    "Return Successful",
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                                quantityInputPanel = null;
                                isProcessingReturn = false;
                                repaint();
                            } else {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Failed to return product.",
                                    "Return Failed",
                                    JOptionPane.ERROR_MESSAGE
                                );
                                quantityInputPanel = null;
                                isProcessingReturn = false;
                                repaint();
                            }
                            return;
                        }
                        
                        // Handle buy confirmation
                        // On confirm: check age restrictions first
                        if (!canBuyProduct(lastSelectedProductId)) {
                            JOptionPane.showMessageDialog(
                                null,
                                "You are not allowed to purchase this item due to age restrictions.",
                                "Purchase Blocked",
                                JOptionPane.WARNING_MESSAGE
                            );
                            quantityInputPanel = null;
                            repaint();
                            return;
                        }
                        
                        // On confirm: add product to inventory with discounts applied
                        double discountedPrice = calculateEffectivePrice(lastSelectedProductId, lastSelectedProductPrice);
                        
                        if (inventory.addProduct(lastSelectedProductId, lastSelectedProductName, quantity, discountedPrice, lastSelectedProductPrice)) {
                            quantityInputPanel = null;
                            menu = null;
                            repaint();
                        } else {
                            JOptionPane.showMessageDialog(
                                null,
                                "Your inventory is full! Please return some items first.",
                                "Inventory Full",
                                JOptionPane.WARNING_MESSAGE
                            );
                            quantityInputPanel = null;
                            repaint();
                        }
                    });
                    repaint();
                    return;
                }

                // If menu is open, handle product selection
                if (menu != null) {
                    // Get product info from menu
                    String productName = menu.getSelectedProductName();
                    String productId = menu.getSelectedProductId();
                    double price = menu.getSelectedProductPrice();
                    
                    System.out.println("DEBUG MENU ENTER: productName=" + productName + ", productId=" + productId + ", inReturnMode=" + menu.isInReturnMode());
                    
                    if (productName != null) {
                        // Handle return mode
                        if (menu.isInReturnMode()) {
                            // productName here is the full name from inventory (e.g., "🍎 Apples (FRU00001) - ₱50.00")
                            // Get the clean name for display
                            String cleanProductName = extractProductName(productName);
                            
                            // Show quantity selector for returns
                            int maxQuantity = inventory.getProductQuantity(productName);  // Use full name for lookup
                            
                            isProcessingReturn = true;
                            lastSelectedProductName = productName;  // Store full name for confirmation
                            
                            quantityInputPanel = new QuantityInputPanel(
                                "Return " + cleanProductName,
                                0,  // Price doesn't matter for returns
                                maxQuantity,
                                maxQuantity,
                                () -> {},
                                () -> {
                                    quantityInputPanel = null;
                                    isProcessingReturn = false;
                                    repaint();
                                }
                            );
                            quantityInputPanel.setSize(getWidth(), getHeight());
                            menu = null;
                            repaint();
                            return;
                        }
                        
                        // Handle buy mode (existing logic)
                        if (productId != null) {
                            // Check age restrictions before allowing selection
                            if (!canBuyProduct(productId)) {
                                menu = null;
                                JOptionPane.showMessageDialog(
                                    null,
                                    "You are not allowed to purchase this item due to age restrictions.\n\n" +
                                    "Players under 18 cannot buy:\n• Alcohol products\n• Cleaning Agents",
                                    "Purchase Not Allowed",
                                    JOptionPane.WARNING_MESSAGE
                                );
                                repaint();
                                return;
                            }
                            
                            // Store for later use in quantity confirmation
                            lastSelectedProductId = productId;
                            lastSelectedProductName = productName;
                            lastSelectedProductPrice = price;
                            
                            // Get actual available quantity from store
                            int maxAvailable = storeInventory.getAvailableQuantity(productId);
                            int maxCanCarry = inventory.getAvailableCapacity();
                            
                            if (maxCanCarry <= 0) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Your inventory is full! Please return some items first.",
                                    "Inventory Full",
                                    JOptionPane.WARNING_MESSAGE
                                );
                                menu = null;
                                repaint();
                                return;
                            }
                            
                            // Show quantity input panel
                            quantityInputPanel = new QuantityInputPanel(
                                productName, 
                                price, 
                                maxAvailable, 
                                maxCanCarry,
                                () -> {
                                    // This callback is no longer used, we handle confirmation through useStairsAction now
                                },
                                () -> {
                                    // On cancel: close quantity input
                                    quantityInputPanel = null;
                                    repaint();
                                }
                            );
                            quantityInputPanel.setSize(getWidth(), getHeight());
                            menu = null;  // Close the product menu when opening quantity panel
                            repaint();
                        }
                    }
                    return;
                }

                // If search kiosk is open, handle its key events
                if (searchKiosk != null) {
                    searchKiosk.handleKeyPress(KeyEvent.VK_ENTER);
                    repaint();
                    return;
                }
                
                // Check if facing Thugger NPC
                if (thugger != null && thugger.getAppearFloor() == currentFloor && isNPCAtPosition(shopperRow + facingDr, shopperCol + facingDc)) {
                    playThuggerSound();
                    playerLocked = true;
                    lockStartTime = System.currentTimeMillis();
                    thuggerInteractionTime = System.currentTimeMillis();  // Track when Thugger interaction started
                    isInteractingWithThugger = true;
                    // If player is normal-aged (18-59), grant permanent 50% discount on all products
                    if (playerAge >= 18 && playerAge < 60) {
                        hasInteractedWithThugger = true;
                    }
                    repaint();
                    return;
                }

                // Check if facing an interactable tile
                int targetRow = shopperRow + facingDr;
                int targetCol = shopperCol + facingDc;

                if (targetRow >= 0 && targetRow < SIZE && targetCol >= 0 && targetCol < SIZE) {
                    TileType facing = getCurrentGrid()[targetRow][targetCol];
                    
                    // Check if facing an ATM tile
                if (facing == TileType.ATM) {
                    atmState = 0;
                    atmSessionBalance = player.getBankBalance();
                    atmMenu = new ATMMenu(playerUsername, atmSessionBalance, () -> {
                        atmMenu = null;
                        atmState = 0;
                        repaint();
                    });
                    repaint();
                    return;
                }                    // Check if facing a SEARCH tile
                    if (facing == TileType.SEARCH) {
                        searchKiosk = new SearchKiosk(() -> searchKiosk = null);
                        searchKiosk.setParentComponent(TileGrid.this);
                        searchKiosk.setSize(getWidth(), getHeight());
                        requestFocusInWindow();  // Keep focus on TileGrid so it receives key events
                        repaint();
                        return;
                    }

                    // Check if facing a BASKET or CART tile
                    if (facing == TileType.BASKET) {
                        if (inventory.isEquipmentEquipped() && inventory.getEquipment() == ShopperInventory.EquipmentType.BASKET) {
                            // Already equipped, check if empty before allowing return
                            if (inventory.getTotalQuantity() > 0) {
                                // Not empty, ask if they want to return and forfeit
                                equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                    "Your basket is not empty.\nReturning it will cause the items\nin your equipment to be forfeited.",
                                    true,
                                    () -> {
                                        // Confirm return and forfeit items
                                        inventory.clear();
                                        inventory.returnEquipment();
                                        equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                            "Your items have been forfeited.",
                                            false,
                                            () -> {},
                                            () -> {
                                                equipmentConfirmationPanel = null;
                                                repaint();
                                                updateStatus();
                                            }
                                        );
                                        equipmentConfirmationPanel.setSize(getWidth(), getHeight());
                                        repaint();
                                        updateStatus();
                                    },
                                    () -> {
                                        equipmentConfirmationPanel = null;
                                        repaint();
                                    }
                                );
                            } else {
                                // Empty, ask if they want to return it
                                equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                    "Are you sure you want to\nreturn this basket?",
                                    true,
                                    () -> {
                                        inventory.returnEquipment();
                                        equipmentConfirmationPanel = null;
                                        repaint();
                                        updateStatus();
                                    },
                                    () -> {
                                        equipmentConfirmationPanel = null;
                                        repaint();
                                    }
                                );
                            }
                            equipmentConfirmationPanel.setSize(getWidth(), getHeight());
                            repaint();
                        } else {
                            // Show equip confirmation
                            inventory.setEquipment(ShopperInventory.EquipmentType.BASKET);
                            inventory.equipCurrentEquipment();
                            equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                "Basket equipped!",
                                false,
                                () -> {},
                                () -> {
                                    equipmentConfirmationPanel = null;
                                    repaint();
                                }
                            );
                            equipmentConfirmationPanel.setSize(getWidth(), getHeight());
                            repaint();
                            updateStatus();
                        }
                        return;
                    }

                    if (facing == TileType.CART) {
                        if (inventory.isEquipmentEquipped() && inventory.getEquipment() == ShopperInventory.EquipmentType.CART) {
                            // Already equipped, check if empty before allowing return
                            if (inventory.getTotalQuantity() > 0) {
                                // Not empty, ask if they want to return and forfeit
                                equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                    "Your cart is not empty.\nReturning it will cause the items\nin your equipment to be forfeited.",
                                    true,
                                    () -> {
                                        // Confirm return and forfeit items
                                        inventory.clear();
                                        inventory.returnEquipment();
                                        equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                            "Your items have been forfeited.",
                                            false,
                                            () -> {},
                                            () -> {
                                                equipmentConfirmationPanel = null;
                                                repaint();
                                                updateStatus();
                                            }
                                        );
                                        equipmentConfirmationPanel.setSize(getWidth(), getHeight());
                                        repaint();
                                        updateStatus();
                                    },
                                    () -> {
                                        equipmentConfirmationPanel = null;
                                        repaint();
                                    }
                                );
                            } else {
                                // Empty, ask if they want to return it
                                equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                    "Are you sure you want to\nreturn this cart?",
                                    true,
                                    () -> {
                                        inventory.returnEquipment();
                                        equipmentConfirmationPanel = null;
                                        repaint();
                                        updateStatus();
                                    },
                                    () -> {
                                        equipmentConfirmationPanel = null;
                                        repaint();
                                    }
                                );
                            }
                            equipmentConfirmationPanel.setSize(getWidth(), getHeight());
                            repaint();
                        } else {
                            // Show equip confirmation
                            inventory.setEquipment(ShopperInventory.EquipmentType.CART);
                            inventory.equipCurrentEquipment();
                            equipmentConfirmationPanel = new EquipmentConfirmationPanel(
                                "Cart equipped!",
                                false,
                                () -> {},
                                () -> {
                                    equipmentConfirmationPanel = null;
                                    repaint();
                                }
                            );
                            equipmentConfirmationPanel.setSize(getWidth(), getHeight());
                            repaint();
                            updateStatus();
                        }
                        return;
                    }

                    // Check if it's a cashier tile
                    if (facing == TileType.CASHIER) {
                        if (inventory.isEmpty()) {
                            JOptionPane.showMessageDialog(
                                null,
                                "You have no items to check out.",
                                "Empty Checkout",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            return;
                        }
                        
                        double totalPrice = inventory.getTotalPrice();
                        
                        // Show checkout confirmation
                        int choice = JOptionPane.showConfirmDialog(
                            null,
                            "Proceed with checkout?\n\nTotal: ₱" + String.format("%.2f", totalPrice),
                            "Checkout",
                            JOptionPane.YES_NO_OPTION
                        );
                        
                        if (choice == JOptionPane.YES_OPTION) {
                            // Check if player has sufficient balance
                            double playerBalance = player.getBalance();
                            if (playerBalance < totalPrice) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Insufficient balance!",
                                    "Checkout Failed",
                                    JOptionPane.WARNING_MESSAGE
                                );
                                repaint();
                                return;
                            }
                            
                            // Generate receipt only after successful balance check
                            String receipt = inventory.generateReceipt();
                            
                            // Complete checkout - deduct balance
                            player.setBalance(playerBalance - totalPrice);
                            inventory.checkout();
                            JOptionPane.showMessageDialog(
                                null,
                                "Checkout successful!\n\nYour receipt has been saved.\nPress B to view it.",
                                "Checkout Complete",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                            repaint();
                            updateStatus();
                        }
                        return;
                    }

                    // Check if it's an interactable tile
                    String tileKey = null;
                    if (facing == TileType.SHELF) tileKey = "shelf";
                    else if (facing == TileType.CHILLED) tileKey = "chilled";
                    else if (facing == TileType.FRIDGE) tileKey = "ref";
                    else if (facing == TileType.TABLE) tileKey = "table";

                    if (tileKey != null && TileInventory.isInteractable(tileKey)) {
                        // Clear waypoints when opening a menu (player is interacting with a tile)
                        hasWaypoint = false;
                        waypoints.clear();
                        if (searchKiosk != null) {
                            searchKiosk.clearWaypoint();
                        }
                        
                        // Store current tile info for return functionality
                        currentTileRow = targetRow;
                        currentTileCol = targetCol;
                        currentReturnableProducts = getReturnableProducts(tileKey, targetRow, targetCol, currentFloor);
                        
                        menu = new InteractionMenu(tileKey, targetRow, targetCol, currentFloor, () -> menu = null);
                        menu.setParentComponent(TileGrid.this);  // Set parent for repaint on mode switch
                        menu.setSize(getWidth(), getHeight());
                        requestFocusInWindow();  // Ensure TileGrid keeps focus for KeyListener
                        repaint();
                        return;
                    }
                }

                // Otherwise, handle stairs interaction
                TileType t = getCurrentGrid()[shopperRow][shopperCol];
                if (t == TileType.STAIRS_UP && currentFloor == 0) {
                    if (gridFloor1[shopperRow][shopperCol] != TileType.WALL) {
                        currentFloor = 1;
                        System.out.println("DEBUG: Stairs UP pressed - currentFloor now = 1");
                        regenerateBlackTile();
                        // Filter waypoints to only show those on floor 1
                        if (hasWaypoint && !allWaypoints.isEmpty()) {
                            waypoints = new java.util.ArrayList<>();
                            for (int[] wp : allWaypoints) {
                                if (wp[0] == 1) {
                                    waypoints.add(wp);
                                }
                            }
                        }
                        repaint();
                        updateStatus();
                    }
                } else if (t == TileType.STAIRS_DOWN && currentFloor == 1) {
                    if (gridFloor0[shopperRow][shopperCol] != TileType.WALL) {
                        currentFloor = 0;
                        System.out.println("DEBUG: Stairs DOWN pressed - currentFloor now = 0");
                        // Filter waypoints to only show those on floor 0
                        if (hasWaypoint && !allWaypoints.isEmpty()) {
                            waypoints = new java.util.ArrayList<>();
                            for (int[] wp : allWaypoints) {
                                if (wp[0] == 0) {
                                    waypoints.add(wp);
                                }
                            }
                        }
                        repaint();
                        updateStatus();
                    }
                } else if (t == TileType.EXIT && currentFloor == 0) {
                    // Handle exit tile
                    handleExitTile();
                }
            }
        };
    }
    
    /**
     * Handle when player steps on the EXIT tile
     */
    private void handleExitTile() {
        // Check if player has equipment
        if (inventory.getEquipment() != ShopperInventory.EquipmentType.HANDS) {
            JOptionPane.showMessageDialog(
                null,
                "You cannot leave the supermarket with equipment!\nPlease return your equipment first.",
                "Cannot Exit",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Check if player has unchecked items in inventory
        if (!inventory.isEmpty()) {
            JOptionPane.showMessageDialog(
                null,
                "You have unchecked items in your inventory!\nPlease checkout first.",
                "Cannot Exit",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        // Player can exit - ask to restart or end simulation
        int choice = JOptionPane.showConfirmDialog(
            null,
            "You have left the supermarket.\n\nWould you like to restart the simulation?",
            "Supermarket Exit",
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // Restart the simulation
            restartSimulation();
        } else {
            // End the simulation
            System.exit(0);
        }
    }
    
    /**
     * Restart the simulation with all values reset
     */
    private void restartSimulation() {
        // Reset player position
        shopperRow = 21;
        shopperCol = 11;
        currentFloor = 0;
        
        // Reset player state
        player.face(-1, 0);  // Face up
        
        // Reset inventory
        storeInventory = new StoreInventory();
        inventory = new ShopperInventory(storeInventory);
        
        // Reset player balance and other state
        playerAge = 18;
        playerUsername = "Player";
        isInteractingWithThugger = false;
        hasInteractedWithThugger = false;
        thuggerInteractionTime = 0;
        discountMultiplier = 1.0;
        player.setBalance(initialBalance);
        
        // Reset other UI elements
        inventoryDisplay = null;
        menu = null;
        atmMenu = null;
        searchKiosk = null;
        hasWaypoint = false;
        waypoints.clear();
        allWaypoints.clear();
        
        // Clear dialogue
        dialogueText = null;
        
        repaint();
        updateStatus();
    }

    private void updateStatus() {
        if (board != null) {
            Tile[][] btiles = board.getTiles();
            String type = "<unknown>";
            if (shopperRow >= 0 && shopperRow < btiles.length && shopperCol >= 0 && shopperCol < btiles[0].length) {
                Tile bt = btiles[shopperRow][shopperCol];
                if (bt != null) type = bt.getType();
            }
            status.setText(String.format("Floor: %d — Shopper: (%d, %d) — Tile: %s", currentFloor + 1, shopperRow, shopperCol, type));
            return;
        }

        TileType t = getCurrentGrid()[shopperRow][shopperCol];
        status.setText(String.format("Floor: %d — Shopper: (%d, %d) — Tile: %s", currentFloor + 1, shopperRow, shopperCol, t.name()));
    }

    /**
     * Renders the entire game grid including tiles, player, NPCs, and UI components.
     * Called automatically by the Swing repaint loop.
     *
     * @param g0 the Graphics object to paint on
     */
    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        TileType[][] grid = getCurrentGrid();

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int x = c * CELL, y = r * CELL;
                if (board != null) {
                    renderBoardTile(g, x, y, r, c);
                } else {
                    renderGridTile(g, x, y, grid[r][c]);
                }

                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, CELL, CELL);
            }
        }

        int centerX = shopperCol * CELL + CELL / 2;
        int centerY = shopperRow * CELL + CELL / 2;

        // Draw Thugger first (so player appears in front)
        if (thugger != null && thugger.getAppearFloor() == currentFloor) {
            thugger.draw(g, CELL);
        }

        // Draw player sprite
        if (player != null) {
            player.draw(g, centerX, centerY, CELL);
        } else {
            int sx = shopperCol * CELL, sy = shopperRow * CELL;
            int pad = CELL / 6;
            g.setColor(new Color(220, 40, 40));
            g.fillOval(sx + pad, sy + pad, CELL - 2 * pad, CELL - 2 * pad);
        }

        // Draw player name and age labels AFTER player sprite (so they appear on top)
        if (player != null) {
            g.setColor(new Color(220, 220, 220));
            g.setFont(new Font("Arial", Font.BOLD, 9));
            FontMetrics fm = g.getFontMetrics();
            String playerLabel = playerUsername;
            int labelX = centerX - fm.stringWidth(playerLabel) / 2;
            int labelY = centerY - CELL + 5;
            g.drawString(playerLabel, labelX, labelY);
            
            // Draw age below name
            g.setFont(new Font("Arial", Font.PLAIN, 8));
            String ageStr = "Age: " + playerAge;
            fm = g.getFontMetrics();
            int ageX = centerX - fm.stringWidth(ageStr) / 2;
            int ageY = labelY + 10;
            g.drawString(ageStr, ageX, ageY);
        }

        // Draw Thugger label AFTER player (so it appears on top of Thugger sprite)
        if (thugger != null && thugger.getAppearFloor() == currentFloor) {
            g.setColor(new Color(220, 220, 220));
            g.setFont(new Font("Arial", Font.BOLD, 10));
            int thuggerScreenX = thugger.getX();
            int thuggerScreenY = thugger.getY() - CELL;  // Position above Thugger
            String thuggerLabel = "Young Thug";
            FontMetrics fm = g.getFontMetrics();
            int labelWidth = fm.stringWidth(thuggerLabel);
            int labelX = thuggerScreenX - labelWidth / 2;
            int labelY = thuggerScreenY;
            g.drawString(thuggerLabel, labelX, labelY);
        }

        // Draw floor indicator (top left)
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(4, 4, 70, 18);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 12f));
        String floorText = currentFloor == 2 ? "SECRET" : "Floor " + (currentFloor + 1);
        g.drawString(floorText, 8, 16);

        // Draw inventory status with remaining capacity (next to floor)
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 11f));
        int currentQuantity = inventory.getTotalQuantity();
        int maxCapacity = inventory.getEquipment().getCapacity();
        int remainingCapacity = maxCapacity - currentQuantity;
        String equipmentStr = "Equipment: " + inventory.getEquipment().getDisplayName() + " (" + remainingCapacity + "/" + maxCapacity + ")";
        FontMetrics fm = g.getFontMetrics();
        int equipmentWidth = fm.stringWidth(equipmentStr) + 8;
        
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(80, 4, equipmentWidth, 18);
        g.setColor(Color.CYAN);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 11f));
        g.drawString(equipmentStr, 84, 16);
        
        // Draw balance (top right)
        String balanceStr = "Balance: ₱" + String.format("%.2f", player.getBalance());
        int balanceWidth = fm.stringWidth(balanceStr) + 8;
        
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(getWidth() - balanceWidth - 4, 4, balanceWidth, 18);
        g.setColor(new Color(144, 238, 144));
        g.setFont(g.getFont().deriveFont(Font.BOLD, 11f));
        g.drawString(balanceStr, getWidth() - balanceWidth, 16);
        
        // Draw help text (under floor, top left)
        String helpStr = "V: Inventory | ENTER: Interact";
        int helpWidth = fm.stringWidth(helpStr) + 8;
        
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(4, 24, helpWidth, 18);
        g.setColor(new Color(200, 200, 100));
        g.setFont(g.getFont().deriveFont(Font.PLAIN, 10f));
        g.drawString(helpStr, 8, 36);
        
        // Draw dialogue bubble if active (only show Thugger dialogue on floor 2)
        if (dialogueText != null && currentFloor == 2) {
            drawDialogueBubble(g, dialogueText);
        }

        // Draw waypoint highlights if active (AFTER sprites so visible on top)
        // Check both TileGrid's hasWaypoint and SearchKiosk's active waypoint state
        boolean shouldShowWaypoints = hasWaypoint || (searchKiosk != null && searchKiosk.hasActiveWaypoint());
        java.util.List<int[]> wpToRender = (searchKiosk != null && searchKiosk.hasActiveWaypoint()) ? searchKiosk.getWaypoints() : waypoints;
        
        if (shouldShowWaypoints && !wpToRender.isEmpty()) {
            // Draw pulsing border effect - alternates visibility to show through sprites
            long elapsed = System.currentTimeMillis() % 1000;
            float pulse = (float) Math.sin(elapsed / 500.0 * Math.PI);
            
            // When pulse is positive (> 0.3), draw the waypoints
            if (pulse > 0.3) {
                int alpha = (int) (100 + 155 * pulse);
                alpha = Math.max(50, Math.min(255, alpha));
                
                g.setColor(new Color(255, 100, 100, alpha));
                g.setStroke(new BasicStroke(3));
                
                // Draw all waypoint tiles on current floor only
                for (int[] wp : wpToRender) {
                    int waypointFloor = wp[0];
                    int waypointRow = wp[1];
                    int waypointCol = wp[2];
                    
                    // Only draw if waypoint is on current floor
                    if (waypointFloor == currentFloor && waypointRow >= 0 && waypointRow < SIZE && waypointCol >= 0 && waypointCol < SIZE) {
                        int wpX = waypointCol * CELL;
                        int wpY = waypointRow * CELL;
                        
                        g.drawRect(wpX + 1, wpY + 1, CELL - 2, CELL - 2);
                        
                        // Draw corner markers
                        g.fillRect(wpX, wpY, 6, 6);
                        g.fillRect(wpX + CELL - 6, wpY, 6, 6);
                        g.fillRect(wpX, wpY + CELL - 6, 6, 6);
                        g.fillRect(wpX + CELL - 6, wpY + CELL - 6, 6, 6);
                    }
                }
            }
        }

        // Draw search kiosk if open
        if (searchKiosk != null) {
            searchKiosk.setSize(getWidth(), getHeight());
            searchKiosk.paintComponent(g);
        }

        // Draw menu if open
        if (menu != null) {
            menu.setSize(getWidth(), getHeight());
            menu.paintComponent(g);
        }

        // Draw inventory display if open
        if (inventoryDisplay != null) {
            inventoryDisplay.setSize(getWidth(), getHeight());
            inventoryDisplay.paintComponent(g);
        }

        // Draw quantity input if open
        if (quantityInputPanel != null) {
            quantityInputPanel.setSize(getWidth(), getHeight());
            quantityInputPanel.paintComponent(g);
        }

        // Draw return product panel if open
        if (returnProductPanel != null) {
            returnProductPanel.setSize(getWidth(), getHeight());
            returnProductPanel.paintComponent(g);
        }
        
        // Draw ATM menu if open
        if (atmMenu != null) {
            atmMenu.setSize(getWidth(), getHeight());
            atmMenu.paintComponent(g);
        }
        
        // Draw balance inquiry if open
        if (balanceInquiryPanel != null) {
            balanceInquiryPanel.setSize(getWidth(), getHeight());
            balanceInquiryPanel.paintComponent(g);
        }
        
        // Draw ATM withdrawal if open
        if (atmWithdrawalPanel != null) {
            atmWithdrawalPanel.setSize(getWidth(), getHeight());
            atmWithdrawalPanel.paintComponent(g);
        }
        
        // Draw withdrawal success if open
        if (withdrawalSuccessPanel != null) {
            withdrawalSuccessPanel.setSize(getWidth(), getHeight());
            withdrawalSuccessPanel.paintComponent(g);
        }

        // Draw equipment confirmation if open
        if (equipmentConfirmationPanel != null) {
            equipmentConfirmationPanel.setSize(getWidth(), getHeight());
            equipmentConfirmationPanel.paintComponent(g);
        }

        g.dispose();
    }

    private void renderBoardTile(Graphics2D g, int x, int y, int r, int c) {
        Tile bt = board.getTiles()[r][c];
        String ty = bt != null ? bt.getType() : "empty";
        if (ty.equalsIgnoreCase("empty") && imageLoader.getFloorImage() != null) {
            g.drawImage(imageLoader.getFloorImage(), x, y, CELL, CELL, null);
        } else {
            switch (ty.toLowerCase()) {
                case "wall": g.setColor(new Color(80, 80, 80)); break;
                case "ref": g.setColor(new Color(30, 90, 200)); break;
                case "chilled": g.setColor(new Color(150, 210, 255)); break;
                case "shelf": g.setColor(new Color(245, 175, 35)); break;
                case "table": g.setColor(new Color(80, 160, 60)); break;
                case "ladder": g.setColor(new Color(200, 200, 200)); break;
                case "counter": g.setColor(new Color(180, 140, 200)); break;
                case "basket": g.setColor(new Color(200, 160, 120)); break;
                case "cart": g.setColor(new Color(160, 120, 200)); break;
                case "door": g.setColor(new Color(180, 180, 250)); break;
                case "player": g.setColor(new Color(220, 220, 220)); break;
                default: g.setColor(TileType.FLOOR.color); break;
            }
            g.fillRect(x, y, CELL, CELL);
            if (ty.equalsIgnoreCase("ladder")) {
                drawStairIcon(g, x, y, CELL, true);
            }
        }
    }

    private void renderGridTile(Graphics2D g, int x, int y, TileType t) {
        if (t == TileType.FLOOR && imageLoader.getFloorImage() != null) {
            g.drawImage(imageLoader.getFloorImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.SECRETFLOOR && imageLoader.getSecretFloorImage() != null) {
            g.drawImage(imageLoader.getSecretFloorImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.TABLE && imageLoader.getTableImage() != null) {
            g.drawImage(imageLoader.getTableImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.FRIDGE && imageLoader.getFridgeImage() != null) {
            g.drawImage(imageLoader.getFridgeImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.CHILLED && imageLoader.getChilledImage() != null) {
            g.drawImage(imageLoader.getChilledImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.SHELF && imageLoader.getShelfImage() != null) {
            g.drawImage(imageLoader.getShelfImage(), x, y, CELL, CELL, null);
        } else if ((t == TileType.STAIRS_UP || t == TileType.STAIRS_DOWN) && imageLoader.getStairsImage() != null) {
            g.drawImage(imageLoader.getStairsImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.SEARCH && imageLoader.getSearchImage() != null) {
            g.drawImage(imageLoader.getSearchImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.BASKET && imageLoader.getBasketImage() != null) {
            g.drawImage(imageLoader.getBasketImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.CART && imageLoader.getCartImage() != null) {
            g.drawImage(imageLoader.getCartImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.CASHIER && imageLoader.getCashierImage() != null) {
            g.drawImage(imageLoader.getCashierImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.ATM && imageLoader.getAtmImage() != null) {
            g.drawImage(imageLoader.getAtmImage(), x, y, CELL, CELL, null);
        } else if (t == TileType.BLACK_TILE) {
            g.setColor(t.color);
            g.fillRect(x, y, CELL, CELL);
        } else if (t == TileType.DOOR) {
            g.setColor(t.color);
            g.fillRect(x, y, CELL, CELL);
        } else {
            // WALL and other tiles - use dark gray for secret floor, normal color otherwise
            if (currentFloor == 2 && t == TileType.WALL) {
                g.setColor(new Color(40, 40, 45));
            } else {
                g.setColor(t.color);
            }
            g.fillRect(x, y, CELL, CELL);
        }

        if (t == TileType.DOOR) {
            drawDoorArrow(g, x, y, CELL);
        } else if (t == TileType.EXIT) {
            drawExitArrow(g, x, y, CELL);
        }
    }

    private void drawStairIcon(Graphics2D g, int x, int y, int size, boolean up) {
        int stepH = size / 6;
        int stepW = size / 6;
        int sx = x + size / 6;
        int sy = y + size / 6;
        g.setColor(new Color(60, 60, 60, 200));
        for (int i = 0; i < 4; i++) {
            int rx = sx + i * stepW;
            int ry = sy + (3 - i) * stepH;
            g.fillRect(rx, ry, stepW * (i + 1), stepH);
            g.setColor(g.getColor().brighter());
        }
        g.setColor(new Color(20, 20, 20));
        int ax = x + size - size / 4;
        int ay = y + size / 2;
        if (up) {
            int[] px = {ax, ax - size / 8, ax + size / 8};
            int[] py = {ay - size / 6, ay + size / 12, ay + size / 12};
            g.fillPolygon(px, py, 3);
        } else {
            int[] px = {ax, ax - size / 8, ax + size / 8};
            int[] py = {ay + size / 6, ay - size / 12, ay - size / 12};
            g.fillPolygon(px, py, 3);
        }
    }

    private void drawDoorArrow(Graphics2D g, int x, int y, int size) {
        // Determine which door this is based on position
        // Row 21, Col 11 = spawn (arrow up), Row 21, Col 10 = exit (arrow down)
        int col = x / size;
        int row = y / size;
        
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int arrowSize = size / 3;
        
        g.setColor(new Color(255, 255, 200));
        g.setStroke(new BasicStroke(2));
        
        if (row == 21 && col == 11) {
            // Spawn door - arrow pointing up
            g.drawLine(centerX, centerY - arrowSize, centerX, centerY + arrowSize);
            g.drawLine(centerX - arrowSize / 2, centerY - arrowSize / 2, centerX, centerY - arrowSize);
            g.drawLine(centerX + arrowSize / 2, centerY - arrowSize / 2, centerX, centerY - arrowSize);
        } else if (row == 21 && col == 10) {
            // Exit door - arrow pointing down
            g.drawLine(centerX, centerY - arrowSize, centerX, centerY + arrowSize);
            g.drawLine(centerX - arrowSize / 2, centerY + arrowSize / 2, centerX, centerY + arrowSize);
            g.drawLine(centerX + arrowSize / 2, centerY + arrowSize / 2, centerX, centerY + arrowSize);
        }
    }

    private void drawExitArrow(Graphics2D g, int x, int y, int size) {
        // Draw exit arrow pointing down
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int arrowSize = size / 3;
        
        g.setColor(new Color(100, 255, 100));
        g.setStroke(new BasicStroke(2));
        
        // Arrow pointing down
        g.drawLine(centerX, centerY - arrowSize, centerX, centerY + arrowSize);
        g.drawLine(centerX - arrowSize / 2, centerY + arrowSize / 2, centerX, centerY + arrowSize);
        g.drawLine(centerX + arrowSize / 2, centerY + arrowSize / 2, centerX, centerY + arrowSize);
    }

    private void regenerateBlackTile() {
        // Clear previous black tiles on Floor 2 (gridFloor1)
        if (blackTileRow != -1 && blackTileCol != -1) {
            if (gridFloor1[blackTileRow][blackTileCol] == TileType.BLACK_TILE) {
                gridFloor1[blackTileRow][blackTileCol] = TileType.ATM;
            }
        }
        
        // Generate new random black tile (10% chance)
        if (Math.random() < 0.1) {
            // Choose either ATM at (16, 1) or (16, 20)
            if (Math.random() < 0.5) {
                blackTileRow = 16;
                blackTileCol = 1;
            } else {
                blackTileRow = 16;
                blackTileCol = 20;
            }
            gridFloor1[blackTileRow][blackTileCol] = TileType.BLACK_TILE;
            System.out.println("DEBUG: BLACK TILE REGENERATED AT (" + blackTileRow + ", " + blackTileCol + ")");
            System.out.flush();
        } else {
            blackTileRow = -1;
            blackTileCol = -1;
            System.out.println("DEBUG: No black tile this visit");
            System.out.flush();
        }
    }
    
    private void playThuggerSound() {
        // Only show notification in secret room (floor 2)
        boolean showNotification = (currentFloor == 2);
        if (showNotification) {
            dialogueText = "Now Playing:\nHalftime - Young Thug";
            dialogueStartTime = System.currentTimeMillis();
        }
        
        new Thread(() -> {
            try {
                String soundPath = "C:\\Users\\CHARLZ\\Desktop\\mco1 - gui\\Graphics\\Player\\Halftime.wav";
                File soundFile = new File(soundPath);
                if (!soundFile.exists()) {
                    System.err.println("Sound file not found: " + soundPath);
                    return;
                }
                
                AudioInputStream audioInputStream = 
                    AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                
                // Keep dialogue visible for the duration of the clip (only if it was shown)
                long clipDuration = (long) (clip.getMicrosecondLength() / 1000.0);
                Thread.sleep(clipDuration);
                if (showNotification) {
                    dialogueText = null;
                }
                
                // Apply 50% discount after song finishes
                discountMultiplier = 0.5;
                System.out.println("DEBUG: 50% discount applied to all items!");
            } catch (Exception e) {
                System.err.println("Error playing sound: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
    
    private void drawDialogueBubble(Graphics2D g, String text) {
        // Position bubble above thugger if he's visible
        int bubbleX, bubbleY;
        
        if (thugger != null && thugger.getAppearFloor() == currentFloor) {
            // Position above thugger's position
            bubbleX = thugger.getX() - 75;
            bubbleY = thugger.getY() - 95;
        } else {
            // Fallback position
            bubbleX = getWidth() / 2 - 75;
            bubbleY = 80;
        }
        
        int bubbleWidth = 150;
        int bubbleHeight = 65;
        
        // Draw bubble background with border (square, no tail)
        g.setColor(new Color(40, 40, 40, 230));
        g.fillRect(bubbleX, bubbleY, bubbleWidth, bubbleHeight);
        
        g.setColor(new Color(255, 215, 0));
        g.setStroke(new BasicStroke(2.5f));
        g.drawRect(bubbleX, bubbleY, bubbleWidth, bubbleHeight);
        
        // Draw text
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 11f));
        FontMetrics fm = g.getFontMetrics();
        
        String[] lines = text.split("\n");
        
        // Calculate total text height
        int totalHeight = lines.length * fm.getHeight();
        int startY = bubbleY + (bubbleHeight - totalHeight) / 2 + fm.getAscent();
        
        for (String line : lines) {
            int textWidth = fm.stringWidth(line);
            int textX = bubbleX + (bubbleWidth - textWidth) / 2;
            g.drawString(line, textX, startY);
            startY += fm.getHeight();
        }
    }

    /**
     * Determine if product can be purchased based on age
     */
    private boolean canBuyProduct(String productId) {
        if (playerAge < 18) {
            // Players under 18 cannot buy Alcohol (ALC) or Cleaning Agents (CLE)
            // UNLESS they are interacting with Thugger (who sells alcohol to minors)
            if (isInteractingWithThugger) {
                return !productId.startsWith("CLE");  // Can buy anything except cleaning agents
            }
            return !productId.startsWith("ALC") && !productId.startsWith("CLE");
        }
        return true;
    }

    /**
     * Get returnable products for a tile - returns list of products in player's inventory
     * that are also sold at the given tile
     */
    private java.util.List<String> getReturnableProducts(String tileKey, int row, int col, int floor) {
        java.util.List<String> returnable = new java.util.ArrayList<>();
        String[] tileProducts = TileInventory.getItemsForTile(tileKey, row, col, floor);
        
        System.out.println("DEBUG getReturnableProducts: tileKey=" + tileKey + ", tileProducts.length=" + tileProducts.length);
        
        for (String product : tileProducts) {
            // Extract product name without ID and price for lookup
            String cleanName = extractProductName(product);
            
            // Try to find this product in inventory by matching clean names
            for (String inventoryProductName : inventory.getAllProductNames()) {
                String inventoryCleanName = extractProductName(inventoryProductName);
                if (inventoryCleanName.equals(cleanName)) {
                    int quantity = inventory.getProductQuantity(inventoryProductName);
                    System.out.println("  Checking product: '" + product + "' -> clean: '" + cleanName + "' -> found in inventory as: '" + inventoryProductName + "' -> quantity: " + quantity);
                    if (quantity > 0) {
                        returnable.add(inventoryProductName);  // Add the full name
                    }
                    break;
                }
            }
        }
        
        System.out.println("DEBUG: Returnable products count = " + returnable.size());
        return returnable;
    }

    /**
     * Extract product name from full product string (e.g., "🍪 Candies (SNK00001) - ₱45.00" -> "Candies")
     */
    private String extractProductName(String productString) {
        // Format: "emoji Name (ID) - ₱price"
        int parenIndex = productString.indexOf('(');
        if (parenIndex > 0) {
            String namePart = productString.substring(0, parenIndex).trim();
            // Remove emoji
            if (namePart.length() > 0 && Character.isHighSurrogate(namePart.charAt(0))) {
                namePart = namePart.substring(2).trim();
            } else if (namePart.length() > 1 && namePart.charAt(0) > 127) {
                namePart = namePart.substring(1).trim();
            }
            return namePart;
        }
        return productString;
    }

    /**
     * Get product category from product ID
     */
    private String getProductCategory(String productId) {
        if (productId == null || productId.length() < 3) return "";
        return productId.substring(0, 3).toUpperCase();
    }

    /**
     * Check if product qualifies for senior discount
     * Senior citizens (60+) get discounts on consumables except alcohol
     */
    private boolean qualifiesForSeniorDiscount(String productId) {
        if (playerAge < 60) return false;
        
        String category = getProductCategory(productId);
        // Cannot discount alcohol (ALC)
        if (category.equals("ALC")) return false;
        
        // Beverages: SFT (soft drinks), JUC (juice), MLK (milk)
        // Food: CER (cereal), NDL (noodles), SNK (snacks), CAN (canned), 
        //       FRZ (frozen), BRD (bread), FRU (fruit), VEG (vegetable), EGG (eggs),
        //       CHK (chicken), BEF (beef), SEA (seafood)
        // Condiments (CON) are food
        String[] foodCategories = {"CER", "NDL", "SNK", "CAN", "FRZ", "BRD", "FRU", "VEG", "EGG", "CHK", "BEF", "SEA", "CHS", "CON"};
        String[] beverageCategories = {"SFT", "JUC", "MLK"};
        
        for (String cat : foodCategories) {
            if (category.equals(cat)) return true;
        }
        for (String cat : beverageCategories) {
            if (category.equals(cat)) return true;
        }
        
        return false;
    }

    /**
     * Get senior discount percentage for a product
     * Food items: 20% off, Beverages: 10% off
     */
    private double getSeniorDiscountMultiplier(String productId) {
        String category = getProductCategory(productId);
        
        // Beverages: 10% off (0.9)
        String[] beverageCategories = {"SFT", "JUC", "MLK"};
        for (String cat : beverageCategories) {
            if (category.equals(cat)) return 0.9;
        }
        
        // Food items: 20% off (0.8)
        return 0.8;
    }

    /**
     * Calculate effective price considering age restrictions and senior discounts
     */
    private double calculateEffectivePrice(String productId, double basePrice) {
        // First check age restriction
        if (!canBuyProduct(productId)) {
            return -1;  // Indicate purchase not allowed
        }
        
        double price = basePrice;
        
        // Apply Thugger discount (50% - already applied via discountMultiplier)
        price *= discountMultiplier;
        
        // Apply 50% discount for normal-aged players who have interacted with Thugger
        if (hasInteractedWithThugger) {
            price *= 0.5;  // 50% off all products
        }
        
        // Apply senior discount if qualified
        if (qualifiesForSeniorDiscount(productId)) {
            price *= getSeniorDiscountMultiplier(productId);
        }
        
        return price;
    }
}
