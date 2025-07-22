package com.example.main.models;

import java.util.ArrayList;
import java.util.Iterator;

import com.example.main.enums.items.ItemType;
import com.example.main.models.item.Item;

public class HouseRefrigerator {
    ArrayList<Item> items;
    private final int capacity = 36; // Stardew Valley standard fridge size

    public HouseRefrigerator() {
        this.items = new ArrayList<>();
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public boolean isFull() {
        return items.size() >= capacity;
    }

    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public boolean putItem(Item newItem) {
        if (isFull() && findItemByType(newItem.getItemType()) == null) {
            return false;
        }

        for (Item item : items) {
            if (item.getItemType().equals(newItem.getItemType())) {
                item.setNumber(item.getNumber() + newItem.getNumber());
                return true;
            }
        }
        this.items.add(newItem);
        return true;
    }

    public Item findItemByType(ItemType itemType){
        for (Item item : items) {
            if (item.getItemType() == itemType) {
                return item;
            }
        }
        return null;
    }

    public boolean remove(ItemType itemType, int quantity) {
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()) {
            Item existing = iterator.next();
            if (existing.getItemType().equals(itemType)) {
                if (existing.getNumber() < quantity) {
                    return false; // Not enough to remove
                }
                if (existing.getNumber() == quantity) {
                    iterator.remove(); // Remove the whole stack
                } else {
                    existing.setNumber(existing.getNumber() - quantity);
                }
                return true;
            }
        }
        return false; // Item not found
    }
}
