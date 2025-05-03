package enums.items;

public enum Backpacks implements Items {
    PrimitiveBackpack(12),
    BigBackpack(24),
    DeluxeBackpack(Integer.MAX_VALUE);

    private int value;

    Backpacks(int value) {
        this.value = value;
    }
}
