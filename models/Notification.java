package models;

public record Notification(Player sender, String message) {
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return sender + ": " + message + "\n-------------------\n";
    }
}
