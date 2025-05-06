package enums.items;

public enum Backpacks implements Items {
    PrimitiveBackpack(12),
    BigBackpack(24),
    DeluxeBackpack(Integer.MAX_VALUE);

    private int size;

    Backpacks(int size) {
        this.size = size;
    }
}
