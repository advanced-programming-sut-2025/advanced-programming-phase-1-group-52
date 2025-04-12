package enums.items;


public enum Materials implements Items {
    
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
    Fiber("Fiber"),
    Coffee("Coffee"),

    // Processed Items
    Rice("Rice"),
    Bread("Bread"),
    HashBrowns("Hash browns"),
    Omelet("Omelet");

    private final String name;

    Materials(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return name;
    }
}
