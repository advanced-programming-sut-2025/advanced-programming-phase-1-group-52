package models.item;

import enums.items.Items;

public abstract class Item {
    private String name;
    private int number = 0;
    protected Items itemType;
    protected abstract int calculateEnergyConsumption();

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
}
