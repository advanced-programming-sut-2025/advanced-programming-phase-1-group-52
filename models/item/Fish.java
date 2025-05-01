package models.item;

import enums.items.MaterialType;

public class Fish extends Item {
    public Fish(MaterialType material) {
        this.itemType = MaterialType.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
