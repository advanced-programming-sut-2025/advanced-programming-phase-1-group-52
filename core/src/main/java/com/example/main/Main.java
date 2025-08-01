package com.example.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.main.GDXmodels.DatabaseManager;
import com.example.main.GDXviews.GDXLoginMenu;
import com.example.main.GDXviews.GDXSignUpMenu;
import com.example.main.views.AppView;

public class Main extends Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static Main main;
    private static SpriteBatch batch;
    private static DatabaseManager databaseManager;
    
    // Network configuration
    private static String serverIp = "localhost";
    private static int serverPort = 8080;
    private static boolean isNetworkMode = false;

    public static void main(String[] args) {
        // Check if we're in network mode
        if (isNetworkMode) {
            // In network mode, start with the login screen
            System.out.println("Network mode enabled. Starting with login screen...");
        }
        (new AppView()).runProgram();
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        databaseManager = new DatabaseManager();

        if (isNetworkMode) {
            // In network mode, start with login screen
            this.setScreen(new GDXLoginMenu());
        } else {
            // In single player mode, start with sign up screen
            this.setScreen(new GDXSignUpMenu());
        }
    }

    @Override
    public void render() {
        super.render();
    }
    @Override
    public void dispose() {
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
    
    // Network methods
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
}
