package enums.items;

import enums.design.Season;

public enum FishType implements ItemType {
    Salmon("Salmon", 75, Season.Fall,"Ordinary"),
    Sardine("Sardine", 40, Season.Fall,"Ordinary"),
    Shad("Shad", 60, Season.Fall,"Ordinary"),
    BlueDiscus("Blue Discus", 120, Season.Fall,"Ordinary"),
    MidnightCarp("Midnight Carp", 150, Season.Winter,"Ordinary"),
    Squid("Squid", 80, Season.Winter,"Ordinary"),
    Tuna("Tuna", 100, Season.Winter,"Ordinary"),
    Perch("Perch", 55, Season.Winter,"Ordinary"),
    Flounder("Flounder", 100, Season.Spring,"Ordinary"),
    Lionfish("Lionfish", 100, Season.Spring,"Ordinary"),
    Herring("Herring", 30, Season.Spring,"Ordinary"),
    GhostFish("GhostFish", 45, Season.Spring,"Ordinary"),
    Tilapia("Tilapia", 75, Season.Summer,"Ordinary"),
    Dorado("Dorado", 100, Season.Summer,"Ordinary"),
    Sunfish("Sunfish", 30, Season.Summer,"Ordinary"),
    RainbowTrout("Rainbow Trout", 65, Season.Summer,"Ordinary"),
    Legend("Legend", 5000, Season.Spring,"Legendary"),
    GlacierFish("GlacierFish", 1000, Season.Winter,"Legendary"),
    Angler("Angler", 900, Season.Fall,"Legendary"),
    CrimsonFish("CrimsonFish", 1500, Season.Summer,"Legendary"),
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
