package com.example.main.models.item;

import com.example.main.enums.items.CookingRecipeType;

public class CookingRecipe{
    CookingRecipeType recipeType;

    public CookingRecipe(CookingRecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public CookingRecipeType getRecipeType() {
        return recipeType;
    }
}
