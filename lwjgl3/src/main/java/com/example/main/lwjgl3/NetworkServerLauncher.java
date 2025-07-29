package com.example.main.lwjgl3;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.example.main.models.User;
import com.example.main.network.NetworkConstants;
import com.example.main.network.server.GameServer;

/**
 * Launches the network game server
 */
public class NetworkServerLauncher {
    
    public static void main(String[] args) {
        String ip = NetworkConstants.DEFAULT_HOST;
        int port = NetworkConstants.DEFAULT_PORT;
        
        // Parse command line arguments
        if (args.length >= 1) {
            ip = args[0];
        }
        if (args.length >= 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[1]);
                System.err.println("Usage: NetworkServerLauncher [ip] [port]");
                System.exit(1);
            }
        }
        
        System.out.println("=== Stardew Valley Network Server ===");
        System.out.println("Starting server on " + ip + ":" + port);
        System.out.println("Press Ctrl+C to stop the server");
        
        try {
            GameServer server = new GameServer(port);
            
            // Load users from assets/users.json database
            System.out.println("Loading users from assets/users.json...");
            List<User> users = loadUsersFromAssets();
            
            if (users != null && !users.isEmpty()) {
                System.out.println("Loaded " + users.size() + " users from database:");
                for (User user : users) {
                    server.addAvailableUser(user);
                    System.out.println("  - " + user.getUsername() + " (" + user.getNickname() + ")");
                }
            } else {
                System.out.println("No users found in database. Adding default test users.");
                // Add some test users if database is empty
                server.addAvailableUser(new User("player1", "password1", "Player 1", "player1@test.com", null));
                server.addAvailableUser(new User("player2", "password2", "Player 2", "player2@test.com", null));
                server.addAvailableUser(new User("player3", "password3", "Player 3", "player3@test.com", null));
                server.addAvailableUser(new User("player4", "password4", "Player 4", "player4@test.com", null));
            }
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down server...");
                server.stop();
            }));
            
            server.start();
            
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            System.exit(1);
        }
    }
    
        private static List<User> loadUsersFromAssets() {
        File file = new File("assets/users.json");
        if (!file.exists()) {
            System.err.println("users.json not found in assets directory");
            return new ArrayList<>();
        }

        StringBuilder content = new StringBuilder();
        try (FileReader reader = new FileReader(file)) {
            int character;
            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }

            Json json = new Json();
            // Configure Json to handle LibGDX format
            json.setTypeName("class");
            json.setOutputType(JsonWriter.OutputType.json);
            return json.fromJson(ArrayList.class, User.class, content.toString());
        } catch (IOException e) {
            System.err.println("Error loading users from assets/users.json: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error parsing users from assets/users.json: " + e.getMessage());
            System.err.println("Content: " + content.toString());
            return new ArrayList<>();
        }
    }
} 