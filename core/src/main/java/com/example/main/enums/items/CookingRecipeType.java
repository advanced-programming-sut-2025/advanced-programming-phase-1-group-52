package com.example.main.enums.items;

import java.util.HashMap;
import java.util.Map;
import com.example.main.enums.player.Skills;

public enum CookingRecipeType implements ItemType {
    Fried_Egg("Fried egg Recipe",50,null, false,0, 35, FoodType.Fried_Egg) {
        { ingredients.put(AnimalProductType.Egg, 1); }
    },
    Baked_Fish("Baked Fish Recipe", 75, null, false, 0, 100, FoodType.Baked_Fish) {
        {
            ingredients.put(FishType.Sardine, 1);
            ingredients.put(FishType.Salmon, 1);
        }
    },
    Salad("Salad Recipe", 113, null, false, 0, 110, FoodType.Salad) {
        {
            ingredients.put(ForagingCropType.Leek, 1);
            ingredients.put(ForagingCropType.Dandelion, 1);
        }
    },
    Omelet("Omelet Recipe", 100, null, false, 0, 125, FoodType.Omelet) {
        {
            ingredients.put(AnimalProductType.Egg, 1);
            ingredients.put(AnimalProductType.Milk, 1);
        }
    },
    Pumpkin_Pie("Pumpkin Pie Recipe", 225, null, false, 0, 385, FoodType.Pumpkin_Pie) {
        {
            ingredients.put(CropType.Pumpkin, 1);
            ingredients.put(MaterialType.Wheat_Flour, 1);
            ingredients.put(AnimalProductType.Milk, 1);
            ingredients.put(MaterialType.Sugar, 1);
        }
    },
    Spaghetti("Spaghetti Recipe", 75, null, false, 0, 120, FoodType.Spaghetti) {
        {
            ingredients.put(MaterialType.Wheat_Flour, 1);
            ingredients.put(CropType.Tomato, 1);
        }
    },
    Pizza("Pizza Recipe", 150, null, false, 0, 300, FoodType.Pizza) {
        {
            ingredients.put(MaterialType.Wheat_Flour, 1);
            ingredients.put(CropType.Tomato, 1);
            ingredients.put(ArtisanProductType.Cheese, 1);
        }
    },
    Tortilla("Tortilla Recipe", 50, null, false, 0, 50, FoodType.Tortilla) {
        { ingredients.put(CropType.Corn, 1); }
    },
    Maki_Roll("Maki Roll Recipe", 100, null, false, 0, 220, FoodType.Maki_Roll) {
        {
            ingredients.put(FishType.Albacore, 1); // Using a placeholder for "AnyFish"
            ingredients.put(MaterialType.Rice, 1);
        }
    },
    Triple_Shot_Espresso("Triple Shot Espresso Recipe", 200, null, true, 5, 450, FoodType.Triple_Shot_Espresso) {
        { ingredients.put(FoodType.Coffee, 3); }
    },
    Cookie("Cookie Recipe", 90, null, false, 0, 140, FoodType.Cookie) {
        {
            ingredients.put(MaterialType.Wheat_Flour, 1);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(AnimalProductType.Egg, 1);
        }
    },
    Hashbrowns("Hashbrowns Recipe", 90, Skills.Farming, false, 5, 120, FoodType.Hashbrowns) {
        {
            ingredients.put(CropType.Potato, 1);
            ingredients.put(MaterialType.Oil, 1);
        }
    },
    Pancakes("Pancakes Recipe", 90, Skills.Foraging, false, 11, 80, FoodType.Pancakes) {
        {
            ingredients.put(MaterialType.Wheat_Flour, 1);
            ingredients.put(AnimalProductType.Egg, 1);
        }
    },
    Fruit_Salad("Fruit Salad Recipe", 263, null, false, 0, 450, FoodType.Fruit_Salad) {
        {
            ingredients.put(CropType.Blueberry, 1);
            ingredients.put(CropType.Melon, 1);
            ingredients.put(FruitType.Apricot, 1);
        }
    },
    Red_Plate("Red Plate Recipe", 240, null, true, 3, 400, FoodType.Red_Plate) {
        {
            ingredients.put(CropType.Red_Cabbage, 1);
            ingredients.put(CropType.Radish, 1);
        }
    },
    Bread("Bread Recipe", 50, null, false, 0, 60, FoodType.Bread) {
        { ingredients.put(MaterialType.Wheat_Flour, 1); }
    },
    Salmon_Dinner("Salmon Dinner Recipe", 125, null, false, 0, 300, FoodType.Salmon_Dinner) {
        {
            ingredients.put(FishType.Salmon, 1);
            ingredients.put(CropType.Amaranth, 1);
            ingredients.put(CropType.Kale, 1);
        }
    },
    Vegetable_Medley("Vegetable Medley Recipe", 165, null, false, 0, 120, FoodType.Vegetable_Medley) {
        {
            ingredients.put(CropType.Tomato, 1);
            ingredients.put(CropType.Beet, 1);
        }
    },
    Farmers_Lunch("Farmer's Lunch Recipe", 200, Skills.Farming, false, 5, 150, FoodType.Farmers_Lunch) {
        {
            ingredients.put(FoodType.Omelet, 1);
            ingredients.put(CropType.Parsnip, 1);
        }
    },
    Survival_Burger("Survival Burger Recipe", 125, Skills.Foraging, false, 5, 180, FoodType.Survival_Burger) {
        {
            ingredients.put(FoodType.Bread, 1);
            ingredients.put(CropType.Carrot, 1);
            ingredients.put(CropType.Eggplant, 1);
        }
    },
    Dish_O_The_Sea("Dish O' The Sea Recipe", 150, Skills.Fishing, false, 5, 220, FoodType.Dish_O_The_Sea) {
        {
            ingredients.put(FishType.Sardine, 2);
            ingredients.put(FoodType.Hashbrowns, 1);
        }
    },
    Seafoam_Pudding("Seafoam Pudding Recipe", 175, Skills.Fishing, false, 10, 300, FoodType.Seafoam_Pudding) {
        {
            ingredients.put(FishType.Flounder, 1);
            ingredients.put(FishType.Midnight_Carp, 1);
        }
    },
    Miners_Treat("Miner's Treat Recipe", 125, Skills.Mining, false, 5, 200, FoodType.Miners_Treat) {
        {
            ingredients.put(CropType.Carrot, 2);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(AnimalProductType.Milk, 1);
        }
    };

    protected final Map<ItemType, Integer> ingredients = new HashMap<>();
    private final String displayName;
    private final int energy;
    private final Skills skillBuff;
    private final boolean isBuffMaxEnergy;
    private final int effectiveTime;
    private final int sellPrice;
    private final FoodType foodType;

    CookingRecipeType(String displayName, int energy, Skills skillBuff, boolean isBuffMaxEnergy, int effectiveTime, int sellPrice, FoodType foodType) {
        this.displayName = displayName;
        this.energy = energy;
        this.skillBuff = skillBuff;
        this.isBuffMaxEnergy = isBuffMaxEnergy;
        this.effectiveTime = effectiveTime;
        this.sellPrice = sellPrice;
        this.foodType = foodType;
    }

    public Map<ItemType, Integer> getIngredients() { return ingredients; }
    public String getDisplayName() { return displayName; }
    @Override public boolean isTool() { return false; }
    public int getEnergy() { return energy; }
    public Skills getSkillBuff() { return skillBuff; }
    public boolean isBuffMaxEnergy() { return isBuffMaxEnergy; }
    public int getEffectiveTime() { return effectiveTime; }
    public int getSellPrice() { return sellPrice; }
    @Override public String getName() { return this.displayName; }
    @Override public String getEnumName() { return name(); }
    public FoodType getFoodType() { return foodType; }
}
