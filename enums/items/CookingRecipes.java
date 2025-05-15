package enums.items;


import enums.design.ShopType;
import enums.player.Skills;

import java.util.HashMap;
import java.util.Map;

public enum CookingRecipes implements ItemType {
    FriedEgg("Fried egg",50,null, false,0, 35) {
        {
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    BakedFish("Baked Fish", 75, null, false, 0, 100) {
        {
            ingredients.put(MaterialType.Sardine, 1);
            ingredients.put(MaterialType.Salmon, 1);
            ingredients.put(MaterialType.Wheat, 1);
        }
    },

    Salad("Salad", 113, null, false, 0, 110) {
        {
            ingredients.put(MaterialType.Leek, 1);
            ingredients.put(MaterialType.Dandelion, 1);
        }
    },

    Omelet("Omelet", 100, null, false, 0, 125) {
        {
            ingredients.put(MaterialType.Egg, 1);
            ingredients.put(MaterialType.Milk, 1);
        }
    },

    PumpkinPie("Pumpkin pie", 225, null, false, 0, 385) {
        {
            ingredients.put(MaterialType.Pumpkin, 1);
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Milk, 1);
            ingredients.put(MaterialType.Sugar, 1);
        }
    },

    Spaghetti("Spaghetti", 75, null, false, 0, 120) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Tomato, 1);
        }
    },

    Pizza("Pizza", 150, null, false, 0, 300) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Tomato, 1);
            ingredients.put(MaterialType.Cheese, 1);
        }
    },

    Tortilla("Tortilla", 50, null, false, 0, 50) {
        {
            ingredients.put(MaterialType.Corn, 1);
        }
    },

    MakiRoll("Maki Roll", 100, null, false, 0, 220) {
        {
            ingredients.put(MaterialType.AnyFish, 1);
            ingredients.put(MaterialType.Rice, 1);
            ingredients.put(MaterialType.Fiber, 1);
        }
    },

    TripleShotEspresso("Triple Shot Espresso", 200, null, true, 5, 450) {
        {
            ingredients.put(MaterialType.Coffee, 3);
        }
    },

    Cookie("Cookie", 90, null, false, 0, 140) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    HashBrowns("Hash browns", 90, Skills.Farming, false, 5, 120) {
        {
            ingredients.put(MaterialType.Potato, 1);
            ingredients.put(MaterialType.Oil, 1);
        }
    },

    Pancakes("Pancakes", 90, Skills.Foraging, false, 11, 80) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    FruitSalad("Fruit salad", 263, null, false, 0, 450) {
        {
            ingredients.put(MaterialType.Blueberry, 1);
            ingredients.put(MaterialType.Melon, 1);
            ingredients.put(MaterialType.Apricot, 1);
        }
    },

    RedPlate("Red plate", 240, null, true, 3, 400) {
        {
            ingredients.put(MaterialType.RedCabbage, 1);
            ingredients.put(MaterialType.Radish, 1);
        }
    },

    Bread("Bread", 50, null, false, 0, 60) {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
        }
    },

    SalmonDinner("Salmon dinner", 125, null, false, 0, 300) {
        {
            ingredients.put(MaterialType.Salmon, 1);
            ingredients.put(MaterialType.Amaranth, 1);
            ingredients.put(MaterialType.Kale, 1);
        }
    },

    VegetableMedley("Vegetable medley", 165, null, false, 0, 120) {
        {
            ingredients.put(MaterialType.Tomato, 1);
            ingredients.put(MaterialType.Beet, 1);
        }
    },

    FarmersLunch("Farmer's lunch", 200, Skills.Farming, false, 5, 150) {
        {
            ingredients.put(MaterialType.Omelet, 1);
            ingredients.put(MaterialType.Parsnip, 1);
        }
    },

    SurvivalBurger("Survival burger", 125, Skills.Foraging, false, 5, 180) {
        {
            ingredients.put(MaterialType.Bread, 1);
            ingredients.put(MaterialType.Carrot, 1);
            ingredients.put(MaterialType.Eggplant, 1);
        }
    },

    DishOTheSea("Dish O' the Sea", 150, Skills.Fishing, false, 5, 220) {
        {
            ingredients.put(MaterialType.Sardine, 2);
            ingredients.put(MaterialType.HashBrowns, 1);
        }
    },

    SeaFoamPudding("SeaFoam Pudding", 175, Skills.Fishing, false, 10, 300) {
        {
            ingredients.put(MaterialType.Flounder, 1);
            ingredients.put(MaterialType.MidnightCarp, 1);
        }
    },

    MinersTreat("Miner's treat", 125, Skills.Extraction, false, 5, 200) {
        {
            ingredients.put(MaterialType.Carrot, 2);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(MaterialType.Milk, 1);
        }
    };

    protected final Map<MaterialType, Integer> ingredients = new HashMap<>();
    private final String displayName;
    private final int energy;
    private final Skills skillBuff;
    private final boolean isBuffMaxEnergy;
    private final int effectiveTime;
    private final int sellPrice;

    private CookingRecipes(String displayName, int energy, Skills skillBuff, boolean isBuffMaxEnergy, int effectiveTime, int sellPrice) {
        this.displayName = displayName;
        this.energy = energy;
        this.skillBuff = skillBuff;
        this.isBuffMaxEnergy = isBuffMaxEnergy;
        this.effectiveTime = effectiveTime;
        this.sellPrice = sellPrice;
    }

    public Map<MaterialType, Integer> getIngredients() {
        return new HashMap<>(ingredients);
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
}
