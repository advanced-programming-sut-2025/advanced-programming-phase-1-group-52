package com.example.main.service;

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

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    private GameClient client;
    private final AtomicBoolean isConnected;
    private final AtomicBoolean isAuthenticated;
    private Game currentGame;
    
    public NetworkService() {
        this.isConnected = new AtomicBoolean(false);
        this.isAuthenticated = new AtomicBoolean(false);
    }
    
    /**
     * Connects to the game server
     * @param host Server host
     * @param port Server port
     * @return true if connection successful
     */
    public boolean connectToServer(String host, int port) {
        // If already connected, don't create a new connection
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
                System.out.println("Successfully connected to server");
            } else {
                System.out.println("Failed to connect to server");
            }
            return connected;
        } catch (Exception e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Authenticates with the server
     * @param username Username
     * @param password Password
     * @return true if authentication successful
     */
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
    
    /**
     * Sends a player action to the server
     * @param action Action type (e.g., "MOVE", "PLANT", "HARVEST")
     * @param actionData Action data
     */
    public void sendPlayerAction(String action, Object actionData) {
        // No authentication required for player actions
        client.sendPlayerAction(action, actionData);
    }
    
    /**
     * Disconnects from the server
     */
    public void disconnect() {
        if (client != null) {
            client.disconnect();
        }
        isConnected.set(false);
        isAuthenticated.set(false);
        currentUser = null;
        currentGame = null;
    }
    
    /**
     * Updates the local game state
     * @param game New game state
     */
    public void updateGameState(Game game) {
        this.currentGame = game;
    }
    

    
    /**
     * Gets the current game state
     * @return Current game or null if not available
     */
    public Game getCurrentGame() {
        return currentGame;
    }
    
    /**
     * Checks if connected to server
     * @return true if connected
     */
    public boolean isConnected() {
        return isConnected.get();
    }
    
    /**
     * Checks if authenticated with server
     * @return true if authenticated
     */
    public boolean isAuthenticated() {
        return isAuthenticated.get();
    }
    
    /**
     * Gets the underlying client instance
     * @return GameClient instance
     */
    public GameClient getClient() {
        return client;
    }
    
    public void setControllerCallback(Object callback) {
        if (client != null) {
            client.setControllerCallback(callback);
        }
    }
    
    /**
     * Connects to the default server
     * @return true if connection successful
     */
    public boolean connectToDefaultServer() {
        return connectToServer(NetworkConstants.DEFAULT_HOST, NetworkConstants.DEFAULT_PORT);
    }
    
    /**
     * Sends a custom message to the server
     * @param messageType Type of message
     * @param data Message data
     */
    public void sendCustomMessage(String messageType, Object data) {
        // No authentication required for custom messages
        HashMap<String, Object> messageData = new HashMap<>();
        messageData.put("type", messageType);
        messageData.put("data", data);
        Message message = new Message(messageData, MessageType.PLAYER_ACTION);
        client.sendMessage(message);
    }
    
    /**
     * Sends a message to the server
     * @param message Message to send
     */
    public void sendMessage(Message message) {
        // No authentication required for sending messages
        if (client != null) {
            client.sendMessage(message);
        }
    }
} 