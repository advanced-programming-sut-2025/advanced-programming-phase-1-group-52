package com.example.main.controller;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.main.enums.design.Season;
import com.example.main.enums.design.Shop.Blacksmith;
import com.example.main.enums.design.Shop.CarpentersShop;
import com.example.main.enums.design.Shop.FishShop;
import com.example.main.enums.design.Shop.JojaMart;
import com.example.main.enums.design.Shop.MarniesRanch;
import com.example.main.enums.design.Shop.PierresGeneralStore;
import com.example.main.enums.design.Shop.ShopEntry;
import com.example.main.enums.design.Shop.TheStardropSaloon;
import com.example.main.enums.design.ShopType;
import com.example.main.enums.design.TileType;
import com.example.main.enums.items.AnimalType;
import com.example.main.enums.items.Backpacks;
import com.example.main.enums.items.CageType;
import com.example.main.enums.items.CookingRecipeType;
import com.example.main.enums.items.CraftingMachineType;
import com.example.main.enums.items.CraftingRecipes;
import com.example.main.enums.items.FoodType;
import com.example.main.enums.items.ForagingSeedType;
import com.example.main.enums.items.MaterialType;
import com.example.main.enums.items.MineralType;
import com.example.main.enums.items.ToolType;
import com.example.main.models.App;
import com.example.main.models.BankAccount;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.Inventory;
import com.example.main.models.Player;
import com.example.main.models.Result;
import com.example.main.models.Tile;
import com.example.main.models.User;
import com.example.main.models.building.Housing;
import com.example.main.models.building.Shop;
import com.example.main.models.item.CookingRecipe;
import com.example.main.models.item.CraftingMachine;
import com.example.main.models.item.CraftingRecipe;
import com.example.main.models.item.Food;
import com.example.main.models.item.Item;
import com.example.main.models.item.Material;
import com.example.main.models.item.Mineral;
import com.example.main.models.item.PurchasedAnimal;
import com.example.main.models.item.Seed;
import com.example.main.models.item.Tool;

public class StoreMenuController {
    public Result showAllProducts() {
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
        StringBuilder stringBuilder = new StringBuilder();
        Tile currentTile = map.getTile(game.getCurrentPlayer().currentX(), game.getCurrentPlayer().currentY());
        switch (currentTile.getShop().getShopType()) {
            case ShopType.Blacksmith -> {
                for (Blacksmith entry : Blacksmith.values()) {
                    stringBuilder.append(entry.toString()).append("\n");
                }
            }
            case ShopType.JojaMart -> {
                for (JojaMart entry : JojaMart.values()) {
                    stringBuilder.append(entry.toString()).append("\n");
                }
            }
            case ShopType.CarpentersShop -> {
                for (CarpentersShop entry : CarpentersShop.values()) {
                    stringBuilder.append(entry.toString()).append("\n");
                }
            }
            case ShopType.FishShop -> {
                for (FishShop entry : FishShop.values()) {
                    stringBuilder.append(entry.toString()).append("\n");
                }
            }
            case ShopType.MarniesRanch -> {
                for (MarniesRanch entry : MarniesRanch.values()) {
                    stringBuilder.append(entry.toString()).append("\n");
                }
            }
            case ShopType.PierresGeneralStore -> {
                for (PierresGeneralStore entry : PierresGeneralStore.values()) {
                    stringBuilder.append(entry.toString()).append("\n");
                }
            }
            case ShopType.TheStardropSaloon -> {
                for (TheStardropSaloon entry : TheStardropSaloon.values()) {
                    stringBuilder.append(entry.toString()).append("\n");
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
            case ShopType.Blacksmith ->  {
                for (Blacksmith entry : Blacksmith.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.JojaMart ->  {
                for (JojaMart entry : JojaMart.values()) {
                    if (entry.getDisplayName().equals(name) && (entry.getSeason() == null || entry.getSeason().equals(game.getDate().getCurrentSeason()))) item = entry;
                }
            }
            case ShopType.CarpentersShop ->  {
                for (CarpentersShop entry : CarpentersShop.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.FishShop ->  {
                for (FishShop entry : FishShop.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.MarniesRanch ->  {
                for (MarniesRanch entry : MarniesRanch.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            case ShopType.PierresGeneralStore ->  {
                for (PierresGeneralStore entry : PierresGeneralStore.values()) {
                    if (entry.getDisplayName().equals(name) && (entry.getSeason() == null || entry.getSeason().equals(game.getDate().getCurrentSeason()))) item = entry;
                }
            }
            case ShopType.TheStardropSaloon ->  {
                for (TheStardropSaloon entry : TheStardropSaloon.values()) {
                    if (entry.getDisplayName().equals(name)) item = entry;
                }
            }
            default -> {
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
                case MineralType mineralType -> {
                    Mineral mineral = new Mineral(mineralType, amount);
                    game.getCurrentPlayer().getInventory().addItem(mineral);
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
                case CookingRecipeType cookingRecipes -> {
                    CookingRecipe cookingRecipe = new CookingRecipe(cookingRecipes);
                    game.getCurrentPlayer().addCookingRecipe(cookingRecipe);
                }
                case FoodType foodType -> {
                    Food food = new Food(foodType, amount);
                    game.getCurrentPlayer().getInventory().addItem(food);
                }
                case MaterialType materialType -> {
                    Material material = new Material(materialType, amount);
                    game.getCurrentPlayer().getInventory().addItem(material);
                }
                case ToolType toolType -> {
                    this.upgradeTool(toolType.getName());
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
                    Season itemSeason = ((JojaMart) entry).getSeason();
                    if (itemSeason == null || itemSeason.equals(game.getDate().getCurrentSeason()))  {
                        stringBuilder.append(entry.toString());
                        stringBuilder.append("\nStock: ").append(stock.get(entry.getDisplayName())).append("\n");
                    }
                }
            }
            case PierresGeneralStore -> {
                for (ShopEntry entry : entries) {
                    Season itemSeason = ((PierresGeneralStore) entry).getSeason();
                    if (itemSeason == null || itemSeason.equals(game.getDate().getCurrentSeason()))  {
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

        String requiredBuilding = ranchEnum.getBuildingRequired().contains("Barn") ? "barn" : "coop";
        if (!target.getType().getName().toLowerCase().contains(requiredBuilding.toLowerCase())) {
            return Result.failure(animalType.getName() + " must live in a " + requiredBuilding + ".");
        }

        if (ranchEnum.getBuildingRequired().contains("deluxe")) {
            if (target.getType().getLevel() < 3) {
                return Result.failure("You need to upgrade your " + requiredBuilding + " to house " + animalType.getName() + ".");
            }
        }

        if (ranchEnum.getBuildingRequired().contains("big")) {
            if (target.getType().getLevel() < 2) {
                return Result.failure("You need to upgrade your " + requiredBuilding + " to house " + animalType.getName() + ".");
            }
        }

        if (ranch.purchase(animalKey, 1)) {
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
        else {
            return Result.failure("Out of stock!");
        }
  
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

            if (map.isPlantThere(x, x + 5, y, y + 7)) {
                return new Result(false, "You can't place your coop there!");
            }

            ArrayList<Player> players = new ArrayList<>();
            for (User user : game.getPlayers()) {
                players.add(user.getPlayer());
            }

            if (carpShop.purchase(buildingKey, 1)) {
                map.generateBuilding(players, players.indexOf(game.getCurrentPlayer()), TileType.Housing, x, x + 5, y, y + 7);
            }
            else {
                return new Result(false, "Out of stock!");
            }
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

            if (map.isPlantThere(x, x + 6, y, y + 7)) {
                return new Result(false, "You can't place your barn there!");
            }

            ArrayList<Player> players = new ArrayList<>();
            for (User user : game.getPlayers()) {
                players.add(user.getPlayer());
            }

            if (carpShop.purchase(buildingKey, 1)) {
                map.generateBuilding(players, players.indexOf(game.getCurrentPlayer()), TileType.Housing, x, x + 6, y, y + 7);
            }
            else {
                return new Result(false, "Out of stock!");
            }
        }

        req1.forEach((mat, amt) -> inv.remove2(mat.getName(), amt));
        req2.forEach((mat, amt) -> inv.remove2(mat.getName(), amt));
        player.getBankAccount().withdraw(carpEnum.getPrice());
        player.addHousing(cageType, x, y);
        return new Result(true,
                carpEnum.getDisplayName() + " #" + player.getNextHousingId() + " successfully built at (" + x + "," + y + ")."
        );
    }

    public Result upgradeTool(String toolName) {
        Game game = App.getInstance().getCurrentGame();
        GameMap map = App.getInstance().getCurrentGame().getMap();
        Tile currentTile = map.getTile(game.getCurrentPlayer().currentX(), game.getCurrentPlayer().currentY());
        Shop shop = currentTile.getShop();

        Tool tool = game.getCurrentPlayer().getInventory().getToolByName(toolName);
        if (tool == null) {
            return new Result(false, "Invalid tool name!");
        }

        if (tool.getItemType().getName().contains("rod")) {
            switch (tool.getLevel()) {
                case 0 -> {
                    tool.setLevel(1);
                    tool.setItemType(ToolType.Bamboo_Rod);
                    return new Result(true, "Tool upgraded!");
                }
                case 1 -> {
                    tool.setLevel(2);
                    tool.setItemType(ToolType.Fiberglass_Rod);
                    return new Result(true, "Tool upgraded!");
                }
                case 2 -> {
                    tool.setLevel(3);
                    tool.setItemType(ToolType.Iridium_Rod);
                    return new Result(true, "Tool upgraded!");
                }
                default -> {
                    return new Result(false, "Already max level!");
                }
            }
        }

        if (!shop.getShopType().equals(ShopType.Blacksmith)) {
            return new Result(false, "You should be at blacksmith's shop to upgrade tools!");
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

                game.getCurrentPlayer().getInventory().remove2(MineralType.Copper_Bar.getName(), 5);
                game.getCurrentPlayer().getBankAccount().withdraw(2000);
                tool.setLevel(1);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.Copper_Hoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.Copper_Pickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.Copper_Axe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.Copper_Watering_Can);
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

                game.getCurrentPlayer().getInventory().remove2(MineralType.Iron_Bar.getName(), 5);
                game.getCurrentPlayer().getBankAccount().withdraw(5000);
                tool.setLevel(2);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.Steel_Hoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.Steel_Pickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.Steel_Axe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.Steel_Watering_Can);
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

                game.getCurrentPlayer().getInventory().remove2(MineralType.Gold_Bar.getName(), 5);
                game.getCurrentPlayer().getBankAccount().withdraw(10000);
                tool.setLevel(3);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.Gold_Hoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.Gold_Pickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.Gold_Axe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.Gold_Watering_Can);
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

                game.getCurrentPlayer().getInventory().remove2(MineralType.Iridium_Bar.getName(), 5);
                game.getCurrentPlayer().getBankAccount().withdraw(25000);
                tool.setLevel(4);

                if (tool.getItemType().getName().contains("hoe")) {
                    tool.setItemType(ToolType.Iridium_Hoe);
                }
                else if (tool.getItemType().getName().contains("pickaxe")) {
                    tool.setItemType(ToolType.Iridium_Pickaxe);
                }
                else if (tool.getItemType().getName().contains("axe")) {
                    tool.setItemType(ToolType.Iridium_Axe);
                }
                else if (tool.getItemType().getName().contains("can")) {
                    tool.setItemType(ToolType.Iridium_Watering_Can);
                }

                return new Result(true, "Tool upgraded!");
            }
            default -> {
                return new Result(false, "Already max level!");
            }
        }
    }
}
