package com.example.main.models;

import com.example.main.enums.items.TreeType;
import com.example.main.enums.items.Growable;

public class Tree implements Growable {
    TreeType type;

    private int dayPassed;
    private int dayRemaining;
    private boolean wateredToday;
    private boolean fertilizedToday;
    private boolean needsWaterToday;
    private int currentStage;
    private boolean readyToHarvest;
    private boolean notWateredForTwoDays;


    public Tree(TreeType type) {
        this.type = type;
        this.dayPassed = 0;
        this.dayRemaining = type.getTotalHarvestTime();
        this.wateredToday = false;
        this.fertilizedToday = false;
        this.needsWaterToday = true;
        this.currentStage = 0;
        this.readyToHarvest = false;
        this.notWateredForTwoDays = false;

    }

    public TreeType getType() {
        return type;
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
        return wateredToday;
    }

    @Override
    public void setWateredToday(boolean wateredToday) {
        this.wateredToday = wateredToday;
    }

    @Override
    public boolean isFertilizedToday() {
        return fertilizedToday;
    }

    @Override
    public void setFertilizedToday(boolean fertilizedToday) {
        this.fertilizedToday = fertilizedToday;
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
        return readyToHarvest;
    }

    @Override
    public void setReadyToHarvest(boolean readyToHarvest) {
        this.readyToHarvest = readyToHarvest;
    }

    @Override
    public void growFaster() {
        // Logic for growing faster, if applicable for trees
    }

    @Override
    public boolean isNotWateredForTwoDays() {
        return notWateredForTwoDays;
    }

    @Override
    public void setNotWateredForTwoDays(boolean notWateredForTwoDays) {
        this.notWateredForTwoDays = notWateredForTwoDays;
    }

    // Additional methods not from Growable, if any
    public String getSeason() {
        if (!type.getSeasons().isEmpty()) {
            return type.getSeasons().get(0).name();
        }
        return "";
    }

    public int getHarvestTime() {
        return type.getTotalHarvestTime();
    }

    public String getName() {
        return type.getName();
    }

    public boolean canBeHarvestAgain() {
        return true; // Trees can be harvested multiple times
    }

    public void setCanBeHarvestAgain(boolean canBeHarvestAgain) {
        // Not typically used for trees, but implemented for interface consistency
    }

    public void incrementDayPassed() {
        this.dayPassed++;
        this.dayRemaining--;
        // Additional logic for stage progression, etc.
    }
}
