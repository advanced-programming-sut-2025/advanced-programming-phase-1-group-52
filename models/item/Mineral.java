package models.item;

import enums.items.MineralType;

public class Mineral extends Item {
    public Mineral(MineralType mineral,int number) {
        super(mineral,number);
    }
    @Override
    protected int calculateEnergyConsumption()  {
        return 0;
    }

    public MineralType getMineralType() {
        return (MineralType) itemType;
    }
}
