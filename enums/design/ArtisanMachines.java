package enums.design;

import enums.items.Items;
import enums.items.Materials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum ArtisanMachines {
    CherryBomb("Cherry Bomb", Map.of(
            Materials.CopperOre, 4,
            Materials.Coal, 1
    ), 50),

    Bomb("Bomb", Map.of(
            Materials.IronOre, 4,
            Materials.Coal, 1
    ), 50 ),

    MegaBomb("Mega Bomb", Map.of(
            Materials.GoldOre, 4,
            Materials.Coal, 1
    ), 50 ),

    Sprinkler("Sprinkler", Map.of(
            Materials.CopperBar, 1,
            Materials.IronBar, 1
    ), 0 ),

    QualitySprinkler("Quality Sprinkler", Map.of(
            Materials.IronBar, 1,
            Materials.GoldBar, 1
    ), 0 ),

    IridiumSprinkler("Iridium Sprinkler", Map.of(
            Materials.GoldBar, 1,
            Materials.IridiumBar, 1
    ), 0 ),

    CharcoalKiln("Charcoal Kiln", Map.of(
            Materials.Wood, 20,
            Materials.CopperBar, 2
    ), 0 ),

    Furnace("Furnace", Map.of(
            Materials.CopperOre, 20,
            Materials.Stone, 25
    ), 0),

    Scarecrow("Scarecrow", Map.of(
            Materials.Wood, 50,
            Materials.Coal, 1,
            Materials.Fiber, 20
    ), 0),

    DeluxeScarecrow("Deluxe Scarecrow", Map.of(
            Materials.Wood, 50,
            Materials.Coal, 1,
            Materials.Fiber, 20,
            Materials.IridiumOre, 1
    ), 0 ),

    BeeHouse("Bee House", Map.of(
            Materials.Wood, 40,
            Materials.Coal, 8,
            Materials.IronBar, 1
    ), 0 ),

    CheesePress("Cheese Press", Map.of(
            Materials.Wood, 45,
            Materials.Stone, 45,
            Materials.CopperBar, 1
    ), 0 ),

    Keg("Keg", Map.of(
            Materials.Wood, 30,
            Materials.CopperBar, 1,
            Materials.IronBar, 1
    ), 0 ),

    Loom("Loom", Map.of(
            Materials.Wood, 60,
            Materials.Fiber, 30
    ), 0 ),

    MayonnaiseMachine("Mayonnaise Machine", Map.of(
            Materials.Wood, 15,
            Materials.Stone, 15,
            Materials.CopperBar, 1
    ), 0),

    OilMaker("Oil Maker", Map.of(
            Materials.Wood, 100,
            Materials.GoldBar, 1,
            Materials.IronBar, 1
    ), 0 ),

    PreservesJar("Preserves Jar", Map.of(
            Materials.Wood, 50,
            Materials.Stone, 40,
            Materials.Coal, 8
    ), 0),

    Dehydrator("Dehydrator", Map.of(
            Materials.Wood, 30,
            Materials.Stone, 20,
            Materials.Fiber, 30
    ), 0),

    FishSmoker("Fish Smoker", Map.of(
            Materials.Wood, 50,
            Materials.IronBar, 3,
            Materials.Coal, 10
    ), 0),

    MysticTreeSeed("Mystic Tree Seed", Map.of(
            Materials.Acorn, 5,
            Materials.MapleSeed, 5,
            Materials.PineCone, 5,
            Materials.MahoganySeed, 5
    ), 100);

    private final String displayName;
    private final Map<Materials, Integer> ingredients;
    private final int price;

    ArtisanMachines(String displayName, Map<Materials, Integer> ingredients,
                  int price) {
        this.displayName = displayName;
        this.ingredients = new HashMap<>(ingredients);
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<Items, Integer> getIngredients() {
        return new HashMap<>(ingredients);
    }

    public int getPrice() {
        return price;
    }
}
