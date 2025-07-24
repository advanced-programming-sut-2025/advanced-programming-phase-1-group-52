package com.example.main.controller;

import java.util.ArrayList;
import java.util.Map;

import com.example.main.enums.Menu;
import com.example.main.enums.design.TileType;
import com.example.main.enums.items.CookingRecipeType;
import com.example.main.enums.items.CraftingMachineType;
import com.example.main.enums.items.CraftingRecipes;
import com.example.main.enums.items.ItemType;
import com.example.main.models.App;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.HouseRefrigerator;
import com.example.main.models.Player;
import com.example.main.models.Result;
import com.example.main.models.Tile;
import com.example.main.models.item.CookingRecipe;
import com.example.main.models.item.CraftingMachine;
import com.example.main.models.item.CraftingRecipe;
import com.example.main.models.item.Food;
import com.example.main.models.item.Item;

public class HomeMenuController {
    public Result showCraftingRecipes(){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        ArrayList<CraftingRecipe> playerCraftingRecipes = player.getCraftingRecipe();
        StringBuilder recipeString = new StringBuilder();
        recipeString.append(player.getUsername() + "'s crafting recipes:\n");
        for(CraftingRecipe craftingRecipe : playerCraftingRecipes) {
            recipeString.append(craftingRecipe.getRecipeType().getName() + "\n");
        }
        recipeString.deleteCharAt(recipeString.length() - 1);
        return new Result(true, recipeString.toString());
    }

    public Result craftItem(String itemName) {
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        GameMap map = game.getMap();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        CraftingMachineType machineType = findCraftingMachineType(itemName);
        if(machineType == null) {
            return new Result(false, "There is no machine with that name");
        }
        if(!currentTile.getType().equals(TileType.House)) {
            return new Result(false, "You should be at home to craft item");
        }

        CraftingRecipes recipeType = findCraftingRecipeType(machineType.getName() + " Recipe");
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

    public Result back(){
        App.getInstance().setCurrentMenu(Menu.GameMenu);
        return new Result(true, "you are in game menu now!");
    }

    public Result cheatAddCraftingRecipe(String recipeName){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        CraftingRecipes recipeType = findCraftingRecipeType(recipeName);
        if(recipeType == null) {
            return new Result(false, "error2");
        }
        CraftingRecipe recipe = new CraftingRecipe(recipeType,1);
        player.getCraftingRecipe().add(recipe);
        return new Result(true,recipe.getName() + " added successfully");
    }

    public Result cheatAddCookingRecipe(String recipeName){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        CookingRecipeType recipeType = findCookingRecipeType(recipeName);
        if(recipeType == null) {
            return new Result(false, "error2");
        }
        CookingRecipe recipe = new CookingRecipe(recipeType);
        player.getCookingRecipe().add(recipe);
        return new Result(true,recipeName + " added successfully");
    }

    public Result cook(String recipeName){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        CookingRecipeType recipe = findCookingRecipeType(recipeName);
        if(recipe == null) {
            return new Result(false, "wrong recipe");
        }
        CookingRecipe cookingRecipe = findCookingRecipe(recipe);
        if(cookingRecipe == null) {
            return new Result(false, "you don't have this cooking recipe");
        }

        if(player.getInventory().isFull()){
            return new Result(false, "your inventory is full");
        }

        Result result1 = isInventoryReadyToCook(recipe);
        Result result2 = isRefrigeratorReadyToCook(recipe);

        if(!result1.isSuccessful() && !result2.isSuccessful()){
            return new Result(false, "you can't cook this food");
        }

        Food food = new Food(recipe.getFoodType(),2);
        player.getInventory().getItems().add(food);
        player.getInventory().addNumOfItems(1);
        player.addEnergy(-3);
        return new Result(true,food.getName() + " crafted successfully");
    }

    public Result showCookingRecipes(){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        ArrayList<CookingRecipe> playerCookingRecipes = player.getCookingRecipe();
        StringBuilder recipeString = new StringBuilder();
        recipeString.append(player.getUsername() + "'s cooking recipes:\n");
        for(CookingRecipe cookingRecipe : playerCookingRecipes) {
            recipeString.append(cookingRecipe.getRecipeType().getDisplayName() + "\n");
        }
        recipeString.deleteCharAt(recipeString.length() - 1);
        return new Result(true, recipeString.toString());
    }

    public Result refrigeratorHandler(String action, String itemName) {
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Tile tile = game.getMap().getTile(player.currentX(), player.currentY());
        if(!tile.getType().equals(TileType.House)){
            return new Result(false, "you should be at home to use refrigerator!");
        }
        HouseRefrigerator refrigerator = player.getHouseRefrigerator();
        if(action.equals("put")) {
            Item item = findItem(itemName,player.getInventory().getItems());
            if(item == null) {
                return new Result(false, "you do not have this item");
            }
            if(refrigerator.putItem(item)) {
                player.getInventory().remove2(item.getName());
                return new Result(true, item.getName() + " put successfully");
            }
            return new Result(false, "refrigerator is full");
        }
        else if(action.equals("pick")) {
            if(player.getInventory().isFull()) {
                return new Result(false, "Your inventory is full");
            }
            Item pickedItem = null;
            if(pickedItem == null) {
                return new Result(false, "this item is not in the refrigerator");
            }
            player.getInventory().addItem(pickedItem);
            return new Result(true, pickedItem.getName() + " picked successfully");
        }
        else {
            return new Result(false, "Invalid action");
        }
    }

    private CraftingRecipes findCraftingRecipeType(String recipeName) {
        for(CraftingRecipes craftingRecipe : CraftingRecipes.values()) {
            if(craftingRecipe.getName().equals(recipeName)) {
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
        Game game = App.getInstance().getCurrentGame();
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
        Game game = App.getInstance().getCurrentGame();
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

    private Item findItem(String itemName, ArrayList<Item> items) {
        for(Item item : items){
            if(item.getName().equals(itemName)){
                return item;
            }
        }
        return null;
    }

    private CraftingMachineType findCraftingMachineType(String machineName) {
        for(CraftingMachineType craftingMachineType : CraftingMachineType.values()) {
            if(craftingMachineType.getName().equals(machineName)) {
                return craftingMachineType;
            }
        }
        return null;
    }

    private CookingRecipeType findCookingRecipeType(String recipeName) {
        for(CookingRecipeType craftingRecipe : CookingRecipeType.values()) {
            if(craftingRecipe.getName().equals(recipeName)) {
                return craftingRecipe;
            }
        }
        return null;
    }

    private CookingRecipe findCookingRecipe(CookingRecipeType recipeType) {
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        for(CookingRecipe recipe : player.getCookingRecipe()) {
            if(recipe.getRecipeType().equals(recipeType) || recipe.getRecipeType().getName().equals(recipeType.getName())) {
                return recipe;
            }
        }
        return null;
    }

    private Result isInventoryReadyToCook(CookingRecipeType cookingRecipe) {
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = cookingRecipe.getIngredients();
        ArrayList<Item> playerItems = player.getInventory().getItems();
        ItemType itemType;
        Integer quantity;
        Item item;
        Result result = isReadySecondCooking(cookingRecipe);

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

    private Result isReadySecondCooking(CookingRecipeType cookingRecipe) {
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = cookingRecipe.getIngredients();
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

    private Result isRefrigeratorReadyToCook(CookingRecipeType cookingRecipe) {
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = cookingRecipe.getIngredients();
        ArrayList<Item> playerItems = player.getHouseRefrigerator().getItems();
        ItemType itemType;
        Integer quantity;
        Item item;
        Result result = isReadySecondCookingRefrigerator(cookingRecipe);

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

    private Result isReadySecondCookingRefrigerator(CookingRecipeType cookingRecipe) {
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = cookingRecipe.getIngredients();
        ArrayList<Item> playerItems = player.getHouseRefrigerator().getItems();
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
}
