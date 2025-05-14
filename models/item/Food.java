package models.item;

import enums.items.FoodType;

public class Food extends Item {
    public Food(FoodType foodType, int number) {
        super(foodType, number);
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public FoodType getFoodType() {
        return (FoodType) itemType;
    }
}
