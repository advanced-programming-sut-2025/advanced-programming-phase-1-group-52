package models.item;

import enums.items.Tools;

public abstract class Item {
    String name;
    protected abstract int calculateEnergyConsumption();
}
