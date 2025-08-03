package com.example.main.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
<<<<<<< HEAD
import java.util.UUID;

import com.example.main.models.User;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;
=======
import java.util.List;
import java.util.Map; // Import Map
import java.util.UUID;

import com.example.main.enums.player.Gender;
import com.example.main.enums.regex.SecurityQuestion;
import com.example.main.models.User;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;
import com.badlogic.gdx.utils.Json;
>>>>>>> main

/**
 * Handles individual client connections on the server
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final GameServer server;
    private final String clientId;
    private final PrintWriter out;
    private final BufferedReader in;
    private boolean authenticated = false;
    private boolean running = true;
<<<<<<< HEAD
    
=======
    private final Json json;

>>>>>>> main
    public ClientHandler(Socket socket, GameServer server) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        this.clientId = UUID.randomUUID().toString();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
<<<<<<< HEAD
    }
    
=======
        this.json = new Json();
    }

>>>>>>> main
    @Override
    public void run() {
        try {
            server.addClient(clientId, this);
<<<<<<< HEAD
            
=======

>>>>>>> main
            String inputLine;
            while (running && (inputLine = in.readLine()) != null) {
                handleMessage(inputLine);
            }
        } catch (IOException e) {
            System.err.println("Error handling client " + clientId + ": " + e.getMessage());
        } finally {
            cleanup();
        }
    }
<<<<<<< HEAD
    
=======

>>>>>>> main
    private void handleMessage(String messageJson) {
        try {
            // Parse the message JSON and create Message object
            Message message = parseMessageFromJson(messageJson);
<<<<<<< HEAD
            
=======

>>>>>>> main
            if (message == null) {
                System.err.println("Failed to parse message, skipping");
                return;
            }
<<<<<<< HEAD
            
=======

>>>>>>> main
            if (message.getType() == MessageType.ERROR) {
                // Skip processing error messages that were created as fallbacks
                System.err.println("Received error message, skipping processing");
                return;
            }
<<<<<<< HEAD
            
=======

>>>>>>> main
            switch (message.getType()) {
                case AUTHENTICATION:
                    handleAuthentication(message);
                    break;
                case PLAYER_ACTION:
                    handlePlayerAction(message);
                    break;
                case HEARTBEAT:
                    handleHeartbeat();
                    break;
                case DISCONNECT:
                    handleDisconnect();
                    break;
<<<<<<< HEAD
=======
                case LOBBY_JOIN:
                    handleLobbyJoin(message);
                    break;
                case LOBBY_LEAVE:
                    handleLobbyLeave();
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
                case LOBBY_READY:
                    handleLobbyReady(message);
                    break;
                case LOBBY_START_GAME:
                    handleLobbyStartGame();
                    break;
                case REQUEST_ONLINE_USERS:
                    handleRequestOnlineUsers();
                    break;
                case REGISTER:
                    handleRegister(message);
                    break;
>>>>>>> main
                default:
                    System.out.println("Unknown message type: " + message.getType());
            }
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            sendErrorMessage("Invalid message format");
        }
    }
<<<<<<< HEAD
    
    private void handleAuthentication(Message message) {
        try {
            String username = message.getFromBody("username");
            String password = message.getFromBody("password");
            
            if (server.authenticateUser(clientId, username, password)) {
                authenticated = true;
                try {
                    sendAuthSuccessMessage(server.getAuthenticatedUser(clientId));
                    System.out.println("Authentication successful for client: " + clientId);
                    
=======

    private void handleAuthentication(Message message) {
        try {
            // *** THIS IS THE CORRECTED PART ***
            // When the client sends an AUTHENTICATION message, its body is a Map.
            // We need to cast the body to a Map to safely get the username and password.
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String username = (String) body.get("username");
            String password = (String) body.get("password");

            if (server.authenticateUser(clientId, username, password)) {
                authenticated = true;
                try {
                    // This part was already correct. It sends a success message with the user's data.
                    sendAuthSuccessMessage(server.getAuthenticatedUser(clientId));
                    System.out.println("Authentication successful for client: " + clientId);

>>>>>>> main
                    // Check if we can start the game
                    if (server.getAuthenticatedUsersCount() >= 1) {
                        server.startGame();
                    }
                } catch (Exception e) {
                    System.err.println("Error sending success message: " + e.getMessage());
                    // Don't send failure message if success message failed
                }
            } else {
                sendAuthFailedMessage("Invalid credentials");
                System.out.println("Authentication failed for client: " + clientId);
            }
        } catch (Exception e) {
            System.err.println("Error in authentication: " + e.getMessage());
            sendAuthFailedMessage("Authentication error");
        }
    }
<<<<<<< HEAD
    
    private void handlePlayerAction(Message message) {
        if (!authenticated) {
            sendErrorMessage("Not authenticated");
            return;
        }
        
        try {
            String action = message.getFromBody("action");
            Object actionData = message.getFromBody("actionData");
=======

    private void handlePlayerAction(Message message) {
        // No authentication required for player actions
        try {
            // Assuming getFromBody works like a map getter, if not, this needs the same Map cast as above
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String action = (String) body.get("action");
            Object actionData = body.get("actionData");
>>>>>>> main
            server.handlePlayerAction(clientId, action, actionData);
        } catch (Exception e) {
            sendErrorMessage("Invalid action data");
        }
    }
<<<<<<< HEAD
    
=======

>>>>>>> main
    private void handleHeartbeat() {
        // Simply acknowledge the heartbeat
        sendHeartbeatMessage();
    }
<<<<<<< HEAD
    
=======

>>>>>>> main
    private void handleDisconnect() {
        System.out.println("Client " + clientId + " requested disconnect");
        disconnect();
    }
<<<<<<< HEAD
    
    public void sendMessage(Message message) {
        if (running && out != null) {
            try {
                // Use proper JSON serialization
                com.badlogic.gdx.utils.Json json = new com.badlogic.gdx.utils.Json();
                String messageJson = json.toJson(message);
                out.println(messageJson);
            } catch (Exception e) {
                System.err.println("Error sending message to client " + clientId + ": " + e.getMessage());
=======

    public void sendMessage(Message message) {
        if (running && out != null) {
            try {
                // Manually create a simple JSON string to avoid serialization issues
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{");
                
                // Add type
                jsonBuilder.append("\"type\":\"").append(message.getType().toString()).append("\"");
                
                // Add body
                jsonBuilder.append(",\"body\":{");
                HashMap<String, Object> body = message.getBody();
                boolean first = true;
                for (java.util.Map.Entry<String, Object> entry : body.entrySet()) {
                    if (!first) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\"").append(entry.getKey()).append("\":\"");
                    
                    // Escape quotes in the value
                    String value = entry.getValue() != null ? entry.getValue().toString() : "";
                    jsonBuilder.append(value.replace("\"", "\\\""));
                    jsonBuilder.append("\"");
                    
                    first = false;
                }
                jsonBuilder.append("}");
                
                jsonBuilder.append("}");
                
                String messageJson = jsonBuilder.toString();
                out.println(messageJson);
            } catch (Exception e) {
                System.err.println("Error sending message to client " + clientId + ": " + e.getMessage());
                e.printStackTrace();
>>>>>>> main
                disconnect();
            }
        }
    }
<<<<<<< HEAD
    
=======

>>>>>>> main
    public void disconnect() {
        running = false;
        server.removeClient(clientId);
        cleanup();
    }
<<<<<<< HEAD
    
=======

>>>>>>> main
    private void cleanup() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error cleaning up client " + clientId + ": " + e.getMessage());
        }
    }
<<<<<<< HEAD
    
    public String getClientId() {
        return clientId;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    // Helper methods for creating messages
    private Message parseMessageFromJson(String messageJson) {
        try {
            com.badlogic.gdx.utils.Json json = new com.badlogic.gdx.utils.Json();
            Message message = json.fromJson(Message.class, messageJson);
            System.out.println("Successfully parsed message: " + message.getType());
            return message;
        } catch (Exception e) {
            System.err.println("Error parsing message JSON: " + e.getMessage());
            System.err.println("Message JSON: " + messageJson);
            // Return null instead of a dummy message to avoid confusion
            return null;
        }
    }
    
=======

    public String getClientId() {
        return clientId;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean isRunning() {
        return running;
    }

    // Helper methods for creating messages
    private Message parseMessageFromJson(String messageJson) {
        try {
            return json.fromJson(Message.class, messageJson);
        } catch (Exception e) {
            System.err.println("Error parsing message JSON: " + e.getMessage());
            System.err.println("Message JSON: " + messageJson);
            return null;
        }
    }

>>>>>>> main
    private void sendErrorMessage(String error) {
        HashMap<String, Object> errorData = new HashMap<>();
        errorData.put("error", error);
        Message errorMessage = new Message(errorData, MessageType.ERROR);
        sendMessage(errorMessage);
    }
<<<<<<< HEAD
    
    private void sendAuthSuccessMessage(User user) {
        HashMap<String, Object> authData = new HashMap<>();
        authData.put("user", user);
        Message authMessage = new Message(authData, MessageType.AUTH_SUCCESS);
        sendMessage(authMessage);
    }
    
=======

    private void sendAuthSuccessMessage(User user) {
        // Create a simple message structure to avoid serialization issues
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", user.getUsername() != null ? user.getUsername() : "");
        body.put("nickname", user.getNickname() != null ? user.getNickname() : "");
        body.put("email", user.getEmail() != null ? user.getEmail() : "");
        // Add other relevant user fields as needed
        Message authMessage = new Message(body, MessageType.AUTH_SUCCESS);
        sendMessage(authMessage);
    }


>>>>>>> main
    private void sendAuthFailedMessage(String reason) {
        HashMap<String, Object> authData = new HashMap<>();
        authData.put("reason", reason);
        Message authMessage = new Message(authData, MessageType.AUTH_FAILED);
        sendMessage(authMessage);
    }
<<<<<<< HEAD
    
=======

>>>>>>> main
    private void sendHeartbeatMessage() {
        HashMap<String, Object> heartbeatData = new HashMap<>();
        Message heartbeatMessage = new Message(heartbeatData, MessageType.HEARTBEAT);
        sendMessage(heartbeatMessage);
    }
<<<<<<< HEAD
} 
=======

    // Lobby handling methods
    private void handleLobbyJoin(Message message) {
        // No authentication required for lobby operations
        try {
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String lobbyId = (String) body.get("lobbyId");
            if (lobbyId == null) {
                // Create new lobby
                String newLobbyId = server.createLobby(clientId);
                sendLobbyJoinSuccess(newLobbyId);
            } else {
                // Join existing lobby
                if (server.joinLobby(clientId, lobbyId)) {
                    sendLobbyJoinSuccess(lobbyId);
                    broadcastLobbyUpdate(lobbyId);
                } else {
                    sendLobbyJoinFailed("Failed to join lobby");
                }
            }
        } catch (Exception e) {
            sendLobbyJoinFailed("Error joining lobby");
        }
    }

    private void handleLobbyLeave() {
        // No authentication required for lobby operations
        String lobbyId = server.getClientLobbyId(clientId);
        if (lobbyId != null) {
            server.leaveLobby(clientId);
            sendLobbyLeaveSuccess();
            broadcastLobbyUpdate(lobbyId);
        }
    }

    private void handleLobbyInvite(Message message) {
        // No authentication required for lobby operations
        try {
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String targetUsername = (String) body.get("targetUsername");
            String lobbyId = server.getClientLobbyId(clientId);

            if (lobbyId != null && targetUsername != null) {
                Lobby lobby = server.getLobby(lobbyId);
                if (lobby != null && lobby.getHostId().equals(clientId)) {
                    // Get client ID by username
                    String targetClientId = server.getClientIdByUsername(targetUsername);
                    if (targetClientId != null) {
                        // Send invite to target client
                        sendLobbyInvite(targetClientId, lobbyId);
                        System.out.println("Invitation sent from " + clientId + " to " + targetUsername + " (client: " + targetClientId + ")");
                    } else {
                        sendErrorMessage("User " + targetUsername + " is not online");
                    }
                } else {
                    sendErrorMessage("You are not the host of this lobby");
                }
            } else {
                sendErrorMessage("Invalid lobby or target user");
            }
        } catch (Exception e) {
            System.err.println("Error handling lobby invite: " + e.getMessage());
            sendErrorMessage("Error sending invite");
        }
    }

    private void handleLobbyInviteAccept(Message message) {
        // No authentication required for lobby operations
        try {
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String lobbyId = (String) body.get("lobbyId");
            if (server.joinLobby(clientId, lobbyId)) {
                sendLobbyJoinSuccess(lobbyId);
                broadcastLobbyUpdate(lobbyId);
            } else {
                sendLobbyJoinFailed("Failed to join lobby");
            }
        } catch (Exception e) {
            sendLobbyJoinFailed("Error accepting invite");
        }
    }

    private void handleLobbyInviteDecline(Message message) {
        // Just acknowledge the decline
        sendLobbyInviteDeclineAck();
    }

    private void handleLobbyReady(Message message) {
        // No authentication required for lobby operations
        try {
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            boolean ready = (Boolean) body.get("ready");
            if (server.setPlayerReady(clientId, ready)) {
                sendLobbyReadySuccess(ready);
                String lobbyId = server.getClientLobbyId(clientId);
                if (lobbyId != null) {
                    broadcastLobbyUpdate(lobbyId);
                }
            }
        } catch (Exception e) {
            sendErrorMessage("Error setting ready status");
        }
    }

    private void handleLobbyStartGame() {
        // No authentication required for lobby operations
        String lobbyId = server.getClientLobbyId(clientId);
        if (lobbyId != null) {
            Lobby lobby = server.getLobby(lobbyId);
            if (lobby != null && lobby.getHostId().equals(clientId) && lobby.canStartGame()) {
                lobby.setGameStarted(true);
                broadcastLobbyStartGame(lobbyId);
            } else {
                sendErrorMessage("Cannot start game");
            }
        }
    }

    // Lobby message sending methods
    private void sendLobbyJoinSuccess(String lobbyId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("lobbyId", lobbyId);
        Message message = new Message(data, MessageType.LOBBY_JOIN);
        sendMessage(message);
    }

    private void sendLobbyJoinFailed(String reason) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("reason", reason);
        Message message = new Message(data, MessageType.ERROR);
        sendMessage(message);
    }

    private void sendLobbyLeaveSuccess() {
        HashMap<String, Object> data = new HashMap<>();
        Message message = new Message(data, MessageType.LOBBY_LEAVE);
        sendMessage(message);
    }

    private void sendLobbyInvite(String targetClientId, String lobbyId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("lobbyId", lobbyId);
        data.put("inviterUsername", server.getAuthenticatedUser(clientId).getUsername());
        Message message = new Message(data, MessageType.LOBBY_INVITE);
        server.sendMessageToClient(targetClientId, message);
    }

    private void sendLobbyInviteDeclineAck() {
        HashMap<String, Object> data = new HashMap<>();
        Message message = new Message(data, MessageType.LOBBY_INVITE_DECLINE);
        sendMessage(message);
    }

    private void sendLobbyReadySuccess(boolean ready) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ready", ready);
        Message message = new Message(data, MessageType.LOBBY_READY);
        sendMessage(message);
    }

    private void broadcastLobbyUpdate(String lobbyId) {
        Lobby lobby = server.getLobby(lobbyId);
        if (lobby != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("lobbyId", lobbyId);
            data.put("players", lobby.getPlayers());
            data.put("playerReady", lobby.getPlayerReadyStatus());
            data.put("canStart", lobby.canStartGame());
            Message message = new Message(data, MessageType.LOBBY_PLAYERS_UPDATE);

            for (String playerId : lobby.getPlayerIds()) {
                server.sendMessageToClient(playerId, message);
            }
        }
    }

    private void broadcastLobbyStartGame(String lobbyId) {
        Lobby lobby = server.getLobby(lobbyId);
        if (lobby != null) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("lobbyId", lobbyId);
            Message message = new Message(data, MessageType.LOBBY_START_GAME);

            for (String playerId : lobby.getPlayerIds()) {
                server.sendMessageToClient(playerId, message);
            }
        }
    }

    private void handleRequestOnlineUsers() {
        List<User> onlineUsers = server.getOnlinePlayers();
        HashMap<String, Object> body = new HashMap<>();
        body.put("users", onlineUsers);
        Message message = new Message(body, MessageType.ONLINE_USERS_UPDATE);
        sendMessage(message);
    }

    private void handleRegister(Message message) {
        try {
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String username = (String) body.get("username");
            String password = (String) body.get("password");
            String nickname = (String) body.get("nickname");
            String email = (String) body.get("email");
            String genderStr = (String) body.get("gender");

            // Validate input
            if (username == null || password == null || nickname == null || email == null || genderStr == null) {
                sendErrorMessage("Missing required fields for registration");
                return;
            }

            // Check if user already exists
            if (server.getAuthManager().userExists(username)) {
                sendErrorMessage("Username already exists");
                return;
            }

            // Create new user
            Gender gender = Gender.valueOf(genderStr);
            User newUser = new User(username, password, nickname, email, gender);
            // Initialize security fields to avoid serialization issues
            newUser.setSecurityQuestion(SecurityQuestion.PetName);
            newUser.setSecurityAnswer("default");

            // Register user
            boolean success = server.getAuthManager().registerUser(newUser);

            if (success) {
                // Send success message
                HashMap<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "User registered successfully");
                Message response = new Message(responseBody, MessageType.AUTH_SUCCESS);
                sendMessage(response);
                System.out.println("User registered successfully: " + username);
            } else {
                sendErrorMessage("Failed to register user");
            }
        } catch (Exception e) {
            System.err.println("Error handling registration: " + e.getMessage());
            sendErrorMessage("Registration failed: " + e.getMessage());
        }
    }
}
>>>>>>> main
