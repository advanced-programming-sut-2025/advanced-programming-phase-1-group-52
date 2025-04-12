package models.item;

import enums.items.Materials;

public class AnimalProduct extends Item {
    public AnimalProduct(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
