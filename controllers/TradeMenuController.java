package controllers;

import models.App;
import models.BuyOffer;
import models.BuyRequest;
import models.Game;
import models.Player;
import models.Result;
import models.Trade;
import models.TradeRequest;
import models.item.Item;

public class TradeMenuController {
    App app = App.getInstance();
    Game game = App.getInstance().getCurrentGame();

    public Result buyRequest(String username, String itemName, String amountString, String priceString) {
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

        BuyRequest buyRequest = new BuyRequest(buyer, seller, itemName, amount, price);
        buyer.addTrade(buyRequest);
        seller.addTrade(buyRequest);
        seller.addNotif(buyer, "Buy request received!");
        return new Result(true, "Buy request sent to " + username + ".");
    }

    public Result buyOffer(String username, String itemName, String amountString, String priceString) {
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

        BuyOffer buyOffer = new BuyOffer(buyer, seller, itemName, amount, price);
        buyer.addTrade(buyOffer);
        seller.addTrade(buyOffer);
        buyer.addNotif(seller, "Buy offer received!");
        return new Result(true, "Buy offer sent to " + username + ".");
    }

    public Result tradeRequest(String username, String givingItemName, String givingAmount, String receivingItemName, String receivingAmountString) {
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

        TradeRequest tradeRequest = new TradeRequest(buyer, seller, givingItemName, givingAmountInt, receivingItemName, receivingAmountInt);
        buyer.addTrade(tradeRequest);
        seller.addTrade(tradeRequest);
        seller.addNotif(buyer, "Trade request received!");
        return new Result(true, "Trade request sent to " + username + ".");
    }

    public Result tradeOffer(String username, String givingItemName, String givingAmount, String receivingItemName, String receivingAmountString) {
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

        Trade tradeOffer = new TradeRequest(buyer, seller, givingItemName, givingAmountInt, receivingItemName, receivingAmountInt);
        buyer.addTrade(tradeOffer);
        seller.addTrade(tradeOffer);
        buyer.addNotif(buyer, "Trade offer received!");
        return new Result(true, "Trade offer sent to " + username + ".");
    }
}
