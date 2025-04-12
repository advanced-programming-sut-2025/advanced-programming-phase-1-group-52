package models.item;

import enums.items.Materials;

public class Seed extends Item{
    public Seed(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
