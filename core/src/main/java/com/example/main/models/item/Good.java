package com.example.main.models.item;

import com.example.main.enums.items.ArtisanProductType;

public class Good extends Item{
    ArtisanProductType productType;
    private boolean isReadyToUse = false;
    private int timePassed = 0;

    public Good(ArtisanProductType artisanProductType, int number) {
        super(artisanProductType, number);
        this.productType = artisanProductType;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public ArtisanProductType getProductType() {
        return productType;
    }

    public boolean isReadyToUse() {
        return this.isReadyToUse;
    }

    public void setReadyToUSe(boolean isReadyToUse) {
        this.isReadyToUse = isReadyToUse;
    }

    public int getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(int timePassed) {
        this.timePassed = timePassed;
    }
}
