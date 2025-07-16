package com.example.main.GDXviews;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.GDXmodels.TextureManager;
import com.example.main.Main;
import com.example.main.controller.GameMenuController;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.player.Skills;
import com.example.main.models.App;
import com.example.main.models.Date;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.Player;
import com.example.main.models.Tile;
import com.example.main.models.Time;
import com.example.main.models.User;
import com.example.main.models.item.Item;
import com.example.main.models.item.Tool;

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

    // Time-related variables
    private float timeAccumulator = 0f;
    private static final float SECONDS_PER_10_IN_GAME_MINUTES = 0.7f;

    private TextureManager textureManager;

    // HUD Assets
    private Texture clockTexture;
    private BitmapFont hudFont;

    private Texture rainTexture;
    private Texture snowTexture;
    private Array<Rectangle> rainParticles;
    private Array<Rectangle> snowParticles;
    private float weatherStateTime = 0f;

    //Inventory Fields
    private boolean isInventoryOpen = false;
    private Stage inventoryStage;
    private Texture inventoryBackground;
    private Table menuContentTable;
    private Table mainInventoryContainer;

    // NEW: Tool Selection Menu Fields
    private boolean isToolMenuOpen = false;
    private Stage toolMenuStage;
    private ArrayList<Tool> playerTools;
    private int currentToolIndex = 0;
    private Image currentToolImage;
    private Label currentToolLabel;

    // NEW: Fainted Player UI Fields
    private Label faintedMessageLabel;
    private float faintedMessageTimer = 0f;
    private static final float FAINTED_MESSAGE_DURATION = 2.5f;

    // NEW: Storm Effect Fields
    private float stormEffectTimer = 0f;
    private float nextLightningTime = 0f;
    private boolean lightningActive = false;
    private float lightningDuration = 0f;
    private boolean cheatLightningActive = false;

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

        faintedMessageLabel = new Label("I'm too exhausted to move...", skin);
        faintedMessageLabel.setColor(Color.RED);
        faintedMessageLabel.setVisible(false); // Start with the message hidden

        Table messageTable = new Table();
        messageTable.top().padTop(50);
        messageTable.setFillParent(true);
        messageTable.add(faintedMessageLabel);
        stage.addActor(messageTable);

        random = new Random();
        baseGroundMap = new int[MAP_WIDTH][MAP_HEIGHT];
        grassVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        treeVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        playerHouseVariants = new int[4];
        npcHouseVariants = new int[5];
        stoneVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        waterVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];

        textureManager = new TextureManager();
        textureManager.loadAllItemTextures();
        loadTextures();
        loadHudAssets();
        loadWeatherAssets();

        game = App.getInstance().getCurrentGame();
        gameMap = game.getMap();

        controller.setGame(game);
        controller.setMap(gameMap);

        generateRandomMaps();

        initializePlayerPosition();

        inventoryStage = new Stage(new ScreenViewport());
        inventoryBackground = new Texture("content/Cut/menu_background.png");
        setupInventoryUI();

        setupToolMenuUI();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inventoryStage);
        multiplexer.addProcessor(toolMenuStage); // Add the new stage
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
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

        if (!isInventoryOpen) {
            updateTime(delta);
        }

        Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        renderMap();
        spriteBatch.end();

        renderWeather(delta);
        renderDayNightOverlay();

        if (showMinimap) {
            renderMinimap();
        }

        renderWeather(delta);
        renderHud();
        renderDayNightOverlay();
        renderHud();
        renderToolMenu(delta);
        renderInventoryOverlay(delta);

        if (faintedMessageTimer > 0) {
            faintedMessageTimer -= delta;
            if (faintedMessageTimer <= 0) {
                faintedMessageLabel.setVisible(false);
            }
        }

        stage.act(delta);
        stage.draw();
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

    private void loadWeatherAssets() {
        // Load the single images
        rainTexture = new Texture("content/weather/rain1.png");
        snowTexture = new Texture("content/weather/snow.png");

        // Initialize rain particles
        rainParticles = new Array<>();
        for (int i = 0; i < 200; i++) { // Create 200 raindrops
            rainParticles.add(new Rectangle(
                (float) (Math.random() * Gdx.graphics.getWidth()),
                (float) (Math.random() * Gdx.graphics.getHeight()),
                rainTexture.getWidth(),
                rainTexture.getHeight()
            ));
        }

        // Initialize snow particles
        snowParticles = new Array<>();
        for (int i = 0; i < 100; i++) { // Create 100 snowflakes
            snowParticles.add(new Rectangle(
                (float) (Math.random() * Gdx.graphics.getWidth()),
                (float) (Math.random() * Gdx.graphics.getHeight()),
                snowTexture.getWidth(),
                snowTexture.getHeight()
            ));
        }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isInventoryOpen = !isInventoryOpen;
            if (isInventoryOpen) {
                showMainMenuButtons(); // Show the main buttons when opening
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            isToolMenuOpen = !isToolMenuOpen;
            if (isToolMenuOpen) {
                playerTools = game.getCurrentPlayer().getTools();
                currentToolIndex = 0; // Reset to the first tool
                updateToolMenuDisplay();
            }
        }

        if (isInventoryOpen || isToolMenuOpen) {
            return;
        }

        handleMinimapToggle();
        handleTurnSwitching();
        handlePlayerMovement(delta);
        handleCameraMovement(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (controller != null) {
                controller.changeTime("1");
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (controller != null) {
                controller.changeDate("1");
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            controller.cheatSetEnergy(200);
        }

        // In the handleInput method...
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            // This part is modified to trigger the animation
            if (controller != null && game != null) {
                Player currentPlayer = game.getCurrentPlayer();
                String x = String.valueOf(currentPlayer.currentX());
                String y = String.valueOf(currentPlayer.currentY());
                System.out.println(controller.cheatLightning(x, y).Message());

                // Trigger the visual flash effect
                cheatLightningActive = true;
                lightningDuration = 0.15f; // Set the duration of the flash
            }
        }
    }

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
                currentPlayer.reduceEnergy(10);
            }
            return;
        }

        boolean upPressed = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downPressed = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        if (currentPlayer.isFainted() && (upPressed || downPressed || leftPressed || rightPressed)) {
            faintedMessageLabel.setVisible(true);
            faintedMessageTimer = FAINTED_MESSAGE_DURATION;
            return; // Prevent any further movement logic
        }

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

            if (player.isFainted()) {
                // Draw the sleeping "Z z Z" animation
                hudFont.setColor(Color.WHITE);
                // Animate the text bobbing up and down
                float bobOffset = (float) (Math.sin(weatherStateTime * 4) * 5);
                hudFont.draw(spriteBatch, "Z z Z", worldX + 8, worldY + 48 + bobOffset);
            } else {
                // Draw the normal player sprite
                Texture playerTexture = getPlayerTexture(player);
                float playerWidth = playerTexture.getWidth() * 2;
                float playerHeight = playerTexture.getHeight() * 2;
                float renderX = worldX + (TILE_SIZE - playerWidth) / 2f;
                float renderY = worldY;

                if (player.equals(game.getCurrentPlayer())) {
                    spriteBatch.draw(playerTexture, renderX, renderY, playerWidth, playerHeight);
                } else {
                    spriteBatch.setColor(1f, 1f, 1f, 0.7f);
                    spriteBatch.draw(playerTexture, renderX, renderY, playerWidth, playerHeight);
                    spriteBatch.setColor(1f, 1f, 1f, 1f);
                }
            }

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

    private void renderHud() {
        // --- Draw UI Backgrounds ---
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0.0f, 0.0f, 0.5f, 0.5f);

        float infoBgX = 10;
        float infoBgY = Gdx.graphics.getHeight() - 70;
        float infoBgWidth = 220; // Adjust width as needed
        float infoBgHeight = 60;
        shapeRenderer.rect(infoBgX, infoBgY, infoBgWidth, infoBgHeight);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();

        float screenWidth = Gdx.graphics.getWidth();
        float clockWidth = clockTexture.getWidth() * 1f;
        float clockHeight = clockTexture.getHeight() * 1f;
        float clockX = screenWidth - clockWidth - 20;
        float clockY = Gdx.graphics.getHeight() - clockHeight - 20;
        spriteBatch.draw(clockTexture, clockX, clockY, clockWidth, clockHeight);

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
            balanceString += " | ";
            String energyString = String.valueOf(player.getEnergy());
            hudFont.draw(spriteBatch, balanceString, clockX + 110, clockY + 40);
            hudFont.draw(spriteBatch, energyString,clockX + 170, clockY + 40);
        }
        // --- END NEW ---

        // Revert font color to white for the dark background
        hudFont.setColor(Color.WHITE);
        String seasonString = "Season: " + controller.showSeason();
        hudFont.draw(spriteBatch, seasonString, 20, Gdx.graphics.getHeight() - 20);

        String weatherString = "Weather: " + game.getTodayWeather().name();
        hudFont.draw(spriteBatch, weatherString, 20, Gdx.graphics.getHeight() - 50);

        spriteBatch.end();
    }

    private void renderWeather(float delta) {
        if (game == null || game.getTodayWeather() == null) {
            return;
        }

        weatherStateTime += delta;
        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();

        switch (game.getTodayWeather()) {
            case Rainy:
            case Stormy: // Also show rain during a storm
                for (Rectangle particle : rainParticles) {
                    // Move rain straight down
                    particle.y -= 300 * delta;
                    // If it goes off screen, reset it to the top
                    if (particle.y < 0) {
                        particle.y = Gdx.graphics.getHeight();
                        particle.x = (float) (Math.random() * Gdx.graphics.getWidth());
                    }
                    spriteBatch.draw(rainTexture, particle.x, particle.y);
                }
                break;

            case Snowy:
                for (Rectangle particle : snowParticles) {
                    // Move snow down and add a gentle side-to-side sway
                    particle.y -= 60 * delta;
                    particle.x += (float) (Math.sin(particle.y / 30) * 20 * delta);

                    // If it goes off screen, reset it to the top
                    if (particle.y < 0) {
                        particle.y = Gdx.graphics.getHeight();
                        particle.x = (float) (Math.random() * Gdx.graphics.getWidth());
                    }
                    spriteBatch.draw(snowTexture, particle.x, particle.y, 24, 24); // Drawing snow slightly larger
                }
                break;

            default:
                // No weather effect for sunny days
                break;
        }

        spriteBatch.end();

        // Handle lightning flash for storms OR manual cheat
        boolean shouldFlash = false;
        if (game.getTodayWeather() == Weather.Stormy) {
            stormEffectTimer += delta;
            if (stormEffectTimer > nextLightningTime) {
                lightningActive = true;
                lightningDuration = 0.15f;
                nextLightningTime = (float) (stormEffectTimer + 3 + Math.random() * 5);
            }
            if (lightningActive) {
                shouldFlash = true;
            }
        }

        // Also check if the cheat is active
        if (cheatLightningActive) {
            shouldFlash = true;
        }

        // If a flash should happen, render it
        if (shouldFlash) {
            lightningDuration -= delta;
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.setProjectionMatrix(hudCamera.combined);
            shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 1, 1, 0.7f); // White flash
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Reset the flags when the duration is over
            if (lightningDuration <= 0) {
                lightningActive = false;
                cheatLightningActive = false;
            }
        }
    }

    private void renderDayNightOverlay() {
        if (game == null) return;

        Time time = game.getTime();
        int hour = time.getHour();
        int minute = time.getMinute();

        // The darkening effect starts at 6 PM (hour 18) and ends at 2 AM (hour 26)
        float startHour = 18f;
        float endHour = 26f;
        float maxOpacity = 0.85f; // End with a dark, but not pitch-black, screen

        float currentHour = hour + (minute / 60f);
        float opacity = 0f;

        if (currentHour >= startHour) {
            // Calculate the progress through the evening
            float progress = (currentHour - startHour) / (endHour - startHour);
            // Clamp the value between 0 and 1 to prevent errors
            progress = Math.max(0, Math.min(1, progress));
            opacity = progress * maxOpacity;
        }

        // If opacity is greater than 0, draw the overlay
        if (opacity > 0) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(hudCamera.combined);
            shapeRenderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);

            // Black color with calculated opacity
            shapeRenderer.setColor(0, 0, 0, opacity);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private void renderInventoryOverlay(float delta) {
        if (!isInventoryOpen) {
            return;
        }

        float width = Gdx.graphics.getWidth() * 0.8f;
        float height = Gdx.graphics.getHeight() * 0.8f;
        float x = (Gdx.graphics.getWidth() - width) / 2;
        float y = (Gdx.graphics.getHeight() - height) / 2;

        spriteBatch.setProjectionMatrix(hudCamera.combined);
        spriteBatch.begin();
        spriteBatch.draw(inventoryBackground, x, y, width, height);
        spriteBatch.end();

        inventoryStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        inventoryStage.act(delta);
        inventoryStage.draw();
    }

    private void renderToolMenu(float delta) {
        if (!isToolMenuOpen) return;

        toolMenuStage.act(delta);
        toolMenuStage.draw();
    }

    private void setupInventoryUI() {
        mainInventoryContainer = new Table();
        inventoryStage.addActor(mainInventoryContainer);
        mainInventoryContainer.setFillParent(true);
        mainInventoryContainer.center();

        menuContentTable = new Table();
        mainInventoryContainer.add(menuContentTable).expand().fill();
    }

    private void showMainMenuButtons() {
        menuContentTable.clear();

        TextButton inventoryButton = new TextButton("Inventory", skin);
        TextButton skillsButton = new TextButton("Skills", skin);
        TextButton socialButton = new TextButton("Social", skin);
        TextButton mapButton = new TextButton("Map", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton closeButton = new TextButton("Close", skin);

        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showInventoryDisplay();
            }
        });

        skillsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSkillsDisplay();
            }
        });

        socialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSocialDisplay();
            }
        });

        mapButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMinimap = !showMinimap;
                isInventoryOpen = false; // Close menu to see the map
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSettingsMenu();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isInventoryOpen = false;
            }
        });

        float buttonWidth = 200f;
        float buttonPad = 10f;

        menuContentTable.add(inventoryButton).width(buttonWidth).pad(buttonPad).row();
        menuContentTable.add(skillsButton).width(buttonWidth).pad(buttonPad).row();
        menuContentTable.add(socialButton).width(buttonWidth).pad(buttonPad).row();
        menuContentTable.add(mapButton).width(buttonWidth).pad(buttonPad).row();
        menuContentTable.add(settingsButton).width(buttonWidth).pad(buttonPad).row();
        menuContentTable.add(closeButton).width(buttonWidth).pad(buttonPad).row();
    }

    private void showInventoryDisplay() {
        menuContentTable.clear();

        Table itemsTable = new Table();
        updateInventoryGrid(itemsTable);

        // Create a new style for the ScrollPane
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        // Set its background to be the same as the main menu
        scrollPaneStyle.background = new TextureRegionDrawable(new TextureRegion(inventoryBackground));

        // Create the ScrollPane with the new style
        ScrollPane scrollPane = new ScrollPane(itemsTable, scrollPaneStyle);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // Enables vertical scrolling

        menuContentTable.add(scrollPane).expand().fill().pad(20).row();
        addBackButtonToMenu();
    }

    private void showSkillsDisplay() {
        menuContentTable.clear();
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        Table skillsTable = new Table();
        TooltipManager tooltipManager = new TooltipManager();
        tooltipManager.instant();

        // --- NEW: Programmatically create a style for the ProgressBar ---
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle();
        // Use simple, reliable drawables from the default skin
        barStyle.background = skin.getDrawable("default-slider");
        barStyle.knobBefore = skin.getDrawable("default-slider-knob");
        // --- End of New Code ---

        for (Skills skill : Skills.values()) {
            Table skillRow = new Table();

            // Icon and Tooltip
            Texture iconTexture = textureManager.getTexture(skill.name() + "_Skill_Icon");
            Image icon;
            if (iconTexture != null) {
                icon = new Image(iconTexture);
                Tooltip<Label> tooltip = new Tooltip<>(new Label(skill.getSkillDescription(), skin));
                icon.addListener(tooltip);
            } else {
                icon = new Image(skin.getDrawable("default-round"));
                Gdx.app.log("Skills", "Missing texture for skill icon: " + skill.name() + "_Skill_Icon.png");
            }
            skillRow.add(icon).size(48, 48).padRight(10);

            // Name
            skillRow.add(new Label(skill.name(), skin)).width(100);

            // Progress Bar - Using the new custom style
            ProgressBar progressBar = new ProgressBar(0, 100, 1, false, barStyle);

            // Calculate and set progress
            int currentExp = currentPlayer.getSkillExperience(skill);
            int expForNextLevel = skill.getExpForNextLevel();
            float progress = (expForNextLevel > 0) ? ((float)currentExp / expForNextLevel) * 100f : 0f;
            progressBar.setValue(progress);

            skillRow.add(progressBar).width(200).padRight(10);

            // Level
            skillRow.add(new Label("Level " + currentPlayer.getSkillLevel(skill), skin));

            skillsTable.add(skillRow).padBottom(10).row();
        }

        menuContentTable.add(skillsTable).expand().center().row();
        addBackButtonToMenu();
    }

    private void showSocialDisplay() {
        menuContentTable.clear();

        Stack socialStack = new Stack();
        socialStack.add(new Image(inventoryBackground));

        Table contentTable = new Table();

        Table socialInfoTable = new Table();
        String playerFriendships = controller.showAllFriendShips().Message();
        String npcFriendships = controller.showNPCFriendships().Message();

        socialInfoTable.add(new Label("--- Player Friendships ---", skin)).padBottom(10).row();
        socialInfoTable.add(new Label(playerFriendships, skin)).left().padBottom(20).row();
        socialInfoTable.add(new Label("--- NPC Friendships ---", skin)).padBottom(10).row();
        socialInfoTable.add(new Label(npcFriendships, skin)).left().row();

        ScrollPane scrollPane = new ScrollPane(socialInfoTable, skin);
        scrollPane.setFadeScrollBars(false);

        contentTable.add(scrollPane).expand().fill().pad(40).row();

        socialStack.add(contentTable);
        menuContentTable.add(socialStack).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.8f);
        addBackButtonToMenu();
    }

    private void showSettingsMenu() {
        menuContentTable.clear();

        TextButton leaveGameButton = new TextButton("Leave Game", skin);
        TextButton kickPlayerButton = new TextButton("Kick Player", skin);

        leaveGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isInventoryOpen = false;
                Main.getInstance().setScreen(new GDXMainMenu());
            }
        });

        kickPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Kick Player clicked - functionality not implemented.");
            }
        });

        menuContentTable.add(leaveGameButton).width(200).pad(10).row();
        menuContentTable.add(kickPlayerButton).width(200).pad(10).row();
        addBackButtonToMenu();
    }

    private void addBackButtonToMenu() {
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMainMenuButtons();
            }
        });
        menuContentTable.add(backButton).pad(10).bottom().left();
    }

    private void updateInventoryGrid(Table table) {
        table.clear();
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        ArrayList<Item> items = currentPlayer.getInventory().getItems();
        int column = 0;
        final int ITEMS_PER_ROW = 4;

        for (Item item : items) {
            String key = generateTextureKey(item);
            Texture texture = textureManager.getTexture(key);

            Table itemSlot = new Table(skin);
            itemSlot.setBackground("default-round");

            if (texture != null) {
                itemSlot.add(new Image(texture)).size(48, 48);
            } else {
                itemSlot.add(new Label("?", skin)).size(48, 48);
                Gdx.app.log("Inventory", "Missing texture for item: '" + item.getName() + "' (tried key: '" + key + "')");
            }

            itemSlot.row();
            itemSlot.add(new Label(String.valueOf(item.getNumber()), skin));

            table.add(itemSlot).pad(4);
            column++;
            if (column >= ITEMS_PER_ROW) {
                table.row();
                column = 0;
            }
        }
    }

    private String generateTextureKey(Item item) {
        if (item == null || item.getName() == null) {
            return "Unknown";
        }
        // This version relies on the item's display name matching the asset file name.
        // Example: item.getName() -> "Basic Fertilizer" -> "Basic_Fertilizer"
        return item.getName().replace(" ", "_");
    }

    private void setupToolMenuUI() {
        toolMenuStage = new Stage(new ScreenViewport());

        Table toolTable = new Table();
        toolTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        toolMenuStage.addActor(toolTable);

        // Initial placeholder image and label
        currentToolImage = new Image();
        currentToolLabel = new Label("", skin);

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        TextButton acceptButton = new TextButton("Equip", skin);

        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerTools.isEmpty()) {
                    currentToolIndex = (currentToolIndex - 1 + playerTools.size()) % playerTools.size();
                    updateToolMenuDisplay();
                }
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerTools.isEmpty()) {
                    currentToolIndex = (currentToolIndex + 1) % playerTools.size();
                    updateToolMenuDisplay();
                }
            }
        });

        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!playerTools.isEmpty()) {
                    Tool selectedTool = playerTools.get(currentToolIndex);
                    game.getCurrentPlayer().setCurrentTool(selectedTool);
                    isToolMenuOpen = false; // Close menu after equipping
                }
            }
        });

        // Layout the tool menu
        toolTable.add(leftButton).pad(5);
        toolTable.add(currentToolImage).size(64, 64).pad(5);
        toolTable.add(rightButton).pad(5);
        toolTable.row();
        toolTable.add(currentToolLabel).colspan(3).pad(5);
        toolTable.row();
        toolTable.add(acceptButton).colspan(3).pad(5);

        // Position the table in the bottom-left corner
        toolTable.pack();
        toolTable.setPosition(10, 10);
    }

    private void updateToolMenuDisplay() {
        if (playerTools.isEmpty()) {
            currentToolImage.setDrawable(null);
            currentToolLabel.setText("No Tools");
            return;
        }

        Tool tool = playerTools.get(currentToolIndex);
        Texture texture = textureManager.getTexture(generateTextureKey(tool));

        if (texture != null) {
            currentToolImage.setDrawable(new Image(texture).getDrawable());
        } else {
            // Fallback if texture is missing
            currentToolImage.setDrawable(skin.getDrawable("default-round"));
        }
        currentToolLabel.setText(tool.getName());
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
        inventoryStage.dispose();
        inventoryBackground.dispose();
        textureManager.dispose();
        toolMenuStage.dispose();

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

        textureManager.dispose();
    }
}

