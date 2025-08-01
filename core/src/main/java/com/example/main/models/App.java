package com.example.main.models;

import java.util.ArrayList;
import java.util.List;

import com.example.main.auth.AuthManager;
import com.example.main.enums.Menu;

public class App {
    private static App instance;
    private ArrayList<Game> games;
    private Menu currentMenu;
    private User currentUser;
    private Game currentGame;

    private App() {
        games = new ArrayList<>();
    }

    public static App getInstance() {
        if (instance == null) instance = new App();
        return instance;
    }

    public List<User> getUsers() {
        return AuthManager.getInstance().getAllUsers();
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public List<User> users() {
        return AuthManager.getInstance().getAllUsers();
    }

    public void addUsers(User user) {
        AuthManager.getInstance().registerUser(user);
    }

    public void updateUserData() {
        // User data is now managed by AuthManager
        // This method is kept for compatibility
    }
}
