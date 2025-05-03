package views;

import controllers.LoginMenuController;
import enums.regex.LoginMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginMenu implements AppMenu {
    private final LoginMenuController controller = new LoginMenuController();

    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = LoginMenuCommands.Login.getMatcher(input)) != null) {
            System.out.println(controller.login(
                    matcher.group("username"),
                    matcher.group("password")
            ).toString());
        } else if ((matcher = LoginMenuCommands.ForgetPassword.getMatcher(input)) != null) {
            System.out.println(controller.forgetPassword(
                    matcher.group("username")
            ).toString());
        } else if ((matcher = LoginMenuCommands.Answer.getMatcher(input)) != null) {
            System.out.println(controller.answer(
                    matcher.group("answer")
            ).toString());
        } else if ((matcher = LoginMenuCommands.ShowCurrentMenu.getMatcher(input)) != null) {
            controller.showCurrentMenu();
        } else if ((matcher = LoginMenuCommands.MenuExit.getMatcher(input)) != null) {
            controller.menuExit();
        }
    }
}
