package views;

import controllers.MainMenuController;
import enums.regex.MainMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu implements AppMenu {
    private final MainMenuController controller = new MainMenuController();

    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = MainMenuCommands.MenuEnter.getMatcher(input)) != null ) {
            System.out.println(controller.menuEnter(
                    matcher.group("menuName")
            ).Message());
        } else if ((matcher = MainMenuCommands.MenuExit.getMatcher(input)) != null) {
            controller.menuExit();
        } else if ((matcher = MainMenuCommands.ShowCurrentMenu.getMatcher(input)) != null ) {
            controller.showCurrentMenu();
        } else if ((matcher = MainMenuCommands.UserLogout.getMatcher(input)) != null ) {
            controller.userLogout();
        } else {
            System.out.println("Invalid command");
        }
    }
}
