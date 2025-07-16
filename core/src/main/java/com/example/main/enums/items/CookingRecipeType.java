package com.example.main.enums.items;


import java.util.HashMap;
import java.util.Map;

import com.example.main.enums.player.Skills;

public enum CookingRecipeType implements ItemType {
    FriedEgg("Fried egg Recipe",50,null, false,0, 35, FoodType.FriedEgg) {
        {
            ingredients.put(AnimalProductType.Egg, 1);
        }
    },

    BakedFish("Baked Fish Recipe", 75, null, false, 0, 100, FoodType.BakedFish) {
        {
            ingredients.put(MaterialType.Sardine, 1);
            ingredients.put(MaterialType.Salmon, 1);
            ingredients.put(MaterialType.Wheat, 1);
        }
    },

    Salad("Salad Recipe", 113, null, false, 0, 110, FoodType.Salad) {
        {
            ingredients.put(MaterialType.Leek, 1);
            ingredients.put(MaterialType.Dandelion, 1);
        }
    },

    Omelet("Omelet Recipe", 100, null, false, 0, 125, FoodType.Olmelet) {
        {
            ingredients.put(MaterialType.Egg, 1);
            ingredients.put(MaterialType.Milk, 1);
        }
    },

    PumpkinPie("Pumpkin pie Recipe", 225, null, false, 0, 385, FoodType.PumpkinPie) {
        {
            ingredients.put(MaterialType.Pumpkin, 1);
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Milk, 1);
            ingredients.put(MaterialType.Sugar, 1);
        }
    },

    Spaghetti("Spaghetti Recipe", 75, null, false, 0, 120, FoodType.Spaghetti) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Tomato, 1);
        }
    },

    Pizza("Pizza Recipe", 150, null, false, 0, 300, FoodType.Pizza) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Tomato, 1);
            ingredients.put(MaterialType.Cheese, 1);
        }
    },

    Tortilla("Tortilla Recipe", 50, null, false, 0, 50, FoodType.Tortilla) {
        {
            ingredients.put(MaterialType.Corn, 1);
        }
    },

    MakiRoll("Maki Roll Recipe", 100, null, false, 0, 220, FoodType.MakiRoll) {
        {
            ingredients.put(MaterialType.AnyFish, 1);
            ingredients.put(MaterialType.Rice, 1);
            ingredients.put(MaterialType.Fiber, 1);
        }
    },

    TripleShotEspresso("Triple Shot Espresso Recipe", 200, null, true, 5, 450, FoodType.TripleShotEspresso) {
        {
            ingredients.put(MaterialType.Coffee, 3);
        }
    },

    Cookie("Cookie Recipe", 90, null, false, 0, 140, FoodType.Cookie) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    HashBrowns("Hash browns Recipe", 90, Skills.Farming, false, 5, 120, FoodType.HashBrowns) {
        {
            ingredients.put(MaterialType.Potato, 1);
            ingredients.put(MaterialType.Oil, 1);
        }
    },

    Pancakes("Pancakes Recipe", 90, Skills.Foraging, false, 11, 80, FoodType.Pancakes) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    FruitSalad("Fruit salad Recipe", 263, null, false, 0, 450, FoodType.FruitSalad) {
        {
            ingredients.put(MaterialType.Blueberry, 1);
            ingredients.put(MaterialType.Melon, 1);
            ingredients.put(MaterialType.Apricot, 1);
        }
    },

    RedPlate("Red plate Recipe", 240, null, true, 3, 400, FoodType.RedPlate) {
        {
            ingredients.put(MaterialType.RedCabbage, 1);
            ingredients.put(MaterialType.Radish, 1);
        }
    },

    Bread("Bread Recipe", 50, null, false, 0, 60, FoodType.Bread) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
        }
    },

    SalmonDinner("Salmon Dinner Recipe", 125, null, false, 0, 300, FoodType.SalmonDinner) {
        {
            ingredients.put(MaterialType.Salmon, 1);
            ingredients.put(MaterialType.Amaranth, 1);
            ingredients.put(MaterialType.Kale, 1);
        }
    },

    VegetableMedley("Vegetable medley Recipe", 165, null, false, 0, 120, FoodType.VegetableMedley) {
        {
            ingredients.put(MaterialType.Tomato, 1);
            ingredients.put(MaterialType.Beet, 1);
        }
    },

    FarmersLunch("Farmer's lunch Recipe", 200, Skills.Farming, false, 5, 150, FoodType.FarmersLunch) {
        {
            ingredients.put(MaterialType.Omelet, 1);
            ingredients.put(MaterialType.Parsnip, 1);
        }
    },

    SurvivalBurger("Survival burger Recipe", 125, Skills.Foraging, false, 5, 180, FoodType.SurvivalBurger) {
        {
            ingredients.put(MaterialType.Bread, 1);
            ingredients.put(MaterialType.Carrot, 1);
            ingredients.put(MaterialType.Eggplant, 1);
        }
    },

    DishOTheSea("Dish O' the Sea Recipe", 150, Skills.Fishing, false, 5, 220, FoodType.DishOfTheSea) {
        {
            ingredients.put(MaterialType.Sardine, 2);
            ingredients.put(MaterialType.HashBrowns, 1);
        }
    },

    SeaFoamPudding("SeaFoam Pudding Recipe", 175, Skills.Fishing, false, 10, 300, FoodType.SeaFormPudding) {
        {
            ingredients.put(MaterialType.Flounder, 1);
            ingredients.put(MaterialType.MidnightCarp, 1);
        }
    },

    MinersTreat("Miner's treat Recipe", 125, Skills.Mining, false, 5, 200, FoodType.MinersTreat) {
        {
            ingredients.put(MaterialType.Carrot, 2);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(MaterialType.Milk, 1);
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

    private CookingRecipeType(String displayName, int energy, Skills skillBuff, boolean isBuffMaxEnergy, int effectiveTime, int sellPrice, FoodType foodType) {
        this.displayName = displayName;
        this.energy = energy;
        this.skillBuff = skillBuff;
        this.isBuffMaxEnergy = isBuffMaxEnergy;
        this.effectiveTime = effectiveTime;
        this.sellPrice = sellPrice;
        this.foodType = foodType;
    }

    public Map<ItemType, Integer> getIngredients() {
        return ingredients;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    public int getEnergy() {
        return energy;
    }

    public Skills getSkillBuff() {
        return skillBuff;
    }

    public boolean isBuffMaxEnergy() {
        return isBuffMaxEnergy;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    @Override
    public String getName() {
        return this.displayName;
    }

    @Override
    public String getEnumName() {
        return name();
    }

    public FoodType getFoodType() {
        return foodType;
    }
}
