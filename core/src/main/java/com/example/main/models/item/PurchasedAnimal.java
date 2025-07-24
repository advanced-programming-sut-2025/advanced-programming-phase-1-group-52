package com.example.main.models.item;

import java.util.concurrent.ThreadLocalRandom;

import com.example.main.enums.design.Shop.MarniesRanch;
import com.example.main.enums.items.AnimalType;
import com.example.main.enums.items.ToolType;

public class PurchasedAnimal {
    private final AnimalType type;
    private final String name;
    private int friendshipPoints = 0;
    private boolean isInCage = true;
    private boolean isFull = false;
    private boolean wasFull = false;
    private boolean productCollected = false;
    private int x;
    private int y;
    
    private int targetX;
    private int targetY;
    private boolean isMoving = false;
    private float moveProgress = 0f;
    public static final float MOVE_SPEED = 2.0f; 
    private long lastMoveTime = 0;
    public static final long MOVE_INTERVAL = 3000; 

    public PurchasedAnimal(AnimalType type, String name, int x, int y) {
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public boolean getCollected() {
        return this.productCollected;
    }

    public void setCollected(boolean x) {
        this.productCollected = x;
    }

    public boolean getWasFull() {
        return this.wasFull;
    }

    public void setWasFull(boolean wasFull) {
        this.wasFull = wasFull;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getTargetX() {
        return targetX;
    }
    
    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }
    
    public int getTargetY() {
        return targetY;
    }
    
    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    public void setMoving(boolean moving) {
        isMoving = moving;
    }
    
    public float getMoveProgress() {
        return moveProgress;
    }
    
    public void setMoveProgress(float moveProgress) {
        this.moveProgress = moveProgress;
    }
    
    public long getLastMoveTime() {
        return lastMoveTime;
    }
    
    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public boolean checkFriendshipPoints () {
        return friendshipPoints >= 100;
    }

    public boolean getProducedProductType () {
        double rnd1 = 0.05 + ThreadLocalRandom.current().nextDouble() * (1.5 - 0.05);
        double chanceForSecondary = (friendshipPoints + 150 * rnd1) / 1500.0;
        return chanceForSecondary > 0.2;
    }

    public String quantityProduced () {
        double R = 1.0 + ThreadLocalRandom.current().nextDouble() * 9.0;
        double quantity = (friendshipPoints/1000.0) * (0.5 + 0.5 * R);
        if (quantity <= 0.5) {
            return "normal";
        } else if (quantity <= 0.7) {
            return "silver";
        } else if (quantity <= 0.9) {
            return "gold";
        } else {
            return "iridium";
        }
    }

    public double getPriceMultiplier() {
        return switch (quantityProduced()) {
            case "silver" -> 1.25;
            case "gold" -> 1.5;
            case "iridium" -> 2.0;
            default -> 1.0;
        };
    }

    public boolean isToolInInventory (ToolType toolType) {
        for (ToolType t : ToolType.values()) {
            if (t.equals(toolType)) {
                return true;
            }
        }
        return false;
    }

    public double sellingPrice () {
        MarniesRanch shopEntry = MarniesRanch.forAnimal(this.type);
        int basePrice = shopEntry.getPrice();
        return basePrice * ((friendshipPoints/1000.0) + 0.3) * getPriceMultiplier();
    }

    public AnimalType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getFriendshipPoints() {
        return friendshipPoints;
    }
    public void setFriendshipPoints(int friendshipPoints) {
        this.friendshipPoints = friendshipPoints;
    }
    public boolean isInCage() {
        return isInCage;
    }
    public void setInCage(boolean isInCage) {
        this.isInCage = isInCage;
    }
    public boolean isFull() {
        return isFull;
    }
    public void setFull(boolean isFull) {
        this.isFull = isFull;
    }

    public void addFriendshipPoints(int amount) {
        if (this.friendshipPoints + amount > 1000) this.friendshipPoints = 1000;
        else this.friendshipPoints += amount;
    }

    @Override
    public String toString() {
        return this.name + "\n-----------------\nType: " + this.type + 
        "\nFriendship points: " + this.friendshipPoints + 
        "\nis Full: " + this.isFull + "\nin Housing: " + 
        this.isInCage + "\nCollected: " + this.productCollected + "\n";
    }
}
