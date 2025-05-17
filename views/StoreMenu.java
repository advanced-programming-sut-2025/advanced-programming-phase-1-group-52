package views;

import controllers.StoreMenuController;
import enums.Menu;
import enums.regex.StoreMenuCommands;
import java.util.Scanner;
import java.util.regex.Matcher;
import models.App;

public class StoreMenu implements AppMenu {
    StoreMenuController controller = new StoreMenuController();
    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine();
        Matcher matcher;

        if ((matcher = StoreMenuCommands.BuildBarnOrCoop.getMatcher(input)) != null) {
            System.out.println(controller.buildBarnOrCoop(matcher.group("buildingName"), matcher.group("x"), matcher.group("y"))); 
        }
        else if ((matcher = StoreMenuCommands.BuyAnimal.getMatcher(input)) != null) {
            System.out.println(controller.buyAnimal(matcher.group("animal"), matcher.group("id"), matcher.group("name")));
        }
        else if ((matcher = StoreMenuCommands.Purchase.getMatcher(input)) != null) {
            System.out.println(controller.purchase(matcher.group("productName"), matcher.group("amount")));
        }
        else if (StoreMenuCommands.ShowAllProducts.getMatcher(input) != null) {
            System.out.println(controller.showAllProducts());
        }
        else if (StoreMenuCommands.ShowAvailableProducts.getMatcher(input) != null) {
            System.out.println(controller.showAvailableProducts());
        }
        else if (StoreMenuCommands.GoToGameMenu.getMatcher(input) != null) {
            App.getInstance().setCurrentMenu(Menu.GameMenu);
        }
        else {
            System.out.println("Invalid command!");
        }
    }
}
