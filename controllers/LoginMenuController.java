package controllers;

import enums.Menu;
import enums.regex.SecurityQuestion;
import enums.regex.SignUpMenuCommands;
import models.App;
import models.Result;
import models.User;

public class LoginMenuController {
    private boolean awaitingNewPassword = false;

    public Result login(String username, String password) {
        User foundUser = null;
        for (User user : App.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
                foundUser = user;
            }
        }
        if (foundUser == null) {
            return new Result(false,"No user account found with the provided Username.");
        }
        if (!foundUser.getPassword().equals(password)) {
            return new Result(false,"Wrong password.");
        }

        App.getInstance().setCurrentUser(foundUser);
        App.getInstance().setCurrentMenu(Menu.MainMenu);

        return new Result(true,"Login Successful.");
    }

    public Result forgetPassword(String username) {
        User user = App.getInstance()
                .getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return new Result(false,"No user account found with the provided Username.");
        }
        App.getInstance().setCurrentUser(user);

        SecurityQuestion question = user.getSecurityQuestion();

        return new Result(true, "Security question: " + question.getQuestionText());
    }

    public Result answer(String answer) {
        User user = App.getInstance().getCurrentUser();

        if (!user.getSecurityAnswer().equals(answer)) {
            App.getInstance().setCurrentUser(null);
            App.getInstance().setCurrentMenu(Menu.LoginMenu);
            return new Result(false, "Wrong answer. Returning to login menu.");
        }

        awaitingNewPassword = true;
        return new Result(true, "Answer correct. Please enter your new password:");
    }

    public Result resetPassword(String password) {
        User user = App.getInstance().getCurrentUser();

        if (user == null || !awaitingNewPassword) {
            return new Result(false, "No password reset in progress.");
        }
        if (password.length() < 8) {
            return new Result(false, "Password must be at least 8 characters");
        }
        if (!password.matches(String.valueOf(SignUpMenuCommands.ValidDigit))) {
            return new Result(false, "Password must be digit");
        }
        if (!password.matches(String.valueOf(SignUpMenuCommands.ValidLower))) {
            return new Result(false, "Password must be lower case letter");
        }
        if (!password.matches(String.valueOf(SignUpMenuCommands.ValidUpper))) {
            return new Result(false, "Password must be upper case letter");
        }
        if (!password.matches(String.valueOf(SignUpMenuCommands.ValidSpecial))) {
            return new Result(false, "Password must be special character");
        }

        user.setPassword(password);
        App.getInstance().setCurrentUser(null);
        App.getInstance().setCurrentMenu(Menu.LoginMenu);
        awaitingNewPassword = false;

        return new Result(true, "Password reset successfully. You can now login.");
    }

    public void showCurrentMenu() {
        System.out.println("Current menu: " + App.getInstance().getCurrentMenu().name());
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }


}
