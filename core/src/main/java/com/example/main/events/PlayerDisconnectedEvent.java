package com.example.main.events;

/**
 * An event that is published when a player disconnects from a game.
 * The GameMenuController listens for this to update the player list.
 */
public class PlayerDisconnectedEvent implements AppEvent {
    private final String username;

    public PlayerDisconnectedEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
