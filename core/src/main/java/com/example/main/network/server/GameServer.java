package com.example.main.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.UUID;
>>>>>>> main
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

<<<<<<< HEAD
=======
import com.example.main.auth.AuthManager;
import com.example.main.auth.AuthResult;
>>>>>>> main
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
<<<<<<< HEAD
=======
    private final ConcurrentHashMap<String, Lobby> lobbies;
    private final ConcurrentHashMap<String, String> clientToLobby; // clientId -> lobbyId
    private final ConcurrentHashMap<String, String> usernameToClientId; // username -> clientId
>>>>>>> main
    private Game game;
    private final List<User> availableUsers;
    
    public GameServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.running = new AtomicBoolean(false);
        this.executorService = Executors.newCachedThreadPool();
        this.connectedClients = new ConcurrentHashMap<>();
        this.authenticatedUsers = new ConcurrentHashMap<>();
<<<<<<< HEAD
=======
        this.lobbies = new ConcurrentHashMap<>();
        this.clientToLobby = new ConcurrentHashMap<>();
        this.usernameToClientId = new ConcurrentHashMap<>();
>>>>>>> main
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
        connectedClients.put(clientId, handler);
        System.out.println("Client added: " + clientId + " (Total: " + connectedClients.size() + ")");
    }
    
    public void removeClient(String clientId) {
        ClientHandler handler = connectedClients.remove(clientId);
        if (handler != null) {
            handler.disconnect();
        }
        
<<<<<<< HEAD
        // Remove authenticated user
        authenticatedUsers.remove(clientId);
=======
        // Remove authenticated user and logout from AuthManager
        User user = authenticatedUsers.remove(clientId);
        if (user != null) {
            usernameToClientId.remove(user.getUsername());
            AuthManager authManager = AuthManager.getInstance();
            authManager.logoutByUsername(user.getUsername());
            System.out.println("User logged out: " + user.getUsername());
        }
>>>>>>> main
        
        System.out.println("Client removed: " + clientId + " (Total: " + connectedClients.size() + ")");
    }
    
    public boolean authenticateUser(String clientId, String username, String password) {
<<<<<<< HEAD
        // Simple authentication - in a real application, you'd check against a database
        System.out.println("Authentication attempt for client " + clientId + ": username=" + username + ", password=" + password);
        
        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            System.out.println("Available users: " + availableUsers.size());
            for (User availableUser : availableUsers) {
                System.out.println("  - " + availableUser.getUsername() + " (" + availableUser.getNickname() + ")");
            }
            return false;
        }
        
        if (!user.getPassword().equals(password)) {
            System.out.println("Password mismatch for user: " + username);
            System.out.println("Expected: " + user.getPassword() + ", Got: " + password);
            return false;
        }
        
        // Check if user is already logged in
        if (isUserAlreadyLoggedIn(username)) {
            System.out.println("User " + username + " is already logged in");
            return false;
        }
        
        authenticatedUsers.put(clientId, user);
        System.out.println("User authenticated: " + username + " for client: " + clientId);
        return true;
=======
        System.out.println("Authentication attempt for client " + clientId + ": username=" + username + ", password=" + password);
        
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
>>>>>>> main
    }
    
    private boolean isUserAlreadyLoggedIn(String username) {
        for (User user : authenticatedUsers.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
<<<<<<< HEAD
=======
    // Lobby management methods
    public String createLobby(String hostClientId) {
        String lobbyId = UUID.randomUUID().toString();
        Lobby lobby = new Lobby(lobbyId, hostClientId);
        lobbies.put(lobbyId, lobby);
        
        // Add host to lobby
        User hostUser = authenticatedUsers.get(hostClientId);
        if (hostUser != null) {
            lobby.addPlayer(hostClientId, hostUser);
            clientToLobby.put(hostClientId, lobbyId);
        }
        
        System.out.println("Lobby created: " + lobbyId + " by " + hostUser.getUsername());
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
    
    public String getClientLobbyId(String clientId) {
        return clientToLobby.get(clientId);
    }
    

    
>>>>>>> main
    public User getAuthenticatedUser(String clientId) {
        return authenticatedUsers.get(clientId);
    }
    
<<<<<<< HEAD
=======
    public String getClientIdByUsername(String username) {
        for (java.util.Map.Entry<String, User> entry : authenticatedUsers.entrySet()) {
            if (entry.getValue().getUsername().equals(username)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
>>>>>>> main
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
<<<<<<< HEAD
        return new ArrayList<>(availableUsers);
=======
        return availableUsers;
    }
    
    public AuthManager getAuthManager() {
        return AuthManager.getInstance();
>>>>>>> main
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