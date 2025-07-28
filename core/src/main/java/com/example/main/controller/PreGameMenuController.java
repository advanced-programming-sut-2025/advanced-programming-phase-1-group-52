package com.example.main.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.example.main.enums.design.FarmThemes;
import com.example.main.models.App;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.Player;
import com.example.main.models.Result;
import com.example.main.models.User;

public class PreGameMenuController {

    public Result startNewGame(String username2, String username3, String username4, int map1, int map2, int map3, int map4) {
        App app = App.getInstance();
        User loggedInUser = app.getCurrentUser();

        if (loggedInUser == null) {
            return new Result(false, "No user is logged in.");
        }

        if (username2.trim().isEmpty() || username3.trim().isEmpty() || username4.trim().isEmpty()) {
            return new Result(false, "Please enter usernames for all three players.");
        }

        Set<String> usernames = new HashSet<>();
        usernames.add(username2);
        usernames.add(username3);
        usernames.add(username4);
        if (usernames.size() < 3) {
            return new Result(false, "Please enter three different usernames.");
        }

        // Check if any of the entered usernames are the same as the logged-in user
        if (loggedInUser.getUsername().equals(username2) || loggedInUser.getUsername().equals(username3) || loggedInUser.getUsername().equals(username4)) {
            return new Result(false, "You cannot invite yourself to the game.");
        }

        User user2 = findUser(username2);
        User user3 = findUser(username3);
        User user4 = findUser(username4);

        if (user2 == null) {
            return new Result(false, "Player '" + username2 + "' not found.");
        }
        if (user3 == null) {
            return new Result(false, "Player '" + username3 + "' not found.");
        }
        if (user4 == null) {
            return new Result(false, "Player '" + username4 + "' not found.");
        }

        ArrayList<User> players = new ArrayList<>();
        players.add(loggedInUser);
        players.add(user2);
        players.add(user3);
        players.add(user4);

        for (User user : players) {
            if (!isUserAvailable(user)) {
                return new Result(false, "User " + user.getUsername() + " is already in a game.");
            }
        }

        Player p1 = new Player(loggedInUser.getUsername(), loggedInUser.getGender());
        p1.setOriginX(4);
        p1.setOriginY(4);
        p1.setCurrentX(4);
        p1.setCurrentY(4);
        p1.setTrashCanX(9);
        p1.setTrashCanY(1);
        loggedInUser.setCurrentPlayer(p1);

        Player p2 = new Player(user2.getUsername(), user2.getGender());
        p2.setOriginX(84);
        p2.setOriginY(4);
        p2.setCurrentX(84);
        p2.setCurrentY(4);
        p2.setTrashCanX(80);
        p2.setTrashCanY(1);
        user2.setCurrentPlayer(p2);

        Player p3 = new Player(user3.getUsername(), user3.getGender());
        p3.setOriginX(4);
        p3.setOriginY(34);
        p3.setCurrentX(4);
        p3.setCurrentY(34);
        p3.setTrashCanX(9);
        p3.setTrashCanY(31);
        user3.setCurrentPlayer(p3);

        Player p4 = new Player(user4.getUsername(), user4.getGender());
        p4.setOriginX(84);
        p4.setOriginY(34);
        p4.setCurrentX(84);
        p4.setCurrentY(34);
        p4.setTrashCanX(80);
        p4.setTrashCanY(31);
        user4.setCurrentPlayer(p4);

        Game newGame = new Game(players);
        newGame.setMainPlayer(loggedInUser);
        app.addGame(newGame);
        app.setCurrentGame(newGame);

        ArrayList<FarmThemes> farmThemes = new ArrayList<>();
        farmThemes.add(FarmThemes.values()[map1]);
        farmThemes.add(FarmThemes.values()[map2]);
        farmThemes.add(FarmThemes.values()[map3]);
        farmThemes.add(FarmThemes.values()[map4]);

        GameMap gameMap = new GameMap(newGame.getPlayers(), farmThemes);
        newGame.setGameMap(gameMap);


        return new Result(true, "New game created successfully! You can start the game now.");
    }

    private User findUser(String username) {
        for (User user : App.getInstance().getUsers()) {
            if (user.getUsername() != null && user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private boolean isUserAvailable(User user) {
        for (Game game : App.getInstance().getGames()) {
            if (game.getPlayers().contains(user)) {
                return false;
            }
        }
        return true;
    }
}
