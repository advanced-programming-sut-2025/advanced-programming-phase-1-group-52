package com.example.main.models;

import com.example.main.GDXmodels.DatabaseManager;
import com.example.main.enums.Menu;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static App instance;
    private List<User> users;
    private ArrayList<Game> games;
    private final DatabaseManager dbManager;
    private Menu currentMenu;
    private User currentUser;
    private Game currentGame;

    private App() {
        dbManager = new DatabaseManager();
        users = dbManager.loadUsers();
        if (users == null) {
            users = new ArrayList<>();
        }
        games = new ArrayList<>();
    }

    public static App getInstance() {
        if (instance == null) instance = new App();
        return instance;
    }

    public List<User> getUsers() {
        return users;
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
        return users;
    }

    public void addUsers(User user) {
        this.users.add(user);
        dbManager.saveUsers(users);
    }

    public void updateUserData() {
        dbManager.saveUsers(users);
    }
}
