package views;

import controllers.ProfileMenuController;
import enums.regex.ProfileMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu implements AppMenu {
    private final ProfileMenuController controller = new ProfileMenuController();

    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input)) != null ) {
            System.out.println(controller.changeUsername(
                    matcher.group("username")
            ).toString());
        } else if ((matcher = ProfileMenuCommands.ChangeNickname.getMatcher(input)) != null ) {
            System.out.println(controller.changeNickname(
                    matcher.group("nickname")
            ).toString());
        } else if ((matcher = ProfileMenuCommands.ChangeEmail.getMatcher(input)) != null ) {
            System.out.println(controller.changeEmail(
                    matcher.group("email")
            ));
        } else if ((matcher = ProfileMenuCommands.ChangePassword.getMatcher(input)) != null ) {
            System.out.println(controller.changePassword(
                    matcher.group("new_password"),
                    matcher.group("old_password")
            ).toString());
        } else if ((matcher = ProfileMenuCommands.UserInfo.getMatcher(input)) != null ) {
            controller.userInfo();
        } else if ((matcher = ProfileMenuCommands.ShowCurrentMenu.getMatcher(input)) != null ) {
            controller.showCurrentMenu();
        } else if ((matcher = ProfileMenuCommands.MenuExit.getMatcher(input)) != null ) {
            controller.menuExit();
        } else {
            System.out.println("Invalid command");
        }

    }
}
