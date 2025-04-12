package models.item;

public class Food extends Item {
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
