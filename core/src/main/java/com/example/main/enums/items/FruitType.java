package com.example.main.enums.items;

public enum FruitType implements ItemType{
    Apricot("Apricot", 50),
    Cherry("Cherry", 10),
    Banana("Banana", 30),
    Mango("Mango", 40),
    Orange("Orange", 20),
    Peach("Peach", 15),
    Apple("Apple", 15),
    Pomegranate("Pomegranate", 30),
    Oak_Resin("Oak resin", 40),
    Maple_Syrup("Maple syrup", 50),
    Pine_Tar("Pine tar", 50),
    Sap("Sap", 40),
    Common_Mushroom("Common mushroom", 10),
    Mystic_Syrup("Mystic syrup", 50);

    private final String fruitName;
    private final int price;

    FruitType(String fruitName, int price) {
        this.fruitName = fruitName;
        this.price = price;
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

    public int getPrice() {
        return price;
    }
}
