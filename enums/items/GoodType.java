package enums.items;

public enum GoodType implements ItemType {
    ;

    @Override
    public boolean isTool() {
        return false;
    }
}
