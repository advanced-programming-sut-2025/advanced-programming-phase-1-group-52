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
    
    // Random base ground assignment for each map position
    private int[][] baseGroundMap;  // 0 = ground1, 1 = ground2
    // Store random choices for grass and tree overlays to prevent flickering
    private int[][] grassVariantMap;  // 0 = grass1, 1 = grass2
    private int[][] treeVariantMap;   // 0 = tree1, 1 = tree2, 2 = tree3
    private Random random;
    
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
            }
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
                float worldY = y * TILE_SIZE;
                
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
        // We need to render trees from top to bottom (high Y to low Y) so trees
        // lower on screen appear in front of trees higher on screen
        for (int y = MAP_HEIGHT - 1; y >= 0; y--) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (tiles[x] != null && tiles[x][y] != null) {
                    Tile tile = tiles[x][y];
                    if (tile.getType() == TileType.Tree) {
                        float worldX = x * TILE_SIZE;
                        float worldY = y * TILE_SIZE;
                        renderTreeSprite(x, y, worldX, worldY);
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
                
            // Trees are now handled separately in renderTreeSprite for proper Z-ordering
            case Tree:
                // Do nothing here - trees are rendered in separate pass
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
        
        stage.dispose();
        skin.dispose();
    }
}