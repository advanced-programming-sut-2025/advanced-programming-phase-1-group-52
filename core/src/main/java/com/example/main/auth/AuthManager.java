package com.example.main.auth;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.utils.Json;
import com.example.main.models.User;

/**
 * Simple authentication manager that reads from users.json file
 */
public class AuthManager {
    private static AuthManager instance;
    private final Map<String, User> usersByUsername;
    private final Map<String, String> onlineUsers; // username -> clientId
    private final Json json;
    private static final String USERS_FILE = "assets/users.json";
    
    private AuthManager() {
        this.usersByUsername = new ConcurrentHashMap<>();
        this.onlineUsers = new ConcurrentHashMap<>();
        this.json = new Json();
        loadUsers();
    }
    
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }
    
    /**
     * Authenticate user with username and password
     * @param username Username
     * @param password Password
     * @return AuthResult with success status
     */
    public AuthResult authenticate(String username, String password) {
        User user = usersByUsername.get(username);
        if (user == null) {
            return new AuthResult(false, "User not found", null);
        }
        
        if (!user.getPassword().equals(password)) {
            return new AuthResult(false, "Invalid password", null);
        }
        
        // Check if user is already logged in
        if (onlineUsers.containsKey(username)) {
            return new AuthResult(false, "User already logged in", null);
        }
        
        return new AuthResult(true, "Authentication successful", null);
    }
    
    /**
     * Mark user as online
     * @param username Username
     * @param clientId Client ID
     */
    public void setUserOnline(String username, String clientId) {
        onlineUsers.put(username, clientId);
    }
    
    /**
     * Mark user as offline
     * @param username Username
     * @return true if logout successful
     */
    public boolean logoutByUsername(String username) {
        return onlineUsers.remove(username) != null;
    }
    
    /**
     * Get all online users
     * @return List of usernames of online users
     */
    public List<String> getOnlineUsers() {
        return new ArrayList<>(onlineUsers.keySet());
    }
    
    /**
     * Get all users (for registration, etc.)
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(usersByUsername.values());
    }
    
    /**
     * Register new user
     * @param user User to register
     * @return true if registration successful
     */
    public boolean registerUser(User user) {
        if (usersByUsername.containsKey(user.getUsername())) {
            return false; // User already exists
        }
        
        usersByUsername.put(user.getUsername(), user);
        saveUsers();
        return true;
    }
    
    /**
     * Check if user exists
     * @param username Username to check
     * @return true if user exists
     */
    public boolean userExists(String username) {
        return usersByUsername.containsKey(username);
    }
    
    /**
     * Get user by username
     * @param username Username
     * @return User or null if not found
     */
    public User getUser(String username) {
        return usersByUsername.get(username);
    }
    
    /**
     * Check if user is online
     * @param username Username to check
     * @return true if user is online
     */
    public boolean isUserOnline(String username) {
        return onlineUsers.containsKey(username);
    }
    
    private void loadUsers() {
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    List<User> users = json.fromJson(ArrayList.class, User.class, reader);
                    for (User user : users) {
                        usersByUsername.put(user.getUsername(), user);
                    }
                    System.out.println("Loaded " + users.size() + " users from database:");
                    for (User user : users) {
                        System.out.println("  - " + user.getUsername() + " (" + user.getNickname() + ")");
                    }
                }
            } else {
                System.out.println("Users file not found: " + USERS_FILE);
                System.out.println("Please create the users.json file with your user data.");
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
    

    
    private void saveUsers() {
        try {
            String data = json.prettyPrint(new ArrayList<>(usersByUsername.values()));
            try (java.io.FileWriter writer = new java.io.FileWriter(USERS_FILE)) {
                writer.write(data);
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    /**
     * Clear all online users (for testing)
     */
    public void clearAllOnlineUsers() {
        onlineUsers.clear();
    }
    
    /**
     * Reset the AuthManager instance (for testing)
     */
    public static void resetInstance() {
        instance = null;
    }
} 