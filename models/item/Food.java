package models.item;

import enums.items.FoodType;
import enums.items.MaterialType;

public class Food extends Item {
    public Food(FoodType foodType, int number) {
        super(foodType, number);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
