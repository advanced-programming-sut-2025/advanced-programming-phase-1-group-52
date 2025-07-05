package com.example.main.views;

import java.util.Scanner;
import java.util.regex.Matcher;

import com.example.main.controller.LoginMenuController;
import com.example.main.enums.regex.LoginMenuCommands;

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
            ).Message());
        } else if ((matcher = LoginMenuCommands.ForgetPassword.getMatcher(input)) != null) {
            System.out.println(controller.forgetPassword(
                    matcher.group("username"), scanner
            ).Message());
        } else if ((matcher = LoginMenuCommands.Answer.getMatcher(input)) != null) {
            System.out.println(controller.answer(
                    matcher.group("answer")
            ).Message());
        } else if ((matcher = LoginMenuCommands.ShowCurrentMenu.getMatcher(input)) != null) {
            controller.showCurrentMenu();
        } else if ((matcher = LoginMenuCommands.ResetPassword.getMatcher(input)) != null) {
            System.out.println(controller.resetPassword(
                    matcher.group("resetPassword")
            ).Message());
        } else if ((matcher = LoginMenuCommands.MenuExit.getMatcher(input)) != null) {
            controller.menuExit();
        }
        else if ((matcher = LoginMenuCommands.GotoSignUpMenu.getMatcher(input)) != null) {
            System.out.println(controller.gotoSignUpMenu().Message());
        }
        else if ((matcher = LoginMenuCommands.Logout.getMatcher(input)) != null) {
            System.out.println(controller.logout().Message());
        }
        else{
            System.out.println("Invalid command");
        }
    }
}
