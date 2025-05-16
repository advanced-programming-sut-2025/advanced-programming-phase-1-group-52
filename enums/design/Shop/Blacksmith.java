package enums.design.Shop;

import enums.items.MaterialType;
import java.util.HashMap;
import java.util.Map;

public enum Blacksmith implements ShopEntry{
    // Stock items
    CopperOre(Type.STOCK, "Copper Ore", "A common ore that can be smelted into bars.",
            75, MaterialType.CopperOre, Integer.MAX_VALUE,0, null),
    IronOre(Type.STOCK, "Iron Ore", "A fairly common ore that can be smelted into bars.",
            150, MaterialType.IronOre, Integer.MAX_VALUE, 0, null),
    Coal(Type.STOCK, "Coal", "A combustible rock that is useful for crafting and smelting.",
            150, MaterialType.Coal, Integer.MAX_VALUE, 0, null),
    GoldOre(Type.STOCK, "Gold Ore", "A precious ore that can be smelted into bars.",
            400, MaterialType.GoldOre, Integer.MAX_VALUE, 0, null),
    IridiumOre(Type.STOCK, "Iridium Ore", "A rare and valuable ore used for advanced crafts.",
            1000, MaterialType.IridiumOre, Integer.MAX_VALUE, 0, null),

    // Upgrade items
    CopperHoes(Type.UPGRADE,"Copper Hoe", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),
    CopperPickaxe(Type.UPGRADE,"Copper Pickaxe", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),
    CopperAxe(Type.UPGRADE,"Copper Axe", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),
    CopperWaterCan(Type.UPGRADE,"Copper Watering Can", null, null,
            null, 1, 2000, createIngredients(MaterialType.CopperBar, 5)),

    SteelHoes(Type.UPGRADE,"Steel Hoe", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),
    SteelPickaxe(Type.UPGRADE,"Steel Pickaxe", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),
    SteelAxe(Type.UPGRADE,"Steel Axe", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),
    SteelWaterCan(Type.UPGRADE,"Steel Watering Can", null, null,
            null, 1, 5000, createIngredients(MaterialType.IronBar, 5)),

    GoldHoes(Type.UPGRADE,"Gold Hoe", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),
    GoldPickaxe(Type.UPGRADE,"Gold Pickaxe", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),
    GoldAxe(Type.UPGRADE,"Gold Axe", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),
    GoldWaterCan(Type.UPGRADE,"Gold Watering Can", null, null,
            null, 1, 10000, createIngredients(MaterialType.GoldBar, 5)),

    IridiumHoes(Type.UPGRADE,"Iridium Hoe", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),
    IridiumPickaxe(Type.UPGRADE,"Iridium Pickaxe", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),
    IridiumAxe(Type.UPGRADE,"Iridium Axe", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),
    IridiumWaterCan(Type.UPGRADE,"Iridium Watering Can", null, null,
            null, 1, 25000, createIngredients(MaterialType.IridiumBar, 5)),

    CopperTrashCan(Type.UPGRADE,"Copper Trash Can", null, null,
            null, 1, 1000, createIngredients(MaterialType.CopperBar, 5)),
    SteelTrashCan(Type.UPGRADE,"Steel Trash Can", null, null,
            null, 1, 2500, createIngredients(MaterialType.IronBar, 5)),
    GoldTrashCan(Type.UPGRADE,"Gold Trash Can", null, null,
            null, 1, 5000, createIngredients(MaterialType.GoldBar, 5)),
    IridiumTrashCan(Type.UPGRADE,"Iridium Trash Can", null, null,
            null, 1, 12500, createIngredients(MaterialType.IridiumBar, 5)),
    ;
    public enum Type {
        STOCK,
        UPGRADE
    }

    private final Type type;
    private final String displayName;
    private final String description;
    private final Integer price;
    private final MaterialType materialType;
    private final int dailyLimit;
    private final int upgradeCost;
    private final Map<MaterialType, Integer> ingredient;

    Blacksmith(Type type, String displayName, String description, Integer price,
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

    public Type getType() { return type; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    public MaterialType getMaterialType() { return materialType; }
    @Override public int getDailyLimit() { return dailyLimit; }

//     @Override
//     public ItemType getItemType() {
//         return this.materialType;
//     }

    public int getUpgradeCost() { return upgradeCost; }
    public Map<MaterialType, Integer> getIngredient() { return ingredient; }

}
