package models;

import enums.items.AnimalType;

public class PurchasedAnimal {
    private final AnimalType type;
    private final String name;

    public PurchasedAnimal(AnimalType type, String name) {
        this.type = type;
        this.name = name;
    }

    public AnimalType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
