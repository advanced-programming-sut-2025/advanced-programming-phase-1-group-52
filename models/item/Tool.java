package models.item;

import enums.items.Materials;

public class Tool extends Item{


    public Tool(Materials material) {
        this.itemType = Materials.valueOf(material.name());
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
