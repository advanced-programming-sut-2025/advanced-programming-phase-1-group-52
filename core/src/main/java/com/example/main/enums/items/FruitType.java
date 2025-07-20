package com.example.main.enums.items;

public enum FruitType implements ItemType{
    Apricot("Apricot"),
    Cherry("Cherry"),
    Banana("Banana"),
    Mango("Mango"),
    Orange("Orange"),
    Peach("Peach"),
    Apple("Apple"),
    Pomegranate("Pomegranate"),
    Oak_Resin("Oak resin"),
    Maple_Syrup("Maple syrup"),
    Pine_Tar("Pine tar"),
    Sap("Sap"),
    Common_Mushroom("Common mushroom"),
    Mystic_Syrup("Mystic syrup");

    private final String fruitName;

    FruitType(String fruitName) {
        this.fruitName = fruitName;
    }

    @Override
    public String getName() {
        return fruitName;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    public String getEnumName() {
        return name();
    }
}
