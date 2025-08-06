package com.example.main.network.serialization;

import com.example.main.models.NPC;
import com.example.main.models.NPCFriendship;
import com.example.main.models.Player;
import com.example.main.enums.design.NPCType;
import com.example.main.enums.player.Gender;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NPCFriendshipAdapter implements JsonSerializer<NPCFriendship>, JsonDeserializer<NPCFriendship> {

    @Override
    public JsonElement serialize(NPCFriendship src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("friendshipLevel", src.getFriendshipLevel());
        jsonObject.addProperty("friendshipPoints", src.getFriendshipPoints());

        // Serialize only the NPCType and Player's username to break cycles
        if (src.getNpc() != null) {
            jsonObject.addProperty("npcType", src.getNpc().getType().name());
        }
        if (src.getPlayer() != null) {
            jsonObject.addProperty("playerUsername", src.getPlayer().getUsername());
        }

        return jsonObject;
    }

    @Override
    public NPCFriendship deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int friendshipLevel = jsonObject.get("friendshipLevel").getAsInt();
        int friendshipPoints = jsonObject.get("friendshipPoints").getAsInt();

        // Deserialize NPCType and Player username
        NPCType npcType = null;
        if (jsonObject.has("npcType")) {
            npcType = NPCType.valueOf(jsonObject.get("npcType").getAsString());
        }

        String playerUsername = null;
        if (jsonObject.has("playerUsername")) {
            playerUsername = jsonObject.get("playerUsername").getAsString();
        }

        // Create dummy NPC and Player objects. Actual linking happens in Game/GameServer.
        NPC dummyNpc = null;
        if (npcType != null) {
            dummyNpc = new NPC(npcType, new ArrayList<Player>()); // Pass empty list for players for dummy creation
        }

        Player dummyPlayer = null;
        if (playerUsername != null) {
            dummyPlayer = new Player(playerUsername, Gender.Male); // Default gender for dummy
        }

        // Instantiate NPCFriendship using dummy objects. Real objects linked post-deserialization.
        NPCFriendship friendship = new NPCFriendship(dummyNpc, dummyPlayer);
        friendship.setFriendshipLevel(friendshipLevel);
        friendship.setFriendshipPoints(friendshipPoints); // Use the new setter

        return friendship;
    }
}