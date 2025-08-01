package com.example.main.controller;

import java.util.Scanner;

import com.example.main.auth.AuthManager;
import com.example.main.auth.AuthResult;
import com.example.main.enums.Menu;
import com.example.main.enums.regex.SecurityQuestion;
import com.example.main.enums.regex.SignUpMenuCommands;
import com.example.main.models.App;
import com.example.main.models.Result;
import com.example.main.models.User;

public class LoginMenuController {
    private boolean awaitingNewPassword = false;

    public Result login(String username, String password) {
        AuthManager authManager = AuthManager.getInstance();
        AuthResult result = authManager.authenticate(username, password);
        
        if (result.isSuccess()) {
            User user = authManager.getUser(username);
            App.getInstance().setCurrentUser(user);
            App.getInstance().setCurrentMenu(Menu.MainMenu);
            return new Result(true, "Login Successful.");
        } else {
            return new Result(false, result.getMessage());
        }
    }

    public Result forgetPassword(String username, Scanner scanner) {
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
        System.out.println("security question: " + question);
        String answer = scanner.nextLine().trim();
        Result result = answer(answer);
        if(result.isSuccessful()){
            System.out.println(result.Message());
            String newPassword = scanner.nextLine().trim();
            user.setPassword(newPassword);
        }
        else{
            return new Result(false,"Wrong answer.");
        }
        return new Result(true,"password changed successfully");
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
    public Result resetPasswordProcess(String username, String answer, String newPassword) {
        User user = App.getInstance()
            .getUsers()
            .stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);

        if (user == null) {
            return new Result(false, "No user account found with the provided Username.");
        }

        if (!user.getSecurityAnswer().equals(answer)) {
            return new Result(false, "Wrong answer to security question.");
        }

        if (newPassword.length() < 8) {
            return new Result(false, "Password must be at least 8 characters");
        }
        if (SignUpMenuCommands.ValidDigit.getMatcher(newPassword) == null) {
            return new Result(false, "Password must contain a digit");
        }
        if (SignUpMenuCommands.ValidLower.getMatcher(newPassword) == null) {
            return new Result(false, "Password must contain a lowercase letter");
        }
        if (SignUpMenuCommands.ValidUpper.getMatcher(newPassword) == null) {
            return new Result(false, "Password must contain an uppercase letter");
        }
        if (SignUpMenuCommands.ValidSpecial.getMatcher(newPassword) == null) {
            return new Result(false, "Password must contain a special character");
        }

        user.setPassword(newPassword);
        App.getInstance().updateUserData();
        App.getInstance().setCurrentUser(null);
        App.getInstance().setCurrentMenu(Menu.LoginMenu);

        return new Result(true, "Password reset successfully. You can now login.");
    }

    public Result logout() {
        App.getInstance().setCurrentUser(null);
        return new Result(true, "Logout successful.");
    }

    public Result gotoSignUpMenu() {
        App.getInstance().setCurrentMenu(Menu.SignUpMenu);
        return new Result(true, "you are in sign up menu now!");
    }

    public void showCurrentMenu() {
        System.out.println("Current menu: " + App.getInstance().getCurrentMenu().name());
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }


}
