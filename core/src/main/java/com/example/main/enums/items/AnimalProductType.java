package com.example.main.enums.items;

public enum AnimalProductType implements ItemType {
    Egg("ChickenEgg",50),
    Large_Egg("BigChickenEgg",95),
    Brown_Egg("Brown Egg", 50),
    Large_Brown_Egg("Large Brown Egg", 95),
    Duck_Egg("DuckEgg",95),
    Duck_Feather("DuckFeather",250),
    Wool("RabbitWool",340),
    Rabbit_Foot("RabbitLeg",565),
    Dinosaur_Egg("DinosaurEgg",350),
    Milk("NormalCowMilk",125),
    Large_Milk("LargeCowMilk",190),
    Goat_Milk("NormalGoatMilk",225),
    Large_Goat_Milk("LargeGoatMilk",345),
    Truffle("Truffle",625),
    Void_Egg("Void Egg", 65),
    Golden_Egg("Golden Egg", 500),
    Ostrich_Egg("Ostrich Egg", 600);


    private String name;
    private int price;

    AnimalProductType(String name, int price) {
        this.name = name;
        this.price = price;
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
        return this.name();
    }

    @Override
    public String getEnumName() {
        return name();
    }
}
