package enums.items;


import enums.design.Season;

public enum ForagingCropType implements ItemType{
    // All crops:

    // Foraging crops :
    CommonMushroom(Season.Special, 40, 38),

    // Spring Crops
    Daffodil(Season.Spring, 30, 0),
    Dandelion(Season.Spring, 40, 25),
    Leek(Season.Spring, 60, 40),
    Morel(Season.Spring, 150, 20),
    SalmonBerry(Season.Spring, 5, 25),
    SpringOnion(Season.Spring, 8, 13),
    WildHorseradish(Season.Spring, 50, 13),

    // Summer Crops
    FiddleHeadFern(Season.Summer, 90, 25),
    Grape(Season.Summer, 80, 38),
    RedMushroom(Season.Summer, 75, -50),
    SpiceBerry(Season.Summer, 80, 25),
    SweetPea(Season.Summer, 50, 0),

    // Fall Crops
    Blackberry(Season.Fall, 25, 25),
    Chanterelle(Season.Fall, 160, 75),
    Hazelnut(Season.Fall, 40, 38),
    PurpleMushroom(Season.Fall, 90, 30),
    WildPlum(Season.Fall, 80, 25),

    // Winter Crops
    Crocus(Season.Winter, 60, 0),
    CrystalFruit(Season.Winter, 150, 63),
    Holly(Season.Winter, 80, -37),
    SnowYam(Season.Winter, 100, 30),
    WinterRoot(Season.Winter, 70, 25);

    private final Season season;
    private final int baseSellPrice;
    private final int energy;

    ForagingCropType(Season season, int baseSellPrice, int energy) {
        this.season = season;
        this.baseSellPrice = baseSellPrice;
        this.energy = energy;
    }

    // Getters
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
}
