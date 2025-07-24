package com.example.main.enums.items;


public enum MaterialType implements ItemType {
    Cheese("cheese"),
    Sugar("sugar"),
    Oil("oil"),

    // Crops
    Wheat_Flour("Wheat flour mat"),
    Rice("Rice"),

    Wood("Wood"),
    Stone("Stone"),
    Fiber("Fiber"),
    Hardwood("Hard wood"),
    Gold_Coin("Gold coin"),
    Hay("hay"),

    // pierre's remainings
    Bouquet("Bouquet"),
    Wedding_Ring("Wedding Ring"),
    Deluxe_Retaining_Soil("Deluxe retaining Soil"),
    Speed_Gro("Speed-Gro"),
    Basic_Retaining_Soil("Basic retaining Soil"),
    Quality_Retaining_Soil("Quality-Retaining Soil"),

    // Joja's remainings
    Joja_Cola("Joja Cola"),

    //todo: what is this :|
    Trout_Soup( "Trout Soup");

    private final String name;

    MaterialType(String name) {
        this.name = name;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getEnumName() {
        return name();
    }
}
