package controllers;

import enums.regex.GameMenuCommands;
import models.App;
import models.Game;
import models.Result;
import models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameMenuController {
    public Result startNewGame(String input) {
        List<String> usernames;
        try {
            usernames = validateGameCommand(input);
        } catch (IllegalArgumentException e) {
            return new Result(false, e.getMessage());
        }

        User user1,user2,user3;

        if((user1 = findUser(usernames.get(0))) == null || (user2 = findUser(usernames.get(1))) == null || (user3 = findUser(usernames.get(2))) == null) {
            return new Result(false, "User not found, please try again");
        }

        if(!isUserAvailable(user1) || !isUserAvailable(user2) || !isUserAvailable(user3)) {
            return new Result(false, "Users are not available");
        }
        return new Result(true, "Now Choose your map!");
    }

    public Result chooseMap(String mapIdStr) {
        // todo : get each user map in game view
        int mapId = Integer.parseInt(mapIdStr);

    }
    private User findUser(String username) {
        for(User user : App.getInstance().getUsers()){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    private List<String> validateGameCommand(String command) throws IllegalArgumentException {

        // Check for -u flag
        if (GameMenuCommands.CheckFlagInNewGame.getMatcher(command) == null) {
            throw new IllegalArgumentException("Missing '-u' flag. Usage: 'game new -u <user1> <user2> <user3>'");
        }

        // Extract usernames
        Matcher usernameMatcher = GameMenuCommands.ExtractUsernames.getMatcher(command);
        List<String> usernames = new ArrayList<>();

        while (usernameMatcher.find()) {
            usernames.add(usernameMatcher.group(1));
        }

        // Validate username count
        if (usernames.size() < 3) {
            throw new IllegalArgumentException("Too few usernames. Expected 3, got " + usernames.size());
        }
        if (usernames.size() > 3) {
            throw new IllegalArgumentException("Too many usernames. Expected 3, got " + usernames.size());
        }

        return usernames;
    }

    private boolean isUserAvailable(User user) {
        for(Game game : App.getInstance().getGames()){
            if(game.players().contains(user)){
                return false;
            }
        }
        return true;
    }

}
