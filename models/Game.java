package models;

import enums.design.NPCType;
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
    private final ArrayList<Friendship> friendships = new ArrayList<>();
    private final ArrayList<NPC> NPCs = new ArrayList<>();

    public Game(ArrayList<User> players) {
        this.time = new Time();
        this.date = new Date();
        this.players = players;
        this.currentPlayer = players.getFirst().getPlayer();
        this.todayWeather = Weather.Sunny;
        this.tomorrowWeather = Weather.Rainy;

        this.friendships.add(new Friendship(players.get(0), players.get(1)));
        this.friendships.add(new Friendship(players.get(0), players.get(2)));
        this.friendships.add(new Friendship(players.get(0), players.get(3)));
        this.friendships.add(new Friendship(players.get(1), players.get(2)));
        this.friendships.add(new Friendship(players.get(1), players.get(3)));
        this.friendships.add(new Friendship(players.get(2), players.get(3)));

        this.NPCs.add(new NPC(NPCType.Abigail, players));
        this.NPCs.add(new NPC(NPCType.Harvey, players));
        this.NPCs.add(new NPC(NPCType.Lia, players));
        this.NPCs.add(new NPC(NPCType.Robin, players));
        this.NPCs.add(new NPC(NPCType.Sebastian, players));
    }

    public ArrayList<NPC> getNPCs() {
        return NPCs;
    }

    public NPC getNPCByName(String name) {
        for (NPC npc : NPCs) {
            if (npc.getType().name().equalsIgnoreCase(name)) {
                return npc;
            }
        }
        return null;
    }

    public User getUserByUsername(String username) {
        for (User user : this.players) {
            if (user.getUsername().equals(username)) return user;
        }

        return null;
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

            if (candidate.getEnergy() > 0) {
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

    public ArrayList<User> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    public void setGameMap(GameMap map) {
        this.map = map;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public User getMainPlayer() {
        return mainPlayer;
    }

    public void setMainPlayer(User mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public Weather getTodayWeather() {
        return todayWeather;
    }

    public void setTodayWeather(Weather todayWeather) {
        this.todayWeather = todayWeather;
    }

    public Weather getTomorrowWeather() {
        return tomorrowWeather;
    }

    public void setTomorrowWeather(Weather tomorrowWeather) {
        this.tomorrowWeather = tomorrowWeather;
    }
}
