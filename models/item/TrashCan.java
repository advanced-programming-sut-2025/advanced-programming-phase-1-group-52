package models.item;

import enums.items.TrashCans;

public class TrashCan {
    private TrashCans trashCanType;

    public TrashCans trashCanType() {
        return trashCanType;
    }

    public void addItem(Item item, int amount) {}

    public void setTrashCanType(TrashCans trashCanType) {
        this.trashCanType = trashCanType;
    }

    protected int calculateEnergyConsumption() {
        return 0;
    }
}
