package enums.items;


import enums.design.Season;

import java.util.ArrayList;
import java.util.List;

public enum ForagingCropType implements ItemType{
    // All crops:
    CommonMushroom("common mushroom", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), 40, 38),

    // Spring Crops
    Daffodil("daffodil", List.of(Season.Spring), 30, 0),
    Dandelion("dandelion", List.of(Season.Spring), 40, 25),
    Leek("leek", List.of(Season.Spring), 60, 40),
    Morel("morel", List.of(Season.Spring), 150, 20),
    SalmonBerry("salmonberry", List.of(Season.Spring), 5, 25),
    SpringOnion("spring onion", List.of(Season.Spring), 8, 13),
    WildHorseradish("wild horseradish", List.of(Season.Spring), 50, 13),

    // Summer Crops
    FiddleHeadFern("fiddlehead fern", List.of(Season.Summer), 90, 25),
    Grape("grape", List.of(Season.Summer), 80, 38),
    RedMushroom("red mushroom", List.of(Season.Summer), 75, -50),
    SpiceBerry("spice berry", List.of(Season.Summer), 80, 25),
    SweetPea("sweet pea", List.of(Season.Summer), 50, 0),

    // Fall Crops
    Blackberry("blackberry", List.of(Season.Fall), 25, 25),
    Chanterelle("chanterelle", List.of(Season.Fall), 160, 75),
    Hazelnut("hazelnut", List.of(Season.Fall), 40, 38),
    PurpleMushroom("purple mushroom", List.of(Season.Fall), 90, 30),
    WildPlum("wild plum", List.of(Season.Fall), 80, 25),

    // Winter Crops
    Crocus("crocus", List.of(Season.Winter), 60, 0),
    CrystalFruit("crystal fruit", List.of(Season.Winter), 150, 63),
    Holly("holly", List.of(Season.Winter), 80, -37),
    SnowYam("snow yam", List.of(Season.Winter), 100, 30),
    WinterRoot("winter root", List.of(Season.Winter), 70, 25);

    private final String name;
    private final List<Season> seasons;
    private final int baseSellPrice;
    private final int energy;

    ForagingCropType(String name, List<Season> seasons, int baseSellPrice, int energy) {
        this.name = name;
        this.seasons = seasons;
        this.baseSellPrice = baseSellPrice;
        this.energy = energy;
    }

    // Getters
    public List<Season> getSeason() {
        return seasons;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
