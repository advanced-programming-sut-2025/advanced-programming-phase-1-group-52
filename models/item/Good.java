package models.item;

import enums.items.ArtisanProductType;
import enums.items.ItemType;

public class Good extends Item{
    ArtisanProductType productType;
    private boolean canBeUsed = false;

    public Good(ArtisanProductType artisanProductType, int number) {
        super(artisanProductType, number);
        this.productType = artisanProductType;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public ArtisanProductType getProductType() {
        return productType;
    }

    public boolean canBeUsed() {
        return canBeUsed;
    }

    public void setCanBeUsed(boolean canBeUsed) {
        this.canBeUsed = canBeUsed;
    }
}
