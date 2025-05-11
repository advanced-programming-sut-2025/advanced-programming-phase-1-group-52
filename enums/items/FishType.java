package enums.items;

import enums.design.Season;

public enum FishType implements Items {
    Salmon("Salmon", 75, Season.Fall),
    Sardine("Sardine", 40, Season.Fall),
    Shad("Shad", 60, Season.Fall),
    BlueDiscus("Blue Discus", 120, Season.Fall),
    MidnightCarp("Midnight Carp", 150, Season.Winter),
    Squid("Squid", 80, Season.Winter),
    Tuna("Tuna", 100, Season.Winter),
    Perch("Perch", 55, Season.Winter),
    Flounder("Flounder", 100, Season.Spring),
    Lionfish("Lionfish", 100, Season.Spring),
    Herring("Herring", 30, Season.Spring),
    Ghostfish("Ghostfish", 45, Season.Spring),
    Tilapia("Tilapia", 75, Season.Summer),
    Dorado("Dorado", 100, Season.Summer),
    Sunfish("Sunfish", 30, Season.Summer),
    RainbowTrout("Rainbow Trout", 65, Season.Summer),
    Legend("Legend", 5000, Season.Spring),
    Glacierfish("Glacierfish", 1000, Season.Winter),
    Angler("Angler", 900, Season.Fall),
    Crimsonfish("Crimsonfish", 1500, Season.Summer);

    private String name;
    private int price;
    private Season season;

    FishType(String name, int price, Season season) {
        this.name = name;
        this.price = price;
        this.season = season;
    }
}
