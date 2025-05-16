package views;

import controllers.SignUpMenuController;
import enums.regex.SignUpMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class SignUpMenu implements AppMenu {
    private final SignUpMenuController controller = new SignUpMenuController();

    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = SignUpMenuCommands.Register.getMatcher(input)) != null) {
            System.out.println(controller.register(
                    matcher.group("username").trim(),
                    matcher.group("password").trim(),
                    matcher.group("passwordConfirm").trim(),
                    matcher.group("nickname").trim(),
                    matcher.group("email").trim(),
                    matcher.group("gender").trim()
            ).toString());
        } else if ((matcher = SignUpMenuCommands.PickQuestion.getMatcher(input)) != null) {
            System.out.println(controller.pickQuestion(
                    matcher.group("questionNumber"),
                    matcher.group("answer"),
                    matcher.group("answerConfirm")
            ).toString());
        } else if ((matcher = SignUpMenuCommands.ShowCurrentMenu.getMatcher(input)) != null) {
            controller.showCurrentMenu();
        } else if ((matcher = SignUpMenuCommands.MenuExit.getMatcher(input)) != null) {
            controller.menuExit();
        } else {
            System.out.println("Invalid command");
        }
    }
}
