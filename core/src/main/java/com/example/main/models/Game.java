package com.example.main.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.main.enums.design.NPCType;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.Growable;
import com.example.main.enums.items.TreeType;
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

    /**
     * Advances the game time by a specified number of minutes and triggers daily updates if a day passes.
     * @param minutes The number of minutes to advance.
     */
    public void advanceTimeByMinutes(int minutes) {
        int daysPassed = this.time.addMinutes(minutes);
        if (daysPassed > 0) {
            for (int i = 0; i < daysPassed; i++) {
                advanceDay();
            }
        }
    }

    /**
     * Contains all the logic that should be executed when a day passes.
     */
    public void advanceDay() {
        this.daysPassed++;
        this.date.addDays(1);
        this.todayWeather = this.tomorrowWeather;
        randomizeTomorrowWeather();
        handlePlayersCoordinateInMorning();
        handlePlayersEnergy();
        if (map != null) {
            map.generateRandomForagingSeeds();
            map.generatePlantsFromSeeds();
            map.regenerateQuarries();
        }
        updateTreesAndPlants();
        resetHarvestFlags();
        checkForLightning();
        crowsAttack();
        handleFardaei();
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
            // The timePassed() method is deprecated in favor of advanceTimeByMinutes
            // timePassed();
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
        if (map == null) return;
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
        if (this.getTomorrowWeather().equals(Weather.Stormy) && map != null) {
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

    private void handlePlayersEnergy(){
        Player player;
        for(User user : players){
            player = user.currentPlayer();
            if(player.isFainted()){
                player.setFainted(false);
                player.setEnergy(150);
            }
            else {
                player.setEnergy(200);
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

    public void resetHarvestFlags() {
        if (map == null) return;
        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                Tile tile = map.getTile(i, j);
                if (tile.getPlant() != null && tile.getPlant() instanceof Fruit) {
                    ((Fruit) tile.getPlant()).setHasBeenHarvestedToday(false);
                }
            }
        }
    }

    public void updateTreesAndPlants() {
        if (map == null) return;

        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                Tile tile = map.getTile(i, j);
                if (tile.getPlant() != null) {
                    Growable plant = tile.getPlant();
                    boolean wasWatered = plant.isWateredToday();

                    // --- Logic for removing dead plants ---
                    if (!wasWatered) {
                        if (plant instanceof Fruit fruit) {
                            fruit.incrementUnwateredDays();
                            if (fruit.getUnwateredDays() >= 2) {
                                tile.setPlant(null);
                                continue;
                            }
                        } else if (plant instanceof Crop crop) {
                            if (crop.isNotWateredForTwoDays()) {
                                tile.setPlant(null);
                                tile.setType(TileType.Shoveled);
                                continue;
                            } else {
                                crop.setNotWateredForTwoDays(true);
                            }
                        }
                    } else {
                        if (plant instanceof Fruit fruit) {
                            fruit.setUnwateredDays(0);
                        }
                        if (plant instanceof Crop crop) {
                            crop.setNotWateredForTwoDays(false);
                        }
                    }

                    // --- Growth Logic ---
                    if (!plant.isReadyToHarvest()) {
                        // Only increment days passed if watered (or if it's a wild tree that doesn't need water)
                        if (wasWatered || (plant instanceof Fruit && ((Fruit)plant).getTreeType().isForaging())) {
                            plant.setDayPassed(plant.getDayPassed() + 1);
                        }

                        int daysPassed = plant.getDayPassed();
                        int newStage = 1; // Start at stage 1
                        int daysRequiredForNextStage = 0;

                        List<Integer> stages = new ArrayList<>();
                        if (plant instanceof Fruit fruit) {
                            stages = fruit.getTreeType().getStages();
                        } else if (plant instanceof Crop crop && crop.getCropType() instanceof CropType) {
                            stages = ((CropType) crop.getCropType()).getStages();
                        }

                        // Calculate the current stage based on days passed
                        for (Integer stageDuration : stages) {
                            daysRequiredForNextStage += stageDuration;
                            if (daysPassed >= daysRequiredForNextStage) {
                                newStage++;
                            } else {
                                break; // Stop when we find the current stage
                            }
                        }
                        plant.setCurrentStage(newStage);
                    }

                    // Check for maturity
                    if (plant instanceof Fruit fruit) {
                        if (fruit.getDayPassed() >= fruit.getTreeType().getTotalHarvestTime() && fruit.getTreeType().getSeasons().contains(date.getCurrentSeason())) {
                            fruit.setReadyToHarvest(true);
                        } else {
                            fruit.setReadyToHarvest(false);
                        }
                    } else if (plant instanceof Crop crop && crop.getCropType() instanceof CropType) {
                        if (crop.getDayPassed() >= ((CropType) crop.getCropType()).getTotalHarvestTime()) {
                            crop.setReadyToHarvest(true);
                        }
                    }

                    plant.setWateredToday(false);
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
}
