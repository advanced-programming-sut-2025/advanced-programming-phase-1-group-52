package enums.items;


public enum ProductType implements ItemType {
    ;

    @Override
    public boolean isTool() {
        return false;
    }
}
