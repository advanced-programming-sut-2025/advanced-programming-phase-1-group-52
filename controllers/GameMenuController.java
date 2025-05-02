package controllers;

import enums.regex.GameMenuCommands;
import models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

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
        ArrayList<User> players = new ArrayList<>();
        User loggedInUser = App.getInstance().getCurrentUser();

        Player player1 = new Player(loggedInUser.getUsername());
        loggedInUser.setCurrentPlayer(player1);
        players.add(loggedInUser);

        Player player2 = new Player(user1.getUsername());
        user1.setCurrentPlayer(player2);
        players.add(user1);


        Player player3 = new Player(user2.getUsername());
        user2.setCurrentPlayer(player3);
        players.add(user2);


        Player player4 = new Player(user3.getUsername());
        user3.setCurrentPlayer(player4);
        players.add(user3);

        Game newGame = new Game(players);
        newGame.setMainPlayer(loggedInUser);
        App.getInstance().addGames(newGame);
        App.getInstance().setCurrentGame(newGame);
        return new Result(true, "Now Choose your map!");
    }

    public Result chooseMap(User user, String mapIdStr) {
        // todo : get each user map in game view
        // todo : print options for users in game view
        // todo : check the other error
        int mapId = Integer.parseInt(mapIdStr);
        if (mapId > 3 || mapId < 1) {
            return new Result(false, "Invalid map id, please try again");
        }
        Player userPlayer = user.currentPlayer();
        // todo : return player and mapId to be built
        return new Result(true,user.getUsername() + "'s map is " + mapId);
    }

    public Result loadMap() {
        if(App.getInstance().getCurrentUser().userGame() == null) {
            return new Result(false, "You are not in a game");
        }
        // todo : load game
        return new Result(true, "Your last game is loaded");
    }

    public Result exitGame() {
        Game game = App.getInstance().getCurrentGame();
        User loggedInUser = App.getInstance().getCurrentUser();
        if(game.mainPlayer().equals(loggedInUser)){
            // todo : exit the game and go to game menu
            return new Result(true, "You are in game menu now");
        }
        else{
            return new Result(false, "You are not the creator of this game");
        }
    }

    public Result terminateGame() {
        return new Result(true, "Game terminated");
    }

    public Result switchTurn(){
        Game game = App.getInstance().getCurrentGame();
        game.switchCurrentPlayer();
        return new Result(true, "Game switched to " + game.currentPlayer().username() + " ");
    }

    public Result showTime(){
        Game game = App.getInstance().getCurrentGame();
        return new Result(true, "It's " + game.time().hour() + "O'clock");
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
