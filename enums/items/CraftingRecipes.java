package enums.items;


import java.util.HashMap;
import java.util.Map;

public enum CraftingRecipes implements ItemType {
    CherryBombRecipe("Cherry Bomb Recipe", Map.of(
        MaterialType.CopperOre, 4,
        MaterialType.Coal, 1
    ), 50, CraftingMachineType.CHERRY_BOMB),

    BombRecipe("Bomb Recipe", Map.of(
            MaterialType.IronOre, 4,
            MaterialType.Coal, 1
    ), 50, CraftingMachineType.BOMB ),

    MegaBombRecipe("Mega Bomb Recipe", Map.of(
            MaterialType.GoldOre, 4,
            MaterialType.Coal, 1
    ), 50 , CraftingMachineType.MEGA_BOMB),

    SprinklerRecipe("Sprinkler Recipe", Map.of (
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0 , CraftingMachineType.SPRINKLER),

    QualitySprinklerRecipe("Quality Sprinkler Recipe", Map.of(
            MaterialType.IronBar, 1,
            MaterialType.GoldBar, 1
    ), 0 , CraftingMachineType.QUALITY_SPRINKLER),

    IridiumSprinklerRecipe("Iridium Sprinkler Recipe", Map.of(
            MaterialType.GoldBar, 1,
            MaterialType.IridiumBar, 1
    ), 0 , CraftingMachineType.IRIDIUM_SPRINKLER),

    CharcoalKilnRecipe("Charcoal Kiln Recipe", Map.of(
            MaterialType.Wood, 20,
            MaterialType.CopperBar, 2
    ), 0 , CraftingMachineType.CHARCOAL_KILN),

    FurnaceRecipe("Furnace Recipe", Map.of(
            MaterialType.CopperOre, 20,
            MaterialType.Stone, 25
    ), 0, CraftingMachineType.FURNACE),

    ScarecrowRecipe("Scarecrow Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20
    ), 0, CraftingMachineType.SCARECROW),

    DeluxeScarecrowRecipe("Deluxe Scarecrow Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Coal, 1,
            MaterialType.Fiber, 20,
            MaterialType.IridiumOre, 1
    ), 0 , CraftingMachineType.DELUXE_SCARECROW),

    BeeHouseRecipe("Bee House Recipe", Map.of(
            MaterialType.Wood, 40,
            MaterialType.Coal, 8,
            MaterialType.IronBar, 1
    ), 0 , CraftingMachineType.BEE_HOUSE),

    CheesePressRecipe("Cheese Press Recipe", Map.of(
            MaterialType.Wood, 45,
            MaterialType.Stone, 45,
            MaterialType.CopperBar, 1
    ), 0, CraftingMachineType.CHEESE_PRESS ),

    KegRecipe("Keg Recipe", Map.of(
            MaterialType.Wood, 30,
            MaterialType.CopperBar, 1,
            MaterialType.IronBar, 1
    ), 0 , CraftingMachineType.KEG),

    LoomRecipe("Loom Recipe", Map.of(
            MaterialType.Wood, 60,
            MaterialType.Fiber, 30
    ), 0, CraftingMachineType.LOOM ),

    MayonnaiseMachineRecipe("Mayonnaise Machine Recipe", Map.of(
            MaterialType.Wood, 15,
            MaterialType.Stone, 15,
            MaterialType.CopperBar, 1
    ), 0, CraftingMachineType.MAYONNAISE_MACHINE),

    OilMakerRecipe("Oil Maker Recipe", Map.of(
            MaterialType.Wood, 100,
            MaterialType.GoldBar, 1,
            MaterialType.IronBar, 1
    ), 0, CraftingMachineType.OIL_MAKER),

    PreservesJarRecipe("Preserves Jar Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.Stone, 40,
            MaterialType.Coal, 8
    ), 0, CraftingMachineType.PRESERVES_JAR),

    DehydratorRecipe("Dehydrator Recipe", Map.of(
            MaterialType.Wood, 30,
            MaterialType.Stone, 20,
            MaterialType.Fiber, 30
    ), 0, CraftingMachineType.DEHYDRATOR),

    FishSmokerRecipe("Fish Smoker Recipe", Map.of(
            MaterialType.Wood, 50,
            MaterialType.IronBar, 3,
            MaterialType.Coal, 10
    ), 0, CraftingMachineType.FISH_SMOKER),

    MysticTreeSeedRecipe("Mystic Tree Seed Recipe", Map.of(
            ForagingSeedType.Acorn, 5,
            ForagingSeedType.MapleSeed, 5,
            ForagingSeedType.PineCone, 5,
            ForagingSeedType.MahoganySeed, 5
    ), 100, ForagingSeedType.MysticTreeSeed),

    GrassStarterRecipe("Grass Starter Recipe", Map.of(
            MaterialType.Wood, 1,
            MaterialType.Fiber, 1
    ), 0, CraftingMachineType.GRASS_STARTER);


    private final String displayName;
    private final Map<ItemType, Integer> ingredients;
    private final int price;
    private final ItemType product;

    CraftingRecipes(String displayName, Map<ItemType, Integer> ingredients, int price, ItemType product) {
        this.displayName = displayName;
        this.ingredients = new HashMap<>(ingredients);
        this.price = price;
        this.product = product;
    }

    public Map<ItemType, Integer> getIngredients() {
        return this.ingredients;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.displayName;
    }

    public ItemType getProduct() {
        return product;
    }
}
