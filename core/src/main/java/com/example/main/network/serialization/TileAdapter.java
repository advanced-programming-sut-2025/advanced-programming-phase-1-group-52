package com.example.main.network.serialization;

import com.example.main.models.Player;
import com.example.main.models.Tile;
import com.example.main.enums.design.TileType;
import com.example.main.models.building.Shop;
import com.example.main.models.item.Item;
import com.example.main.models.item.PlacedMachine;
import com.example.main.models.item.Seed;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TileAdapter implements JsonSerializer<Tile>, JsonDeserializer<Tile> {

    @Override
    public JsonElement serialize(Tile src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", src.getX());
        jsonObject.addProperty("y", src.getY());
        jsonObject.addProperty("type", src.getType().name());

        if (src.getOwner() != null) {
            jsonObject.addProperty("ownerUsername", src.getOwner().getUsername());
        }

        // Serialize other complex fields, relying on their own adapters
        if (src.getPlant() != null) {
            jsonObject.add("plant", context.serialize(src.getPlant()));
        }
        if (src.getTree() != null) {
            jsonObject.add("tree", context.serialize(src.getTree()));
        }
        if (src.getSeed() != null) {
            jsonObject.add("seed", context.serialize(src.getSeed()));
        }
        if (src.getItem() != null) {
            jsonObject.add("item", context.serialize(src.getItem()));
        }
        if (src.getShop() != null) {
            jsonObject.add("shop", context.serialize(src.getShop()));
        }
        if (src.getPlacedMachine() != null) {
            jsonObject.add("placedMachine", context.serialize(src.getPlacedMachine()));
        }

        jsonObject.addProperty("isPartOfGiantCrop", src.isPartOfGiantCrop());
        jsonObject.addProperty("giantCropRootX", src.getGiantCropRootX());
        jsonObject.addProperty("giantCropRootY", src.getGiantCropRootY());

        return jsonObject;
    }

    @Override
    public Tile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int x = jsonObject.get("x").getAsInt();
        int y = jsonObject.get("y").getAsInt();
        TileType type = TileType.valueOf(jsonObject.get("type").getAsString());

        // Create a dummy Player for the owner, actual linking happens later.
        Player owner = null;
        if (jsonObject.has("ownerUsername")) {
            String ownerUsername = jsonObject.get("ownerUsername").getAsString();
            owner = new Player(ownerUsername, null); // Gender can be null or a default for now
        }

        Tile tile = new Tile(x, y, type, owner);

        if (jsonObject.has("plant")) {
            tile.setPlant(context.deserialize(jsonObject.get("plant"), com.example.main.enums.items.Growable.class));
        }
        if (jsonObject.has("tree")) {
            tile.setTree(context.deserialize(jsonObject.get("tree"), com.example.main.models.Tree.class));
        }
        if (jsonObject.has("seed")) {
            tile.setSeed(context.deserialize(jsonObject.get("seed"), com.example.main.models.item.Seed.class));
        }
        if (jsonObject.has("item")) {
            tile.setItem(context.deserialize(jsonObject.get("item"), com.example.main.models.item.Item.class));
        }
        if (jsonObject.has("shop")) {
            tile.setShop(context.deserialize(jsonObject.get("shop"), com.example.main.models.building.Shop.class));
        }
        if (jsonObject.has("placedMachine")) {
            tile.setPlacedMachine(context.deserialize(jsonObject.get("placedMachine"), com.example.main.models.item.PlacedMachine.class));
        }

        tile.setPartOfGiantCrop(jsonObject.get("isPartOfGiantCrop").getAsBoolean());
        tile.setGiantCropRootX(jsonObject.get("giantCropRootX").getAsInt());
        tile.setGiantCropRootY(jsonObject.get("giantCropRootY").getAsInt());

        return tile;
    }
}