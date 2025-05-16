package models.item;

import enums.items.CropType;
import enums.items.Growable;
import enums.items.ItemType;

public class Crop extends Item implements Growable {
    private ItemType cropType;
    private int dayPassed = 0;
    private int dayRemaining = 0;
    private boolean isWateredToday = false;
    private boolean isFertilizedToday = false;
    private boolean needsWaterToday = false;
    private int currentStage = 0;
    private boolean isReadyToHarvest = false;
    private boolean isNotWateredForTwoDays = false;
    private boolean canBeHarvestAgain = true;

    public Crop(ItemType cropType, int number) {
        super(cropType, number);
        this.cropType = cropType;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    @Override
    public int getDayPassed() {
        return dayPassed;
    }

    @Override
    public void setDayPassed(int dayPassed) {
        this.dayPassed = dayPassed;
    }

    @Override
    public int getDayRemaining() {
        return dayRemaining;
    }

    @Override
    public void setDayRemaining(int dayRemaining) {
        this.dayRemaining = dayRemaining;
    }

    @Override
    public boolean isWateredToday() {
        return isWateredToday;
    }

    @Override
    public void setWateredToday(boolean wateredToday) {
        this.isWateredToday = wateredToday;
    }

    @Override
    public boolean isFertilizedToday() {
        return isFertilizedToday;
    }

    @Override
    public void setFertilizedToday(boolean fertilizedToday) {
        this.isFertilizedToday = fertilizedToday;
    }

    @Override
    public boolean isNeedsWaterToday() {
        return needsWaterToday;
    }

    @Override
    public void setNeedsWaterToday(boolean needsWaterToday) {
        this.needsWaterToday = needsWaterToday;
    }

    @Override
    public int getCurrentStage() {
        return currentStage;
    }

    @Override
    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    @Override
    public boolean isReadyToHarvest() {
        return isReadyToHarvest;
    }

    @Override
    public void setReadyToHarvest(boolean readyToHarvest) {
        this.isReadyToHarvest = readyToHarvest;
    }

    @Override
    public void growFaster() {
        this.dayRemaining -= 1;
    }

    public ItemType getCropType() {
        return cropType;
    public CropType getCropType() {
        return (CropType) cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    @Override
    public boolean isNotWateredForTwoDays() {
        return isNotWateredForTwoDays;
    }

    @Override
    public void setNotWateredForTwoDays(boolean notWateredForTwoDays) {
        isNotWateredForTwoDays = notWateredForTwoDays;
    }

    public boolean canBeHarvestAgain() {
        return canBeHarvestAgain;
    }

    public void setCanBeHarvestAgain(boolean canBeHarvestAgain) {
        this.canBeHarvestAgain = canBeHarvestAgain;
    }
}
