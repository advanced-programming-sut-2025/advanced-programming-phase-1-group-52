package com.example.main.enums.items;

public enum TrashCanType implements ItemType {
    PrimitiveTrashCan("primitive trash can",0),
    CopperTrashCan("copper trash can",15),
    IronicTrashCan("ironic trash can",30),
    GoldenTrashCan("golden trash can",45),
    IridiumTrashCan("iridium trash can",60);

    private final String name;
    private int percentage;

    private TrashCanType(String name, int percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
