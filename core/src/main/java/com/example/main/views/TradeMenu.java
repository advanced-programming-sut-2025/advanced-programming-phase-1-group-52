package com.example.main.views;

import java.util.Scanner;
import java.util.regex.Matcher;

import com.example.main.controller.TradeMenuController;
import com.example.main.enums.regex.TradeMenuCommands;

public class TradeMenu implements AppMenu {
    TradeMenuController controller = new TradeMenuController();
    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;

        if ((matcher = TradeMenuCommands.Buy.getMatcher(input)) != null) {
            if (matcher.group("type").equals("offer")) {
                System.out.println(controller.buyOffer(matcher.group("username"), matcher.group("item"), matcher.group("amount"), matcher.group("price")));
            }
            else {
                System.out.println(controller.buyRequest(matcher.group("username"), matcher.group("item"), matcher.group("amount"), matcher.group("price")));
            }
        }
        else if ((matcher = TradeMenuCommands.Trade.getMatcher(input)) != null) {
            if (matcher.group("type").equals("offer")) {
                System.out.println(controller.tradeOffer(matcher.group("username"), matcher.group("item"), matcher.group("amount"), matcher.group("targetItem"), matcher.group("targetAmount")));
            }
            else {
                System.out.println(controller.tradeRequest(matcher.group("username"), matcher.group("item"), matcher.group("amount"), matcher.group("targetItem"), matcher.group("targetAmount")));
            }
        }
        else if (TradeMenuCommands.TradeList.getMatcher(input) != null) {
            System.out.println(controller.listTrades());
        }
        else if ((matcher = TradeMenuCommands.TradeResponse.getMatcher(input)) != null) {
            System.out.println(controller.respondToTrade(matcher.group("response"), matcher.group("id")));
        }
        else if (TradeMenuCommands.TradeHistory.getMatcher(input) != null) {
            System.out.println(controller.tradeHistory());
        }
        else if (TradeMenuCommands.Back.getMatcher(input) != null) {
            System.out.println(controller.goToGameMenu());
        }
        else {
            System.out.println("Invalid command");
        }
    }
}
