package models.item;

import enums.items.CraftingRecipes;
import enums.items.ItemType;

public class CraftingRecipe extends Item {
    CraftingRecipes recipeType;

    public CraftingRecipe(CraftingRecipes recipeType, int number) {
        super(recipeType, number);
        this.recipeType = recipeType;
    }

    public CraftingRecipes getRecipeType() {
        return recipeType;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }
}

