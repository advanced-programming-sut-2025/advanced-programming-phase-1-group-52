package com.example.main.enums.items;


public enum MineralType implements ItemType {
    Quartz("quartz", "A clear crystal commonly found in caves and mines.", 25),
    Earth_Crystal("earth crystal", "A resinous substance found near the surface.", 50),
    Frozen_Tear("frozen tear", "A crystal fabled to be the frozen tears of a yeti.", 75),
    Fire_Quartz("fire quartz", "A glowing red crystal commonly found near hot lava.", 100),
    Emerald("emerald", "A precious stone with a brilliant green color.", 250),
    Aquamarine("aquamarine", "A shimmery blue-green gem.", 180),
    Ruby("ruby", "A precious stone that is sought after for its rich color and beautiful luster.", 250),
    Amethyst("amethyst", "A purple variant of quartz.", 100),
    Topaz("topaz", "Fairly common but still prized for its beauty.", 80),
    Jade("jade", "A pale green ornamental stone.", 200),
    Diamond("diamond", "A rare and valuable gem.", 750),
    Prismatic_Shard("prismatic shard", "A very rare and powerful substance with unknown origins.", 2000),
    Copper("copper ore", "A common ore that can be smelted into bars.", 5),
    Iron("iron ore", "A fairly common ore that can be smelted into bars.", 10),
    Gold("gold ore", "A precious ore that can be smelted into bars.", 25),
    Iridium("iridium ore", "An exotic ore with many curious properties. Can be smelted into bars.", 100),
    Coal("coal", "A combustible rock that is useful for crafting and smelting.", 15),
    Copper_Ore("Copper ore", ".", 20),
    Iron_Ore("Iron ore", ".", 20),
    Gold_Ore("Gold ore", ".", 20),
    Iridium_Ore("Iridium ore", ".", 20),
    Copper_Bar("Copper bar", ".", 20),
    Iron_Bar("Iron bar", ".", 20),
    Gold_Bar("Gold bar", ".", 20),
    Iridium_Bar("Iridium bar", ".", 20);

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

    @Override
    public String getEnumName() {
        return name();
    }
}
