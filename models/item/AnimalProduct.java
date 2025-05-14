package models.item;

import enums.items.AnimalType;
import enums.items.MaterialType;

public class AnimalProduct extends Item {
    public AnimalProduct(AnimalType animalType, int number) {
        super(animalType, number);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
