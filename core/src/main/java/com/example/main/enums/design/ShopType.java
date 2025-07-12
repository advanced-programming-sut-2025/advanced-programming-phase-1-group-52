package com.example.main.enums.design;

public enum ShopType {
    Blacksmith("Clint", 32, 2),
    JojaMart("Morris", 46, 2),
    PierresGeneralStore("Pierre", 32, 12),
    CarpentersShop("Robin", 46, 12),
    FishShop("Willy", 32, 22),
    MarniesRanch("Marnie", 46, 22),
    TheStardropSaloon("Gus", 32, 32);

    private final String name;
    private final int cornerX;
    private final int cornerY;

    ShopType(String name, int cornerX, int cornerY) {
        this.name = name;
        this.cornerX = cornerX;
        this.cornerY = cornerY;
    }

    public String getName() {
        return name;
    }

    public int getCornerX() {
        return cornerX;
    }

    public int getCornerY() {
        return cornerY;
    }
}
