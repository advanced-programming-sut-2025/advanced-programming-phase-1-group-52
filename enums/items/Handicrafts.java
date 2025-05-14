package enums.items;

import java.util.HashMap;
import java.util.Map;

public enum Handicrafts implements ItemType {
    CherryBomb("Cherry Bomb", Map.of(
            MaterialType.CopperOre, 4,
            MaterialType.Coal, 1
    ), 50),

    Bomb("Bomb", Map.of(
            MaterialType.IronOre, 4,
            MaterialType.Coal, 1
    ), 50),

    MegaBomb("Mega Bomb", Map.of(
            MaterialType.GoldOre, 4,
            MaterialType.Coal, 1
    ), 50),

    Sprinkler("Sprinkler", Map.of(
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0),

    QualitySprinkler("Quality Sprinkler", Map.of(
            MaterialType.IronBar, 1,
            MaterialType.GoldBar, 1
    ), 0),

    IridiumSprinkler("Iridium Sprinkler", Map.of(
            MaterialType.GoldBar, 1,
            MaterialType.IridiumBar, 1
    ), 0),

    CharcoalKiln("Charcoal Kiln", Map.of(
            MaterialType.Wood, 20,
            MaterialType.CopperBar, 2
    ), 0),

    Furnace("Furnace", Map.of(
            MaterialType.CopperOre, 20,
            MaterialType.Stone, 25
    ), 0),

    Scarecrow("Scarecrow", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20
    ), 0),

    DeluxeScarecrow("Deluxe Scarecrow", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20,
            MaterialType.IridiumOre, 1
    ), 0),

    BeeHouse("Bee House", Map.of(
            MaterialType.Wood, 40,
            MaterialType.Coal, 8,
            MaterialType.IronBar, 1
    ), 0),

    CheesePress("Cheese Press", Map.of(
            MaterialType.Wood, 45,
            MaterialType.Stone, 45,
            MaterialType.CopperBar, 1
    ), 0),

    Keg("Keg", Map.of(
            MaterialType.Wood, 30,
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0),

    Loom("Loom", Map.of(
            MaterialType.Wood, 60,
            MaterialType.Fiber, 30
    ), 0),

    MayonnaiseMachine("Mayonnaise Machine", Map.of(
            MaterialType.Wood, 15,
            MaterialType.Stone, 15,
            MaterialType.CopperBar, 1
    ), 0),

    OilMaker("Oil Maker", Map.of(
            MaterialType.Wood, 100,
            MaterialType.GoldBar, 1,
            MaterialType.IronBar, 1
    ), 0),

    PreservesJar("Preserves Jar", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Stone, 40,
            MaterialType.Coal, 8
    ), 0),

    Dehydrator("Dehydrator", Map.of(
            MaterialType.Wood, 30,
            MaterialType.Stone, 20,
            MaterialType.Fiber, 30
    ), 0),

    FishSmoker("Fish Smoker", Map.of(
            MaterialType.Wood, 50,
            MaterialType.IronBar, 3,
            MaterialType.Coal, 10
    ), 0),

    MysticTreeSeed("Mystic Tree Seed", Map.of(
            MaterialType.Acorn, 5,
            MaterialType.MapleSeed, 5,
            MaterialType.PineCone, 5,
            MaterialType.MahoganySeed, 5
    ), 100);

    private final String displayName;
    private final Map<MaterialType, Integer> ingredients;
    private final int price;

    Handicrafts(String displayName, Map<MaterialType, Integer> ingredients,
                  int price) {
        this.displayName = displayName;
        this.ingredients = new HashMap<>(ingredients);
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<MaterialType, Integer> getIngredients() {
        return new HashMap<>(ingredients);
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean isTool() {
        return false;
    }
}
