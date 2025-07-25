package com.example.main.GDXmodels;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.example.main.models.User;

public class DatabaseManager {
    private static final String USERS_FILE = "users.json";
    private final Json json;
    private final FileHandle fileHandle;
    private final boolean isTerminalMode;

    public DatabaseManager() {
        json = new Json();
        
        // Check if we're in terminal mode (Gdx.files is null)
        if (Gdx.files == null) {
            isTerminalMode = true;
            fileHandle = null;
        } else {
            isTerminalMode = false;
            fileHandle = Gdx.files.local(USERS_FILE);
        }
    }

    public void saveUsers(List<User> users) {
        String data = json.prettyPrint(users);
        
        if (isTerminalMode) {
            saveUsersTerminal(data);
        } else {
            fileHandle.writeString(data, false);
        }
    }

    public List<User> loadUsers() {
        if (isTerminalMode) {
            return loadUsersTerminal();
        } else {
            if (!fileHandle.exists()) return new ArrayList<>();
            return json.fromJson(ArrayList.class, User.class, fileHandle);
        }
    }
    
    private void saveUsersTerminal(String data) {
        try (FileWriter writer = new FileWriter(USERS_FILE)) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    private List<User> loadUsersTerminal() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (FileReader reader = new FileReader(file)) {
            StringBuilder content = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                content.append((char) character);
            }
            return json.fromJson(ArrayList.class, User.class, content.toString());
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
