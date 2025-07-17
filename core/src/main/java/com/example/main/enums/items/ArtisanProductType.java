package com.example.main.enums.items;

import java.util.Map;

public enum ArtisanProductType implements ItemType {
    Cloth(CraftingMachineType.LOOM,"cloth", "A bolt of fine wool cloth.", 0,4,Map.of(AnimalProductType.Wool, 1),470),
    HONEY(CraftingMachineType.BEE_HOUSE,"honey", "It's a sweet syrup produced by bees.", 75, 52, null,350),
    CHEESE(CraftingMachineType.CHEESE_PRESS, "cheese", "It's your basic cheese.", 100, 3, Map.of(AnimalProductType.Milk, 1), 230),
    BIG_CHEESE(CraftingMachineType.CHEESE_PRESS ,"big cheese", "It's your basic cheese.", 100, 3, Map.of(AnimalProductType.Large_Milk, 1), 345),

    GOAT_CHEESE(CraftingMachineType.CHEESE_PRESS ,"goat cheese", "Soft cheese made from goat's milk.", 100, 3, Map.of(AnimalProductType.Goat_Milk, 1), 400),
    BIG_GOAT_CHEESE(CraftingMachineType.CHEESE_PRESS ,"big goat Cheese", "Soft cheese made from goat's milk.", 100, 3, Map.of(AnimalProductType.Large_Goat_Milk, 1), 600),

    // Mayonnaise
    MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"mayonnaise", "It looks spreadable.", 50, 3, Map.of(AnimalProductType.Egg, 1), 190),
    BIG_MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"big mayonnaise", "It looks spreadable.", 50,3, Map.of(AnimalProductType.Large_Egg, 1), 237),
    DUCK_MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"duck Mayonnaise", "It's a rich, yellow mayonnaise.", 75, 3, Map.of(AnimalProductType.Duck_Egg, 1), 375),
    DINOSAUR_MAYONNAISE(CraftingMachineType.MAYONNAISE_MACHINE,"dinosaur Mayonnaise", "It's thick and creamy, with a vivid green hue. It smells like grass and leather.", 125, 3, Map.of(AnimalProductType.Dinosaur_Egg, 1), 800),

    // Beverages
    BEER(CraftingMachineType.KEG,"Beer", "Drink in moderation.", 50, 24, Map.of(MaterialType.Wheat, 1), 200),
    VINEGAR(CraftingMachineType.KEG,"Vinegar", "An aged fermented liquid used in many cooking recipes.", 13, 10, Map.of(MaterialType.Rice,1), 100),
    COFFEE(CraftingMachineType.KEG,"Coffee", "It smells delicious. This is sure to give you a boost.", 75, 2, Map.of(ForagingSeedType.Coffee_Bean, 5), 150),
    CARROT_JUICE(CraftingMachineType.KEG,"carrot juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80), // Uses multiplier

    BLUE_JAZZ_JUICE(CraftingMachineType.KEG, "blue jazz juice",
            "A floral-infused beverage.", -1, 96,
            Map.of(CropType.BlueJazz, 1), 112),

    CAULIFLOWER_JUICE(CraftingMachineType.KEG, "cauliflower juice",
            "A hearty vegetable drink.", -1, 96,
            Map.of(CropType.Cauliflower, 1), 393),

    GARLIC_JUICE(CraftingMachineType.KEG, "garlic juice",
            "A pungent health tonic.", -1, 96,
            Map.of(CropType.Garlic, 1), 135),

    KALE_JUICE(CraftingMachineType.KEG, "kale juice",
            "A nutrient-packed green drink.", -1, 96,
            Map.of(CropType.Kale, 1), 247),

    PARSNIP_JUICE(CraftingMachineType.KEG, "parsnip juice",
            "An earthy root vegetable beverage.", -1, 96,
            Map.of(CropType.Parsnip, 1), 80),

    POTATO_JUICE(CraftingMachineType.KEG, "potato juice",
            "A starchy, filling drink.", -1, 96,
            Map.of(CropType.Potato, 1), 180),

    RHUBARB_JUICE(CraftingMachineType.KEG, "rhubarb juice",
            "A tart, pink-colored juice.", -1, 96,
            Map.of(CropType.Rhubarb, 1), 495),

    BEET_JUICE(CraftingMachineType.KEG, "beet juice",
            "A vibrant red juice packed with nutrients.", -1, 96,
            Map.of(CropType.Beet, 1), 225),

    BOK_CHOY_JUICE(CraftingMachineType.KEG, "bok choy juice",
            "A light, Asian-inspired vegetable drink.", -1, 96,
            Map.of(CropType.BokChoy, 1), 180),

    PUMPKIN_JUICE(CraftingMachineType.KEG, "pumpkin juice",
            "A seasonal fall favorite.", -1, 96,
            Map.of(CropType.Pumpkin, 1), 720),

    YAM_JUICE(CraftingMachineType.KEG, "yam juice",
            "A sweet, orange-colored beverage.", -1, 96,
            Map.of(CropType.Yam, 1), 360),

    // Special cases
    ANCIENT_FRUIT_JUICE(CraftingMachineType.KEG, "ancient fruit juice",
            "A mysterious, energizing elixir.", -1, 96,
            Map.of(CropType.AncientFruit, 1), 1237),

    SWEET_GEM_BERRY_JUICE(CraftingMachineType.KEG, "sweet gem berry juice",
            "An incredibly rare and valuable drink.", -1, 96,
            Map.of(CropType.SweetGemBerry, 1), 6750),
    MEAD(CraftingMachineType.KEG,"Mead", "A fermented beverage made from honey. Drink in moderation.", 100, 10, Map.of(ArtisanProductType.HONEY, 1), 300),
    PALE_ALE(CraftingMachineType.KEG,"Pale Ale", "Drink in moderation.", 50, 72, Map.of(CropType.Hops,1), 300),
    GRAPE_WINE(CraftingMachineType.KEG,"grape wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240), // Uses multiplier

    // Oils
    TRUFFLE_OIL(CraftingMachineType.OIL_MAKER,"truffle Oil", "A gourmet cooking ingredient.", 38, 6, Map.of(AnimalProductType.Truffle, 1), 1065),
    SUNFLOWER_OIL(CraftingMachineType.OIL_MAKER,"sunflower oil", "All purpose cooking oil.", 13, -1, Map.of(CropType.Sunflower, 1), 100), // Variable processing time
    CORN_OIL(CraftingMachineType.OIL_MAKER,"corn oil", "All purpose cooking oil.", 13, -1, Map.of(CropType.Corn, 1), 100), // Variable processing time
    SUNFLOWER_SEED_OIL(CraftingMachineType.OIL_MAKER,"sunflower seed oil", "All purpose cooking oil.", 13, -1, Map.of(ForagingSeedType.Sunflower_Seeds, 1), 100), // Variable processing time

    // Preserves
    PICKLES(CraftingMachineType.PRESERVES_JAR,"pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190), // Uses multiplier
    JELLY(CraftingMachineType.PRESERVES_JAR,"jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210), // Uses multiplier

    // Dried Goods
    DRIED_MUSHROOMS(CraftingMachineType.DEHYDRATOR,"dried Mushrooms", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.Common_Mushroom, 5), 325), // Uses multiplier
    DRIED_FRUIT(CraftingMachineType.DEHYDRATOR,"dried Fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775), // Uses multiplier
    RAISINS(CraftingMachineType.DEHYDRATOR,"raisins", "It's said to be the Junimos' favorite food.", 125, -1, Map.of(CropType.Grape, 5), 600),

    // Smoked/Processed
    SMOKED_FISH(CraftingMachineType.FISH_SMOKER,"smoked fish", "A whole fish, smoked to perfection.", -1, 1, Map.of(FishType.Salmon, 1), 150), // Uses multiplier
    SMOKED_SALMON(CraftingMachineType.FISH_SMOKER, "smoked salmon",
            "Delicately smoked salmon with rich flavor.", -1, 1,
            Map.of(FishType.Salmon, 1), 150),

    SMOKED_SARDINE(CraftingMachineType.FISH_SMOKER, "smoked sardine",
            "Small but flavorful smoked fish.", -1, 1,
            Map.of(FishType.Sardine, 1), 80),

    SMOKED_SHAD(CraftingMachineType.FISH_SMOKER, "smoked shad",
            "A traditionally smoked river fish.", -1, 1,
            Map.of(FishType.Shad, 1), 120),

    SMOKED_BLUE_DISCUS(CraftingMachineType.FISH_SMOKER, "smoked blue discus",
            "Exotic smoked tropical fish.", -1, 1,
            Map.of(FishType.Blue_Discus, 1), 240),

    SMOKED_MIDNIGHT_CARP(CraftingMachineType.FISH_SMOKER, "smoked midnight carp",
            "Dark-fleshed smoked fish with unique aroma.", -1, 1,
            Map.of(FishType.Midnight_Carp, 1), 300),

    SMOKED_SQUID(CraftingMachineType.FISH_SMOKER, "smoked squid",
            "Tender smoked cephalopod.", -1, 1,
            Map.of(FishType.Squid, 1), 160),

    SMOKED_TUNA(CraftingMachineType.FISH_SMOKER, "smoked tuna",
            "Meaty smoked tuna steaks.", -1, 1,
            Map.of(FishType.Tuna, 1), 200),

    SMOKED_PERCH(CraftingMachineType.FISH_SMOKER, "smoked perch",
            "Mild smoked freshwater fish.", -1, 1,
            Map.of(FishType.Perch, 1), 110),

    SMOKED_FLOUNDER(CraftingMachineType.FISH_SMOKER, "smoked flounder",
            "Delicate smoked flatfish.", -1, 1,
            Map.of(FishType.Flounder, 1), 200),

    SMOKED_LIONFISH(CraftingMachineType.FISH_SMOKER, "smoked lionfish",
            "Spicy-smoked exotic fish.", -1, 1,
            Map.of(FishType.Lionfish, 1), 200),

    SMOKED_HERRING(CraftingMachineType.FISH_SMOKER, "smoked herring",
            "Classic northern European smoked fish.", -1, 1,
            Map.of(FishType.Herring, 1), 60),

    SMOKED_GHOST_FISH(CraftingMachineType.FISH_SMOKER, "smoked ghost fish",
            "Eerily translucent smoked fish.", -1, 1,
            Map.of(FishType.Ghostfish, 1), 90),

    SMOKED_TILAPIA(CraftingMachineType.FISH_SMOKER, "smoked tilapia",
            "Mild and versatile smoked fish.", -1, 1,
            Map.of(FishType.Tilapia, 1), 150),

    SMOKED_DORADO(CraftingMachineType.FISH_SMOKER, "smoked dorado",
            "Premium smoked tropical fish.", -1, 1,
            Map.of(FishType.Dorado, 1), 200),

    SMOKED_SUNFISH(CraftingMachineType.FISH_SMOKER, "smoked sunfish",
            "Small but tasty smoked panfish.", -1, 1,
            Map.of(FishType.Sunfish, 1), 60),

    SMOKED_RAINBOW_TROUT(CraftingMachineType.FISH_SMOKER, "smoked rainbow trout",
            "Beautifully colored smoked fish.", -1, 1,
            Map.of(FishType.Rainbow_Trout, 1), 130),

    // Legendary fish variants
    SMOKED_LEGEND(CraftingMachineType.FISH_SMOKER, "smoked legend",
            "The rarest smoked fish in the valley.", -1, 1,
            Map.of(FishType.Legend, 1), 10000),

    SMOKED_GLACIERFISH(CraftingMachineType.FISH_SMOKER, "smoked glacierfish",
            "Icy-cold smoked legendary fish.", -1, 1,
            Map.of(FishType.Glacierfish, 1), 2000),

    SMOKED_ANGLER(CraftingMachineType.FISH_SMOKER, "smoked angler",
            "Odd-looking but delicious smoked fish.", -1, 1,
            Map.of(FishType.Angler, 1), 1800),

    SMOKED_CRIMSONFISH(CraftingMachineType.FISH_SMOKER, "smoked crimsonfish",
            "Vibrant red smoked legendary fish.", -1, 1,
            Map.of(FishType.Crimsonfish, 1), 3000),
    // Fuel/Refined
    COAL(CraftingMachineType.CHARCOAL_KILN,"coal", "Turns 10 pieces of wood into one piece of coal.", -1, 1, Map.of(MaterialType.Wood, 1), 50),

    COPPER_BAR(CraftingMachineType.FURNACE,"copper bar",
            "A bar of smelted copper. Useful for crafting.",
            -1,
            4,
            Map.of(MineralType.Copper, 5),
            60),

    IRON_BAR(CraftingMachineType.FURNACE,"iron bar",
            "A bar of smelted iron. Useful for crafting.",
            -1,
            4,
            Map.of(MineralType.Iron, 5),
            120),

    GOLD_BAR(CraftingMachineType.FURNACE,"gold bar",
            "A bar of smelted gold. Valuable and useful for crafting.",
            -1,
            4,
            Map.of(MineralType.Gold, 5),
            250),

    IRIDIUM_BAR(CraftingMachineType.FURNACE,"iridium bar",
            "A bar of smelted iridium. Extremely valuable with special properties.",
            -1,
            4,
            Map.of(MineralType.Iridium, 5),
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

    @Override
    public String getEnumName() {
        return name();
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
