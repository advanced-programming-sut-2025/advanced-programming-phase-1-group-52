package com.example.main.enums.items;

public enum CraftingMachineType implements ItemType {
    // Corrected names to match PNG files
    Cherry_Bomb("Cherry Bomb", 50),
    Bomb("Bomb", 50),
    Mega_Bomb("Mega Bomb", 50),
    Sprinkler("Sprinkler", null),
    Quality_Sprinkler("Quality Sprinkler", null),
    Iridium_Sprinkler("Iridium Sprinkler", null),
    Charcoal_Kiln("Charcoal Kiln", null),
    Furnace("Furnace", null),
    Scarecrow("Scarecrow", null),
    Deluxe_Scarecrow("Deluxe Scarecrow", null),
    Bee_House("Bee House", null),
    Cheese_Press("Cheese Press", null),
    Keg("Keg", null),
    Loom("Loom", null),
    Mayonnaise_Machine("Mayonnaise Machine", null),
    Oil_Maker("Oil Maker", null),
    Preserves_Jar("Preserves Jar", null),
    Dehydrator("Dehydrator", null),
    Grass_Starter("Grass Starter", null),
    Fish_Smoker("Fish Smoker", null),
    Mystic_Tree_Seed("Mystic Tree Seed", 100);

    private final String productName;
    private final Integer price;

    CraftingMachineType(String productName, Integer price) {
        this.productName = productName;
        this.price = price;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.productName;
    }

    @Override
    public String getEnumName() {
        return name();
    }

    public Integer getPrice() {
        return this.price;
    }
}
