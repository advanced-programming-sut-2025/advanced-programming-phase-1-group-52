package models.item;

import enums.items.GoodType;

public class Good extends Item{
    public Good(GoodType goodType, int number) {
        super(goodType, number);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public GoodType getGoodType() {
        return (GoodType) itemType;
    }
}
