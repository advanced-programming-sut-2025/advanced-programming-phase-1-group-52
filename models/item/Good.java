package models.item;

import enums.items.Materials;

public class Good extends Item{
    public Good(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
