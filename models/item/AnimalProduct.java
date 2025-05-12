package models.item;

import enums.items.AnimalType;
import enums.items.MaterialType;

public class AnimalProduct extends Item {
    public AnimalProduct(AnimalType animalType) {
        super(animalType);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
