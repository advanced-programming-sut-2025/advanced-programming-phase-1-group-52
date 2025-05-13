package models.item;

import enums.items.*;

public class Fruit extends Item implements Growable {
    private FruitType fruitType;
    private TreeType treeType;
    private int dayPassed = 0;
    private int dayRemaining = 0;
    private boolean isWatered = false;
    private boolean isFertilized = false;
    private int currentStage = 0;

    public Fruit(FruitType fruitType, int number) {
        super(fruitType, number);
        this.fruitType = fruitType;
        this.treeType = findTreeType();
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public FruitType getFruitType() {
        return fruitType;
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

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public void setDayPassed(int dayPassed) {
        this.dayPassed = dayPassed;
    }

    public void setDayRemaining(int dayRemaining) {
        this.dayRemaining = dayRemaining;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public void setWatered(boolean watered) {
        isWatered = watered;
    }

    public void setFertilized(boolean fertilized) {
        isFertilized = fertilized;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    private TreeType findTreeType() {
        for(TreeType treeType : TreeType.values()) {
            if(treeType.getProduct().equals(fruitType)) {
                return treeType;
            }
        }
        return null;
    }

}
