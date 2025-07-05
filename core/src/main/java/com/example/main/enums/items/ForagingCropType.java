package com.example.main.enums.items;


import com.example.main.enums.design.Season;

public enum ForagingCropType implements ItemType{
    // All crops:
    CommonMushroom("common mushroom", Season.Special, 40, 38),

    // Spring Crops
    Daffodil("daffodil", Season.Spring, 30, 0),
    Dandelion("dandelion", Season.Spring, 40, 25),
    Leek("leek", Season.Spring, 60, 40),
    Morel("morel", Season.Spring, 150, 20),
    SalmonBerry("salmonberry", Season.Spring, 5, 25),
    SpringOnion("spring onion", Season.Spring, 8, 13),
    WildHorseradish("wild horseradish", Season.Spring, 50, 13),

    // Summer Crops
    FiddleHeadFern("fiddlehead fern", Season.Summer, 90, 25),
    Grape("grape", Season.Summer, 80, 38),
    RedMushroom("red mushroom", Season.Summer, 75, -50),
    SpiceBerry("spice berry", Season.Summer, 80, 25),
    SweetPea("sweet pea", Season.Summer, 50, 0),

    // Fall Crops
    Blackberry("blackberry", Season.Fall, 25, 25),
    Chanterelle("chanterelle", Season.Fall, 160, 75),
    Hazelnut("hazelnut", Season.Fall, 40, 38),
    PurpleMushroom("purple mushroom", Season.Fall, 90, 30),
    WildPlum("wild plum", Season.Fall, 80, 25),

    // Winter Crops
    Crocus("crocus", Season.Winter, 60, 0),
    CrystalFruit("crystal fruit", Season.Winter, 150, 63),
    Holly("holly", Season.Winter, 80, -37),
    SnowYam("snow yam", Season.Winter, 100, 30),
    WinterRoot("winter root", Season.Winter, 70, 25);

    private final String name;
    private final Season season;
    private final int baseSellPrice;
    private final int energy;

    ForagingCropType(String name, Season season, int baseSellPrice, int energy) {
        this.name = name;
        this.season = season;
        this.baseSellPrice = baseSellPrice;
        this.energy = energy;
    }

    public Season getSeason() {
        return season;
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
