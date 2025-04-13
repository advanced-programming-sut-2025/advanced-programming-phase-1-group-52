package models.item;

import enums.items.Materials;

public class Fish extends Item {
    public Fish(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
