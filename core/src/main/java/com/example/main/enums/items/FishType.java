package com.example.main.enums.items;

import com.example.main.enums.design.Season;

public enum FishType implements ItemType {
    Albacore("Albacore", 75, Season.Fall, "Ordinary"),
    Anchovy("Anchovy", 30, Season.Spring, "Ordinary"),
    Angler("Angler", 900, Season.Fall,"Legendary"),
    Blobfish("Blobfish", 500, Season.Winter, "Ordinary"),
    Blue_Discus("Blue Discus", 120, Season.Fall,"Ordinary"),
    Bream("Bream", 45, Season.Special, "Ordinary"),
    Bullhead("Bullhead", 75, Season.Special, "Ordinary"),
    Carp("Carp", 30, Season.Special, "Ordinary"),
    Catfish("Catfish", 200, Season.Special, "Ordinary"),
    Chub("Chub", 50, Season.Special, "Ordinary"),
    Crimsonfish("Crimsonfish", 1500, Season.Summer,"Legendary"),
    Dorado("Dorado", 100, Season.Summer,"Ordinary"),
    Eel("Eel", 85, Season.Special, "Ordinary"),
    Flounder("Flounder", 100, Season.Spring,"Ordinary"),
    Ghostfish("Ghostfish", 45, Season.Special,"Ordinary"),
    Glacierfish("Glacierfish", 1000, Season.Winter,"Legendary"),
    Halibut("Halibut", 80, Season.Special, "Ordinary"),
    Herring("Herring", 30, Season.Spring,"Ordinary"),
    Ice_Pip("Ice Pip", 500, Season.Special, "Ordinary"),
    Largemouth_Bass("Largemouth Bass", 100, Season.Special, "Ordinary"),
    Lava_Eel("Lava Eel", 700, Season.Special, "Ordinary"),
    Legend("Legend", 5000, Season.Spring,"Legendary"),
    Lingcod("Lingcod", 120, Season.Winter, "Ordinary"),
    Lionfish("Lionfish", 100, Season.Special, "Ordinary"),
    Midnight_Carp("Midnight Carp", 150, Season.Winter,"Ordinary"),
    Midnight_Squid("Midnight Squid", 100, Season.Winter, "Ordinary"),
    Mutant_Carp("Mutant Carp", 1000, Season.Special, "Legendary"),
    Octopus("Octopus", 150, Season.Summer, "Ordinary"),
    Perch("Perch", 55, Season.Winter,"Ordinary"),
    Pike("Pike", 100, Season.Special, "Ordinary"),
    Pufferfish("Pufferfish", 200, Season.Summer, "Ordinary"),
    Rainbow_Trout("Rainbow Trout", 65, Season.Summer,"Ordinary"),
    Red_Mullet("Red Mullet", 75, Season.Summer, "Ordinary"),
    Red_Snapper("Red Snapper", 50, Season.Special, "Ordinary"),
    Salmon("Salmon", 75, Season.Fall,"Ordinary"),
    Sandfish("Sandfish", 75, Season.Special, "Ordinary"),
    Sardine("Sardine", 40, Season.Fall,"Ordinary"),
    Scorpion_Carp("Scorpion Carp", 150, Season.Special, "Ordinary"),
    Sea_Cucumber("Sea Cucumber", 75, Season.Special, "Ordinary"),
    Shad("Shad", 60, Season.Fall,"Ordinary"),
    Slimejack("Slimejack", 100, Season.Special, "Ordinary"),
    Smallmouth_Bass("Smallmouth Bass", 50, Season.Special, "Ordinary"),
    Spook_Fish("Spook Fish", 220, Season.Special, "Ordinary"),
    Squid("Squid", 80, Season.Winter,"Ordinary"),
    Stingray("Stingray", 180, Season.Special, "Ordinary"),
    Stonefish("Stonefish", 300, Season.Special, "Ordinary"),
    Sturgeon("Sturgeon", 200, Season.Special, "Ordinary"),
    Sunfish("Sunfish", 30, Season.Summer,"Ordinary"),
    Super_Cucumber("Super Cucumber", 250, Season.Special, "Ordinary"),
    Tilapia("Tilapia", 75, Season.Summer,"Ordinary"),
    Tiger_Trout("Tiger Trout", 150, Season.Special, "Ordinary"),
    Tuna("Tuna", 100, Season.Winter,"Ordinary"),
    Void_Salmon("Void Salmon", 150, Season.Special, "Ordinary"),
    Walleye("Walleye", 105, Season.Fall, "Ordinary"),
    Woodskip("Woodskip", 75, Season.Special, "Ordinary");


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

    @Override
    public String getEnumName() {
        return name();
    }

    public int getPrice() { return price; }

    public Season getSeason() { return season; }

    public String getType() { return type; }

    @Override
    public boolean isTool() {
        return false;
    }
}
