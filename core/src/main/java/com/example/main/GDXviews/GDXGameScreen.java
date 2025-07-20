package com.example.main.GDXviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tooltip;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.GDXmodels.TextureManager;
import com.example.main.Main;
import com.example.main.controller.GameMenuController;
import com.example.main.controller.StoreMenuController;
import com.example.main.enums.design.NPCType;
import com.example.main.enums.design.ShopType;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.TreeType;
import com.example.main.enums.player.Skills;
import com.example.main.models.App;
import com.example.main.models.Date;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.NPC;
import com.example.main.models.NPCFriendship;
import com.example.main.models.Notification;
import com.example.main.models.Player;
import com.example.main.models.Quest;
import com.example.main.models.Result;
import com.example.main.models.Tile;
import com.example.main.models.Time;
import com.example.main.models.User;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fruit;
import com.example.main.models.item.Item;
import com.example.main.models.item.Seed;
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
    
    // Shop menu variables
    private boolean showShopMenu = false;
    private Table shopMenuTable;
    private ShopType currentShopType = null;
    private StoreMenuController shopController;
    
    // Shop menu state management
    private enum ShopMenuState {
        MAIN_MENU,
        ALL_PRODUCTS,
        AVAILABLE_PRODUCTS
    }
    
    private ShopMenuState currentShopMenuState = ShopMenuState.MAIN_MENU;
    private String shopResultMessage = "";
    private boolean shopResultSuccess = false;

    // Time-related variables
    private float timeAccumulator = 0f;
    private static final float SECONDS_PER_10_IN_GAME_MINUTES = 5f;

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

    // NEW: Storm Effect Fields
    private float stormEffectTimer = 0f;
    private float nextLightningTime = 0f;
    private boolean lightningActive = false;
    private float lightningDuration = 0f;
    private boolean cheatLightningActive = false;

    private boolean isPlantingMode = false;
    private Tile plantingTargetTile = null;
    private Label plantingPromptLabel;

    // Cheat Menu Fields
    private boolean isCheatMenuOpen = false;
    private Stage cheatMenuStage;
    private TextField cheatItemNameField;
    private TextField cheatItemQuantityField;

    private InputMultiplexer multiplexer;

    private Label generalMessageLabel;
    private float generalMessageTimer = 0f;
    private static final float GENERAL_MESSAGE_DURATION = 3.0f;

    private boolean isPlantingSelectionOpen = false;
    private Stage plantingStage;
    private ArrayList<Item> plantableItems;
    private int selectedPlantableIndex = 0;
    private Label plantingItemNameLabel;
    private Image plantingSelectionHighlight;
    private HorizontalGroup plantingItemsGroup;

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

    // NPC textures
    private Texture sebastianTexture;
    private Texture abigailTexture;
    private Texture harveyTexture;
    private Texture liaTexture;
    private Texture robinTexture;

    // NPC interaction textures
    private Texture dialogBoxTexture;
    private Texture menuBackgroundTexture;

    // NPC interaction state
    private ArrayList<NPC> nearbyNPCs = new ArrayList<>();
    private NPC currentDialogNPC = null;
    private String currentDialogMessage = "";
    private boolean showDialog = false;
    private ArrayList<NPC> clickedNPCs = new ArrayList<>(); // Track which NPCs have been clicked
    
    // NPC menu state
    private boolean showNPCMenu = false;
    private NPC selectedNPC = null;
    private Table npcMenuTable;
    
    // NPC menu state management
    private enum NPCMenuState {
        MAIN_MENU,
        FRIENDSHIP,
        QUESTS,
        GIFT
    }
    
    private NPCMenuState currentNPCMenuState = NPCMenuState.MAIN_MENU;
    
    // Quest result message storage
    private String questResultMessage = "";
    private boolean questResultSuccess = false;

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
    private float toolRotation = 0f;

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
        shopController = new StoreMenuController();
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

        generalMessageLabel = new Label("", skin);
        generalMessageLabel.setVisible(false);

        plantingPromptLabel = new Label("Double-click a seed to plant", skin);
        plantingPromptLabel.setColor(Color.LIME);
        plantingPromptLabel.setVisible(false);

        Table messageTable = new Table();
        messageTable.top().padTop(20);
        messageTable.setFillParent(true);
        messageTable.add(generalMessageLabel);
        messageTable.add(plantingPromptLabel);
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
        setupCheatMenuUI();
        setupPlantingUI();

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inventoryStage);
        multiplexer.addProcessor(toolMenuStage);
        multiplexer.addProcessor(cheatMenuStage);
        plantableItems = new ArrayList<>();
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

        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null && !currentPlayer.getNotifications().isEmpty()) {
            Notification notif = currentPlayer.getNotifications().get(0);
            generalMessageLabel.setText(notif.getMessage());
            generalMessageLabel.setColor(Color.CYAN); // Use a distinct color for notifications
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;
            currentPlayer.resetNotifs(); // Clear notifications after displaying
        }

        if (currentPlayer != null && currentPlayer.getCurrentTool() != null) {
            // Get mouse position in screen coordinates
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();

            // Convert mouse position to world coordinates
            com.badlogic.gdx.math.Vector3 mouseInWorld = camera.unproject(new com.badlogic.gdx.math.Vector3(mouseX, mouseY, 0));

            // Get player's center position in world coordinates
            float playerCenterX = (currentPlayer.currentX() * TILE_SIZE) + (TILE_SIZE / 2f);
            float playerCenterY = ((MAP_HEIGHT - 1 - currentPlayer.currentY()) * TILE_SIZE) + (TILE_SIZE / 2f);

            // Calculate the angle
            float deltaX = mouseInWorld.x - playerCenterX;
            float deltaY = mouseInWorld.y - playerCenterY;
            toolRotation = com.badlogic.gdx.math.MathUtils.atan2(deltaY, deltaX) * com.badlogic.gdx.math.MathUtils.radiansToDegrees;
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
        renderCheatMenu(delta);
        renderPlantingUI(delta);

        if (showMinimap) {
            renderMinimap();
        }

        renderWeather(delta);
        renderHud();
        renderDayNightOverlay();
        renderHud();
        renderDayNightOverlay();
        renderHud();
        renderToolMenu(delta);
        renderInventoryOverlay(delta);

        stage.act(delta);
        stage.draw();
        
        // Shop menu is handled by the stage, no need to recreate it every frame
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

        // Load NPC textures
        sebastianTexture = new Texture("content/Cut/NPC/sebastian.png");
        abigailTexture = new Texture("content/Cut/NPC/abigail.png");
        harveyTexture = new Texture("content/Cut/NPC/harvey.png");
        liaTexture = new Texture("content/Cut/NPC/lia.png");
        robinTexture = new Texture("content/Cut/NPC/robin.png");

        // Load NPC interaction textures
        dialogBoxTexture = new Texture("content/Cut/map_elements/dialog_box.png");
        menuBackgroundTexture = new Texture("content/Cut/menu_background.png");
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
        // If shop menu is open, only allow shop menu UI to handle input
        if (showShopMenu) {
            // Allow UI events (handled by scene2d stage) but block game input
            return;
        }
        // If trade menu is open, only allow trade menu UI
        if (showTradeMenu) {
            return;
        }
        // If NPC menu is open, only allow NPC menu UI
        if (showNPCMenu) {
            // Allow ESC key to close NPC menu
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                showNPCMenu = false;
                selectedNPC = null;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }
            }
            return;
        }
        //handleTradeMenuToggle();
        // Only handle game input if trade menu and shop menu are not showing
        com.badlogic.gdx.math.Vector3 mouseInWorld = camera.unproject(new com.badlogic.gdx.math.Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        int targetTileX = (int) (mouseInWorld.x / TILE_SIZE);
        int targetTileY = MAP_HEIGHT - 1 - (int) (mouseInWorld.y / TILE_SIZE);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (isPlantingSelectionOpen) {
                closePlantingMenu();
            } else {
                isInventoryOpen = !isInventoryOpen;
                if (isInventoryOpen) {
                    showMainMenuButtons();
                    Gdx.input.setInputProcessor(inventoryStage);
                } else {
                    Gdx.input.setInputProcessor(multiplexer);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            isToolMenuOpen = !isToolMenuOpen;
            if (isToolMenuOpen) {
                playerTools = game.getCurrentPlayer().getTools();
                currentToolIndex = 0;
                updateToolMenuDisplay();
                Gdx.input.setInputProcessor(toolMenuStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            isCheatMenuOpen = !isCheatMenuOpen;
            if(isCheatMenuOpen) {
                Gdx.input.setInputProcessor(cheatMenuStage);
            } else {
                Gdx.input.setInputProcessor(multiplexer);
            }
        }

        if (isInventoryOpen || isToolMenuOpen || isCheatMenuOpen || isPlantingSelectionOpen) {
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (targetTileX >= 0 && targetTileX < MAP_WIDTH && targetTileY >= 0 && targetTileY < MAP_HEIGHT) {
                Tile hoveredTile = gameMap.getTiles()[targetTileX][targetTileY];
                if (hoveredTile.getType() == TileType.Shoveled && hoveredTile.getPlant() == null && hoveredTile.getSeed() == null) {
                    openPlantingMenu(hoveredTile);
                    return;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (controller != null && game.getCurrentPlayer() != null) {
                String message;
                if (game.getCurrentPlayer().getCurrentTool() == null) {
                    message = "No tool equipped!";
                    generalMessageLabel.setColor(Color.YELLOW);
                } else {
                    Result result;
                    if (targetTileX >= 0 && targetTileX < MAP_WIDTH && targetTileY >= 0 && targetTileY < MAP_HEIGHT) {
                        Tile targetTile = gameMap.getTiles()[targetTileX][targetTileY];
                        result = controller.useTool(targetTile);
                    } else {
                        result = new Result(false, "Target is outside the map.");
                    }
                    message = result.Message();
                    generalMessageLabel.setColor(Color.WHITE);
                }
                generalMessageLabel.setText(message);
                generalMessageLabel.setVisible(true);
                generalMessageTimer = GENERAL_MESSAGE_DURATION;
            }
        }

        handleMinimapToggle();
        handleTurnSwitching();
        handlePlayerMovement(delta);
        handleCameraMovement(delta);
        handleShopInteraction();
        
        // Handle NPC interactions
        if (showDialog) {
            handleDialogDismiss();
        } else {
            // Only handle NPC clicks if no dialog is showing
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                handleNPCClick(Gdx.input.getX(), Gdx.input.getY());
            }
            // Handle right-click for NPC menu
            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                handleNPCRightClick(Gdx.input.getX(), Gdx.input.getY());
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            if (controller != null) {
                controller.changeTime("1");
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            if (controller != null) {
                controller.changeDate(1);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            if (controller != null && game != null) {
                Player currentPlayer = game.getCurrentPlayer();
                String x = String.valueOf(currentPlayer.currentX());
                String y = String.valueOf(currentPlayer.currentY());
                System.out.println(controller.cheatLightning(x, y).Message());
                cheatLightningActive = true;
                lightningDuration = 0.15f;
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            controller.cheatSetEnergy(200);
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
            generalMessageLabel.setText("I'm too exhausted to move...");
            generalMessageLabel.setColor(Color.RED); // Set color to red for fainted message
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION; // Use the general timer
            return;
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
    
    private void handleShopInteraction() {
        // Check for mouse click
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Get mouse position in screen coordinates
            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.input.getY();
            
            // Convert screen coordinates to world coordinates
            Vector3 worldCoords = camera.unproject(new Vector3(mouseX, mouseY, 0));
            
            // Convert world coordinates to tile coordinates
            int tileX = (int) (worldCoords.x / TILE_SIZE);
            int tileY = (int) ((MAP_HEIGHT * TILE_SIZE - worldCoords.y) / TILE_SIZE);
            
            // Check if the clicked tile is within a shop area
            int shopIndex = getShopIndex(tileX, tileY);
            if (shopIndex != -1) {
                // Get the shop type based on the index
                ShopType[] shopTypes = ShopType.values();
                if (shopIndex < shopTypes.length) {
                    currentShopType = shopTypes[shopIndex];
                    
                    // Move player to the shop location for the shop controller to work
                    Player currentPlayer = game.getCurrentPlayer();
                    if (currentPlayer != null) {
                        // Set player position to the shop corner + 1 to be inside the shop area
                        // (the corner is a wall, so we need to be inside)
                        currentPlayer.setCurrentX(currentShopType.getCornerX() + 1);
                        currentPlayer.setCurrentY(currentShopType.getCornerY() + 1);
                        
                        // Update camera to follow player
                        updateCameraToFollowPlayer(currentPlayer);
                    }
                    
                    showShopMenu = true;
                    createShopMenuUI();
                }
            }
        }
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
        renderNPCs();
        
        // Update nearby NPCs and render interaction elements
        updateNearbyNPCs();
        renderMeetButtons();
        renderDialogBox();
    }

// In main/GDXviews/GDXGameScreen.java

    private void renderPlayer() {
        for (User user : game.getPlayers()) {
            Player player = user.getPlayer();
            if (player == null) {
                continue;
            }

            // --- Player Position Calculation ---
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

            // --- Draw Player Sprite (Now handles fainted state correctly) ---
            Texture playerTexture = getPlayerTexture(player); // Get texture regardless of fainted state
            float playerWidth = playerTexture.getWidth() * 2;
            float playerHeight = playerTexture.getHeight() * 2;
            float renderX = worldX + (TILE_SIZE - playerWidth) / 2f;
            float renderY = worldY;

            // Set transparency for non-current players
            if (!player.equals(game.getCurrentPlayer())) {
                spriteBatch.setColor(1f, 1f, 1f, 0.7f);
            }

            spriteBatch.draw(playerTexture, renderX, renderY, playerWidth, playerHeight);

            // Reset color to default
            spriteBatch.setColor(1f, 1f, 1f, 1f);

            // If fainted, draw the "Z z Z" animation on top of the player
            if (player.isFainted()) {
                hudFont.setColor(Color.WHITE);
                float bobOffset = (float) (Math.sin(weatherStateTime * 4) * 5);
                hudFont.draw(spriteBatch, "Z z Z", worldX + 8, worldY + 48 + bobOffset);
                if(player.getEnergy() > 0) player.setFainted(false);
            }


            // --- Draw Equipped Tool (Always After Player) ---
            if (player.equals(game.getCurrentPlayer()) && player.getCurrentTool() != null) {
                Tool currentTool = player.getCurrentTool();
                String key = generateTextureKey(currentTool);
                Texture toolTexture = textureManager.getTexture(key);

                if (toolTexture != null) {
                    float toolWidth = toolTexture.getWidth() * 1.0f;
                    float toolHeight = toolTexture.getHeight() * 1.0f;

                    float toolX = (worldX + (TILE_SIZE / 2f)) - (toolWidth / 2f);
                    float toolY = (worldY + (TILE_SIZE / 2f)) - (toolHeight / 4f);

                    spriteBatch.draw(
                        new TextureRegion(toolTexture),
                        toolX, toolY,
                        toolWidth / 2f, toolHeight / 4f,
                        toolWidth, toolHeight,
                        1f, 1f,
                        toolRotation - 90f
                    );
                }
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

    private void renderNPCs() {
        ArrayList<NPC> npcs = game.getNPCs();
        if (npcs == null) {
            return;
        }

        for (NPC npc : npcs) {
            if (npc == null) {
                continue;
            }

            int npcX = npc.getX();
            int npcY = npc.getY();

            // Check if NPC is within map bounds
            if (npcX < 0 || npcX >= MAP_WIDTH || npcY < 0 || npcY >= MAP_HEIGHT) {
                continue;
            }

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;

            Texture npcTexture = getNPCTexture(npc);
            if (npcTexture == null) {
                continue;
            }

            float npcWidth = npcTexture.getWidth() * 2;
            float npcHeight = npcTexture.getHeight() * 2;
            float renderX = worldX + (TILE_SIZE - npcWidth) / 2f;
            float renderY = worldY;

            spriteBatch.draw(npcTexture, renderX, renderY, npcWidth, npcHeight);
        }
    }

    private Texture getNPCTexture(NPC npc) {
        NPCType npcType = npc.getType();
        switch (npcType) {
            case Sebastian:
                return sebastianTexture;
            case Abigail:
                return abigailTexture;
            case Harvey:
                return harveyTexture;
            case Lia:
                return liaTexture;
            case Robin:
                return robinTexture;
            default:
                return null;
        }
    }

    private void updateNearbyNPCs() {
        nearbyNPCs.clear();
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        int playerX = currentPlayer.currentX();
        int playerY = currentPlayer.currentY();

        ArrayList<NPC> npcs = game.getNPCs();
        if (npcs == null) return;

        // Create a list of NPCs to remove from clickedNPCs
        ArrayList<NPC> npcsToRemove = new ArrayList<>();

        for (NPC npc : npcs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();

            // Check if NPC is within 8 tiles (3x3 area around player)
            int distanceX = Math.abs(playerX - npcX);
            int distanceY = Math.abs(playerY - npcY);

            if (distanceX <= 1 && distanceY <= 1) {
                // Only add if this NPC hasn't been clicked recently
                if (!clickedNPCs.contains(npc)) {
                    nearbyNPCs.add(npc);
                }
            } else {
                // If NPC is no longer nearby, remove it from clickedNPCs
                if (clickedNPCs.contains(npc)) {
                    npcsToRemove.add(npc);
                }
            }
        }

        // Remove NPCs that are no longer nearby from clickedNPCs
        clickedNPCs.removeAll(npcsToRemove);
    }

    private void renderMeetButtons() {
        for (NPC npc : nearbyNPCs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;

            // Position the "meet" button to the right of the NPC's head
            float buttonWidth = 60f;
            float buttonHeight = 24f;
            float buttonX = worldX + TILE_SIZE + 8f; // 8 pixels to the right of NPC
            float buttonY = worldY + TILE_SIZE + 8f; // 8 pixels above the NPC

            // Draw a visible button background (similar to trade menu buttons)
            spriteBatch.setColor(0.8f, 0.8f, 0.8f, 1f); // Light gray background
            spriteBatch.draw(ground1Texture, buttonX, buttonY, buttonWidth, buttonHeight);
            spriteBatch.setColor(1f, 1f, 1f, 1f); // Reset color
            
            // Draw button border
            spriteBatch.setColor(0.2f, 0.2f, 0.2f, 1f); // Dark border
            // Draw border lines
            float borderThickness = 2f;
            spriteBatch.draw(ground1Texture, buttonX, buttonY, buttonWidth, borderThickness); // Bottom
            spriteBatch.draw(ground1Texture, buttonX, buttonY + buttonHeight - borderThickness, buttonWidth, borderThickness); // Top
            spriteBatch.draw(ground1Texture, buttonX, buttonY, borderThickness, buttonHeight); // Left
            spriteBatch.draw(ground1Texture, buttonX + buttonWidth - borderThickness, buttonY, borderThickness, buttonHeight); // Right
            spriteBatch.setColor(1f, 1f, 1f, 1f); // Reset color
            
            // Draw "meet" text
            if (hudFont != null) {
                hudFont.setColor(Color.BLACK);
                hudFont.getData().setScale(0.8f);
                hudFont.draw(spriteBatch, "meet", buttonX + 15, buttonY + 15);
                hudFont.getData().setScale(1.0f);
                hudFont.setColor(Color.WHITE);
            }
        }
    }

    private void renderDialogBox() {
        if (!showDialog || currentDialogNPC == null) return;

        int npcX = currentDialogNPC.getX();
        int npcY = currentDialogNPC.getY();

        float worldX = npcX * TILE_SIZE;
        float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;

        // Position dialog box above the NPC's head
        float dialogWidth = 200f;
        float dialogHeight = 80f;
        float dialogX = worldX + TILE_SIZE + 10f; // 10 pixels to the right of NPC
        float dialogY = worldY + TILE_SIZE + 20f; // 20 pixels above the NPC

        // Draw dialog box background
        spriteBatch.draw(dialogBoxTexture, dialogX, dialogY, dialogWidth, dialogHeight);

        // Draw dialog text with smaller font
        if (hudFont != null && currentDialogMessage != null && !currentDialogMessage.isEmpty()) {
            hudFont.setColor(Color.BLACK);
            // Scale down the font size (twice the previous size)
            hudFont.getData().setScale(1.0f);
            hudFont.draw(spriteBatch, currentDialogMessage, dialogX + 10, dialogY + dialogHeight - 10);
            // Reset font scale
            hudFont.getData().setScale(1.0f);
            hudFont.setColor(Color.WHITE);
        }
    }

    private void handleNPCClick(int screenX, int screenY) {
        if (nearbyNPCs.isEmpty()) return;

        // Convert screen coordinates to world coordinates
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

        for (NPC npc : nearbyNPCs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;

            // Check if click is on the "meet" button area (to the right of NPC's head)
            float buttonWidth = 60f;
            float buttonHeight = 24f;
            float buttonX = worldX + TILE_SIZE + 8f;
            float buttonY = worldY + TILE_SIZE + 8f;

            if (worldCoords.x >= buttonX && worldCoords.x <= buttonX + buttonWidth &&
                worldCoords.y >= buttonY && worldCoords.y <= buttonY + buttonHeight) {
                
                // Call meetNPC method
                Result result = controller.meetNPC(npc.getType().name());
                
                // Show dialog with result
                currentDialogNPC = npc;
                currentDialogMessage = result.Message();
                showDialog = true;
                
                // Add NPC to clicked list to hide + button
                clickedNPCs.add(npc);
                nearbyNPCs.remove(npc);
                
                break;
            }
        }
    }

    private void handleDialogDismiss() {
        if (showDialog && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            showDialog = false;
            currentDialogNPC = null;
            currentDialogMessage = "";
        }
    }

    private void handleNPCRightClick(int screenX, int screenY) {
        // Convert screen coordinates to world coordinates
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        
        ArrayList<NPC> npcs = game.getNPCs();
        if (npcs == null) return;

        for (NPC npc : npcs) {
            if (npc == null) continue;

            int npcX = npc.getX();
            int npcY = npc.getY();

            float worldX = npcX * TILE_SIZE;
            float worldY = (MAP_HEIGHT - 1 - npcY) * TILE_SIZE;

            // Check if right-click is on the NPC
            if (worldCoords.x >= worldX && worldCoords.x <= worldX + TILE_SIZE &&
                worldCoords.y >= worldY && worldCoords.y <= worldY + TILE_SIZE) {
                
                selectedNPC = npc;
                showNPCMenu = true;
                createNPCMenuUI();
                break;
            }
        }
    }

    private void createNPCMenuUI() {
        if (npcMenuTable != null) {
            npcMenuTable.remove();
        }

        npcMenuTable = new Table();
        npcMenuTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        // Make the background 75% of screen size (same as trade menu)
        npcMenuTable.setSize(Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.75f);
        npcMenuTable.setPosition(
            (Gdx.graphics.getWidth() - npcMenuTable.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - npcMenuTable.getHeight()) / 2f
        );

        stage.addActor(npcMenuTable);

        switch (currentNPCMenuState) {
            case MAIN_MENU:
                createMainNPCMenu();
                break;
            case FRIENDSHIP:
                createFriendshipMenu();
                break;
            case QUESTS:
                createQuestsMenu();
                break;
            case GIFT:
                createGiftMenu();
                break;
        }
    }

    private void createMainNPCMenu() {
        npcMenuTable.clear();

        // NPC name label
        Label npcNameLabel = new Label(selectedNPC.getType().name(), skin);
        npcNameLabel.setFontScale(1.2f);
        npcMenuTable.add(npcNameLabel).colspan(2).pad(10).row();

        // Buttons
        TextButton friendshipButton = new TextButton("Friendship", skin);
        TextButton questsButton = new TextButton("Quests", skin);
        TextButton giftButton = new TextButton("Gift", skin);
        TextButton closeButton = new TextButton("Close", skin);

        // Add button listeners
        friendshipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.FRIENDSHIP;
                createNPCMenuUI();
            }
        });

        questsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.QUESTS;
                questResultMessage = "";
                questResultSuccess = false;
                createNPCMenuUI();
            }
        });

        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.GIFT;
                createNPCMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }
            }
        });

        // Add buttons to table with larger sizes for the bigger menu
        npcMenuTable.add(friendshipButton).size(200, 50).pad(10);
        npcMenuTable.add(questsButton).size(200, 50).pad(10).row();
        npcMenuTable.add(giftButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void createFriendshipMenu() {
        npcMenuTable.clear();

        // Title with NPC name
        Label titleLabel = new Label(selectedNPC.getType().name() + " friendship status", skin);
        titleLabel.setFontScale(1.5f);
        npcMenuTable.add(titleLabel).colspan(2).pad(20).row();

        // Get friendship information
        NPCFriendship friendship = selectedNPC.getFriendShipWith(game.getCurrentPlayer());
        if (friendship != null) {
            Label friendshipLabel = new Label(friendship.toString(), skin);
            friendshipLabel.setFontScale(1.0f);
            npcMenuTable.add(friendshipLabel).colspan(2).pad(20).row();
        } else {
            Label noFriendshipLabel = new Label("No friendship data available", skin);
            noFriendshipLabel.setFontScale(1.0f);
            npcMenuTable.add(noFriendshipLabel).colspan(2).pad(20).row();
        }

        // Back and Close buttons
        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                createNPCMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }
            }
        });

        npcMenuTable.add(backButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void createQuestsMenu() {
        npcMenuTable.clear();

        // Title
        Label titleLabel = new Label(selectedNPC.getType().name() + " Quests", skin);
        titleLabel.setFontScale(1.5f);
        npcMenuTable.add(titleLabel).colspan(2).pad(20).row();

        // Result message area
        Label resultMessageLabel = new Label(questResultMessage, skin);
        resultMessageLabel.setFontScale(1.2f);
        resultMessageLabel.setColor(questResultSuccess ? Color.GREEN : Color.RED);
        // Make sure the result message is visible and properly positioned
        npcMenuTable.add(resultMessageLabel).colspan(2).pad(10).fillX().center().row();

        // Create quest list table
        Table questListTable = new Table();
        questListTable.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
        
        // Get quests from NPC
        HashMap<Quest, Boolean> quests = selectedNPC.getQuests();
        if (quests != null && !quests.isEmpty()) {
            int questIndex = 0;
            for (Map.Entry<Quest, Boolean> entry : quests.entrySet()) {
                Quest quest = entry.getKey();
                Boolean isCompleted = entry.getValue();
                
                // Create quest entry
                Table questEntry = new Table();
                questEntry.setBackground(new TextureRegionDrawable(menuBackgroundTexture));
                
                // Quest information
                Label questInfoLabel = new Label(quest.toString(), skin);
                questInfoLabel.setColor(Color.BLACK);
                questInfoLabel.setFontScale(1.2f);
                questEntry.add(questInfoLabel).pad(5).row();
                
                // Status
                String statusText = (isCompleted != null && isCompleted) ? "Status: COMPLETED" : "Status: AVAILABLE";
                Color statusColor = (isCompleted != null && isCompleted) ? Color.GRAY : Color.GREEN;
                Label statusLabel = new Label(statusText, skin);
                statusLabel.setColor(statusColor);
                statusLabel.setFontScale(0.9f);
                questEntry.add(statusLabel).pad(5).row();
                
                // Complete button (only for available quests)
                if (isCompleted == null || !isCompleted) {
                    TextButton completeButton = new TextButton("Complete Quest", skin);
                    final int finalQuestIndex = questIndex;
                                            completeButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                // Call finishQuest method
                                Result result = controller.finishQuest(String.valueOf(quest.getId()));
                                
                                // Store result message
                                questResultMessage = result.Message();
                                questResultSuccess = result.isSuccessful();
                                
                                // Refresh the quest menu to show updated status
                                createQuestsMenu();
                            }
                        });
                    questEntry.add(completeButton).size(150, 40).pad(5).row();
                }
                
                questListTable.add(questEntry).fillX().pad(10).row();
                questIndex++;
            }
        } else {
            Label noQuestsLabel = new Label("No quests available for this NPC", skin);
            noQuestsLabel.setFontScale(1.0f);
            questListTable.add(noQuestsLabel).pad(20).row();
        }
        
        // Create scroll pane for quest list
        ScrollPane scrollPane = new ScrollPane(questListTable, skin);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setFadeScrollBars(false);
        
        npcMenuTable.add(scrollPane).colspan(2).fill().expand().pad(10).row();

        // Back and Close buttons
        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                questResultMessage = "";
                questResultSuccess = false;
                createNPCMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                questResultMessage = "";
                questResultSuccess = false;
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }
            }
        });

        npcMenuTable.add(backButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
    }

    private void createGiftMenu() {
        npcMenuTable.clear();

        Label titleLabel = new Label(selectedNPC.getType().name() + " Gift", skin);
        titleLabel.setFontScale(1.5f);
        npcMenuTable.add(titleLabel).colspan(2).pad(20).row();

        Label placeholderLabel = new Label("Gift functionality coming soon...", skin);
        placeholderLabel.setFontScale(1.0f);
        npcMenuTable.add(placeholderLabel).colspan(2).pad(20).row();

        // Back and Close buttons
        TextButton backButton = new TextButton("Back", skin);
        TextButton closeButton = new TextButton("Close", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                createNPCMenuUI();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showNPCMenu = false;
                selectedNPC = null;
                currentNPCMenuState = NPCMenuState.MAIN_MENU;
                if (npcMenuTable != null) {
                    npcMenuTable.remove();
                    npcMenuTable = null;
                }
            }
        });

        npcMenuTable.add(backButton).size(200, 50).pad(10);
        npcMenuTable.add(closeButton).size(200, 50).pad(10);
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

        if (tile.getPlant() != null && tile.getPlant() instanceof Crop) {
            Crop crop = (Crop) tile.getPlant();
            ItemType itemType = crop.getCropType();

            // Ensure we are only dealing with CropType enums here
            if (itemType instanceof CropType) {
                CropType cropType = (CropType) itemType;

                // Construct texture name based on growth stage
                // The stage is 1-based, so we don't need to add 1
                String textureKey = cropType.getEnumName() + "_Stage_" + crop.getCurrentStage();
                Texture cropTexture = textureManager.getTexture(textureKey);

                if (cropTexture != null) {
                    spriteBatch.draw(cropTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                }
            }
        } else if (tile.getSeed() != null) {
            String textureKey = tile.getSeed().getForagingSeedType().getEnumName();
            Texture seedTexture = textureManager.getTexture(textureKey);

            if (seedTexture != null) {
                spriteBatch.draw(seedTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void renderTreeSprite(int tileX, int tileY, float worldX, float worldY) {
        Tile tile = gameMap.getTiles()[tileX][tileY];

        if (tile.getPlant() instanceof Fruit) {
            Fruit fruitTree = (Fruit) tile.getPlant();
            TreeType treeType = fruitTree.getTreeType();

            if (treeType == null) return;

            // --- NEW RENDERING LOGIC ---
            // Determine the texture key based on the current growth stage
            String textureKey;
            if (fruitTree.isReadyToHarvest()) {
                // If it's ready to harvest, show the final stage with fruit
                textureKey = treeType.getEnumName() + "_Stage_5_Fruit";
            } else {
                // Otherwise, show the current growth stage
                textureKey = treeType.getEnumName() + "_Stage_" + fruitTree.getCurrentStage();
            }

            Texture treeTexture = textureManager.getTexture(textureKey);
            if (treeTexture == null) {
                // Fallback to a generic tree if a specific stage is missing
                treeTexture = tree1Texture;
            }

            spriteBatch.draw(treeTexture, worldX, worldY, TILE_SIZE * 1.5f, TILE_SIZE * 1.5f);

            // If it's ready to harvest, draw the fruit on top of the mature tree
            if (fruitTree.isReadyToHarvest()) {
                String fruitKey = fruitTree.getFruitType().getEnumName();
                Texture fruitTexture = textureManager.getTexture(fruitKey);
                if (fruitTexture != null) {
                    Random rand = new Random((long)tileX * tileY);
                    for(int i = 0; i < 3; i++) {
                        float fruitX = worldX + (rand.nextFloat() * (TILE_SIZE - 8));
                        float fruitY = worldY + (TILE_SIZE / 2f) + (rand.nextFloat() * (TILE_SIZE / 2f));
                        spriteBatch.draw(fruitTexture, fruitX, fruitY, TILE_SIZE / 2f, TILE_SIZE / 2f);
                    }
                }
            }
        } else if (tile.getTree() != null) {
            // Fallback for generic, non-fruiting trees
            Texture treeTexture;
            switch (treeVariantMap[tileX][tileY]) {
                case 0: treeTexture = tree1Texture; break;
                case 1: treeTexture = tree2Texture; break;
                case 2: treeTexture = tree3Texture; break;
                default: treeTexture = tree1Texture; break;
            }
            spriteBatch.draw(treeTexture, worldX, worldY, TILE_SIZE * 1.5f, TILE_SIZE * 1.5f);
        }
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
        // --- THIS VALUE HAS BEEN CHANGED ---
        float maxOpacity = 0.4f; // Was 0.85f, now it's less dense

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

    private void renderPlantingUI(float delta) {
        if (!isPlantingSelectionOpen) return;
        plantingStage.act(delta);
        plantingStage.draw();
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
                showInventoryDisplay(false);
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

    private void showInventoryDisplay(boolean showOnlyPlantables) {
        menuContentTable.clear();
        Table itemsTable = new Table();
        updateInventoryGrid(itemsTable, showOnlyPlantables); // Pass the filter flag

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = new TextureRegionDrawable(new TextureRegion(inventoryBackground));
        ScrollPane scrollPane = new ScrollPane(itemsTable, scrollPaneStyle);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        menuContentTable.add(scrollPane).expand().fill().pad(20).row();

        // In planting mode, don't show the regular back button
        if (!isPlantingMode) {
            addBackButtonToMenu();
        }
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

    private void updateInventoryGrid(Table table, boolean showOnlyPlantables) {
        table.clear();
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer == null) return;

        ArrayList<Item> items = currentPlayer.getInventory().getItems();
        if (showOnlyPlantables) {
            items = items.stream()
                .filter(item -> item instanceof Seed)
                .collect(Collectors.toCollection(ArrayList::new));
        }

        int column = 0;
        final int ITEMS_PER_ROW = 4;

        for (Item item : items) {
            String key = generateTextureKey(item);
            Texture texture = textureManager.getTexture(key);

            Table itemSlot = new Table(skin);
            itemSlot.setBackground("default-round");

            if (texture != null) {
                itemSlot.add(new Image(texture)).size(40, 40);
            } else {
                itemSlot.add(new Label("?", skin)).size(40, 40);
                Gdx.app.log("Inventory", "Missing texture for item: '" + item.getName() + "' (tried key: '" + key + "')");
            }

            itemSlot.row();
            itemSlot.add(new Label(String.valueOf(item.getNumber()), skin));

            if (showOnlyPlantables) {
                itemSlot.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // This is the core fix for the planting action
                        Result result = controller.plantItem(item, plantingTargetTile);

                        isPlantingMode = false;
                        plantingTargetTile = null;
                        isInventoryOpen = false;
                        plantingPromptLabel.setVisible(false);
                        Gdx.input.setInputProcessor(multiplexer);

                        // Show feedback to the player
                        generalMessageLabel.setText(result.Message());
                        generalMessageLabel.setVisible(true);
                        generalMessageTimer = GENERAL_MESSAGE_DURATION;
                    }
                });
            }

            table.add(itemSlot).pad(8);

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
        return item.getItemType().getEnumName();
    }

    private void setupToolMenuUI() {
        toolMenuStage = new Stage(new ScreenViewport());

        Table toolTable = new Table();
        toolTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        toolMenuStage.addActor(toolTable);

        currentToolImage = new Image();
        currentToolLabel = new Label("", skin);

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        TextButton acceptButton = new TextButton("Equip", skin);
        TextButton unequipButton = new TextButton("Unequip", skin); // Create the new button

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
                    isToolMenuOpen = false;
                }
            }
        });

        // Add listener for the unequip button
        unequipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getCurrentPlayer().setCurrentTool(null); // Set the current tool to null
                isToolMenuOpen = false; // Close menu
            }
        });

        // Layout the tool menu
        toolTable.add(leftButton).pad(5);
        toolTable.add(currentToolImage).size(64, 64).pad(5);
        toolTable.add(rightButton).pad(5);
        toolTable.row();
        toolTable.add(currentToolLabel).colspan(3).pad(5);
        toolTable.row();

        // Create a horizontal group for the buttons
        HorizontalGroup buttonGroup = new HorizontalGroup();
        buttonGroup.space(10);
        buttonGroup.addActor(acceptButton);
        buttonGroup.addActor(unequipButton); // Add the unequip button to the group

        toolTable.add(buttonGroup).colspan(3).pad(5);

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

    private void renderCheatMenu(float delta) {
        if (!isCheatMenuOpen) return;
        cheatMenuStage.act(delta);
        cheatMenuStage.draw();
    }

    private void setupCheatMenuUI() {
        cheatMenuStage = new Stage(new ScreenViewport());
        Table cheatTable = new Table(skin);
        cheatTable.setBackground(skin.newDrawable("white", new Color(0.1f, 0.1f, 0.1f, 0.8f)));
        cheatMenuStage.addActor(cheatTable);

        cheatItemNameField = new TextField("", skin);
        cheatItemNameField.setMessageText("Item Name");
        cheatItemQuantityField = new TextField("", skin);
        cheatItemQuantityField.setMessageText("Quantity");

        TextButton addButton = new TextButton("Add Item", skin);
        TextButton closeButton = new TextButton("Close", skin); // Create a close button
        Label cheatMessageLabel = new Label("", skin);

        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String itemName = cheatItemNameField.getText();
                String quantityStr = cheatItemQuantityField.getText();
                Result result = controller.cheatAddItem(itemName, quantityStr);
                cheatMessageLabel.setText(result.Message());
            }
        });

        // Add a listener to the close button
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isCheatMenuOpen = false;
                Gdx.input.setInputProcessor(multiplexer); // Return focus to the game
            }
        });

        cheatTable.add(new Label("Cheat Menu", skin)).colspan(2).pad(10).row();
        cheatTable.add(cheatItemNameField).width(200).pad(5);
        cheatTable.add(cheatItemQuantityField).width(100).pad(5).row();

        // Add both buttons to the table
        Table buttonTable = new Table();
        buttonTable.add(addButton).pad(5);
        buttonTable.add(closeButton).pad(5);
        cheatTable.add(buttonTable).colspan(2).pad(10).row();

        cheatTable.add(cheatMessageLabel).colspan(2).pad(10);
        cheatTable.pack();
        cheatTable.setPosition(Gdx.graphics.getWidth() / 2f - cheatTable.getWidth() / 2f,
            Gdx.graphics.getHeight() / 2f - cheatTable.getHeight() / 2f);
    }

    private void setupPlantingUI() {
        plantingStage = new Stage(new ScreenViewport());
        Table mainTable = new Table(skin);
        mainTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.7f)));
        mainTable.setPosition(Gdx.graphics.getWidth() / 2f, 80, com.badlogic.gdx.utils.Align.center);

        plantingItemNameLabel = new Label("", skin);
        plantingItemsGroup = new HorizontalGroup();
        plantingItemsGroup.space(10);
        plantingSelectionHighlight = new Image(skin.newDrawable("white", Color.RED));
        plantingSelectionHighlight.setSize(52, 52);

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        TextButton selectButton = new TextButton("Plant", skin);
        TextButton closeButton = new TextButton("Close", skin);

        leftButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (!plantableItems.isEmpty()) {
                    selectedPlantableIndex = (selectedPlantableIndex - 1 + plantableItems.size()) % plantableItems.size();
                    updatePlantingSelection();
                }
            }
        });
        rightButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (!plantableItems.isEmpty()) {
                    selectedPlantableIndex = (selectedPlantableIndex + 1) % plantableItems.size();
                    updatePlantingSelection();
                }
            }
        });
        selectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (!plantableItems.isEmpty() && plantingTargetTile != null) {
                    Item selectedItem = plantableItems.get(selectedPlantableIndex);
                    controller.plantItem(selectedItem, plantingTargetTile);
                    closePlantingMenu();
                }
            }
        });
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                closePlantingMenu();
            }
        });

        mainTable.add(plantingItemNameLabel).colspan(4).pad(5).row();
        mainTable.add(leftButton).pad(10);
        mainTable.add(plantingItemsGroup).pad(10);
        mainTable.add(rightButton).pad(10).row();
        mainTable.add(selectButton).colspan(2).pad(5);
        mainTable.add(closeButton).colspan(2).pad(5);
        plantingStage.addActor(mainTable);
    }

    private void updatePlantingSelection() {
        plantingItemsGroup.clear();
        if (plantableItems.isEmpty()) {
            plantingItemNameLabel.setText("No seeds to plant!");
            return;
        }
        Item selectedItem = plantableItems.get(selectedPlantableIndex);
        plantingItemNameLabel.setText(selectedItem.getName());
        Stack itemStack = new Stack();
        itemStack.add(plantingSelectionHighlight);
        Texture itemTexture = textureManager.getTexture(generateTextureKey(selectedItem));
        if (itemTexture != null) {
            itemStack.add(new Image(itemTexture));
        }
        plantingItemsGroup.addActor(itemStack);
    }

    private void openPlantingMenu(Tile target) {
        plantableItems.clear();
        plantableItems.addAll(
            game.getCurrentPlayer().getInventory().getItems().stream()
                .filter(item -> item instanceof Seed)
                .collect(Collectors.toCollection(ArrayList::new))
        );
        if (plantableItems.isEmpty()) {
            generalMessageLabel.setText("You have no seeds to plant.");
            generalMessageLabel.setVisible(true);
            generalMessageTimer = GENERAL_MESSAGE_DURATION;
            return;
        }
        isPlantingSelectionOpen = true;
        plantingTargetTile = target;
        selectedPlantableIndex = 0;
        updatePlantingSelection();
        Gdx.input.setInputProcessor(plantingStage);
    }

    private void closePlantingMenu() {
        isPlantingSelectionOpen = false;
        plantingTargetTile = null;
        Gdx.input.setInputProcessor(multiplexer);
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
        cheatMenuStage.dispose();

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

        // Dispose NPC interaction textures
        dialogBoxTexture.dispose();
        menuBackgroundTexture.dispose();

        textureManager.dispose();
    }
    
    private void createShopMenuUI() {
        if (shopMenuTable != null) {
            shopMenuTable.remove();
        }

        shopMenuTable = new Table();
        // Make the background 75% of screen size
        shopMenuTable.setSize(Gdx.graphics.getWidth() * 0.75f, Gdx.graphics.getHeight() * 0.75f);
        shopMenuTable.setPosition(
            (Gdx.graphics.getWidth() - shopMenuTable.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - shopMenuTable.getHeight()) / 2f
        );
        
        // Create a background drawable from the menu background texture
        TextureRegionDrawable backgroundDrawable = new TextureRegionDrawable(menuBackgroundTexture);
        shopMenuTable.setBackground(backgroundDrawable);
        
        stage.addActor(shopMenuTable);
        
        // Create the appropriate menu based on state
        switch (currentShopMenuState) {
            case MAIN_MENU:
                createMainShopMenu();
                break;
            case ALL_PRODUCTS:
                createAllProductsMenu();
                break;
            case AVAILABLE_PRODUCTS:
                createAvailableProductsMenu();
                break;
        }
    }
    
    private void createMainShopMenu() {
        shopMenuTable.clear();
        
        // Add shop name at the top with blue color
        Label titleLabel = new Label(currentShopType.getName(), skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.BLUE);
        shopMenuTable.add(titleLabel).padBottom(20).row();
        
        // Available Products button
        TextButton availableProductsButton = new TextButton("Available Products", skin);
        availableProductsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.AVAILABLE_PRODUCTS;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        shopMenuTable.add(availableProductsButton).width(200).pad(10).row();
        
        // All Products button
        TextButton allProductsButton = new TextButton("All Products", skin);
        allProductsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.ALL_PRODUCTS;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        shopMenuTable.add(allProductsButton).width(200).pad(10).row();
        
        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showShopMenu = false;
                if (shopMenuTable != null) {
                    shopMenuTable.remove();
                    shopMenuTable = null;
                }
            }
        });
        shopMenuTable.add(closeButton).width(200).pad(10).row();
    }
    
    private void createAllProductsMenu() {
        shopMenuTable.clear();
        // Add shop name at the top with blue color
        Label titleLabel = new Label(currentShopType.getName() + " - All Products", skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.BLUE);
        shopMenuTable.add(titleLabel).padBottom(20).row();
        // Show result message if exists
        if (!shopResultMessage.isEmpty()) {
            Label resultLabel = new Label(shopResultMessage, skin);
            resultLabel.setColor(shopResultSuccess ? Color.GREEN : Color.RED);
            resultLabel.setFontScale(1.1f);
            shopMenuTable.add(resultLabel).padBottom(15).row();
        }
        // Get all products from the shop controller
        Result result = shopController.showAllProducts();
        String productsText = result.isSuccessful() ? result.Message() : "Failed to load products";
        // Create a table to hold all product cards
        Table productsTable = new Table();
        if (result.isSuccessful() && !productsText.trim().isEmpty()) {
            String[] products = productsText.split("\n");
            for (String product : products) {
                if (product.trim().isEmpty()) continue;
                
                // Create beautiful product card similar to trade menu
                Table productCard = new Table();
                productCard.setBackground(skin.getDrawable("default-round"));
                productCard.pad(15);
                
                // Product name and price
                Label productLabel = new Label(product.trim(), skin);
                productLabel.setColor(Color.WHITE);
                productLabel.setFontScale(1.2f);
                productCard.add(productLabel).colspan(2).padBottom(10).row();
                
                productsTable.add(productCard).width(500).pad(15).row();
            }
        } else {
            Label noProductsLabel = new Label("No products available", skin);
            noProductsLabel.setFontScale(1.2f);
            noProductsLabel.setColor(Color.GRAY);
            productsTable.add(noProductsLabel).pad(30).row();
        }
        // Create scroll pane for the products with proper configuration
        ScrollPane scrollPane = new ScrollPane(productsTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setScrollingDisabled(false, false);
        // Add scroll pane to main table with fixed height
        shopMenuTable.add(scrollPane).width(550).height(400).pad(10).row();
        // Back and Close buttons (always show both)
        Table buttonTable = new Table();
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.MAIN_MENU;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        buttonTable.add(backButton).width(200).pad(20);
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showShopMenu = false;
                if (shopMenuTable != null) {
                    shopMenuTable.remove();
                    shopMenuTable = null;
                }
            }
        });
        buttonTable.add(closeButton).width(200).pad(20);
        shopMenuTable.add(buttonTable).row();
    }
    
    private void createAvailableProductsMenu() {
        shopMenuTable.clear();
        // Add shop name at the top with blue color
        Label titleLabel = new Label(currentShopType.getName() + " - Available Products", skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.BLUE);
        shopMenuTable.add(titleLabel).padBottom(20).row();
        // Show result message if exists
        if (!shopResultMessage.isEmpty()) {
            Label resultLabel = new Label(shopResultMessage, skin);
            resultLabel.setColor(shopResultSuccess ? Color.GREEN : Color.RED);
            resultLabel.setFontScale(1.1f);
            shopMenuTable.add(resultLabel).padBottom(15).row();
        }
        // Get available products from the shop controller
        Result result = shopController.showAvailableProducts();
        String productsText = result.isSuccessful() ? result.Message() : "Failed to load products";
        // Create a table to hold all product cards
        Table productsTable = new Table();
        if (result.isSuccessful() && !productsText.trim().isEmpty()) {
            String[] products = productsText.split("\n");
            for (int i = 0; i < products.length; i += 2) { // Each product and its stock info
                if (i >= products.length) break;
                String product = products[i];
                String stockInfo = (i + 1 < products.length) ? products[i + 1] : "";
                if (product.trim().isEmpty()) continue;
                
                // Extract product name for purchase
                String productName = extractProductName(product);
                
                // Create display text with stock info
                String displayText = product.trim();
                if (!stockInfo.isEmpty()) {
                    displayText += " | " + stockInfo;
                }
                Table productCard = new Table();
                productCard.setBackground(skin.getDrawable("default-round"));
                productCard.pad(15);
                
                // Product name and price with stock info
                Label productLabel = new Label(displayText, skin);
                productLabel.setColor(Color.WHITE);
                productLabel.setFontScale(1.2f);
                productCard.add(productLabel).colspan(4).padBottom(10).row();
                // Purchase controls
                Label amountLabel = new Label("Amount:", skin);
                amountLabel.setColor(Color.WHITE);
                productCard.add(amountLabel).padRight(5);
                TextField amountField = new TextField("1", skin);
                amountField.setWidth(80);
                amountField.setMaxLength(3);
                amountField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
                productCard.add(amountField).padRight(5);
                TextButton plusButton = new TextButton("+", skin);
                plusButton.setWidth(30);
                plusButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        try {
                            int currentAmount = Integer.parseInt(amountField.getText());
                            amountField.setText(String.valueOf(currentAmount + 1));
                        } catch (NumberFormatException e) {
                            amountField.setText("1");
                        }
                    }
                });
                productCard.add(plusButton).padRight(10);
                TextButton purchaseButton = new TextButton("Purchase", skin);
                purchaseButton.setWidth(100);
                purchaseButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        try {
                            int amount = Integer.parseInt(amountField.getText());
                            if (amount > 0) {
                                Result purchaseResult = shopController.purchase(productName, String.valueOf(amount));
                                shopResultMessage = purchaseResult.Message();
                                shopResultSuccess = purchaseResult.isSuccessful();
                                createShopMenuUI(); // Refresh the menu to show result
                            }
                        } catch (NumberFormatException e) {
                            shopResultMessage = "Invalid amount!";
                            shopResultSuccess = false;
                            createShopMenuUI();
                        }
                    }
                });
                productCard.add(purchaseButton).row();
                productsTable.add(productCard).width(500).pad(15).row();
            }
        } else {
            Label noProductsLabel = new Label("No products available", skin);
            noProductsLabel.setFontScale(1.2f);
            noProductsLabel.setColor(Color.GRAY);
            productsTable.add(noProductsLabel).pad(30).row();
        }
        // Create scroll pane for the products with proper configuration
        ScrollPane scrollPane = new ScrollPane(productsTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setScrollingDisabled(false, false);
        // Add scroll pane to main table with fixed height
        shopMenuTable.add(scrollPane).width(550).height(400).pad(10).row();
        // Back and Close buttons (always show both)
        Table buttonTable = new Table();
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentShopMenuState = ShopMenuState.MAIN_MENU;
                shopResultMessage = "";
                createShopMenuUI();
            }
        });
        buttonTable.add(backButton).width(200).pad(20);
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showShopMenu = false;
                if (shopMenuTable != null) {
                    shopMenuTable.remove();
                    shopMenuTable = null;
                }
            }
        });
        buttonTable.add(closeButton).width(200).pad(20);
        shopMenuTable.add(buttonTable).row();
    }
    
    private String extractProductName(String productText) {
        // Extract the product name from the product text
        // The format is usually "Product Name - Price: X"
        if (productText.contains(" - ")) {
            return productText.split(" - ")[0].trim();
        }
        return productText.trim();
    }
    

}
