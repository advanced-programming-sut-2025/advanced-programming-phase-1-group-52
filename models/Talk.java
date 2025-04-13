package models;

public class Talk {
    private final Player receiver;
    private final String message;

    public Talk(Player receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }
}
