package models.item;

import enums.items.MaterialType;

public class Mineral extends Item {
    public Mineral(MaterialType material) {
        super(material);
        this.itemType = MaterialType.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption()  {
        return 0;
    }
}
