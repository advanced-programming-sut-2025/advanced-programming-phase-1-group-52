package enums.design.Shop;

import enums.items.MaterialType;
import java.util.HashMap;
import java.util.Map;

public enum CarpentersShop {

    // Permanent Stock
    Wood(ItemType.Permanent, "Wood", "A sturdy, yet flexible plant material with a wide variety of uses.",
            10, Integer.MAX_VALUE, null, null, null),

    Stone(ItemType.Permanent, "Stone", "A common material with many uses in crafting and building.",
            20, Integer.MAX_VALUE, null, null, null),

    // Farm Buildings
    Barn(ItemType.FarmBuilding, "Barn", "Houses 4 barn-dwelling animals.",
            6000, 1, createMaterial(MaterialType.Wood, 350), createMaterial(MaterialType.Stone, 150), "7x4"),

    BigBarn(ItemType.FarmBuilding, "Big Barn", "Houses 8 barn-dwelling animals. Unlocks goats.",
            12000, 1, createMaterial(MaterialType.Wood, 450), createMaterial(MaterialType.Stone, 200), "7x4"),

    DeluxeBarn(ItemType.FarmBuilding, "Deluxe Barn", "Houses 12 barn-dwelling animals. Unlocks sheep and pigs.",
            25000, 1, createMaterial(MaterialType.Wood, 550), createMaterial(MaterialType.Stone, 300), "7x4"),

    Coop(ItemType.FarmBuilding, "Coop", "Houses 4 coop-dwelling animals.",
            4000, 1, createMaterial(MaterialType.Wood, 300), createMaterial(MaterialType.Stone, 100), "6x3"),

    BigCoop(ItemType.FarmBuilding, "Big Coop", "Houses 8 coop-dwelling animals. Unlocks ducks.",
            10000, 1, createMaterial(MaterialType.Wood, 400), createMaterial(MaterialType.Stone, 150), "6x3"),

    DeluxeCoop(ItemType.FarmBuilding, "Deluxe Coop", "Houses 12 coop-dwelling animals. Unlocks rabbits.",
            20000, 1, createMaterial(MaterialType.Wood, 500), createMaterial(MaterialType.Stone, 200), "6x3"),

    Well(ItemType.FarmBuilding, "Well", "Provides a place for you to refill your watering can.",
            1000, 1, null, createMaterial(MaterialType.Stone, 75), "3x3"),

    ShippingBin(ItemType.FarmBuilding, "Shipping Bin", "Items placed in it will be included in the nightly shipment.",
            250, Integer.MAX_VALUE, createMaterial(MaterialType.Wood, 150), null, "1x1");

    public enum ItemType {
        Permanent,
        FarmBuilding,
    }

    private final ItemType type;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final Map<MaterialType, Integer> material1;
    private final Map<MaterialType, Integer> material2;
    private final String size;

    CarpentersShop(ItemType type, String name, String description, int price, int dailyLimit,
                   Map<MaterialType, Integer> material1, Map<MaterialType, Integer> material2, String size) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.material1 = (material1 != null) ? material1 : new HashMap<>();
        this.material2 = (material2 != null) ? material2 : new HashMap<>();
        this.size = size;
    }

    private static Map<MaterialType, Integer> createMaterial(MaterialType type, int amount) {
        Map<MaterialType, Integer> map = new HashMap<>();
        if (amount > 0) map.put(type, amount);
        return map;
    }

    public ItemType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public Map<MaterialType, Integer> getMaterial1() {
        return material1;
    }

    public Map<MaterialType, Integer> getMaterial2() {
        return material2;
    }

    public String getSize() {
        return size;
    }
}
