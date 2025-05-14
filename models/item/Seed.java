package models.item;

import enums.items.ForagingSeedType;
import enums.items.Growable;

public class Seed extends Item {
    private ForagingSeedType foragingSeedType;
    public Seed(ForagingSeedType foragingSeedType, int number) {
        super(foragingSeedType,number);
        this.foragingSeedType = foragingSeedType;
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public ForagingSeedType getForagingSeedType() {
        return foragingSeedType;
    }
}
