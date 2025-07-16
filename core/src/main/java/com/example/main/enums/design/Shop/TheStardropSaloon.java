package com.example.main.enums.design.Shop;

import com.example.main.enums.items.CookingRecipeType;
import com.example.main.enums.items.FoodType;
import com.example.main.enums.items.ItemType;

public enum TheStardropSaloon implements ShopEntry {

    // Permanent Stock
    Beer(FoodType.Beer, null, "Beer", "Drink in moderation.",
            400, Integer.MAX_VALUE),
    Salad(FoodType.Salad, null, "Salad", "A healthy garden salad.",
            220, Integer.MAX_VALUE),
    Bread(FoodType.Bread, null, "Bread", "A crusty baguette.",
            120, Integer.MAX_VALUE),
    Spaghetti(FoodType.Spaghetti, null, "Spaghetti", "An old favorite.",
            240, Integer.MAX_VALUE),
    Pizza(FoodType.Pizza, null, "Pizza", "It's popular for all the right reasons.",
            600, Integer.MAX_VALUE),
    Coffee(FoodType.Coffee, null, "Coffee", "It smells delicious. This is sure to give you a boost.",
            300, Integer.MAX_VALUE),

    // Recipe
    HashbrownsRecipe(CookingRecipeType.HashBrowns, CookingRecipeType.HashBrowns, "Hashbrowns Recipe",
            "A recipe to make Hashbrowns", 50, 1),
    OmeletRecipe(CookingRecipeType.Omelet, CookingRecipeType.Omelet, "Omelet Recipe",
            "A recipe to make Omelet", 100, 1),
    PancakesRecipe(CookingRecipeType.Pancakes, CookingRecipeType.Pancakes, "Pancakes Recipe",
            "A recipe to make Pancakes", 100, 1),
    BreadRecipe(CookingRecipeType.Bread, CookingRecipeType.Bread, "Bread Recipe",
            "A recipe to make Bread", 100, 1),
    TortillaRecipe(CookingRecipeType.Tortilla, CookingRecipeType.Tortilla, "Tortilla Recipe",
            "A recipe to make Tortilla", 100, 1),
    PizzaRecipe(CookingRecipeType.Pizza, CookingRecipeType.Pizza, "Pizza Recipe",
            "A recipe to make Pizza", 150, 1),
    MakiRollRecipe(CookingRecipeType.MakiRoll, CookingRecipeType.MakiRoll, "Maki Roll Recipe",
            "A recipe to make Maki Roll", 300, 1),
    TripleShotEspressoRecipe(CookingRecipeType.TripleShotEspresso, CookingRecipeType.TripleShotEspresso, "Triple Shot Espresso Recipe",
            "A recipe to make Triple Shot Espresso", 5000, 1),
    CookieRecipe(CookingRecipeType.Cookie, CookingRecipeType.Cookie, "Cookie Recipe",
            "A recipe to make Cookie", 300, 1);



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
