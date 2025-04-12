package models;

import java.util.ArrayList;

public class Game {
    private final ArrayList<User> players;
    private final gameMap map;
    private Player currentPlayer;
    private Date date;
    private Time time;

    public Game(ArrayList<User> players, gameMap map) {
        this.players = players;
        this.map = map;
        this.currentPlayer = players.getFirst().getPlayer();
    }
}
