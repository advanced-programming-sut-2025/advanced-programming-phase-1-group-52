package com.example.main.network.serialization;

import com.example.main.models.Friendship;
import com.example.main.models.Player;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FriendshipAdapter implements JsonSerializer<Friendship>, JsonDeserializer<Friendship> {

    @Override
    public JsonElement serialize(Friendship src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("friendshipLevel", src.getFriendshipLevel());
        jsonObject.addProperty("friendshipPoints", src.getFriendshipPoints());

        JsonArray playersArray = new JsonArray();
        for (Player player : src.getPlayers()) {
            // Only serialize player's username to break the cycle
            if (player != null) { // Add null check here
                playersArray.add(player.getUsername());
            }
        }
        jsonObject.add("playersUsernames", playersArray);

        return jsonObject;
    }

    @Override
    public Friendship deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int friendshipLevel = jsonObject.get("friendshipLevel").getAsInt();
        int friendshipPoints = jsonObject.get("friendshipPoints").getAsInt();

        JsonArray playersUsernamesArray = jsonObject.getAsJsonArray("playersUsernames");
        ArrayList<Player> players = new ArrayList<>();
        for (JsonElement element : playersUsernamesArray) {
            String username = element.getAsString();
            // Create a dummy Player object with just the username.
            // The actual Player object linkage needs to happen at a higher level (e.g., in Game).
            // For now, this prevents circular deserialization.
            players.add(new Player(username, null)); // Gender can be null or a default for now
        }

        // Note: The Friendship constructor expects two Player objects directly.
        // We are creating a dummy one here to avoid circular dependency during deserialization.
        // A post-processing step in Game.java or GameServer.java will be required to link these.
        Friendship friendship = new Friendship(players.get(0), players.get(1));
        friendship.setFriendshipLevel(friendshipLevel);
        friendship.addFriendshipPoints(friendshipPoints - friendship.getFriendshipPoints()); // Adjust points without re-adding

        return friendship;
    }
}