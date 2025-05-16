package enums.design.Shop;

import enums.items.CookingRecipes;
import enums.items.FoodType;
import enums.items.ItemType;

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
    HashbrownsRecipe(null,CookingRecipes.HashBrowns, "Hashbrowns Recipe",
            "A recipe to make Hashbrowns", 50, 1),
    OmeletRecipe(null,CookingRecipes.Omelet, "Omelet Recipe",
            "A recipe to make Omelet", 100, 1),
    PancakesRecipe(null,CookingRecipes.Pancakes, "Pancakes Recipe",
            "A recipe to make Pancakes", 100, 1),
    BreadRecipe(null,CookingRecipes.Bread, "Bread Recipe",
            "A recipe to make Bread", 100, 1),
    TortillaRecipe(null,CookingRecipes.Tortilla, "Tortilla Recipe",
            "A recipe to make Tortilla", 100, 1),
    PizzaRecipe(null,CookingRecipes.Pizza, "Pizza Recipe",
            "A recipe to make Pizza", 150, 1),
    MakiRollRecipe(null,CookingRecipes.MakiRoll, "Maki Roll Recipe",
            "A recipe to make Maki Roll", 300, 1),
    TripleShotEspressoRecipe(null,CookingRecipes.TripleShotEspresso, "Triple Shot Espresso Recipe",
            "A recipe to make Triple Shot Espresso", 5000, 1),
    CookieRecipe(null,CookingRecipes.Cookie, "Cookie Recipe",
            "A recipe to make Cookie", 300, 1);



    private final FoodType foodType;
    private final CookingRecipes cookingRecipes;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;

    TheStardropSaloon(FoodType foodType, CookingRecipes cookingRecipes, String name, String description, int price, int dailyLimit) {

        this.foodType = foodType;
        this.cookingRecipes = cookingRecipes;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    public FoodType getFoodType() { return foodType; }
    public CookingRecipes getCookingRecipes() { return cookingRecipes; }
    @Override public String getDisplayName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public ItemType getItemType() {
        return null;
    }
}
