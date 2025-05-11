package models;

import enums.items.Backpacks;
import models.item.Item;

import java.util.ArrayList;

public class Inventory {
    private Backpacks backpack = Backpacks.PrimitiveBackpack;
    private ArrayList<Item> items = new ArrayList<>();

    private int getTotalItemCount() {
        return items.stream().mapToInt(Item::number).sum();
    }

    public boolean addItem(Item newItem) {
        int total = getTotalItemCount();
        if (total + newItem.number() > backpack.getCapacity()) return false;
        for (Item item : items) {
            if (item.getClass().equals(newItem.getClass())) {
                item.setNumber(item.number() + newItem.number());
                return true;
            }
        }
        items.add(newItem);
        return true;
    }

    public boolean removeItem(Class<? extends Item> itemClass, int quantity) {
        for (Item item : items) {
            if (item.getClass().equals(itemClass)) {
                if (item.number() < quantity) return false;

                if (item.number() == quantity) {
                    items.remove(item);
                } else {
                    item.setNumber(item.number() - quantity);
                }
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(Class<? extends Item> itemClass, int quantity) {
        for (Item item : items) {
            if (item.getClass().equals(itemClass)) {
                return item.number() >= quantity;
            }
        }
        return false;
    }

    public ArrayList<Item> getItems() {
        return new ArrayList<>(items);
    }

    public Backpacks getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpacks backpack) {
        this.backpack = backpack;
    }
}
