package models.item;

import enums.items.ForagingSeedType;

public class Seed extends Item{
    public Seed(ForagingSeedType foragingSeedType, int number) {
        super(foragingSeedType,number);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
