package com.example.main.models;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.example.main.enums.design.NPCType;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.items.CropType;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fruit;
import com.example.main.models.item.Good;
import com.example.main.models.item.Item;

public class Game {
    private int daysPassed = 0;
    private ArrayList<User> players;
    private User mainPlayer;
    private GameMap map;
    private Player currentPlayer;
    private User currentUser;
    private Date date;
    private Time time;
    private Weather todayWeather;
    private Weather tomorrowWeather;
    private final ArrayList<Friendship> friendships = new ArrayList<>();
    private final ArrayList<NPC> NPCs = new ArrayList<>();
    private int switchCounter = 0;

    public Game(ArrayList<User> players) {
        this.time = new Time();
        this.date = new Date();
        this.players = players;
        this.currentPlayer = players.getFirst().getPlayer();
        this.currentUser = players.getFirst(); // Initialize currentUser to match currentPlayer
        this.todayWeather = Weather.Sunny;
        this.tomorrowWeather = Weather.Rainy;

        this.friendships.add(new Friendship(players.get(0).getPlayer(), players.get(1).getPlayer()));
        this.friendships.add(new Friendship(players.get(0).getPlayer(), players.get(2).getPlayer()));
        this.friendships.add(new Friendship(players.get(0).getPlayer(), players.get(3).getPlayer()));
        this.friendships.add(new Friendship(players.get(1).getPlayer(), players.get(2).getPlayer()));
        this.friendships.add(new Friendship(players.get(1).getPlayer(), players.get(3).getPlayer()));
        this.friendships.add(new Friendship(players.get(2).getPlayer(), players.get(3).getPlayer()));

        ArrayList<Player> realPlayers = new ArrayList<>();
        for (User user : this.players) {
            realPlayers.add(user.getPlayer());
        }

        this.NPCs.add(new NPC(NPCType.Abigail, realPlayers));
        this.NPCs.add(new NPC(NPCType.Harvey, realPlayers));
        this.NPCs.add(new NPC(NPCType.Lia, realPlayers));
        this.NPCs.add(new NPC(NPCType.Robin, realPlayers));
        this.NPCs.add(new NPC(NPCType.Sebastian, realPlayers));
    }

    public int getDaysPassed() {
        return daysPassed;
    }

    public ArrayList<Friendship> getFriendshipsByPlayer(Player player) {
        ArrayList<Friendship> friendships = new ArrayList<>();
        for (Friendship friendship : this.friendships) {
            if (friendship.getPlayers().contains(player)) {
                friendships.add(friendship);
            }
        }

        return friendships;
    }

    public Friendship getFriendshipByPlayers(Player player1, Player player2) {
        for (Friendship friendship : friendships) {
            if (friendship.getPlayers().contains(player1) && friendship.getPlayers().contains(player2)) {
                return friendship;
            }
        }
        
        return null;
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
        int currentIndex = players.indexOf(currentUser);
        if (currentIndex == -1){
            return false;
        }

        Player nextPlayer = findNextAvailablePlayer(currentIndex);
        User nextUser = findNextAvailableUser(currentIndex);

        if (nextPlayer == null) {
            return false;
        }
        if(nextUser == null){
            return false;
        }

        setCurrentPlayer(nextPlayer);
        setCurrentUser(nextUser);
        this.switchCounter++;
        if(this.switchCounter >= 4){
            this.switchCounter = 0;
            timePassed();
        }
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

    public void crowsAttack() {
        Random rand = new Random();
        if (rand.nextInt() == 0) {
            int numPlants = 0;
            Tile[][] tiles = this.map.getTiles();
            ArrayList<Tile> targetTiles = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 30; j++) {
                    if (tiles[i][j].getPlant() != null) {
                        numPlants++;
                        targetTiles.add(tiles[i][j]);
                    }
                }
            }

            for (int i = 0; i < numPlants / 16; i++) {
                targetTiles.get(rand.nextInt(targetTiles.size())).setPlant(null);
            }
            
            numPlants = 0;
            targetTiles = new ArrayList<>();

            for (int i = 60; i < 90; i++) {
                for (int j = 0; j < 30; j++) {
                    if (tiles[i][j].getPlant() != null) {
                        numPlants++;
                        targetTiles.add(tiles[i][j]);
                    }
                }
            }

            for (int i = 0; i < numPlants / 16; i++) {
                targetTiles.get(rand.nextInt(targetTiles.size())).setPlant(null);
            }

            numPlants = 0;
            targetTiles = new ArrayList<>();

            for (int i = 0; i < 30; i++) {
                for (int j = 30; j < 60; j++) {
                    if (tiles[i][j].getPlant() != null) {
                        numPlants++;
                        targetTiles.add(tiles[i][j]);
                    }
                }
            }

            for (int i = 0; i < numPlants / 16; i++) {
                targetTiles.get(rand.nextInt(targetTiles.size())).setPlant(null);
            }

            numPlants = 0;
            targetTiles = new ArrayList<>();

            for (int i = 60; i < 90; i++) {
                for (int j = 30; j < 60; j++) {
                    if (tiles[i][j].getPlant() != null) {
                        numPlants++;
                        targetTiles.add(tiles[i][j]);
                    }
                }
            }

            for (int i = 0; i < numPlants / 16; i++) {
                targetTiles.get(rand.nextInt(targetTiles.size())).setPlant(null);
            }
        }
    }

    public void checkForLightning() {
        if (this.getTomorrowWeather().equals(Weather.Stormy)) {
            Random rand = new Random();
            if (rand.nextInt(5) == 0) {
                this.getMap().lightning(0);
                this.getMap().lightning(1);
                this.getMap().lightning(2);
                this.getMap().lightning(3);
            }
        }
    }

    private User findNextAvailableUser(int currentIndex) {
        for (int offset = 1; offset <= players.size(); offset++) {
            int nextIndex = (currentIndex + offset) % players.size();
            Player candidate = players.get(nextIndex).currentPlayer();

            if (candidate.getEnergy() > 0) {
                System.out.println(candidate.getEnergy());
                return players.get(nextIndex);
            }
        }
        return null;
    }

    public void handleFardaei() {
        for (User user : this.players) {
            user.getPlayer().getBankAccount().depositFardaei();
        }
    }

    public void timePassed() {
        int dayPassed = this.time.addHours(1);
        updateArtisanProduct();
        if(dayPassed > 0){
            this.daysPassed++;
            int seasonPassed = this.date.addDays(dayPassed);
            // todo : add thor
            this.todayWeather = this.tomorrowWeather;
            randomizeTomorrowWeather();
            handlePlayersCoordinateInMorning();
            handleFaintedPlayers();
            map.generateRandomForagingSeeds();
            map.generatePlantsFromSeeds();
            eraseCrops();
            updateCrops();
            checkForLightning();
            crowAttack();
            handleFardaei();
        }
    }

    public void randomizeTomorrowWeather() {
        Map<Weather, Double> probabilities = date.getCurrentSeason().weatherProbabilities();

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

    public User getUrrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void eraseCrops(){
        for(int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                Tile tile = map.getTile(i, j);
                if(tile.getPlant().isNotWateredForTwoDays()){
                    tile.setPlant(null);
                }
                if(!tile.getPlant().isFertilizedToday()){
                    tile.setPlant(null);
                }
            }
        }
    }

    public void updateCrops(){
        for(int i = 0; i < 90; i++){
            for(int j = 0; j < 60; j++){
                Tile tile = map.getTile(i, j);
                if(tile.getType().equals(TileType.Planted)){
                    if(tile.getPlant() == null){
                        System.out.println("error in crops");
                    }
                    else{
                        if(tile.getPlant() instanceof Crop plant){
                            if(plant.getCropType() instanceof CropType type){
                                if(plant.getDayPassed() >= type.getTotalHarvestTime()){
                                    plant.setReadyToHarvest(true);
                                    if(type.isOneTimeHarvest()){
                                        plant.setCanBeHarvestAgain(false);
                                    }
                                }
                                else{
                                    plant.setDayPassed(plant.getDayPassed() + 1);
                                    plant.setWateredToday(false);
                                }
                            }
//                            else {
//                                System.out.println("error 1");
//                            }
                        }
//                        else{
//                            System.out.println("error 2");
//                        }
                    }
                }
                else if(tile.getType().equals(TileType.Tree)){
                    if(tile.getPlant() == null){
                        System.out.println("error in tree");
                    }
                    else{
                        if(tile.getPlant() instanceof Fruit fruit){
//                            if(fruit.getDayPassed() >= tile.getTree().getType().getTotalHarvestTime()){
//                                fruit.setReadyToHarvest(true);
//                            }
//                            else{
//                                fruit.setDayPassed(fruit.getDayPassed() + 1);
//                                fruit.setWateredToday(false);
//                            }
                        }
//                        else {
//                            System.out.println("error 3");
//                        }
                    }
                }
            }
        }
    }

    private void updateArtisanProduct(){
        for(Item item : this.currentPlayer.getInventory().getItems()){
            if(item instanceof Good good){
                if(good.getTimePassed() >= good.getProductType().getProcessingTime()){
                    good.setReadyToUSe(true);
                }
                else{
                    good.setTimePassed(good.getTimePassed() + 1);
                }
            }
        }
    }

    private void crowAttack(){

    }
}
