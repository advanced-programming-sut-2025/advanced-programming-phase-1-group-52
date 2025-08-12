package com.example.main.events;


public class PlayerDisconnectedEvent implements AppEvent {
    private final String username;

    public PlayerDisconnectedEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
