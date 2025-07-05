package com.example.main.views;

import java.util.Scanner;
import java.util.regex.Matcher;

import com.example.main.controller.SignUpMenuController;
import com.example.main.enums.regex.SignUpMenuCommands;

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
                    matcher.group("gender").trim(), scanner
            ).Message());
        }
        else if((matcher = SignUpMenuCommands.GeneratePassword.getMatcher(input)) != null) {
            System.out.println(controller.generatePassword().Message());
        }else if ((matcher = SignUpMenuCommands.ShowCurrentMenu.getMatcher(input)) != null) {
            controller.showCurrentMenu();
        } else if ((matcher = SignUpMenuCommands.MenuExit.getMatcher(input)) != null) {
            controller.menuExit();
        }
        else if((matcher = SignUpMenuCommands.GoToLoginMenu.getMatcher(input)) != null) {
            System.out.println(controller.gotoLoginMenu().Message());
        }
        else {
            System.out.println("Invalid command");
        }
    }
}
