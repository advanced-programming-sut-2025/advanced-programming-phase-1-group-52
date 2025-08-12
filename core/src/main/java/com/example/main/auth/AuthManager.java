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


public class AuthManager {
    private static AuthManager instance;
    private final Map<String, User> usersByUsername;
    private final Map<String, String> onlineUsers;
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


    public AuthResult authenticate(String username, String password) {
        User user = usersByUsername.get(username);
        if (user == null) {
            return new AuthResult(false, "User not found", null);
        }

        if (!user.getPassword().equals(password)) {
            return new AuthResult(false, "Invalid password", null);
        }


        if (onlineUsers.containsKey(username)) {
            return new AuthResult(false, "User already logged in", null);
        }

        return new AuthResult(true, "Authentication successful", null);
    }


    public void setUserOnline(String username, String clientId) {
        onlineUsers.put(username, clientId);
    }


    public boolean logoutByUsername(String username) {
        return onlineUsers.remove(username) != null;
    }


    public List<String> getOnlineUsers() {
        return new ArrayList<>(onlineUsers.keySet());
    }


    public List<User> getAllUsers() {
        return new ArrayList<>(usersByUsername.values());
    }


    public boolean registerUser(User user) {
        if (usersByUsername.containsKey(user.getUsername())) {
            return false;
        }

        usersByUsername.put(user.getUsername(), user);
        saveUsers();
        return true;
    }


    public boolean userExists(String username) {
        return usersByUsername.containsKey(username);
    }


    public User getUser(String username) {
        return usersByUsername.get(username);
    }


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


    public void clearAllOnlineUsers() {
        onlineUsers.clear();
    }


    public static void resetInstance() {
        instance = null;
    }
}
