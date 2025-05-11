package models.item;

import enums.items.MaterialType;

public class Good extends Item{
    public Good(MaterialType material) {
        super(material);
        this.itemType = MaterialType.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
