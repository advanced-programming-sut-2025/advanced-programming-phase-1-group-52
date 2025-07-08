package com.example.main.controller;

import com.example.main.models.App;
import com.example.main.models.Result;
import com.example.main.models.User;
import com.example.main.models.Game;
import com.example.main.models.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PreGameMenuController {

    public Result startNewGame(String username1, String username2, String username3) {
        App app = App.getInstance();
        User loggedInUser = app.getCurrentUser();

        if (loggedInUser == null) {
            return new Result(false, "No user is logged in.");
        }

        // Check for empty usernames
        if (username1.trim().isEmpty() || username2.trim().isEmpty() || username3.trim().isEmpty()) {
            return new Result(false, "Please enter usernames for all three players.");
        }

        // Check for duplicate usernames among the entered names
        Set<String> usernames = new HashSet<>();
        usernames.add(username1);
        usernames.add(username2);
        usernames.add(username3);
        if (usernames.size() < 3) {
            return new Result(false, "Please enter three different usernames.");
        }

        // Check if any of the entered usernames are the same as the logged-in user
        if (loggedInUser.getUsername().equals(username1) || loggedInUser.getUsername().equals(username2) || loggedInUser.getUsername().equals(username3)) {
            return new Result(false, "You cannot invite yourself to the game.");
        }

        User user1 = findUser(username1);
        User user2 = findUser(username2);
        User user3 = findUser(username3);

        if (user1 == null) {
            return new Result(false, "Player '" + username1 + "' not found.");
        }
        if (user2 == null) {
            return new Result(false, "Player '" + username2 + "' not found.");
        }
        if (user3 == null) {
            return new Result(false, "Player '" + username3 + "' not found.");
        }

        ArrayList<User> players = new ArrayList<>();
        players.add(loggedInUser);
        players.add(user1);
        players.add(user2);
        players.add(user3);

        // Check if users are available
        for (User user : players) {
            if (!isUserAvailable(user)) {
                return new Result(false, "User " + user.getUsername() + " is already in a game.");
            }
        }

        // Create Player objects and set their initial state
        Player p1 = new Player(loggedInUser.getUsername(), loggedInUser.getGender());
        loggedInUser.setCurrentPlayer(p1);

        Player p2 = new Player(user1.getUsername(), user1.getGender());
        user1.setCurrentPlayer(p2);

        Player p3 = new Player(user2.getUsername(), user2.getGender());
        user2.setCurrentPlayer(p3);

        Player p4 = new Player(user3.getUsername(), user3.getGender());
        user3.setCurrentPlayer(p4);

        Game newGame = new Game(players);
        newGame.setMainPlayer(loggedInUser);
        app.addGame(newGame);
        app.setCurrentGame(newGame);

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
