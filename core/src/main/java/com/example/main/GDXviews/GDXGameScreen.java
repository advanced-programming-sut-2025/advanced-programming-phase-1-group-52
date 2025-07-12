package com.example.main.GDXviews;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.main.controller.GameMenuController;
import com.example.main.enums.design.TileType;
import com.example.main.models.App;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.Player;
import com.example.main.models.Tile;
import com.example.main.models.User;

public class GDXGameScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private GameMap gameMap;
    private GameMenuController controller;
    
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private com.badlogic.gdx.graphics.glutils.ShapeRenderer shapeRenderer;
    
    private float cameraSpeed = 200f;
    private boolean cameraFollowsPlayer = true;
    
    private boolean showMinimap = false;
    private boolean mKeyPressed = false;
    
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
        {1, 51},
        {81, 51}
    };
    
    private static final int[][] HOUSE_AREAS = {
        {1, 8, 1, 8},
        {81, 88, 1, 8},
        {1, 8, 51, 58},
        {81, 88, 51, 58}
    };
    
    private static final int[][] NPC_HOUSE_POSITIONS = {
        {32, 40},
        {42, 40},
        {52, 40},
        {37, 50},
        {47, 50}
    };
    
    private static final int[][] NPC_HOUSE_AREAS = {
        {32, 35, 40, 45},
        {42, 45, 40, 45},
        {52, 55, 40, 45},
        {37, 40, 50, 55},
        {47, 50, 50, 55}
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
        {33, 37, 3, 7},
        {47, 51, 3, 7},
        {33, 37, 13, 17},
        {47, 51, 13, 17},
        {33, 37, 23, 27},
        {47, 51, 23, 27},
        {33, 37, 33, 37}
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
        shapeRenderer = new com.badlogic.gdx.graphics.glutils.ShapeRenderer();
        
        float worldWidth = MAP_WIDTH * TILE_SIZE;
        float worldHeight = MAP_HEIGHT * TILE_SIZE;
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        camera.setToOrtho(false, screenWidth, screenHeight);
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

        game = App.getInstance().getCurrentGame();
        gameMap = game.getMap();
        
        controller.setGame(game);
        controller.setMap(gameMap);
        
        generateRandomMaps();
        
        initializePlayerPosition();
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
            blacksmithTexture = new Texture("content/Cut/map_elements/Blacksmith.png");
            jojamartTexture = new Texture("content/Cut/map_elements/Jojamart.png");
            pierresShopTexture = new Texture("content/Cut/map_elements/Pierres_shop.png");
            carpentersShopTexture = new Texture("content/Cut/map_elements/Carpenter's_Shop.png");
            fishShopTexture = new Texture("content/Cut/map_elements/Fish_Shop.png");
            ranchTexture = new Texture("content/Cut/map_elements/Ranch.png");
            saloonTexture = new Texture("content/Cut/map_elements/Saloon.png");
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
        handleMinimapToggle();
        handleTurnSwitching();
        handlePlayerMovement(delta);
        handleCameraMovement(delta);
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

    @Override
    public void render(float delta) {
        handleInput(delta);
        
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        
        spriteBatch.begin();
        renderMap();
        spriteBatch.end();
        
        if (showMinimap) {
            renderMinimap();
        }
        
        stage.act(delta);
        stage.draw();
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
                    if (tile.getType() == TileType.Tree) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderTreeSprite(x, y, worldX, worldY);
                    }
                }
            }
        }
        
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    if (tile.getType() == TileType.House) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderHouseSprite(x, y, worldX, worldY);
                    } else if (tile.getType() == TileType.NPCHouse) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderNPCHouseSprite(x, y, worldX, worldY);
                    } else if (tile.getType() == TileType.Shop) {
                        float worldX = x * TILE_SIZE;
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderShopSprite(x, y, worldX, worldY);
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
        int houseStartY = houseArea[2];
        
        if (tileX == houseStartX && tileY == houseStartY) {
            float houseWidth = houseTexture.getWidth();
            float houseHeight = houseTexture.getHeight();
            
            float houseX = worldX;
            float houseY = worldY;
            
            spriteBatch.draw(houseTexture, houseX, houseY, houseWidth, houseHeight);
        }
    }
    
    private void renderNPCHouseSprite(int tileX, int tileY, float worldX, float worldY) {
        int npcIndex = getNPCIndexForHouse(tileX, tileY);
        if (npcIndex == -1) return;
        
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
        int npcHouseStartX = npcHouseArea[0];
        int npcHouseStartY = npcHouseArea[2];
        
        if (tileX == npcHouseStartX && tileY == npcHouseStartY) {
            float npcHouseWidth = npcHouseTexture.getWidth();
            float npcHouseHeight = npcHouseTexture.getHeight();
            
            float npcHouseX = worldX;
            float npcHouseY = worldY;
            
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
            int shopWidthInTiles = shopArea[1] - shopArea[0] + 1;
            int shopHeightInTiles = shopArea[3] - shopArea[2] + 1;
            
            float shopWidth = shopWidthInTiles * TILE_SIZE;
            float shopHeight = shopHeightInTiles * TILE_SIZE;
            
            float shopX = worldX;
            float shopY = worldY;
            
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
    }
}