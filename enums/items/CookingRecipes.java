package enums.items;


import java.util.HashMap;
import java.util.Map;

public enum CookingRecipes implements ItemType {
    FriedEgg("Fried egg") {
        {
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    BakedFish("Baked Fish") {
        {
            ingredients.put(MaterialType.Sardine, 1);
            ingredients.put(MaterialType.Salmon, 1);
            ingredients.put(MaterialType.Wheat, 1);
        }
    },

    Salad("Salad") {
        {
            ingredients.put(MaterialType.Leek, 1);
            ingredients.put(MaterialType.Dandelion, 1);
        }
    },

    Omelet("Omelet") {
        {
            ingredients.put(MaterialType.Egg, 1);
            ingredients.put(MaterialType.Milk, 1);
        }
    },

    PumpkinPie("Pumpkin pie") {
        {
            ingredients.put(MaterialType.Pumpkin, 1);
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Milk, 1);
            ingredients.put(MaterialType.Sugar, 1);
        }
    },

    Spaghetti("Spaghetti") {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Tomato, 1);
        }
    },

    Pizza("Pizza") {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Tomato, 1);
            ingredients.put(MaterialType.Cheese, 1);
        }
    },

    Tortilla("Tortilla") {
        {
            ingredients.put(MaterialType.Corn, 1);
        }
    },

    MakiRoll("Maki Roll") {
        {
            ingredients.put(MaterialType.AnyFish, 1);
            ingredients.put(MaterialType.Rice, 1);
            ingredients.put(MaterialType.Fiber, 1);
        }
    },

    TripleShotEspresso("Triple Shot Espresso") {
        {
            ingredients.put(MaterialType.Coffee, 3);
        }
    },

    Cookie("Cookie") {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    HashBrowns("Hash browns") {
        {
            ingredients.put(MaterialType.Potato, 1);
            ingredients.put(MaterialType.Oil, 1);
        }
    },

    Pancakes("Pancakes") {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
            ingredients.put(MaterialType.Egg, 1);
        }
    },

    FruitSalad("Fruit salad") {
        {
            ingredients.put(MaterialType.Blueberry, 1);
            ingredients.put(MaterialType.Melon, 1);
            ingredients.put(MaterialType.Apricot, 1);
        }
    },

    RedPlate("Red plate") {
        {
            ingredients.put(MaterialType.RedCabbage, 1);
            ingredients.put(MaterialType.Radish, 1);
        }
    },

    Bread("Bread") {
        {
            ingredients.put(MaterialType.WheatFlour, 1);
        }
    },

    SalmonDinner("Salmon dinner") {
        {
            ingredients.put(MaterialType.Salmon, 1);
            ingredients.put(MaterialType.Amaranth, 1);
            ingredients.put(MaterialType.Kale, 1);
        }
    },

    VegetableMedley("Vegetable medley") {
        {
            ingredients.put(MaterialType.Tomato, 1);
            ingredients.put(MaterialType.Beet, 1);
        }
    },

    FarmersLunch("Farmer's lunch") {
        {
            ingredients.put(MaterialType.Omelet, 1);
            ingredients.put(MaterialType.Parsnip, 1);
        }
    },

    SurvivalBurger("Survival burger") {
        {
            ingredients.put(MaterialType.Bread, 1);
            ingredients.put(MaterialType.Carrot, 1);
            ingredients.put(MaterialType.Eggplant, 1);
        }
    },

    DishOTheSea("Dish O' the Sea") {
        {
            ingredients.put(MaterialType.Sardine, 2);
            ingredients.put(MaterialType.HashBrowns, 1);
        }
    },

    SeafoamPudding("Seafoam Pudding") {
        {
            ingredients.put(MaterialType.Flounder, 1);
            ingredients.put(MaterialType.MidnightCarp, 1);
        }
    },

    MinersTreat("Miner's treat") {
        {
            ingredients.put(MaterialType.Carrot, 2);
            ingredients.put(MaterialType.Sugar, 1);
            ingredients.put(MaterialType.Milk, 1);
        }
    };

    protected final Map<MaterialType, Integer> ingredients = new HashMap<>();
    private final String displayName;

    private CookingRecipes(String displayName) {
        this.displayName = displayName;
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

}
