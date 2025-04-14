package models.item;

import enums.items.Cages;

public class Animal extends Item {

    Cages cage;

    public Animal(Cages cage) {
        this.cage = cage;
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
