package com.example.main.enums.design.Shop;

import com.example.main.enums.items.CookingRecipeType;
import com.example.main.enums.items.FoodType;
import com.example.main.enums.items.ItemType;

public enum TheStardropSaloon implements ShopEntry {
    // Permanent Stock
    Beer_Product(FoodType.Beer, null, "Beer", "Drink in moderation.", 400, Integer.MAX_VALUE),
    Salad_Product(FoodType.Salad, null, "Salad", "A healthy garden salad.", 220, Integer.MAX_VALUE),
    Bread_Product(FoodType.Bread, null, "Bread", "A crusty baguette.", 120, Integer.MAX_VALUE),
    Spaghetti_Product(FoodType.Spaghetti, null, "Spaghetti", "An old favorite.", 240, Integer.MAX_VALUE),
    Pizza_Product(FoodType.Pizza, null, "Pizza", "It's popular for all the right reasons.", 600, Integer.MAX_VALUE),
    Coffee_Product(FoodType.Coffee, null, "Coffee", "It smells delicious. This is sure to give you a boost.", 300, Integer.MAX_VALUE),

    // Recipes
    Hashbrowns_Recipe(CookingRecipeType.Hashbrowns, CookingRecipeType.Hashbrowns, "Hashbrowns Recipe", "A recipe to make Hashbrowns", 50, 1),
    Omelet_Recipe(CookingRecipeType.Omelet, CookingRecipeType.Omelet, "Omelet Recipe", "A recipe to make Omelet", 100, 1),
    Pancakes_Recipe(CookingRecipeType.Pancakes, CookingRecipeType.Pancakes, "Pancakes Recipe", "A recipe to make Pancakes", 100, 1),
    Bread_Recipe(CookingRecipeType.Bread, CookingRecipeType.Bread, "Bread Recipe", "A recipe to make Bread", 100, 1),
    Tortilla_Recipe(CookingRecipeType.Tortilla, CookingRecipeType.Tortilla, "Tortilla Recipe", "A recipe to make Tortilla", 100, 1),
    Pizza_Recipe(CookingRecipeType.Pizza, CookingRecipeType.Pizza, "Pizza Recipe", "A recipe to make Pizza", 150, 1),
    MakiRoll_Recipe(CookingRecipeType.Maki_Roll, CookingRecipeType.Maki_Roll, "Maki Roll Recipe", "A recipe to make Maki Roll", 300, 1),
    TripleShotEspresso_Recipe(CookingRecipeType.Triple_Shot_Espresso, CookingRecipeType.Triple_Shot_Espresso, "Triple Shot Espresso Recipe", "A recipe to make Triple Shot Espresso", 5000, 1),
    Cookie_Recipe(CookingRecipeType.Cookie, CookingRecipeType.Cookie, "Cookie Recipe", "A recipe to make Cookie", 300, 1);

    private final ItemType itemType;
    private final CookingRecipeType cookingRecipes;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;

    TheStardropSaloon(ItemType itemType, CookingRecipeType cookingRecipes, String name, String description, int price, int dailyLimit) {
        this.itemType = itemType;
        this.cookingRecipes = cookingRecipes;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    @Override
    public ItemType getItemType() { return itemType; }
    public CookingRecipeType getCookingRecipes() { return cookingRecipes; }
    @Override public String getDisplayName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public String toString() {
        return this.name + " - Price: " + this.price;
    }
}
