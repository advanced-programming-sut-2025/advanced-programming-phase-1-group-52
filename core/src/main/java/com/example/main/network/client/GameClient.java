package com.example.main.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.main.models.Game;
import com.example.main.models.User;
import com.example.main.network.NetworkConstants;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

/**
 * Game client that connects to the server and handles network communication
 */
public class GameClient {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final AtomicBoolean connected;
    private final AtomicBoolean authenticated;
    private final ExecutorService executorService;
    private User authenticatedUser;
    private Game currentGame;
    private final ClientMessageHandler messageHandler;
    private final ClientNetworkListener networkListener;
    
    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.connected = new AtomicBoolean(false);
        this.authenticated = new AtomicBoolean(false);
        this.executorService = Executors.newCachedThreadPool();
        this.messageHandler = new ClientMessageHandler(this);
        this.networkListener = new ClientNetworkListener(this);
    }
    
    public boolean connect() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            connected.set(true);
            
            System.out.println("Connected to server at " + host + ":" + port);
            
            // Start listening for messages from server
            executorService.execute(networkListener);
            
            return true;
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }
    
    public boolean authenticate(String username, String password) {
        if (!connected.get()) {
            System.err.println("Not connected to server");
            return false;
        }
        
        try {
            HashMap<String, Object> authData = new HashMap<>();
            authData.put("username", username);
            authData.put("password", password);
            Message authMessage = new Message(authData, MessageType.AUTHENTICATION);
            System.out.println("Sending authentication message for user: " + username);
            sendMessage(authMessage);
            
            // Wait for authentication response
            // In a real implementation, you might want to use a callback or future
            Thread.sleep(2000); // Increased wait time
            
            boolean authResult = authenticated.get();
            System.out.println("Authentication result: " + authResult);
            return authResult;
        } catch (Exception e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return false;
        }
    }
    
    public void sendPlayerAction(String action, Object actionData) {
        if (!authenticated.get()) {
            System.err.println("Not authenticated");
            return;
        }
        
        HashMap<String, Object> actionDataMap = new HashMap<>();
        actionDataMap.put("action", action);
        actionDataMap.put("actionData", actionData);
        Message actionMessage = new Message(actionDataMap, MessageType.PLAYER_ACTION);
        sendMessage(actionMessage);
    }
    
    public void sendMessage(Message message) {
        if (connected.get() && out != null) {
            try {
                // Use proper JSON serialization
                com.badlogic.gdx.utils.Json json = new com.badlogic.gdx.utils.Json();
                String messageJson = json.toJson(message);
                out.println(messageJson);
            } catch (Exception e) {
                System.err.println("Error sending message: " + e.getMessage());
                disconnect();
            }
        }
    }
    
    public void disconnect() {
        connected.set(false);
        authenticated.set(false);
        
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
        
        executorService.shutdown();
        System.out.println("Disconnected from server");
    }
    
    public void handleMessage(Message message) {
        messageHandler.handleMessage(message);
    }
    
    public boolean isConnected() {
        return connected.get();
    }
    
    public boolean isAuthenticated() {
        return authenticated.get();
    }
    
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
    
    public void setAuthenticatedUser(User user) {
        this.authenticatedUser = user;
        this.authenticated.set(user != null);
    }
    
    public Game getCurrentGame() {
        return currentGame;
    }
    
    public void setCurrentGame(Game game) {
        this.currentGame = game;
    }
    
    public BufferedReader getInputStream() {
        return in;
    }
    
    public boolean isRunning() {
        return connected.get();
    }
    
    // Getters for testing
    public String getHost() { return host; }
    public int getPort() { return port; }
    
    public static void main(String[] args) {
        GameClient client = new GameClient(NetworkConstants.DEFAULT_HOST, NetworkConstants.DEFAULT_PORT);
        
        if (client.connect()) {
            // Test authentication with a valid user from assets/users.json
            boolean authSuccess = client.authenticate("sanyar", "sanyar");
            System.out.println("Authentication: " + (authSuccess ? "SUCCESS" : "FAILED"));
            
            if (authSuccess) {
                // Test sending a player action
                client.sendPlayerAction("MOVE", "UP");
                
                // Keep the client running for a while
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        client.disconnect();
    }
} 