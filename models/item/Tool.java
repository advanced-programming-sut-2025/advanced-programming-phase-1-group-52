package models.item;

import enums.items.MaterialType;

public class Tool extends Item{
    public Tool(MaterialType material) {
        this.itemType = MaterialType.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
