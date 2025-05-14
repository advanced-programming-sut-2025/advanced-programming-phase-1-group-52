package enums.design.Shop;

import enums.design.ShopType;
import enums.items.MaterialType;

import java.util.HashMap;
import java.util.Map;

public enum Blacksmith implements ShopEntry{
    // Stock items
    CopperOre(ItemType.STOCK, "Copper Ore", "A common ore that can be smelted into bars.",
            75, MaterialType.CopperOre, Integer.MAX_VALUE,0, null),
    IronOre(ItemType.STOCK, "Iron Ore", "A fairly common ore that can be smelted into bars.",
            150, MaterialType.IronOre, Integer.MAX_VALUE, 0, null),
    Coal(ItemType.STOCK, "Coal", "A combustible rock that is useful for crafting and smelting.",
            150, MaterialType.Coal, Integer.MAX_VALUE, 0, null),
    GoldOre(ItemType.STOCK, "Gold Ore", "A precious ore that can be smelted into bars.",
            400, MaterialType.GoldOre, Integer.MAX_VALUE, 0, null),
    IridiumOre(ItemType.STOCK, "Iridium Ore", "A rare and valuable ore used for advanced crafts.",
            1000, MaterialType.IridiumOre, Integer.MAX_VALUE, 0, null),

    // Upgrade items
    CopperHoes(ItemType.UPGRADE,"Copper Hoe", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),
    CopperPickaxe(ItemType.UPGRADE,"Copper Pickaxe", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),
    CopperAxe(ItemType.UPGRADE,"Copper Axe", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),
    CopperWaterCan(ItemType.UPGRADE,"Copper Watering Can", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),

    SteelHoes(ItemType.UPGRADE,"Steel Hoe", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),
    SteelPickaxe(ItemType.UPGRADE,"Steel Pickaxe", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),
    SteelAxe(ItemType.UPGRADE,"Steel Axe", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),
    SteelWaterCan(ItemType.UPGRADE,"Steel Watering Can", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),

    GoldHoes(ItemType.UPGRADE,"Gold Hoe", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),
    GoldPickaxe(ItemType.UPGRADE,"Gold Pickaxe", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),
    GoldAxe(ItemType.UPGRADE,"Gold Axe", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),
    GoldWaterCan(ItemType.UPGRADE,"Gold Watering Can", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),

    IridiumHoes(ItemType.UPGRADE,"Iridium Hoe", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),
    IridiumPickaxe(ItemType.UPGRADE,"Iridium Pickaxe", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),
    IridiumAxe(ItemType.UPGRADE,"Iridium Axe", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),
    IridiumWaterCan(ItemType.UPGRADE,"Iridium Watering Can", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),

    CopperTrashCan(ItemType.UPGRADE,"Copper Trash Can", null, null,
            null, 1, 1000, createIngredients(MaterialType.CopperBar, 5)),
    SteelTrashCan(ItemType.UPGRADE,"Steel Trash Can", null, null,
            null, 1, 2500, createIngredients(MaterialType.IronBar, 5)),
    GoldTrashCan(ItemType.UPGRADE,"Gold Trash Can", null, null,
            null, 1, 5000, createIngredients(MaterialType.GoldBar, 5)),
    IridiumTrashCan(ItemType.UPGRADE,"Iridium Trash Can", null, null,
            null, 1, 12500, createIngredients(MaterialType.IridiumBar, 5)),
    ;
    public enum ItemType {
        STOCK,
        UPGRADE
    }

    private final ItemType type;
    private final String displayName;
    private final String description;
    private final Integer price;
    private final MaterialType materialType;
    private final int dailyLimit;
    private final int upgradeCost;
    private final Map<MaterialType, Integer> ingredient;

    Blacksmith(ItemType type, String displayName, String description, Integer price,
                MaterialType materialType, int dailyLimit, int upgradeCost, Map<MaterialType, Integer> ingredient) {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.materialType = materialType;
        this.dailyLimit = dailyLimit;
        this.upgradeCost = upgradeCost;
        this.ingredient = (ingredient != null) ? ingredient : new HashMap<>();
    }

    private static Map<MaterialType, Integer> createIngredients(MaterialType type, int amount) {
        Map<MaterialType, Integer> map = new HashMap<>();
        map.put(type, amount);
        return map;
    }

    public ItemType getType() { return type; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    public MaterialType getMaterialType() { return materialType; }
    @Override public int getDailyLimit() { return dailyLimit; }
    public int getUpgradeCost() { return upgradeCost; }
    public Map<MaterialType, Integer> getIngredient() { return ingredient; }

}
