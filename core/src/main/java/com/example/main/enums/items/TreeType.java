// In main/enums/items/TreeType.java

package com.example.main.enums.items;

import java.util.List;

import com.example.main.enums.design.Season;

public enum TreeType implements ItemType{
    // Fruit trees
    APRICOT_TREE("apricot tree", "Apricot_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Apricot, 1, 59, true, 38, List.of(Season.Spring), false),
    CHERRY_TREE("cherry tree", "Cherry_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Cherry, 1, 80, true, 38, List.of(Season.Spring), false),
    BANANA_TREE("banana tree", "Banana_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Banana, 1, 150, true, 75, List.of(Season.Summer), false),
    MANGO_TREE("mango tree", "Mango_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Mango, 1, 130, true, 100, List.of(Season.Summer), false),
    ORANGE_TREE("orange tree", "Orange_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Orange, 1, 100, true, 38, List.of(Season.Summer), false),
    PEACH_TREE("peach tree", "Peach_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Peach, 1, 140, true, 38, List.of(Season.Summer), false),
    APPLE_TREE("apple tree", "Apple_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Apple, 1, 100, true, 38, List.of(Season.Fall), false),
    POMEGRANATE_TREE("pomegranate tree", "Pomegranate_Sapling", List.of(7, 7, 7, 7), 28, FruitType.Pomegranate, 1, 140, true, 38, List.of(Season.Fall), false),

    // Special trees
    OAK_TREE("oak tree", "Acorn", List.of(7, 7, 7, 7), 28, FruitType.OakResin, 7, 150, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MAPLE_TREE("maple tree", "Maple_Seed", List.of(7, 7, 7, 7), 28, FruitType.MapleSyrup, 9, 200, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    PINE_TREE("pine tree", "Pine_Cone", List.of(7, 7, 7, 7), 28, FruitType.PineTar, 5, 100, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MAHOGANY_TREE("mahogany tree", "Mahogany_Seed", List.of(7, 7, 7, 7), 28, FruitType.Sap, 1, 2, true, -2, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MUSHROOM_TREE("mushroom tree", "Mushroom_Tree_Seed", List.of(7, 7, 7, 7), 28, FruitType.CommonMushroom, 1, 40, true, 38, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MYSTIC_TREE("mystic tree", "Mystic_Tree_Seed", List.of(7, 7, 7, 7), 28, FruitType.MysticSyrup, 7, 1000, true, 500, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false);

    private final String name;
    private final String sourceSeedName; // Changed from ForagingSeedType to String
    private final List<Integer> stages;
    private final int totalHarvestTime;
    private final FruitType product;
    private final int harvestCycle;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;
    private final List<Season> seasons;
    private final boolean isForaging;

    TreeType(String name, String sourceSeedName, List<Integer> stages, int totalHarvestTime, FruitType product,
             int harvestCycle, int baseSellPrice, boolean isEdible,
             int energy, List<Season> seasons, boolean isForaging) {
        this.name = name;
        this.sourceSeedName = sourceSeedName;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.product = product;
        this.harvestCycle = harvestCycle;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.seasons = seasons;
        this.isForaging = isForaging;
    }

    public ForagingSeedType getSource() {
        // Look up the enum by name at runtime to avoid circular dependency
        return ForagingSeedType.valueOf(this.sourceSeedName);
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isEdible() {
        return isEdible;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public int getHarvestCycle() {
        return harvestCycle;
    }

    public FruitType getProduct() {
        return product;
    }

    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }

    public List<Integer> getStages() {
        return stages;
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
}
