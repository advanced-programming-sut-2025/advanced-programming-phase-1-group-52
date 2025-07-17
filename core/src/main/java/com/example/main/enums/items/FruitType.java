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
    OakResin("Oak resin"),
    MapleSyrup("Maple syrup"),
    PineTar("Pine tar"),
    Sap("Sap"),
    CommonMushroom("Common mushroom"),
    MysticSyrup("Mystic syrup");

    private final String fruitName;

    FruitType(String fruitName) {
        this.fruitName = fruitName;
    }

    @Override
    public String getName() {
        return fruitName;
    }

    @Override
    public String getEnumName() {
        return name();
    }

    @Override
    public boolean isTool() {
        return false;
    }
}
