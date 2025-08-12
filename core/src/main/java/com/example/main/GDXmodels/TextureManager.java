package com.example.main.GDXmodels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.util.HashMap;
import java.util.Map;


public class TextureManager {

    private final Map<String, Texture> textureMap = new HashMap<>();


    public void loadAllItemTextures() {
        Gdx.app.log("TextureManager", "Starting to load all item textures...");


        String[] assetDirs = {
            "Animal_product", "Animals", "Artisan_good", "Craftable_item",
            "Crafting", "Crops", "Fence", "Fertilizer", "Fish", "Foraging",
            "Gem", "Ingredient", "Mineral", "Recipe", "Resource", "Rock",
            "Tools", "Portraits", "Fishing_Pole", "Skill",
            "Tools/Axe", "Tools/Hoe", "Tools/Pickaxe", "Tools/Trash_can", "Tools/Watering_Can", "Trees", "Tools_animation",
            "Cut", "Cut/map_elements", "Cut/player", "Cut/animals", "Cut/NPC", "weather",
            "emojis"
        };

        for (String dir : assetDirs) {

            String[] roots = new String[] {
                "content/" + dir,
                dir,
                "assets/content/" + dir
            };
            for (String root : roots) {
                try {
                    recursivelyLoadTextures(Gdx.files.internal(root));
                } catch (Exception ignored) {

                }
            }
        }
        Gdx.app.log("TextureManager", "Finished loading " + textureMap.size() + " textures.");
    }




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


    private void loadTexture(FileHandle fileHandle) {
        try {
            Texture texture = new Texture(fileHandle);
            String key = fileHandle.nameWithoutExtension();
            textureMap.put(key, texture);
        } catch (GdxRuntimeException e) {
            Gdx.app.error("TextureManager", "Couldn't load texture: " + fileHandle.path(), e);
        }
    }


    public Texture getTexture(String key) {
        return textureMap.get(key);
    }


    public void dispose() {
        for (Texture texture : textureMap.values()) {
            texture.dispose();
        }
        textureMap.clear();
        Gdx.app.log("TextureManager", "All textures disposed.");
    }
}
