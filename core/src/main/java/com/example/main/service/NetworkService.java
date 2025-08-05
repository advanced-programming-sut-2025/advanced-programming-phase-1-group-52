package com.example.main.service;

import com.example.main.controller.NetworkLobbyController; // <-- Add this import
import com.example.main.models.User;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import com.example.main.models.Game;
import com.example.main.network.NetworkConstants;
import com.example.main.network.client.GameClient;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

/**
 * Service class that integrates network functionality with the game
 */
public class NetworkService {
    private User currentUser;
    private GameClient client;
    private final AtomicBoolean isConnected;
    private final AtomicBoolean isAuthenticated;
    private Game currentGame;

    // **FIX 1**: Add a field for the single, persistent lobby controller.
    private final NetworkLobbyController lobbyController;

    public NetworkService() {
        this.isConnected = new AtomicBoolean(false);
        this.isAuthenticated = new AtomicBoolean(false);
        // **FIX 2**: Instantiate the controller here, in the constructor.
        // It will now live as long as the NetworkService does.
        this.lobbyController = new NetworkLobbyController(this);
    }

    /**
     * **FIX 3**: Add a getter for the lobby controller.
     * This allows any screen to access the shared instance.
     */
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
                // **FIX 4**: CRITICAL - After connecting, set the persistent lobby controller
                // as the callback for the GameClient. This is the missing link.
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
            // The ClientMessageHandler now sets the current user in the App singleton.
            // This line can be simplified or removed if the handler does it all.
            currentUser = client.getAuthenticatedUser();
        }
        return authenticated;
    }

    // ... No other changes are needed below this point ...

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
