package com.example.main.enums.items;

import java.util.ArrayList;

public enum AnimalType implements ItemType{
    Chicken("Chicken",800, true) {
        {
            products.add(AnimalProductType.Egg);
            products.add(AnimalProductType.Large_Egg);
        }
    },
    Blue_Chicken("Blue Chicken", 800, true),
    Brown_Chicken("Brown Chicken", 800, true),
    Golden_Chicken("Golden Chicken", 100000, true),
    Void_Chicken("Void Chicken", 8000, true),
    Duck("Duck", 1200, true),
    Rabbit("Rabbit", 8000, true),
    Dinosaur("Dinosaur", 10000, false),

    Cow("Cow",5000, false),
    Brown_Cow("Brown Cow", 5000, false),
    White_Cow("White Cow", 5000, false),
    Goat("Goat",4000, false),
    Sheep("Sheep",8000, false),
    Pig("Pig",16000, false),
    Ostrich("Ostrich", 20000, false);


    protected final ArrayList<AnimalProductType> products = new ArrayList<>();
    private final String name;
    private final int price;
    private final boolean needsCage;

    private AnimalType(String name, int price, boolean needsCage) {
        this.price = price;
        this.name = name;
        this.needsCage = needsCage;
    }


    public ArrayList<AnimalProductType> getProducts() {
        return new ArrayList<>(products); // Return defensive copy
    }

    public int getPrice() {
        return price;
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
