package com.example.main.models.item;

import com.example.main.enums.items.ItemType;

public class CraftingMachine extends Item{
    ItemType productType;

    public CraftingMachine(ItemType itemType, int number) {
        super(itemType, number);
        this.productType = itemType;
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
