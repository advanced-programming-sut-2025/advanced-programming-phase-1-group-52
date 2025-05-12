package models;

public class Talk {
    private final Player receiver;
    private final String message;

    public Talk(Player receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public Player getReceiver() {
        return receiver;
    }

    @Override
    public String toString() {
        return receiver + ":\n" + message + "\n----------------------\n";
    }
}
