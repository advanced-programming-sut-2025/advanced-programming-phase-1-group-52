package com.example.main.enums.items;

import com.example.main.enums.design.Season;

public enum FishType implements ItemType {

    // Spring Fish
    Anchovy("Anchovy", 30, Season.Spring, "Ordinary", MovementPattern.DARTER),
    Sardine("Sardine", 40, Season.Spring, "Ordinary", MovementPattern.DARTER),
    Bream("Bream", 45, Season.Spring, "Ordinary", MovementPattern.SMOOTH),
    Flounder("Flounder", 100, Season.Spring, "Ordinary", MovementPattern.SINKER),
    Herring("Herring", 30, Season.Spring, "Ordinary", MovementPattern.DARTER),
    Eel("Eel", 85, Season.Spring, "Ordinary", MovementPattern.SMOOTH),
    Legend("Legend", 5000, Season.Spring, "Legendary", MovementPattern.SMOOTH),

    // Summer Fish
    Pufferfish("Pufferfish", 200, Season.Summer, "Ordinary", MovementPattern.FLOATER),
    Rainbow_Trout("Rainbow Trout", 65, Season.Summer, "Ordinary", MovementPattern.SMOOTH),
    Red_Mullet("Red Mullet", 75, Season.Summer, "Ordinary", MovementPattern.SMOOTH),
    Sunfish("Sunfish", 30, Season.Summer, "Ordinary", MovementPattern.MIXED),
    Tilapia("Tilapia", 75, Season.Summer, "Ordinary", MovementPattern.MIXED),
    Dorado("Dorado", 100, Season.Summer, "Ordinary", MovementPattern.DARTER),
    Octopus("Octopus", 150, Season.Summer, "Ordinary", MovementPattern.SINKER),
    Crimsonfish("Crimsonfish", 1500, Season.Summer, "Legendary", MovementPattern.DARTER),

    // Fall Fish
    Albacore("Albacore", 75, Season.Fall, "Ordinary", MovementPattern.SMOOTH),
    Salmon("Salmon", 75, Season.Fall, "Ordinary", MovementPattern.SMOOTH),
    Walleye("Walleye", 105, Season.Fall, "Ordinary", MovementPattern.SMOOTH),
    Shad("Shad", 60, Season.Fall, "Ordinary", MovementPattern.DARTER),
    Angler("Angler", 900, Season.Fall, "Legendary", MovementPattern.SINKER),
    Blue_Discus("Blue Discus", 120, Season.Fall, "Ordinary", MovementPattern.FLOATER),

    // Winter Fish
    Blobfish("Blobfish", 500, Season.Winter, "Ordinary", MovementPattern.FLOATER),
    Lingcod("Lingcod", 120, Season.Winter, "Ordinary", MovementPattern.SINKER),
    Midnight_Carp("Midnight Carp", 150, Season.Winter, "Ordinary", MovementPattern.MIXED),
    Midnight_Squid("Midnight Squid", 100, Season.Winter, "Ordinary", MovementPattern.SINKER),
    Perch("Perch", 55, Season.Winter, "Ordinary", MovementPattern.DARTER),
    Squid("Squid", 80, Season.Winter, "Ordinary", MovementPattern.SINKER),
    Tuna("Tuna", 100, Season.Winter, "Ordinary", MovementPattern.SMOOTH),
    Glacierfish("Glacierfish", 1000, Season.Winter, "Legendary", MovementPattern.MIXED),

    // Special Fish (can be caught in any season in specific locations)
    Bullhead("Bullhead", 75, Season.Special, "Ordinary", MovementPattern.SMOOTH),
    Carp("Carp", 30, Season.Special, "Ordinary", MovementPattern.MIXED),
    Catfish("Catfish", 200, Season.Special, "Ordinary", MovementPattern.SINKER),
    Chub("Chub", 50, Season.Special, "Ordinary", MovementPattern.DARTER),
    Ghostfish("Ghostfish", 45, Season.Special, "Ordinary", MovementPattern.MIXED),
    Ice_Pip("Ice Pip", 500, Season.Special, "Ordinary", MovementPattern.SINKER),
    Largemouth_Bass("Largemouth Bass", 100, Season.Special, "Ordinary", MovementPattern.SMOOTH),
    Lava_Eel("Lava Eel", 700, Season.Special, "Ordinary", MovementPattern.DARTER),
    Lionfish("Lionfish", 100, Season.Special, "Ordinary", MovementPattern.FLOATER),
    Mutant_Carp("Mutant Carp", 1000, Season.Special, "Legendary", MovementPattern.DARTER),
    Pike("Pike", 100, Season.Special, "Ordinary", MovementPattern.DARTER),
    Red_Snapper("Red Snapper", 50, Season.Special, "Ordinary", MovementPattern.MIXED),
    Sandfish("Sandfish", 75, Season.Special, "Ordinary", MovementPattern.SINKER),
    Scorpion_Carp("Scorpion Carp", 150, Season.Special, "Ordinary", MovementPattern.DARTER),
    Sea_Cucumber("Sea Cucumber", 75, Season.Special, "Ordinary", MovementPattern.SINKER),
    Slimejack("Slimejack", 100, Season.Special, "Ordinary", MovementPattern.MIXED),
    Smallmouth_Bass("Smallmouth Bass", 50, Season.Special, "Ordinary", MovementPattern.SMOOTH),
    Spook_Fish("Spook Fish", 220, Season.Special, "Ordinary", MovementPattern.FLOATER),
    Stingray("Stingray", 180, Season.Special, "Ordinary", MovementPattern.SINKER),
    Stonefish("Stonefish", 300, Season.Special, "Ordinary", MovementPattern.SINKER),
    Sturgeon("Sturgeon", 200, Season.Special, "Ordinary", MovementPattern.SMOOTH),
    Super_Cucumber("Super Cucumber", 250, Season.Special, "Ordinary", MovementPattern.FLOATER),
    Tiger_Trout("Tiger Trout", 150, Season.Special, "Ordinary", MovementPattern.DARTER),
    Void_Salmon("Void Salmon", 150, Season.Special, "Ordinary", MovementPattern.MIXED),
    Woodskip("Woodskip", 75, Season.Special, "Ordinary", MovementPattern.MIXED);

    private final String name;
    private final int price;
    private final Season season;
    private final String type;
    private final MovementPattern movementPattern;

    FishType(String name, int price, Season season, String type, MovementPattern pattern) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.type = type;
        this.movementPattern = pattern;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEnumName() {
        return name();
    }

    public int getPrice() {
        return price;
    }

    public Season getSeason() {
        return season;
    }

    public String getType() {
        return type;
    }

    public MovementPattern getMovementPattern() {
        return movementPattern;
    }

    @Override
    public boolean isTool() {
        return false;
    }
}
