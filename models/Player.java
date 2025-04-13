package models;

import java.util.ArrayList;

public class Player {
    private final String username;
    private final Inventory inventory;
    private final ArrayList<Trade> trades;

    public Player(String username) {
        this.username = username;
        this.inventory = new Inventory();
        this.trades = new ArrayList<>();
    }
}
