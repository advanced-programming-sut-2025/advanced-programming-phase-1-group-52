package models.item;

import enums.items.MaterialType;

public class Seed extends Item{
    public Seed(MaterialType material) {
        super(material);
        this.itemType = MaterialType.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
