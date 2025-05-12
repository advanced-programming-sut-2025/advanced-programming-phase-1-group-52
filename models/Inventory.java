package models;

import enums.items.Backpacks;
import models.item.Item;

import java.util.ArrayList;

public class Inventory {
    private Backpacks backpack = Backpacks.PrimitiveBackpack;
    private ArrayList<Item> items = new ArrayList<>();
    private int numOfItems = 0;

    public Inventory() {

    }

    public Backpacks getBackpack() {
        return backpack;
    }
    public void setBackpack(Backpacks backpack) {
        this.backpack = backpack;
    }
    public ArrayList<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    public void addItem(Item item) {
        items.add(item);
    }
    public int getNumOfItems() {
        return numOfItems;
    }
    public void setNumOfItems(int numOfItems) {
        this.numOfItems = numOfItems;
    }
    public void addNumOfItems(int numOfItems) {
        this.numOfItems += numOfItems;
    }
    public void removeItem(Item item) {
        items.remove(item);
    }
}
