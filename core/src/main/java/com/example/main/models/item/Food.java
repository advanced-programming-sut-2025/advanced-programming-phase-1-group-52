package com.example.main.models.item;

import com.example.main.enums.items.FoodType;

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
