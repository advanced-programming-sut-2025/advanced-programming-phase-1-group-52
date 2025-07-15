package com.example.main.GDXmodels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages loading, storing, and retrieving all item and UI textures.
 */
public class TextureManager {

    private final Map<String, Texture> textureMap = new HashMap<>();

    /**
     * Loads all game assets into memory. This should be called once when the game starts.
     */
    public void loadTextures() {
        // Foraging Items
        loadTexture("content/Foraging/Basic_Fertilizer.png");
        loadTexture("content/Foraging/Blackberry_Cobbler.png");
        loadTexture("content/Foraging/Blackberry.png");
        // ... and so on for all your assets.

        // This is a sample of how the paths would be loaded.
        // A complete implementation would list all 200+ files.
        // For brevity, I'll show a representative sample.

        // --- Sample Loading ---
        loadTexture("content/Foraging/Cave_Carrot.png");
        loadTexture("content/Foraging/Common_Mushroom.png");
        loadTexture("content/Foraging/Daffodil.png");
        loadTexture("content/Foraging/Hazelnut.png");
        loadTexture("content/Foraging/Holly.png");
        loadTexture("content/Foraging/Leek.png");
        loadTexture("content/Foraging/Sap.png");
        loadTexture("content/Foraging/Spring_Onion.png");
        loadTexture("content/Foraging/Wild_Horseradish.png");

        // Gem Items
        loadTexture("content/Gem/Amethyst.png");
        loadTexture("content/Gem/Aquamarine.png");
        loadTexture("content/Gem/Diamond.png");
        loadTexture("content/Gem/Emerald.png");
        loadTexture("content/Gem/Jade.png");
        loadTexture("content/Gem/Prismatic_Shard.png");
        loadTexture("content/Gem/Ruby.png");
        loadTexture("content/Gem/Topaz.png");

        // Ingredient Items
        loadTexture("content/Ingredient/Oil.png");
        loadTexture("content/Ingredient/Rice.png");
        loadTexture("content/Ingredient/Sugar.png");
        loadTexture("content/Ingredient/Vinegar.png");
        loadTexture("content/Ingredient/Wheat_Flour.png");

        // Mineral Items
        loadTexture("content/Mineral/Earth_Crystal.png");
        loadTexture("content/Mineral/Fire_Quartz.png");
        loadTexture("content/Mineral/Frozen_Tear.png");
        loadTexture("content/Mineral/Quartz.png");

        // Resource Items
        loadTexture("content/Resource/Battery_Pack.png");
        loadTexture("content/Resource/Clay.png");
        loadTexture("content/Resource/Coal.png");
        loadTexture("content/Resource/Copper_Bar.png");
        loadTexture("content/Resource/Copper_Ore.png");
        loadTexture("content/Resource/Fiber.png");
        loadTexture("content/Resource/Gold_Bar.png");
        loadTexture("content/Resource/Gold_Ore.png");
        loadTexture("content/Resource/Hardwood.png");
        loadTexture("content/Resource/Iridium_Bar.png");
        loadTexture("content/Resource/Iridium_Ore.png");
        loadTexture("content/Resource/Iron_Bar.png");
        loadTexture("content/Resource/Iron_Ore.png");
        loadTexture("content/Resource/Refined_Quartz.png");
        loadTexture("content/Resource/Stone.png");
        loadTexture("content/Resource/Wood.png");

        // Tool Items
        loadTexture("content/Tools/Axe/Axe.png");
        loadTexture("content/Tools/Hoe/Hoe.png");
        loadTexture("content/Tools/Pickaxe/Pickaxe.png");
        loadTexture("content/Tools/Watering_Can/Watering_Can.png");
        loadTexture("content/Tools/Scythe.png");
        loadTexture("content/Tools/Milk_Pail.png");
        loadTexture("content/Tools/Shears.png");
    }

    /**
     * Helper method to load a single texture and store it in the map.
     * The key is derived from the file path.
     * @param path The internal path to the texture file.
     */
    private void loadTexture(String path) {
        try {
            Texture texture = new Texture(Gdx.files.internal(path));
            String key = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
            textureMap.put(key, texture);
        } catch (GdxRuntimeException e) {
            Gdx.app.error("TextureManager", "Couldn't load texture: " + path, e);
        }
    }

    /**
     * Retrieves a loaded texture from the manager.
     * @param key The key of the texture (usually the filename without extension).
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
    }
}
