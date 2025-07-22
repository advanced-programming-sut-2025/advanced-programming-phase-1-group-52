package com.example.main.enums.items;

public enum CageType implements ItemType {
    NormalCage("normal cage", 4),
    BigCage("big cage", 8),
    DeluxeCage("deluxe cage", 12),
    NormalBarn("normal barn", 4),
    BigBarn("big barn", 8),
    DeluxeBarn("deluxe barn", 12);

    private final String name;
    private final int capacity;

    CageType(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getCapacity() { return this.capacity; }

    @Override
    public String getEnumName() {
        return name();
    }

}

