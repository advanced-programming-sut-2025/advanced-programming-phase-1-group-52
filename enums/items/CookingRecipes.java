package enums.items;


import java.util.HashMap;
import java.util.Map;

public enum CookingRecipes implements Items {
    FriedEgg("Fried egg") {
        {
            ingredients.put(Materials.Egg, 1);
        }
    },

    BakedFish("Baked Fish") {
        {
            ingredients.put(Materials.Sardine, 1);
            ingredients.put(Materials.Salmon, 1);
            ingredients.put(Materials.Wheat, 1);
        }
    },

    Salad("Salad") {
        {
            ingredients.put(Materials.Leek, 1);
            ingredients.put(Materials.Dandelion, 1);
        }
    },

    Omelet("Omelet") {
        {
            ingredients.put(Materials.Egg, 1);
            ingredients.put(Materials.Milk, 1);
        }
    },

    PumpkinPie("Pumpkin pie") {
        {
            ingredients.put(Materials.Pumpkin, 1);
            ingredients.put(Materials.WheatFlour, 1);
            ingredients.put(Materials.Milk, 1);
            ingredients.put(Materials.Sugar, 1);
        }
    },

    Spaghetti("Spaghetti") {
        {
            ingredients.put(Materials.WheatFlour, 1);
            ingredients.put(Materials.Tomato, 1);
        }
    },

    Pizza("Pizza") {
        {
            ingredients.put(Materials.WheatFlour, 1);
            ingredients.put(Materials.Tomato, 1);
            ingredients.put(Materials.Cheese, 1);
        }
    },

    Tortilla("Tortilla") {
        {
            ingredients.put(Materials.Corn, 1);
        }
    },

    MakiRoll("Maki Roll") {
        {
            ingredients.put(Materials.AnyFish, 1);
            ingredients.put(Materials.Rice, 1);
            ingredients.put(Materials.Fiber, 1);
        }
    },

    TripleShotEspresso("Triple Shot Espresso") {
        {
            ingredients.put(Materials.Coffee, 3);
        }
    },

    Cookie("Cookie") {
        {
            ingredients.put(Materials.WheatFlour, 1);
            ingredients.put(Materials.Sugar, 1);
            ingredients.put(Materials.Egg, 1);
        }
    },

    HashBrowns("Hash browns") {
        {
            ingredients.put(Materials.Potato, 1);
            ingredients.put(Materials.Oil, 1);
        }
    },

    Pancakes("Pancakes") {
        {
            ingredients.put(Materials.WheatFlour, 1);
            ingredients.put(Materials.Egg, 1);
        }
    },

    FruitSalad("Fruit salad") {
        {
            ingredients.put(Materials.Blueberry, 1);
            ingredients.put(Materials.Melon, 1);
            ingredients.put(Materials.Apricot, 1);
        }
    },

    RedPlate("Red plate") {
        {
            ingredients.put(Materials.RedCabbage, 1);
            ingredients.put(Materials.Radish, 1);
        }
    },

    Bread("Bread") {
        {
            ingredients.put(Materials.WheatFlour, 1);
        }
    },

    SalmonDinner("Salmon dinner") {
        {
            ingredients.put(Materials.Salmon, 1);
            ingredients.put(Materials.Amaranth, 1);
            ingredients.put(Materials.Kale, 1);
        }
    },

    VegetableMedley("Vegetable medley") {
        {
            ingredients.put(Materials.Tomato, 1);
            ingredients.put(Materials.Beet, 1);
        }
    },

    FarmersLunch("Farmer's lunch") {
        {
            ingredients.put(Materials.Omelet, 1);
            ingredients.put(Materials.Parsnip, 1);
        }
    },

    SurvivalBurger("Survival burger") {
        {
            ingredients.put(Materials.Bread, 1);
            ingredients.put(Materials.Carrot, 1);
            ingredients.put(Materials.Eggplant, 1);
        }
    },

    DishOTheSea("Dish O' the Sea") {
        {
            ingredients.put(Materials.Sardine, 2);
            ingredients.put(Materials.HashBrowns, 1);
        }
    },

    SeafoamPudding("Seafoam Pudding") {
        {
            ingredients.put(Materials.Flounder, 1);
            ingredients.put(Materials.MidnightCarp, 1);
        }
    },

    MinersTreat("Miner's treat") {
        {
            ingredients.put(Materials.Carrot, 2);
            ingredients.put(Materials.Sugar, 1);
            ingredients.put(Materials.Milk, 1);
        }
    };

    protected final Map<Materials, Integer> ingredients = new HashMap<>();
    private final String displayName;

    private CookingRecipes(String displayName) {
        this.displayName = displayName;
    }

    public Map<Materials, Integer> getIngredients() {
        return new HashMap<>(ingredients);
    }

    public String getDisplayName() {
        return displayName;
    }

}
