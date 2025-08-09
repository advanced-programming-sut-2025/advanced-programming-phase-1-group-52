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
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;
import com.google.gson.Gson;

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
    private final ClientNetworkListener networkListener;
    private Object controllerCallback; // For GUI callbacks
    private final Gson gson;
    private final ClientMessageHandler messageHandler; // <-- Add this field

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.connected = new AtomicBoolean(false);
        this.authenticated = new AtomicBoolean(false);
        this.executorService = Executors.newCachedThreadPool();
        this.networkListener = new ClientNetworkListener(this);
        this.gson = new Gson();
        this.messageHandler = new ClientMessageHandler(this); // <-- Instantiate it here
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

    // =================================================================
    // IMPORTANT: Replace the entire handleMessage method with this one
    // =================================================================
    public void handleMessage(Message message) {
        if (message == null) {
            return;
        }
        // Delegate all message handling to the single, dedicated message handler.
        // This ensures that all message types are processed correctly.
        messageHandler.handleMessage(message);
    }
    // =================================================================


    public void setAuthenticated(boolean status) {
        this.authenticated.set(status);
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

            // Wait for authentication response.
            Thread.sleep(3000);

            boolean authResult = authenticated.get();
            System.out.println("Authentication result: " + authResult);
            return authResult;
        } catch (Exception e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return false;
        }
    }

    public void sendPlayerAction(String action, Object actionData) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("action", action);
        body.put("actionData", actionData);
        Message actionMessage = new Message(body, MessageType.PLAYER_ACTION);
        sendMessage(actionMessage);
    }

    public void sendMessage(Message message) {
        if (connected.get() && out != null) {
            try {
                String messageJson = gson.toJson(message);
                out.println(messageJson);
                out.flush();
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

    public void logout() {
        if (!connected.get()) {
            System.err.println("Cannot log out, not connected.");
            return;
        }

        System.out.println("Logging out user: " + (authenticatedUser != null ? authenticatedUser.getUsername() : ""));

        Message disconnectMessage = new Message(new HashMap<>(), MessageType.DISCONNECT);
        sendMessage(disconnectMessage);

        setAuthenticatedUser(null);
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

    public String getHost() { return host; }
    public int getPort() { return port; }

    public void setControllerCallback(Object callback) {
        this.controllerCallback = callback;
    }

    public Object getControllerCallback() {
        return controllerCallback;
    }

    public static void main(String[] args) {
        System.out.println("GameClient main method - for testing only");
        System.out.println("GUI clients should use NetworkClientLauncher instead");
    }
}
