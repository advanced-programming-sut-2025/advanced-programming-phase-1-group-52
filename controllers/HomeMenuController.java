package controllers;

import enums.design.TileType;
import enums.items.CraftingRecipes;
import enums.items.ItemType;
import models.*;
import models.item.CraftingMachine;
import models.item.CraftingRecipe;
import models.item.Item;

import java.util.ArrayList;
import java.util.Map;

public class HomeMenuController {
    private Game game = App.getInstance().getCurrentGame();
    public Result showCraftingRecipes(){
        Player player = game.getCurrentPlayer();
        ArrayList<CraftingRecipe> playerCraftingRecipes = player.getCraftingRecipe();
        StringBuilder recipeString = new StringBuilder();
        recipeString.append(player.getUsername() + "'s crafting recipes:\n");
        for(CraftingRecipe craftingRecipe : playerCraftingRecipes) {
            recipeString.append(craftingRecipe.getRecipeType().getDisplayName() + "\n");
        }
        recipeString.deleteCharAt(recipeString.length() - 1);
        return new Result(true, recipeString.toString());
    }
    public Result craftItem(String itemName) {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getMap();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());

        if(!currentTile.getType().equals(TileType.House)) {
            return new Result(false, "You should be at home to craft item");
        }

        CraftingRecipes recipeType = findCraftingRecipeType(itemName);
        if(recipeType == null) {
            return new Result(false, "error");
        }

        CraftingRecipe recipe;

        if((recipe = findCraftingRecipeInPlayer(player.getCraftingRecipe(), recipeType)) == null) {
            return new Result(false, "The crafting recipe is not in your inventory");
        }

        if(player.getInventory().isFull()){
            return new Result(false, "your inventory is full");
        }

        Result result = isInventoryReadyToCraft(recipeType);

        if(!result.isSuccessful()){
            return result;
        }

        CraftingMachine craftingProduct = new CraftingMachine(recipeType.getProduct(),10);
        player.getInventory().getItems().add(craftingProduct);
        player.getInventory().addNumOfItems(1);
        player.addEnergy(-2);
        return new Result(true,craftingProduct.getName() + " crafted successfully");
    }

    private CraftingRecipes findCraftingRecipeType(String recipeName) {
        for(CraftingRecipes craftingRecipe : CraftingRecipes.values()) {
            if(craftingRecipe.name().equals(recipeName)) {
                return craftingRecipe;
            }
        }
        return null;
    }

    private CraftingRecipe findCraftingRecipeInPlayer(ArrayList<CraftingRecipe> recipes, CraftingRecipes craftingRecipeType) {
        for(CraftingRecipe craftingRecipe : recipes) {
            if(craftingRecipe.getRecipeType().equals(craftingRecipeType)) {
                return craftingRecipe;
            }
        }
        return null;
    }

    private Result isInventoryReadyToCraft(CraftingRecipes craftingRecipe) {
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = craftingRecipe.getIngredients();
        ArrayList<Item> playerItems = player.getInventory().getItems();
        ItemType itemType;
        Integer quantity;
        Item item;
        Result result = isReadySecond(craftingRecipe);

        if(!result.isSuccessful()){
            return result;
        }

        for(Map.Entry<ItemType, Integer> entry : neededIngredients.entrySet()) {
            itemType = entry.getKey();
            quantity = entry.getValue();
            item = findItem(itemType,playerItems);
            item.setNumber(item.getNumber() - quantity);
        }
        return result;
    }

    private Result isReadySecond(CraftingRecipes craftingRecipe){
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = craftingRecipe.getIngredients();
        ArrayList<Item> playerItems = player.getInventory().getItems();
        ItemType itemType;
        Integer quantity;
        Item item;

        for(Map.Entry<ItemType, Integer> entry : neededIngredients.entrySet()) {
            itemType = entry.getKey();
            quantity = entry.getValue();
            item = findItem(itemType,playerItems);
            if(item == null) {
                return new Result(false, "Item not found in your inventory");
            }
            if(item.getNumber() < quantity){
                return new Result(false, "Not enough items in your inventory");
            }
        }
        return new Result(true, "All items in your inventory");
    }

    private Item findItem(ItemType itemType, ArrayList<Item> items) {
        for(Item item : items){
            if(item.getItemType().equals(itemType)){
                return item;
            }
        }
        return null;
    }
}
