

package com.example.main.models;

public record Notification(Player sender, String message) {
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {

        if (sender == null) {
            return "System: " + message + "\n-------------------\n";
        }
        return sender.getUsername() + ": " + message + "\n-------------------\n";
    }
}
