package controllers;

import enums.design.Shop.Blacksmith;
import enums.design.Shop.CarpentersShop;
import enums.design.Shop.FishShop;
import enums.design.Shop.JojaMart;
import enums.design.Shop.MarniesRanch;
import enums.design.Shop.PierresGeneralStore;
import enums.design.Shop.ShopEntry;
import enums.design.Shop.TheStardropSaloon;
import enums.design.ShopType;
import enums.items.AnimalType;
import enums.items.Backpacks;
import enums.items.CookingRecipes;
import enums.items.CraftingMachineType;
import enums.items.CraftingRecipes;
import enums.items.FoodType;
import enums.items.ForagingSeedType;
import enums.items.MaterialType;
import models.App;
import models.Game;
import models.GameMap;
import models.Result;
import models.Tile;
import models.building.Shop;
import models.item.CraftingRecipe;
import models.item.Material;
import models.item.Seed;

public class StoreMenuController {
    private final App app = App.getInstance();
    private final Game game = App.getInstance().getCurrentGame();
    private final GameMap map = App.getInstance().getCurrentGame().getMap();

    public Result showAllProducts() {
        StringBuilder stringBuilder = new StringBuilder();
        Tile currentTile = map.getTile(game.getCurrentPlayer().currentX(), game.getCurrentPlayer().currentY());
        switch (currentTile.getShop().getShopType()) {
            case ShopType.Blacksmith -> {
                for (Blacksmith entry : Blacksmith.values()) {
                    stringBuilder.append(entry.toString());
                }
            }
            case ShopType.JojaMart -> {
                for (JojaMart entry : JojaMart.values()) {
                    stringBuilder.append(entry.toString());
                }
            }
            case ShopType.CarpentersShop -> {
                for (CarpentersShop entry : CarpentersShop.values()) {
                    stringBuilder.append(entry.toString());
                }
            }
            case ShopType.FishShop -> {
                for (FishShop entry : FishShop.values()) {
                    stringBuilder.append(entry.toString());
                }
            }
            case ShopType.MarniesRanch -> {
                for (MarniesRanch entry : MarniesRanch.values()) {
                    stringBuilder.append(entry.toString());
                }
            }
            case ShopType.PierresGeneralStore -> {
                for (PierresGeneralStore entry : PierresGeneralStore.values()) {
                    stringBuilder.append(entry.toString());
                }
            }
            case ShopType.TheStardropSaloon -> {
                for (TheStardropSaloon entry : TheStardropSaloon.values()) {
                    stringBuilder.append(entry.toString());
                }
            }
            default -> {
                return new Result(false, "Invalid entry!");
            }
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result purchase(String name, String amountString) {
        int amount;
        ShopEntry item = null;
        Tile currentTile = map.getTile(game.getCurrentPlayer().currentX(), game.getCurrentPlayer().currentY());
        Shop shop = currentTile.getShop();
        
        switch (currentTile.getShop().getShopType()) {
            case ShopType.Blacksmith: {
                for (Blacksmith entry : Blacksmith.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.JojaMart: {
                for (JojaMart entry : JojaMart.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.CarpentersShop: {
                for (CarpentersShop entry : CarpentersShop.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.FishShop: {
                for (FishShop entry : FishShop.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.MarniesRanch: {
                for (MarniesRanch entry : MarniesRanch.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.PierresGeneralStore: {
                for (PierresGeneralStore entry : PierresGeneralStore.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.TheStardropSaloon: {
                for (TheStardropSaloon entry : TheStardropSaloon.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            default: {
                item = null;
            }
        }

        if (item == null) {
            return new Result(false, "Invalid entry!");
        }

        try {
            amount = Integer.parseInt(amountString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid amount format!");
        }

        if (game.getCurrentPlayer().getBankAccount().getBalance() < item.getPrice() * amount) {
            return new Result(false, "You don't have enough money!");
        }

        if (shop.purchase(name, amount)) {
            switch (item.getItemType()) {
                case MaterialType materialType -> {
                    Material material = new Material(materialType, amount);
                    game.getCurrentPlayer().getInventory().addItem(material);
                }
                case CraftingRecipes craftingRecipes -> {
                    CraftingRecipe craftingRecipe = new CraftingRecipe(craftingRecipes);
                    game.getCurrentPlayer().getCraftingRecipe().add(craftingRecipe);
                }
                case ForagingSeedType foragingSeedType -> {
                    Seed seed = new Seed(foragingSeedType, amount);
                    game.getCurrentPlayer().getInventory().addItem(seed);
                }
                case AnimalType animalType -> {
                    Animal
                }
                case Backpacks backpacks -> {

                }
                case CraftingMachineType craftingMachineType -> {

                }
                case CookingRecipes cookingRecipes -> {

                }
                case FoodType foodType -> {

                }
                default -> {
                    return new Result(false, "You can't buy that item right now!");
                }
            }
        }
        else {

        }
    }
}
