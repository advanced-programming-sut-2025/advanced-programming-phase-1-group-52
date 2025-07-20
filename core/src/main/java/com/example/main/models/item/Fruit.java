package com.example.main.models.item;

import com.example.main.enums.items.FruitType;
import com.example.main.enums.items.Growable;
import com.example.main.enums.items.TreeType;

public class Fruit extends Item implements Growable {
    private FruitType fruitType;
    private TreeType treeType;

    private int currentStage = 0;
    private int dayPassed = 0;
    private int dayRemaining = 0;
    private boolean isWateredToday = false;
    private boolean isFertilizedToday = true;
    private boolean needsWaterToday = false;
    private boolean isReadyToHarvest = false;
    private int unwateredDays = 0;
    private boolean hasBeenHarvestedToday = false; // Add this flag

    public Fruit(FruitType fruitType, int number) {
        super(fruitType, number);
        this.fruitType = fruitType;
        this.treeType = findTreeType();
    }

    // Add getter and setter for the new flag
    public boolean hasBeenHarvestedToday() {
        return hasBeenHarvestedToday;
    }

    public void setHasBeenHarvestedToday(boolean hasBeenHarvestedToday) {
        this.hasBeenHarvestedToday = hasBeenHarvestedToday;
    }

    public int getUnwateredDays() {
        return unwateredDays;
    }

    public void setUnwateredDays(int unwateredDays) {
        this.unwateredDays = unwateredDays;
    }

    public void incrementUnwateredDays() {
        this.unwateredDays++;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    @Override
    public void growFaster() {
        this.dayRemaining -= 1;
    }

    @Override
    public boolean isNotWateredForTwoDays() {
        return this.unwateredDays >= 2;
    }

    @Override
    public void setNotWateredForTwoDays(boolean notWateredForTwoDays) {
        // This method is less relevant now with the unwateredDays counter,
        // but we keep it to satisfy the Growable interface.
    }

    private TreeType findTreeType() {
        for (TreeType treeType : TreeType.values()) {
            if (treeType.getProduct().equals(fruitType)) {
                return treeType;
            }
        }
        return null;
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

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    public void setTreeType(TreeType treeType) {
        this.treeType = treeType;
    }
}
