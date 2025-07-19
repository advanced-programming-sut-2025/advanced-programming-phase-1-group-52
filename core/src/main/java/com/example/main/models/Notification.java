// In main/models/Notification.java

package com.example.main.models;

public record Notification(Player sender, String message) {
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        // Handle system notifications where the sender is null
        if (sender == null) {
            return "System: " + message + "\n-------------------\n";
        }
        return sender.getUsername() + ": " + message + "\n-------------------\n";
    }
}
