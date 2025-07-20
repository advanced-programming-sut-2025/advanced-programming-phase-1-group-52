package com.example.main.enums.items;

public enum Backpacks implements ItemType {
    PrimitiveBackpack("primitive backpack", 12),
    BigBackpack("big backpack", 24),
    DeluxeBackpack("deluxe backpack", Integer.MAX_VALUE);

    private final String name;
    private final int size;

    Backpacks(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public int getCapacity() {
        return size;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getEnumName() {
        return name();
    }
}
