package com.example.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Utility class for extracting tiles from sprite sheets/tile sheets
 * Useful for extracting specific elements from PNG files like the ones in assets/content/Maps
 */
public class TileExtractor {
    private ObjectMap<String, Texture> loadedTextures;
    private ObjectMap<String, Array<TextureRegion>> extractedTiles;
    
    public TileExtractor() {
        loadedTextures = new ObjectMap<>();
        extractedTiles = new ObjectMap<>();
    }
    
    /**
     * Load a texture from the assets
     * @param texturePath Path to the texture file (e.g., "content/Maps/winter_outdoorsTileSheet.png")
     * @param textureKey Unique key to reference this texture
     */
    public void loadTexture(String texturePath, String textureKey) {
        Texture texture = new Texture(texturePath);
        loadedTextures.put(textureKey, texture);
    }
    
    /**
     * Extract a specific tile from a loaded texture
     * @param textureKey The key of the loaded texture
     * @param x X position of the tile in pixels
     * @param y Y position of the tile in pixels  
     * @param width Width of the tile in pixels
     * @param height Height of the tile in pixels
     * @return TextureRegion containing the extracted tile
     */
    public TextureRegion extractTile(String textureKey, int x, int y, int width, int height) {
        Texture texture = loadedTextures.get(textureKey);
        if (texture == null) {
            throw new IllegalArgumentException("Texture not loaded: " + textureKey);
        }
        
        return new TextureRegion(texture, x, y, width, height);
    }
    
    /**
     * Extract multiple tiles in a grid pattern (useful for tile sheets)
     * @param textureKey The key of the loaded texture
     * @param startX Starting X position
     * @param startY Starting Y position
     * @param tileWidth Width of each tile
     * @param tileHeight Height of each tile
     * @param columns Number of columns to extract
     * @param rows Number of rows to extract
     * @param tilesKey Key to store the extracted tiles array
     */
    public void extractTileGrid(String textureKey, int startX, int startY, 
                               int tileWidth, int tileHeight, 
                               int columns, int rows, String tilesKey) {
        Texture texture = loadedTextures.get(textureKey);
        if (texture == null) {
            throw new IllegalArgumentException("Texture not loaded: " + textureKey);
        }
        
        Array<TextureRegion> tiles = new Array<>();
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int x = startX + col * tileWidth;
                int y = startY + row * tileHeight;
                tiles.add(new TextureRegion(texture, x, y, tileWidth, tileHeight));
            }
        }
        
        extractedTiles.put(tilesKey, tiles);
    }
    
    /**
     * Get a specific tile from an extracted grid
     * @param tilesKey Key of the extracted tiles array
     * @param index Index of the tile (0-based)
     * @return TextureRegion of the requested tile
     */
    public TextureRegion getTile(String tilesKey, int index) {
        Array<TextureRegion> tiles = extractedTiles.get(tilesKey);
        if (tiles == null || index >= tiles.size) {
            throw new IllegalArgumentException("Invalid tiles key or index: " + tilesKey + ", " + index);
        }
        return tiles.get(index);
    }
    
    /**
     * Get all tiles from an extracted grid
     * @param tilesKey Key of the extracted tiles array
     * @return Array of all extracted tiles
     */
    public Array<TextureRegion> getAllTiles(String tilesKey) {
        return extractedTiles.get(tilesKey);
    }
    
    /**
     * Clean up resources
     */
    public void dispose() {
        for (Texture texture : loadedTextures.values()) {
            texture.dispose();
        }
        loadedTextures.clear();
        extractedTiles.clear();
    }
} 