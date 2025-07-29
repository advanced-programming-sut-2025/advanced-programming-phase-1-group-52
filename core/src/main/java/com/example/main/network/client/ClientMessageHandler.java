package com.example.main.network.client;

import java.util.HashMap;

import com.example.main.models.Game;
import com.example.main.models.User;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

/**
 * Handles incoming messages from the server
 */
public class ClientMessageHandler {
    private final GameClient client;
    
    public ClientMessageHandler(GameClient client) {
        this.client = client;
    }
    
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case AUTH_SUCCESS:
                handleAuthSuccess(message);
                break;
            case AUTH_FAILED:
                handleAuthFailed(message);
                break;
            case GAME_STATE:
                handleGameState(message);
                break;
            case PLAYER_ACTION:
                handlePlayerAction(message);
                break;
            case HEARTBEAT:
                handleHeartbeat();
                break;
            case ERROR:
                handleError(message);
                break;
            default:
                handleCustomMessage(message);
        }
    }
    
    private void handleAuthSuccess(Message message) {
        try {
            User user = message.getFromBody("user");
            client.setAuthenticatedUser(user);
            System.out.println("Authentication successful for user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("Error parsing authentication success: " + e.getMessage());
        }
    }
    
    private void handleAuthFailed(Message message) {
        try {
            String reason = message.getFromBody("reason");
            System.err.println("Authentication failed: " + reason);
            client.setAuthenticatedUser(null);
        } catch (Exception e) {
            System.err.println("Error parsing authentication failed message: " + e.getMessage());
        }
    }
    
    private void handleGameState(Message message) {
        try {
            Game game = message.getFromBody("game");
            client.setCurrentGame(game);
            System.out.println("Received game state update");
        } catch (Exception e) {
            System.err.println("Error parsing game state: " + e.getMessage());
        }
    }
    
    private void handlePlayerAction(Message message) {
        try {
            String action = message.getFromBody("action");
            Object actionData = message.getFromBody("actionData");
            String senderId = message.getFromBody("senderId");
            System.out.println("Received player action: " + action + " from " + senderId);
            
            // Here you would integrate with your game logic to handle the action
            // For example, update the local game state based on the action
        } catch (Exception e) {
            System.err.println("Error parsing player action: " + e.getMessage());
        }
    }
    
    private void handleTurnChange(Message message) {
        String playerId = message.getFromBody("playerId");
        System.out.println("Turn changed to player: " + playerId);
        // Here you would update the UI to show whose turn it is
    }
    
    private void handleHeartbeat() {
        // Simply acknowledge the heartbeat
        sendHeartbeatMessage();
    }
    
    private void handleError(Message message) {
        String error = message.getFromBody("error");
        System.err.println("Server error: " + error);
    }
    
    private void handleCustomMessage(Message message) {
        // Override this method in your game-specific implementation
        // to handle custom game messages
        System.out.println("Received custom message: " + message.getType());
    }
    
    private void sendHeartbeatMessage() {
        HashMap<String, Object> heartbeatData = new HashMap<>();
        Message heartbeatMessage = new Message(heartbeatData, MessageType.HEARTBEAT);
        client.sendMessage(heartbeatMessage);
    }
} 