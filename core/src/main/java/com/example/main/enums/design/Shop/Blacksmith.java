package com.example.main.enums.design.Shop;


import com.example.main.enums.items.MineralType;
import java.util.HashMap;
import java.util.Map;

import com.example.main.enums.items.MaterialType;

public enum Blacksmith implements ShopEntry{
    // Stock items
    Copper_Ore_Product(Type.STOCK, "Copper Ore", "A common ore that can be smelted into bars.",
            75, MineralType.Copper_Ore, Integer.MAX_VALUE,0, null),
    Iron_Ore_Product(Type.STOCK, "Iron Ore", "A fairly common ore that can be smelted into bars.",
            150, MineralType.Iron_Ore, Integer.MAX_VALUE, 0, null),
    Coal_Product(Type.STOCK, "Coal", "A combustible rock that is useful for crafting and smelting.",
            150, MineralType.Coal, Integer.MAX_VALUE, 0, null),
    Gold_Ore_Product(Type.STOCK, "Gold Ore", "A precious ore that can be smelted into bars.",
            400, MineralType.Gold_Ore, Integer.MAX_VALUE, 0, null),
    Iridium_Ore_Product(Type.STOCK, "Iridium Ore", "A rare and valuable ore used for advanced crafts.",
            1000, MineralType.Iridium_Ore, Integer.MAX_VALUE, 0, null);

    public enum Type {
        STOCK,
        UPGRADE
    }

    private final Type type;
    private final String displayName;
    private final String description;
    private final Integer price;
    private final MineralType materialType;
    private final int dailyLimit;
    private final int upgradeCost;
    private final Map<MineralType, Integer> ingredient;

    Blacksmith(Type type, String displayName, String description, Integer price,
               MineralType materialType, int dailyLimit, int upgradeCost, Map<MineralType, Integer> ingredient) {
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.materialType = materialType;
        this.dailyLimit = dailyLimit;
        this.upgradeCost = upgradeCost;
        this.ingredient = (ingredient != null) ? ingredient : new HashMap<>();
    }

    private static Map<MineralType, Integer> createIngredients(MineralType type, int amount) {
        Map<MineralType, Integer> map = new HashMap<>();
        map.put(type, amount);
        return map;
    }

    public Type getType() { return type; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    public MineralType getMineralType() { return materialType; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public MineralType getItemType() {
        return this.materialType;
    }

    public int getUpgradeCost() { return upgradeCost; }
    public Map<MineralType, Integer> getIngredient() { return ingredient; }

    @Override
    public String toString() {
        return this.displayName + " - Price: " + this.price;
    }
}
