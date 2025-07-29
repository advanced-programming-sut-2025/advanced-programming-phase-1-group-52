package com.example.main.network.client;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.utils.Json;
import com.example.main.network.common.Message;

/**
 * Listens for incoming messages from the server
 */
public class ClientNetworkListener implements Runnable {
    private final GameClient client;
    
    public ClientNetworkListener(GameClient client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        BufferedReader in = client.getInputStream();
        
        while (client.isRunning()) {
            try {
                String messageJson = in.readLine();
                if (messageJson != null) {
                    Message message = parseMessageFromJson(messageJson);
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
            Json json = new Json();
            Message message = json.fromJson(Message.class, messageJson);
            System.out.println("Client received message: " + message.getType());
            return message;
        } catch (Exception e) {
            System.err.println("Error parsing message JSON: " + e.getMessage());
            System.err.println("Message JSON: " + messageJson);
            // Return null instead of a dummy message to avoid confusion
            return null;
        }
    }
}