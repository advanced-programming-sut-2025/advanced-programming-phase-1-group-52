package com.example.main.models.item;

import com.example.main.enums.items.FishType;
import com.example.main.enums.items.MaterialType;


public class  Fish extends Item {
    private double quality;
    private boolean legendary;
    private FishType type;

    public Fish(FishType fishType, int number) {
        super(fishType, number);
        this.type = fishType;
        if(this.type.getType().equals("Ordinary")){
            this.legendary = false;
        }
        this.legendary = true;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public FishType getFishType() {
        return (FishType) itemType;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }
    public boolean isLegendary() {
        return legendary;
    }
    public void setLegendary(boolean legendary) {
        this.legendary = legendary;
    }
}
