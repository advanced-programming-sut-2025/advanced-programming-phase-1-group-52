package models.item;

import enums.items.ItemType;
import enums.items.MaterialType;

public abstract class Item {
    private String name;
    private int number;
    protected ItemType itemType;
    protected abstract int calculateEnergyConsumption();

    public Item(ItemType itemType, int number) {
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
