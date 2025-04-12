package models;

import enums.Menu;

import java.util.ArrayList;

public class App {
    private static App instance = null;
    private final ArrayList<User> users;
    private final ArrayList<Game> games;
    private Menu currentMenu;
    private User currentUser;

    private App() {
        users = new ArrayList<>();
        games = new ArrayList<>();
        currentMenu = Menu.LoginMenu;
    }

    public App getInstance() {
        if (instance == null) instance = new App();
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
