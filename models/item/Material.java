package models.item;

import enums.items.MaterialType;

public class Material extends Item{
    public Material(MaterialType material) {
        super(material);
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
