package com.example.main.network.serialization;

import com.example.main.models.Player;
import com.example.main.models.User;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User> {

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", user.getUsername());
        jsonObject.addProperty("nickname", user.getNickname());
        jsonObject.addProperty("email", user.getEmail());
        // You may need to add other basic User fields here
        // To break the circular dependency, DO NOT serialize the full Player object here.
        // If you need player info, serialize only its ID or username.
        // For now, let's assume the client will reconstruct its Player from User data.
        if (user.getPlayer() != null) {
            jsonObject.addProperty("currentPlayerUsername", user.getPlayer().getUsername());
        }
        return jsonObject;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String username = jsonObject.get("username").getAsString();
        String nickname = jsonObject.get("nickname").getAsString();
        String email = jsonObject.get("email").getAsString();

        User user = new User(username, null, nickname, email, null); // Password and Gender are not serialized for game state

        // Note: currentPlayer will NOT be directly deserialized here.
        // The game setup logic on the client (e.g., in handleInitializeGame) will need to recreate the Player.
        return user;
    }
}