package com.example.main.models.item;

import com.example.main.enums.items.AnimalProductType;
import com.example.main.enums.items.ItemType;

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
