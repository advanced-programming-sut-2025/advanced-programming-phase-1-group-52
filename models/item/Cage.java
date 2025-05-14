package models.item;

import enums.items.Cages;
import enums.items.ItemType;

public class Cage extends Item {
    public Cage(Cages cageType, int quantity) {
        super(cageType, quantity);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
