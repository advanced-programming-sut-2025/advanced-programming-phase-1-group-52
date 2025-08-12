package com.example.main.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.example.main.network.common.Message;
import com.example.main.network.common.MessageType;

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


        if (client.isRunning()) {
            System.out.println("Connection to server lost");
            client.disconnect();
        }
    }

    private Message parseMessageFromJson(String messageJson) {
        try {
            System.out.println("Parsing message JSON: " + messageJson);




            String innerJson = messageJson.substring(1, messageJson.length() - 1);


            int bodyIndex = innerJson.indexOf(",\"body\":");
            String typePart = innerJson.substring(0, bodyIndex);
            String bodyPart = innerJson.substring(bodyIndex + 8);


            String typeStr = typePart.split(":")[1].replace("\"", "");
            System.out.println("Parsed type: " + typeStr);
            MessageType type = MessageType.valueOf(typeStr);


            String bodyContent = bodyPart;
            if (bodyContent.startsWith("{")) {
                bodyContent = bodyContent.substring(1);
            }
            if (bodyContent.endsWith("}")) {
                bodyContent = bodyContent.substring(0, bodyContent.length() - 1);
            }
            System.out.println("Parsed body content: " + bodyContent);
            HashMap<String, Object> body = new HashMap<>();


            if (!bodyContent.isEmpty()) {

                String[] pairs = bodyContent.split(",\"");
                for (String pair : pairs) {

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

            return null;
        }
    }
}
