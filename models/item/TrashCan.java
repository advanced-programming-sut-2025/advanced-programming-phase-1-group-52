package models.item;

import enums.items.TrashCanType;

public class TrashCan extends Item{
    private TrashCanType trashCanType;
    private int percentage;
    public TrashCan(TrashCanType trashcanType, int number) {
        super(trashcanType, number);
        if (this.trashCanType.equals(TrashCanType.PrimitiveTrashCan)){
            this.percentage = 0;
        }
        else if (this.trashCanType.equals(TrashCanType.CopperTrashCan)){
            this.percentage = 15;
        }
        else if (this.trashCanType.equals(TrashCanType.IronicTrashCan)){
            this.percentage = 30;
        }
        else if (this.trashCanType.equals(TrashCanType.GoldenTrashCan)){
            this.percentage = 45;
        }
        else if (this.trashCanType.equals(TrashCanType.GoldenTrashCan)){
            this.percentage = 60;
        }
    }

    public TrashCanType getTrashCanType() {
        return trashCanType;
    }

    public void addItem(Item item, int amount) {}

    public void setTrashCanType(TrashCanType trashCanType) {
        this.trashCanType = trashCanType;
    }

    protected int calculateEnergyConsumption() {
        return 0;
    }

    public int getPercentage() {
        return percentage;
    }
}
