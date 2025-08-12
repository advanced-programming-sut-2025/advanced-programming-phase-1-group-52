package com.example.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.main.GDXmodels.DatabaseManager;
import com.example.main.GDXviews.GDXLoginMenu;
import com.example.main.GDXviews.GDXSignUpMenu;
import com.example.main.controller.MainMenuController;
import com.example.main.network.client.GameClient;
import com.example.main.views.AppView;

public class Main extends Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static Main main;
    private static SpriteBatch batch;
    private static DatabaseManager databaseManager;
    private GameClient gameClient;
    private static MainMenuController mainMenuController;


    private static String serverIp = "localhost";
    private static int serverPort = 8080;
    private static boolean isNetworkMode = false;

    public static void main(String[] args) {

        if (isNetworkMode) {

            System.out.println("Network mode enabled. Starting with login screen...");
        }
        (new AppView()).runProgram();
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        databaseManager = new DatabaseManager();
        mainMenuController = new MainMenuController();
        gameClient = new GameClient("localhost", 8080);

        if (isNetworkMode) {

            this.setScreen(new GDXLoginMenu());
        } else {

            this.setScreen(new GDXSignUpMenu());
        }
    }

    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
        if (gameClient != null && gameClient.isConnected()) {
            gameClient.disconnect();
        }
        super.dispose();
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public static Main getInstance() {
        return main;
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static MainMenuController getMainMenuController() {
        return mainMenuController;
    }



    public static void setNetworkInfo(String ip, int port) {
        serverIp = ip;
        serverPort = port;
        isNetworkMode = true;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static boolean isNetworkMode() {
        return isNetworkMode;
    }

    public GameClient getGameClient() {
        return gameClient;
    }
}
