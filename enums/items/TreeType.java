package enums.items;

import enums.design.Season;

import java.util.List;

public enum TreeType implements Growable {
    APRICOT_TREE("Apricot Sapling", List.of(7, 7, 7, 7), 28, FruitType.Apricot, 1, 59, true, 38, 17, List.of(Season.Spring)),
    CHERRY_TREE("Cherry Sapling", List.of(7, 7, 7, 7), 28, FruitType.Cherry, 1, 80, true, 38, 17, List.of(Season.Spring)),
    BANANA_TREE("Banana Sapling", List.of(7, 7, 7, 7), 28, FruitType.Banana, 1, 150, true, 75, 33, List.of(Season.Summer)),
    MANGO_TREE("Mango Sapling", List.of(7, 7, 7, 7), 28, FruitType.Mango, 1, 130, true, 100, 45, List.of(Season.Summer)),
    ORANGE_TREE("Orange Sapling", List.of(7, 7, 7, 7), 28, FruitType.Orange, 1, 100, true, 38, 17, List.of(Season.Summer)),
    PEACH_TREE("Peach Sapling", List.of(7, 7, 7, 7), 28, FruitType.Peach, 1, 140, true, 38, 17, List.of(Season.Summer)),
    APPLE_TREE("Apple Sapling", List.of(7, 7, 7, 7), 28, FruitType.Apple, 1, 100, true, 38, 17, List.of(Season.Fall)),
    POMEGRANATE_TREE("Pomegranate Sapling", List.of(7, 7, 7, 7), 28, FruitType.Pomegranate, 1, 140, true, 38, 17, List.of(Season.Fall)),
    OAK_TREE("Acorns", List.of(7, 7, 7, 7), 28, FruitType.OakResin, 7, 150, false, -1, -1, List.of(Season.Special)),
    MAPLE_TREE("Maple Seeds", List.of(7, 7, 7, 7), 28, FruitType.MapleSyrup, 9, 200, false, -1, -1, List.of(Season.Special)),
    PINE_TREE("Pine Cones", List.of(7, 7, 7, 7), 28, FruitType.PineTar, 5, 100, false, -1, -1, List.of(Season.Special)),
    MAHOGANY_TREE("Mahogany Seeds", List.of(7, 7, 7, 7), 28, FruitType.Sap, 1, 2, true, -2, 0, List.of(Season.Special)),
    MUSHROOM_TREE("Mushroom Tree Seeds", List.of(7, 7, 7, 7), 28, FruitType.CommonMushroom, 1, 40, true, 38, 17, List.of(Season.Special)),
    MYSTIC_TREE("Mystic Tree Seeds", List.of(7, 7, 7, 7), 28, FruitType.MysticSyrup, 7, 1000, true, 500, 225, List.of(Season.Special));

    public final String source;
    public final List<Integer> stages;
    public final int totalHarvestTime;
    public final FruitType product;
    public final int harvestCycle;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final int health;
    public final List<Season> seasons;

    TreeType(String source, List<Integer> stages, int totalHarvestTime, FruitType product,
             int harvestCycle, int baseSellPrice, boolean isEdible,
             int energy, int health, List<Season> seasons) {
        this.source = source;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.product = product;
        this.harvestCycle = harvestCycle;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.health = health;
        this.seasons = seasons;
    }
}
