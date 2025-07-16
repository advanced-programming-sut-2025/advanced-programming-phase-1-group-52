package com.example.main.enums.items;

public enum TrashCanType implements ItemType {
    Trash_Can("primitive trash can",0),
    Copper_Trash_Can("copper trash can",15),
    Steel_Trash_Can("iron trash can",30),
    Gold_Trash_Can("gold trash can",45),
    Iridium_Trash_Can("iridium trash can",60);


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
        return name();
    }

    @Override
    public String getEnumName() {
        return name();
    }
}
