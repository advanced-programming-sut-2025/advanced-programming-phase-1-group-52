package models.item;

import enums.items.Materials;

public class Food extends Item {
    public Food(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
