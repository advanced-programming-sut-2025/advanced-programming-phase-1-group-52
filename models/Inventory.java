package models;

import enums.items.Backpacks;
import enums.items.ItemType;
import enums.items.MaterialType;
import java.util.ArrayList;
import models.item.Item;
import models.item.Tool;

public class Inventory {
    private Backpacks backpack = Backpacks.PrimitiveBackpack;
    private ArrayList<Item> items = new ArrayList<>();
    private int numOfItems = 0;
    private boolean isFull = false;

    public Inventory() {
    }

    public Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }   
        return null;
    }

    public Tool getToolByName(String name) {
        for (Item item : items) {
            if (item instanceof Tool && item.getName().equals(name)) return (Tool) item;
        }
        return null;
    }

    private int getTotalItemCount() {
        return items.stream()
                .mapToInt(Item::getNumber)
                .sum();
    }

    public boolean addItem(Item newItem) {
        for (Item item : items) {
            if(item.getItemType().equals(newItem.getItemType()) || item.getName().equals(newItem.getName())) {
                item.setNumber(item.getNumber() + newItem.getNumber());
                return true;
            }
            if(this.isFull){
                return false;
            }
            if(this.numOfItems >= backpack.getCapacity()) {
                this.isFull = true;
                return false;
            }
        }
        this.items.add(newItem);
        this.numOfItems += 1;
        return false;
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

    public boolean addNumOfItems(int num) {
        this.numOfItems += num;
        if(this.numOfItems > backpack.getCapacity()) {
            this.numOfItems -= 1;
            this.isFull = true;
            return false;
        }
        this.isFull = false;
        return true;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Backpacks getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpacks backpack) {
        this.backpack = backpack;
    }

    public void setNumOfItems(int numOfItems) {
        this.numOfItems = numOfItems;
    }

    public int getNumOfItems() {
        return numOfItems;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public Tool getTool(String toolName) {
        for (Item item : items) {
            if (item.getName().equals(toolName)) {
                return (Tool) item;
            }
        }
        return null;
    }


    public int getCount(MaterialType type) {
        Item item = getItemByName(type.name());
        return item == null ? 0 : item.getNumber();
    }

    public boolean remove(MaterialType type, int quantity) {
        for (Item item : items) {
            if (item.getItemType() == type) {
                boolean success = item.remove(quantity);
                if (success && item.getNumber() == 0) {
                    items.remove(item);
                }
                return success;
            }
        }
        return false;
    }

    public Item findItemByType(ItemType itemType){
        for (Item item : items) {
            if (item.getItemType() == itemType) {
                return item;
            }
        }
        return null;
    }
}
