package models;

import enums.Menu;
import java.util.ArrayList;

public class App {
    private static App instance = null;
    private final ArrayList<User> users;
    private ArrayList<Game> games;
    private Menu currentMenu;
    private User currentUser;
    private Game currentGame;

    private App() {
        users = new ArrayList<>();
        games = new ArrayList<>();
        currentMenu = Menu.LoginMenu;
    }

    public static App getInstance() {
        if (instance == null) instance = new App();
        return instance;
    }

    public ArrayList<User> getUsers() {
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

    public ArrayList<User> users() {
        return users;
    }

    public void addUsers(User user) {
        this.users.add(user);
    }
}
