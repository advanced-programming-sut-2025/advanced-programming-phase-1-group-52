package enums.design.Shop;

import enums.items.MaterialType;

public enum Blacksmith {
    // Stock items
    COPPER_ORE(ItemType.STOCK, "Copper Ore", "A common ore that can be smelted into bars.", 75, Integer.MAX_VALUE, MaterialType.CopperOre, 0),
    IRON_ORE(ItemType.STOCK, "Iron Ore", "A fairly common ore that can be smelted into bars.", 150, Integer.MAX_VALUE, MaterialType.IronOre, 0),
    COAL(ItemType.STOCK, "Coal", "A combustible rock that is useful for crafting and smelting.", 150, Integer.MAX_VALUE, MaterialType.Coal, 0),
    GOLD_ORE(ItemType.STOCK, "Gold Ore", "A precious ore that can be smelted into bars.", 400, Integer.MAX_VALUE, MaterialType.GoldOre, 0),
    IRIDIUM_ORE(ItemType.STOCK, "Iridium Ore", "A rare and valuable ore used for advanced crafts.", 1000, Integer.MAX_VALUE, MaterialType.IridiumOre, 0),

    // Upgrade items
    COPPER_TOOL(ItemType.UPGRADE, "Copper Tool", null, 0, 1, MaterialType.CopperBar, 2000),
    STEEL_TOOL(ItemType.UPGRADE, "Steel Tool", null, 0, 1, MaterialType.IronBar, 5000),
    GOLD_TOOL(ItemType.UPGRADE, "Gold Tool", null, 0, 1, MaterialType.GoldBar, 10000),
    IRIDIUM_TOOL(ItemType.UPGRADE, "Iridium Tool", null, 0, 1, MaterialType.IridiumBar, 25000),
    COPPER_TRASH_CAN(ItemType.UPGRADE, "Copper Trash Can", null, 0, 1, MaterialType.CopperBar, 1000),
    STEEL_TRASH_CAN(ItemType.UPGRADE, "Steel Trash Can", null, 0, 1, MaterialType.IronBar, 2500),
    GOLD_TRASH_CAN(ItemType.UPGRADE, "Gold Trash Can", null, 0, 1, MaterialType.GoldBar, 5000),
    IRIDIUM_TRASH_CAN(ItemType.UPGRADE, "Iridium Trash Can", null, 0, 1, MaterialType.IridiumBar, 12500);

    public enum ItemType {
        STOCK,
        UPGRADE
    }

    private final ItemType type;
    private final String displayName;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final MaterialType ingredient;
    private final int upgradeCost;

    Blacksmith(ItemType type, String displayName, String description, int price, int dailyLimit, MaterialType ingredient, int upgradeCost) {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.ingredient = ingredient;
        this.upgradeCost = upgradeCost;
    }

    public ItemType getType() { return type; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getDailyLimit() { return dailyLimit; }
    public MaterialType getIngredient() { return ingredient; }
    public int getUpgradeCost() { return upgradeCost; }
}
