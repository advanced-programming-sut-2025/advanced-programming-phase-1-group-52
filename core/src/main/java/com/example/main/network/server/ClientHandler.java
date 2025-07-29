package com.example.main.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

import com.example.main.models.User;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

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
    
    public ClientHandler(Socket socket, GameServer server) throws IOException {
        this.clientSocket = socket;
        this.server = server;
        this.clientId = UUID.randomUUID().toString();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        try {
            // Parse the message JSON and create Message object
            Message message = parseMessageFromJson(messageJson);
            
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
            String username = message.getFromBody("username");
            String password = message.getFromBody("password");
            
            if (server.authenticateUser(clientId, username, password)) {
                authenticated = true;
                try {
                    sendAuthSuccessMessage(server.getAuthenticatedUser(clientId));
                    System.out.println("Authentication successful for client: " + clientId);
                    
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
    
    private void handlePlayerAction(Message message) {
        if (!authenticated) {
            sendErrorMessage("Not authenticated");
            return;
        }
        
        try {
            String action = message.getFromBody("action");
            Object actionData = message.getFromBody("actionData");
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
        if (running && out != null) {
            try {
                // Use proper JSON serialization
                com.badlogic.gdx.utils.Json json = new com.badlogic.gdx.utils.Json();
                String messageJson = json.toJson(message);
                out.println(messageJson);
            } catch (Exception e) {
                System.err.println("Error sending message to client " + clientId + ": " + e.getMessage());
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
    
    private void sendErrorMessage(String error) {
        HashMap<String, Object> errorData = new HashMap<>();
        errorData.put("error", error);
        Message errorMessage = new Message(errorData, MessageType.ERROR);
        sendMessage(errorMessage);
    }
    
    private void sendAuthSuccessMessage(User user) {
        HashMap<String, Object> authData = new HashMap<>();
        authData.put("user", user);
        Message authMessage = new Message(authData, MessageType.AUTH_SUCCESS);
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
} 