package com.example.main.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import com.example.main.enums.design.FarmThemes;
import com.example.main.enums.player.Gender;
import com.example.main.enums.regex.SecurityQuestion;
import com.example.main.models.User;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;
import com.google.gson.Gson;

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
    private final Gson gson;

    public ClientHandler(Socket socket, GameServer server) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        this.clientId = UUID.randomUUID().toString();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.gson = new Gson();
    }

    @Override
    public void run() {
        try {
            server.addClient(clientId, this);
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

    private void handleMessage(String messageJson) {
        if(!messageJson.contains("{\"type\":\"HEARTBEAT\",\"body\":{}}")){
            System.out.println("[SERVER LOG] Received message from client " + clientId + ": " + messageJson);
        }
        try {
            // Parse the message JSON and create Message object
            Message message = gson.fromJson(messageJson, Message.class);
            if (message == null) {
                System.err.println("Failed to parse message, skipping");
                return;
            }
            if (message.getType() == MessageType.ERROR) {
                // Skip processing error messages that were created as fallbacks
                System.err.println("Received error message, skipping processing");
                return;
            }
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
                case LOBBY_JOIN:
                    handleLobbyJoin(message);
                    break;
                case LOBBY_LEAVE:
                    handleLobbyLeave();
                    break;
                case CREATE_LOBBY:
                    handleCreateLobby(message);
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
                case REQUEST_AVAILABLE_LOBBIES:
                    handleRequestAvailableLobbies();
                    break;
                case LOBBY_FIND_BY_ID:
                    handleLobbyFindById(message);
                    break;
                case SUBMIT_FARM_CHOICE:
                    handleFarmChoice(message);
                    break;
                case PLAYER_MOVE:
                    handlePlayerMove(message);
                    break;
                case SEND_GAME_MAP:
                    server.handleSendGameMap(message, clientId);
                    break;
                default:
                    System.out.println("Unknown message type: " + message.getType());
            }
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            sendErrorMessage("Invalid message format");
        }
    }

    private void handleAuthentication(Message message) {
        try {
            // When the client sends an AUTHENTICATION message, its body is a Map.
            // We need to cast the body to a Map to safely get the username and password.
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String username = (String) body.get("username");
            String password = (String) body.get("password");

            if (server.authenticateUser(clientId, username, password)) {
                authenticated = true;
                User user = server.getAuthenticatedUser(clientId);
                if (user != null) {
                    user.setClientId(clientId);
                }
                try {
                    sendAuthSuccessMessage(user);
                    System.out.println("Authentication successful for client: " + clientId);

                    // Check if we can start the game
                    /* if (server.getAuthenticatedUsersCount() >= 1) {
                        server.startGame();
                    } */
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

    private void handlePlayerAction(Message message) {
        // No authentication required for player actions
        try {
            // Assuming getFromBody works like a map getter, if not, this needs the same Map cast as above
            Map<String, Object> body = (Map<String, Object>) message.getBody();
            String action = (String) body.get("action");
            Object actionData = body.get("actionData");
            server.handlePlayerAction(clientId, action, actionData);
        } catch (Exception e) {
            sendErrorMessage("Invalid action data");
        }
    }

    private void handleHeartbeat() {
        // Simply acknowledge the heartbeat
        sendHeartbeatMessage();
    }

    private void handleDisconnect() {
        System.out.println("Client " + clientId + " requested disconnect");
        disconnect();
    }

    public void sendMessage(Message message) {
        if (out != null) {
            try {
                String messageJson = gson.toJson(message);
                out.println(messageJson);
            } catch (Exception e) {
                System.err.println("Error sending message to client " + clientId + ": " + e.getMessage());
                e.printStackTrace();
                disconnect();
            }
        }
    }
    public void disconnect() {
        running = false;
        server.removeClient(clientId);
        cleanup();
    }
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
            return gson.fromJson(messageJson, Message.class);
        } catch (Exception e) {
            System.err.println("Error parsing message JSON: " + e.getMessage());
            System.err.println("Message JSON: " + messageJson);
            return null;
        }
    }

    private void sendErrorMessage(String error) {
        HashMap<String, Object> errorData = new HashMap<>();
        errorData.put("error", error);
        Message errorMessage = new Message(errorData, MessageType.ERROR);
        sendMessage(errorMessage);
    }

    private void sendAuthSuccessMessage(User user) {
        // Manually create a HashMap to avoid Gson serialization issues with the User object
        HashMap<String, Object> body = new HashMap<>();
        body.put("username", user.getUsername());
        body.put("nickname", user.getNickname());
        body.put("email", user.getEmail());

        Message authMessage = new Message(body, MessageType.AUTH_SUCCESS);
        sendMessage(authMessage);
    }


    private void sendAuthFailedMessage(String reason) {
        HashMap<String, Object> authData = new HashMap<>();
        authData.put("reason", reason);
        Message authMessage = new Message(authData, MessageType.AUTH_FAILED);
        sendMessage(authMessage);
    }
    private void sendHeartbeatMessage() {
        HashMap<String, Object> heartbeatData = new HashMap<>();
        Message heartbeatMessage = new Message(heartbeatData, MessageType.HEARTBEAT);
        sendMessage(heartbeatMessage);
    }

    private void handleLobbyJoin(Message message) {
        User user = server.getAuthenticatedUser(clientId);
        if (user == null) {
            sendLobbyJoinFailed("You must be logged in to join a lobby.");
            return;
        }
        Map<String, Object> body = message.getBody();
        String lobbyId = (String) body.get("lobbyId");
        String password = (String) body.get("password");

        if (server.joinLobby(clientId, lobbyId, password)) {
            Lobby lobby = server.getLobby(lobbyId);
            sendLobbyJoinSuccess(lobbyId, lobby.getName(), false);
            broadcastLobbyUpdate(lobbyId);
        } else {
            Lobby lobby = server.getLobby(lobbyId);
            if (lobby != null && lobby.isPrivate()) {
                sendLobbyJoinFailed("Incorrect password.");
            } else if (lobby != null && lobby.isFull()) {
                sendLobbyJoinFailed("Lobby is full.");
            } else {
                sendLobbyJoinFailed("Lobby does not exist.");
            }
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

    private void handleCreateLobby(Message message) {
        User host = server.getAuthenticatedUser(clientId);
        if (host == null) {
            sendErrorMessage("Authentication required to create a lobby.");
            return;
        }
        Map<String, Object> body = message.getBody();
        String lobbyName = (String) body.get("lobbyName");
        boolean isPrivate = (Boolean) body.get("isPrivate");
        String password = (String) body.get("password");
        boolean isVisible = (Boolean) body.get("isVisible");

        Lobby lobby = server.createLobby(lobbyName, host, isPrivate, password, isVisible);
        if (lobby != null) {
            sendLobbyJoinSuccess(lobby.getLobbyId(), lobby.getName(), true);
        } else {
            sendLobbyJoinFailed("Failed to create lobby on server.");
        }
    }

    private void handleLobbyStartGame() {
        String lobbyId = server.getClientLobbyId(clientId);
        if (lobbyId != null) {
            Lobby lobby = server.getLobby(lobbyId);
            if (lobby != null && lobby.getHostId().equals(clientId) && lobby.canStartGame()) {
                lobby.setGameStarted(true);

                // Create the message to tell clients to go to the pre-game screen.
                Message navigateMessage = new Message(new HashMap<>(), MessageType.NAVIGATE_TO_PREGAME);
                System.out.println("[SERVER LOG] Host " + clientId + " started lobby " + lobbyId + ". Sending NAVIGATE_TO_PREGAME to all players.");

                // Send this message to every player in the lobby.
                for (String playerId : lobby.getPlayerIds()) {
                    server.sendMessageToClient(playerId, navigateMessage);
                }
            } else {
                // This error message is now more accurate.
                sendErrorMessage("Cannot start game. You must be the host and have at least 2 players.");
            }
        }
    }


    private void sendLobbyLeaveSuccess() {
        HashMap<String, Object> data = new HashMap<>();
        Message message = new Message(data, MessageType.LOBBY_LEAVE);
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
        body.put("onlineUsers", onlineUsers);

        // Convert users to a JSON array string representation
        StringBuilder usersJson = new StringBuilder("[");
        boolean first = true;
        for (User user : onlineUsers) {
            if (!first) {
                usersJson.append(",");
            }
            usersJson.append("{");
            usersJson.append("\"username\":\"").append(user.getUsername()).append("\",");
            usersJson.append("\"nickname\":\"").append(user.getNickname()).append("\",");
            usersJson.append("\"email\":\"").append(user.getEmail()).append("\"");
            usersJson.append("}");
            first = false;
        }
        usersJson.append("]");

        body.put("users", usersJson.toString());
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

    private void handleRequestAvailableLobbies() {
        List<Lobby> allLobbies = server.getAllLobbies();
        List<Map<String, Object>> visibleLobbies = new ArrayList<>();
        for (Lobby lobby : allLobbies) {
            if (lobby.isVisible()) {
                Map<String, Object> lobbyInfo = new HashMap<>();
                lobbyInfo.put("lobbyId", lobby.getLobbyId());
                lobbyInfo.put("name", lobby.getName());
                lobbyInfo.put("isPrivate", lobby.isPrivate());

                // **THE FIX**: Add the missing data that the client needs.
                lobbyInfo.put("playerCount", lobby.getPlayerCount());
                lobbyInfo.put("maxPlayers", lobby.getMaxPlayers());
                lobbyInfo.put("host", lobby.getHostUsername());

                visibleLobbies.add(lobbyInfo);
            }
        }
        // This message now contains all the required information.
        sendMessage(new Message(new HashMap<>() {{ put("lobbies", visibleLobbies); }}, MessageType.AVAILABLE_LOBBIES_UPDATE));
    }

    private void sendLobbyJoinSuccess(String lobbyId, String lobbyName, boolean isAdmin) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("lobbyId", lobbyId);
        data.put("lobbyName", lobbyName);
        data.put("isAdmin", isAdmin);
        sendMessage(new Message(data, MessageType.LOBBY_JOIN_SUCCESS));
    }

    private void sendLobbyJoinFailed(String reason) {
        sendMessage(new Message(new HashMap<>() {{ put("reason", reason); }}, MessageType.LOBBY_JOIN_FAILED));
    }

    private void handleLobbyFindById(Message message) {
        String lobbyId = message.getFromBody("lobbyId");
        Lobby lobby = server.getLobby(lobbyId);

        HashMap<String, Object> body = new HashMap<>();
        if (lobby != null) {
            // Lobby found, send back its details
            body.put("found", true);
            body.put("lobbyId", lobby.getLobbyId());
            body.put("name", lobby.getName());
            body.put("isPrivate", lobby.isPrivate());
        } else {
            // Lobby not found
            body.put("found", false);
        }
        sendMessage(new Message(body, MessageType.LOBBY_FIND_RESULT));
    }

    private void handleFarmChoice(Message message) {
        String lobbyId = server.getClientLobbyId(clientId);
        if (lobbyId != null) {
            try {
                // Extract the farm theme from the message body
                String themeName = message.getFromBody("farmTheme");
                FarmThemes theme = FarmThemes.valueOf(themeName);
                // Pass the choice to the main server logic
                server.handleFarmChoice(clientId, lobbyId, theme);
            } catch (Exception e) {
                System.err.println("[SERVER ERROR] Could not process farm choice from client " + clientId + ": " + e.getMessage());
            }
        }
    }

    private void handlePlayerMove(Message message) {
        String lobbyId = server.getClientLobbyId(clientId);
        if (lobbyId != null) {
            try {
                // Extract coordinates from the message. Gson deserializes numbers as Double.
                int newX = ((Double) message.getFromBody("x")).intValue();
                int newY = ((Double) message.getFromBody("y")).intValue();

                // Pass the data to the main server for processing.
                server.handlePlayerMove(lobbyId, clientId, newX, newY);
            } catch (Exception e) {
                System.err.println("[SERVER ERROR] Could not process PLAYER_MOVE message from client " + clientId + ": " + e.getMessage());
            }
        }
    }

}
