package enums.items;

public enum TrashCans {
    PrimitiveTrashCan(0),
    CopperTrashCan(15),
    IronicTrashCan(30),
    GoldenTrashCan(45),
    IridiumTrashCan(60);

    private int percentage;

    private TrashCans(int percentage) {
        this.percentage = percentage;
    }
}
