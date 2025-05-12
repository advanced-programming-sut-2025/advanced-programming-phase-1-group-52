package enums.items;


public enum MineralType implements ItemType {
    CopperStone,
    IronStone,
    GoldStone,
    IridiumStone,
    JewelStone,
    NormalStone;

    MineralType() {
    }

    @Override
    public boolean isTool() {
        return false;
    }
}
