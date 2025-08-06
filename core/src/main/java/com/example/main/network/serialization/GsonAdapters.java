package com.example.main.network.serialization;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.example.main.models.item.*;
import com.example.main.enums.items.*;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.FruitType;
import com.example.main.enums.items.ArtisanProductType; // Changed from GoodType
import com.example.main.enums.items.ToolType;
import com.example.main.enums.items.FoodType;
import com.example.main.enums.items.MineralType;
import com.example.main.enums.items.Growable;
import com.example.main.models.Trade;
import com.example.main.models.TradeRequest;
import com.example.main.models.Buy;
import com.example.main.models.building.Building;
import com.example.main.models.building.Shop;
import com.example.main.models.building.GreenHouse;
import com.example.main.models.building.House;
import com.example.main.models.building.NPCHouse;
import com.example.main.models.User;
import com.example.main.network.serialization.UserAdapter;
import com.example.main.models.Player;
import com.example.main.network.serialization.PlayerAdapter;
import com.example.main.models.Friendship;
import com.example.main.network.serialization.FriendshipAdapter;
import com.example.main.models.Tile;
import com.example.main.network.serialization.TileAdapter;
import com.example.main.models.NPCFriendship;
import com.example.main.network.serialization.NPCFriendshipAdapter;
import com.example.main.network.serialization.GrowableAdapter;
import com.example.main.models.Talk;
import com.example.main.network.serialization.TalkAdapter;
import com.example.main.models.Tree; // Added for TreeAdapter

import java.lang.reflect.Type;

/**
 * A utility class to provide pre-configured Gson instances
 * with custom TypeAdapters for polymorphic types.
 */
public class GsonAdapters {

    private static Gson gsonInstance;

    public static Gson getGsonInstance() {
        if (gsonInstance == null) {
            gsonInstance = new GsonBuilder()
                    .registerTypeAdapter(Item.class, new ItemAdapter())
                    .registerTypeAdapter(Growable.class, new GrowableAdapter())
                    .registerTypeAdapter(Trade.class, new TradeAdapter())
                    .registerTypeAdapter(Building.class, new BuildingAdapter())
                    .registerTypeAdapter(User.class, new UserAdapter())
                    .registerTypeAdapter(Player.class, new PlayerAdapter())
                    .registerTypeAdapter(Friendship.class, new FriendshipAdapter())
                    .registerTypeAdapter(Tile.class, new TileAdapter())
                    .registerTypeAdapter(NPCFriendship.class, new NPCFriendshipAdapter())
                    .registerTypeAdapter(Growable.class, new GrowableAdapter())
                    .registerTypeAdapter(Talk.class, new TalkAdapter())
                    // Register other adapters here as needed
                    .create();
        }
        return gsonInstance;
    }

    // Custom TypeAdapter for Item (and its subclasses)
    private static class ItemAdapter implements JsonSerializer<Item>, JsonDeserializer<Item> {
        @Override
        public JsonElement serialize(Item src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            if (src.getItemType() != null) {
                result.addProperty("itemTypeClassName", src.getItemType().getClass().getSimpleName()); // e.g., "CropType"
                result.addProperty("itemEnumValue", src.getItemType().getEnumName()); // e.g., "CARROT"
            } else {
                result.addProperty("itemTypeClassName", (String) null);
                result.addProperty("itemEnumValue", (String) null);
            }
            result.addProperty("number", src.getNumber()); // Serialize the number field
            // Manually serialize properties of the concrete Item subclass
            JsonObject properties = new JsonObject();
            if (src instanceof Crop) {
                Crop crop = (Crop) src;
                properties.addProperty("dayPassed", crop.getDayPassed());
                properties.addProperty("dayRemaining", crop.getDayRemaining());
                properties.addProperty("isWateredToday", crop.isWateredToday());
                properties.addProperty("isFertilizedToday", crop.isFertilizedToday());
                properties.addProperty("needsWaterToday", crop.isNeedsWaterToday());
                properties.addProperty("currentStage", crop.getCurrentStage());
                properties.addProperty("isReadyToHarvest", crop.isReadyToHarvest());
                properties.addProperty("isNotWateredForTwoDays", crop.isNotWateredForTwoDays());
                properties.addProperty("canBeHarvestAgain", crop.canBeHarvestAgain());
            } else if (src instanceof Fruit) {
                Fruit fruit = (Fruit) src;
                properties.addProperty("currentStage", fruit.getCurrentStage());
                properties.addProperty("dayPassed", fruit.getDayPassed());
                properties.addProperty("dayRemaining", fruit.getDayRemaining());
                properties.addProperty("isWateredToday", fruit.isWateredToday());
                properties.addProperty("isFertilizedToday", fruit.isFertilizedToday());
                properties.addProperty("needsWaterToday", fruit.isNeedsWaterToday());
                properties.addProperty("isReadyToHarvest", fruit.isReadyToHarvest());
                properties.addProperty("hasBeenHarvestedToday", fruit.hasBeenHarvestedToday());
                properties.addProperty("unwateredDays", fruit.getUnwateredDays());
                if (fruit.getTreeType() != null) {
                    properties.addProperty("treeType", fruit.getTreeType().name());
                }
            } else if (src instanceof Tool) {
                // Tool currently has no specific properties beyond Item
            } else if (src instanceof Good) {
                // Good currently has no specific properties beyond Item
            } else if (src instanceof Food) {
                // Food currently has no specific properties beyond Item
            } else if (src instanceof Mineral) {
                // Mineral currently has no specific properties beyond Item, but add an empty properties object
            }
            // Add other item types here if they have specific properties
            result.add("properties", properties);
            return result;
        }

        @Override
        public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String itemTypeClassName = null;
            if (jsonObject.has("itemTypeClassName") && !jsonObject.get("itemTypeClassName").isJsonNull()) {
                itemTypeClassName = jsonObject.get("itemTypeClassName").getAsString();
            } else {
                System.err.println("Warning: Missing or null 'itemTypeClassName' in Item JSON: " + json.toString());
            }

            String itemEnumValue = null;
            if (jsonObject.has("itemEnumValue") && !jsonObject.get("itemEnumValue").isJsonNull()) {
                itemEnumValue = jsonObject.get("itemEnumValue").getAsString();
            } else {
                System.err.println("Warning: Missing or null 'itemEnumValue' in Item JSON: " + json.toString());
            }

            int number = 0; // Default to 0 if missing
            if (jsonObject.has("number") && !jsonObject.get("number").isJsonNull()) {
                number = jsonObject.get("number").getAsInt();
            } else {
                System.err.println("Warning: Missing or null 'number' in Item JSON, defaulting to 0: " + json.toString());
            }

            JsonElement properties = jsonObject.get("properties");
            if (properties == null || properties.isJsonNull()) {
                System.err.println("Warning: Missing or null 'properties' object in Item JSON: " + json.toString());
                // If properties are missing, we can't deserialize the specific item details.
                // For now, return a generic Item or null, depending on your application logic.
                // For robustness, let's return a basic Item with the known ItemType if possible.
                if (itemTypeClassName != null && itemEnumValue != null) {
                    try {
                        // Attempt to create a basic item if type info is present
                        ItemType type = null;
                        switch (itemTypeClassName) {
                            case "CropType": type = CropType.valueOf(itemEnumValue); break;
                            case "FruitType": type = FruitType.valueOf(itemEnumValue); break;
                            case "GoodType": type = ArtisanProductType.valueOf(itemEnumValue); break;
                            case "ToolType": type = ToolType.valueOf(itemEnumValue); break;
                            case "FoodType": type = FoodType.valueOf(itemEnumValue); break;
                            case "MineralType": type = MineralType.valueOf(itemEnumValue); break;
                        }
                        if (type != null) {
                            return new Item(type, number) {
                                @Override
                                protected int calculateEnergyConsumption() {
                                    return 0;
                                }
                            };
                        }
                    } catch (IllegalArgumentException e) {
                        // Fall through to throw JsonParseException if type enum is invalid
                    }
                }
                throw new JsonParseException("Cannot deserialize Item due to missing 'properties' and/or invalid type info: " + json.toString());
            }

            ItemType concreteItemType = null;
            Class<? extends Item> itemClass = null; // Move declaration here to ensure scope

            if (itemTypeClassName != null && itemEnumValue != null) {
                switch (itemTypeClassName) {
                    case "CropType":
                        concreteItemType = CropType.valueOf(itemEnumValue);
                        itemClass = Crop.class;
                        break;
                    case "FruitType":
                        concreteItemType = FruitType.valueOf(itemEnumValue);
                        itemClass = Fruit.class;
                        break;
                    case "GoodType":
                        concreteItemType = ArtisanProductType.valueOf(itemEnumValue);
                        itemClass = Good.class;
                        break;
                    case "ToolType":
                        concreteItemType = ToolType.valueOf(itemEnumValue);
                        itemClass = Tool.class;
                        break;
                    case "FoodType":
                        concreteItemType = FoodType.valueOf(itemEnumValue);
                        itemClass = Food.class;
                        break;
                    case "MineralType":
                        concreteItemType = MineralType.valueOf(itemEnumValue);
                        itemClass = Mineral.class;
                        break;
                    default: throw new JsonParseException("Unknown ItemType class: " + itemTypeClassName);
                }
            } else {
                throw new JsonParseException("Cannot determine concrete ItemType due to missing info: " + json.toString());
            }

            // Manually deserialize properties based on the concrete item class
            Item item = null;
            if (itemClass == Crop.class) {
                Crop crop = new Crop((CropType) concreteItemType, 0); // Use 0 as a placeholder for 'number'
                // Manually deserialize other properties for Crop
                JsonObject cropProperties = properties.getAsJsonObject();
                if (cropProperties.has("dayPassed")) crop.setDayPassed(cropProperties.get("dayPassed").getAsInt());
                if (cropProperties.has("dayRemaining")) crop.setDayRemaining(cropProperties.get("dayRemaining").getAsInt());
                if (cropProperties.has("isWateredToday")) crop.setWateredToday(cropProperties.get("isWateredToday").getAsBoolean());
                if (cropProperties.has("isFertilizedToday")) crop.setFertilizedToday(cropProperties.get("isFertilizedToday").getAsBoolean());
                if (cropProperties.has("needsWaterToday")) crop.setNeedsWaterToday(cropProperties.get("needsWaterToday").getAsBoolean());
                if (cropProperties.has("currentStage")) crop.setCurrentStage(cropProperties.get("currentStage").getAsInt());
                if (cropProperties.has("isReadyToHarvest")) crop.setReadyToHarvest(cropProperties.get("isReadyToHarvest").getAsBoolean());
                if (cropProperties.has("isNotWateredForTwoDays")) crop.setNotWateredForTwoDays(cropProperties.get("isNotWateredForTwoDays").getAsBoolean());
                if (cropProperties.has("canBeHarvestAgain")) crop.setCanBeHarvestAgain(cropProperties.get("canBeHarvestAgain").getAsBoolean());

                crop.setNumber(number); // Set the number field
                item = crop;
            } else if (itemClass == Fruit.class) {
                Fruit fruit = new Fruit((FruitType) concreteItemType, 0); // Use 0 as a placeholder for 'number'
                // Manually deserialize other properties for Fruit
                JsonObject fruitProperties = properties.getAsJsonObject();
                if (fruitProperties.has("currentStage")) fruit.setCurrentStage(fruitProperties.get("currentStage").getAsInt());
                if (fruitProperties.has("dayPassed")) fruit.setDayPassed(fruitProperties.get("dayPassed").getAsInt());
                if (fruitProperties.has("dayRemaining")) fruit.setDayRemaining(fruitProperties.get("dayRemaining").getAsInt());
                if (fruitProperties.has("isWateredToday")) fruit.setWateredToday(fruitProperties.get("isWateredToday").getAsBoolean());
                if (fruitProperties.has("isFertilizedToday")) fruit.setFertilizedToday(fruitProperties.get("isFertilizedToday").getAsBoolean());
                if (fruitProperties.has("needsWaterToday")) fruit.setNeedsWaterToday(fruitProperties.get("needsWaterToday").getAsBoolean());
                if (fruitProperties.has("isReadyToHarvest")) fruit.setReadyToHarvest(fruitProperties.get("isReadyToHarvest").getAsBoolean());
                if (fruitProperties.has("hasBeenHarvestedToday")) fruit.setHasBeenHarvestedToday(fruitProperties.get("hasBeenHarvestedToday").getAsBoolean());
                if (fruitProperties.has("unwateredDays")) fruit.setUnwateredDays(fruitProperties.get("unwateredDays").getAsInt());

                // Set TreeType for Fruit, which is derived
                if (fruitProperties.has("treeType")) {
                    try {
                        fruit.setTreeType(TreeType.valueOf(fruitProperties.get("treeType").getAsString()));
                    } catch (IllegalArgumentException e) {
                        // Handle case where TreeType enum value might not be found
                        System.err.println("Warning: Invalid TreeType value in JSON: " + fruitProperties.get("treeType").getAsString());
                    }
                }
                fruit.setNumber(number); // Set the number field
                item = fruit;
            } else if (itemClass == Tool.class) {
                Tool tool = new Tool((ToolType) concreteItemType, number);
                item = tool;
            } else if (itemClass == Good.class) {
                Good good = new Good((ArtisanProductType) concreteItemType, number);
                item = good;
            } else if (itemClass == Food.class) {
                Food food = new Food((FoodType) concreteItemType, number);
                item = food;
            } else if (itemClass == Mineral.class) {
                Mineral mineral = new Mineral((MineralType) concreteItemType, number);
                item = mineral;
            }
            return item;
        }
    }

    // Custom TypeAdapter for Growable (and its subclasses)
    private static class GrowableAdapter implements JsonSerializer<Growable>, JsonDeserializer<Growable> {
        @Override
        public JsonElement serialize(Growable src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            String typeName = "";
            JsonObject properties = new JsonObject();
            if (src instanceof Crop) {
                typeName = "Crop";
                Crop crop = (Crop) src;
                properties.addProperty("cropType", crop.getCropType().getName());
                properties.addProperty("dayPassed", crop.getDayPassed());
                properties.addProperty("dayRemaining", crop.getDayRemaining());
                properties.addProperty("isWateredToday", crop.isWateredToday());
                properties.addProperty("isFertilizedToday", crop.isFertilizedToday());
                properties.addProperty("needsWaterToday", crop.isNeedsWaterToday());
                properties.addProperty("currentStage", crop.getCurrentStage());
                properties.addProperty("isReadyToHarvest", crop.isReadyToHarvest());
                properties.addProperty("isNotWateredForTwoDays", crop.isNotWateredForTwoDays());
                properties.addProperty("canBeHarvestAgain", crop.canBeHarvestAgain());
            } else if (src instanceof Fruit) {
                typeName = "Fruit";
                Fruit fruit = (Fruit) src;
                properties.addProperty("fruitType", fruit.getFruitType().name());
                properties.addProperty("currentStage", fruit.getCurrentStage());
                properties.addProperty("dayPassed", fruit.getDayPassed());
                properties.addProperty("dayRemaining", fruit.getDayRemaining());
                properties.addProperty("isWateredToday", fruit.isWateredToday());
                properties.addProperty("isFertilizedToday", fruit.isFertilizedToday());
                properties.addProperty("needsWaterToday", fruit.isNeedsWaterToday());
                properties.addProperty("isReadyToHarvest", fruit.isReadyToHarvest());
                properties.addProperty("hasBeenHarvestedToday", fruit.hasBeenHarvestedToday());
                properties.addProperty("unwateredDays", fruit.getUnwateredDays());
                if (fruit.getTreeType() != null) {
                    properties.addProperty("treeType", fruit.getTreeType().name());
                }
            } else if (src instanceof Tree) {
                typeName = "Tree";
                Tree tree = (Tree) src;
                properties.addProperty("treeType", tree.getType().name());
                properties.addProperty("dayPassed", tree.getDayPassed());
                properties.addProperty("dayRemaining", tree.getDayRemaining());
                properties.addProperty("wateredToday", tree.isWateredToday());
                properties.addProperty("fertilizedToday", tree.isFertilizedToday());
                properties.addProperty("needsWaterToday", tree.isNeedsWaterToday());
                properties.addProperty("currentStage", tree.getCurrentStage());
                properties.addProperty("readyToHarvest", tree.isReadyToHarvest());
                properties.addProperty("notWateredForTwoDays", tree.isNotWateredForTwoDays());
            }
            result.addProperty("type", typeName);
            result.add("properties", properties);
            return result;
        }

        @Override
        public Growable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String type;
            if (jsonObject.has("type") && !jsonObject.get("type").isJsonNull()) {
                type = jsonObject.get("type").getAsString();
            } else {
                throw new JsonParseException("Missing 'type' field in Growable JSON: " + json.toString());
            }

            JsonObject properties = jsonObject.getAsJsonObject("properties"); // Get as JsonObject directly
            if (properties == null || properties.isJsonNull()) {
                throw new JsonParseException("Missing 'properties' object in Growable JSON: " + json.toString());
            }

            Growable growable;
            switch (type) {
                case "Crop":
                    CropType cropType = null;
                    if (properties.has("cropType") && !properties.get("cropType").isJsonNull()) {
                        cropType = CropType.valueOf(properties.get("cropType").getAsString());
                    }
                    Crop crop = new Crop(cropType, 1); // Number can be default for now, adjusted later if needed
                    if (properties.has("dayPassed")) crop.setDayPassed(properties.get("dayPassed").getAsInt());
                    if (properties.has("dayRemaining")) crop.setDayRemaining(properties.get("dayRemaining").getAsInt());
                    if (properties.has("isWateredToday")) crop.setWateredToday(properties.get("isWateredToday").getAsBoolean());
                    if (properties.has("isFertilizedToday")) crop.setFertilizedToday(properties.get("isFertilizedToday").getAsBoolean());
                    if (properties.has("needsWaterToday")) crop.setNeedsWaterToday(properties.get("needsWaterToday").getAsBoolean());
                    if (properties.has("currentStage")) crop.setCurrentStage(properties.get("currentStage").getAsInt());
                    if (properties.has("isReadyToHarvest")) crop.setReadyToHarvest(properties.get("isReadyToHarvest").getAsBoolean());
                    if (properties.has("isNotWateredForTwoDays")) crop.setNotWateredForTwoDays(properties.get("isNotWateredForTwoDays").getAsBoolean());
                    if (properties.has("canBeHarvestAgain")) crop.setCanBeHarvestAgain(properties.get("canBeHarvestAgain").getAsBoolean());
                    growable = crop;
                    break;
                case "Fruit":
                    FruitType fruitType = null;
                    if (properties.has("fruitType") && !properties.get("fruitType").isJsonNull()) {
                        fruitType = FruitType.valueOf(properties.get("fruitType").getAsString());
                    }
                    TreeType treeType = null;
                    if (properties.has("treeType") && !properties.get("treeType").isJsonNull()) {
                        try {
                            treeType = TreeType.valueOf(properties.get("treeType").getAsString());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Warning: Invalid TreeType value in Growable JSON: " + properties.get("treeType").getAsString());
                        }
                    }
                    Fruit fruit = new Fruit(fruitType, 1); // Number can be default for now
                    fruit.setTreeType(treeType);
                    if (properties.has("currentStage")) fruit.setCurrentStage(properties.get("currentStage").getAsInt());
                    if (properties.has("dayPassed")) fruit.setDayPassed(properties.get("dayPassed").getAsInt());
                    if (properties.has("dayRemaining")) fruit.setDayRemaining(properties.get("dayRemaining").getAsInt());
                    if (properties.has("isWateredToday")) fruit.setWateredToday(properties.get("isWateredToday").getAsBoolean());
                    if (properties.has("isFertilizedToday")) fruit.setFertilizedToday(properties.get("isFertilizedToday").getAsBoolean());
                    if (properties.has("needsWaterToday")) fruit.setNeedsWaterToday(properties.get("needsWaterToday").getAsBoolean());
                    if (properties.has("isReadyToHarvest")) fruit.setReadyToHarvest(properties.get("isReadyToHarvest").getAsBoolean());
                    if (properties.has("hasBeenHarvestedToday")) fruit.setHasBeenHarvestedToday(properties.get("hasBeenHarvestedToday").getAsBoolean());
                    if (properties.has("unwateredDays")) fruit.setUnwateredDays(properties.get("unwateredDays").getAsInt());
                    growable = fruit;
                    break;
                case "Tree":
                    TreeType treeTypeForTree = null; // Renamed to avoid shadowing
                    if (properties.has("treeType") && !properties.get("treeType").isJsonNull()) {
                        try {
                            treeTypeForTree = TreeType.valueOf(properties.get("treeType").getAsString());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Warning: Invalid TreeType value in Growable JSON: " + properties.get("treeType").getAsString());
                        }
                    }
                    Tree tree = new Tree(treeTypeForTree); // Instantiate Tree object

                    // Deserialize common Growable properties into the Tree object
                    if (properties.has("dayPassed")) tree.setDayPassed(properties.get("dayPassed").getAsInt());
                    if (properties.has("dayRemaining")) tree.setDayRemaining(properties.get("dayRemaining").getAsInt());
                    if (properties.has("wateredToday")) tree.setWateredToday(properties.get("wateredToday").getAsBoolean());
                    if (properties.has("fertilizedToday")) tree.setFertilizedToday(properties.get("fertilizedToday").getAsBoolean());
                    if (properties.has("needsWaterToday")) tree.setNeedsWaterToday(properties.get("needsWaterToday").getAsBoolean());
                    if (properties.has("currentStage")) tree.setCurrentStage(properties.get("currentStage").getAsInt());
                    if (properties.has("readyToHarvest")) tree.setReadyToHarvest(properties.get("readyToHarvest").getAsBoolean());
                    if (properties.has("notWateredForTwoDays")) tree.setNotWateredForTwoDays(properties.get("notWateredForTwoDays").getAsBoolean());

                    growable = tree;
                    break;
                default: throw new JsonParseException("Unknown Growable type: " + type);
            }
            return growable;
        }
    }

    // Custom TypeAdapter for Trade (and its subclasses)
    private static class TradeAdapter implements JsonSerializer<Trade>, JsonDeserializer<Trade> {
        @Override
        public JsonElement serialize(Trade src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            String typeName = "";
            if (src instanceof TradeRequest) {
                typeName = "TradeRequest";
            } else if (src instanceof Buy) {
                typeName = "Buy";
            }
            result.addProperty("type", typeName);
            result.add("properties", context.serialize(src, src.getClass()));
            return result;
        }

        @Override
        public Trade deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            JsonElement properties = jsonObject.get("properties");

            switch (type) {
                case "TradeRequest": return context.deserialize(properties, TradeRequest.class);
                case "Buy": return context.deserialize(properties, Buy.class);
                default: throw new JsonParseException("Unknown Trade type: " + type);
            }
        }
    }

    // Custom TypeAdapter for Building (and its subclasses)
    private static class BuildingAdapter implements JsonSerializer<Building>, JsonDeserializer<Building> {
        @Override
        public JsonElement serialize(Building src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject result = new JsonObject();
            String typeName = "";
            if (src instanceof Shop) {
                typeName = "Shop";
            } else if (src instanceof GreenHouse) {
                typeName = "GreenHouse";
            } else if (src instanceof House) {
                typeName = "House";
            } else if (src instanceof NPCHouse) {
                typeName = "NPCHouse";
            }
            result.addProperty("type", typeName);
            // Serialize complex objects using the context to ensure adapters are used
            result.add("properties", context.serialize(src, src.getClass()));
            return result;
        }

        @Override
        public Building deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String type = jsonObject.get("type").getAsString();
            JsonElement properties = jsonObject.get("properties");

            switch (type) {
                case "Shop": return context.deserialize(properties, Shop.class);
                case "GreenHouse": return context.deserialize(properties, GreenHouse.class);
                case "House": return context.deserialize(properties, House.class);
                case "NPCHouse": return context.deserialize(properties, NPCHouse.class);
                default: throw new JsonParseException("Unknown Building type: " + type);
            }
        }
    }
}