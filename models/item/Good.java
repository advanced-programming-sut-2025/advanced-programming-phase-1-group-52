package models.item;

import enums.items.GoodType;
import enums.items.MaterialType;

public class Good extends Item{
    public Good(GoodType goodType) {
        super(goodType);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
