package com.example.main.events;

/**
 * An event published when the client receives a command from the server
 * to navigate to the main game screen.
 */
public class NavigateToGameScreenEvent implements AppEvent {
    // This event needs no data. Its existence is the command.
    public NavigateToGameScreenEvent() {}
}
