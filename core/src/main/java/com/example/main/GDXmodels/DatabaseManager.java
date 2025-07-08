package com.example.main.GDXmodels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.example.main.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String USERS_FILE = "users.json";
    private final Json json;
    private final FileHandle fileHandle;

    public DatabaseManager() {
        json = new Json();
        fileHandle = Gdx.files.local(USERS_FILE);
    }

    public void saveUsers(List<User> users) {
        String data = json.prettyPrint(users);
        fileHandle.writeString(data, false);
    }

    public List<User> loadUsers() {
        if (!fileHandle.exists()) return new ArrayList<>();
        return json.fromJson(ArrayList.class, User.class, fileHandle);
    }
}
