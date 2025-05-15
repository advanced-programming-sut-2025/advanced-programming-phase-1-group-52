package controllers;

import enums.items.ItemType;
import javax.tools.Tool;
import models.App;
import models.Buy;
import models.BuyOffer;
import models.BuyRequest;
import models.Game;
import models.Player;
import models.Result;
import models.Trade;
import models.TradeRequest;
import models.TradeRequest;
import models.item.Animal;
import models.item.AnimalProduct;
import models.item.Crop;
import models.item.Fish;
import models.item.Food;
import models.item.ArtisanMachineProduct;
import models.item.Item;
import models.item.Material;
import models.item.Mineral;
import models.item.Seed;
import models.item.TrashCan;
import models.item.WateringCan;

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

        Buy buyRequest = new Buy(buyer, seller, itemName, amount, price);
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

        Buy buyOffer = new Buy(buyer, seller, itemName, amount, price);
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

    public Result listTrades() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Trade trade : game.getCurrentPlayer().getTrades()) {
            stringBuilder.append(trade.toString());
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result respondToTrade(String respond, String idString) {
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
            trade.setAnswered(true);
            buyer.addNotif(seller, seller.getUsername() + " don't have the wanted item!");
            return new Result(false, "You don't have the wanted item!");
        }
        if (trade.getAmount() > item.getNumber()) {
            trade.setAnswered(true);
            buyer.addNotif(seller, seller.getUsername() + " don't have enough items!");
            return new Result(false, "Insufficient item amount!");
        }

        switch (item) {
            case AnimalProduct animalProduct -> {
                seller.getInventory().removeItem(animalProduct.getClass(), trade.getAmount());
                AnimalProduct newAnimalProduct = new AnimalProduct(animalProduct.getAnimalType(), trade.getAmount());
                
                buyer.getInventory().addItem(newAnimalProduct);
                buyer.getBankAccount().withdraw(trade.getPrice());
                
                seller.getBankAccount().deposit(trade.getPrice());
                buyer.addNotif(seller, "You bought " + trade.getAmount() + " " + item.getName() + " for " + trade.getPrice() + "$");
                
                trade.setAccepted(true);
                trade.setAnswered(true);
                return new Result(true, "Trade completed!");
            }
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
            case Good good -> {
                seller.getInventory().removeItem(good.getClass(), trade.getAmount());
                Good newGood = new Good(good.getGoodType(), trade.getAmount());
                
                buyer.getInventory().addItem(newGood);
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
        Player buyer = trade.getBuyer();
        Player seller = trade.getSeller();
        Item givingItem = buyer.getInventory().getItemByName(trade.getGivingItemName());
        Item receivingItem = seller.getInventory().getItemByName(trade.getReceivingItemName());

        if (buyer.equals(game.getCurrentPlayer())) {
            if (givingItem == null) {
                trade.setAnswered(true);
                seller.addNotif(buyer, buyer.getUsername() + " doesn't have the wanted item!");
                return new Result(false, "You don't have the wanted item!");
            }
            if (givingItem.getNumber() < trade.getGivingAmount()) {
                trade.setAnswered(true);
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
            case AnimalProduct animalProduct -> {
                buyer.getInventory().removeItem(animalProduct.getClass(), trade.getGivingAmount());
                AnimalProduct newAnimalProduct = new AnimalProduct(animalProduct.getAnimalType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newAnimalProduct);
                buyer.addNotif(seller, "You got " + trade.getGivingAmount() + " " + givingItem.getName());
                
                trade.setAccepted(true);
                trade.setAnswered(true);
            }
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
            case Good good -> {
                buyer.getInventory().removeItem(good.getClass(), trade.getGivingAmount());
                Good newGood = new Good(good.getGoodType(), trade.getGivingAmount());
                
                seller.getInventory().addItem(newGood);
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
            case AnimalProduct animalProduct -> {
                seller.getInventory().removeItem(animalProduct.getClass(), trade.getReceivingAmount());
                AnimalProduct newAnimalProduct = new AnimalProduct(animalProduct.getAnimalType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newAnimalProduct);

                return new Result(true, "Trade completed!");
            }
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
            case Good good -> {
                seller.getInventory().removeItem(good.getClass(), trade.getReceivingAmount());
                Good newGood = new Good(good.getGoodType(), trade.getReceivingAmount());
                buyer.getInventory().addItem(newGood);

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
}
