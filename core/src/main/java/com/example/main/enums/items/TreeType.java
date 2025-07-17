package com.example.main.enums.items;

import java.util.List;

import com.example.main.enums.design.Season;

public enum TreeType implements ItemType{
    // Fruit trees
    APRICOT_TREE("apricot tree", ForagingSeedType.Apricot_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Apricot, 1, 59, true, 38, List.of(Season.Spring), false),
    CHERRY_TREE("cherry tree", ForagingSeedType.Cherry_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Cherry, 1, 80, true, 38, List.of(Season.Spring), false),
    BANANA_TREE("banana tree", ForagingSeedType.Banana_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Banana, 1, 150, true, 75, List.of(Season.Summer), false),
    MANGO_TREE("mango tree", ForagingSeedType.Mango_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Mango, 1, 130, true, 100, List.of(Season.Summer), false),
    ORANGE_TREE("orange tree", ForagingSeedType.Orange_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Orange, 1, 100, true, 38, List.of(Season.Summer), false),
    PEACH_TREE("peach tree", ForagingSeedType.Peach_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Peach, 1, 140, true, 38, List.of(Season.Summer), false),
    APPLE_TREE("apple tree", ForagingSeedType.Apple_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Apple, 1, 100, true, 38, List.of(Season.Fall), false),
    POMEGRANATE_TREE("pomegranate tree", ForagingSeedType.Pomegranate_Sapling, List.of(7, 7, 7, 7), 28, FruitType.Pomegranate, 1, 140, true, 38, List.of(Season.Fall), false),

    // Special trees
    OAK_TREE("oak tree", ForagingSeedType.Acorn, List.of(7, 7, 7, 7), 28, FruitType.OakResin, 7, 150, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MAPLE_TREE("maple tree", ForagingSeedType.Maple_Seed, List.of(7, 7, 7, 7), 28, FruitType.MapleSyrup, 9, 200, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    PINE_TREE("pine tree", ForagingSeedType.Pine_Cone, List.of(7, 7, 7, 7), 28, FruitType.PineTar, 5, 100, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MAHOGANY_TREE("mahogany tree", ForagingSeedType.Mahogany_Seed, List.of(7, 7, 7, 7), 28, FruitType.Sap, 1, 2, true, -2, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MUSHROOM_TREE("mushroom tree", ForagingSeedType.Mushroom_Tree_Seed, List.of(7, 7, 7, 7), 28, FruitType.CommonMushroom, 1, 40, true, 38, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MYSTIC_TREE("mystic tree", ForagingSeedType.Mystic_Tree_Seed, List.of(7, 7, 7, 7), 28, FruitType.MysticSyrup, 7, 1000, true, 500, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false);

    private final String name;
    private final ForagingSeedType source;
    private final List<Integer> stages;
    private final int totalHarvestTime;
    private final FruitType product;
    private final int harvestCycle;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;
    private final List<Season> seasons;
    private final boolean isForaging;

    TreeType(String name, ForagingSeedType source, List<Integer> stages, int totalHarvestTime, FruitType product,
             int harvestCycle, int baseSellPrice, boolean isEdible,
             int energy, List<Season> seasons, boolean isForaging) {
        this.name = name;
        this.source = source;
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

    public ForagingSeedType getSource() {
        return source;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEnumName() {
        return name();
    }
}
