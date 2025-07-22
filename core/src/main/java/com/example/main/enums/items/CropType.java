package com.example.main.enums.items;

import com.example.main.enums.design.Season;
import java.util.List;

public enum CropType implements ItemType {
    // Spring crops
    Blue_Jazz("blue jazz", 1,2,2,2, 7, true, null, 50, true, 45, 20, Season.Spring, false),
    Carrot("carrot", 1,1,1,3, 6, true, null, 35, true, 75, 33, Season.Spring, false),
    Cauliflower("cauliflower", 1,2,4,4, 11, true, null, 175, true, 75, 33, Season.Spring, true),
    Coffee_Bean("coffee bean", 1,2,2,3, 8, false, 2, 15, false, 0, 0, Season.Spring, false),
    Garlic("garlic", 1,1,1,1, 4, true, null, 60, true, 20, 9, Season.Spring, false),
    Green_Bean("green bean", 1,1,1,3, 9, false, 3, 40, true, 25, 11, Season.Spring, false),
    Kale("kale", 1,2,2,1, 6, true, null, 110, true, 50, 22, Season.Spring, false),
    Parsnip("parsnip", 1,1,1,1, 4, true, null, 35, true, 25, 11, Season.Spring, false),
    Potato("potato",  1,1,1,2, 5, true, null, 80, true, 25, 11, Season.Spring, false),
    Rhubarb("rhubarb", 2,2,2,3, 9, true, -1, 220, false, -1, -1, Season.Spring, false),
    Strawberry("strawberry", 1,1,2,2, 6, false, 4, 120, true, 50, 22, Season.Spring, false),
    Tulip("tulip", 1,1,2,2, 6, true, -1, 30, true, 45, 20, Season.Spring, false),
    Unmilled_Rice("unmilled rice", 1,2,2,3, 8, true, -1, 30, true, 3, 1, Season.Spring, false),

    // Summer crops
    Blueberry("blueberry", 1,3,3,4, 13, false, 2, 50, true, 25, 11, Season.Summer, false),
    Corn("corn", 2,3,3,3, 14, false, 3, 50, true, 25, 11, Season.Summer, false),
    Hops("hops", 1,1,2,3, 11, false, 4, 25, true, 45, 20, Season.Summer, false),
    Hot_Pepper("hot pepper", 1,1,1,1, 5, false, 3, 40, true, 13, 5, Season.Summer, false),
    Melon("melon", 1,2,3,3, 12, true, -1, 250, true, 113, 50, Season.Summer, true),
    Poppy("poppy", 1,2,2,2, 7, true, -1, 140, true, 45, 20, Season.Summer, false),
    Radish("radish", 2,1,2,1, 6, true, -1, 90, true, 45, 20, Season.Summer, false),
    Red_Cabbage("red cabbage", 2,1,2,2, 9, true, -1, 260, true, 75, 33, Season.Summer, false),
    Starfruit("starfruit", 2,3,2,3, 13, true, -1, 750, true, 125, 56, Season.Summer, false),
    Summer_Spangle("summer spangle", 1,2,3,1, 8, true, -1, 90, true, 45, 20, Season.Summer, false),
    Summer_Squash("summer squash", 1,1,1,2, 6, false, 3, 45, true, 63, 28, Season.Summer, false),
    Sunflower("sunflower", 1,2,3,2, 8, true, -1, 80, true, 45, 20, Season.Summer, false),
    Tomato("tomato", 2,2,2,2, 11, false, 3, 60, true, 20, 9, Season.Summer, false),
    Wheat("wheat", 1,1,1,1, 4, true, 0, 25, false, 0, 0, Season.Summer, false),

    // Fall crops
    Amaranth("amaranth", 1,2,2,2, 7, true, 0, 150, true, 50, 22, Season.Fall, false),
    Artichoke("artichoke", 2,2,1,2, 8, true, 0, 160, true, 30, 13, Season.Fall, false),
    Beet("beet", 1,1,2,2, 6, true, 0, 100, true, 30, 13, Season.Fall, false),
    Bok_Choy("bok choy", 1,1,1,1, 4, true, 0, 80, true, 25, 11, Season.Fall, false),
    Broccoli("broccoli", 2,2,2,2, 8, false, 4, 70, true, 63, 28, Season.Fall, false),
    Cranberries("cranberries", 1,2,1,1, 7, false, 5, 75, true, 38, 17, Season.Fall, false),
    Eggplant("eggplant", 1,1,1,1, 5, false, 5, 60, true, 20, 9, Season.Fall, false),
    Fairy_Rose("fairy rose", 1,4,4,3, 12, true, 0, 290, true, 45, 20, Season.Fall, false),
    Grape("grape", 1,1,2,3, 10, false, 3, 80, true, 38, 17, Season.Fall, false),
    Pumpkin("pumpkin", 1,2,3,4, 13, true, 0, 320, false, 0, 0, Season.Fall, true),
    Yam("yam", 1,3,3,3, 10, true, 0, 160, true, 45, 20, Season.Fall, false),
    Sweet_Gem_Berry("sweet gem berry", 2,4,6,6, 24, true, 0, 3000, false, 0, 0, Season.Fall, false),

    // Winter & special crops
    PowderMelon("powder melon", 1,2,1,2, 7, true, 0, 60, true, 63, 28, Season.Winter, true),
    Ancient_Fruit("ancient fruit", 2,7,7,7, 28, false, 7, 550, false, 0, 0, Season.Spring, false);

    private final String name;
    private final int growthStages1;
    private final int growthStages2;
    private final int growthStages3;
    private final int growthStages4;
    private final int totalHarvestTime;
    private final boolean oneTimeHarvest;
    private final Integer regrowthTime;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;
    private final int baseHealth;
    private final Season seasons;
    private final boolean canBecomeGiant;

    CropType(String name, int growthStages1,  int growthStages2,  int growthStages3,  int growthStages4, int totalHarvestTime,
             boolean oneTimeHarvest, Integer regrowthTime, int baseSellPrice,
             boolean isEdible, int energy, int baseHealth,
             Season seasons, boolean canBecomeGiant) {
        this.name = name;
        this.growthStages1 = growthStages1;
        this.growthStages2 = growthStages2;
        this.growthStages3 = growthStages3;
        this.growthStages4 = growthStages4;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTimeHarvest = oneTimeHarvest;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.baseHealth = baseHealth;
        this.seasons = seasons;
        this.canBecomeGiant = canBecomeGiant;
    }

    public List<Integer> getStages() {
        return List.of(growthStages1, growthStages2, growthStages3, growthStages4);
    }

    public int getTotalHarvestTime() { return totalHarvestTime; }
    public boolean isOneTimeHarvest() { return oneTimeHarvest; }
    public Integer getRegrowthTime() { return regrowthTime; }
    public int getBaseSellPrice() { return baseSellPrice; }
    public boolean isEdible() { return isEdible; }
    public int getEnergy() { return energy; }
    public int getBaseHealth() { return baseHealth; }
    public Season getSeasons() { return seasons; }
    public boolean canBecomeGiant() { return canBecomeGiant; }

    @Override
    public boolean isTool() { return false; }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEnumName() {
        return name();
    }

    public ForagingSeedType getSeed() {
        for (ForagingSeedType type : ForagingSeedType.values()) {
            if (type.getPlantType() == this) {
                return type;
            }
        }
        return null;
    }
}
