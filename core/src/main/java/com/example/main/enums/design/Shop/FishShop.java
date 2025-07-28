package com.example.main.enums.design.Shop;

import com.example.main.enums.items.CraftingRecipes;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.MaterialType;
import com.example.main.enums.items.ToolType;


public enum FishShop implements ShopEntry{

    FishSmokerRecipe_Product(CraftingRecipes.FishSmokerRecipe, "Fish Smoker (Recipe)",
            "A recipe to make Fish Smoker", 10000, -1, 1),
    Trout_Soup_Product(MaterialType.Trout_Soup,  "Trout Soup",
            "Pretty salty.", 250, -1, 1),
    Bamboo_Rod_Product(ToolType.Bamboo_Rod, "Bamboo rod",
            "Use in the water to catch fish.", 500, -1, 1),
    Training_Rod_Product(ToolType.Training_Rod, "Training rod",
            "It's a lot easier to use than other rods, but can only catch basic fish.", 25, -1, 1),
    Fiberglass_Rod_Product(ToolType.Fiberglass_Rod, "Fiberglass rod",
            "Use in the water to catch fish.", 1800, 2, 1),
    Iridium_Rod_Product(ToolType.Iridium_Rod, "Iridium rod",
            "Use in the water to catch fish.", 7500, 4, 1);


    private final ItemType itemType;
    private final String displayName;
    private final String description;
    private final int price;
    private final int fishingSkillRequired;
    private final int dailyLimit;

    FishShop(ItemType itemType, String displayName, String description, int price,
                int fishingSkillRequired, int dailyLimit) {
        this.itemType = itemType;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.fishingSkillRequired = fishingSkillRequired;
        this.dailyLimit = dailyLimit;
    }

    @Override
    public ItemType getItemType() { return itemType; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    public int getFishingSkillRequired() { return fishingSkillRequired; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public String toString() {
        return this.displayName + " - Price: " + this.price;
    }
}
