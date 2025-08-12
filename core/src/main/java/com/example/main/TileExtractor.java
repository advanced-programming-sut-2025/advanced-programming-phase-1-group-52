package com.example.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;


public class TileExtractor {
    private ObjectMap<String, Texture> loadedTextures;
    private ObjectMap<String, Array<TextureRegion>> extractedTiles;

    public TileExtractor() {
        loadedTextures = new ObjectMap<>();
        extractedTiles = new ObjectMap<>();
    }


    public void loadTexture(String texturePath, String textureKey) {
        Texture texture = new Texture(texturePath);
        loadedTextures.put(textureKey, texture);
    }


    public TextureRegion extractTile(String textureKey, int x, int y, int width, int height) {
        Texture texture = loadedTextures.get(textureKey);
        if (texture == null) {
            throw new IllegalArgumentException("Texture not loaded: " + textureKey);
        }

        return new TextureRegion(texture, x, y, width, height);
    }


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


    public TextureRegion getTile(String tilesKey, int index) {
        Array<TextureRegion> tiles = extractedTiles.get(tilesKey);
        if (tiles == null || index >= tiles.size) {
            throw new IllegalArgumentException("Invalid tiles key or index: " + tilesKey + ", " + index);
        }
        return tiles.get(index);
    }


    public Array<TextureRegion> getAllTiles(String tilesKey) {
        return extractedTiles.get(tilesKey);
    }


    public void dispose() {
        for (Texture texture : loadedTextures.values()) {
            texture.dispose();
        }
        loadedTextures.clear();
        extractedTiles.clear();
    }
}
