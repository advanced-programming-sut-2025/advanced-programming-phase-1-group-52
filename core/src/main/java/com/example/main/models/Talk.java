package com.example.main.models;

public class Talk {
    private final Player sender;
    private final Player receiver;
    private final String message;

    public Talk(Player sender, Player receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return sender.getUsername() + " to " + receiver.getUsername() + ": " + message;
    }
}
