package models.item;

public class Good extends Item{

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
