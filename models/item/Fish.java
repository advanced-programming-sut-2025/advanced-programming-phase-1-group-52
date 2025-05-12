package models.item;

import enums.items.FishType;
import enums.items.MaterialType;

public class Fish extends Item {
    public Fish(FishType fishType) {
        super(fishType);
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

}
