package com.example.main.enums.items;


public enum MaterialType implements ItemType {

    Egg("egg"),
    Milk("milk"),
    Cheese("cheese"),
    Sugar("sugar"),
    Oil("oil"),

    // Crops
    Wheat("Wheat mat"),
    WheatFlour("Wheat flour mat"),
    Pumpkin("Pumpkin mat"),
    Tomato("Tomato mat"),
    Corn("Corn mat"),
    Potato("Potato mat"),
    Blueberry("Blueberry mat"),
    Melon("Melon mat"),
    Apricot("Apricot mat"),
    RedCabbage("Red cabbage mat"),
    Radish("Radish mat"),
    Amaranth("Amaranth mat"),
    Kale("Kale mat"),
    Beet("Beet mat"),
    Parsnip("Parsnip mat"),
    Carrot("Carrot mat"),
    Eggplant("Eggplant mat"),
    Leek("Leek mat"),

    // Fish
    Sardine("Sardine"),
    Salmon("Salmon"),
    AnyFish("Any fish"),
    Flounder("Flounder"),
    MidnightCarp("Midnight carp"),

    // Foraged Type
    Dandelion("Dandelion"),
    Coffee("Coffee"),

    // Processed Type
    Rice("Rice"),
    Bread("Bread mat"),
    HashBrowns("Hash browns mat"),
    Omelet("Omelet mat"),

    // Handicrafts Type
    CopperOre("Copper ore"),
    IronOre("Iron ore"),
    GoldOre("Gold ore"),
    IridiumOre("Iridium ore"),
    CopperBar("Copper bar"),
    IronBar("Iron bar"),
    GoldBar("Gold bar"),
    IridiumBar("Iridium bar"),
    Wood("Wood"),
    Stone("Stone"),
    Coal("Coal"),
    Fiber("Fiber"),
    Iron("Iron"),
    HardWood("Hard wood"),
    GoldCoin("Gold coin"),
    Diamond("Diamond"),

    // pierre's remainings
    Bouquet("Bouquet"),
    WeddingRing("Wedding Ring"),
    DehydratorRecipe("Dehydrator recipe"),
    GrassStarterRecipe("Grass Starter recipe"),
    Vinegar("Vinegar"),
    DeluxeRetainingSoil("Deluxe retaining Soil"),
    GrassStarter("Grass Starter"),
    SpeedGro("Speed-Gro"),
    BasicRetainingSoil("Basic retaining Soil"),
    QualityRetainingSoil("Quality-Retaining Soil"),

    // Joja's remainings
    JojaCola("Joja Cola"),

    // Carpenter's items,

    // Marine's Ranch items:
    Hay("Hay"),
    MilkPail("Milk Pail"),
    Shears("Shears"),

    // Fishing poles :
    TroutSoup( "Trout Soup"),
    BambooPole( "Bamboo Pole"),
    TrainingRod("Training Rod"),
    FiberglassRod("Fiberglass Rod"),
    IridiumRod("Iridium Rod");

    private final String name;

    MaterialType(String name) {
        this.name = name;
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
