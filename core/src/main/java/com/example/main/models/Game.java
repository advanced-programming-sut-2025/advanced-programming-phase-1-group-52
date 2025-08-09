package com.example.main.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.main.enums.design.NPCType;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.Growable;
import com.example.main.enums.items.ItemType;
import com.example.main.models.building.Housing;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fruit;
import com.example.main.models.item.PurchasedAnimal;

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
    private boolean crowAttackHappened = false;
    private transient User localPlayerUser;

    // --- THIS IS THE CORRECTED CONSTRUCTOR ---
    public Game(ArrayList<User> players) {
        this.time = new Time();
        this.date = new Date();
        this.players = players;

        // Safely set the current player and user to the first player in the list.
        if (!players.isEmpty()) {
            this.currentPlayer = players.get(0).getPlayer();
            this.currentUser = players.get(0);
        }

        this.todayWeather = Weather.Sunny;
        this.tomorrowWeather = Weather.Rainy;

        // --- DYNAMIC FRIENDSHIP CREATION ---
        // This nested loop creates a friendship between every unique pair of players,
        // regardless of whether there are 2, 3, or 4 players. This is now safe.
        for (int i = 0; i < players.size(); i++) {
            for (int j = i + 1; j < players.size(); j++) {
                User user1 = players.get(i);
                User user2 = players.get(j);
                if (user1 != null && user2 != null && user1.getPlayer() != null && user2.getPlayer() != null) {
                    this.friendships.add(new Friendship(user1.getPlayer(), user2.getPlayer()));
                }
            }
        }
        // --- END OF CORRECTION ---

        ArrayList<Player> realPlayers = new ArrayList<>();
        for (User user : this.players) {
            // Ensure we don't add null players if the user object is just a placeholder
            if (user != null && user.getPlayer() != null) {
                realPlayers.add(user.getPlayer());
            }
        }

        // The rest of the constructor is safe.
        this.NPCs.add(new NPC(NPCType.Abigail, realPlayers));
        this.NPCs.add(new NPC(NPCType.Harvey, realPlayers));
        this.NPCs.add(new NPC(NPCType.Lia, realPlayers));
        this.NPCs.add(new NPC(NPCType.Robin, realPlayers));
        this.NPCs.add(new NPC(NPCType.Sebastian, realPlayers));
    }


    public Game(ArrayList<User> users, Map<String, Player> playerMap) {
        this(users); // This calls the original constructor to do all the basic setup.

        // Now, iterate through the game's list of users and assign the correct Player object.
        for (User user : this.players) {
            if (user != null && playerMap.containsKey(user.getUsername())) {
                user.setCurrentPlayer(playerMap.get(user.getUsername()));
            }
        }
    }

    // --- ALL OTHER METHODS IN YOUR CLASS REMAIN EXACTLY THE SAME ---
    // (advanceTimeByMinutes, advanceDay, getFriendshipsByPlayer, etc.)
    public void advanceTimeByMinutes(int minutes) {
        int tensOfMinutesPassed = (this.time.getMinute() + minutes) / 10 - this.time.getMinute() / 10;
        int daysPassed = this.time.addMinutes(minutes);

        if (tensOfMinutesPassed > 0) {
            for (int i = 0; i < tensOfMinutesPassed; i++) {
                updatePlayerBuffs();
                updatePlacedMachines();
            }
        }

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
        checkForGiantCrops();
        resetHarvestFlags();
        checkForLightning();
        crowsAttack();
        handleFardaei();
        handleAnimalProducts();
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
        if (rand.nextInt(5) == 0) {
            crowAttackHappened = true;
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
            if (user == null || user.getPlayer() == null || user.getPlayer().getBankAccount() == null) {
                continue;
            }
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
            if (user == null) continue;
            player = user.currentPlayer();
            if (player == null) continue;
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
            if (user == null) continue;
            player = user.currentPlayer();
            if (player == null) continue;
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

    // In main/models/Game.java

    public void updateTreesAndPlants() {
        if (map == null) return;

        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                Tile tile = map.getTile(i, j);
                if (tile.getPlant() != null) {
                    Growable plant = tile.getPlant();

                    // --- Handle Crops (Both Farm and Wild Forageables) ---
                    if (plant instanceof Crop crop) {
                        ItemType type = crop.getCropType();

                        // THIS IS THE CRITICAL CHECK: We only process growth for actual farm crops.
                        if (type instanceof CropType cropType) {
                            boolean wasWatered = plant.isWateredToday();

                            // 1. Logic for removing dead farm crops
                            if (!wasWatered) {
                                if (crop.isNotWateredForTwoDays()) {
                                    tile.setPlant(null);
                                    tile.setType(TileType.Shoveled);
                                    continue; // Skip to the next tile
                                } else {
                                    crop.setNotWateredForTwoDays(true);
                                }
                            } else {
                                crop.setNotWateredForTwoDays(false);
                            }

                            // 2. Growth Logic (only runs if watered)
                            if (!plant.isReadyToHarvest() && wasWatered) {
                                plant.setDayPassed(plant.getDayPassed() + 1);

                                int daysPassed = plant.getDayPassed();
                                int newStage = 1;
                                int daysRequiredForNextStage = 0;
                                List<Integer> stages = cropType.getStages();

                                for (Integer stageDuration : stages) {
                                    daysRequiredForNextStage += stageDuration;
                                    if (daysPassed >= daysRequiredForNextStage) {
                                        newStage++;
                                    } else {
                                        break;
                                    }
                                }
                                plant.setCurrentStage(newStage);
                            }

                            // 3. Maturity Check
                            if (plant.getDayPassed() >= cropType.getTotalHarvestTime()) {
                                plant.setReadyToHarvest(true);
                            }
                        }
                        // If 'type' is a ForagingCropType, all the logic above is skipped.

                    }
                    // --- Handle Fruit Trees ---
                    else if (plant instanceof Fruit fruit) {
                        boolean wasWatered = fruit.isWateredToday();
                        if (!wasWatered && !fruit.getTreeType().isForaging()) {
                            fruit.incrementUnwateredDays();
                            if (fruit.getUnwateredDays() >= 2) {
                                tile.setPlant(null);
                                continue;
                            }
                        } else {
                            fruit.setUnwateredDays(0);
                        }

                        if (!fruit.isReadyToHarvest()) {
                            if (wasWatered || fruit.getTreeType().isForaging()) {
                                fruit.setDayPassed(fruit.getDayPassed() + 1);
                            }
                            // Update fruit tree stage logic here if needed
                        }

                        if (fruit.getDayPassed() >= fruit.getTreeType().getTotalHarvestTime() && fruit.getTreeType().getSeasons().contains(date.getCurrentSeason())) {
                            fruit.setReadyToHarvest(true);
                        } else {
                            fruit.setReadyToHarvest(false);
                        }
                    }

                    // Reset watered status for ALL plants at the end of the day
                    plant.setWateredToday(false);
                }
            }
        }
    }

    public void updatePlacedMachines() {
        if (map == null) return;
        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                Tile tile = map.getTile(i, j);
                if (tile != null && tile.getPlacedMachine() != null) {
                    tile.getPlacedMachine().updateProgress();
                }
            }
        }
    }


    public void handleAnimalProducts() {
        for (User user : this.players) {
            if (user == null || user.getPlayer() == null) continue;
            Player player = user.getPlayer();
            if (player.getHousings() == null) continue;
            for (Housing housing : player.getHousings()) {
                for (PurchasedAnimal animal : housing.getOccupants()) {
                    if (animal.isFull()) {
                        animal.setFull(false);
                        animal.setWasFull(true);
                    }
                    else {
                        animal.setWasFull(false);
                    }

                    animal.setCollected(false);
                }
            }
        }
    }

    public boolean isCrowAttackHappened() {
        return crowAttackHappened;
    }

    public void resetCrowAttackFlag() {
        this.crowAttackHappened = false;
    }

    public void updatePlayerBuffs() {
        for (User user : players) {
            Player player = user.getPlayer();
            if (player == null) continue;
            boolean hasMaxEnergyBuff = player.getActiveBuffs().stream()
                .anyMatch(buff -> buff.getType() == ActiveBuff.BuffType.MAX_ENERGY);
            if (hasMaxEnergyBuff) {
                player.setEnergy(200);
            }
            Iterator<ActiveBuff> iterator = player.getActiveBuffs().iterator();
            while (iterator.hasNext()) {
                ActiveBuff buff = iterator.next();
                buff.decrementDuration();
                if (buff.isExpired()) {
                    if (buff.getType() == ActiveBuff.BuffType.SKILL) {
                        player.getSkillData(buff.getSkill()).removeBuff();
                    }
                    iterator.remove();
                }
            }
        }
    }

    public void checkForGiantCrops() {
        if (map == null) return;
        Tile[][] tiles = map.getTiles();
        Random rand = new Random();

        // Iterate through all possible top-left corners of a 2x2 square
        for (int x = 0; x < 89; x++) {
            for (int y = 0; y < 59; y++) {

                // 1. Check if a 2x2 square of the same giant-able crop exists
                Tile topLeft = tiles[x][y];
                if (topLeft == null || !(topLeft.getPlant() instanceof Crop)) continue;

                Crop firstCrop = (Crop) topLeft.getPlant();
                ItemType firstType = firstCrop.getCropType();
                if (!(firstType instanceof CropType)) {
                    continue; // Only actual farm crops can become giant
                }
                CropType cropType = (CropType) firstType;

                if (!cropType.canBecomeGiant() || !firstCrop.isReadyToHarvest()) continue;

                Tile topRight = tiles[x + 1][y];
                Tile bottomLeft = tiles[x][y + 1];
                Tile bottomRight = tiles[x + 1][y + 1];

                if (topRight != null && bottomLeft != null && bottomRight != null &&
                    topRight.getPlant() instanceof Crop && ((Crop) topRight.getPlant()).getCropType() instanceof CropType && ((CropType) ((Crop) topRight.getPlant()).getCropType()) == cropType && ((Crop) topRight.getPlant()).isReadyToHarvest() &&
                    bottomLeft.getPlant() instanceof Crop && ((Crop) bottomLeft.getPlant()).getCropType() instanceof CropType && ((CropType) ((Crop) bottomLeft.getPlant()).getCropType()) == cropType && ((Crop) bottomLeft.getPlant()).isReadyToHarvest() &&
                    bottomRight.getPlant() instanceof Crop && ((Crop) bottomRight.getPlant()).getCropType() instanceof CropType && ((CropType) ((Crop) bottomRight.getPlant()).getCropType()) == cropType && ((Crop) bottomRight.getPlant()).isReadyToHarvest()) {

                    // 2. 5% chance to merge into a giant crop
                    if (rand.nextInt(100) < 5) {
                        // 3. Mark all four tiles as part of a giant crop, pointing to the top-left tile
                        topLeft.setPartOfGiantCrop(true);
                        topLeft.setGiantCropRootX(x);
                        topLeft.setGiantCropRootY(y);

                        topRight.setPartOfGiantCrop(true);
                        topRight.setGiantCropRootX(x);
                        topRight.setGiantCropRootY(y);

                        bottomLeft.setPartOfGiantCrop(true);
                        bottomLeft.setGiantCropRootX(x);
                        bottomLeft.setGiantCropRootY(y);

                        bottomRight.setPartOfGiantCrop(true);
                        bottomRight.setGiantCropRootX(x);
                        bottomRight.setGiantCropRootY(y);
                    }
                }
            }
        }
    }
    public synchronized void removePlayer(String username) {
        if (username == null) {
            return; // or throw an exception
        }
        // Using removeIf for a cleaner and safer removal from the list
        players.removeIf(user -> user != null && username.equals(user.getUsername()));
    }

    public User getLocalPlayerUser() {
        return localPlayerUser;
    }

    public void setLocalPlayerUser(User localPlayerUser) {
        this.localPlayerUser = localPlayerUser;
    }
}
