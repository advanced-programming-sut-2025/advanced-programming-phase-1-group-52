package com.example.main.enums.items;


public enum MineralType implements ItemType {
    QUARTZ("quartz", "A clear crystal commonly found in caves and mines.", 25),
    EARTH_CRYSTAL("earth crystal", "A resinous substance found near the surface.", 50),
    FROZEN_TEAR("frozen tear", "A crystal fabled to be the frozen tears of a yeti.", 75),
    FIRE_QUARTZ("fire quartz", "A glowing red crystal commonly found near hot lava.", 100),
    EMERALD("emerald", "A precious stone with a brilliant green color.", 250),
    AQUAMARINE("aquamarine", "A shimmery blue-green gem.", 180),
    RUBY("ruby", "A precious stone that is sought after for its rich color and beautiful luster.", 250),
    AMETHYST("amethyst", "A purple variant of quartz.", 100),
    TOPAZ("topaz", "Fairly common but still prized for its beauty.", 80),
    JADE("jade", "A pale green ornamental stone.", 200),
    DIAMOND("diamond", "A rare and valuable gem.", 750),
    PRISMATIC_SHARD("prismatic shard", "A very rare and powerful substance with unknown origins.", 2000),
    COPPER("copper ore", "A common ore that can be smelted into bars.", 5),
    IRON("iron ore", "A fairly common ore that can be smelted into bars.", 10),
    GOLD("gold ore", "A precious ore that can be smelted into bars.", 25),
    IRIDIUM("iridium ore", "An exotic ore with many curious properties. Can be smelted into bars.", 100),
    COAL("coal", "A combustible rock that is useful for crafting and smelting.", 15);

    private final String name;
    public final String description;
    public final int sellPrice;

    MineralType(String name, String description, int sellPrice) {
        this.name = name;
        this.description = description;
        this.sellPrice = sellPrice;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.sellPrice;
    }
}
