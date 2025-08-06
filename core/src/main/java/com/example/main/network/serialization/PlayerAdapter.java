package com.example.main.network.serialization;

import com.example.main.models.Player;
import com.example.main.models.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

public class PlayerAdapter implements JsonSerializer<Player>, JsonDeserializer<Player> {

    @Override
    public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Serialize basic fields normally
        jsonObject.addProperty("username", src.getUsername());
        jsonObject.addProperty("gender", src.getGender().name());
        jsonObject.addProperty("energy", src.getEnergy());
        jsonObject.addProperty("originX", src.originX());
        jsonObject.addProperty("originY", src.originY());
        jsonObject.addProperty("currentX", src.currentX());
        jsonObject.addProperty("currentY", src.currentY());
        jsonObject.addProperty("isFainted", src.isFainted());
        jsonObject.addProperty("trashCanX", src.getTrashCanX());
        jsonObject.addProperty("trashCanY", src.getTrashCanY());
        // Removed numPlayed and highScore serialization as they belong to User

        // Serialize complex objects that are not part of circular reference directly
        // Use context.serialize for these to allow their own adapters to be applied
        jsonObject.add("inventory", context.serialize(src.getInventory()));
        jsonObject.add("bankAccount", context.serialize(src.getBankAccount()));
        jsonObject.add("houseRefrigerator", context.serialize(src.getHouseRefrigerator()));
        // Assuming lists of simple objects or objects already handled by other adapters
        jsonObject.add("trades", context.serialize(src.getTrades()));
        jsonObject.add("talks", context.serialize(src.getTalks()));
        jsonObject.add("craftingRecipes", context.serialize(src.getCraftingRecipe()));
        jsonObject.add("cookingRecipes", context.serialize(src.getCookingRecipe()));
        jsonObject.add("activeBuffs", context.serialize(src.getActiveBuffs()));
        jsonObject.add("skills", context.serialize(src.getSkills()));
        jsonObject.add("gifts", context.serialize(src.getGifts()));
        jsonObject.add("notifications", context.serialize(src.getNotifications()));
        jsonObject.add("housings", context.serialize(src.getHousings()));

        // Handle spouse to break circular dependency
        if (src.getSpouse() != null) {
            // Only serialize the username of the spouse to break the cycle
            jsonObject.addProperty("spouseUsername", src.getSpouse().getUsername());
        }

        // Handle currentTool to break circular dependency, if Tool itself has a Player reference
        if (src.getCurrentTool() != null) {
            // Serialize the tool, but ensure ToolAdapter (if exists) handles its Player reference carefully
            jsonObject.add("currentTool", context.serialize(src.getCurrentTool()));
        }

        return jsonObject;
    }

    @Override
    public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Deserialize basic fields
        String username = jsonObject.get("username").getAsString();
        String genderStr = jsonObject.get("gender").getAsString();
        Player player = new Player(username, com.example.main.enums.player.Gender.valueOf(genderStr));

        player.setEnergy(jsonObject.get("energy").getAsInt());
        player.setOriginX(jsonObject.get("originX").getAsInt());
        player.setOriginY(jsonObject.get("originY").getAsInt());
        player.setCurrentX(jsonObject.get("currentX").getAsInt());
        player.setCurrentY(jsonObject.get("currentY").getAsInt());
        player.setFainted(jsonObject.get("isFainted").getAsBoolean());
        player.setTrashCanX(jsonObject.get("trashCanX").getAsInt());
        player.setTrashCanY(jsonObject.get("trashCanY").getAsInt());
        // Removed numPlayed and highScore deserialization


        // Deserialize complex objects. Use explicit type tokens for collections if needed.
        // For inventory, add items to existing instance since it's final
        Type inventoryType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.item.Item>>() {}.getType();
        java.util.ArrayList<com.example.main.models.item.Item> deserializedItems = context.deserialize(jsonObject.get("inventory"), inventoryType);
        player.getInventory().getItems().clear(); // Clear existing items if any
        player.getInventory().getItems().addAll(deserializedItems);

        player.setBankAccount(context.deserialize(jsonObject.get("bankAccount"), com.example.main.models.BankAccount.class));
        player.setHouseRefrigerator(context.deserialize(jsonObject.get("houseRefrigerator"), com.example.main.models.HouseRefrigerator.class));

        // For collections, use TypeToken to retain generic type information
        Type tradeListType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.Trade>>() {}.getType();
        player.getTrades().addAll(context.deserialize(jsonObject.get("trades"), tradeListType));

        Type talkListType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.Talk>>() {}.getType();
        player.getTalks().addAll(context.deserialize(jsonObject.get("talks"), talkListType));

        Type craftingRecipeListType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.item.CraftingRecipe>>() {}.getType();
        player.getCraftingRecipe().addAll(context.deserialize(jsonObject.get("craftingRecipes"), craftingRecipeListType));

        Type cookingRecipeListType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.item.CookingRecipe>>() {}.getType();
        player.getCookingRecipe().addAll(context.deserialize(jsonObject.get("cookingRecipes"), cookingRecipeListType));

        Type activeBuffListType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.ActiveBuff>>() {}.getType();
        player.getActiveBuffs().addAll(context.deserialize(jsonObject.get("activeBuffs"), activeBuffListType));

        Type skillMapType = new com.google.gson.reflect.TypeToken<java.util.HashMap<com.example.main.enums.player.Skills, com.example.main.models.SkillData>>() {}.getType();
        player.getSkills().putAll(context.deserialize(jsonObject.get("skills"), skillMapType));

        Type giftMapType = new com.google.gson.reflect.TypeToken<java.util.HashMap<java.lang.Integer, com.example.main.models.Gift>>() {}.getType();
        player.getGifts().putAll(context.deserialize(jsonObject.get("gifts"), giftMapType));

        Type notificationListType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.Notification>>() {}.getType();
        player.getNotifications().addAll(context.deserialize(jsonObject.get("notifications"), notificationListType));

        Type housingListType = new com.google.gson.reflect.TypeToken<java.util.ArrayList<com.example.main.models.building.Housing>>() {}.getType();
        player.getHousings().addAll(context.deserialize(jsonObject.get("housings"), housingListType));

        // Handle spouse: store username for later linking
        if (jsonObject.has("spouseUsername")) {
            String spouseUsername = jsonObject.get("spouseUsername").getAsString();
            // In real scenario, you'd link this player later based on username
            // For now, setting spouse to null to break immediate cycle.
            // You might add a post-processing step in Game or GameServer to link spouses.
            // player.setSpouse(findPlayerByUsername(spouseUsername)); // This would require game context
        }

        // Deserialize currentTool
        if (jsonObject.has("currentTool")) {
            player.setCurrentTool(context.deserialize(jsonObject.get("currentTool"), com.example.main.models.item.Tool.class));
        }

        return player;
    }
}