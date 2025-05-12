package enums.items;

import enums.design.Season;

import java.util.Arrays;
import java.util.List;

public enum CropType implements ItemType {
    BlueJazz(
            "Jazz Seeds", Arrays.asList(1,2,2,2), 7, true, null, 50,
            true, 45, 20, Arrays.asList(Season.Spring), false
    ),
    Carrot(
            "Carrot Seeds", Arrays.asList(1,1,1), 3, true, null, 35,
            true, 75, 33, Arrays.asList(Season.Spring), false
    ),
    Cauliflower(
            "Cauliflower Seeds", Arrays.asList(1,2,4,4,1), 12, true, null, 175,
            true, 75, 33, Arrays.asList(Season.Spring), true
    ),
    CoffeeBean(
            "Coffee Bean", Arrays.asList(1,2,2,3,2), 10, false, 2, 15,
            false, 0, 0, Arrays.asList(Season.Spring, Season.Summer), false
    ),
    Garlic(
            "Garlic Seeds", Arrays.asList(1,1,1,1), 4, true, null, 60,
            true, 20, 9, Arrays.asList(Season.Spring), false
    ),
    GreenBean(
            "Bean Starter", Arrays.asList(1,1,1,3,4), 10, false, 3, 40,
            true, 25, 11, Arrays.asList(Season.Spring), false
    ),
    Kale(
            "Kale Seeds", Arrays.asList(1,2,2,1), 6, true, null, 110,
            true, 50, 22, Arrays.asList(Season.Spring), false
    ),
    Parsnip(
            "Parsnip Seeds", Arrays.asList(1,1,1,1), 4, true, null, 35,
            true, 25, 11, Arrays.asList(Season.Spring), false
    ),
    Potato(
            "Potato Seeds", Arrays.asList(1,1,1,2,1), 6, true, null, 80,
            true, 25, 11, Arrays.asList(Season.Spring), false
    ),
    Rhubarb("Rhubarb", Arrays.asList(2, 2, 2, 3, 4), 13, true, -1, 220,
            false, -1, -1, Arrays.asList(Season.Spring), false
    ),
    Strawberry(
            "Strawberry",  Arrays.asList(1, 1, 2, 2, 2), 8, false, 4, 120,
            true, 50, 22, Arrays.asList(Season.Spring), false
    ),
    Tulip(
            "Tulip", Arrays.asList(1, 1, 2, 2), 6, true, -1, 30,
            true, 45, 20, Arrays.asList(Season.Spring), false
    ),
    UnmilledRice(
            "Unmilled Rice", Arrays.asList(1, 2, 2, 3), 8, true, -1, 30,
            true, 3, 1, Arrays.asList(Season.Spring), false
    ),
    Blueberry(
            "Blueberry", Arrays.asList(1, 3, 3, 4, 2), 13, false, 4, 50,
            true, 25, 11, Arrays.asList(Season.Summer), false
    ),
    Corn(
            "Corn", Arrays.asList(2, 3, 3, 3, 3), 14, false, 4, 50,
            true, 25, 11, Arrays.asList(Season.Summer, Season.Fall), false
    ),
    Hops(
            "Hops", Arrays.asList(1, 1, 2, 3, 4), 11, false, 1, 25,
            true, 45, 20, Arrays.asList(Season.Summer), false
    ),
    HotPepper(
            "Hot Pepper", Arrays.asList(1, 1, 1, 1, 1), 5, false, 3, 40,
            true, 13, 5, Arrays.asList(Season.Summer), false
    ),
    Melon(
            "Melon", Arrays.asList(1, 2, 3, 3, 3), 12, true, -1, 250,
            true, 113, 50, Arrays.asList(Season.Summer), true
    ),
    Poppy(
            "Poppy", Arrays.asList(1, 2, 2, 2), 7, true, -1, 140,
            true, 45, 20, Arrays.asList(Season.Summer), false
    ),
    Radish(
            "Radish", Arrays.asList(2, 1, 2, 1), 6, true, -1, 90,
            true, 45, 20, Arrays.asList(Season.Summer), false
    ),
    RedCabbage(
            "Red Cabbage", Arrays.asList(2, 1, 2, 2, 2), 9, true, -1, 260,
            true, 75, 33, Arrays.asList(Season.Summer), false
    ),
    Starfruit(
            "Starfruit", Arrays.asList(2, 3, 2, 3, 3), 13, true, -1, 750,
            true, 125, 56, Arrays.asList(Season.Summer), false
    ),
    SummerSpangle(
            "Summer Spangle", Arrays.asList(1, 2, 3, 1), 8, true, -1, 90,
            true, 45, 20, Arrays.asList(Season.Summer), false
    ),
    SummerSquash(
            "Summer Squash", Arrays.asList(1, 1, 1, 2, 1), 6, false, 3, 45,
            true, 63, 28, Arrays.asList(Season.Summer), false
    ),
    Sunflower(
            "Sunflower", Arrays.asList(1, 2, 3, 2), 8, true, -1, 80,
            true, 45, 20, Arrays.asList(Season.Summer, Season.Fall), false
    ),
    Tomato(
            "Tomato", Arrays.asList(2, 2, 2, 2, 3), 11, false, 4, 60,
            true, 20, 9, Arrays.asList(Season.Summer), false
    ),
    WHEAT(
            "Wheat", Arrays.asList(1, 1, 1, 1), 4, true, 0, 25,
            false, 0, 0, Arrays.asList(Season.Summer, Season.Fall), false
    ),
    Amaranth(
            "Amaranth", Arrays.asList(1, 2, 2, 2), 7, true, 0, 150,
            true, 50, 22, Arrays.asList(Season.Fall), false
    ),
    Artichoke(
            "Artichoke", Arrays.asList(2, 2, 1, 2, 1), 8, true, 0, 160,
            true, 30, 13, Arrays.asList(Season.Fall), false
    ),
    Beet(
            "Beet", Arrays.asList(1, 1, 2, 2), 6, true, 0, 100,
            true, 30, 13, Arrays.asList(Season.Fall), false
    ),
    BokChoy(
            "Bok Choy", Arrays.asList(1, 1, 1, 1), 4, true, 0, 80
            , true, 25, 11, Arrays.asList(Season.Fall), false
    ),
    Broccoli(
            "Broccoli", Arrays.asList(2, 2, 2, 2), 8, false, 4, 70,
            true, 63, 28, Arrays.asList(Season.Fall), false
    ),
    Cranberries(
            "Cranberries", Arrays.asList(1, 2, 1, 1, 2), 7, false, 5, 75,
            true, 38, 17, Arrays.asList(Season.Fall), false
    ),
    Eggplant(
            "Eggplant", Arrays.asList(1, 1, 1, 1), 5, false, 5, 60,
            true, 20, 9, Arrays.asList(Season.Fall), false
    ),
    FairyRose(
            "Fairy Rose", Arrays.asList(1, 4, 4, 3), 12, true, 0, 290,
            true, 45, 20, Arrays.asList(Season.Fall), false
    ),
    Grape(
            "Grape", Arrays.asList(1, 1, 2, 3, 3), 10, false, 3, 80,
            true, 38, 17, Arrays.asList(Season.Fall), false
    ),
    Pumpkin(
            "Pumpkin", Arrays.asList(1, 2, 3, 4, 3), 13, true, 0, 320,
            false, 0, 0, Arrays.asList(Season.Fall), true
    ),
    Yam(
            "Yam", Arrays.asList(1, 3, 3, 3), 10, true, 0, 160,
            true, 45, 20, Arrays.asList(Season.Fall), false
    ),
    SweetGemBerry(
            "Sweet Gem Berry", Arrays.asList(2, 4, 6, 6, 6), 24, true, 0, 3000,
            false, 0, 0, Arrays.asList(Season.Fall), false
    ),
    PowderMelon(
            "Powder melon", Arrays.asList(1, 2, 1, 2, 1), 7, true, 0, 60,
            true, 63, 28, Arrays.asList(Season.Winter), true
    ),
    AncientFruit(
            "Ancient Fruit", Arrays.asList(2, 7, 7, 7, 5), 28, false, 7, 550,
            false, 0, 0, Arrays.asList(Season.Spring, Season.Summer, Season.Fall), false
    );

    private final String seedSource;
    private final List<Integer> growthStages;
    private final int totalHarvestTime;
    private final boolean oneTimeHarvest;
    private final Integer regrowthTime;
    private final int baseSellPrice;
    private final boolean isEdible;
    private final int energy;
    private final int baseHealth;
    private final List<Season> seasons;
    private final boolean canBecomeGiant;

    CropType(String seedSource, List<Integer> growthStages, int totalHarvestTime,
             boolean oneTimeHarvest, Integer regrowthTime, int baseSellPrice,
             boolean isEdible, int energy, int baseHealth,
             List<Season> seasons, boolean canBecomeGiant) {
        this.seedSource = seedSource;
        this.growthStages = growthStages;
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

    public String getSeedSource() { return seedSource; }
    public List<Integer> getGrowthStages() { return growthStages; }
    public int getTotalHarvestTime() { return totalHarvestTime; }
    public boolean isOneTimeHarvest() { return oneTimeHarvest; }
    public Integer getRegrowthTime() { return regrowthTime; }
    public int getBaseSellPrice() { return baseSellPrice; }
    public boolean isEdible() { return isEdible; }
    public int getEnergy() { return energy; }
    public int getBaseHealth() { return baseHealth; }
    public List<Season> getSeasons() { return seasons; }
    public boolean canBecomeGiant() { return canBecomeGiant; }

    @Override
    public boolean isTool() { return false; }
}
