package models.item;

import enums.items.CookingRecipes;
import enums.items.ItemType;

public class CookingRecipe{
    CookingRecipes recipeType;

    public CookingRecipe(CookingRecipes recipeType) {
        this.recipeType = recipeType;
    }

    public CookingRecipes getRecipeType() {
        return recipeType;
    }
}
