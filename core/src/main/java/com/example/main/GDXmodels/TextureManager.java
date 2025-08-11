package com.example.main.GDXmodels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages loading, storing, and retrieving all item and UI textures.
 * This class recursively scans asset directories to load all images.
 */
public class TextureManager {

    private final Map<String, Texture> textureMap = new HashMap<>();

    /**
     * Loads all game assets from the content directory into memory.
     * This should be called once when the game starts.
     */
    public void loadAllItemTextures() {
        Gdx.app.log("TextureManager", "Starting to load all item textures...");

        // ### FIX: Added "emojis" to the main asset list ###
        String[] assetDirs = {
            "Animal_product", "Animals", "Artisan_good", "Craftable_item",
            "Crafting", "Crops", "Fence", "Fertilizer", "Fish", "Foraging",
            "Gem", "Ingredient", "Mineral", "Recipe", "Resource", "Rock",
            "Tools", "Portraits", "Fishing_Pole", "Skill",
            "Tools/Axe", "Tools/Hoe", "Tools/Pickaxe", "Tools/Trash_can", "Tools/Watering_Can", "Trees", "Tools_animation",
            "Cut", "Cut/map_elements", "Cut/player", "Cut/animals", "Cut/NPC", "weather",
            "emojis" // Emojis are now treated like any other asset.
        };

        for (String dir : assetDirs) {
            // This robust loader tries multiple possible paths, which will now find the emojis.
            String[] roots = new String[] {
                "content/" + dir,            // Standard LibGDX assets root
                dir,                         // In case assets were flattened
                "assets/content/" + dir      // Fallback path
            };
            for (String root : roots) {
                try {
                    recursivelyLoadTextures(Gdx.files.internal(root));
                } catch (Exception ignored) {
                    // Ignore; we'll try the next root if one fails
                }
            }
        }
        Gdx.app.log("TextureManager", "Finished loading " + textureMap.size() + " textures.");
    }

    // ### FIX: The separate, less reliable emoji loader has been removed. ###

    /**
     * Recursively scans a directory and loads any .png files found.
     * @param directory The directory to scan.
     */
    private void recursivelyLoadTextures(FileHandle directory) {
        if (directory.isDirectory()) {
            for (FileHandle file : directory.list()) {
                if (file.isDirectory()) {
                    recursivelyLoadTextures(file);
                } else {
                    if (file.extension().equalsIgnoreCase("png")) {
                        loadTexture(file);
                    }
                }
            }
        }
    }

    /**
     * Helper method to load a single texture and store it in the map.
     * The key is derived from the filename (e.g., "Basic_Fertilizer.png" -> "Basic_Fertilizer").
     * @param fileHandle The file handle of the texture to load.
     */
    private void loadTexture(FileHandle fileHandle) {
        try {
            Texture texture = new Texture(fileHandle);
            String key = fileHandle.nameWithoutExtension();
            textureMap.put(key, texture);
        } catch (GdxRuntimeException e) {
            Gdx.app.error("TextureManager", "Couldn't load texture: " + fileHandle.path(), e);
        }
    }

    /**
     * Retrieves a loaded texture from the manager.
     * @param key The key of the texture (the filename without extension).
     * @return The Texture object, or null if not found.
     */
    public Texture getTexture(String key) {
        return textureMap.get(key);
    }

    /**
     * Disposes all loaded textures to free up memory.
     */
    public void dispose() {
        for (Texture texture : textureMap.values()) {
            texture.dispose();
        }
        textureMap.clear();
        Gdx.app.log("TextureManager", "All textures disposed.");
    }
}
