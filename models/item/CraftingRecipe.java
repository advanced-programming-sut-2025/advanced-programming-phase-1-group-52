package models.item;

import enums.items.CraftingRecipes;
import enums.items.ItemType;

public class CraftingRecipe {
    CraftingRecipes recipeType;

    public CraftingRecipe(CraftingRecipes recipeType) {
        this.recipeType = recipeType;
    }

    public CraftingRecipes getRecipeType() {
        return recipeType;
    }
}

