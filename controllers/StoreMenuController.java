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
import enums.design.TileType;
import enums.items.AnimalType;
import enums.items.Backpacks;
import enums.items.CageType;
import enums.items.CookingRecipes;
import enums.items.CraftingMachineType;
import enums.items.CraftingRecipes;
import enums.items.FoodType;
import enums.items.ForagingSeedType;
import enums.items.MaterialType;
import enums.items.ToolType;
import java.util.ArrayList;
import java.util.HashMap;
import models.App;
import models.BankAccount;
import models.Game;
import models.GameMap;
import models.Inventory;
import models.Player;
import models.item.PurchasedAnimal;
import models.Result;
import models.Tile;
import models.User;
import models.building.Housing;
import models.building.Shop;
import models.item.CookingRecipe;
import models.item.CraftingMachine;
import models.item.CraftingRecipe;
import models.item.Food;
import models.item.Item;
import models.item.Material;
import models.item.Seed;
import models.item.Tool;

public class StoreMenuController {
    public Result showAllProducts() {
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
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
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
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
                    if (entry.getDisplayName().equals(name) && (entry.getSeason() == null || entry.getSeason().equals(game.getDate().getCurrentSeason()))) item = entry;
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
                    if (entry.getDisplayName().equals(name) && (entry.getSeason() == null || entry.getSeason().equals(game.getDate().getCurrentSeason()))) item = entry;
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
                    CraftingRecipe craftingRecipe = new CraftingRecipe(craftingRecipes, 1);
                    game.getCurrentPlayer().addCraftingRecipe(craftingRecipe);
                }
                case ForagingSeedType foragingSeedType -> {
                    Seed seed = new Seed(foragingSeedType, amount);
                    game.getCurrentPlayer().getInventory().addItem(seed);
                }
                case Backpacks backpacks -> {
                    game.getCurrentPlayer().getInventory().setBackpack(backpacks);
                }
                case CraftingMachineType craftingMachineType -> {
                    CraftingMachine craftingMachine = new CraftingMachine(craftingMachineType, amount);
                    game.getCurrentPlayer().getInventory().addItem(craftingMachine);
                }
                case CookingRecipes cookingRecipes -> {
                    CookingRecipe cookingRecipe = new CookingRecipe(cookingRecipes);
                    game.getCurrentPlayer().addCookingRecipe(cookingRecipe);
                }
                case FoodType foodType -> {
                    Food food = new Food(foodType, amount);
                    game.getCurrentPlayer().getInventory().addItem(food);
                }
                default -> {
                    return new Result(false, "You can't buy that item right now!");
                }
            }

            game.getCurrentPlayer().getBankAccount().withdraw(item.getPrice() * amount);
            return new Result(true, "Purchase complete!");
        }
        else {
            return new Result(false, "Purchase failed!");
        }
    }

    public Result showAvailableProducts() {
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
        Tile currentTile = map.getTile(game.getCurrentPlayer().currentX(), game.getCurrentPlayer().currentY());
        Shop shop = currentTile.getShop();
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<ShopEntry> entries = shop.getEntries();
        HashMap<String, Integer> stock = shop.getStock();

        switch (shop.getShopType()) {
            case JojaMart -> {
                for (ShopEntry entry : entries) {
                    if (((JojaMart) entry).getSeason().equals(game.getDate().getCurrentSeason()))  {
                        stringBuilder.append(entry.toString());
                        stringBuilder.append("\nStock: ").append(stock.get(entry.getDisplayName())).append("\n");
                    }
                }
            }
            case PierresGeneralStore -> {
                for (ShopEntry entry : entries) {
                    if (((JojaMart) entry).getSeason().equals(game.getDate().getCurrentSeason()))  {
                        stringBuilder.append(entry.toString());
                        stringBuilder.append("\nStock: ").append(stock.get(entry.getDisplayName())).append("\n");
                    }
                }
            }
            default -> {
                for (ShopEntry entry : entries) {
                    stringBuilder.append(entry.toString());
                    stringBuilder.append("\nStock: ").append(stock.get(entry.getDisplayName())).append("\n");
                }
            }
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result buyAnimal(String animalKey, String housingIdString, String givenName) {
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
        int housingId = Integer.parseInt(housingIdString);

        Shop ranch = new Shop(ShopType.MarniesRanch);
        ShopEntry entry = ranch.findEntry(animalKey);
        if (!(entry instanceof MarniesRanch ranchEnum)) {
            return Result.failure("'" + animalKey + "' is not sold at Marnie's Ranch.");
        }

        AnimalType animalType = ranchEnum.getAnimalType();
        if (animalType == null) {
            return Result.failure("'" + animalKey + "' is not an animal.");
        }

        Player player = game.getCurrentPlayer();
        BankAccount account = player.getBankAccount();
        int price = ranchEnum.getPrice();

        if (account.getBalance() < price) {
            return Result.failure("Insufficient funds to buy " + animalType.getName() + ".");
        }

        Housing target = null;
        for (Housing h : player.getHousings()) {
            if (h.getId() == housingId) {
                target = h;
                break;
            }
        }
        if (target == null) {
            return Result.failure("No housing found with ID " + housingId + ".");
        }

        String requiredBuilding = ranchEnum.getBuildingRequired();
        if (!target.getType().getName().toLowerCase().contains(requiredBuilding.toLowerCase())) {
            return Result.failure(animalType.getName() + " must live in a " + requiredBuilding + ".");
        }

        PurchasedAnimal newAnimal = new PurchasedAnimal(animalType, givenName, target.getX() + 1, target.getY() + 1);
        Result addResult = player.addAnimalToHousing(housingId, newAnimal);
        if (!addResult.isSuccessful()) {
            return addResult;
        }

        account.withdraw(price);
        return Result.success(
                animalType.getName() +
                        " named \"" + givenName + "\" purchased for " +
                        price + "g and assigned to " +
                        target.getType().getName() +
                        " #" + housingId + "."
        );
    }

    public Result buildBarnOrCoop(String buildingKey, String xString, String yString) {
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
        int x = Integer.parseInt(xString);
        int y = Integer.parseInt(yString);

        if (!map.inBounds(x, y)) {
            return new Result(false, "Coordinates are out of farm bounds.");
        }

        Shop carpShop = new Shop(ShopType.CarpentersShop);
        ShopEntry entry = carpShop.findEntry(buildingKey);
        if (!(entry instanceof CarpentersShop carpEnum) ||
                !(carpEnum.getDisplayName().contains("Barn") || carpEnum.getDisplayName().contains("Coop"))) {
            return new Result(false, "'" + buildingKey + "' is not a valid barn or coop building.");
        }

        Player player = game.getCurrentPlayer();
        Inventory inv = player.getInventory();
        var req1 = carpEnum.getMaterial1();
        var req2 = carpEnum.getMaterial2();

        for (var e1 : req1.entrySet()) {
            if (inv.getCount(e1.getKey()) < e1.getValue()) {
                return new Result(false, "Insufficient " + e1.getKey() + ".");
            }
        }
        for (var e2 : req2.entrySet()) {
            if (inv.getCount(e2.getKey()) < e2.getValue()) {
                return new Result(false, "Insufficient " + e2.getKey() + ".");
            }
        }

        if (player.getBankAccount().getBalance() < carpEnum.getPrice()) {
            return new Result(false, "You don't have enough money!");
        }
        
        CageType cageType;
        if (carpEnum.getDisplayName().contains("Coop")) {
            if (carpEnum.getDisplayName().contains("Big")) {
                cageType = CageType.BigCage;
            }
            else if (carpEnum.getDisplayName().contains("Deluxe")) {
                cageType = CageType.DeluxeCage;
            }
            else {
                cageType = CageType.NormalCage;
            }
            
            if (map.isPlantThere(x, x + 6, y, y + 3)) {
                return new Result(false, "You can't place your coop there!");
            }

            ArrayList<Player> players = new ArrayList<>();
            for (User user : game.getPlayers()) {
                players.add(user.getPlayer());
            }

            map.generateBuilding(players, players.indexOf(game.getCurrentPlayer()), TileType.Housing, x, x + 6, y, y + 3);
        } 
        else {
            if (carpEnum.getDisplayName().contains("Big")) {
                cageType = CageType.BigBarn;
            }
            else if (carpEnum.getDisplayName().contains("Deluxe")) {
                cageType = CageType.DeluxeBarn;
            }
            else {
                cageType = CageType.NormalBarn;
            }

            if (map.isPlantThere(x, x + 7, y, y + 4)) {
                return new Result(false, "You can't place your barn there!");
            }

            ArrayList<Player> players = new ArrayList<>();
            for (User user : game.getPlayers()) {
                players.add(user.getPlayer());
            }

            map.generateBuilding(players, players.indexOf(game.getCurrentPlayer()), TileType.Housing, x, x + 7, y, y + 4);
        }
        
        req1.forEach((mat, amt) -> inv.remove(mat, amt));
        req2.forEach((mat, amt) -> inv.remove(mat, amt));
        player.getBankAccount().withdraw(carpEnum.getPrice());
        player.addHousing(cageType, x, y);
        return new Result(true,
                carpEnum.getDisplayName() + " successfully built at (" + x + "," + y + ")."
        );
    }

    public Result upgradeTool(String toolName) {
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
        Tile currentTile = map.getTile(game.getCurrentPlayer().currentX(), game.getCurrentPlayer().currentY());
        Shop shop = currentTile.getShop();

        if (!shop.getShopType().equals(ShopType.Blacksmith)) {
            return new Result(false, "You should be at blacksmith's shop to upgrade tools!");
        }

        Tool tool = game.getCurrentPlayer().getInventory().getToolByName(toolName);
        if (tool == null) {
            return new Result(false, "Invalid tool name!");
        }

        if (tool.isMax()) {
            return new Result(false, "Already max level!");
        }

        switch (tool.getLevel()) {
            case 0 -> {
                if (game.getCurrentPlayer().getBankAccount().getBalance() < 2000) {
                    return new Result(false, "You don't have enough money!");
                }

                Item bar = game.getCurrentPlayer().getInventory().getItemByName("Copper bar");
                if (bar == null) {
                    return new Result(false, "You don't have any Copper bar!");
                }
                if (bar.getNumber() < 5) {
                    return new Result(false, "You don't have enough Copper bars!");
                }

                game.getCurrentPlayer().getInventory().remove(MaterialType.CopperBar, 5);
                game.getCurrentPlayer().getBankAccount().withdraw(2000);
                tool.setLevel(1);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.CopperHoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.CopperPickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.CopperAxe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.CopperWateringCan);
                }

                return new Result(true, "Tool upgraded!");
            }
            case 1 -> {
                if (game.getCurrentPlayer().getBankAccount().getBalance() < 5000) {
                    return new Result(false, "You don't have enough money!");
                }

                Item bar = game.getCurrentPlayer().getInventory().getItemByName("Iron bar");
                if (bar == null) {
                    return new Result(false, "You don't have any Iron bar!");
                }
                if (bar.getNumber() < 5) {
                    return new Result(false, "You don't have enough Iron bars!");
                }

                game.getCurrentPlayer().getInventory().remove(MaterialType.IronBar, 5);
                game.getCurrentPlayer().getBankAccount().withdraw(5000);
                tool.setLevel(2);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.IronicHoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.IronicPickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.IronicAxe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.IronicWateringCan);
                }

                return new Result(true, "Tool upgraded!");
            }
            case 2 -> {
                if (game.getCurrentPlayer().getBankAccount().getBalance() < 10000) {
                    return new Result(false, "You don't have enough money!");
                }

                Item bar = game.getCurrentPlayer().getInventory().getItemByName("Gold bar");
                if (bar == null) {
                    return new Result(false, "You don't have any Gold bar!");
                }
                if (bar.getNumber() < 5) {
                    return new Result(false, "You don't have enough Gold bars!");
                }

                game.getCurrentPlayer().getInventory().remove(MaterialType.GoldBar, 5);
                game.getCurrentPlayer().getBankAccount().withdraw(10000);
                tool.setLevel(3);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.GoldenHoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.GoldenPickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.GoldenAxe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.GoldenWateringCan);
                }

                return new Result(true, "Tool upgraded!");
            }
            case 3 -> {
                if (game.getCurrentPlayer().getBankAccount().getBalance() < 25000) {
                    return new Result(false, "You don't have enough money!");
                }

                Item bar = game.getCurrentPlayer().getInventory().getItemByName("Iridium bar");
                if (bar == null) {
                    return new Result(false, "You don't have any Iridium bar!");
                }
                if (bar.getNumber() < 5) {
                    return new Result(false, "You don't have enough Iridium bars!");
                }

                game.getCurrentPlayer().getInventory().remove(MaterialType.IridiumBar, 5);
                game.getCurrentPlayer().getBankAccount().withdraw(25000);
                tool.setLevel(4);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.IridiumHoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.IridiumPickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.IridiumAxe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.IridiumWateringCan);
                }

                return new Result(true, "Tool upgraded!");
            }
            default -> {
                return new Result(false, "Already max level!");
            }
        }
    }
}
