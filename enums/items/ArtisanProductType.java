package enums.items;

import java.util.Map;

public enum ArtisanProductType implements ItemType {
    Cloth(CraftingMachineType.LOOM,"cloth", "A bolt of fine wool cloth.", 0,4,Map.of(AnimalProductType.SheepWool, 1),470),
    HONEY(CraftingMachineType.BEE_HOUSE,"honey", "It's a sweet syrup produced by bees.", 75, 52, null,350),
    CHEESE(CraftingMachineType.CHEESE_PRESS, "cheese", "It's your basic cheese.", 100, 3, Map.of(AnimalProductType.NormalCowMilk, 1), 230),
    BIG_CHEESE(CraftingMachineType.CHEESE_PRESS ,"big cheese", "It's your basic cheese.", 100, 3, Map.of(AnimalProductType.LargeCowMilk, 1), 345),

    GOAT_CHEESE(CraftingMachineType.CHEESE_PRESS ,"goat cheese", "Soft cheese made from goat's milk.", 100, 3, Map.of(AnimalProductType.NormalGoatMilk, 1), 400),
    BIG_GOAT_CHEESE(CraftingMachineType.CHEESE_PRESS ,"big goat Cheese", "Soft cheese made from goat's milk.", 100, 3, Map.of(AnimalProductType.LargeGoatMilk, 1), 600),

    // Mayonnaise
    MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"mayonnaise", "It looks spreadable.", 50, 3, Map.of(AnimalProductType.ChickenEgg, 1), 190),
    BIG_MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"big mayonnaise", "It looks spreadable.", 50,3, Map.of(AnimalProductType.BigChickenEgg, 1), 237),
    DUCK_MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"duck Mayonnaise", "It's a rich, yellow mayonnaise.", 75, 3, Map.of(AnimalProductType.DuckEgg, 1), 375),
    DINOSAUR_MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"dinosaur Mayonnaise", "It's thick and creamy, with a vivid green hue. It smells like grass and leather.", 125, 3, Map.of(AnimalProductType.DinosaurEgg, 1), 800),

    // Beverages
    BEER(CraftingMachineType.KEG,"Beer", "Drink in moderation.", 50, 24, Map.of(MaterialType.Wheat, 1), 200),
    VINEGAR(CraftingMachineType.KEG,"Vinegar", "An aged fermented liquid used in many cooking recipes.", 13, 10, Map.of(MaterialType.Rice,1), 100),
    COFFEE(CraftingMachineType.KEG,"Coffee", "It smells delicious. This is sure to give you a boost.", 75, 2, Map.of(ForagingSeedType.CoffeeBean, 5), 150),
    CARROT_JUICE(CraftingMachineType.KEG,"carrot juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80), // Uses multiplier
    // todo : add more juice
    MEAD(CraftingMachineType.KEG,"Mead", "A fermented beverage made from honey. Drink in moderation.", 100, 10, Map.of(ArtisanProductType.HONEY, 1), 300),
    PALE_ALE(CraftingMachineType.KEG,"Pale Ale", "Drink in moderation.", 50, 72, Map.of(CropType.Hops,1), 300),
    GRAPE_WINE(CraftingMachineType.KEG,"grape wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240), // Uses multiplier

    // Oils
    TRUFFLE_OIL(CraftingMachineType.OIL_MAKER,"truffle Oil", "A gourmet cooking ingredient.", 38, 6, Map.of(AnimalProductType.Truffle, 1), 1065),
    SUNFLOWER_OIL(CraftingMachineType.OIL_MAKER,"sunflower oil", "All purpose cooking oil.", 13, -1, Map.of(CropType.Sunflower, 1), 100), // Variable processing time
    CORN_OIL(CraftingMachineType.OIL_MAKER,"corn oil", "All purpose cooking oil.", 13, -1, Map.of(CropType.Corn, 1), 100), // Variable processing time
    SUNFLOWER_SEED_OIL(CraftingMachineType.OIL_MAKER,"sunflower seed oil", "All purpose cooking oil.", 13, -1, Map.of(ForagingSeedType.SunflowerSeeds, 1), 100), // Variable processing time

    // Preserves
    PICKLES(CraftingMachineType.PRESERVES_JAR,"pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190), // Uses multiplier
    JELLY(CraftingMachineType.PRESERVES_JAR,"jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210), // Uses multiplier

    // Dried Goods
    DRIED_MUSHROOMS(CraftingMachineType.DEHYDRATOR,"dried Mushrooms", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.CommonMushroom, 5), 325), // Uses multiplier
    DRIED_FRUIT(CraftingMachineType.DEHYDRATOR,"dried Fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775), // Uses multiplier
    RAISINS(CraftingMachineType.DEHYDRATOR,"raisins", "It's said to be the Junimos' favorite food.", 125, -1, Map.of(CropType.Grape, 5), 600),

    // Smoked/Processed
    SMOKED_FISH(CraftingMachineType.FISH_SMOKER,"smoked fish", "A whole fish, smoked to perfection.", -1, 1, Map.of(FishType.Salmon, 1), 150), // Uses multiplier

    // Fuel/Refined
    COAL(CraftingMachineType.CHARCOAL_KILN,"coal", "Turns 10 pieces of wood into one piece of coal.", -1, 1, Map.of(MaterialType.Wood, 1), 50),

    COPPER_BAR(CraftingMachineType.FURNACE,"copper bar",
            "A bar of smelted copper. Useful for crafting.",
            -1,
            4,
            Map.of(MineralType.COPPER, 5),
            60),

    IRON_BAR(CraftingMachineType.FURNACE,"iron bar",
            "A bar of smelted iron. Useful for crafting.",
            -1,
            4,
            Map.of(MineralType.IRON, 5),
            120),

    GOLD_BAR(CraftingMachineType.FURNACE,"gold bar",
            "A bar of smelted gold. Valuable and useful for crafting.",
            -1,
            4,
            Map.of(MineralType.GOLD, 5),
            250),

    IRIDIUM_BAR(CraftingMachineType.FURNACE,"iridium bar",
            "A bar of smelted iridium. Extremely valuable with special properties.",
            -1,
            4,
            Map.of(MineralType.IRIDIUM, 5),
            1000);

    private final CraftingMachineType machine;
    private final String name;
    private final String description;
    private final Integer energy;
    private final int processingTime;
    private final Map<ItemType, Integer> ingredients;
    private final int sellPrice;

    ArtisanProductType(CraftingMachineType machine, String name, String description, Integer energy, int processingTime, Map<ItemType, Integer> ingredients, int sellPrice) {
        this.machine = machine;
        this.name = name;
        this.description = description;
        this.energy = energy;
        this.processingTime = processingTime;
        this.ingredients = ingredients;
        this.sellPrice = sellPrice;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
    public Integer getEnergy() {
        return this.energy;
    }
    public int getProcessingTime() {
        return this.processingTime;
    }
    public Map<ItemType, Integer> getIngredients() {
        return this.ingredients;
    }
    public int getSellPrice() {
        return this.sellPrice;
    }
    public CraftingMachineType getMachine() {
        return this.machine;
    }
}
