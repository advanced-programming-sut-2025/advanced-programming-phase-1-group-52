package com.example.main.enums.items;

public enum CageType implements ItemType {
    NormalCage("normal coop", 4, 1),
    BigCage("big coop", 8, 2),
    DeluxeCage("deluxe coop", 12, 3),
    NormalBarn("normal barn", 4, 1),
    BigBarn("big barn", 8, 2),
    DeluxeBarn("deluxe barn", 12, 3);

    private final String name;
    private final int capacity;
    private final int level;

    CageType(String name, int capacity, int level) {
        this.name = name;
        this.capacity = capacity;
        this.level = level;
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
    
    public int getLevel() { return this.level; }

    @Override
    public String getEnumName() {
        return name();
    }

}

