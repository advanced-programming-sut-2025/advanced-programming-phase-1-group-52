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
import com.example.main.models.Tile;

public class GDXGameScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private Game game;
    private GameMap gameMap;
    private GameMenuController controller;
    
    // Rendering components
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    
    // Camera controls
    private float cameraSpeed = 200f; // pixels per second
    
    // Direct texture loading (no TileExtractor)
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
    
    // Random base ground assignment for each map position
    private int[][] baseGroundMap;  // 0 = ground1, 1 = ground2
    // Store random choices for grass and tree overlays to prevent flickering
    private int[][] grassVariantMap;  // 0 = grass1, 1 = grass2
    private int[][] treeVariantMap;   // 0 = tree1, 1 = tree2, 2 = tree3
    // Store random house choices for each player (0-3 for house1-house3)
    private int[] playerHouseVariants;  // One per player
    // Store random house choices for each NPC (0-4 for npc_house1-npc_house5)
    private int[] npcHouseVariants;     // One per NPC
    // Store random stone choices for regular stones (0 = stone1, 1 = stone2)
    private int[][] stoneVariantMap;    // One per stone position
    private Random random;
    
    // Player house coordinates from GameMap.java
    // Houses should be positioned at the top-left wall corner
    private static final int[][] HOUSE_POSITIONS = {
        {1, 1},   // Player 0: generateBuilding(players, 0, TileType.House, 1, 8, 1, 8) -> wall starts at (1,1)
        {81, 1},  // Player 1: generateBuilding(players, 1, TileType.House, 81, 88, 1, 8) -> wall starts at (81,1)  
        {1, 31},  // Player 2: generateBuilding(players, 2, TileType.House, 1, 8, 31, 38) -> wall starts at (1,31)
        {81, 31}  // Player 3: generateBuilding(players, 3, TileType.House, 81, 88, 31, 38) -> wall starts at (81,31)
    };
    
    private static final int[][] HOUSE_AREAS = {
        {1, 8, 1, 8},      // Player 0: x 1-8, y 1-8 (full building area including walls)
        {81, 88, 1, 8},    // Player 1: x 81-88, y 1-8
        {1, 8, 31, 38},    // Player 2: x 1-8, y 31-38  
        {81, 88, 31, 38}   // Player 3: x 81-88, y 31-38
    };
    
    // NPC house coordinates from NPCType enum (Sebastian, Abigail, Harvey, Lia, Robin)
    // Houses are 5x7 (width x height) from generateBuilding call: cornerX+4, cornerY+6
    private static final int[][] NPC_HOUSE_POSITIONS = {
        {42, 32},  // Sebastian: (42, 32)
        {52, 32},  // Abigail: (52, 32)  
        {32, 42},  // Harvey: (32, 42)
        {42, 42},  // Lia: (42, 42)
        {52, 42}   // Robin: (52, 42)
    };
    
    private static final int[][] NPC_HOUSE_AREAS = {
        {42, 46, 32, 38},  // Sebastian: x 42-46, y 32-38 (5x7 area)
        {52, 56, 32, 38},  // Abigail: x 52-56, y 32-38
        {32, 36, 42, 48},  // Harvey: x 32-36, y 42-48
        {42, 46, 42, 48},  // Lia: x 42-46, y 42-48
        {52, 56, 42, 48}   // Robin: x 52-56, y 42-48
    };
    
    // Tile size constants
    private static final int TILE_SIZE = 32;
    private static final int MAP_WIDTH = 90;
    private static final int MAP_HEIGHT = 60;

    public GDXGameScreen() {
        controller = new GameMenuController();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        
        // Initialize rendering components
        spriteBatch = new SpriteBatch();
        camera = new OrthographicCamera();
        
        // Calculate world size in pixels
        float worldWidth = MAP_WIDTH * TILE_SIZE;  // 90 * 32 = 2880 pixels
        float worldHeight = MAP_HEIGHT * TILE_SIZE; // 60 * 32 = 1920 pixels
        
        // Set camera to show the entire map with some padding
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        // Calculate zoom to fit the entire map on screen
        float zoomX = screenWidth / worldWidth;
        float zoomY = screenHeight / worldHeight;
        float zoom = Math.min(zoomX, zoomY) * 0.9f; // 0.9f for some padding
        
        camera.setToOrtho(false, screenWidth / zoom, screenHeight / zoom);
        
        // Center camera on the map
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        camera.update();
        
        // Initialize random and base tile map
        random = new Random();
        baseGroundMap = new int[MAP_WIDTH][MAP_HEIGHT];
        grassVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        treeVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        playerHouseVariants = new int[4]; // 4 players
        npcHouseVariants = new int[5]; // 5 NPCs
        stoneVariantMap = new int[MAP_WIDTH][MAP_HEIGHT];
        
        loadTextures();

        game = App.getInstance().getCurrentGame();
        gameMap = game.getMap();
        
        generateRandomMaps();
    }
    
    private void loadTextures() {
        // Load all cut textures directly
        ground1Texture = new Texture("content/Cut/ground1.png");
        ground2Texture = new Texture("content/Cut/ground2.png");
        grass1Texture = new Texture("content/Cut/grass1.png");
        grass2Texture = new Texture("content/Cut/grass2.png");
        shoveledTexture = new Texture("content/Cut/shoveled.png");
        tree1Texture = new Texture("content/Cut/tree1.png");
        tree2Texture = new Texture("content/Cut/tree2.png");
        tree3Texture = new Texture("content/Cut/tree3.png");
        house1Texture = new Texture("content/Cut/player_house1.png");
        house2Texture = new Texture("content/Cut/player_house2.png");
        house3Texture = new Texture("content/Cut/player_house3.png");
        npcHouse1Texture = new Texture("content/Cut/npc_house1.png");
        npcHouse2Texture = new Texture("content/Cut/npc_house2.png");
        npcHouse3Texture = new Texture("content/Cut/npc_house3.png");
        npcHouse4Texture = new Texture("content/Cut/npc_house4.png");
        npcHouse5Texture = new Texture("content/Cut/npc_house5.png");
        bushTexture = new Texture("content/Cut/bush.png");
        stone1Texture = new Texture("content/Cut/stone1.png");
        stone2Texture = new Texture("content/Cut/stone2.png");
        copperStoneTexture = new Texture("content/Cut/copper_stone.png");
        ironStoneTexture = new Texture("content/Cut/iron_stone.png");
        goldStoneTexture = new Texture("content/Cut/gold_stone.png");
        iridiumStoneTexture = new Texture("content/Cut/iridium_stone.png");
        jewelStoneTexture = new Texture("content/Cut/jewel_stone.png");
    }
    
    private void generateRandomMaps() {
        // Generate random choices for every position on the map
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                // Random ground (0 = ground1, 1 = ground2)
                baseGroundMap[x][y] = random.nextInt(2);
                
                // Random grass variant (0 = grass1, 1 = grass2)
                grassVariantMap[x][y] = random.nextInt(2);
                
                // Random tree variant (0 = tree1, 1 = tree2, 2 = tree3)
                treeVariantMap[x][y] = random.nextInt(3);

                // Random stone variant (0 = stone1, 1 = stone2)
                stoneVariantMap[x][y] = random.nextInt(2);
            }
        }
        
        // Generate random house variants for each player
        for (int i = 0; i < 4; i++) {
            playerHouseVariants[i] = random.nextInt(3); // 0-2 for house1-house3
        }

        // Generate random house variants for each NPC
        for (int i = 0; i < 5; i++) {
            npcHouseVariants[i] = i; // Assign each NPC their specific house (0=house1, 1=house2, etc.)
        }
    }
    
    private void handleCameraInput(float delta) {
        float movement = cameraSpeed * delta;
        
        // WASD or Arrow key movement
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += movement;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= movement;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= movement;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += movement;
        }
        
        // Zoom controls
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
        }
        
        // Clamp zoom
        camera.zoom = Math.max(0.1f, Math.min(camera.zoom, 3.0f));
        
        // Keep camera within map bounds
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
        // Handle camera input
        handleCameraInput(delta);
        
        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Update camera
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        
        // Render map
        spriteBatch.begin();
        renderMap();
        spriteBatch.end();
        
        // Render UI
        stage.act(delta);
        stage.draw();
    }
    
    private void renderMap() {
        Tile[][] tiles = gameMap.getTiles();
        
        // First pass: Render base ground and non-tree overlays
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                float worldX = x * TILE_SIZE;
                // Flip Y coordinate to fix upside-down rendering
                float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                
                // Always render base ground (ground1 or ground2)
                renderBaseGround(x, y, worldX, worldY);
                
                // Render non-tree overlays if they exist
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    TileType tileType = tile.getType();
                    
                    // Render everything except trees in first pass
                    if (tileType != TileType.Tree) {
                        renderTileOverlay(tile, x, y, worldX, worldY);
                    }
                }
            }
        }
        
        // Second pass: Render trees in proper Z-order (back to front)
        // Since Y is flipped, we need to render from y=0 to y=MAP_HEIGHT-1 
        // so trees with higher flipped Y coordinates (visually higher up) render first (behind)
        // and trees with lower flipped Y coordinates (visually lower down) render last (in front)
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    if (tile.getType() == TileType.Tree) {
                        float worldX = x * TILE_SIZE;
                        // Flip Y coordinate to fix upside-down rendering
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderTreeSprite(x, y, worldX, worldY);
                    }
                }
            }
        }
        
        // Third pass: Render houses on top of everything (back to front for proper layering)
        // Since Y is flipped, we need to render from y=0 to y=MAP_HEIGHT-1
        // so houses with higher flipped Y coordinates (visually higher up) render first (behind)
        // and houses with lower flipped Y coordinates (visually lower down) render last (in front)
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    // Render houses for House, Wall, and NPCHouse tile types
                    if (tile.getType() == TileType.House || tile.getType() == TileType.Wall || tile.getType() == TileType.NPCHouse) {
                        float worldX = x * TILE_SIZE;
                        // Flip Y coordinate to fix upside-down rendering
                        float worldY = (MAP_HEIGHT - 1 - y) * TILE_SIZE;
                        renderHouseSprite(x, y, worldX, worldY);
                    }
                }
            }
        }
    }
    
    private void renderBaseGround(int x, int y, float worldX, float worldY) {
        Texture groundTexture = baseGroundMap[x][y] == 0 ? ground1Texture : ground2Texture;
        spriteBatch.draw(groundTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
    }
    
    private void renderTileOverlay(Tile tile, int tileX, int tileY, float worldX, float worldY) {
        TileType tileType = tile.getType();
        
        switch (tileType) {
            case Grass:
                // Render grass overlay ON TOP of ground using pre-generated random choice
                Texture grassTexture = grassVariantMap[tileX][tileY] == 0 ? grass1Texture : grass2Texture;
                spriteBatch.draw(grassTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case Shoveled:
                // Render shoveled overlay ON TOP of ground
                spriteBatch.draw(shoveledTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case Bush:
                // Render bush overlay ON TOP of ground
                spriteBatch.draw(bushTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case Stone:
                // Render stone overlay ON TOP of ground using pre-generated random choice
                Texture stoneTexture = stoneVariantMap[tileX][tileY] == 0 ? stone1Texture : stone2Texture;
                spriteBatch.draw(stoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case CopperStone:
                // Render copper stone overlay ON TOP of ground
                spriteBatch.draw(copperStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case IronStone:
                // Render iron stone overlay ON TOP of ground
                spriteBatch.draw(ironStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case GoldStone:
                // Render gold stone overlay ON TOP of ground
                spriteBatch.draw(goldStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case IridiumStone:
                // Render iridium stone overlay ON TOP of ground
                spriteBatch.draw(iridiumStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            case JewelStone:
                // Render jewel stone overlay ON TOP of ground
                spriteBatch.draw(jewelStoneTexture, worldX, worldY, TILE_SIZE, TILE_SIZE);
                break;
                
            // Trees are now handled separately in renderTreeSprite for proper Z-ordering
            case Tree:
                // Do nothing here - trees are rendered in separate pass
                break;
                
            // Houses are now handled separately in renderHouseSprite for proper Z-ordering
            case House:
                // Do nothing here - houses are rendered in separate pass
                break;
                
            // NPC Houses are now handled separately in renderHouseSprite for proper Z-ordering  
            case NPCHouse:
                // Do nothing here - NPC houses are rendered in separate pass
                break;
                
            // Wall tiles should render normally - they're part of the base structure
            case Wall:
                // Walls get no special overlay - just show the base ground
                break;
                
            // Add other tile types as needed
            default:
                // No overlay for other tile types - just show the base ground
                break;
        }
    }
    
    private void renderTreeSprite(int tileX, int tileY, float worldX, float worldY) {
        // Render tree sprite using pre-generated random choice
        Texture treeTexture;
        switch (treeVariantMap[tileX][tileY]) {
            case 0: treeTexture = tree1Texture; break;
            case 1: treeTexture = tree2Texture; break;
            case 2: treeTexture = tree3Texture; break;
            default: treeTexture = tree1Texture; break;
        }
        
        // Trees maintain their original size but are positioned on the tile
        float treeWidth = treeTexture.getWidth();
        float treeHeight = treeTexture.getHeight();
        
        // Center the tree horizontally on the tile, align bottom with tile
        float treeX = worldX + (TILE_SIZE - treeWidth) / 2f;
        float treeY = worldY - (treeHeight - TILE_SIZE); // Tree extends upward from tile
        
        spriteBatch.draw(treeTexture, treeX, treeY, treeWidth, treeHeight);
    }
    
    private void renderHouseSprite(int tileX, int tileY, float worldX, float worldY) {
        // Determine which player's house this is based on coordinates
        int playerIndex = getPlayerIndexForHouse(tileX, tileY);
        if (playerIndex == -1) {
            // Check if it's an NPC house
            int npcIndex = getNPCIndexForHouse(tileX, tileY);
            if (npcIndex != -1) {
                renderNPCHouseSprite(tileX, tileY, worldX, worldY);
                return;
            }
            return; // Not a house position we recognize
        }
        
        // Only render the house image at the top-left corner of the house area
        int[] housePos = HOUSE_POSITIONS[playerIndex];
        if (tileX != housePos[0] || tileY != housePos[1]) {
            return; // Not the top-left corner, don't render here
        }
        
        // Get the house texture for this player
        Texture houseTexture;
        switch (playerHouseVariants[playerIndex]) {
            case 0: houseTexture = house1Texture; break;
            case 1: houseTexture = house2Texture; break;
            case 2: houseTexture = house3Texture; break;
            default: houseTexture = house1Texture; break;
        }
        
        // Player houses are 8x8 tiles, so scale to fit exactly
        float houseWidth = 8 * TILE_SIZE;  // 8 tiles wide
        float houseHeight = 8 * TILE_SIZE; // 8 tiles tall
        
        // Adjust Y position so bottom-left of house aligns with bottom-left of house area
        // Since Y is flipped, we need to move the house down by its height minus one tile
        float adjustedY = worldY - houseHeight + TILE_SIZE;
        
        // Render house at its adjusted position
        spriteBatch.draw(houseTexture, worldX, adjustedY, houseWidth, houseHeight);
    }
    
    private void renderNPCHouseSprite(int tileX, int tileY, float worldX, float worldY) {
        // Determine which NPC's house this is based on coordinates
        int npcIndex = getNPCIndexForHouse(tileX, tileY);
        if (npcIndex == -1) return; // Not an NPC house position we recognize

        // Only render the NPC house image at the top-left corner of the NPC house area
        int[] npcHousePos = NPC_HOUSE_POSITIONS[npcIndex];
        if (tileX != npcHousePos[0] || tileY != npcHousePos[1]) {
            return; // Not the top-left corner, don't render here
        }

        // Get the NPC house texture for this NPC
        Texture npcHouseTexture;
        switch (npcHouseVariants[npcIndex]) {
            case 0: npcHouseTexture = npcHouse1Texture; break;
            case 1: npcHouseTexture = npcHouse2Texture; break;
            case 2: npcHouseTexture = npcHouse3Texture; break;
            case 3: npcHouseTexture = npcHouse4Texture; break;
            case 4: npcHouseTexture = npcHouse5Texture; break;
            default: npcHouseTexture = npcHouse1Texture; break;
        }

        // NPC houses are 5x7 tiles, so scale to fit exactly
        float npcHouseWidth = 5 * TILE_SIZE;  // 5 tiles wide
        float npcHouseHeight = 7 * TILE_SIZE; // 7 tiles tall

        // Adjust Y position so bottom-left of house aligns with bottom-left of house area
        // Since Y is flipped, we need to move the house down by its height minus one tile
        float adjustedY = worldY - npcHouseHeight + TILE_SIZE;

        // Render NPC house at its adjusted position
        spriteBatch.draw(npcHouseTexture, worldX, adjustedY, npcHouseWidth, npcHouseHeight);
    }
    
    private int getPlayerIndexForHouse(int x, int y) {
        // Check which player's house area this coordinate belongs to
        for (int i = 0; i < 4; i++) {
            int[] area = HOUSE_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1; // Not in any house area
    }

    private int getNPCIndexForHouse(int x, int y) {
        // Check which NPC's house area this coordinate belongs to
        for (int i = 0; i < 5; i++) {
            int[] area = NPC_HOUSE_AREAS[i];
            if (x >= area[0] && x <= area[1] && y >= area[2] && y <= area[3]) {
                return i;
            }
        }
        return -1; // Not in any NPC house area
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
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
        spriteBatch.dispose();
        
        // Dispose of all textures
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
        
        stage.dispose();
        skin.dispose();
    }
}