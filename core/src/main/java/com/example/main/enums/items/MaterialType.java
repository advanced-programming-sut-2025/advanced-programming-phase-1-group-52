package com.example.main.enums.items;


public enum MaterialType implements ItemType {
    Cheese("cheese", 50),
    Sugar("sugar", 40),
    Oil("oil", 10),

    // Crops
    Wheat_Flour("Wheat flour mat", 10),
    Rice("Rice", 30),

    Wood("Wood", 10),
    Stone("Stone", 10),
    Fiber("Fiber", 30),
    Hardwood("Hard wood", 50),
    Gold_Coin("Gold coin", 1000),
    Hay("hay", 10),

    // pierre's remainings
    Bouquet("Bouquet", 100),
    Wedding_Ring("Wedding Ring", 500),
    Deluxe_Retaining_Soil("Deluxe retaining Soil", 50),
    Speed_Gro("Speed-Gro", 30),
    Basic_Retaining_Soil("Basic retaining Soil", 10),
    Quality_Retaining_Soil("Quality-Retaining Soil", 30),

    // Joja's remainings
    Joja_Cola("Joja Cola", 15),

    //todo: what is this :|
    Trout_Soup( "Trout Soup", 10);

    private final String name;
    private final int price;

    MaterialType(String name, int price) {
        this.name = name;
        this.price = price;
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

    public int getPrice() {
        return price;
    }
}
