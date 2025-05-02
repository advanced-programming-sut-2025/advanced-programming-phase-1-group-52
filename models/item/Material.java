package models.item;

import enums.items.MaterialType;

public class Material extends Item{
    public Material(MaterialType material) {
        this.itemType = MaterialType.valueOf(material.name());
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
