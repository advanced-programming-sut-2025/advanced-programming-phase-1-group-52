package com.example.main.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private Game game;
    private final List<User> availableUsers;
    
    public GameServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.running = new AtomicBoolean(false);
        this.executorService = Executors.newCachedThreadPool();
        this.connectedClients = new ConcurrentHashMap<>();
        this.authenticatedUsers = new ConcurrentHashMap<>();
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
        
        // Remove authenticated user
        authenticatedUsers.remove(clientId);
        
        System.out.println("Client removed: " + clientId + " (Total: " + connectedClients.size() + ")");
    }
    
    public boolean authenticateUser(String clientId, String username, String password) {
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
    }
    
    private boolean isUserAlreadyLoggedIn(String username) {
        for (User user : authenticatedUsers.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public User getAuthenticatedUser(String clientId) {
        return authenticatedUsers.get(clientId);
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
        return new ArrayList<>(availableUsers);
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