package models.item;

import enums.items.*;
import enums.items.Growable;

public abstract class GrowableItem extends Item implements Growable {
    protected int dayPassed = 0;
    protected int dayRemaining = 0;
    protected boolean isWatered = false;
    protected boolean isFertilized = false;
    protected int currentStage = 0;

    public GrowableItem(ItemType itemType, int number) {
        super(itemType, number);
    }

    public int getDayPassed() {
        return dayPassed;
    }

    public int getDayRemaining() {
        return dayRemaining;
    }

    public boolean isWatered() {
        return isWatered;
    }

    public boolean isFertilized() {
        return isFertilized;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setDayPassed(int dayPassed) {
        this.dayPassed = dayPassed;
    }

    public void updateDayRemaining() {
        this.dayRemaining = getTotalHarvestTime() - dayPassed;
    }

    public void setIsWatered(boolean isWatered) {
        this.isWatered = isWatered;
    }

    public void setIsFertilized(boolean isFertilized) {
        this.isFertilized = isFertilized;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public abstract int getTotalHarvestTime();
    public abstract String getGrowableName();
}