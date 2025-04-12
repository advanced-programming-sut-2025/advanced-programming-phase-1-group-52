package models.item;

import enums.items.Materials;

public class Material extends Item{
    public Material(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
