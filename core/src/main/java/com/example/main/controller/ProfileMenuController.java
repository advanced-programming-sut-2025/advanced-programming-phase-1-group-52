package com.example.main.controller;

import com.example.main.enums.Menu;
import com.example.main.enums.regex.SignUpMenuCommands;
import com.example.main.models.App;
import com.example.main.models.Result;
import com.example.main.models.User;

public class ProfileMenuController {
    public Result changeUsername(String username) {
        if (username.length() < 3) {
            return new Result(false, "Username must be at least 3 characters");
        }
        if (SignUpMenuCommands.ValidUsername.getMatcher(username) == null) {
            return new Result(false, "Username is invalid");
        }
        for (User user : App.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
                return new Result(false, "Username is already taken");
            }
        }

        App.getInstance().getCurrentUser().setUsername(username);
        App.getInstance().updateUserData(); // Save data after successful change
        return new Result(true, "Username changed successfully.");
    }

    public Result changeNickname(String nickname) {
        for (User user : App.getInstance().getUsers()) {
            if (user.getNickname().equals(nickname)) {
                return new Result(false, "Nickname is already taken");
            }
        }

        App.getInstance().getCurrentUser().setNickname(nickname);
        App.getInstance().updateUserData(); // Save data after successful change
        return new Result(true, "Nickname changed successfully.");
    }

    public Result changeEmail(String email) {
        if (SignUpMenuCommands.ValidEmail.getMatcher(email) == null) {
            return new Result(false, "Email format is invalid");
        }

        for (User user : App.getInstance().getUsers()) {
            if (user.getEmail().equals(email)) {
                return new Result(false, "Email is already taken");
            }
        }

        App.getInstance().getCurrentUser().setEmail(email);
        App.getInstance().updateUserData(); // Save data after successful change
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
        if (SignUpMenuCommands.ValidDigit.getMatcher(newPassword) == null) {
            return new Result(false, "Password must contain at least one digit");
        }
        if (SignUpMenuCommands.ValidLower.getMatcher(oldPassword) == null) {
            return new Result(false, "Password must contain at least one lowercase letter");
        }
        if (SignUpMenuCommands.ValidUpper.getMatcher(newPassword) == null) {
            return new Result(false, "Password must contain at least one uppercase letter");
        }
        if (SignUpMenuCommands.ValidSpecial.getMatcher(newPassword) == null) {
            return new Result(false, "Password must contain at least one special character");
        }

        user.setPassword(newPassword);
        App.getInstance().updateUserData(); // Save data after successful change
        return new Result(true, "Password changed successfully.");

    }

    public Result userInfo() {
        User user = App.getInstance().getCurrentUser();
        StringBuilder userDetail = new StringBuilder();
        userDetail.append("Username: " + user.getUsername()).append("\n");
        userDetail.append("Nickname: " + user.getNickname()).append("\n");
        userDetail.append("Number of game played: " + user.getNumPlayed()).append("\n");
        userDetail.append("High score: " + user.getHighScore());
        return new Result(true, userDetail.toString());
    }

    public Result gotoMainMenu() {
        App.getInstance().setCurrentMenu(Menu.MainMenu);
        return new Result(true, "You are now at main menu now");
    }

    public void showCurrentMenu() {
        System.out.println("Current menu: " + App.getInstance().getCurrentMenu().name());
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }
}
