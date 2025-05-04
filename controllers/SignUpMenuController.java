package controllers;

import enums.Menu;
import enums.player.Gender;
import enums.regex.SecurityQuestion;
import enums.regex.SignUpMenuCommands;
import models.App;
import models.Result;
import models.User;

public class SignUpMenuController {
    public Result register(String username, String password, String passwordConfirm, String nickname, String email, String gender) {
        if (username.length() < 3) {
            return new Result(false, "Username must be at least 3 characters");
        }
        if (!username.matches(String.valueOf(SignUpMenuCommands.ValidUsername))) {
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
        if (!email.matches(String.valueOf(SignUpMenuCommands.ValidEmail))) {
            return new Result(false, "Email is invalid");
        }
        Gender genderEnum = Gender.valueOf(gender);
        User newUser = new User (username, password, nickname, email, genderEnum);
        App.getInstance().getUsers().add(newUser);

        return new Result(true, "User registered successfully");

    }

    public Result pickQuestion(String questionNumber, String answer, String answerConfirm) {
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
        User user = App.getInstance().getUsers()
                .get(App.getInstance().getUsers().size() - 1);

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
}
