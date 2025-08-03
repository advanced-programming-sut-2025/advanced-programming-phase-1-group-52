package com.example.main.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

/**
 * Listens for incoming messages from the server
 */
public class ClientNetworkListener implements Runnable {
    private final GameClient client;
    private final Gson gson;
    
    public ClientNetworkListener(GameClient client) {
        this.client = client;
        this.gson = new Gson();
    }
    
    @Override
    public void run() {
        BufferedReader in = client.getInputStream();
        
        while (client.isRunning()) {
            try {
                String messageJson = in.readLine();
                if (messageJson != null) {
                    Message message = gson.fromJson(messageJson, Message.class);
                    if (message != null) {
                        client.handleMessage(message);
                    } else {
                        System.err.println("Failed to parse message, skipping");
                    }
                } else {
                    // Connection closed by server
                    break;
                }
            } catch (IOException e) {
                if (client.isRunning()) {
                    System.err.println("Error reading from server: " + e.getMessage());
                }
                break;
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
            }
        }
        
        // Connection lost
        if (client.isRunning()) {
            System.out.println("Connection to server lost");
            client.disconnect();
        }
    }
    
    private Message parseMessageFromJson(String messageJson) {
        try {
            System.out.println("Parsing message JSON: " + messageJson);
            // Parse JSON manually to handle the format we're sending
            // Expected format: {"type":"AUTH_SUCCESS","body":{"username":"testuser","nickname":"Test User","email":"test@example.com"}}
            
            // Remove the outer braces
            String innerJson = messageJson.substring(1, messageJson.length() - 1);
            
            // Split by the first occurrence of ","body": to separate type and body
            int bodyIndex = innerJson.indexOf(",\"body\":");
            String typePart = innerJson.substring(0, bodyIndex);
            String bodyPart = innerJson.substring(bodyIndex + 8); // Skip ","body":
            
            // Extract type
            String typeStr = typePart.split(":")[1].replace("\"", "");
            System.out.println("Parsed type: " + typeStr);
            MessageType type = MessageType.valueOf(typeStr);
            
            // Extract body (remove outer braces if present)
            String bodyContent = bodyPart;
            if (bodyContent.startsWith("{")) {
                bodyContent = bodyContent.substring(1);
            }
            if (bodyContent.endsWith("}")) {
                bodyContent = bodyContent.substring(0, bodyContent.length() - 1);
            }
            System.out.println("Parsed body content: " + bodyContent);
            HashMap<String, Object> body = new HashMap<>();
            
            // Parse body key-value pairs
            if (!bodyContent.isEmpty()) {
                // Simple approach: split by "," and process each pair
                String[] pairs = bodyContent.split(",\"");
                for (String pair : pairs) {
                    // Handle the first pair specially since it won't start with "
                    String cleanPair = pair;
                    if (!pair.startsWith("\"")) {
                        cleanPair = "\"" + pair;
                    }
                    
                    if (cleanPair.contains(":")) {
                        int colonIndex = cleanPair.indexOf(":");
                        String key = cleanPair.substring(0, colonIndex).replace("\"", "").trim();
                        String value = cleanPair.substring(colonIndex + 1).replace("\"", "").trim();
                        body.put(key, value);
                    }
                }
            }
            
            System.out.println("Parsed body: " + body);
            Message message = new Message(body, type);
            return message;
        } catch (Exception e) {
            System.err.println("Error parsing message JSON: " + e.getMessage());
            System.err.println("Message JSON: " + messageJson);
            e.printStackTrace();
            // Return null instead of a dummy message to avoid confusion
            return null;
        }
    }
}