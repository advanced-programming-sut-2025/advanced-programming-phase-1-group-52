package models;

import enums.items.Backpacks;
import java.util.ArrayList;
import models.item.Item;

public class Inventory {
    private Backpacks backpack = Backpacks.PrimitiveBackpack;
    private final ArrayList<Item> items = new ArrayList<>();

    public Inventory() {
    }

    private int getTotalItemCount() {
        return items.stream()
                .mapToInt(Item::getNumber)
                .sum();
    }

    public boolean addItem(Item newItem) {
        int total = getTotalItemCount();
        if (total + newItem.getNumber() > backpack.getCapacity()) {
            return false;
        }
        for (Item existing : items) {
            if (existing.getClass().equals(newItem.getClass())) {
                existing.setNumber(existing.getNumber() + newItem.getNumber());
                return true;
            }
        }
        items.add(newItem);
        return true;
    }

    public boolean removeItem(Class<? extends Item> itemClass, int quantity) {
        for (Item existing : items) {
            if (existing.getClass().equals(itemClass)) {
                if (existing.getNumber() < quantity) {
                    return false;
                }
                if (existing.getNumber() == quantity) {
                    items.remove(existing);
                } else {
                    existing.setNumber(existing.getNumber() - quantity);
                }
                return true;
            }
        }
        return false;
    }

    public boolean hasItem(Class<? extends Item> itemClass, int quantity) {
        return items.stream()
                .filter(i -> i.getClass().equals(itemClass))
                .anyMatch(i -> i.getNumber() >= quantity);
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
