package models.item;

import enums.items.ItemType;
import enums.items.ToolType;

public abstract class Item {
    private String name;
    private int number = 0;
    protected ItemType itemType;
    protected abstract int calculateEnergyConsumption();

    public Item(ItemType itemType) {
        this.name = itemType.toString();
        this.itemType = itemType;
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


}
