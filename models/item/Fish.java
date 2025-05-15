package models.item;

import enums.items.FishType;
import enums.items.MaterialType;


public class  Fish extends Item {
    private double quality;
    private boolean legendary;

    public Fish(FishType fishType, int number) {
        super(fishType, number);
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public FishType getFishType() {
        return (FishType) itemType;
    }
}
