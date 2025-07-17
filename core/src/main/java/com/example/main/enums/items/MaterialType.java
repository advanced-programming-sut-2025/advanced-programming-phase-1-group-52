package com.example.main.enums.items;


public enum MaterialType implements ItemType {

    Egg("egg"),
    Milk("milk"),
    Cheese("cheese"),
    Sugar("sugar"),
    Oil("oil"),

    // Crops
    Wheat("Wheat mat"),
    Wheat_Flour("Wheat flour mat"),
    Pumpkin("Pumpkin mat"),
    Tomato("Tomato mat"),
    Corn("Corn mat"),
    Potato("Potato mat"),
    Blueberry("Blueberry mat"),
    Melon("Melon mat"),
    Apricot("Apricot mat"),
    Red_Cabbage("Red cabbage mat"),
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
    Midnight_Carp("Midnight carp"),

    // Foraged Type
    Dandelion("Dandelion"),
    Coffee("Coffee"),

    // Processed Type
    Rice("Rice"),
    Bread("Bread mat"),
    Hashbrowns("Hash browns mat"),
    Omelet("Omelet mat"),

    // Handicrafts Type
    Copper_Ore("Copper ore"),
    Iron_Ore("Iron ore"),
    Gold_Ore("Gold ore"),
    Iridium_Ore("Iridium ore"),
    Copper_Bar("Copper bar"),
    Iron_Bar("Iron bar"),
    Gold_Bar("Gold bar"),
    Iridium_Bar("Iridium bar"),
    Wood("Wood"),
    Stone("Stone"),
    Coal("Coal"),
    Fiber("Fiber"),
    Iron("Iron"),
    Hardwood("Hard wood"),
    Gold_Coin("Gold coin"),
    Diamond("Diamond"),

    // pierre's remainings
    Bouquet("Bouquet"),
    Wedding_Ring("Wedding Ring"),
    Dehydrator_Recipe("Dehydrator recipe"),
    Grass_Starter_Recipe("Grass Starter recipe"),
    Vinegar("Vinegar"),
    Deluxe_Retaining_Soil("Deluxe retaining Soil"),
    Grass_Starter("Grass Starter"),
    Speed_Gro("Speed-Gro"),
    Basic_Retaining_Soil("Basic retaining Soil"),
    Quality_Retaining_Soil("Quality-Retaining Soil"),

    // Joja's remainings
    Joja_Cola("Joja Cola"),

    // Carpenter's items,

    // Marine's Ranch items:
    Hay("Hay"),

    //todo: what is this :|
    Trout_Soup( "Trout Soup");

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
