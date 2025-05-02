package models;

import java.util.ArrayList;

public class Game {
    private ArrayList<User> players;
    private User mainPlayer;
    private GameMap map;
    private Player currentPlayer;
    private Date date;
    private Time time;

    public Game(ArrayList<User> players) {
        this.time = new Time();
        this.date = new Date();
        this.players = players;
        this.currentPlayer = players.getFirst().getPlayer();
    }

    public ArrayList<User> players() {
        return players;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public GameMap map() {
        return map;
    }

    public void setMap(GameMap map) {
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

    public User mainPlayer() {
        return mainPlayer;
    }

    public void setMainPlayer(User mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public void switchCurrentPlayer() {
        for(int i = 0 ; i < players.size() ; i++){
            if(players.get(i).equals(currentPlayer)){
                if(i == 3){
                    setCurrentPlayer(players.get(0).currentPlayer());
                }
                setCurrentPlayer(players.get(i+1).currentPlayer());
            }
        }
        timePassed();
    }

    public void timePassed() {
        this.time.hourPassed();
        if(this.time.isDayOver()){
            this.date.dayPassed();
            this.date.isSeasonOver();
        }
    }
}
