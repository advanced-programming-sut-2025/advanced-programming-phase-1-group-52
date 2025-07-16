package com.example.main.enums.design;

public enum ShopType {
    Blacksmith("Blacksmith", 32, 2),
    JojaMart("Joja Mart", 46, 2),
    PierresGeneralStore("Pierre's General Store", 32, 12),
    CarpentersShop("Carpenter's Shop", 46, 12),
    FishShop("Willy's Fish Shop", 32, 22),
    MarniesRanch("Marnie's Ranch", 46, 22),
    TheStardropSaloon("The Stardrop Saloon", 32, 32);

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
