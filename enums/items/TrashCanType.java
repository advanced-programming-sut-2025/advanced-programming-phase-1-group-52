package enums.items;

public enum TrashCanType implements ItemType {
    PrimitiveTrashCan(0),
    CopperTrashCan(15),
    IronicTrashCan(30),
    GoldenTrashCan(45),
    IridiumTrashCan(60);

    private int percentage;

    private TrashCanType(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean isTool() {
        return false;
    }
}
