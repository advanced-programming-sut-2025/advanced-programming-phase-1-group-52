package models;

import enums.design.Weather;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Game {
    private ArrayList<User> players;
    private User mainPlayer;
    private GameMap map;
    private Player currentPlayer;
    private Date date;
    private Time time;
    private Weather todayWeather;
    private Weather tomorrowWeather;

    public Game(ArrayList<User> players) {
        this.time = new Time();
        this.date = new Date();
        this.players = players;
        this.currentPlayer = players.getFirst().getPlayer();
        this.todayWeather = Weather.Sunny;
        this.tomorrowWeather = Weather.Rainy;
    }

    public boolean switchCurrentPlayer() {
        int currentIndex = players.indexOf(currentPlayer);
        if (currentIndex == -1) return false;

        Player nextPlayer = findNextAvailablePlayer(currentIndex);

        if (nextPlayer == null) {
            return false;
        }

        setCurrentPlayer(nextPlayer);
        timePassed();
        return true;
    }

    private Player findNextAvailablePlayer(int currentIndex) {
        for (int offset = 1; offset <= players.size(); offset++) {
            int nextIndex = (currentIndex + offset) % players.size();
            Player candidate = players.get(nextIndex).currentPlayer();

            if (candidate.energy() > 0) {
                return candidate;
            }
        }
        return null;
    }

    public void timePassed() {
        int dayPassed = this.time.addHours(1);
        if(dayPassed > 0){
            int seasonPassed = this.date.addDays(dayPassed);
            // todo : add page 22 conditions
            this.todayWeather = this.tomorrowWeather;
            randomizeTomorrowWeather();
            handlePlayersCoordinateInMorning();
            handleFaintedPlayers();
        }
    }

    public void randomizeTomorrowWeather() {
        Map<Weather, Double> probabilities = date.currentSeason().weatherProbabilities();

        double random = new Random().nextDouble();
        double cumulative = 0.0;

        for (Map.Entry<Weather, Double> entry : probabilities.entrySet()) {
            cumulative += entry.getValue();
            if (random <= cumulative) {
                this.tomorrowWeather = entry.getKey();
                return;
            }
        }
        this.tomorrowWeather = Weather.Sunny;
    }

    private void handleFaintedPlayers(){
        Player player;
        for(User user : players){
            player = user.currentPlayer();
            if(player.isFainted()){
                player.setFainted(false);
                player.setEnergy(150);
            }
        }
    }

    private void handlePlayersCoordinateInMorning(){
        Player player;
        for(User user : players){
            player = user.currentPlayer();
            if(!player.isFainted()){
                player.setCurrentX(player.originX());
                player.setCurrentY(player.originY());
            }
        }
    }

    public ArrayList<User> players() {
        return players;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public void setGameMap(GameMap map) {
        this.map = map;
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

    public User mainPlayer() {
        return mainPlayer;
    }

    public void setMainPlayer(User mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public Weather todayWeather() {
        return todayWeather;
    }

    public void setTodayWeather(Weather todayWeather) {
        this.todayWeather = todayWeather;
    }

    public Weather tomorrowWeather() {
        return tomorrowWeather;
    }

    public void setTomorrowWeather(Weather tomorrowWeather) {
        this.tomorrowWeather = tomorrowWeather;
    }
}
