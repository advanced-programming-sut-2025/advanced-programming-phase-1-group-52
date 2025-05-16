package views;

import controllers.TradeMenuController;
import enums.regex.TradeMenuCommands;
import java.util.Scanner;
import java.util.regex.Matcher;

public class TradeMenu implements AppMenu {
    TradeMenuController controller = new TradeMenuController();
    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;

        if ((matcher = TradeMenuCommands.Buy.getMatcher(input)) != null) {
            
        } 
        else if ((matcher = TradeMenuCommands.Trade.getMatcher(input)) != null) {
            
        } 
        else if ((matcher = TradeMenuCommands.TradeList.getMatcher(input)) != null) {
            
        } 
        else if ((matcher = TradeMenuCommands.TradeResponse.getMatcher(input)) != null) {
            
        } 
        else if ((matcher = TradeMenuCommands.TradeHistory.getMatcher(input)) != null) {
            
        } 
        else {
            System.out.println("Invalid command");
        }
    }
}
