package models.item;

import enums.items.AnimalType;

public class AnimalProduct extends Item {
    public AnimalProduct(AnimalType animalType, int number) {
        super(animalType, number);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public AnimalType getAnimalType() {
        return (AnimalType) itemType;
    }
}
