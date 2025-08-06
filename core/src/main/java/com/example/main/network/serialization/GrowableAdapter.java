package com.example.main.network.serialization;

import com.example.main.enums.items.CropType;
import com.example.main.enums.items.FruitType;
import com.example.main.enums.items.Growable;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.TreeType;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fruit;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class GrowableAdapter implements JsonSerializer<Growable>, JsonDeserializer<Growable> {

    private static final String CLASS_TYPE_FIELD = "growableClassType";

    @Override
    public JsonElement serialize(Growable src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        // Add a field to indicate the concrete class type
        jsonObject.addProperty(CLASS_TYPE_FIELD, src.getClass().getName());
        // Serialize all fields of the concrete object
        // Use context.serialize for polymorphic fields within Growable if any
        if (src instanceof Crop) {
            Crop crop = (Crop) src;
            jsonObject.addProperty("cropType", crop.getCropType().getName()); // Serialize as string
            jsonObject.addProperty("dayPassed", crop.getDayPassed());
            jsonObject.addProperty("dayRemaining", crop.getDayRemaining());
            jsonObject.addProperty("isWateredToday", crop.isWateredToday());
            jsonObject.addProperty("isFertilizedToday", crop.isFertilizedToday());
            jsonObject.addProperty("needsWaterToday", crop.isNeedsWaterToday());
            jsonObject.addProperty("currentStage", crop.getCurrentStage());
            jsonObject.addProperty("isReadyToHarvest", crop.isReadyToHarvest());
            jsonObject.addProperty("isNotWateredForTwoDays", crop.isNotWateredForTwoDays());
            jsonObject.addProperty("canBeHarvestAgain", crop.canBeHarvestAgain());
        } else if (src instanceof Fruit) {
            Fruit fruit = (Fruit) src;
            jsonObject.addProperty("fruitType", fruit.getFruitType().name()); // Serialize as string
            jsonObject.addProperty("treeType", fruit.getTreeType().name());   // Serialize as string
            jsonObject.addProperty("currentStage", fruit.getCurrentStage());
            jsonObject.addProperty("dayPassed", fruit.getDayPassed());
            jsonObject.addProperty("dayRemaining", fruit.getDayRemaining());
            jsonObject.addProperty("isWateredToday", fruit.isWateredToday());
            jsonObject.addProperty("isFertilizedToday", fruit.isFertilizedToday());
            jsonObject.addProperty("needsWaterToday", fruit.isNeedsWaterToday());
            jsonObject.addProperty("isReadyToHarvest", fruit.isReadyToHarvest());
            jsonObject.addProperty("hasBeenHarvestedToday", fruit.hasBeenHarvestedToday());
            jsonObject.addProperty("unwateredDays", fruit.getUnwateredDays());
        }
        return jsonObject;
    }

    @Override
    public Growable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String classType = jsonObject.get(CLASS_TYPE_FIELD).getAsString();

        Growable growable = null;
        try {
            if (classType.equals(Crop.class.getName())) {
                // Deserialize CropType as String and convert to enum
                CropType cropType = null;
                JsonElement cropTypeElement = jsonObject.get("cropType");
                if (cropTypeElement != null && !cropTypeElement.isJsonNull()) {
                    cropType = CropType.valueOf(cropTypeElement.getAsString());
                }
                Crop crop = new Crop(cropType, 1); // Number can be default for now, adjusted later if needed

                if (jsonObject.has("dayPassed")) crop.setDayPassed(jsonObject.get("dayPassed").getAsInt());
                if (jsonObject.has("dayRemaining")) crop.setDayRemaining(jsonObject.get("dayRemaining").getAsInt());
                if (jsonObject.has("isWateredToday")) crop.setWateredToday(jsonObject.get("isWateredToday").getAsBoolean());
                if (jsonObject.has("isFertilizedToday")) crop.setFertilizedToday(jsonObject.get("isFertilizedToday").getAsBoolean());
                if (jsonObject.has("needsWaterToday")) crop.setNeedsWaterToday(jsonObject.get("needsWaterToday").getAsBoolean());
                if (jsonObject.has("currentStage")) crop.setCurrentStage(jsonObject.get("currentStage").getAsInt());
                if (jsonObject.has("isReadyToHarvest")) crop.setReadyToHarvest(jsonObject.get("isReadyToHarvest").getAsBoolean());
                if (jsonObject.has("isNotWateredForTwoDays")) crop.setNotWateredForTwoDays(jsonObject.get("isNotWateredForTwoDays").getAsBoolean());
                if (jsonObject.has("canBeHarvestAgain")) crop.setCanBeHarvestAgain(jsonObject.get("canBeHarvestAgain").getAsBoolean());
                growable = crop;
            } else if (classType.equals(Fruit.class.getName())) {
                // Deserialize FruitType as String and convert to enum
                FruitType fruitType = null;
                JsonElement fruitTypeElement = jsonObject.get("fruitType");
                if (fruitTypeElement != null && !fruitTypeElement.isJsonNull()) {
                    fruitType = FruitType.valueOf(fruitTypeElement.getAsString());
                }
                // Deserialize TreeType as String and convert to enum
                TreeType treeType = null;
                JsonElement treeTypeElement = jsonObject.get("treeType");
                if (treeTypeElement != null && !treeTypeElement.isJsonNull()) {
                    treeType = TreeType.valueOf(treeTypeElement.getAsString());
                }
                Fruit fruit = new Fruit(fruitType, 1); // Number can be default for now

                if (jsonObject.has("currentStage")) fruit.setCurrentStage(jsonObject.get("currentStage").getAsInt());
                if (jsonObject.has("dayPassed")) fruit.setDayPassed(jsonObject.get("dayPassed").getAsInt());
                if (jsonObject.has("dayRemaining")) fruit.setDayRemaining(jsonObject.get("dayRemaining").getAsInt());
                if (jsonObject.has("isWateredToday")) fruit.setWateredToday(jsonObject.get("isWateredToday").getAsBoolean());
                if (jsonObject.has("isFertilizedToday")) fruit.setFertilizedToday(jsonObject.get("isFertilizedToday").getAsBoolean());
                if (jsonObject.has("needsWaterToday")) fruit.setNeedsWaterToday(jsonObject.get("needsWaterToday").getAsBoolean());
                if (jsonObject.has("isReadyToHarvest")) fruit.setReadyToHarvest(jsonObject.get("isReadyToHarvest").getAsBoolean());
                if (jsonObject.has("hasBeenHarvestedToday")) fruit.setHasBeenHarvestedToday(jsonObject.get("hasBeenHarvestedToday").getAsBoolean());
                if (jsonObject.has("unwateredDays")) fruit.setUnwateredDays(jsonObject.get("unwateredDays").getAsInt());
                growable = fruit;
            } else {
                throw new JsonParseException("Unknown Growable type: " + classType);
            }
        } catch (Exception e) {
            throw new JsonParseException("Error deserializing Growable: " + e.getMessage(), e);
        }
        return growable;
    }
}