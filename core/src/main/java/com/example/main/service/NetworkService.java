package com.example.main.service;

import com.example.main.controller.NetworkLobbyController;
import com.example.main.models.User;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import com.example.main.models.Game;
import com.example.main.network.NetworkConstants;
import com.example.main.network.client.GameClient;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;


public class NetworkService {
    private User currentUser;
    private GameClient client;
    private final AtomicBoolean isConnected;
    private final AtomicBoolean isAuthenticated;
    private Game currentGame;


    private final NetworkLobbyController lobbyController;

    public NetworkService() {
        this.isConnected = new AtomicBoolean(false);
        this.isAuthenticated = new AtomicBoolean(false);


        this.lobbyController = new NetworkLobbyController(this);
    }


    public NetworkLobbyController getLobbyController() {
        return this.lobbyController;
    }

    public boolean connectToServer(String host, int port) {
        if (isConnected.get() && client != null) {
            System.out.println("Already connected to server, reusing existing connection");
            return true;
        }
        System.out.println("Creating new connection to server at " + host + ":" + port);
        try {
            client = new GameClient(host, port);
            boolean connected = client.connect();
            isConnected.set(connected);
            if (connected) {


                client.setControllerCallback(this.lobbyController);
                System.out.println("Successfully connected to server and set lobby controller callback.");
            } else {
                System.out.println("Failed to connect to server");
            }
            return connected;
        } catch (Exception e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }

    public boolean authenticate(String username, String password) {
        if (!isConnected.get()) {
            System.err.println("Not connected to server");
            return false;
        }
        boolean authenticated = client.authenticate(username, password);
        isAuthenticated.set(authenticated);
        if (authenticated) {


            currentUser = client.getAuthenticatedUser();
        }
        return authenticated;
    }



    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public void sendPlayerAction(String action, Object actionData) {
        client.sendPlayerAction(action, actionData);
    }
    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
        isConnected.set(false);
        isAuthenticated.set(false);
        currentUser = null;
        currentGame = null;
    }
    public void updateGameState(Game game) {
        this.currentGame = game;
    }
    public Game getCurrentGame() {
        return currentGame;
    }
    public boolean isConnected() {
        return isConnected.get();
    }
    public boolean isAuthenticated() {
        return isAuthenticated.get();
    }
    public GameClient getClient() {
        return client;
    }
    public void setControllerCallback(Object callback) {
        if (client != null) {
            client.setControllerCallback(callback);
        }
    }
    public boolean connectToDefaultServer() {
        return connectToServer(NetworkConstants.DEFAULT_HOST, NetworkConstants.DEFAULT_PORT);
    }
    public void sendCustomMessage(String messageType, Object data) {
        HashMap<String, Object> messageData = new HashMap<>();
        messageData.put("type", messageType);
        messageData.put("data", data);
        Message message = new Message(messageData, MessageType.PLAYER_ACTION);
        client.sendMessage(message);
    }
    public void sendMessage(Message message) {
        if (client != null) {
            client.sendMessage(message);
        }
    }
}
