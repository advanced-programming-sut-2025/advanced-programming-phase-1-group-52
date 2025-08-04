package com.example.main.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.main.auth.AuthManager;
import com.example.main.auth.AuthResult;
import com.example.main.models.Game;
import com.example.main.models.User;
import com.example.main.network.NetworkConstants;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

/**
 * Main game server that handles multiple client connections
 */
public class GameServer {
    private final int port;
    private final ServerSocket serverSocket;
    private final AtomicBoolean running;
    private final ExecutorService executorService;
    private final ConcurrentHashMap<String, ClientHandler> connectedClients;
    private final ConcurrentHashMap<String, User> authenticatedUsers;
    private final ConcurrentHashMap<String, Lobby> lobbies;
    private final ConcurrentHashMap<String, String> clientToLobby; // clientId -> lobbyId
    private final ConcurrentHashMap<String, String> usernameToClientId; // username -> clientId
    private Game game;
    private final List<User> availableUsers;
    
    public GameServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.running = new AtomicBoolean(false);
        this.executorService = Executors.newCachedThreadPool();
        this.connectedClients = new ConcurrentHashMap<>();
        this.authenticatedUsers = new ConcurrentHashMap<>();
        this.lobbies = new ConcurrentHashMap<>();
        this.clientToLobby = new ConcurrentHashMap<>();
        this.usernameToClientId = new ConcurrentHashMap<>();
        this.availableUsers = new ArrayList<>();
        
        System.out.println("Game Server started on port " + port);
    }
    
    public void start() {
        running.set(true);
        System.out.println("Server is running and accepting connections...");
        
        // Start heartbeat thread
        startHeartbeatThread();
        
        while (running.get()) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                executorService.execute(clientHandler);
                
            } catch (IOException e) {
                if (running.get()) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }
    
    public void stop() {
        running.set(false);
        
        // Disconnect all clients
        for (ClientHandler handler : connectedClients.values()) {
            handler.disconnect();
        }
        
        // Shutdown executor service
        executorService.shutdown();
        
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
        
        System.out.println("Server stopped");
    }
    
    public void addClient(String clientId, ClientHandler handler) {
        System.out.println("Adding client: " + clientId + " (Total before: " + connectedClients.size() + ")");
        connectedClients.put(clientId, handler);
        System.out.println("Client added: " + clientId + " (Total after: " + connectedClients.size() + ")");
        
        // Print current authenticated users for debugging
        System.out.println("Current authenticated users: " + authenticatedUsers.size());
        for (Map.Entry<String, User> entry : authenticatedUsers.entrySet()) {
            System.out.println("  Authenticated user: " + entry.getValue().getUsername() + " (client ID: " + entry.getKey() + ")");
        }
    }
    
    public void removeClient(String clientId) {
        ClientHandler handler = connectedClients.remove(clientId);
        if (handler != null) {
            handler.disconnect();
        }
        
        // Remove authenticated user and logout from AuthManager
        User user = authenticatedUsers.remove(clientId);
        if (user != null) {
            usernameToClientId.remove(user.getUsername());
            AuthManager authManager = AuthManager.getInstance();
            authManager.logoutByUsername(user.getUsername());
            System.out.println("User logged out: " + user.getUsername());
            // Broadcast online users update
            broadcastOnlineUsersUpdate();
        }
        
        System.out.println("Client removed: " + clientId + " (Total: " + connectedClients.size() + ")");
    }
    
    public boolean authenticateUser(String clientId, String username, String password) {
        System.out.println("TEST Authentication attempt for client " + clientId + ": username=" + username + ", password=" + password);
        
        // Use the new AuthManager for authentication
        AuthManager authManager = AuthManager.getInstance();
        AuthResult result = authManager.authenticate(username, password);
        
        if (result.isSuccess()) {
            User user = authManager.getUser(username);
            authenticatedUsers.put(clientId, user);
            usernameToClientId.put(username, clientId);
            authManager.setUserOnline(username, clientId);
            System.out.println("User authenticated: " + username + " for client: " + clientId);
            return true;
        } else {
            System.out.println("Authentication failed: " + result.getMessage());
            return false;
        }
    }
    
    private boolean isUserAlreadyLoggedIn(String username) {
        for (User user : authenticatedUsers.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    // Lobby management methods
    public String createLobby(String hostClientId) {
        System.out.println("Creating new lobby for client " + hostClientId);
        System.out.println("Current authenticated users: " + authenticatedUsers.size());
        for (Map.Entry<String, User> entry : authenticatedUsers.entrySet()) {
            System.out.println("  Authenticated user: " + entry.getValue().getUsername() + " (client ID: " + entry.getKey() + ")");
        }
        
        String lobbyId = UUID.randomUUID().toString();
        // This is a placeholder for a simple lobby, we need to decide what to do here.
        // For now, let's create a default public lobby.
        Lobby lobby = new Lobby(lobbyId, "Default Lobby", hostClientId, false, "", true);
        lobbies.put(lobbyId, lobby);
        
        // Add host to lobby
        User hostUser = authenticatedUsers.get(hostClientId);
        System.out.println("Host user for client " + hostClientId + ": " + hostUser);
        if (hostUser != null) {
            lobby.addPlayer(hostClientId, hostUser);
            System.out.println("Added host user to lobby: " + hostUser.getUsername());
        } else {
            System.out.println("Host user is null for client " + hostClientId);
            System.out.println("Available client IDs in authenticatedUsers: " + authenticatedUsers.keySet());
        }
        
        System.out.println("Creating new lobby " + lobbyId + " for client " + hostClientId);
        System.out.println("Host user: " + hostUser + " (username: " + (hostUser != null ? hostUser.getUsername() : "null") + ")");
        
        return lobbyId;
    }
    
    public boolean joinLobby(String clientId, String lobbyId) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null || lobby.isFull()) {
            return false;
        }
        
        User user = authenticatedUsers.get(clientId);
        if (user == null) {
            return false;
        }
        
        if (lobby.addPlayer(clientId, user)) {
            clientToLobby.put(clientId, lobbyId);
            System.out.println("Player " + user.getUsername() + " joined lobby " + lobbyId);
            return true;
        }
        return false;
    }
    
    public boolean leaveLobby(String clientId) {
        String lobbyId = clientToLobby.remove(clientId);
        if (lobbyId == null) {
            return false;
        }
        
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby != null) {
            lobby.removePlayer(clientId);
            System.out.println("Player left lobby " + lobbyId);
            
            // If lobby is empty, remove it
            if (lobby.getPlayerCount() == 0) {
                lobbies.remove(lobbyId);
                System.out.println("Lobby " + lobbyId + " removed (empty)");
            }
            return true;
        }
        return false;
    }
    
    public Lobby createLobby(String lobbyName, User host, boolean isPrivate, String password, boolean isVisible) {
        String lobbyId = java.util.UUID.randomUUID().toString();
        Lobby lobby = new Lobby(lobbyId, lobbyName, host.getUsername(), isPrivate, password, isVisible);
        lobby.setPrivate(isPrivate);
        if (isPrivate) {
            lobby.setPassword(password);
        }
        lobby.setVisible(isVisible);

        lobbies.put(lobbyId, lobby);
        // The host automatically joins the lobby they create
        joinLobby(host.getClientId(), lobbyId);

        System.out.println("Lobby created: " + lobbyName + " (ID: " + lobbyId + ") by " + host.getUsername());
        return lobby;
    }

    public Lobby getLobby(String lobbyId) {
        return lobbies.get(lobbyId);
    }
    
    public Lobby getClientLobby(String clientId) {
        String lobbyId = clientToLobby.get(clientId);
        return lobbyId != null ? lobbies.get(lobbyId) : null;
    }
    
    public List<Lobby> getAllLobbies() {
        return new ArrayList<>(lobbies.values());
    }
    
    public List<User> getOnlinePlayers() {
        // Use AuthManager to get online users
        AuthManager authManager = AuthManager.getInstance();
        List<String> onlineUsernames = authManager.getOnlineUsers();
        List<User> onlineUsers = new ArrayList<>();
        
        for (String username : onlineUsernames) {
            User user = authManager.getUser(username);
            if (user != null) {
                onlineUsers.add(user);
            }
        }
        
        return onlineUsers;
    }
    
    public boolean setPlayerReady(String clientId, boolean ready) {
        Lobby lobby = getClientLobby(clientId);
        if (lobby != null) {
            return lobby.setPlayerReady(clientId, ready);
        }
        return false;
    }
    
    public boolean canStartGame(String clientId) {
        Lobby lobby = getClientLobby(clientId);
        return lobby != null && lobby.canStartGame();
    }
    
    public void broadcastOnlineUsersUpdate() {
        List<User> onlineUsers = new ArrayList<>(authenticatedUsers.values());

        if (onlineUsers.size() < 1) return; // Don't broadcast an empty list or a list with one user

        // Create a message with the list of online users
        java.util.HashMap<String, Object> messageData = new java.util.HashMap<>();
        messageData.put("users", onlineUsers);
        Message message = new Message(messageData, MessageType.ONLINE_USERS_UPDATE);
        broadcastMessage(message);
    }
    
    public String getClientLobbyId(String clientId) {
        return clientToLobby.get(clientId);
    }
    

    
    public User getAuthenticatedUser(String clientId) {
        return authenticatedUsers.get(clientId);
    }
    
    public String getClientIdByUsername(String username) {
        for (java.util.Map.Entry<String, User> entry : authenticatedUsers.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public void broadcastMessage(Message message) {
        for (ClientHandler handler : connectedClients.values()) {
            handler.sendMessage(message);
        }
    }
    
    public void broadcastMessageToOthers(Message message, String excludeClientId) {
        for (java.util.Map.Entry<String, ClientHandler> entry : connectedClients.entrySet()) {
            if (!entry.getKey().equals(excludeClientId)) {
                entry.getValue().sendMessage(message);
            }
        }
    }
    
    public void sendMessageToClient(String clientId, Message message) {
        ClientHandler handler = connectedClients.get(clientId);
        if (handler != null) {
            handler.sendMessage(message);
        }
    }
    
    public void handlePlayerAction(String clientId, String action, Object actionData) {
        // Handle player actions and update game state
        User user = getAuthenticatedUser(clientId);
        if (user != null && game != null) {
            // Process the action and update game state
            // This would integrate with your existing game logic
            
            // Broadcast the action to other players
            java.util.HashMap<String, Object> messageData = new java.util.HashMap<>();
            messageData.put("action", action);
            messageData.put("actionData", actionData);
            messageData.put("senderId", clientId);
            Message actionMessage = new Message(messageData, MessageType.PLAYER_ACTION);
            broadcastMessageToOthers(actionMessage, clientId);
        }
    }
    
    public void startGame() {
        if (authenticatedUsers.size() >= 1) {
            ArrayList<User> players = new ArrayList<>(authenticatedUsers.values());
            this.game = new Game(players);
            
            // Broadcast game start to all clients
            java.util.HashMap<String, Object> gameData = new java.util.HashMap<>();
            gameData.put("game", game);
            Message gameStateMessage = new Message(gameData, MessageType.GAME_STATE);
            broadcastMessage(gameStateMessage);
            
            System.out.println("Game started with " + players.size() + " players");
        }
    }
    
    public Game getGame() {
        return game;
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public int getConnectedClientsCount() {
        return connectedClients.size();
    }
    
    public int getAuthenticatedUsersCount() {
        return authenticatedUsers.size();
    }
    
    public boolean isFull() {
        return connectedClients.size() >= NetworkConstants.MAX_CLIENTS;
    }
    
    private void startHeartbeatThread() {
        Thread heartbeatThread = new Thread(() -> {
            while (running.get()) {
                try {
                    Thread.sleep(NetworkConstants.HEARTBEAT_INTERVAL);
                    java.util.HashMap<String, Object> heartbeatData = new java.util.HashMap<>();
                    Message heartbeat = new Message(heartbeatData, MessageType.HEARTBEAT);
                    broadcastMessage(heartbeat);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        heartbeatThread.setDaemon(true);
        heartbeatThread.start();
    }
    
    private User findUserByUsername(String username) {
        // In a real application, this would query a database
        // For now, we'll use the available users list
        for (User user : availableUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    public void addAvailableUser(User user) {
        availableUsers.add(user);
    }
    
    public List<User> getAvailableUsers() {
        return availableUsers;
    }
    
    public AuthManager getAuthManager() {
        return AuthManager.getInstance();
    }
    
    public static void main(String[] args) {
        try {
            GameServer server = new GameServer(NetworkConstants.DEFAULT_PORT);
            
            // Add some test users
            server.addAvailableUser(new User("player1", "password1", "Player 1", "player1@test.com", null));
            server.addAvailableUser(new User("player2", "password2", "Player 2", "player2@test.com", null));
            server.addAvailableUser(new User("player3", "password3", "Player 3", "player3@test.com", null));
            server.addAvailableUser(new User("player4", "password4", "Player 4", "player4@test.com", null));
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
} 