package models;

import java.util.ArrayList;

public class Player {

    private final String username;
    private final Inventory inventory;
    private final ArrayList<Trade> trades;
    private int energy = 200;

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

    // todo: having private method for each tool functionality
}
