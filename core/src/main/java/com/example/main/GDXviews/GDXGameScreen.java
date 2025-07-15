package com.example.main.GDXviews;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.controller.GameMenuController;
import com.example.main.controller.TradeMenuController;
import com.example.main.enums.design.TileType;
import com.example.main.models.App;
import com.example.main.models.Date;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.Player;
import com.example.main.models.Tile;
import com.example.main.models.Time;
import com.example.main.models.User;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.example.main.models.Result;

public class GDXGameScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private GameMap gameMap;
    private GameMenuController controller;

    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRenderer;

    private float cameraSpeed = 200f;
    private boolean cameraFollowsPlayer = true;

    private boolean showMinimap = false;
    private boolean mKeyPressed = false;
    
    // Trade menu variables
    private boolean showTradeMenu = false;
    private boolean tKeyPressed = false;
    private Table tradeMenuTable;
    
    // Trade menu state management
    private enum TradeMenuState {
        NEW_TRADE_NOTIFICATIONS,
        MAIN_MENU,
        PLAYER_SELECTION,
        TRADE_TYPE_SELECTION,
        TRADE_DETAILS,
        ACTIVE_TRADES,
        TRADE_HISTORY
    }
    
    private TradeMenuState currentTradeMenuState = TradeMenuState.MAIN_MENU;
    private List<String> newTradeNotifications = new ArrayList<>();
    
    // Trade form data
    private String selectedPlayerForTrade = null;
    private String selectedTradeType = null;
    private String itemName = "";
    private int itemAmount = 1;
    private int itemPrice = 1;
    private String givingItemName = "";
    private int givingItemAmount = 1;
    private String receivingItemName = "";
    private int receivingItemAmount = 1;
    private String tradeErrorMessage = "";

    // Time-related variables
    private float timeAccumulator = 0f;
    private static final float SECONDS_PER_10_IN_GAME_MINUTES = 0.7f;

    // HUD Assets
    private Texture clockTexture;
    private BitmapFont hudFont;

    private Texture ground1Texture;
    private Texture ground2Texture;
    private Texture grass1Texture;
    private Texture grass2Texture;
    private Texture shoveledTexture;
    private Texture tree1Texture;
    private Texture tree2Texture;
    private Texture tree3Texture;
    private Texture house1Texture;
    private Texture house2Texture;
    private Texture house3Texture;
    private Texture npcHouse1Texture;
    private Texture npcHouse2Texture;
    private Texture npcHouse3Texture;
    private Texture npcHouse4Texture;
    private Texture npcHouse5Texture;
    private Texture bushTexture;
    private Texture stone1Texture;
    private Texture stone2Texture;
    private Texture copperStoneTexture;
    private Texture ironStoneTexture;
    private Texture goldStoneTexture;
    private Texture iridiumStoneTexture;
    private Texture jewelStoneTexture;

    private Texture blacksmithTexture;
    private Texture jojamartTexture;
    private Texture pierresShopTexture;
    private Texture carpentersShopTexture;
    private Texture fishShopTexture;
    private Texture ranchTexture;
    private Texture saloonTexture;

    private Texture lake1Texture;
    private Texture lake2Texture;
    private Texture lake3Texture;

    private Texture maleIdleTexture;
    private Texture maleDown1Texture, maleDown2Texture;
    private Texture maleUp1Texture, maleUp2Texture;
    private Texture maleLeft1Texture, maleLeft2Texture;
    private Texture maleRight1Texture, maleRight2Texture;

    private Texture femaleIdleTexture;
    private Texture femaleDown1Texture, femaleDown2Texture;
    private Texture femaleUp1Texture, femaleUp2Texture;
    private Texture femaleLeft1Texture, femaleLeft2Texture;
    private Texture femaleRight1Texture, femaleRight2Texture;

    private int[][] baseGroundMap;
    private int[][] grassVariantMap;
    private int[][] treeVariantMap;
    private int[] playerHouseVariants;
    private int[] npcHouseVariants;
    private int[][] stoneVariantMap;
    private int[][] waterVariantMap;
    private Random random;

    private enum PlayerDirection {
        DOWN, UP, LEFT, RIGHT, DOWN_LEFT, DOWN_RIGHT, UP_LEFT, UP_RIGHT
    }
    private PlayerDirection playerDirection = PlayerDirection.DOWN;
    private boolean playerMoving = false;
    private float playerAnimationTime = 0f;
    private static final float ANIMATION_SPEED = 0.15f;
    private static final float PLAYER_MOVE_SPEED = 4.5f;
    private float playerMoveProgress = 0f;
    private int playerTargetX, playerTargetY;

    private static final int[][] HOUSE_POSITIONS = {
        {1, 1},
        {81, 1},
        {1, 31},
        {81, 31}
    };

    private static final int[][] HOUSE_AREAS = {
        {1, 8, 1, 8},
        {81, 88, 1, 8},
        {1, 8, 31, 38},
        {81, 88, 31, 38}
    };

    private static final int[][] NPC_HOUSE_POSITIONS = {
        {32, 40},
        {42, 40},
        {52, 40},
        {37, 50},
        {47, 50}
    };

    private static final int[][] NPC_HOUSE_AREAS = {
        {42, 46, 40, 46},  // Sebastian: (42,40) to (46,46) - 5x7 including walls
        {52, 56, 40, 46},  // Abigail: (52,40) to (56,46) - 5x7 including walls  
        {32, 36, 50, 56},  // Harvey: (32,50) to (36,56) - 5x7 including walls
        {42, 46, 50, 56},  // Lia: (42,50) to (46,56) - 5x7 including walls
        {52, 56, 50, 56}   // Robin: (52,50) to (56,56) - 5x7 including walls
    };

    private static final int[][] SHOP_POSITIONS = {
        {33, 3},
        {47, 3},
        {33, 13},
        {47, 13},
        {33, 23},
        {47, 23},
        {33, 33}
    };

    private static final int[][] SHOP_AREAS = {
        {32, 39, 2, 9},   // Blacksmith: (32,2) to (39,9) - 8x8 including walls
        {46, 53, 2, 9},   // JojaMart: (46,2) to (53,9) - 8x8 including walls
        {32, 39, 12, 19}, // PierresGeneralStore: (32,12) to (39,19) - 8x8 including walls
        {46, 53, 12, 19}, // CarpentersShop: (46,12) to (53,19) - 8x8 including walls
        {32, 39, 22, 29}, // FishShop: (32,22) to (39,29) - 8x8 including walls
        {46, 53, 22, 29}, // MarniesRanch: (46,22) to (53,29) - 8x8 including walls
        {32, 39, 32, 39}  // TheStardropSaloon: (32,32) to (39,39) - 8x8 including walls
    };

    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 90;
    private static final int MAP_HEIGHT = 60;

    private Texture menuBackgroundTexture;
    private TradeMenuController tradeController;

    public GDXGameScreen() {
        controller = new GameMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        hudCamera = new OrthographicCamera();
        shapeRenderer = new com.badlogic.gdx.graphics.glutils.ShapeRenderer();

        float worldWidth = MAP_WIDTH * TILE_SIZE;
        float worldHeight = MAP_HEIGHT * TILE_SIZE;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        camera.setToOrtho(false, screenWidth, screenHeight);
        hudCamera.setToOrtho(false, screenWidth, screenHeight);
        camera.zoom = 0.85f;

        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        camera.update();

        random = new Random();
        baseGroundMap = new int[MAP_WIDTH][MAP_HEIGHT];
        grassVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        treeVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        playerHouseVariants = new int[4];
        npcHouseVariants = new int[5];
        stoneVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        waterVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];

        loadTextures();
        loadHudAssets();

        game = App.getInstance().getCurrentGame();
        gameMap = game.getMap();

        controller.setGame(game);
        controller.setMap(gameMap);

        generateRandomMaps();

        initializePlayerPosition();

        // Initialize trade menu
        tradeController = new TradeMenuController();
        try {
            menuBackgroundTexture = new Texture(Gdx.files.internal("content/Cut/menu_background.png"));
        } catch (Exception e) {
            // Fallback: create a simple colored texture
            menuBackgroundTexture = new Texture(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        }
    }

    private void loadHudAssets() {
        clockTexture = new Texture("content/clock/Clock.png");
        hudFont = new BitmapFont(); // Use the default LibGDX font
        hudFont.setColor(Color.WHITE); // Changed to WHITE for better contrast
        hudFont.getData().setScale(1.5f); // Adjust scale as needed
    }

    private void updateTime(float delta) {
        timeAccumulator += delta;
        if (timeAccumulator >= SECONDS_PER_10_IN_GAME_MINUTES) {
            timeAccumulator -= SECONDS_PER_10_IN_GAME_MINUTES;
            if (game != null) {
                game.advanceTimeByMinutes(10);
            }
        }
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        updateTime(delta);

        Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Game World
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        renderMap();
        spriteBatch.end();

        if (showMinimap) {
            renderMinimap();
        }

        // Render HUD
        renderHud();

        stage.act(delta);
        stage.draw();
    }

    private void renderHud() {
        // --- Draw UI Backgrounds ---
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

        // Set the background color to a deep, semi-transparent blue
        shapeRenderer.setColor(0.0f, 0.0f, 0.5f, 0.5f);

        // Draw background for weather and season text
        float infoBgX = 10;
        float infoBgY = Gdx.graphics.getHeight() - 70;
        float infoBgWidth = 220; // Adjust width as needed
        float infoBgHeight = 60;
        shapeRenderer.rect(infoBgX, infoBgY, infoBgWidth, infoBgHeight);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // --- Draw Text and Textures ---
        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();

        // Draw the clock texture
        float screenWidth = Gdx.graphics.getWidth();
        float clockWidth = clockTexture.getWidth() * 1f;
        float clockHeight = clockTexture.getHeight() * 1f;
        float clockX = screenWidth - clockWidth - 20;
        float clockY = Gdx.graphics.getHeight() - clockHeight - 20;
        spriteBatch.draw(clockTexture, clockX, clockY, clockWidth, clockHeight);

        // --- Draw Text on top of backgrounds ---
        // Make font black for the clock text as it's on a light background
        hudFont.setColor(Color.BLACK);
        Date date = game.getDate();
        String dateString = date.getCurrentWeekday().name().substring(0, 3) + ". " + date.getCurrentDay();
        hudFont.draw(spriteBatch, dateString, clockX + 160, clockY + 210);

        Time time = game.getTime();
        int hour = time.getHour();
        String ampm = hour < 12 || hour >= 24 ? "am" : "pm";
        if (hour == 0) {
            hour = 12;
        } else if (hour > 12) {
            if (hour < 24) {
                hour -= 12;
            } else {
                hour -= 24;
                if (hour == 0) hour = 12;
            }
        }
        String timeString = String.format("%d:%02d %s", hour, time.getMinute(), ampm);
        hudFont.draw(spriteBatch, timeString, clockX + 148, clockY + 120);

        // --- NEW: Draw the player's balance ---
        Player player = game.getCurrentPlayer();
        if (player != null) {
            String balanceString = String.valueOf(player.getBankAccount().getBalance());
            // You might need to adjust the X and Y coordinates to perfectly align the text
            hudFont.draw(spriteBatch, balanceString, clockX + 140, clockY + 40);
        }
        // --- END NEW ---

        // Revert font color to white for the dark background
        hudFont.setColor(Color.WHITE);
        String seasonString = "Season: " + date.getCurrentSeason().name();
        hudFont.draw(spriteBatch, seasonString, 20, Gdx.graphics.getHeight() - 20);

        String weatherString = "Weather: " + game.getTodayWeather().name();
        hudFont.draw(spriteBatch, weatherString, 20, Gdx.graphics.getHeight() - 50);

        spriteBatch.end();
    }

    private void initializePlayerPosition() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i).getPlayer();
            if (player != null) {
                if (player.originX() == 0 && player.originY() == 0) {
                    if (i < HOUSE_POSITIONS.length) {
                        player.setOriginX(HOUSE_POSITIONS[i][0]);
                        player.setOriginY(HOUSE_POSITIONS[i][1] + 9);
                    }
                }

                player.setCurrentX(player.originX());
                player.setCurrentY(player.originY());
            }
        }

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null) {
            playerTargetX = currentPlayer.originX();
            playerTargetY = currentPlayer.originY();
        }
    }

    private void loadTextures() {
        ground1Texture = new Texture("content/Cut/map_elements/ground1.png");
        ground2Texture = new Texture("content/Cut/map_elements/ground2.png");
        grass1Texture = new Texture("content/Cut/map_elements/grass1.png");
        grass2Texture = new Texture("content/Cut/map_elements/grass2.png");
        shoveledTexture = new Texture("content/Cut/map_elements/shoveled.png");
        tree1Texture = new Texture("content/Cut/map_elements/tree1.png");
        tree2Texture = new Texture("content/Cut/map_elements/tree2.png");
        tree3Texture = new Texture("content/Cut/map_elements/tree3.png");
        house1Texture = new Texture("content/Cut/map_elements/player_house1.png");
        house2Texture = new Texture("content/Cut/map_elements/player_house2.png");
        house3Texture = new Texture("content/Cut/map_elements/player_house3.png");
        npcHouse1Texture = new Texture("content/Cut/map_elements/npc_house1.png");
        npcHouse2Texture = new Texture("content/Cut/map_elements/npc_house2.png");
        npcHouse3Texture = new Texture("content/Cut/map_elements/npc_house3.png");
        npcHouse4Texture = new Texture("content/Cut/map_elements/npc_house4.png");
        npcHouse5Texture = new Texture("content/Cut/map_elements/npc_house5.png");
        bushTexture = new Texture("content/Cut/map_elements/bush.png");
        stone1Texture = new Texture("content/Cut/map_elements/stone1.png");
        stone2Texture = new Texture("content/Cut/map_elements/stone2.png");
        copperStoneTexture = new Texture("content/Cut/map_elements/copper_stone.png");
        ironStoneTexture = new Texture("content/Cut/map_elements/iron_stone.png");
        goldStoneTexture = new Texture("content/Cut/map_elements/gold_stone.png");
        iridiumStoneTexture = new Texture("content/Cut/map_elements/iridium_stone.png");
        jewelStoneTexture = new Texture("content/Cut/map_elements/jewel_stone.png");

        try {
            blacksmithTexture = new Texture("content/Cut/map_elements/blacksmith.png");
            jojamartTexture = new Texture("content/Cut/map_elements/jojamart.png");
            pierresShopTexture = new Texture("content/Cut/map_elements/pierres_shop.png");
            carpentersShopTexture = new Texture("content/Cut/map_elements/carpenters_shop.png");
            fishShopTexture = new Texture("content/Cut/map_elements/fish_shop.png");
            ranchTexture = new Texture("content/Cut/map_elements/marines_ranch.png");
            saloonTexture = new Texture("content/Cut/map_elements/stardrop_saloon.png");
        } catch (Exception e) {
            blacksmithTexture = ground1Texture;
            jojamartTexture = ground1Texture;
            pierresShopTexture = ground1Texture;
            carpentersShopTexture = ground1Texture;
            fishShopTexture = ground1Texture;
            ranchTexture = ground1Texture;
            saloonTexture = ground1Texture;
        }

        lake1Texture = new Texture("content/Cut/map_elements/lake1.png");
        lake2Texture = new Texture("content/Cut/map_elements/lake2.png");
        lake3Texture = new Texture("content/Cut/map_elements/lake3.png");

        maleIdleTexture = new Texture("content/Cut/player/male_idle.png");
        maleDown1Texture = new Texture("content/Cut/player/male_down1.png");
        maleDown2Texture = new Texture("content/Cut/player/male_down2.png");
        maleUp1Texture = new Texture("content/Cut/player/male_up1.png");
        maleUp2Texture = new Texture("content/Cut/player/male_up2.png");
        maleLeft1Texture = new Texture("content/Cut/player/male_left1.png");
        maleLeft2Texture = new Texture("content/Cut/player/male_left2.png");
        maleRight1Texture = new Texture("content/Cut/player/male_right1.png");
        maleRight2Texture = new Texture("content/Cut/player/male_right2.png");

        femaleIdleTexture = new Texture("content/Cut/player/female_idle.png");
        femaleDown1Texture = new Texture("content/Cut/player/female_down1.png");
        femaleDown2Texture = new Texture("content/Cut/player/female_down2.png");
        femaleUp1Texture = new Texture("content/Cut/player/female_up1.png");
        femaleUp2Texture = new Texture("content/Cut/player/female_up2.png");
        femaleLeft1Texture = new Texture("content/Cut/player/female_left1.png");
        femaleLeft2Texture = new Texture("content/Cut/player/female_left2.png");
        femaleRight1Texture = new Texture("content/Cut/player/female_right1.png");
        femaleRight2Texture = new Texture("content/Cut/player/female_right2.png");
    }

    private void generateRandomMaps() {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                baseGroundMap[x][y] = random.nextInt(2);
                grassVariantMap[x][y] = random.nextInt(2);
                treeVariantMap[x][y] = random.nextInt(3);
                stoneVariantMap[x][y] = random.nextInt(2);

                float waterRandom = random.nextFloat();
                if (waterRandom < 0.6f) {
                    waterVariantMap[x][y] = 0;
                } else if (waterRandom < 0.8f) {
                    waterVariantMap[x][y] = 1;
                } else {
                    waterVariantMap[x][y] = 2;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            playerHouseVariants[i] = random.nextInt(3);
        }

        for (int i = 0; i < 5; i++) {
            npcHouseVariants[i] = i;
        }
    }

    private void handleInput(float delta) {
        handleTradeMenuToggle();
        
        // Only handle game input if trade menu is not showing
        if (!showTradeMenu) {
            handleMinimapToggle();
            handleTurnSwitching();
            handlePlayerMovement(delta);
            handleCameraMovement(delta);
        }
    }
    
    private void handleTradeMenuToggle() {
        boolean tKeyCurrentlyPressed = Gdx.input.isKeyPressed(Input.Keys.T);
        
        if (tKeyCurrentlyPressed && !tKeyPressed) {
            toggleTradeMenu();
        }
        
        tKeyPressed = tKeyCurrentlyPressed;
    }
    
    private void toggleTradeMenu() {
        if (showTradeMenu) {
            showTradeMenu = false;
            if (tradeMenuTable != null) {
                tradeMenuTable.remove();
                tradeMenuTable = null;
            }
        } else {
            showTradeMenu = true;
            checkForNewTrades();
            createTradeMenuUI();
        }
    }

    private boolean isPlayerWalkable(int x, int y, Player currentPlayer) {
        Tile tile = gameMap.getTiles()[x][y];
        if (tile == null) {
            return false;
        }

        if (!tile.getType().isReachable()) {
            return false;
        }

        Player tileOwner = tile.getOwner();

        return tileOwner == null ||
               tileOwner.equals(currentPlayer) ||
               (currentPlayer.getSpouse() != null && tileOwner.equals(currentPlayer.getSpouse()));
    }

    private void handleMinimapToggle() {
        boolean mKeyCurrentlyPressed = Gdx.input.isKeyPressed(Input.Keys.M);

        if (mKeyCurrentlyPressed && !mKeyPressed) {
            showMinimap = !showMinimap;
        }

        mKeyPressed = mKeyCurrentlyPressed;
    }

    private boolean nKeyPressed = false;

    private void handleTurnSwitching() {
        boolean nKeyCurrentlyPressed = Gdx.input.isKeyPressed(Input.Keys.N);

        if (nKeyCurrentlyPressed && !nKeyPressed) {
            controller.switchTurn();

            cameraFollowsPlayer = true;

            Player newCurrentPlayer = game.getCurrentPlayer();
            if (newCurrentPlayer != null) {
                updateCameraToFollowPlayer(newCurrentPlayer);
            }

            playerMoving = false;
            playerMoveProgress = 0f;
        }

        nKeyPressed = nKeyCurrentlyPressed;
    }

    private void handleCameraMovement(float delta) {
        Player currentPlayer = game.getCurrentPlayer();

        boolean manualControl = Gdx.input.isKeyPressed(Input.Keys.I) ||
                               Gdx.input.isKeyPressed(Input.Keys.K) ||
                               Gdx.input.isKeyPressed(Input.Keys.J) ||
                               Gdx.input.isKeyPressed(Input.Keys.L);

        if (manualControl) {
            cameraFollowsPlayer = false;

            float movement = cameraSpeed * delta;

            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                camera.position.y += movement;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.K)) {
                camera.position.y -= movement;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.J)) {
                camera.position.x -= movement;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                camera.position.x += movement;
            }
        } else if (currentPlayer != null) {
            cameraFollowsPlayer = true;
            updateCameraToFollowPlayer(currentPlayer);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
        }

        camera.zoom = Math.max(0.1f, Math.min(camera.zoom, 3.0f));

        constrainCameraToMapBounds();
    }

    private void updateCameraToFollowPlayer(Player player) {
        float playerX = player.currentX();
        float playerY = player.currentY();

        if (playerMoving && playerMoveProgress < 1.0f) {
            float startX = player.currentX();
            float startY = player.currentY();
            float endX = playerTargetX;
            float endY = playerTargetY;

            playerX = startX + (endX - startX) * playerMoveProgress;
            playerY = startY + (endY - startY) * playerMoveProgress;
        }

        float worldX = playerX * TILE_SIZE;
        float worldY = (MAP_HEIGHT - 1 - playerY) * TILE_SIZE;

        camera.position.set(worldX, worldY, 0);
    }

    private void constrainCameraToMapBounds() {
        float worldWidth = MAP_WIDTH * TILE_SIZE;
        float worldHeight = MAP_HEIGHT * TILE_SIZE;

        float cameraHalfWidth = camera.viewportWidth * camera.zoom / 2f;
        float cameraHalfHeight = camera.viewportHeight * camera.zoom / 2f;

        camera.position.x = Math.max(cameraHalfWidth, Math.min(camera.position.x, worldWidth - cameraHalfWidth));
        camera.position.y = Math.max(cameraHalfHeight, Math.min(camera.position.y, worldHeight - cameraHalfHeight));
    }

    @Override
    public void show() {
    }

    private void renderMap() {
        Tile[][] tiles = gameMap.getTiles();

        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                float worldX = x * TILE_SIZE;
                float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;

                Tile currentTile = tiles[x] != null && tiles[x][y] != null ? tiles[x][y] : null;
                renderBaseGround(x, y, worldX, worldY, currentTile);

                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    TileType tileType = tile.getType();

                    if (tileType != TileType.Tree) {
                        renderTileOverlay(tile, x, y, worldX, worldY);
                    }
                }
            }
        }

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    TileType tileType = tile.getType();
                    
                    if (tileType == TileType.House || tileType == TileType.Wall) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderHouseSprite(x, y, worldX, worldY);
                        
                        // Also check if this Wall tile belongs to an NPC house area or shop area
                        if (tileType == TileType.Wall) {
                            // Only render NPC house if this wall belongs to an NPC house area
                            int npcIndex = getNPCIndexForHouse(x, y);
                            if (npcIndex != -1) {
                                renderNPCHouseSprite(x, y, worldX, worldY);
                            }
                            
                            // Only render shop if this wall belongs to a shop area
                            int shopIndex = getShopIndex(x, y);
                            if (shopIndex != -1) {
                                renderShopSprite(x, y, worldX, worldY);
                            }
                        }
                    } else if (tileType == TileType.NPCHouse) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderNPCHouseSprite(x, y, worldX, worldY);
                    } else if (tileType == TileType.Shop) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderShopSprite(x, y, worldX, worldY);
                    }
                }
            }
        }

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    if (tile.getType() == TileType.Tree) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderTreeSprite(x, y, worldX, worldY);
                    }
                }
            }
        }

        renderPlayer();
    }

    private void renderPlayer() {
        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player == null) {
                continue;
            }

            float playerX = player.currentX();
            float playerY = player.currentY();

            Player currentPlayer = game.getCurrentPlayer();
            if (player.equals(currentPlayer) && playerMoving && playerMoveProgress < 1.0f) {
                float startX = player.currentX();
                float startY = player.currentY();
                float endX = playerTargetX;
                float endY = playerTargetY;

                playerX = startX + (endX - startX) * playerMoveProgress;
                playerY = startY + (endY - startY) * playerMoveProgress;
            }

            if (playerX < 0 || playerX >= MAP_WIDTH || playerY < 0 || playerY >= MAP_HEIGHT) {
                continue;
            }

            float worldX = playerX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - playerY) * TILE_SIZE;

            Texture playerTexture = getPlayerTexture(player);

            float playerWidth = playerTexture.getWidth() * 2;
            float playerHeight = playerTexture.getHeight() * 2;
            float renderX = worldX + (TILE_SIZE - playerWidth) / 2f;
            float renderY = worldY;

            if (player.equals(currentPlayer)) {
                spriteBatch.draw(playerTexture, renderX, renderY, playerWidth, playerHeight);
            } else {
                spriteBatch.setColor(1f, 1f, 1f, 0.7f);
                spriteBatch.draw(playerTexture, renderX, renderY, playerWidth, playerHeight);
                spriteBatch.setColor(1f, 1f, 1f, 1f);
            }
        }
    }

    private Texture getPlayerTexture(Player player) {
        boolean isMale = player.getGender().name().equals("Male");
        Player currentPlayer = game.getCurrentPlayer();

        if (!player.equals(currentPlayer) || !playerMoving) {
            return isMale ? maleIdleTexture : femaleIdleTexture;
        }

        int animFrame = ((int) (playerAnimationTime / ANIMATION_SPEED)) % 2;

        switch (playerDirection) {
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                if (isMale) {
                    return animFrame == 0 ? maleDown1Texture : maleDown2Texture;
                } else {
                    return animFrame == 0 ? femaleDown1Texture : femaleDown2Texture;
                }

            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                if (isMale) {
                    return animFrame == 0 ? maleUp1Texture : maleUp2Texture;
                } else {
                    return animFrame == 0 ? femaleUp1Texture : femaleUp2Texture;
                }

            case LEFT:
                if (isMale) {
                    return animFrame == 0 ? maleLeft1Texture : maleLeft2Texture;
                } else {
                    return animFrame == 0 ? femaleLeft1Texture : femaleLeft2Texture;
                }

            case RIGHT:
                if (isMale) {
                    return animFrame == 0 ? maleRight1Texture : maleRight2Texture;
                } else {
                    return animFrame == 0 ? femaleRight1Texture : femaleRight2Texture;
                }

            default:
                return isMale ? maleIdleTexture : femaleIdleTexture;
        }
    }

    private void renderBaseGround(int x, int y, float worldX, float worldY, Tile tile) {
        if (tile != null && tile.getType() == TileType.Water) {
            return;
        }

        Texture groundTexture = baseGroundMap[x][y] == 0 ? ground1Texture : ground2Texture;
        spriteBatch.draw(groundTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
    }

    private void renderTileOverlay(Tile tile, int tileX, int tileY, float worldX, float worldY) {
        TileType tileType = tile.getType();

        switch (tileType) {
            case Water:
                Texture waterTexture;
                switch (waterVariantMap[tileX][tileY]) {
                    case 0: waterTexture = lake1Texture; break;
                    case 1: waterTexture = lake2Texture; break;
                    case 2: waterTexture = lake3Texture; break;
                    default: waterTexture = lake1Texture; break;
                }
                spriteBatch.draw(waterTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Grass:
                Texture grassTexture = grassVariantMap[tileX][tileY] == 0 ? grass1Texture : grass2Texture;
                spriteBatch.draw(grassTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Shoveled:
                spriteBatch.draw(shoveledTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Bush:
                spriteBatch.draw(bushTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Stone:
                Texture stoneTexture = stoneVariantMap[tileX][tileY] == 0 ? stone1Texture : stone2Texture;
                spriteBatch.draw(stoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case CopperStone:
                spriteBatch.draw(copperStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case IronStone:
                spriteBatch.draw(ironStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case GoldStone:
                spriteBatch.draw(goldStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case IridiumStone:
                spriteBatch.draw(iridiumStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case JewelStone:
                spriteBatch.draw(jewelStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;

            case Tree:
                break;

            case House:
                break;

            case NPCHouse:
                break;

            case Wall:
                break;

            case Shop:
                break;

            default:
                break;
        }
    }

    private void renderTreeSprite(int tileX, int tileY, float worldX, float worldY) {
        Texture treeTexture;
        switch (treeVariantMap[tileX][tileY]) {
            case 0: treeTexture = tree1Texture; break;
            case 1: treeTexture = tree2Texture; break;
            case 2: treeTexture = tree3Texture; break;
            default: treeTexture = tree1Texture; break;
        }

        float treeWidth = treeTexture.getWidth();
        float treeHeight = treeTexture.getHeight();

        float treeX = worldX + (TILE_SIZE - treeWidth) / 2f;
        float treeY = worldY;

        spriteBatch.draw(treeTexture, treeX, treeY, treeWidth, treeHeight);
    }

    private void renderHouseSprite(int tileX, int tileY, float worldX, float worldY) {
        int playerIndex = getPlayerIndexForHouse(tileX, tileY);
        if (playerIndex == -1) return;

        Texture houseTexture;
        switch (playerHouseVariants[playerIndex]) {
            case 0: houseTexture = house1Texture; break;
            case 1: houseTexture = house2Texture; break;
            case 2: houseTexture = house3Texture; break;
            default: houseTexture = house1Texture; break;
        }

        int[] houseArea = HOUSE_AREAS[playerIndex];
        int houseStartX = houseArea[0];
        int houseStartY = houseArea[2];  // This is the top row of the house area
        int houseEndY = houseArea[3];    // This is the bottom row of the house area

        // Only render the house image once at the top-left corner of the house area
        if (tileX == houseStartX && tileY == houseStartY) {
            // Calculate the size to cover the entire 8x8 house area
            float houseWidth = 8 * TILE_SIZE;  // 8 tiles wide
            float houseHeight = 8 * TILE_SIZE; // 8 tiles tall
            
            // Calculate the bottom-left position for the house image
            // The worldY for houseStartY gives us the screen position of the top row
            // We need to move down by (8-1) tiles to get to the bottom of the house area
            float houseX = worldX;
            float houseY = worldY - (7 * TILE_SIZE); // Move down 7 tiles from the top tile
            
            spriteBatch.draw(houseTexture, houseX, houseY, houseWidth, houseHeight);
        }
    }

    private void renderNPCHouseSprite(int tileX, int tileY, float worldX, float worldY) {
        int npcIndex = getNPCIndexForHouse(tileX, tileY);
        if (npcIndex == -1) {
            return;
        }

        Texture npcHouseTexture;
        switch (npcHouseVariants[npcIndex]) {
            case 0: npcHouseTexture = npcHouse1Texture; break;
            case 1: npcHouseTexture = npcHouse2Texture; break;
            case 2: npcHouseTexture = npcHouse3Texture; break;
            case 3: npcHouseTexture = npcHouse4Texture; break;
            case 4: npcHouseTexture = npcHouse5Texture; break;
            default: npcHouseTexture = npcHouse1Texture; break;
        }

        int[] npcHouseArea = NPC_HOUSE_AREAS[npcIndex];
        int houseStartX = npcHouseArea[0];
        int houseStartY = npcHouseArea[2];  // This is the top row of the house area

        // Only render the house image once at the top-left corner of the house area
        if (tileX == houseStartX && tileY == houseStartY) {
            // Calculate the size to cover the entire 5x7 NPC house area
            float npcHouseWidth = 5 * TILE_SIZE;  // 5 tiles wide
            float npcHouseHeight = 7 * TILE_SIZE; // 7 tiles tall
            
            // Calculate the bottom-left position for the NPC house image
            // The worldY for houseStartY gives us the screen position of the top row
            // We need to move down by (7-1) tiles to get to the bottom of the house area
            float npcHouseX = worldX;
            float npcHouseY = worldY - (6 * TILE_SIZE); // Move down 6 tiles from the top tile

            spriteBatch.draw(npcHouseTexture, npcHouseX, npcHouseY, npcHouseWidth, npcHouseHeight);
        }
    }

    private void renderShopSprite(int tileX, int tileY, float worldX, float worldY) {
        int shopIndex = getShopIndex(tileX, tileY);
        if (shopIndex == -1) {
            renderSimpleShopSprite(tileX, tileY, worldX, worldY);
            return;
        }

        Texture shopTexture;
        switch (shopIndex) {
            case 0: shopTexture = blacksmithTexture; break;
            case 1: shopTexture = jojamartTexture; break;
            case 2: shopTexture = pierresShopTexture; break;
            case 3: shopTexture = carpentersShopTexture; break;
            case 4: shopTexture = fishShopTexture; break;
            case 5: shopTexture = ranchTexture; break;
            case 6: shopTexture = saloonTexture; break;
            default: shopTexture = ground1Texture; break;
        }

        int[] shopArea = SHOP_AREAS[shopIndex];
        int shopStartX = shopArea[0];
        int shopStartY = shopArea[2];

        if (tileX == shopStartX && tileY == shopStartY) {
            // Calculate the size to cover the entire 8x8 shop area
            float shopWidth = 8 * TILE_SIZE;  // 8 tiles wide
            float shopHeight = 8 * TILE_SIZE; // 8 tiles tall
            
            // Calculate the bottom-left position for the shop image
            // The worldY for shopStartY gives us the screen position of the top row
            // We need to move down by (8-1) tiles to get to the bottom of the shop area
            float shopX = worldX;
            float shopY = worldY - (7 * TILE_SIZE); // Move down 7 tiles from the top tile

            spriteBatch.draw(shopTexture, shopX, shopY, shopWidth, shopHeight);
        }
    }

    private void renderSimpleShopSprite(int tileX, int tileY, float worldX, float worldY) {
        Texture shopTexture = ground1Texture;

        float shopWidth = TILE_SIZE;
        float shopHeight = TILE_SIZE;

        float shopX = worldX;
        float shopY = worldY;

        spriteBatch.draw(shopTexture, shopX, shopY, shopWidth, shopHeight);
    }

    private int getPlayerIndexForHouse(int x, int y) {
        for (int i = 0; i < HOUSE_AREAS.length; i++) {
            int[] area = HOUSE_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1;
    }

    private int getNPCIndexForHouse(int x, int y) {
        for (int i = 0; i < NPC_HOUSE_AREAS.length; i++) {
            int[] area = NPC_HOUSE_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1;
    }

    private int getShopIndex(int x, int y) {
        for (int i = 0; i < SHOP_AREAS.length; i++) {
            int[] area = SHOP_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1;
    }

    private void renderMinimap() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float minimapSize = Math.min(screenWidth, screenHeight) * 0.4f;

        float minimapX = (screenWidth - minimapSize) / 2f;
        float minimapY = (screenHeight - minimapSize) / 2f;

        renderMinimapBackgroundAndBorder(minimapX, minimapY, minimapSize, minimapSize);

        spriteBatch.begin();

        OrthographicCamera minimapCamera = new OrthographicCamera();

        float zoomToFitWidth = minimapSize / (MAP_WIDTH * TILE_SIZE);
        float zoomToFitHeight = minimapSize / (MAP_HEIGHT * TILE_SIZE);

        float minimapZoom = Math.min(zoomToFitWidth, zoomToFitHeight);

        minimapZoom *= 0.80f;

        minimapCamera.setToOrtho(false, MAP_WIDTH * TILE_SIZE, MAP_HEIGHT * TILE_SIZE);
        minimapCamera.zoom = 1f / minimapZoom;
        minimapCamera.position.set((MAP_WIDTH * TILE_SIZE) / 2f, (MAP_HEIGHT * TILE_SIZE) / 2f, 0);
        minimapCamera.update();

        minimapCamera.viewportWidth = minimapSize;
        minimapCamera.viewportHeight = minimapSize;
        minimapCamera.position.set(minimapX + minimapSize / 2f, minimapY + minimapSize / 2f, 0);
        minimapCamera.update();

        spriteBatch.setProjectionMatrix(minimapCamera.combined);

        renderMinimapContent();

        spriteBatch.end();

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    private void renderMinimapContent() {
        Tile[][] tiles = gameMap.getTiles();

        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];

                    float worldX = x * TILE_SIZE;
                    float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;

                    Texture minimapTexture = getMinimapTexture(tile.getType());
                    if (minimapTexture != null) {
                        spriteBatch.draw(minimapTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null) {
            float playerX = currentPlayer.currentX();
            float playerY = currentPlayer.currentY();

            float startX = currentPlayer.currentX();
            float startY = currentPlayer.currentY();

            float worldX = playerX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - playerY) * TILE_SIZE;

            spriteBatch.setColor(1f, 1f, 0f, 1f);
            spriteBatch.draw(ground1Texture, worldX, worldY, TILE_SIZE, TILE_SIZE);
            spriteBatch.setColor(1f, 1f, 1f, 1f);
        }
    }

    private Texture getMinimapTexture(TileType tileType) {
        switch (tileType) {
            case Water:
                return lake1Texture;
            case Grass:
                return grass1Texture;
            case Earth:
                return ground1Texture;
            case Stone:
                return stone1Texture;
            case Tree:
                return tree1Texture;
            case House:
                return house1Texture;
            case NPCHouse:
                return npcHouse1Texture;
            case Shop:
                return blacksmithTexture;
            default:
                return ground1Texture;
        }
    }

    private void renderMinimapBackgroundAndBorder(float x, float y, float width, float height) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

        OrthographicCamera uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiCamera.update();
        shapeRenderer.setProjectionMatrix(uiCamera.combined);

        shapeRenderer.setColor(0, 0, 0, 0.8f);
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.end();

        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        hudCamera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        clockTexture.dispose();
        hudFont.dispose();

        ground1Texture.dispose();
        ground2Texture.dispose();
        grass1Texture.dispose();
        grass2Texture.dispose();
        shoveledTexture.dispose();
        tree1Texture.dispose();
        tree2Texture.dispose();
        tree3Texture.dispose();
        house1Texture.dispose();
        house2Texture.dispose();
        house3Texture.dispose();
        npcHouse1Texture.dispose();
        npcHouse2Texture.dispose();
        npcHouse3Texture.dispose();
        npcHouse4Texture.dispose();
        npcHouse5Texture.dispose();
        bushTexture.dispose();
        stone1Texture.dispose();
        stone2Texture.dispose();
        copperStoneTexture.dispose();
        ironStoneTexture.dispose();
        goldStoneTexture.dispose();
        iridiumStoneTexture.dispose();
        jewelStoneTexture.dispose();

        blacksmithTexture.dispose();
        jojamartTexture.dispose();
        pierresShopTexture.dispose();
        carpentersShopTexture.dispose();
        fishShopTexture.dispose();
        ranchTexture.dispose();
        saloonTexture.dispose();

        lake1Texture.dispose();
        lake2Texture.dispose();
        lake3Texture.dispose();

        maleIdleTexture.dispose();
        maleDown1Texture.dispose();
        maleDown2Texture.dispose();
        maleUp1Texture.dispose();
        maleUp2Texture.dispose();
        maleLeft1Texture.dispose();
        maleLeft2Texture.dispose();
        maleRight1Texture.dispose();
        maleRight2Texture.dispose();

        femaleIdleTexture.dispose();
        femaleDown1Texture.dispose();
        femaleDown2Texture.dispose();
        femaleUp1Texture.dispose();
        femaleUp2Texture.dispose();
        femaleLeft1Texture.dispose();
        femaleLeft2Texture.dispose();
        femaleRight1Texture.dispose();
        femaleRight2Texture.dispose();
        
        if (menuBackgroundTexture != null) {
            menuBackgroundTexture.dispose();
        }
    }
    
    // Missing handlePlayerMovement method
    private void handlePlayerMovement(float delta) {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        playerAnimationTime += delta;

        if (playerMoving && playerMoveProgress < 1.0f) {
            playerMoveProgress += PLAYER_MOVE_SPEED * delta;
            if (playerMoveProgress >= 1.0f) {
                playerMoveProgress = 1.0f;
                currentPlayer.setCurrentX(playerTargetX);
                currentPlayer.setCurrentY(playerTargetY);
                playerMoving = false;
            }
            return;
        }

        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        int newX = currentPlayer.currentX();
        int newY = currentPlayer.currentY();
        PlayerDirection newDirection = playerDirection;

        if (upPressed && leftPressed) {
            newDirection = PlayerDirection.UP_LEFT;
            newX -= 1;
            newY -= 1;
        } else if (upPressed && rightPressed) {
            newDirection = PlayerDirection.UP_RIGHT;
            newX += 1;
            newY -= 1;
        } else if (downPressed && leftPressed) {
            newDirection = PlayerDirection.DOWN_LEFT;
            newX -= 1;
            newY += 1;
        } else if (downPressed && rightPressed) {
            newDirection = PlayerDirection.DOWN_RIGHT;
            newX += 1;
            newY += 1;
        } else if (upPressed) {
            newDirection = PlayerDirection.UP;
            newY -= 1;
        } else if (downPressed) {
            newDirection = PlayerDirection.DOWN;
            newY += 1;
        } else if (leftPressed) {
            newDirection = PlayerDirection.LEFT;
            newX -= 1;
        } else if (rightPressed) {
            newDirection = PlayerDirection.RIGHT;
            newX += 1;
        }

        playerDirection = newDirection;

        if (newX != currentPlayer.currentX() || newY != currentPlayer.currentY()) {
            if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT) {
                if (isPlayerWalkable(newX, newY, currentPlayer)) {
                    playerTargetX = newX;
                    playerTargetY = newY;
                    playerMoving = true;
                    playerMoveProgress = 0f;
                    playerAnimationTime = 0f;
                }
            }
        } else {
            playerMoving = false;
            playerAnimationTime = 0f;
        }
    }
    
    // Trade menu methods
    private void checkForNewTrades() {
        tradeErrorMessage = ""; // Clear any previous error messages
        Result result = tradeController.listTrades();
        
        if (result.isSuccessful()) {
            String message = result.Message();
            newTradeNotifications.clear();
            
            if (message.trim().isEmpty()) {
                currentTradeMenuState = TradeMenuState.MAIN_MENU;
                return;
            }
            
            // Split by trade separator
            String[] trades = message.split("-----------------------");
            
            for (int i = 0; i < trades.length; i++) {
                String tradeEntry = trades[i].trim();
                if (tradeEntry.isEmpty()) continue;
                
                String[] lines = tradeEntry.split("\n");
                String sender = null;
                String receiver = null;
                String tradeType = "Trade";
                
                for (String line : lines) {
                    line = line.trim();
                    
                    if (line.startsWith("Buy:")) {
                        tradeType = "Buy Request";
                    } else if (line.startsWith("Trade Offer:")) {
                        tradeType = "Trade Offer";
                    } else if (line.startsWith("Sender:")) {
                        sender = line.substring(7).trim(); // Remove "Sender:" prefix
                    } else if (line.startsWith("Receiver:")) {
                        receiver = line.substring(9).trim(); // Remove "Receiver:" prefix
                    }
                }
                
                // Only show notifications for trades where current player is the receiver
                if (receiver != null && receiver.equals(game.getCurrentPlayer().getUsername()) && sender != null) {
                    String notificationText = "New " + tradeType + " from " + sender;
                    newTradeNotifications.add(notificationText);
                }
            }
            
            if (!newTradeNotifications.isEmpty()) {
                currentTradeMenuState = TradeMenuState.NEW_TRADE_NOTIFICATIONS;
            } else {
                currentTradeMenuState = TradeMenuState.MAIN_MENU;
            }
        } else {
            currentTradeMenuState = TradeMenuState.MAIN_MENU;
        }
    }
    
    private void createTradeMenuUI() {
        if (tradeMenuTable != null) {
            tradeMenuTable.remove();
        }

        tradeMenuTable = new Table();
        // Make the background 75% of screen size
        tradeMenuTable.setSize(Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.75f);
        tradeMenuTable.setPosition(
            (Gdx.graphics.getWidth() - tradeMenuTable.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - tradeMenuTable.getHeight()) / 2f
        );
        
        // Create a background drawable from the menu background texture
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(menuBackgroundTexture);
        tradeMenuTable.setBackground(backgroundDrawable);
        
        stage.addActor(tradeMenuTable);

        switch (currentTradeMenuState) {
            case NEW_TRADE_NOTIFICATIONS:
                createNewTradeNotificationsMenu();
                break;
            case MAIN_MENU:
                createMainTradeMenu();
                break;
            case PLAYER_SELECTION:
                createPlayerSelectionMenu();
                break;
            case TRADE_TYPE_SELECTION:
                createTradeTypeSelectionMenu();
                break;
            case TRADE_DETAILS:
                createTradeDetailsMenu();
                break;
            case ACTIVE_TRADES:
                createActiveTradesMenu();
                break;
            case TRADE_HISTORY:
                createTradeHistoryMenu();
                break;
        }
    }
    
    private void createNewTradeNotificationsMenu() {
        tradeMenuTable.clear();
        
        Label titleLabel = new Label("New Trade Notifications", skin);
        titleLabel.setFontScale(1.5f);
        tradeMenuTable.add(titleLabel).padBottom(20).row();
        
        for (int i = 0; i < newTradeNotifications.size(); i++) {
            String notification = newTradeNotifications.get(i);
            
            Label notificationLabel = new Label(notification, skin);
            tradeMenuTable.add(notificationLabel).padBottom(10).row();
        }
        
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message
                currentTradeMenuState = TradeMenuState.MAIN_MENU;
                createMainTradeMenu();
            }
        });
        tradeMenuTable.add(continueButton).padTop(20);
    }
    
    private void createMainTradeMenu() {
        tradeMenuTable.clear();
        
        Label titleLabel = new Label("Trade Menu", skin);
        titleLabel.setFontScale(1.5f);
        tradeMenuTable.add(titleLabel).padBottom(20).row();
        
        // Show error message if exists
        if (!tradeErrorMessage.isEmpty()) {
            Label errorLabel = new Label(tradeErrorMessage, skin);
            errorLabel.setColor(Color.RED);
            errorLabel.setFontScale(1.1f);
            tradeMenuTable.add(errorLabel).padBottom(15).row();
        }
        
        TextButton newTradeButton = new TextButton("New Trade", skin);
        newTradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message
                currentTradeMenuState = TradeMenuState.PLAYER_SELECTION;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(newTradeButton).width(200).pad(10).row();
        
        TextButton activeTradesButton = new TextButton("Active Trades", skin);
        activeTradesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message
                currentTradeMenuState = TradeMenuState.ACTIVE_TRADES;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(activeTradesButton).width(200).pad(10).row();
        
        TextButton tradeHistoryButton = new TextButton("Trade History", skin);
        tradeHistoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message
                currentTradeMenuState = TradeMenuState.TRADE_HISTORY;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(tradeHistoryButton).width(200).pad(10).row();
        
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message
                showTradeMenu = false;
                if (tradeMenuTable != null) {
                    tradeMenuTable.remove();
                    tradeMenuTable = null;
                }
            }
        });
        tradeMenuTable.add(closeButton).width(200).pad(10).row();
    }
    
    private void createPlayerSelectionMenu() {
        tradeMenuTable.clear();
        
        Label titleLabel = new Label("Select Player to Trade With", skin);
        tradeMenuTable.add(titleLabel).padBottom(20).row();

        // Add buttons for each other player in the game
        for (User user : game.getPlayers()) {
            if (!user.getPlayer().equals(game.getCurrentPlayer())) {
                TextButton playerButton = new TextButton(user.getPlayer().getUsername(), skin);
                playerButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        selectedPlayerForTrade = user.getPlayer().getUsername();
                        currentTradeMenuState = TradeMenuState.TRADE_TYPE_SELECTION;
                        createTradeMenuUI();
                    }
                });
                tradeMenuTable.add(playerButton).width(200).pad(10).row();
            }
        }

        // Add back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message
                currentTradeMenuState = TradeMenuState.MAIN_MENU;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(backButton).width(200).pad(10).row();
    }
    
    private void createTradeTypeSelectionMenu() {
        tradeMenuTable.clear();
        
        Label titleLabel = new Label("Select Trade Type", skin);
        tradeMenuTable.add(titleLabel).padBottom(20).row();
        
        Label playerLabel = new Label("Trading with: " + selectedPlayerForTrade, skin);
        tradeMenuTable.add(playerLabel).padBottom(15).row();
        
        TextButton buyRequestButton = new TextButton("Buy Request", skin);
        buyRequestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedTradeType = "Buy Request";
                currentTradeMenuState = TradeMenuState.TRADE_DETAILS;
                resetTradeFormData();
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(buyRequestButton).width(200).pad(10).row();
        
        TextButton buyOfferButton = new TextButton("Buy Offer", skin);
        buyOfferButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedTradeType = "Buy Offer";
                currentTradeMenuState = TradeMenuState.TRADE_DETAILS;
                resetTradeFormData();
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(buyOfferButton).width(200).pad(10).row();
        
        TextButton sellRequestButton = new TextButton("Trade Request", skin);
        sellRequestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedTradeType = "Trade Request";
                currentTradeMenuState = TradeMenuState.TRADE_DETAILS;
                resetTradeFormData();
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(sellRequestButton).width(200).pad(10).row();
        
        TextButton sellOfferButton = new TextButton("Trade Offer", skin);
        sellOfferButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedTradeType = "Trade Offer";
                currentTradeMenuState = TradeMenuState.TRADE_DETAILS;
                resetTradeFormData();
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(sellOfferButton).width(200).pad(10).row();
        
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message
                currentTradeMenuState = TradeMenuState.PLAYER_SELECTION;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(backButton).width(200).pad(10).row();
    }
    
    private void resetTradeFormData() {
        itemName = "";
        itemAmount = 1;
        itemPrice = 1;
        givingItemName = "";
        givingItemAmount = 1;
        receivingItemName = "";
        receivingItemAmount = 1;
        tradeErrorMessage = "";
    }
    
    private void createTradeDetailsMenu() {
        tradeMenuTable.clear();
        
        Label titleLabel = new Label(selectedTradeType + " Details", skin);
        titleLabel.setFontScale(1.2f);
        tradeMenuTable.add(titleLabel).padBottom(20).row();
        
        Label playerLabel = new Label("Trading with: " + selectedPlayerForTrade, skin);
        tradeMenuTable.add(playerLabel).padBottom(15).row();
        
        // Show error message if exists
        if (!tradeErrorMessage.isEmpty()) {
            Label errorLabel = new Label(tradeErrorMessage, skin);
            errorLabel.setColor(1, 0, 0, 1); // Red color
            tradeMenuTable.add(errorLabel).padBottom(10).row();
        }
        
        if (selectedTradeType.equals("Buy Request") || selectedTradeType.equals("Buy Offer")) {
            createBuyTradeForm();
        } else {
            createSellTradeForm();
        }
    }
    
    private void createBuyTradeForm() {
        // Item name
        Label itemNameLabel = new Label("Item Name:", skin);
        TextField itemNameField = new TextField(itemName, skin);
        itemNameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                itemName = textField.getText();
            }
        });
        tradeMenuTable.add(itemNameLabel).width(120).pad(5);
        tradeMenuTable.add(itemNameField).width(200).pad(5).row();
        
        // Item amount
        Label itemAmountLabel = new Label("Amount:", skin);
        TextField itemAmountField = new TextField(String.valueOf(itemAmount), skin);
        itemAmountField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c);
            }
        });
        itemAmountField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String text = textField.getText();
                if (text.isEmpty()) {
                    itemAmount = 1;
                    textField.setText("1");
                } else {
                    try {
                        itemAmount = Integer.parseInt(text);
                        if (itemAmount < 1) {
                            itemAmount = 1;
                            textField.setText("1");
                        }
                    } catch (NumberFormatException e) {
                        itemAmount = 1;
                        textField.setText("1");
                    }
                }
            }
        });
        TextButton amountPlusButton = new TextButton("+", skin);
        amountPlusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemAmount++;
                itemAmountField.setText(String.valueOf(itemAmount));
            }
        });
        tradeMenuTable.add(itemAmountLabel).width(120).pad(5);
        tradeMenuTable.add(itemAmountField).width(200).pad(5);
        tradeMenuTable.add(amountPlusButton).width(50).pad(5).row();
        
        // Item price
        Label itemPriceLabel = new Label("Price:", skin);
        TextField itemPriceField = new TextField(String.valueOf(itemPrice), skin);
        itemPriceField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c);
            }
        });
        itemPriceField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String text = textField.getText();
                if (text.isEmpty()) {
                    itemPrice = 1;
                    textField.setText("1");
                } else {
                    try {
                        itemPrice = Integer.parseInt(text);
                        if (itemPrice < 1) {
                            itemPrice = 1;
                            textField.setText("1");
                        }
                    } catch (NumberFormatException e) {
                        itemPrice = 1;
                        textField.setText("1");
                    }
                }
            }
        });
        TextButton pricePlusButton = new TextButton("+", skin);
        pricePlusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                itemPrice++;
                itemPriceField.setText(String.valueOf(itemPrice));
            }
        });
        tradeMenuTable.add(itemPriceLabel).width(120).pad(5);
        tradeMenuTable.add(itemPriceField).width(200).pad(5);
        tradeMenuTable.add(pricePlusButton).width(50).pad(5).row();
        
        // Submit button
        TextButton submitButton = new TextButton("Submit " + selectedTradeType, skin);
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result;
                if (selectedTradeType.equals("Buy Request")) {
                    result = tradeController.buyRequest(selectedPlayerForTrade, itemName, String.valueOf(itemAmount), String.valueOf(itemPrice));
                } else {
                    result = tradeController.buyOffer(selectedPlayerForTrade, itemName, String.valueOf(itemAmount), String.valueOf(itemPrice));
                }
                
                if (result.isSuccessful()) {
                    showTradeMenu = false;
                    if (tradeMenuTable != null) {
                        tradeMenuTable.remove();
                        tradeMenuTable = null;
                    }
                } else {
                    tradeErrorMessage = result.Message();
                    createTradeMenuUI();
                }
            }
        });
        tradeMenuTable.add(submitButton).width(250).colspan(3).pad(10).row();
        
        // Back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentTradeMenuState = TradeMenuState.TRADE_TYPE_SELECTION;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(backButton).width(250).colspan(3).pad(10).row();
    }
    
    private void createSellTradeForm() {
        // Giving item name
        Label givingItemNameLabel = new Label("Giving Item:", skin);
        TextField givingItemNameField = new TextField(givingItemName, skin);
        givingItemNameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                givingItemName = textField.getText();
            }
        });
        tradeMenuTable.add(givingItemNameLabel).width(120).pad(5);
        tradeMenuTable.add(givingItemNameField).width(200).pad(5).row();
        
        // Giving item amount
        Label givingItemAmountLabel = new Label("Giving Amount:", skin);
        TextField givingItemAmountField = new TextField(String.valueOf(givingItemAmount), skin);
        givingItemAmountField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c);
            }
        });
        givingItemAmountField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String text = textField.getText();
                if (text.isEmpty()) {
                    givingItemAmount = 1;
                    textField.setText("1");
                } else {
                    try {
                        givingItemAmount = Integer.parseInt(text);
                        if (givingItemAmount < 1) {
                            givingItemAmount = 1;
                            textField.setText("1");
                        }
                    } catch (NumberFormatException e) {
                        givingItemAmount = 1;
                        textField.setText("1");
                    }
                }
            }
        });
        TextButton givingAmountPlusButton = new TextButton("+", skin);
        givingAmountPlusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                givingItemAmount++;
                givingItemAmountField.setText(String.valueOf(givingItemAmount));
            }
        });
        tradeMenuTable.add(givingItemAmountLabel).width(120).pad(5);
        tradeMenuTable.add(givingItemAmountField).width(200).pad(5);
        tradeMenuTable.add(givingAmountPlusButton).width(50).pad(5).row();
        
        // Receiving item name
        Label receivingItemNameLabel = new Label("Receiving Item:", skin);
        TextField receivingItemNameField = new TextField(receivingItemName, skin);
        receivingItemNameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                receivingItemName = textField.getText();
            }
        });
        tradeMenuTable.add(receivingItemNameLabel).width(120).pad(5);
        tradeMenuTable.add(receivingItemNameField).width(200).pad(5).row();
        
        // Receiving item amount
        Label receivingItemAmountLabel = new Label("Receiving Amount:", skin);
        TextField receivingItemAmountField = new TextField(String.valueOf(receivingItemAmount), skin);
        receivingItemAmountField.setTextFieldFilter(new TextField.TextFieldFilter() {
            @Override
            public boolean acceptChar(TextField textField, char c) {
                return Character.isDigit(c);
            }
        });
        receivingItemAmountField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                String text = textField.getText();
                if (text.isEmpty()) {
                    receivingItemAmount = 1;
                    textField.setText("1");
                } else {
                    try {
                        receivingItemAmount = Integer.parseInt(text);
                        if (receivingItemAmount < 1) {
                            receivingItemAmount = 1;
                            textField.setText("1");
                        }
                    } catch (NumberFormatException e) {
                        receivingItemAmount = 1;
                        textField.setText("1");
                    }
                }
            }
        });
        TextButton receivingAmountPlusButton = new TextButton("+", skin);
        receivingAmountPlusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receivingItemAmount++;
                receivingItemAmountField.setText(String.valueOf(receivingItemAmount));
            }
        });
        tradeMenuTable.add(receivingItemAmountLabel).width(120).pad(5);
        tradeMenuTable.add(receivingItemAmountField).width(200).pad(5);
        tradeMenuTable.add(receivingAmountPlusButton).width(50).pad(5).row();
        
        // Submit button
        TextButton submitButton = new TextButton("Submit " + selectedTradeType, skin);
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result;
                if (selectedTradeType.equals("Trade Request")) {
                    result = tradeController.tradeRequest(selectedPlayerForTrade, givingItemName, String.valueOf(givingItemAmount), receivingItemName, String.valueOf(receivingItemAmount));
                } else {
                    result = tradeController.tradeOffer(selectedPlayerForTrade, givingItemName, String.valueOf(givingItemAmount), receivingItemName, String.valueOf(receivingItemAmount));
                }
                
                if (result.isSuccessful()) {
                    showTradeMenu = false;
                    if (tradeMenuTable != null) {
                        tradeMenuTable.remove();
                        tradeMenuTable = null;
                    }
                } else {
                    tradeErrorMessage = result.Message();
                    createTradeMenuUI();
                }
            }
        });
        tradeMenuTable.add(submitButton).width(250).colspan(3).pad(10).row();
        
        // Back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentTradeMenuState = TradeMenuState.TRADE_TYPE_SELECTION;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(backButton).width(250).colspan(3).pad(10).row();
    }
    
    private void createActiveTradesMenu() {
        tradeMenuTable.clear();
        
        Label titleLabel = new Label("Active Trades", skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.GOLD);
        tradeMenuTable.add(titleLabel).padBottom(30).row();
        
        // Show error message if exists
        if (!tradeErrorMessage.isEmpty()) {
            Label errorLabel = new Label(tradeErrorMessage, skin);
            errorLabel.setColor(Color.RED);
            errorLabel.setFontScale(1.1f);
            tradeMenuTable.add(errorLabel).padBottom(15).row();
        }
        
        // Create a table to hold all trade cards
        Table tradesTable = new Table();
        
        Result result = tradeController.listTrades();
        if (result.isSuccessful() && !result.Message().trim().isEmpty()) {
            String[] trades = result.Message().split("-----------------------");
            
            for (String tradeEntry : trades) {
                if (tradeEntry.trim().isEmpty()) continue;
                
                String[] lines = tradeEntry.trim().split("\n");
                String receiver = null;
                String tradeId = null;
                String sender = null;
                String tradeType = "Unknown Trade";
                String itemInfo = "";
                
                // Parse trade info with simple trade type detection
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("Receiver:")) {
                        receiver = line.substring(9).trim();
                    } else if (line.startsWith("Sender:")) {
                        sender = line.substring(7).trim();
                    } else if (line.startsWith("Id:")) {
                        tradeId = line.substring(3).trim();
                    } else if (line.startsWith("Buy:")) {
                        // For Buy trades, we need to determine if it's request or offer
                        // This will be set after we know sender/receiver
                        tradeType = "Buy";
                    } else if (line.startsWith("Trade Offer:")) {
                        // For Trade trades, we need to determine if it's request or offer
                        // This will be set after we know sender/receiver
                        tradeType = "Trade";
                    } else if (line.contains("Item:") || line.contains("Amount:") || line.contains("Price:") || line.contains("Giving:") || line.contains("Receiving:")) {
                        if (!itemInfo.isEmpty()) itemInfo += "\n";
                        itemInfo += line;
                    }
                }
                
                // Set final trade type based on what method would have been called
                if (tradeType.equals("Buy")) {
                    if (sender != null && sender.equals(game.getCurrentPlayer().getUsername())) {
                        tradeType = "Buy Offer"; // buyOffer() method
                    } else {
                        tradeType = "Buy Request"; // buyRequest() method
                    }
                } else if (tradeType.equals("Trade")) {
                    if (sender != null && sender.equals(game.getCurrentPlayer().getUsername())) {
                        tradeType = "Trade Offer"; // tradeOffer() method
                    } else {
                        tradeType = "Trade Request"; // tradeRequest() method
                    }
                }
                
                // Create beautiful trade card
                Table tradeCard = new Table();
                tradeCard.setBackground(skin.getDrawable("default-round"));
                tradeCard.pad(15);
                
                // Trade type header with color coding
                Label typeLabel = new Label(tradeType, skin);
                typeLabel.setFontScale(1.3f);
                if (tradeType.contains("Buy")) {
                    typeLabel.setColor(Color.CYAN);
                } else if (tradeType.contains("Trade")) {
                    typeLabel.setColor(Color.MAGENTA);
                } else {
                    typeLabel.setColor(Color.CYAN);
                }
                tradeCard.add(typeLabel).colspan(2).padBottom(10).row();
                
                // Trade ID
                if (tradeId != null) {
                    Label idLabel = new Label("Trade ID: " + tradeId, skin);
                    idLabel.setColor(Color.LIGHT_GRAY);
                    tradeCard.add(idLabel).colspan(2).padBottom(5).row();
                }
                
                // Sender info
                if (sender != null) {
                    Label senderLabel = new Label("From: " + sender, skin);
                    senderLabel.setColor(Color.YELLOW);
                    tradeCard.add(senderLabel).colspan(2).padBottom(5).row();
                }
                
                // Receiver info
                if (receiver != null) {
                    Label receiverLabel = new Label("To: " + receiver, skin);
                    receiverLabel.setColor(Color.YELLOW);
                    tradeCard.add(receiverLabel).colspan(2).padBottom(8).row();
                }
                
                // Item info
                String[] itemLines = itemInfo.split("\n");
                for (String itemLine : itemLines) {
                    Label itemLabel = new Label(itemLine, skin);
                    itemLabel.setColor(Color.WHITE);
                    tradeCard.add(itemLabel).colspan(2).padBottom(3).row();
                }
                
                // Status and buttons
                if (receiver != null && receiver.equals(game.getCurrentPlayer().getUsername())) {
                    Table buttonTable = new Table();
                    
                    TextButton acceptButton = new TextButton("Accept", skin);
                    acceptButton.setColor(Color.GREEN);
                    final String finalTradeId = tradeId;
                    acceptButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Result acceptResult = tradeController.respondToTrade("accept", finalTradeId);
                            if (!acceptResult.isSuccessful()) {
                                tradeErrorMessage = acceptResult.Message();
                            } else {
                                tradeErrorMessage = "";
                            }
                            createTradeMenuUI(); // Refresh the menu
                        }
                    });
                    
                    TextButton rejectButton = new TextButton("Reject", skin);
                    rejectButton.setColor(Color.RED);
                    rejectButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Result rejectResult = tradeController.respondToTrade("reject", finalTradeId);
                            if (!rejectResult.isSuccessful()) {
                                tradeErrorMessage = rejectResult.Message();
                            } else {
                                tradeErrorMessage = "";
                            }
                            createTradeMenuUI(); // Refresh the menu
                        }
                    });
                    
                    buttonTable.add(acceptButton).width(100).pad(5);
                    buttonTable.add(rejectButton).width(100).pad(5);
                    tradeCard.add(buttonTable).colspan(2).padTop(10).row();
                } else {
                    Label waitingLabel = new Label("Waiting for response...", skin);
                    waitingLabel.setColor(Color.ORANGE);
                    waitingLabel.setFontScale(1.1f);
                    tradeCard.add(waitingLabel).colspan(2).padTop(10).row();
                }
                
                tradesTable.add(tradeCard).width(500).pad(15).row();
            }
        } else {
            Label noTradesLabel = new Label("No active trades", skin);
            noTradesLabel.setFontScale(1.2f);
            noTradesLabel.setColor(Color.GRAY);
            tradesTable.add(noTradesLabel).pad(30).row();
        }
        
        // Create scroll pane for the trades
        ScrollPane scrollPane = new ScrollPane(tradesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollBarPositions(false, true);
        
        // Add scroll pane to main table with fixed height
        tradeMenuTable.add(scrollPane).width(550).height(400).pad(10).row();
        
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tradeErrorMessage = ""; // Clear error message when going back
                currentTradeMenuState = TradeMenuState.MAIN_MENU;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(backButton).width(200).pad(20).row();
    }
    
    private void createTradeHistoryMenu() {
        tradeMenuTable.clear();
        
        Label titleLabel = new Label("Trade History", skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.GOLD);
        tradeMenuTable.add(titleLabel).padBottom(30).row();
        
        // Create a table to hold all trade cards
        Table tradesTable = new Table();
        
        Result result = tradeController.tradeHistory();
        if (result.isSuccessful() && !result.Message().trim().isEmpty()) {
            String[] trades = result.Message().split("-----------------------");
            
            for (String tradeEntry : trades) {
                if (tradeEntry.trim().isEmpty()) continue;
                
                String[] lines = tradeEntry.trim().split("\n");
                String status = "PENDING";
                String tradeId = null;
                String sender = null;
                String receiver = null;
                String tradeType = "Unknown Trade";
                String itemInfo = "";
                
                // Parse trade info with simple trade type detection
                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("Receiver:")) {
                        receiver = line.substring(9).trim();
                    } else if (line.startsWith("Sender:")) {
                        sender = line.substring(7).trim();
                    } else if (line.startsWith("Id:")) {
                        tradeId = line.substring(3).trim();
                    } else if (line.startsWith("Buy:")) {
                        // For Buy trades, we need to determine if it's request or offer
                        // This will be set after we know sender/receiver
                        tradeType = "Buy";
                    } else if (line.startsWith("Trade Offer:")) {
                        // For Trade trades, we need to determine if it's request or offer
                        // This will be set after we know sender/receiver
                        tradeType = "Trade";
                    } else if (line.contains("Item:") || line.contains("Amount:") || line.contains("Price:") || line.contains("Giving:") || line.contains("Receiving:")) {
                        if (!itemInfo.isEmpty()) itemInfo += "\n";
                        itemInfo += line;
                    }
                }
                
                // Set final trade type based on what method would have been called
                if (tradeType.equals("Buy")) {
                    if (sender != null && sender.equals(game.getCurrentPlayer().getUsername())) {
                        tradeType = "Buy Offer"; // buyOffer() method
                    } else {
                        tradeType = "Buy Request"; // buyRequest() method
                    }
                } else if (tradeType.equals("Trade")) {
                    if (sender != null && sender.equals(game.getCurrentPlayer().getUsername())) {
                        tradeType = "Trade Offer"; // tradeOffer() method
                    } else {
                        tradeType = "Trade Request"; // tradeRequest() method
                    }
                }
                
                // Create beautiful trade history card
                Table tradeCard = new Table();
                tradeCard.setBackground(skin.getDrawable("default-round"));
                tradeCard.pad(15);
                
                // Trade type header with color coding
                Label typeLabel = new Label(tradeType, skin);
                typeLabel.setFontScale(1.3f);
                if (tradeType.contains("Buy")) {
                    typeLabel.setColor(Color.CYAN);
                } else if (tradeType.contains("Trade")) {
                    typeLabel.setColor(Color.MAGENTA);
                } else {
                    typeLabel.setColor(Color.CYAN);
                }
                tradeCard.add(typeLabel).colspan(2).padBottom(10).row();
                
                // Trade ID
                if (tradeId != null) {
                    Label idLabel = new Label("Trade ID: " + tradeId, skin);
                    idLabel.setColor(Color.LIGHT_GRAY);
                    tradeCard.add(idLabel).colspan(2).padBottom(5).row();
                }
                
                // Status with color coding
                Label statusLabel = new Label("Status: " + status, skin);
                statusLabel.setFontScale(1.1f);
                if (status.equals("ACCEPTED")) {
                    statusLabel.setColor(Color.GREEN);
                } else if (status.equals("REJECTED")) {
                    statusLabel.setColor(Color.RED);
                } else {
                    statusLabel.setColor(Color.ORANGE);
                }
                tradeCard.add(statusLabel).colspan(2).padBottom(8).row();
                
                // Sender info
                if (sender != null) {
                    Label senderLabel = new Label("From: " + sender, skin);
                    senderLabel.setColor(Color.YELLOW);
                    tradeCard.add(senderLabel).colspan(2).padBottom(5).row();
                }
                
                // Receiver info
                if (receiver != null) {
                    Label receiverLabel = new Label("To: " + receiver, skin);
                    receiverLabel.setColor(Color.YELLOW);
                    tradeCard.add(receiverLabel).colspan(2).padBottom(8).row();
                }
                
                // Item info
                String[] itemLines = itemInfo.split("\n");
                for (String itemLine : itemLines) {
                    Label itemLabel = new Label(itemLine, skin);
                    itemLabel.setColor(Color.WHITE);
                    tradeCard.add(itemLabel).colspan(2).padBottom(3).row();
                }
                
                tradesTable.add(tradeCard).width(500).pad(15).row();
            }
        } else {
            Label noHistoryLabel = new Label("No trade history", skin);
            noHistoryLabel.setFontScale(1.2f);
            noHistoryLabel.setColor(Color.GRAY);
            tradesTable.add(noHistoryLabel).pad(30).row();
        }
        
        // Create scroll pane for the trade history
        ScrollPane scrollPane = new ScrollPane(tradesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollBarPositions(false, true);
        
        // Add scroll pane to main table with fixed height
        tradeMenuTable.add(scrollPane).width(550).height(400).pad(10).row();
        
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentTradeMenuState = TradeMenuState.MAIN_MENU;
                createTradeMenuUI();
            }
        });
        tradeMenuTable.add(backButton).width(200).pad(20).row();
    }
}
