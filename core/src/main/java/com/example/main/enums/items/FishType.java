package com.example.main.enums.items;

import com.example.main.enums.design.Season;

public enum FishType implements ItemType {
    Salmon("salmon", 75, Season.Fall,"Ordinary"),
    Sardine("sardine", 40, Season.Fall,"Ordinary"),
    Shad("shad", 60, Season.Fall,"Ordinary"),
    BlueDiscus("blue discus", 120, Season.Fall,"Ordinary"),
    MidnightCarp("midnight carp", 150, Season.Winter,"Ordinary"),
    Squid("squid", 80, Season.Winter,"Ordinary"),
    Tuna("tuna", 100, Season.Winter,"Ordinary"),
    Perch("perch", 55, Season.Winter,"Ordinary"),
    Flounder("flounder", 100, Season.Spring,"Ordinary"),
    Lionfish("lionfish", 100, Season.Spring,"Ordinary"),
    Herring("herring", 30, Season.Spring,"Ordinary"),
    GhostFish("ghost fish", 45, Season.Spring,"Ordinary"),
    Tilapia("tilapia", 75, Season.Summer,"Ordinary"),
    Dorado("dorado", 100, Season.Summer,"Ordinary"),
    Sunfish("sunfish", 30, Season.Summer,"Ordinary"),
    RainbowTrout("rainbow trout", 65, Season.Summer,"Ordinary"),
    Legend("legend", 5000, Season.Spring,"Legendary"),
    GlacierFish("glacier fish", 1000, Season.Winter,"Legendary"),
    Angler("angler", 900, Season.Fall,"Legendary"),
    CrimsonFish("crimson fish", 1500, Season.Summer,"Legendary"),
    ;

    private final String name;
    private final int price;
    private final Season season;
    private final String type;

    FishType(String name, int price, Season season, String type) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.type = type;
    }

    @Override
    public String getName() { return name; }

    public int getPrice() { return price; }

    public Season getSeason() { return season; }

    public String getType() { return type; }

    @Override
    public boolean isTool() {
        return false;
    }
}
