package com.example.main.enums.items;

import java.util.List;
import com.example.main.enums.design.Season;

public enum TreeType implements ItemType {
    // Fruit trees - 5 growth stages
    Apple("apple tree", "Apple_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Apple, 1, 100, true, 38, List.of(Season.Fall), false),
    Apricot("apricot tree", "Apricot_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Apricot, 1, 59, true, 38, List.of(Season.Spring), false),
    Banana("banana tree", "Banana_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Banana, 1, 150, true, 75, List.of(Season.Summer), false),
    Cherry("cherry tree", "Cherry_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Cherry, 1, 80, true, 38, List.of(Season.Spring), false),
    Mango("mango tree", "Mango_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Mango, 1, 130, true, 100, List.of(Season.Summer), false),
    Orange("orange tree", "Orange_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Orange, 1, 100, true, 38, List.of(Season.Summer), false),
    Peach("peach tree", "Peach_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Peach, 1, 140, true, 38, List.of(Season.Summer), false),
    Pomegranate("pomegranate tree", "Pomegranate_Sapling", List.of(5, 5, 6, 6), 22, FruitType.Pomegranate, 1, 140, true, 38, List.of(Season.Fall), false),

    // Special/Foraging trees - Now with 5 growth stages
    Oak("oak tree", "Acorn", List.of(1, 1, 1, 1), 4, FruitType.Oak_Resin, 7, 150, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    Maple("maple tree", "Maple_Seed", List.of(1, 1, 1, 1), 4, FruitType.Maple_Syrup, 9, 200, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    Pine("pine tree", "Pine_Cone", List.of(1, 1, 1, 1), 4, FruitType.Pine_Tar, 5, 100, false, -1, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    Mahogany("mahogany tree", "Mahogany_Seed", List.of(1, 1, 1, 1), 4, FruitType.Sap, 1, 2, true, -2, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    Mushroom("mushroom tree", "Mushroom_Tree_Seed", List.of(1, 1, 1, 1), 4, FruitType.Common_Mushroom, 1, 40, true, 38, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    Mystic("mystic tree", "Mystic_Tree_Seed", List.of(1, 1, 1, 1), 4, FruitType.Mystic_Syrup, 7, 1000, true, 500, List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false);

    private final String name;
    private final String sourceSeedName;
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
        return ForagingSeedType.valueOf(this.sourceSeedName);
    }

    public boolean isForaging() {
        return isForaging;
    }

    public List<Season> getSeasons() { return seasons; }
    public int getEnergy() { return energy; }
    public boolean isEdible() { return isEdible; }
    public int getBaseSellPrice() { return baseSellPrice; }
    public int getHarvestCycle() { return harvestCycle; }
    public FruitType getProduct() { return product; }
    public int getTotalHarvestTime() { return totalHarvestTime; }
    public List<Integer> getStages() { return stages; }

    @Override
    public boolean isTool() { return false; }

    @Override
    public String getName() { return this.name; }

    public String getEnumName() { return name(); }
}
