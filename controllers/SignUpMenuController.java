package controllers;

import enums.Menu;
import enums.player.Gender;
import enums.regex.SecurityQuestion;
import enums.regex.SignUpMenuCommands;
import models.App;
import models.Result;
import models.User;

import java.util.Scanner;
import java.util.regex.Matcher;

public class SignUpMenuController {
    public Result register(String username, String password, String passwordConfirm, String nickname, String email, String gender, Scanner scanner) {
        if (username.length() < 3) {
            return new Result(false, "Username must be at least 3 characters");
        }
        if (SignUpMenuCommands.ValidUsername.getMatcher(username) == null) {
            return new Result(false, "Username is invalid");
        }
        for (User users : App.getInstance().getUsers()) {
            if (users.getUsername().equals(username)){
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
}
