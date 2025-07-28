package com.example.main.enums.items;

import java.util.Map;

public enum ArtisanProductType implements ItemType {
    Beer(CraftingMachineType.Keg,"beer", "Drink in moderation.", 50, 24, Map.of(CropType.Wheat, 1), 200),
    Blue_Dried_Fruit(CraftingMachineType.Dehydrator,"blue dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Blue_Jelly(CraftingMachineType.Preserves_Jar,"blue jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Blue_Wine(CraftingMachineType.Keg,"blue wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Brown_Dried_Fruit(CraftingMachineType.Dehydrator,"brown dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Brown_Jelly(CraftingMachineType.Preserves_Jar,"brown jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Brown_Juice(CraftingMachineType.Keg, "brown juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Brown_Pickles(CraftingMachineType.Preserves_Jar,"brown pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Brown_Wine(CraftingMachineType.Keg,"brown wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Caviar(CraftingMachineType.Preserves_Jar, "caviar", "The cured roe of a sturgeon. Considered a delicacy.", 500, 100, Map.of(FishType.Sturgeon, 1), 500),
    Cheese(CraftingMachineType.Cheese_Press, "cheese", "It's your basic cheese.", 100, 3, Map.of(AnimalProductType.Milk, 1), 230),
    Cloth(CraftingMachineType.Loom,"cloth", "A bolt of fine wool cloth.", 0,4,Map.of(AnimalProductType.Wool, 1),470),
    Coffee(CraftingMachineType.Keg,"Coffee", "It smells delicious. This is sure to give you a boost.", 75, 2, Map.of(ForagingSeedType.Coffee_Bean, 5), 150),
    Dark_Blue_Dried_Fruit(CraftingMachineType.Dehydrator,"dark blue dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Dark_Blue_Jelly(CraftingMachineType.Preserves_Jar,"dark blue jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Dark_Blue_Wine(CraftingMachineType.Keg,"dark blue wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Dark_Pink_Dried_Fruit(CraftingMachineType.Dehydrator,"dark pink dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Dark_Pink_Jelly(CraftingMachineType.Preserves_Jar,"dark pink jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Dark_Pink_Pickles(CraftingMachineType.Preserves_Jar,"dark pink pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Dark_Pink_Wine(CraftingMachineType.Keg,"dark pink wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Dark_Purple_Jelly(CraftingMachineType.Preserves_Jar,"dark purple jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Dark_Purple_Juice(CraftingMachineType.Keg, "dark purple juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Dark_Purple_Pickles(CraftingMachineType.Preserves_Jar,"dark purple pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Dark_Purple_Wine(CraftingMachineType.Keg,"dark purple wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Dinosaur_Mayonnaise(CraftingMachineType.Mayonnaise_Machine,"dinosaur Mayonnaise", "It's thick and creamy, with a vivid green hue. It smells like grass and leather.", 125, 3, Map.of(AnimalProductType.Dinosaur_Egg, 1), 800),
    Dried_Chanterelles(CraftingMachineType.Dehydrator, "dried chanterelles", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.Chanterelle, 5), 325),
    Dried_Common_Mushrooms(CraftingMachineType.Dehydrator,"dried Common Mushrooms", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.Common_Mushroom, 5), 325),
    Dried_Fruit(CraftingMachineType.Dehydrator,"dried Fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Dried_Magma_Caps(CraftingMachineType.Dehydrator, "dried magma caps", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.Red_Mushroom, 5), 325),
    Dried_Morels(CraftingMachineType.Dehydrator, "dried morels", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.Morel, 5), 325),
    Dried_Mushrooms(CraftingMachineType.Dehydrator,"dried Mushrooms", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.Common_Mushroom, 5), 325),
    Dried_Purple_Mushrooms(CraftingMachineType.Dehydrator, "dried purple mushrooms", "A package of gourmet mushrooms.", 50, -1, Map.of(ForagingCropType.Purple_Mushroom, 5), 325),
    Duck_Mayonnaise(CraftingMachineType.Mayonnaise_Machine,"duck Mayonnaise", "It's a rich, yellow mayonnaise.", 75, 3, Map.of(AnimalProductType.Duck_Egg, 1), 375),
    Goat_Cheese(CraftingMachineType.Cheese_Press ,"goat cheese", "Soft cheese made from goat's milk.", 100, 3, Map.of(AnimalProductType.Goat_Milk, 1), 400),
    Green_Dried_Fruit(CraftingMachineType.Dehydrator,"green dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Green_Jelly(CraftingMachineType.Preserves_Jar,"green jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Green_Juice(CraftingMachineType.Keg, "green juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Green_Pickles(CraftingMachineType.Preserves_Jar,"green pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Green_Wine(CraftingMachineType.Keg,"green wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Honey(CraftingMachineType.Bee_House,"honey", "It's a sweet syrup produced by bees.", 75, 52, null,350),
    Jelly(CraftingMachineType.Preserves_Jar,"jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Juice(CraftingMachineType.Keg, "juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Light_Blue_Dried_Fruit(CraftingMachineType.Dehydrator,"light blue dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Light_Blue_Jelly(CraftingMachineType.Preserves_Jar,"light blue jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Light_Blue_Wine(CraftingMachineType.Keg,"light blue wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Maple_Syrup(CraftingMachineType.Keg,"maple syrup", "A sweet syrup with a unique flavor.", 200, 9, Map.of(TreeType.Maple, 1), 200),
    Mayonnaise(CraftingMachineType.Mayonnaise_Machine,"mayonnaise", "It looks spreadable.", 50, 3, Map.of(AnimalProductType.Egg, 1), 190),
    Mead(CraftingMachineType.Keg,"Mead", "A fermented beverage made from honey. Drink in moderation.", 100, 10, Map.of(ArtisanProductType.Honey, 1), 300),
    Mystic_Syrup(CraftingMachineType.Keg, "mystic syrup", "A mysterious, energizing elixir.", 500, 7, Map.of(TreeType.Mystic, 1), 1000),
    Oak_Resin(CraftingMachineType.Keg,"oak resin", "A sticky, fragrant substance from oak trees.", 150, 7, Map.of(TreeType.Oak, 1), 150),
    Orange_Dried_Fruit(CraftingMachineType.Dehydrator,"orange dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Orange_Jelly(CraftingMachineType.Preserves_Jar,"orange jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Orange_Juice(CraftingMachineType.Keg, "orange juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Orange_Pickles(CraftingMachineType.Preserves_Jar,"orange pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Orange_Wine(CraftingMachineType.Keg,"orange wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Pale_Ale(CraftingMachineType.Keg,"Pale Ale", "Drink in moderation.", 50, 72, Map.of(CropType.Hops,1), 300),
    Pickles(CraftingMachineType.Preserves_Jar,"pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Pine_Tar(CraftingMachineType.Keg, "pine tar", "A smelly, sticky substance from pine trees.", 100, 5, Map.of(TreeType.Pine, 1), 100),
    Pink_Dried_Fruit(CraftingMachineType.Dehydrator,"pink dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Pink_Jelly(CraftingMachineType.Preserves_Jar,"pink jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Pink_Juice(CraftingMachineType.Keg, "pink juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Pink_Pickles(CraftingMachineType.Preserves_Jar,"pink pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Pink_Wine(CraftingMachineType.Keg,"pink wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Purple_Dried_Fruit(CraftingMachineType.Dehydrator,"purple dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Purple_Jelly(CraftingMachineType.Preserves_Jar,"purple jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Purple_Juice(CraftingMachineType.Keg, "purple juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Purple_Pickles(CraftingMachineType.Preserves_Jar,"purple pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Purple_Wine(CraftingMachineType.Keg,"purple wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Raisins(CraftingMachineType.Dehydrator,"raisins", "It's said to be the Junimos' favorite food.", 125, -1, Map.of(CropType.Grape, 5), 600),
    Red_Dried_Fruit(CraftingMachineType.Dehydrator,"red dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Red_Jelly(CraftingMachineType.Preserves_Jar,"red jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Red_Juice(CraftingMachineType.Keg, "red juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Red_Pickles(CraftingMachineType.Preserves_Jar,"red pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Red_Wine(CraftingMachineType.Keg,"red wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Truffle_Oil(CraftingMachineType.Oil_Maker,"truffle Oil", "A gourmet cooking ingredient.", 38, 6, Map.of(AnimalProductType.Truffle, 1), 1065),
    Void_Mayonnaise(CraftingMachineType.Mayonnaise_Machine, "void mayonnaise", "A thick, black paste that smells like burnt hair.", 50, 3, Map.of(AnimalProductType.Void_Egg, 1), 275),
    White_Dried_Fruit(CraftingMachineType.Dehydrator,"white dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    White_Jelly(CraftingMachineType.Preserves_Jar,"white jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    White_Juice(CraftingMachineType.Keg, "white juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    White_Pickles(CraftingMachineType.Preserves_Jar,"white pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    White_Wine(CraftingMachineType.Keg,"white wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Wine(CraftingMachineType.Keg,"wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    Yellow_Dried_Fruit(CraftingMachineType.Dehydrator,"yellow dried fruit", "Chewy pieces of dried fruit.", 75, -1, Map.of(FruitType.Apple, 5), 775),
    Yellow_Jelly(CraftingMachineType.Preserves_Jar,"yellow jelly", "Gooey.", -1, 72, Map.of(FruitType.Cherry, 1), 210),
    Yellow_Juice(CraftingMachineType.Keg, "yellow juice", "A sweet, nutritious beverage.", -1, 96, Map.of(CropType.Carrot, 1), 80),
    Yellow_Pickles(CraftingMachineType.Preserves_Jar,"yellow pickles", "A jar of your home-made pickles.", -1, 6, Map.of(CropType.Broccoli, 1), 190),
    Yellow_Wine(CraftingMachineType.Keg,"yellow wine", "Drink in moderation.", -1, 168, Map.of(CropType.Grape, 1), 240),
    VINEGAR(CraftingMachineType.Keg,"Vinegar", "An aged fermented liquid used in many cooking recipes.", 13, 10, Map.of(MaterialType.Rice,1), 100);

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
        return this.processingTime * 3;
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
