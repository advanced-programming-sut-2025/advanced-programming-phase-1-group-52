package models.item;

import enums.items.MaterialType;

public class Food extends Item {
    public Food(MaterialType material) {
        super(material);
        this.itemType = MaterialType.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
