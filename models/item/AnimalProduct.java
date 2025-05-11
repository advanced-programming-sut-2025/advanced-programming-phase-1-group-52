package models.item;

import enums.items.MaterialType;

public class AnimalProduct extends Item {
    public AnimalProduct(MaterialType material) {
        super(material);
        this.itemType = MaterialType.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
