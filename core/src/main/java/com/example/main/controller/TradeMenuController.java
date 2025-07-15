package com.example.main.controller;

import com.example.main.enums.Menu;
import com.example.main.models.App;
import com.example.main.models.Buy;
import com.example.main.models.Game;
import com.example.main.models.Player;
import com.example.main.models.Result;
import com.example.main.models.Trade;
import com.example.main.models.TradeRequest;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fish;
import com.example.main.models.item.Food;
import com.example.main.models.item.Item;
import com.example.main.models.item.Material;
import com.example.main.models.item.Mineral;
import com.example.main.models.item.Seed;
import com.example.main.models.item.TrashCan;

public class TradeMenuController {
    public Result buyRequest(String username, String itemName, String amountString, String priceString) {
        Game game = App.getInstance().getCurrentGame();
        Player buyer = game.getCurrentPlayer();
        Player seller = game.getUserByUsername(username).getPlayer();
        if (seller == null) {
            return new Result(false, "Player not found.");
        }

        int amount;
        try {
            amount = Integer.parseInt(amountString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid amount format.");
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid price format.");
        }

        if (price > buyer.getBankAccount().getBalance()) {
            return new Result(false, "Insufficient funds.");
        }

        Buy buyRequest = new Buy(buyer, seller, buyer, seller, itemName, amount, price);
        buyer.addTrade(buyRequest);
        seller.addTrade(buyRequest);
        seller.addNotif(buyer, "Buy request received!");
        return new Result(true, "Buy request sent to " + username + ".");
    }

    public Result buyOffer(String username, String itemName, String amountString, String priceString) {
        Game game = App.getInstance().getCurrentGame();
        Player buyer = game.getUserByUsername(username).getPlayer();
        Player seller = game.getCurrentPlayer();
        if (buyer == null) {
            return new Result(false, "Player not found.");
        }

        int amount;
        try {
            amount = Integer.parseInt(amountString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid amount format.");
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid price format.");
        }

        Item itemToGive = null;
        for (Item item : seller.getInventory().getItems()) {
            if (item.getName().equals(itemName)) {
                itemToGive = item;
                break;
            }
        }

        if (itemToGive == null) {
            return new Result(false, "Item not found in inventory.");
        }
        if (itemToGive.getNumber() < amount) {
            return new Result(false, "Not enough items in inventory.");
        }

        Buy buyOffer = new Buy(seller, buyer, buyer, seller, itemName, amount, price);
        buyer.addTrade(buyOffer);
        seller.addTrade(buyOffer);
        buyer.addNotif(seller, "Buy offer received!");
        return new Result(true, "Buy offer sent to " + username + ".");
    }

    public Result tradeRequest(String username, String givingItemName, String givingAmount, String receivingItemName, String receivingAmountString) {
        Game game = App.getInstance().getCurrentGame();
        Player buyer = game.getCurrentPlayer();
        Player seller = game.getUserByUsername(username).getPlayer();
        if (seller == null) {
            return new Result(false, "Player not found.");
        }

        int givingAmountInt;
        try {
            givingAmountInt = Integer.parseInt(givingAmount);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid giving amount format.");
        }
        int receivingAmountInt;
        try {
            receivingAmountInt = Integer.parseInt(receivingAmountString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid receiving amount format.");
        }

        Item givingItem = buyer.getInventory().getItemByName(givingItemName);
        if (givingItem == null) {
            return new Result(false, "Giving item not found in inventory.");
        }
        if (givingItem.getNumber() < givingAmountInt) {
            return new Result(false, "Not enough giving items in inventory.");
        }

        TradeRequest tradeRequest = new TradeRequest(buyer, seller, buyer, seller, givingItemName, givingAmountInt, receivingItemName, receivingAmountInt);
        buyer.addTrade(tradeRequest);
        seller.addTrade(tradeRequest);
        seller.addNotif(buyer, "Trade request received!");
        return new Result(true, "Trade request sent to " + username + ".");
    }

    public Result tradeOffer(String username, String givingItemName, String givingAmount, String receivingItemName, String receivingAmountString) {
        Game game = App.getInstance().getCurrentGame();
        Player buyer = game.getUserByUsername(username).getPlayer();
        Player seller = game.getCurrentPlayer();
        if (buyer == null) {
            return new Result(false, "Player not found.");
        }

        int givingAmountInt;
        try {
            givingAmountInt = Integer.parseInt(givingAmount);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid giving amount format.");
        }
        int receivingAmountInt;
        try {
            receivingAmountInt = Integer.parseInt(receivingAmountString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid receiving amount format.");
        }

        Item givingItem = seller.getInventory().getItemByName(givingItemName);
        if (givingItem == null) {
            return new Result(false, "Giving item not found in inventory.");
        }
        if (givingItem.getNumber() < givingAmountInt) {
            return new Result(false, "Not enough giving items in inventory.");
        }

        Trade tradeOffer = new TradeRequest(seller, buyer, buyer, seller, givingItemName, givingAmountInt, receivingItemName, receivingAmountInt);
        buyer.addTrade(tradeOffer);
        seller.addTrade(tradeOffer);
        buyer.addNotif(buyer, "Trade offer received!");
        return new Result(true, "Trade offer sent to " + username + ".");
    }

    public Result listTrades() {
        Game game = App.getInstance().getCurrentGame();
        StringBuilder stringBuilder = new StringBuilder();
        for (Trade trade : game.getCurrentPlayer().getTrades()) {
            if (!trade.isAnswered()) {
                stringBuilder.append(trade.toString());
            }
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result tradeHistory() {
        Game game = App.getInstance().getCurrentGame();
        StringBuilder stringBuilder = new StringBuilder();
        for (Trade trade : game.getCurrentPlayer().getTrades()) {
            stringBuilder.append(trade.toString());
        }
        return new Result(true, stringBuilder.toString());
    }

    public Result respondToTrade(String respond, String idString) {
        Game game = App.getInstance().getCurrentGame();
        int id;
        try {
            id = Integer.parseInt(idString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid id format!");
        }

        Trade trade = game.getCurrentPlayer().getTradeById(id);
        if (trade == null) {
            return new Result(false, "Trade not found!");
        }
        if (trade.isAnswered()) {
            return new Result(false, "Trade already answered!");
        }

        if (respond.equals("reject")) {
            if (trade.getBuyer().equals(game.getCurrentPlayer())) {
                trade.getSeller().addNotif(game.getCurrentPlayer(), "Your trade offer has been rejected!");
            }
            else {
                trade.getBuyer().addNotif(game.getCurrentPlayer(), "Your trade offer has been rejected!");
            }

            trade.setAnswered(true);
            return new Result(true, "Trade rejected!");
        }

        if (trade instanceof Buy buy) {
            return acceptBuy(buy);
        }
        else {
            return acceptTrade((TradeRequest) trade);
        } 
    }

    private Result acceptBuy(Buy trade) {
        Player buyer = trade.getBuyer();
        Player seller = trade.getSeller();
        Item item = seller.getInventory().getItemByName(trade.getItemName());

        if (item == null) {
            buyer.addNotif(seller, seller.getUsername() + " don't have the wanted item!");
            return new Result(false, "You don't have the wanted item!");
        }
        if (trade.getAmount() > item.getNumber()) {
            buyer.addNotif(seller, seller.getUsername() + " don't have enough items!");
            return new Result(false, "Insufficient item amount!");
        }

        switch (item) {
            case Fish fish -> {
                seller.getInventory().removeItem(fish.getClass(), trade.getAmount());
                Fish newFish = new Fish(fish.getFishType(), trade.getAmount());
                
                buyer.getInventory().addItem(newFish);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
            case Crop crop -> {
                seller.getInventory().removeItem(crop.getClass(), trade.getAmount());
                Crop newCrop = new Crop(crop.getCropType(), trade.getAmount());
                
                buyer.getInventory().addItem(newCrop);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
            case Food food -> {
                seller.getInventory().removeItem(food.getClass(), trade.getAmount());
                Food newFood = new Food(food.getFoodType(), trade.getAmount());
                
                buyer.getInventory().addItem(newFood);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
            case Material material -> {
                seller.getInventory().removeItem(material.getClass(), trade.getAmount());
                Material newMaterial = new Material(material.getMaterialType(), trade.getAmount());
                
                buyer.getInventory().addItem(newMaterial);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
            case Mineral mineral -> {
                seller.getInventory().removeItem(mineral.getClass(), trade.getAmount());
                Mineral newMineral = new Mineral(mineral.getMineralType(), trade.getAmount());
                
                buyer.getInventory().addItem(newMineral);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
            case Seed seed -> {
                seller.getInventory().removeItem(seed.getClass(), trade.getAmount());
                Seed newSeed = new Seed(seed.getForagingSeedType(), trade.getAmount());
                
                buyer.getInventory().addItem(newSeed);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
            case TrashCan trashCan -> {
                seller.getInventory().removeItem(trashCan.getClass(), trade.getAmount());
                TrashCan newTrashCan = new TrashCan(trashCan.getTrashCanType(), trade.getAmount());
                
                buyer.getInventory().addItem(newTrashCan);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
            default -> {
                trade.setAnswered(true);
                return new Result(false, "Untradeable item!");
            }
        }
    }

    private Result acceptTrade(TradeRequest trade) {
        Game game = App.getInstance().getCurrentGame();
        Player buyer = trade.getBuyer();
        Player seller = trade.getSeller();
        Item givingItem = buyer.getInventory().getItemByName(trade.getGivingItemName());
        Item receivingItem = seller.getInventory().getItemByName(trade.getReceivingItemName());

        if (buyer.equals(game.getCurrentPlayer())) {
            if (givingItem == null) {
                seller.addNotif(buyer, buyer.getUsername() + " doesn't have the wanted item!");
                return new Result(false, "You don't have the wanted item!");
            }
            if (givingItem.getNumber() < trade.getGivingAmount()) {
                seller.addNotif(buyer, buyer.getUsername() + " doesn't have enough items!");
                return new Result(false, "Insufficient item amount!");
            }
        }
        else {
            if (receivingItem == null) {
                trade.setAnswered(true);
                buyer.addNotif(seller, seller.getUsername() + " doesn't have the wanted item!");
                return new Result(false, "You don't have the wanted item!");
            }
            if (receivingItem.getNumber() < trade.getReceivingAmount()) {
                trade.setAnswered(true);
                buyer.addNotif(seller, seller.getUsername() + " doesn't have enough items!");
                return new Result(false, "Insufficient item amount!");
            }
        }

        switch (givingItem) {
            case Fish fish -> {
                buyer.getInventory().removeItem(fish.getClass(), trade.getGivingAmount());
                Fish newFish = new Fish(fish.getFishType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newFish);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
            case Crop crop -> {
                buyer.getInventory().removeItem(crop.getClass(), trade.getGivingAmount());
                Crop newCrop = new Crop(crop.getCropType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newCrop);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
            case Food food -> {
                buyer.getInventory().removeItem(food.getClass(), trade.getGivingAmount());
                Food newFood = new Food(food.getFoodType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newFood);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
            case Material material -> {
                buyer.getInventory().removeItem(material.getClass(), trade.getGivingAmount());
                Material newMaterial = new Material(material.getMaterialType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newMaterial);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
            case Mineral mineral -> {
                buyer.getInventory().removeItem(mineral.getClass(), trade.getGivingAmount());
                Mineral newMineral = new Mineral(mineral.getMineralType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newMineral);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
            case Seed seed -> {
                buyer.getInventory().removeItem(seed.getClass(), trade.getGivingAmount());
                Seed newSeed = new Seed(seed.getForagingSeedType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newSeed);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
            case TrashCan trashCan -> {
                buyer.getInventory().removeItem(trashCan.getClass(), trade.getGivingAmount());
                TrashCan newTrashCan = new TrashCan(trashCan.getTrashCanType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newTrashCan);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
            default -> {
                trade.setAnswered(true);
                return new Result(false, "Untradeable item!");
            }
        }

        switch (receivingItem) {
            case Fish fish -> {
                seller.getInventory().removeItem(fish.getClass(), trade.getReceivingAmount());
                Fish newFish = new Fish(fish.getFishType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newFish);

                return new Result(true, "Trade completed!");
            }
            case Crop crop -> {
                seller.getInventory().removeItem(crop.getClass(), trade.getReceivingAmount());
                Crop newCrop = new Crop(crop.getCropType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newCrop);

                return new Result(true, "Trade completed!");
            }
            case Food food -> {
                seller.getInventory().removeItem(food.getClass(), trade.getReceivingAmount());
                Food newFood = new Food(food.getFoodType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newFood);

                return new Result(true, "Trade completed!");
            }
            case Material material -> {
                seller.getInventory().removeItem(material.getClass(), trade.getReceivingAmount());
                Material newMaterial = new Material(material.getMaterialType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newMaterial);

                return new Result(true, "Trade completed!");
            }
            case Mineral mineral -> {
                seller.getInventory().removeItem(mineral.getClass(), trade.getReceivingAmount());
                Mineral newMineral = new Mineral(mineral.getMineralType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newMineral);

                return new Result(true, "Trade completed!");
            }
            case Seed seed -> {
                seller.getInventory().removeItem(seed.getClass(), trade.getReceivingAmount());
                Seed newSeed = new Seed(seed.getForagingSeedType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newSeed);

                return new Result(true, "Trade completed!");
            }
            case TrashCan trashCan -> {
                seller.getInventory().removeItem(trashCan.getClass(), trade.getReceivingAmount());
                TrashCan newTrashCan = new TrashCan(trashCan.getTrashCanType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newTrashCan);

                return new Result(true, "Trade completed!");
            }
            default -> {
                trade.setAnswered(true);
                return new Result(false, "Untradeable item!");
            }
        }
    }

    public Result goToGameMenu() {
        App.getInstance().setCurrentMenu(Menu.GameMenu);
        return new Result(true, "You are now in game menu!");
    }
}
