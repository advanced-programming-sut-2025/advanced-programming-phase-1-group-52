package models.item;

import enums.design.ArtisanMachineProductType;
import enums.items.ItemType;

public class ArtisanMachineProduct extends Item{
    ItemType productType;

    public ArtisanMachineProduct(ItemType itemType, int number) {
        super(itemType, number);
        this.productType = itemType;
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
