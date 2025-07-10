package com.example.main.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

import com.example.main.enums.Menu;
import com.example.main.enums.player.Gender;
import com.example.main.enums.regex.SecurityQuestion;
import com.example.main.enums.regex.SignUpMenuCommands;
import com.example.main.models.App;
import com.example.main.models.Result;
import com.example.main.models.User;

public class    SignUpMenuController {
    public Result register(
        String username,
        String password,
        String passwordConfirm,
        String nickname,
        String email,
        String gender,
        SecurityQuestion question,
        String answer,
        String answerConfirm
    ) {
        if (username.length() < 3) {
            return new Result(false, "Username must be at least 3 characters");
        }
        if (SignUpMenuCommands.ValidUsername.getMatcher(username) == null) {
            return new Result(false, "Username is invalid");
        }
        for (User users : App.getInstance().getUsers()) {
            if (users.getUsername() != null && users.getUsername().equals(username)){
                return new Result(false, "This username is already taken!");
            }
        }
        if(!password.equals(passwordConfirm)){
            return new Result(false, "Passwords do not match");
        }
        if (password.length() < 8) {
            return new Result(false, "Password must be at least 8 characters");
        }
        if (SignUpMenuCommands.ValidDigit.getMatcher(password) == null) {
            return new Result(false, "Password must contain digit");
        }
        if (SignUpMenuCommands.ValidLower.getMatcher(password) == null) {
            return new Result(false, "Password must contain lowercase letter");
        }
        if (SignUpMenuCommands.ValidUpper.getMatcher(password) == null) {
            return new Result(false, "Password must contain uppercase letter");
        }
        if (SignUpMenuCommands.ValidSpecial.getMatcher(password) == null) {
            return new Result(false, "Password must contain special character");
        }
        if (SignUpMenuCommands.ValidEmail.getMatcher(email) == null) {
            return new Result(false, "Email is invalid");
        }

        if (!answer.equals(answerConfirm)) {
            return new Result(false, "Security answers do not match");
        }
        if (answer.trim().isEmpty()) {
            return new Result(false, "Security answer cannot be empty");
        }

        Gender genderEnum = Gender.valueOf(gender);
        User newUser = new User(username, password, nickname, email, genderEnum);
        newUser.setSecurityQuestion(question);
        newUser.setSecurityAnswer(answer);

        App.getInstance().addUsers(newUser);
        App.getInstance().setCurrentMenu(Menu.LoginMenu);

        return new Result(true, "User registered successfully");
    }

    public Result register(String username, String password, String passwordConfirm, String nickname, String email, String gender, Scanner scanner) {
        if (username.length() < 3) {
            return new Result(false, "Username must be at least 3 characters");
        }
        if (SignUpMenuCommands.ValidUsername.getMatcher(username) == null) {
            return new Result(false, "Username is invalid");
        }
        for (User users : App.getInstance().getUsers()) {
            if (users.getUsername() != null && users.getUsername().equals(username)){
                return new Result(false, "this username is already taken!");
            }
        }
        if(!password.equals(passwordConfirm)){
            return new Result(false, "Passwords do not match");
        }
        if (password.length() < 8) {
            return new Result(false, "Password must be at least 8 characters");
        }
        if (SignUpMenuCommands.ValidDigit.getMatcher(password) == null) {
            return new Result(false, "Password must contain digit");
        }
        if (SignUpMenuCommands.ValidLower.getMatcher(password) == null) {
            return new Result(false, "Password must contain lowercase letter");
        }
        if (SignUpMenuCommands.ValidUpper.getMatcher(password) == null) {
            return new Result(false, "Password must contain uppercase letter");
        }
        if (SignUpMenuCommands.ValidSpecial.getMatcher(password) == null) {
            return new Result(false, "Password must contain special character");
        }
        if (SignUpMenuCommands.ValidEmail.getMatcher(email) == null) {
            return new Result(false, "Email is invalid");
        }
        Gender genderEnum = Gender.valueOf(gender);
        System.out.println("Please choose a security question and answer it:");
        for (SecurityQuestion question : SecurityQuestion.values()) {
            System.out.println(question.getQuestionText());
        }
        Matcher matcher;
        Result result = null;
        String answer;
        String answerConfirm;
        String questionNumber;
        String input = scanner.nextLine().trim();
        User newUser = new User (username, password, nickname, email, genderEnum);
        if((matcher = SignUpMenuCommands.PickQuestion.getMatcher(input)) != null){
            questionNumber = matcher.group("questionNumber");
            answer = matcher.group("answer");
            answerConfirm = matcher.group("answerConfirm");
            result = pickQuestion(questionNumber, answer, answerConfirm, newUser);
        }
        if (result == null) {
            return new Result(false, "Invalid entry!");
        }
        if(result.isSuccessful()){
            App.getInstance().addUsers(newUser);
            App.getInstance().setCurrentMenu(Menu.LoginMenu);
            return new Result(true, result.Message() + "\n" + "User registered successfully");
        }
        return new Result(false, result.Message());
    }

    public Result pickQuestion(String questionNumber, String answer, String answerConfirm, User user) {
        int questionNum;
        try {
            questionNum = Integer.parseInt(questionNumber);
        } catch (NumberFormatException e) {
            return new Result(false, "Question number must be a valid integer");
        }
        SecurityQuestion[] questions = SecurityQuestion.values();
        if (questionNum < 1 || questionNum > questions.length) {
            return new Result(false,
                "Invalid question number. Must be between 1 and " + questions.length);
        }
        SecurityQuestion selected = questions[questionNum - 1];
        if (!answer.equals(answerConfirm)) {
            return new Result(false, "Answers do not match");
        }
        if (answer.trim().isEmpty()) {
            return new Result(false, "Answer cannot be empty");
        }

        user.setSecurityQuestion(selected);
        user.setSecurityAnswer(answer);

        App.getInstance().setCurrentMenu(Menu.LoginMenu);

        return new Result(true,
            "Security question set to: \"" + selected.getQuestionText() + "\". " +
                "Please use this to recover your password if needed.");
    }

    public void showCurrentMenu() {
        System.out.println("Current menu: " + App.getInstance().getCurrentMenu().name());
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }

    public Result gotoLoginMenu(){
        App.getInstance().setCurrentMenu(Menu.LoginMenu);
        return new Result(true, "you are in login menu now!");
    }

    public Result generatePassword() {
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialChars = "?><,\"';:\\/|][}{+=)(*&^%$#!";

        String allChars = lowerCase + upperCase + digits + specialChars;

        SecureRandom random = new SecureRandom();
        List<Character> passwordChars = new ArrayList<>();

        // Ensure at least one from each required group
        passwordChars.add(lowerCase.charAt(random.nextInt(lowerCase.length())));
        passwordChars.add(upperCase.charAt(random.nextInt(upperCase.length())));
        passwordChars.add(digits.charAt(random.nextInt(digits.length())));
        passwordChars.add(specialChars.charAt(random.nextInt(specialChars.length())));

        for (int i = 4; i < 8; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }
        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return new Result(true, password.toString());
    }
}
