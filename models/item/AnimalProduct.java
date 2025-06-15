package models.item;

import enums.items.AnimalProductType;
import enums.items.ItemType;

public class AnimalProduct extends Item{

    public AnimalProduct(ItemType itemType, int number) {
        super(itemType, number);
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public AnimalProductType getAnimalProductType() {
        return (AnimalProductType) itemType;
    }
}
