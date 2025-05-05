package models;

import java.util.ArrayList;

public class Player {

    private final String username;
    private final Inventory inventory;
    private final ArrayList<Trade> trades;
    private int energy = 200;
    private int originX;
    private int originY;
    private int currentX;
    private int currentY;
    private boolean isFainted = false;

    public Player(String username) {
        this.username = username;
        this.inventory = new Inventory();
        this.trades = new ArrayList<>();
    }

    public int energy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public ArrayList<Trade> trades() {
        return trades;
    }

    public Inventory inventory() {
        return inventory;
    }

    public String username() {
        return username;
    }

    public void hoeHandler(){}
    public void shearHandler(){}
    public void pickaxeHandler(){}
    public void axeHandler(){}
    public void wateringCanHandler(){}
    public void fishingPoleHandler(){}
    public void seytheHandler(){}
    public void milkPaleHandler(){}
    public void backpackHandler(){}
    public void trashCanHandler(){}

    public int currentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int currentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = Player.this.currentX;
    }

    public boolean checkFaint (){
        return this.energy <= 0;
    }

    public boolean isFainted() {
        return isFainted;
    }

    public void setFainted(boolean fainted) {
        isFainted = fainted;
    }

    public int originY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public int originX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }


    // todo: having private method for each tool functionality
}
