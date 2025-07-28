package com.example.main.enums.items;


import java.util.HashMap;
import java.util.Map;

public enum CraftingRecipes implements ItemType {
    // Updated to point to the corrected CraftingMachineType enums
    CherryBombRecipe("Cherry Bomb Recipe", Map.of(
        MineralType.Copper_Ore, 4,
        MineralType.Coal, 1
    ), 50, CraftingMachineType.Cherry_Bomb),

    BombRecipe("Bomb Recipe", Map.of(
        MineralType.Iron_Ore, 4,
        MineralType.Coal, 1
    ), 50, CraftingMachineType.Bomb ),

    MegaBombRecipe("Mega Bomb Recipe", Map.of(
        MineralType.Gold_Ore, 4,
        MineralType.Coal, 1
    ), 50 , CraftingMachineType.Mega_Bomb),

    SprinklerRecipe("Sprinkler Recipe", Map.of (
        MineralType.Copper_Bar, 1,
        MineralType.Iron_Bar, 1
    ), 0 , CraftingMachineType.Sprinkler),

    QualitySprinklerRecipe("Quality Sprinkler Recipe", Map.of(
        MineralType.Iron_Bar, 1,
        MineralType.Gold_Bar, 1
    ), 0 , CraftingMachineType.Quality_Sprinkler),

    IridiumSprinklerRecipe("Iridium Sprinkler Recipe", Map.of(
        MineralType.Gold_Bar, 1,
        MineralType.Iridium_Bar, 1
    ), 0 , CraftingMachineType.Iridium_Sprinkler),

    CharcoalKilnRecipe("Charcoal Kiln Recipe", Map.of(
        MaterialType.Wood, 20,
        MineralType.Copper_Bar, 2
    ), 0 , CraftingMachineType.Charcoal_Kiln),

    FurnaceRecipe("Furnace Recipe", Map.of(
        MineralType.Copper_Ore, 20,
        MaterialType.Stone, 25
    ), 0, CraftingMachineType.Furnace),

    ScarecrowRecipe("Scarecrow Recipe", Map.of(
        MaterialType.Wood, 50,
        MineralType.Coal, 1,
        MaterialType.Fiber, 20
    ), 0, CraftingMachineType.Scarecrow),

    DeluxeScarecrowRecipe("Deluxe Scarecrow Recipe", Map.of(
        MaterialType.Wood, 50,
        MineralType.Coal, 1,
        MaterialType.Fiber, 20,
        MineralType.Iridium_Ore, 1
    ), 0 , CraftingMachineType.Deluxe_Scarecrow),

    BeeHouseRecipe("Bee House Recipe", Map.of(
        MaterialType.Wood, 40,
        MineralType.Coal, 8,
        MineralType.Iron_Bar, 1
    ), 0 , CraftingMachineType.Bee_House),

    CheesePressRecipe("Cheese Press Recipe", Map.of(
        MaterialType.Wood, 45,
        MaterialType.Stone, 45,
        MineralType.Copper_Bar, 1
    ), 0, CraftingMachineType.Cheese_Press ),

    KegRecipe("Keg Recipe", Map.of(
        MaterialType.Wood, 30,
        MineralType.Copper_Bar, 1,
        MineralType.Iron_Bar, 1
    ), 0 , CraftingMachineType.Keg),

    LoomRecipe("Loom Recipe", Map.of(
        MaterialType.Wood, 60,
        MaterialType.Fiber, 30
    ), 0, CraftingMachineType.Loom ),

    MayonnaiseMachineRecipe("Mayonnaise Machine Recipe", Map.of(
        MaterialType.Wood, 15,
        MaterialType.Stone, 15,
        MineralType.Copper_Bar, 1
    ), 0, CraftingMachineType.Mayonnaise_Machine),

    OilMakerRecipe("Oil Maker Recipe", Map.of(
        MaterialType.Wood, 100,
        MineralType.Gold_Bar, 1,
        MineralType.Iron_Bar, 1
    ), 0, CraftingMachineType.Oil_Maker),

    PreservesJarRecipe("Preserves Jar Recipe", Map.of(
        MaterialType.Wood, 50,
        MaterialType.Stone, 40,
        MineralType.Coal, 8
    ), 0, CraftingMachineType.Preserves_Jar),

    DehydratorRecipe("Dehydrator Recipe", Map.of(
        MaterialType.Wood, 30,
        MaterialType.Stone, 20,
        MaterialType.Fiber, 30
    ), 0, CraftingMachineType.Dehydrator),

    FishSmokerRecipe("Fish Smoker Recipe", Map.of(
        MaterialType.Wood, 50,
        MineralType.Iron_Bar, 3,
        MineralType.Coal, 10
    ), 0, CraftingMachineType.Fish_Smoker),

    MysticTreeSeedRecipe("Mystic Tree Seed Recipe", Map.of(
        ForagingSeedType.Acorn, 5,
        ForagingSeedType.Maple_Seed, 5,
        ForagingSeedType.Pine_Cone, 5,
        ForagingSeedType.Mahogany_Seed, 5
    ), 100, CraftingMachineType.Mystic_Tree_Seed),

    GrassStarterRecipe("Grass Starter Recipe", Map.of(
        MaterialType.Wood, 1,
        MaterialType.Fiber, 1
    ), 0, CraftingMachineType.Grass_Starter);

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

    @Override
    public String getEnumName() {
        return name();
    }

    public ItemType getProduct() {
        return product;
    }
}
