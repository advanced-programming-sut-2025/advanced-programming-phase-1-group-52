package models;

import java.util.ArrayList;

public class Game {
    private ArrayList<User> players;
    private gameMap map;
    private Player currentPlayer;
    private Date date;
    private Time time;

    public Game(ArrayList<User> players, gameMap map) {
        this.players = players;
        this.map = map;
        this.currentPlayer = players.getFirst().getPlayer();
    }

    public ArrayList<User> players() {
        return players;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public gameMap map() {
        return map;
    }
    public void setGameMap(gameMap map) {
        this.map = map;
    }

    public Player currentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Date date() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time time() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
