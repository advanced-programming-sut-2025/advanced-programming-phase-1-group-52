package controllers;

import enums.Menu;
import enums.regex.SignUpMenuCommands;
import models.App;
import models.Result;
import models.User;

public class ProfileMenuController {
    public Result changeUsername(String username) {
        if (username.length() < 3) {
            return new Result(false, "Username must be at least 3 characters");
        }
        if (!username.matches(String.valueOf(SignUpMenuCommands.ValidUsername))) {
            return new Result(false, "Username is invalid");
        }
        for (User user : App.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
                return new Result(false, "Username is already taken");
            }
        }

        App.getInstance().getCurrentUser().setUsername(username);
        return new Result(true, "Username changed successfully.");
    }

    public Result changeNickname(String nickname) {
        for (User user : App.getInstance().getUsers()) {
            if (user.getNickname().equals(nickname)) {
                return new Result(false, "Nickname is already taken");
            }
        }

        App.getInstance().getCurrentUser().setNickname(nickname);
        return new Result(true, "Nickname changed successfully.");
    }

    public Result changeEmail(String email) {
        if (!email.matches(String.valueOf(SignUpMenuCommands.ValidEmail))) {
            return new Result(false, "Email format is invalid");
        }

        for (User user : App.getInstance().getUsers()) {
            if (user.getEmail().equals(email)) {
                return new Result(false, "Email is already taken");
            }
        }

        App.getInstance().getCurrentUser().setEmail(email);
        return new Result(true, "Email changed successfully.");
    }

    public Result changePassword(String newPassword, String oldPassword) {
        User user = App.getInstance().getCurrentUser();

        if (!user.getPassword().equals(oldPassword)) {
            return new Result(false, "Old password is incorrect");
        }

        if (newPassword.length() < 8) {
            return new Result(false, "Password must be at least 8 characters");
        }
        if (!newPassword.matches(String.valueOf(SignUpMenuCommands.ValidDigit))) {
            return new Result(false, "Password must contain at least one digit");
        }
        if (!newPassword.matches(String.valueOf(SignUpMenuCommands.ValidLower))) {
            return new Result(false, "Password must contain at least one lowercase letter");
        }
        if (!newPassword.matches(String.valueOf(SignUpMenuCommands.ValidUpper))) {
            return new Result(false, "Password must contain at least one uppercase letter");
        }
        if (!newPassword.matches(String.valueOf(SignUpMenuCommands.ValidSpecial))) {
            return new Result(false, "Password must contain at least one special character");
        }

        user.setPassword(newPassword);
        return new Result(true, "Password changed successfully.");

    }

    public void userInfo() {
        User user = App.getInstance().getCurrentUser();
        System.out.println("Username: " + user.getUsername());
        System.out.println("Nickname: " + user.getNickname());
    }

    public void showCurrentMenu() {
        System.out.println("Current menu: " + App.getInstance().getCurrentMenu().name());
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }
}
