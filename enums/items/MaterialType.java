package enums.items;


public enum MaterialType implements Items {
    
    Egg("Egg"),
    Milk("Milk"),
    Cheese("Cheese"),
    Sugar("Sugar"),
    Oil("Oil"),

    // Crops
    Wheat("Wheat"),
    WheatFlour("Wheat flour"),
    Pumpkin("Pumpkin"),
    Tomato("Tomato"),
    Corn("Corn"),
    Potato("Potato"),
    Blueberry("Blueberry"),
    Melon("Melon"),
    Apricot("Apricot"),
    RedCabbage("Red cabbage"),
    Radish("Radish"),
    Amaranth("Amaranth"),
    Kale("Kale"),
    Beet("Beet"),
    Parsnip("Parsnip"),
    Carrot("Carrot"),
    Eggplant("Eggplant"),
    Leek("Leek"),

    // Fish
    Sardine("Sardine"),
    Salmon("Salmon"),
    AnyFish("Any fish"),
    Flounder("Flounder"),
    MidnightCarp("Midnight carp"),

    // Foraged Items
    Dandelion("Dandelion"),
    Coffee("Coffee"),

    // Processed Items
    Rice("Rice"),
    Bread("Bread"),
    HashBrowns("Hash browns"),
    Omelet("Omelet"),

    // Handicrafts Items
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
    Acorn("Acorn"),
    MapleSeed("Maple seed"),
    PineCone("Pine cone"),
    MahoganySeed("Mahogany seed");

    private final String name;

    MaterialType(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return name;
    }
}
