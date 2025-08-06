package com.example.main.network.common;

import java.util.HashMap;
import com.google.gson.Gson;
import com.example.main.network.serialization.GsonAdapters;

public class Message {
	private MessageType type;
	private HashMap<String, Object> body;
	private transient Gson gson = GsonAdapters.getGsonInstance(); // Changed to use GsonAdapters

	public Message() {
		this.body = new HashMap<>();
	}

	public Message(HashMap<String, Object> body, MessageType type) {
		this.body = body;
		this.type = type;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public HashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(HashMap<String, Object> body) {
		this.body = body;
	}

	public <T> T getFromBody(String fieldName) {
		return (T) body.get(fieldName);
	}

	// New method for generic deserialization
	public <T> T getFromBodyAs(String fieldName, Class<T> type) {
		Object obj = body.get(fieldName);
		if (obj == null) {
			return null;
		}
		// Convert the object (which might be a LinkedTreeMap) back to JSON and then deserialize to the target type
		return gson.fromJson(gson.toJsonTree(obj), type);
	}

	public void putInBody(String fieldName, Object value) {
		body.put(fieldName, value);
	}

	public int getIntFromBody(String fieldName) {
		Object value = body.get(fieldName);
		if (value instanceof Double) {
			return ((Double) value).intValue();
		} else if (value instanceof Integer) {
			return (Integer) value;
		}
		return 0;
	}
}