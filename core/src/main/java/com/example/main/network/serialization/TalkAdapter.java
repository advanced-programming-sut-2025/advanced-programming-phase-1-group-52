package com.example.main.network.serialization;

import com.example.main.models.Player;
import com.example.main.models.Talk;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TalkAdapter implements JsonSerializer<Talk>, JsonDeserializer<Talk> {

    @Override
    public JsonElement serialize(Talk src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("senderUsername", src.getSender().getUsername());
        jsonObject.addProperty("receiverUsername", src.getReceiver().getUsername());
        jsonObject.addProperty("message", src.getMessage());
        return jsonObject;
    }

    @Override
    public Talk deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String senderUsername = jsonObject.get("senderUsername").getAsString();
        String receiverUsername = jsonObject.get("receiverUsername").getAsString();
        String message = jsonObject.get("message").getAsString();

        // Create dummy Player objects for deserialization. Actual Player objects
        // will need to be linked up in a post-processing step if they refer to
        // actual game players.
        Player dummySender = new Player(senderUsername, null); // Gender can be null or a default
        Player dummyReceiver = new Player(receiverUsername, null); // Gender can be null or a default

        return new Talk(dummySender, dummyReceiver, message);
    }
}