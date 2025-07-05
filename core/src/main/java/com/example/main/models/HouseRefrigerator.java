package com.example.main.models;

import java.util.ArrayList;

import com.example.main.models.item.Item;

public class HouseRefrigerator {
    ArrayList<Item> items;
    private int capacity = 20;
    private int filled = 0;

    public HouseRefrigerator() {
        this.items = new ArrayList<>();
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    public boolean putItem(Item newItem) {
        if(filled > capacity){
            return false;
        }
        for (Item item : items) {
            if(item.getItemType().equals(newItem.getItemType()) || item.getName().equals(newItem.getName())) {
                item.setNumber(item.getNumber() + newItem.getNumber());
                return true;
            }
        }
        this.items.add(newItem);
        this.filled += 1;
        return true;
    }

    public Item pickItem(String targetItemName) {
        Item newItem = null;
        for (Item item : items) {
            if(item.getName().equals(targetItemName)) {
                newItem = item;
            }
        }
        items.remove(newItem);
        this.filled -= 1;
        return newItem;
    }

}
