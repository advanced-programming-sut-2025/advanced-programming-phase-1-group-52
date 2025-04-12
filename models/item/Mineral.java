package models.item;

import enums.items.Materials;

public class Mineral extends Item {
    public Mineral(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption()  {
        return 0;
    }
}
