package com.example.main.enums.items;

import java.util.ArrayList;

public enum AnimalType implements ItemType{
    Chicken("Chicken",800, true) {
        {
            products.add(AnimalProductType.Egg);
            products.add(AnimalProductType.Large_Egg);
        }
    },
    Blue_Chicken("Blue Chicken", 800, true) {
        {
            products.add(AnimalProductType.Egg);
            products.add(AnimalProductType.Large_Egg);
        }
    },
    Brown_Chicken("Brown Chicken", 800, true) {
        {
            products.add(AnimalProductType.Egg);
            products.add(AnimalProductType.Large_Egg);
        }
    },
    Golden_Chicken("Golden Chicken", 100000, true) {
        {
            products.add(AnimalProductType.Egg);
            products.add(AnimalProductType.Large_Egg);
        }
    },
    Void_Chicken("Void Chicken", 8000, true) {
        {
            products.add(AnimalProductType.Egg);
            products.add(AnimalProductType.Large_Egg);
        }
    },
    Duck("Duck", 1200, true) {
        {
            products.add(AnimalProductType.Duck_Egg);
            products.add(AnimalProductType.Duck_Feather);
        }
    },
    Rabbit("Rabbit", 8000, true) {
        {
            products.add(AnimalProductType.Wool);
            products.add(AnimalProductType.Rabbit_Foot);
        }
    },
    Dinosaur("Dinosaur", 10000, false) {
        {
            products.add(AnimalProductType.Dinosaur_Egg);
            products.add(AnimalProductType.Golden_Egg);
        }
    },

    Cow("Cow",5000, false) {
        {
            products.add(AnimalProductType.Milk);
            products.add(AnimalProductType.Large_Milk);
        }
    },
    Brown_Cow("Brown Cow", 5000, false) {
        {
            products.add(AnimalProductType.Milk);
            products.add(AnimalProductType.Large_Milk);
        }
    },
    White_Cow("White Cow", 5000, false) {
        {
            products.add(AnimalProductType.Milk);
            products.add(AnimalProductType.Large_Milk);
        }
    },
    Goat("Goat",4000, false) {
        {
            products.add(AnimalProductType.Goat_Milk);
            products.add(AnimalProductType.Large_Goat_Milk);
        }
    },
    Sheep("Sheep",8000, false) {
        {
            products.add(AnimalProductType.Wool);
        }
    },
    Pig("Pig",16000, false) {
        {
            products.add(AnimalProductType.Truffle);
        }
    },
    Ostrich("Ostrich", 20000, false) {
        {
            products.add(AnimalProductType.Ostrich_Egg);
        }
    };


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
        return new ArrayList<>(products);
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
