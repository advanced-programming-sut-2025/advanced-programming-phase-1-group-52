package com.example.main.network.client;

import com.example.main.GDXviews.GDXMainMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.example.main.GDXviews.GDXLobbyScreen;
import com.example.main.Main;
import com.example.main.models.App;
import com.example.main.controller.NetworkLobbyController;

import java.util.HashMap;
import java.util.Map;

import com.example.main.models.User;
import com.example.main.models.Game;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

/**
 * Handles incoming messages from the server
 */
public class ClientMessageHandler {
    private GameClient client;
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
            case LOBBY_JOIN:
                handleLobbyJoin(message);
                break;
            case LOBBY_JOIN_SUCCESS:
                handleLobbyJoinSuccess(message);
                break;
            case LOBBY_LEAVE:
                handleLobbyLeave(message);
                break;
            case LOBBY_INVITE:
                handleLobbyInvite(message);
                break;
            case LOBBY_INVITE_ACCEPT:
                handleLobbyInviteAccept(message);
                break;
            case LOBBY_INVITE_DECLINE:
                handleLobbyInviteDecline(message);
                break;
            case LOBBY_INVITE_NOTIFICATION:
                handleLobbyInviteNotification(message);
                break;
            case LOBBY_INVITE_RESPONSE:
                handleLobbyInviteResponse(message);
                break;
            case LOBBY_ADMIN_CHANGE:
                handleLobbyAdminChange(message);
                break;
            case LOBBY_PLAYERS_UPDATE:
                handleLobbyPlayersUpdate(message);
                break;
            case LOBBY_READY:
                handleLobbyReady(message);
                break;
            case LOBBY_START_GAME:
                handleLobbyStartGame(message);
                break;
            case ONLINE_USERS_UPDATE:
                handleOnlineUsersUpdate(message);
                break;
            default:
                handleCustomMessage(message);
        }
    }
    
    private void handleAuthSuccess(Message message) {
        try {
            System.out.println("Handling AUTH_SUCCESS message: " + message.getBody());
            // Check if this is a registration success or authentication success
            String regMessage = message.getFromBody("message");
            if (regMessage != null && regMessage.equals("User registered successfully")) {
                System.out.println("Registration successful: " + regMessage);
                // For registration, we might want to automatically log in the user
                // or redirect to login screen
                return;
            }
            
            // Handle authentication success
            // The server sends individual user fields
            String username = message.getFromBody("username");
            String nickname = message.getFromBody("nickname");
            String email = message.getFromBody("email");
            
            System.out.println("Parsed user data - Username: " + username + ", Nickname: " + nickname + ", Email: " + email);
            client.setAuthenticated(true);
            
            if (username != null && !username.isEmpty()) {
                com.example.main.service.NetworkService networkService = com.example.main.models.App.getInstance().getNetworkService();
                if (networkService != null) {
                    User user = new User();
                    user.setUsername(username);
                    user.setNickname(nickname);
                    user.setEmail(email);
                    App.getInstance().setCurrentUser(user);
                    networkService.setCurrentUser(user);
                    System.out.println("Authenticated user set in App singleton: " + user.getUsername());

                    // Switch to the main menu screen on the main GDX thread
                    Gdx.app.postRunnable(() -> {
                        Main.getInstance().setScreen(new GDXMainMenu(networkService));
                    });
                } else {
                    System.err.println("NetworkService is null, cannot set authenticated user.");
                }
            } else {
                System.err.println("Username is null in authentication success message");
            }
        } catch (Exception e) {
            System.err.println("Error parsing authentication success: " + e.getMessage());
            e.printStackTrace();
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
    
    // Lobby handling methods
    private void handleLobbyJoin(Message message) {
        try {
            System.out.println("Handling lobby join success message: " + message.getBody());
            String lobbyId = message.getFromBody("lobbyId");
            if (lobbyId == null || lobbyId.isEmpty()) {
                System.err.println("Invalid lobbyId in lobby join message");
                return;
            }
            
            System.out.println("Joined lobby: " + lobbyId);
            
            // Notify the controller about successful lobby join
            if (client.getControllerCallback() instanceof NetworkLobbyController) {
                NetworkLobbyController controller = (NetworkLobbyController) client.getControllerCallback();
                controller.onLobbyJoinSuccess(lobbyId);
            } else {
                System.err.println("Controller callback is not NetworkLobbyController");
            }
        } catch (Exception e) {
            System.err.println("Error handling lobby join: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleLobbyLeave(Message message) {
        System.out.println("Left lobby");
        // You can add lobby state management here
    }
    
    private void handleLobbyInvite(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            String inviterUsername = message.getFromBody("inviterUsername");
            System.out.println("Invited to lobby by " + inviterUsername + " (ID: " + lobbyId + ")");
            // You can show an invite dialog here
        } catch (Exception e) {
            System.err.println("Error handling lobby invite: " + e.getMessage());
        }
    }
    
    private void handleLobbyInviteAccept(Message message) {
        System.out.println("Lobby invite accepted");
        // You can add lobby state management here
    }
    
    private void handleLobbyInviteDecline(Message message) {
        System.out.println("Lobby invite declined");
        // You can add lobby state management here
    }
    
    private void handleLobbyInviteNotification(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            String inviterUsername = message.getFromBody("inviterUsername");
            System.out.println("Received lobby invite notification from " + inviterUsername + " (ID: " + lobbyId + ")");
            
            // Notify the controller about the invitation
            if (client.getControllerCallback() instanceof com.example.main.controller.NetworkLobbyController) {
                com.example.main.controller.NetworkLobbyController controller = 
                    (com.example.main.controller.NetworkLobbyController) client.getControllerCallback();
                controller.notifyInvitationReceived(lobbyId, inviterUsername);
            }
        } catch (Exception e) {
            System.err.println("Error handling lobby invite notification: " + e.getMessage());
        }
    }
    
    private void handleLobbyInviteResponse(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            String inviterUsername = message.getFromBody("inviterUsername");
            boolean accepted = message.getFromBody("accepted");
            System.out.println("Received lobby invite response from " + inviterUsername + " (ID: " + lobbyId + ") - Accepted: " + accepted);
            // You can update the invite status in the UI
        } catch (Exception e) {
            System.err.println("Error handling lobby invite response: " + e.getMessage());
        }
    }
    
    private void handleLobbyAdminChange(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            String newAdminUsername = message.getFromBody("newAdminUsername");
            System.out.println("Lobby admin changed for lobby: " + lobbyId + " - New admin: " + newAdminUsername);
            // You can update the lobby admin in the UI
        } catch (Exception e) {
            System.err.println("Error handling lobby admin change: " + e.getMessage());
        }
    }
    
    private void handleLobbyPlayersUpdate(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            Map<String, Object> players = message.getFromBody("players");
            Map<String, Boolean> playerReady = message.getFromBody("playerReady");
            boolean canStart = message.getFromBody("canStart");
            
            System.out.println("Lobby update - Players: " + players.size() + ", Can start: " + canStart);
            // You can update the lobby UI here
        } catch (Exception e) {
            System.err.println("Error handling lobby players update: " + e.getMessage());
        }
    }
    
    private void handleLobbyReady(Message message) {
        try {
            boolean ready = message.getFromBody("ready");
            System.out.println("Ready status set to: " + ready);
            // You can update the ready status UI here
        } catch (Exception e) {
            System.err.println("Error handling lobby ready: " + e.getMessage());
        }
    }
    
    private void handleLobbyStartGame(Message message) {
        try {
            String lobbyId = message.getFromBody("lobbyId");
            System.out.println("Game starting for lobby: " + lobbyId);
            // You can transition to the game here
        } catch (Exception e) {
            System.err.println("Error handling lobby start game: " + e.getMessage());
        }
    }
    
        private void handleLobbyJoinSuccess(Message message) {
        try {
            System.out.println("Handling LOBBY_JOIN_SUCCESS message: " + message.getBody());
            String lobbyId = message.getFromBody("lobbyId");
            if (lobbyId != null) {
                // The user who creates the lobby is the admin.
                // We must use postRunnable to execute screen changes on the main GDX thread.
                Gdx.app.postRunnable(() -> {
                    Screen lobbyScreen = new GDXLobbyScreen(lobbyId, "Game Lobby", true);
                    Main.getInstance().setScreen(lobbyScreen);
                });
            } else {
                System.err.println("Lobby ID is null in LOBBY_JOIN_SUCCESS message");
            }
        } catch (Exception e) {
            System.err.println("Error handling LOBBY_JOIN_SUCCESS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleOnlineUsersUpdate(Message message) {
        try {
            String usersJson = message.getFromBody("users");
            System.out.println("Received online users update: " + usersJson);
            
            // Parse the JSON string to extract user information
            java.util.List<Object> onlineUsers = new java.util.ArrayList<>();
            
            // Simple JSON parsing - remove brackets and split by comma
            if (usersJson != null && !usersJson.isEmpty() && !usersJson.equals("[]")) {
                // Remove outer brackets
                String innerJson = usersJson.substring(1, usersJson.length() - 1);
                
                // Split by "},{" to get individual user objects
                String[] userStrings = innerJson.split("\\},\\{");
                
                for (int i = 0; i < userStrings.length; i++) {
                    String userString = userStrings[i];
                    // Fix brackets if needed
                    if (i == 0 && !userString.startsWith("{")) {
                        userString = "{" + userString;
                    }
                    if (i == userStrings.length - 1 && !userString.endsWith("}")) {
                        userString = userString + "}";
                    }
                    
                    // Extract username from the JSON string
                    // This is a simple approach - in a real implementation you might want to use a proper JSON parser
                    String username = "Unknown";
                    if (userString.contains("\"username\":\"")) {
                        int start = userString.indexOf("\"username\":\"") + 13; // length of ""username":""
                        int end = userString.indexOf("\"", start);
                        if (end > start) {
                            username = userString.substring(start, end);
                        }
                    }
                    
                    System.out.println("Online user: " + username);
                    onlineUsers.add(username);
                }
            }
            
            System.out.println("Online users count: " + onlineUsers.size());
            
            // Update controller if available
            Object callback = client.getControllerCallback();
            if (callback instanceof com.example.main.controller.NetworkLobbyController) {
                com.example.main.controller.NetworkLobbyController controller = 
                    (com.example.main.controller.NetworkLobbyController) callback;
                controller.updateOnlineUsersFromServer(onlineUsers);
            }
        } catch (Exception e) {
            System.err.println("Error handling online users update: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 