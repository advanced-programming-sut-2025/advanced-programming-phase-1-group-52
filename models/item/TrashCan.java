package models.item;

import enums.items.Items;
import enums.items.TrashCans;

public class TrashCan extends Item {
    private TrashCans trashCanType;

    public TrashCans trashCanType() {
        return trashCanType;
    }

    public void setTrashCanType(TrashCans trashCanType) {
        this.trashCanType = trashCanType;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}
