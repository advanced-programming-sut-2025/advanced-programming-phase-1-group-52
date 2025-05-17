package models.item;

import enums.items.TrashCanType;

public class TrashCan extends Item{
    private TrashCanType trashCanType;
    private int percentage;
    public TrashCan(TrashCanType trashcanType, int number) {
        super(trashcanType, number);
        switch (this.trashCanType) {
            case PrimitiveTrashCan:
                this.percentage = 0;
                break;
            case CopperTrashCan:
                this.percentage = 15;
                break;
            case IronicTrashCan:
                this.percentage = 30;
                break;
            case GoldenTrashCan:
                this.percentage = 45;
                break;
            case IridiumTrashCan:
                this.percentage = 60;
                break;
            default:
                break;
        }
    }

    public TrashCanType getTrashCanType() {
        return trashCanType;
    }

    public void addItem(Item item, int amount) {}

    public void setTrashCanType(TrashCanType trashCanType) {
        this.trashCanType = trashCanType;
    }
    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public int getPercentage() {
        return percentage;
    }
}
