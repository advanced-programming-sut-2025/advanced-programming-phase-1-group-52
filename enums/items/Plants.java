package enums.items;


import enums.design.Season;

public enum Plants implements Items {
    // Fruit Trees
    ApricotTree(Season.Spring),
    CherryTree(Season.Spring),
    BananaTree(Season.Summer),
    MangoTree(Season.Summer),
    OrangeTree(Season.Summer),
    PeachTree(Season.Summer),
    AppleTree(Season.Summer),
    PomegranateTree(Season.Summer),

    // Special Trees
    OakTree(Season.Special),
    MapleTree(Season.Special),
    PineTree(Season.Special),
    MahoganyTree(Season.Special),
    MushroomTree(Season.Special),
    MysticTree(Season.Special),

    // Foraging Trees:
    Acorns(Season.Special),
    MapleSeeds(Season.Special),
    PineCones(Season.Special),
    MahoganySeeds(Season.Special),
    MushroomTreeSeeds(Season.Special);

    private final Season season;

    Plants(Season season){
        this.season = season;
    }

    // Getters
    public Season getSeason() {
        return season;
    }
}
