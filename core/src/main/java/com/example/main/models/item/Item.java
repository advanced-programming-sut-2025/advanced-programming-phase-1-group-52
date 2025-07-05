package com.example.main.models.item;

import com.example.main.enums.items.ItemType;

public abstract class Item {
    private String name;
    private int number;
    protected ItemType itemType;
    protected abstract int calculateEnergyConsumption();

    public Item(ItemType itemType, int number) {
        if (itemType == null) {
            throw new IllegalArgumentException("ItemType cannot be null when creating Item");
        }
        this.name = itemType.getName();
        this.itemType = itemType;
        this.number = number;
    }

    public boolean isTool() {
        return itemType.isTool();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean remove(int quantity) {
        if (quantity <= 0) return false;
        if (this.number < quantity) return false;


        this.number -= quantity;
        return true;
    }
}
