package enums.items;


public enum PlantType implements ItemType {
    ;

    @Override
    public boolean isTool() {
        return false;
    }
}
