package com.example.main.views;

import java.util.Scanner;
import java.util.regex.Matcher;

import com.example.main.controller.ProfileMenuController;
import com.example.main.enums.regex.ProfileMenuCommands;

public class ProfileMenu implements AppMenu {
    private final ProfileMenuController controller = new ProfileMenuController();

    @Override
    public void checkInput(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = ProfileMenuCommands.ChangeUsername.getMatcher(input)) != null ) {
            System.out.println(controller.changeUsername(
                    matcher.group("username")
            ).Message());
        } else if ((matcher = ProfileMenuCommands.ChangeNickname.getMatcher(input)) != null ) {
            System.out.println(controller.changeNickname(
                    matcher.group("nickname")
            ).Message());
        } else if ((matcher = ProfileMenuCommands.ChangeEmail.getMatcher(input)) != null ) {
            System.out.println(controller.changeEmail(
                    matcher.group("email")
            ));
        } else if ((matcher = ProfileMenuCommands.ChangePassword.getMatcher(input)) != null ) {
            System.out.println(controller.changePassword(
                    matcher.group("newPassword"),
                    matcher.group("oldPassword")
            ).Message());
        } else if ((matcher = ProfileMenuCommands.UserInfo.getMatcher(input)) != null ) {
            System.out.println(controller.userInfo().Message());
        } else if ((matcher = ProfileMenuCommands.ShowCurrentMenu.getMatcher(input)) != null ) {
            controller.showCurrentMenu();
        } else if ((matcher = ProfileMenuCommands.MenuExit.getMatcher(input)) != null ) {
            controller.menuExit();
        }
        else if((matcher = ProfileMenuCommands.GoToMainMenu.getMatcher(input)) != null ) {
            System.out.println(controller.gotoMainMenu().Message());
        }
        else {
            System.out.println("Invalid command");
        }
    }
}
