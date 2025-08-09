package com.example.main.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.example.main.enums.Menu;
import com.example.main.enums.design.Direction;
import com.example.main.enums.design.FarmThemes;
import com.example.main.enums.design.NPCType;
import com.example.main.enums.design.Season;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.items.AnimalProductType;
import com.example.main.enums.items.ArtisanProductType;
import com.example.main.enums.items.CookingRecipeType;
import com.example.main.enums.items.CraftingMachineType;
import com.example.main.enums.items.CraftingRecipes;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.FishType;
import com.example.main.enums.items.FoodType;
import com.example.main.enums.items.ForagingCropType;
import com.example.main.enums.items.ForagingSeedType;
import com.example.main.enums.items.FruitType;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.MaterialType;
import com.example.main.enums.items.MineralType;
import com.example.main.enums.items.ToolType;
import com.example.main.enums.items.TreeType;
import com.example.main.enums.player.Skills;
import com.example.main.enums.regex.GameMenuCommands;
import com.example.main.enums.regex.NPCDialogs;
import com.example.main.events.EventBus;
import com.example.main.events.PlayerDisconnectedEvent;
import com.example.main.models.ActiveBuff;
import com.example.main.models.App;
import com.example.main.models.Date;
import com.example.main.models.FishingMinigame;
import com.example.main.models.Friendship;
import com.example.main.models.Game;
import com.example.main.models.GameMap;
import com.example.main.models.Gift;
import com.example.main.models.NPC;
import com.example.main.models.Player;
import com.example.main.models.Quest;
import com.example.main.models.Result;
import com.example.main.models.Talk;
import com.example.main.models.Tile;
import com.example.main.models.Time;
import com.example.main.models.Tree;
import com.example.main.models.User;
import com.example.main.models.building.House;
import com.example.main.models.building.Housing;
import com.example.main.models.item.AnimalProduct;
import com.example.main.models.item.CookingRecipe;
import com.example.main.models.item.CraftingMachine;
import com.example.main.models.item.CraftingRecipe;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fish;
import com.example.main.models.item.Food;
import com.example.main.models.item.Fruit;
import com.example.main.models.item.Good;
import com.example.main.models.item.Item;
import com.example.main.models.item.ItemFactory;
import com.example.main.models.item.Material;
import com.example.main.models.item.Mineral;
import com.example.main.models.item.PlacedMachine;
import com.example.main.models.item.PurchasedAnimal;
import com.example.main.models.item.Seed;
import com.example.main.models.item.Tool;
import com.example.main.models.item.TrashCan;
import com.example.main.models.item.WateringCan;


public class GameMenuController {
    private Game game;
    private GameMap map;
    private Food lastCookedFood = null;
    private FishingMinigame activeMinigame = null;

    public GameMenuController() {
        subscribeToEvents();
    }

    private void subscribeToEvents() {
        EventBus.getInstance().subscribe(PlayerDisconnectedEvent.class, this::onPlayerDisconnected);
    }

    /**
     * This method is called by the EventBus when a GameMapSyncEvent is published.
     */
    private void onPlayerDisconnected(PlayerDisconnectedEvent event) {
        Gdx.app.postRunnable(() -> {
            removePlayer(event.getUsername());
        });
    }

    public void setGameMap(GameMap newMap) {
        if (game != null) {
            game.setGameMap(newMap);
            System.out.println("[CONTROLLER] Game map has been successfully synchronized.");
        } else {
            System.err.println("[CONTROLLER] Game is null. Cannot sync map.");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public Result startNewGame(String input) {
        List<String> usernames;
        try {
            usernames = validateGameCommand(input);
        } catch (IllegalArgumentException e) {
            return new Result(false, e.getMessage());
        }

        User user1,user2,user3;

        if ((user1 = findUser(usernames.get(0))) == null || (user2 = findUser(usernames.get(1))) == null || (user3 = findUser(usernames.get(2))) == null) {
            return new Result(false, "User not found, please try again");
        }

        if (!isUserAvailable(user1) || !isUserAvailable(user2) || !isUserAvailable(user3)) {
            return new Result(false, "Users are not available");
        }
        ArrayList<User> players = new ArrayList<>();
        User loggedInUser = App.getInstance().getCurrentUser();

        Player player1 = new Player(loggedInUser.getUsername(), loggedInUser.getGender());
        player1.setOriginX(4);
        player1.setOriginY(4);
        player1.setCurrentX(4);
        player1.setCurrentY(4);
        player1.setTrashCanX(9);
        player1.setTrashCanY(1);
        loggedInUser.setCurrentPlayer(player1);
        players.add(loggedInUser);

        Player player2 = new Player(user1.getUsername(), user1.getGender());
        player2.setOriginX(84);
        player2.setOriginY(4);
        player2.setCurrentX(84);
        player2.setCurrentY(4);
        player2.setTrashCanX(80);
        player2.setTrashCanY(1);
        user1.setCurrentPlayer(player2);
        players.add(user1);

        Player player3 = new Player(user2.getUsername(), user2.getGender());
        player3.setOriginX(4);
        player3.setOriginY(34);
        player3.setCurrentX(4);
        player3.setCurrentY(34);
        player3.setTrashCanX(9);
        player3.setTrashCanY(31);
        user2.setCurrentPlayer(player3);
        players.add(user2);

        Player player4 = new Player(user3.getUsername(), user3.getGender());
        player4.setOriginX(84);
        player4.setOriginY(34);
        player4.setCurrentX(84);
        player4.setCurrentY(34);
        player4.setTrashCanX(80);
        player4.setTrashCanY(31);
        user3.setCurrentPlayer(player4);
        players.add(user3);

        Game newGame = new Game(players);
        newGame.setMainPlayer(loggedInUser);
        newGame.setCurrentPlayer(loggedInUser.currentPlayer());
        newGame.setCurrentUser(loggedInUser);
        App.getInstance().addGame(newGame);
        App.getInstance().setCurrentGame(newGame);
        this.setGame(newGame);
        return new Result(true, "Now Choose your map!:" + "\n1.Neutral\n2.Miner\n3.Fisher" + "choose map for users in order:");
    }

    public Result mapSelector(String user1FarmStr, String user2FarmStr, String user3FarmStr, String user4FarmStr) {
        int user1Farm, user2Farm, user3Farm, user4Farm;
        try {
            user1Farm = Integer.parseInt(user1FarmStr);
            user2Farm = Integer.parseInt(user2FarmStr);
            user3Farm = Integer.parseInt(user3FarmStr);
            user4Farm = Integer.parseInt(user4FarmStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid map id");
        }

        if (user1Farm < 1 || user1Farm > 3) {
            return new Result(false, "Invalid map id for user1");
        }
        if (user2Farm < 1 || user2Farm > 3) {
            return new Result(false, "Invalid map id for user2");
        }
        if (user3Farm < 1 || user3Farm > 3) {
            return new Result(false, "Invalid map id for user3");
        }
        if (user4Farm < 1 || user4Farm > 3) {
            return new Result(false, "Invalid map id for user4");
        }

        Game game = App.getInstance().getCurrentGame();
        ArrayList<FarmThemes> farmThemes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0 -> farmThemes.add(FarmThemes.values()[user1Farm - 1]);
                case 1 -> farmThemes.add(FarmThemes.values()[user2Farm - 1]);
                case 2 -> farmThemes.add(FarmThemes.values()[user3Farm - 1]);
                default -> farmThemes.add(FarmThemes.values()[user4Farm - 1]);
            }
        }

        GameMap map = new GameMap(game.getPlayers(), farmThemes);
        game.setGameMap(map);
        this.setMap(map);
        return new Result(true, "Map generated!");
    }

    public Result loadMap() {
        if(App.getInstance().getCurrentUser().userGame() == null) {
            return new Result(false, "You are not in a game");
        }
        // todo : load game
        return new Result(true, "Your last game is loaded");
    }

    public Result exitGame() {
        User loggedInUser = App.getInstance().getCurrentUser();
        if(game.getMainPlayer().equals(loggedInUser)){
            for(User user : App.getInstance().getCurrentGame().getPlayers()){
                user.setHighScore(user.currentPlayer().getBankAccount().getBalance());
                user.addNumPlayed();
            }
            App.getInstance().setCurrentMenu(Menu.MainMenu);
            return new Result(true, "you are in main menu.");
        }
        else{
            return new Result(false, "You are not the creator of this game");
        }
    }

    public Result gotoMainMenu(){
        App.getInstance().setCurrentMenu(Menu.MainMenu);
        return new Result(true, "You are in main menu now!");
    }

    public Result terminateGame() {
        return new Result(true, "Game terminated");
    }

    public FishingMinigame getActiveMinigame() {
        return activeMinigame;
    }

    public Result switchTurn() {
        Game game = App.getInstance().getCurrentGame();
        boolean isPlayerAvailable = game.switchCurrentPlayer();
        if(isPlayerAvailable){
            game.getCurrentPlayer().showNotifs();
            return new Result(true, "Game switched to " + game.getCurrentPlayer().getUsername() + " ");
        }
        return new Result(false, "you can not switch to other players");
    }

    public Result showTime() {
        return new Result(true, "It's " + game.getTime().getHour() + " O'clock");
    }

    public Result showDate() {
        return new Result(true,"Season: " + game.getDate().getCurrentSeason().name() +
            "\nDay: " + game.getDate().getCurrentDay());
    }

    public Result showDateAndTime() {
        return new Result(true, showTime().Message() + "\n" + showDate().Message());
    }

    public Result showDayOfWeek() {
        return new Result(true,"It's " + game.getDate().getCurrentWeekday().name());
    }

    public Result changeTime(String hoursStr) {
        int hours = Integer.parseInt(hoursStr);
        if (hours <= 0) {
            return new Result(false, "Hours must be positive");
        }
        Time time = game.getTime();
        Date date = game.getDate();

        int originalHour = time.getHour();
        int daysPassed = time.addHours(hours);


        if (daysPassed > 0) {
            game.advanceDay();
        }

        return new Result(true, "time changed!");
    }

    public Result changeDate(String daysStr) {
        int days;
        try {
            days = Integer.parseInt(daysStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid number format for days.");
        }
        return changeDate(days); // Call the other method
    }

    // This is the main logic, now corrected
    public Result changeDate(int days) {
        if (days <= 0) {
            return new Result(false, "Days must be positive");
        }

        for (int i = 0; i < days; i++) {
            game.advanceDay(); // Use the centralized method
        }

        return new Result(true, "Date advanced by " + days + " day(s)!");
    }

    public Result showSeason() {
        return new Result(true,game.getDate().getCurrentSeason().name());
    }

    public Result lightningHandling() {
        return new Result(true, "Lightning handling");
    }

    public Result cheatLightning(String xString, String yString) {
        int x = Integer.parseInt(xString);
        int y = Integer.parseInt(yString);
        Tile tile = map.getTile(x, y);
        if (tile.getType().equals(TileType.Tree)) {
            tile.setType(TileType.Stone);
            tile.setPlant(null);
            tile.setTree(null);
            tile.setSeed(null);
        }
        else if (tile.getType().equals(TileType.Planted)) {
            tile.setType(TileType.Earth);
            tile.setPlant(null);
            tile.setTree(null);
            tile.setSeed(null);
        }

        return new Result(true, "Lightning hit!");
    }

    public Result showWeather() {
        return new Result(true, game.getTodayWeather().name());
    }

    public Result showTomorrowWeather() {
        return new Result(true, game.getTomorrowWeather().name());
    }

    public Result changeTomorrowWeather(String weatherStr) {
        try {
            Weather weather = Weather.fromString(weatherStr);
            game.setTomorrowWeather(weather);
        } catch (IllegalArgumentException e) {
            return new Result(false, "Invalid weather string");
        }
        return new Result(true, "Tomorrow weather changed to " + game.getTomorrowWeather().name());
    }

    public Result buildGreenHouse(){
        Player player = game.getCurrentPlayer();
        if(player.getBankAccount().getBalance() < 1000){
            return new Result(false, "you don't have enough money");
        }
        Material wood;
        if((wood = (Material) player.getInventory().findItemByType(MaterialType.Wood)) == null){
            return new Result(false, "you don't have any wood");
        }
        if(wood.getNumber() < 500){
            return new Result(false, "you don't have enough wood");
        }
        player.getBankAccount().withdraw(1000);
        wood.setNumber(wood.getNumber() - 500);
        for (int i = 0; i < 90; i++){
            for(int j = 0; j < 60; j++){
                Tile tile = map.getTile(i,j);
                if(tile.getOwner() == null){
                    continue;
                }
                if(tile.getOwner().equals(player)){
                    if(tile.getType().equals(TileType.BrokenGreenHouse)){
                        tile.setType(TileType.GreenHouse);
                    }
                }
            }
        }
        return new Result(true, "Green house is built successfully");
    }

    public Result walk(String xString, String yString) {
        // todo : calculate closest way and place the player :P (R)
        // chaaaaaaaaaaaaaaaaaaaaaaaashm (S)
        int x = Integer.parseInt(xString);
        int y = Integer.parseInt(yString);

        if (x > 90 || x < 0 || y > 90 || y < 0) {
            return new Result(false, "You can not walk there!");
        }

        Player currentPlayer = game.getCurrentPlayer();
        ArrayList<Tile> path = null;

        try {
            path = map.findWalkPath(currentPlayer.currentX(), currentPlayer.currentY(), x, y);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Result(false, "You can not walk there mate!");
        }

        if (path == null) {
            return new Result(false, "You can not walk there mate!");
        }

        int sumTurns = 0;
        for (int i = 1; i < path.size(); i++) {
            if (path.get(i).getX() != path.get(i - 1).getX() || path.get(i).getY() != path.get(i - 1).getY()) {
                sumTurns++;
            }
        }

        int requiredEnergy = (path.size() + 10 * sumTurns) / 20;

        if (currentPlayer.getEnergy() < requiredEnergy) {
            currentPlayer.setFainted(true);
            currentPlayer.setEnergy(0);
            Tile reachedTile = path.get(currentPlayer.getEnergy() * 20 - 1);
            currentPlayer.setCurrentX(reachedTile.getX());
            currentPlayer.setCurrentY(reachedTile.getY());
            return new Result(false, "You are too idiot to walk that much! You will faint!");
        }

        currentPlayer.setCurrentX(x);
        currentPlayer.setCurrentY(y);
        currentPlayer.setEnergy(currentPlayer.getEnergy() - requiredEnergy);
        return new Result(true, "You made it!");
    }

    public Result cheatWalk(String xString, String yString) {
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.setCurrentX(Integer.parseInt(xString));
        currentPlayer.setCurrentY(Integer.parseInt(yString));
        return new Result(true, "partab shodam :o");
    }

    public Result printMap(String xString, String yString, String sizeString) {
        int x = Integer.parseInt(xString);
        int y = Integer.parseInt(yString);
        int size = Integer.parseInt(sizeString);

        return new Result(true, map.showMap(x, y, size));
    }

    public Result mapInfo() {
        return new Result(true, "earth : blank space\n" +
            "grass : ^\n" +
            "water, shoveled, planted, branch : ~\n" +
            "all kinds of stone : #\n" +
            "wall : /\n" +
            "door : %\n" +
            "house : @\n" +
            "greenhouse : G\n" +
            "broken green house : B\n" +
            "quarry : Q\n" +
            "tree : T\n" +
            "shop : $\n" +
            "NPC house : H");
    }

    public Result showPlayerCoordinates() {
        Player currentPlayer = game.getCurrentPlayer();
        return new Result(true, "player x: " + currentPlayer.currentX() + ", " + "player y: " + currentPlayer.currentY());
    }

    public Result energyShow() {
        int playerEnergy = game.getCurrentPlayer().getEnergy();
        return new Result(true, "Player energy: " + playerEnergy);
    }

    public Result meetNPC(String NPCName) {
        NPC npc = game.getNPCByName(NPCName);
        if (npc == null) {
            return new Result(false, "NPC not found!");
        }
        if (!isPlayerNearSomething(npc.getX(), npc.getY())) {
            return new Result(false, "You are not near the NPC!");
        }

        ArrayList<NPCDialogs> dialogs = Arrays.stream(NPCDialogs.values())
            .filter(dialog -> dialog.getLevel() == npc.getFriendShipLevelWith(game.getCurrentPlayer()))
            .filter(dialog -> dialog.getWeather() == null || dialog.getWeather() == game.getTodayWeather())
            .filter(dialog -> dialog.getSeason() == null || dialog.getSeason() == game.getDate().getCurrentSeason())
            .collect(Collectors.toCollection(ArrayList::new));

        Random rand = new Random();
        npc.getFriendShipWith(game.getCurrentPlayer()).addFriendshipPoints(20);
        return new Result(true, npc.getType().toString() + " says: \n" + dialogs.get(rand.nextInt(dialogs.size())).getDialog());
    }

    public Result giftNPC(String NPCName, String itemName) {
        NPC npc = game.getNPCByName(NPCName);

        if (npc == null) {
            return new Result(false, "NPC not found!");
        }
        if (!isPlayerNearSomething(npc.getX(), npc.getY())) {
            return new Result(false, "You are not near the NPC!");
        }
        Item item = null;
        for (Item i : game.getCurrentPlayer().getInventory().getItems()) {
            if (i.getName().equals(itemName)) {
                item = i;
                break;
            }
        }
        if (item == null) {
            return new Result(false, "Item not found in your inventory!");
        }
        if (item.isTool()) {
            return new Result(false, "You can not gift a tool!");
        }

        if (npc.getType().getFavorites().contains(item.getItemType())) {
            npc.getFriendShipWith(game.getCurrentPlayer()).addFriendshipPoints(200);
        }
        else {
            npc.getFriendShipWith(game.getCurrentPlayer()).addFriendshipPoints(50);
        }

        game.getCurrentPlayer().getInventory().removeItem(item.getClass(), 1);
        return new Result(true, "You gifted " + itemName + " to " + NPCName);
    }

    public Result showQuests() {
        StringBuilder stringBuilder = new StringBuilder();
        if (game.getNPCs() == null) {
            return new Result(false, "No NPCs available!");
        }

        for (NPC npc : game.getNPCs()) {
            if (npc == null || npc.getQuests() == null) {
                continue; // Skip null NPCs or NPCs with null quest maps
            }

            java.util.HashMap<Quest, Boolean> quests = npc.getQuests();
            List<Quest> questsList = new ArrayList<>(quests.keySet());

            // Check if there are any quests for this NPC
            if (questsList.isEmpty()) {
                continue;
            }

            // Always show first quest if it exists AND is not completed
            if (questsList.size() > 0 && questsList.get(0) != null) {
                Quest firstQuest = questsList.get(0);
                Boolean isCompleted = quests.get(firstQuest);
                if (isCompleted == null || !isCompleted) { // Show only if not completed
                    stringBuilder.append(firstQuest.toString());
                }
            }

            // Show second quest if friendship level >= 1, it exists, AND is not completed
            if (questsList.size() > 1 && questsList.get(1) != null &&
                npc.getFriendShipLevelWith(game.getCurrentPlayer()) != null &&
                npc.getFriendShipLevelWith(game.getCurrentPlayer()) >= 1) {
                Quest secondQuest = questsList.get(1);
                Boolean isCompleted = quests.get(secondQuest);
                if (isCompleted == null || !isCompleted) { // Show only if not completed
                    stringBuilder.append(secondQuest.toString());
                }
            }

            // Show third quest based on NPC type, days passed, AND not completed
            if (questsList.size() > 2 && questsList.get(2) != null) {
                boolean showThirdQuest = false;

                if (npc.getType() == NPCType.Abigail && game.getDaysPassed() >= 20) {
                    showThirdQuest = true;
                }
                else if (npc.getType() == NPCType.Harvey && game.getDaysPassed() >= 10) {
                    showThirdQuest = true;
                }
                else if (npc.getType() == NPCType.Lia && game.getDaysPassed() >= 15) {
                    showThirdQuest = true;
                }
                else if (npc.getType() == NPCType.Robin && game.getDaysPassed() >= 25) {
                    showThirdQuest = true;
                }
                else if (npc.getType() == NPCType.Sebastian && game.getDaysPassed() >= 15) {
                    showThirdQuest = true;
                }

                if (showThirdQuest) {
                    Quest thirdQuest = questsList.get(2);
                    Boolean isCompleted = quests.get(thirdQuest);
                    if (isCompleted == null || !isCompleted) { // Show only if not completed
                        stringBuilder.append(thirdQuest.toString());
                    }
                }
            }
        }

        return new Result(true, stringBuilder.toString());
    }

    // Helper method to check if a quest is visible/active (same logic as showQuests)
    private boolean isQuestVisible(Quest quest, NPC npc) {
        if (quest == null || npc == null) return false;

        HashMap<Quest, Boolean> quests = npc.getQuests();
        if (quests == null) return false;

        // Check if quest is completed - if so, it's not visible
        Boolean isCompleted = quests.get(quest);
        if (isCompleted != null && isCompleted) {
            return false; // Completed quests are never visible
        }

        List<Quest> questsList = new ArrayList<>(quests.keySet());
        int questIndex = questsList.indexOf(quest);

        if (questIndex == -1) return false; // Quest not found

        // First quest is always visible (if not completed)
        if (questIndex == 0) return true;

        // Second quest is visible if friendship level >= 1 (if not completed)
        if (questIndex == 1) {
            return npc.getFriendShipLevelWith(game.getCurrentPlayer()) != null &&
                npc.getFriendShipLevelWith(game.getCurrentPlayer()) >= 1;
        }

        // Third quest is visible based on NPC type and days passed (if not completed)
        if (questIndex == 2) {
            switch (npc.getType()) {
                case Abigail -> { return game.getDaysPassed() >= 20; }
                case Harvey -> { return game.getDaysPassed() >= 10; }
                case Lia -> { return game.getDaysPassed() >= 15; }
                case Robin -> { return game.getDaysPassed() >= 25; }
                case Sebastian -> { return game.getDaysPassed() >= 15; }
                default -> { return false; }
            }
        }

        return false; // Quests beyond index 2 are not visible
    }

    public Result finishQuest(String idString) {
        Player currentPlayer = game.getCurrentPlayer();
        int id;
        try {
            id = Integer.parseInt(idString);
        }
        catch (NumberFormatException e) {
            return new Result(false, "Invalid id format!");
        }

        // Add safety check for game NPCs
        if (game == null || game.getNPCs() == null) {
            return new Result(false, "Game not properly initialized!");
        }

        NPC npc = null;
        Quest quest = null;
        boolean isDone = false;

        try {
            for (NPC n : game.getNPCs()) {
                if (n == null || n.getQuests() == null) {
                    continue; // Skip null NPCs or NPCs with null quest maps
                }
                HashMap<Quest, Boolean> quests = n.getQuests();
                for (Quest q : quests.keySet()) {
                    if (q != null && q.getId() == id) {
                        npc = n;
                        quest = q;
                        isDone = quests.get(q);
                        break;
                    }
                }

                if (quest != null) break;
            }
        } catch (Exception e) {
            return new Result(false, "Error searching for quest: " + e.getMessage());
        }

        if (quest == null) {
            return new Result(false, "No quest found with this id!");
        }
        if (isDone) {
            return new Result(false, "This quest is already done!");
        }

        // Check if the quest is visible/active
        if (!isQuestVisible(quest, npc)) {
            return new Result(false, "This quest is not available yet! Check your friendship level or wait for more days to pass.");
        }

        if (quest.getDemand() == null) {
            return new Result(false, "Quest demand is invalid!");
        }

        // Use the more reliable findItemByType method instead of getItemByName
        Item item = currentPlayer.getInventory().findItemByType(quest.getDemand());
        if (item == null) {
            return new Result(false, "You don't have the demanding item: " + quest.getDemand().getName() + "!");
        }
        if (item.getNumber() < quest.getDemandAmount()) {
            return new Result(false, "You don't have enough items! Need " + quest.getDemandAmount() + ", but only have " + item.getNumber() + ".");
        }

        switch (quest.getDemand()) {
            case FoodType foodType -> {
                currentPlayer.getInventory().removeItem(Food.class, quest.getDemandAmount());
            }
            case MaterialType materialType -> {
                currentPlayer.getInventory().removeItem(Material.class, quest.getDemandAmount());
            }
            case MineralType mineralType -> {
                currentPlayer.getInventory().removeItem(Mineral.class, quest.getDemandAmount());
            }
            case FishType fishType -> {
                currentPlayer.getInventory().removeItem(Fish.class, quest.getDemandAmount());
            }
            case ArtisanProductType artisanProductType -> {
                currentPlayer.getInventory().removeItem(Good.class, quest.getDemandAmount());
            }
            default -> {
                return new Result(false, "Invalid Item!");
            }
        }

        if (quest.getReward() == null) {
            if (npc.getFriendShipWith(currentPlayer) != null) {
                npc.getFriendShipWith(currentPlayer).upgradeLevel();
            }
        }
        else {
            switch (quest.getReward()) {
                case MaterialType materialType -> {
                    if (materialType != null) {
                        Material material = new Material(materialType, quest.getRewardAmount());
                        currentPlayer.getInventory().addItem(material);
                    }
                }
                case MineralType mineralType -> {
                    if (mineralType != null) {
                        Mineral mineral = new Mineral(mineralType, quest.getRewardAmount());
                        currentPlayer.getInventory().addItem(mineral);
                    }
                }
                case ToolType toolType -> {
                    if (toolType != null) {
                        Tool tool = new Tool(toolType, quest.getRewardAmount());
                        currentPlayer.getInventory().addItem(tool);
                    }
                }
                case FishType fishType -> {
                    if (fishType != null) {
                        Fish fish = new Fish(fishType, quest.getRewardAmount());
                        currentPlayer.getInventory().addItem(fish);
                    }
                }
                case FoodType foodType -> {
                    if (foodType != null) {
                        Food food = new Food(foodType, quest.getRewardAmount());
                        currentPlayer.getInventory().addItem(food);
                    }
                }
                case CookingRecipeType cookingRecipes -> {
                    if (cookingRecipes != null) {
                        CookingRecipe cookingRecipe = new CookingRecipe(cookingRecipes);
                        if (currentPlayer.getCookingRecipe() != null && !currentPlayer.getCookingRecipe().contains(cookingRecipe)) {
                            currentPlayer.getCookingRecipe().add(cookingRecipe);
                        }
                    }
                }
                case CraftingMachineType craftingMachineType -> {
                    if (craftingMachineType != null) {
                        CraftingMachine craftingMachine = new CraftingMachine(craftingMachineType, quest.getRewardAmount());
                        currentPlayer.getInventory().addItem(craftingMachine);
                    }
                }
                default -> {
                    return new Result(false, "Invalid item!");
                }
            }
        }

        if (npc != null && npc.getQuests() != null) {
            npc.getQuests().put(quest, true);
        }
        return new Result(true, "Nice job! You got your reward!");
    }

    public Result talk(String receiverName, String message) {
        Player currentPlayer = game.getCurrentPlayer();
        Player player = game.getUserByUsername(receiverName).getPlayer();
        if (player == null) {
            return new Result(false, "Player not found!");
        }
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near" + receiverName);
        }

        Talk talk = new Talk(currentPlayer, player, message);

        currentPlayer.addTalk(talk);
        player.addTalk(talk);

        Friendship friendship = game.getFriendshipByPlayers(currentPlayer, player);
        friendship.addFriendshipPoints(10);

        if (player.equals(currentPlayer.getSpouse())) {
            player.addEnergy(50);
            currentPlayer.addEnergy(50);
        }

        return new Result(true, game.getCurrentPlayer().getUsername() + " sent a message to " + player.getUsername() + ":\n" + message);
    }

    // Remote synchronization variant: applies the same effect without proximity checks
    public Result talkRemote(String senderUsername, String receiverUsername, String message) {
        User senderUser = game.getUserByUsername(senderUsername);
        User receiverUser = game.getUserByUsername(receiverUsername);
        if (senderUser == null || receiverUser == null || senderUser.getPlayer() == null || receiverUser.getPlayer() == null) {
            return new Result(false, "Player not found!");
        }

        Player sender = senderUser.getPlayer();
        Player receiver = receiverUser.getPlayer();

        Talk talk = new Talk(sender, receiver, message);
        sender.addTalk(talk);
        receiver.addTalk(talk);

        Friendship friendship = game.getFriendshipByPlayers(sender, receiver);
        if (friendship != null) {
            friendship.addFriendshipPoints(10);
        }

        if (receiver.equals(sender.getSpouse())) {
            receiver.addEnergy(50);
            sender.addEnergy(50);
        }

        return new Result(true, sender.getUsername() + " sent a message to " + receiver.getUsername() + ":\n" + message);
    }

    public Result talkHistory(String username) {
        Player targetPlayer = game.getUserByUsername(username).getPlayer();
        if (targetPlayer == null) {
            return new Result(false, "Player not found!");
        }

        Player currentPlayer = game.getCurrentPlayer();
        StringBuilder stringBuilder = new StringBuilder();

        boolean foundAnyTalks = false;

        for (Talk talk : currentPlayer.getTalks()) {
            if ((talk.getSender().equals(currentPlayer) && talk.getReceiver().equals(targetPlayer)) ||
                (talk.getSender().equals(targetPlayer) && talk.getReceiver().equals(currentPlayer))) {
                stringBuilder.append(talk.toString()).append("\n");
                foundAnyTalks = true;
            }
        }

        if (!foundAnyTalks) {
            stringBuilder.append("No conversation history with ").append(username);
        } else {
            stringBuilder.insert(0, "Conversation history with " + username + ":\n");
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result showNPCFriendships() {
        StringBuilder stringBuilder = new StringBuilder();
        for (NPC npc : game.getNPCs()) {
            stringBuilder.append(npc.getFriendShipWith(game.getCurrentPlayer()).toString());
        }
        return new Result(true, stringBuilder.toString());
    }

    public Result showAllFriendShips() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Friendship friendship : game.getFriendshipsByPlayer(game.getCurrentPlayer())) {
            stringBuilder.append(friendship.toString());
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result giftPlayer(String username, String itemName, String itemAmountStr) {
        Player receiver = game.getUserByUsername(username).getPlayer();
        if (receiver == null) {
            return new Result(false, "Player not found!");
        }
        if (!isPlayerNearSomething(receiver.currentX(), receiver.currentY())) {
            return new Result(false, "You should be near" + username);
        }
        Friendship friendship = game.getFriendshipByPlayers(game.getCurrentPlayer(), receiver);
        if (friendship.getFriendshipLevel() < 1) {
            return new Result(false, "Your friendship level must at least be 1!");
        }

        Item item = null;
        for (Item i : game.getCurrentPlayer().getInventory().getItems()) {
            if (i.getName().equals(itemName)) {
                item = i;
                break;
            }
        }
        if (item == null) {
            return new Result(false, "Item not found in your inventory!");
        }

        int itemAmount;
        try {
            itemAmount = Integer.parseInt(itemAmountStr);
        }
        catch (NumberFormatException e) {
            return new Result(false, "Invalid item amount");
        }

        if (itemAmount > item.getNumber()) {
            return new Result(false, "You don't have enough items!");
        }


        switch (item) {
            case Fish fish -> {
                game.getCurrentPlayer().getInventory().removeItem(fish.getClass(), itemAmount);
                Fish newFish = new Fish(fish.getFishType(), itemAmount);
                receiver.getInventory().addItem(newFish);
            }
            case Crop crop -> {
                game.getCurrentPlayer().getInventory().removeItem(crop.getClass(), itemAmount);
                Crop newCrop = new Crop(crop.getCropType(), itemAmount);
                receiver.getInventory().addItem(newCrop);
            }
            case Food food -> {
                game.getCurrentPlayer().getInventory().removeItem(food.getClass(), itemAmount);
                Food newFood = new Food(food.getFoodType(), itemAmount);
                receiver.getInventory().addItem(newFood);
            }
            case Material material -> {
                game.getCurrentPlayer().getInventory().removeItem(material.getClass(), itemAmount);
                MaterialType materialType = material.getMaterialType();
                if (materialType == null) {
                    return new Result(false, "Error: Item '" + itemName + "' has corrupted data!");
                }
                Material newMaterial = new Material(materialType, itemAmount);
                receiver.getInventory().addItem(newMaterial);
            }
            case Mineral mineral -> {
                game.getCurrentPlayer().getInventory().removeItem(mineral.getClass(), itemAmount);
                Mineral newMineral = new Mineral(mineral.getMineralType(), itemAmount);
                receiver.getInventory().addItem(newMineral);
            }
            case Seed seed -> {
                game.getCurrentPlayer().getInventory().removeItem(seed.getClass(), itemAmount);
                Seed newSeed = new Seed(seed.getForagingSeedType(), itemAmount);
                receiver.getInventory().addItem(newSeed);
            }
            case TrashCan trashCan -> {
                game.getCurrentPlayer().getInventory().removeItem(trashCan.getClass(), itemAmount);
                TrashCan newTrashCan = new TrashCan(trashCan.getTrashCanType(), itemAmount);
                receiver.getInventory().addItem(newTrashCan);
            }
            case Good good -> {
                game.getCurrentPlayer().getInventory().removeItem(good.getClass(), itemAmount);
                Good newGood = new Good(good.getProductType(), itemAmount);
                receiver.getInventory().addItem(newGood);
            }
            default -> {
                return new Result(false, "Item is not giftable!");
            }
        }

        if (receiver.equals(game.getCurrentPlayer().getSpouse())) {
            receiver.addEnergy(50);
            game.getCurrentPlayer().addEnergy(50);
        }

        Gift gift = new Gift(game.getCurrentPlayer(), receiver, item, itemAmount);
        receiver.addGift(gift);
        receiver.addNotif(game.getCurrentPlayer(), game.getCurrentPlayer().getUsername() + " has gifted something to you!");
        return new Result(true, "You gifted " + itemName + " to " + username);
    }

    // Remote variant used by receiving clients to apply the gift without local proximity/inventory checks
    public Result giftPlayerRemote(String senderUsername, String receiverUsername, String itemName, String itemAmountStr) {
        User senderUser = game.getUserByUsername(senderUsername);
        User receiverUser = game.getUserByUsername(receiverUsername);
        if (senderUser == null || receiverUser == null || receiverUser.getPlayer() == null) {
            return new Result(false, "Player not found!");
        }
        Player receiver = receiverUser.getPlayer();

        int itemAmount;
        try {
            itemAmount = Integer.parseInt(itemAmountStr);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid item amount");
        }
        if (itemAmount <= 0) {
            return new Result(false, "Invalid item amount");
        }

        try {
            Item giftedItem = ItemFactory.createItemOrThrow(itemName, itemAmount);
            receiver.getInventory().addItem(giftedItem);
        } catch (IllegalArgumentException ex) {
            return new Result(false, "Item '" + itemName + "' doesn't exist");
        }

        Friendship friendship = game.getFriendshipByPlayers(senderUser.getPlayer(), receiver);
        if (friendship != null) {
            friendship.addFriendshipPoints(5);
        }

        receiver.addNotif(senderUser.getPlayer(), senderUsername + " has gifted something to you!");
        return new Result(true, "Gift delivered to " + receiverUsername + ": " + itemName + " x" + itemAmount);
    }

    public Result rateGift(String idString, String rateString) {
        int giftId;
        try {
            giftId = Integer.parseInt(idString);
        }
        catch (NumberFormatException e) {
            return new Result(false, "Id format is invalid!");
        }

        Gift gift = game.getCurrentPlayer().getGiftById(giftId);
        if (gift == null) {
            return new Result(false, "There's no gift with the given id!");
        }

        if (gift.getRate() != 0) {
            return new Result(false, "You have already rated this gift!");
        }

        int rate;
        try {
            rate = Integer.parseInt(rateString);
        }
        catch (NumberFormatException e) {
            return new Result(false, "Rate format is invalid!");
        }

        if (rate < 1 || rate > 5) {
            return new Result(false, "Rate should be between 1 to 5");
        }

        gift.setRate(rate);
        int friendshipXP = (rate - 3) * 30 + 15;
        game.getFriendshipByPlayers(gift.getSender(), gift.getReceiver()).addFriendshipPoints(friendshipXP);
        return new Result(true, "You have rated the gift!");
    }

    public Result showGiftsList() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Gift gift : game.getCurrentPlayer().getGifts().values()) {
            stringBuilder.append(gift.toString());
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result showGiftHistoryWith(String username) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Gift gift : game.getCurrentPlayer().getGifts().values()) {
            if (gift.getSender().getUsername().equals(username) || gift.getReceiver().getUsername().equals(username)) {
                stringBuilder.append(gift.toString());
            }
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result hug(String username) {
        Player player = game.getUserByUsername(username).getPlayer();
        if (player == null) {
            return new Result(false, "Invalid username!");
        }
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near someone to hug them!");
        }

        Friendship friendship = game.getFriendshipByPlayers(game.getCurrentPlayer(), player);
        if (friendship.getFriendshipLevel() < 2) {
            return new Result(false, "Your friendship level must at least be 2 to hug!");
        }

        if (player.equals(game.getCurrentPlayer().getSpouse())) {
            player.addEnergy(50);
            game.getCurrentPlayer().addEnergy(50);
        }

        friendship.addFriendshipPoints(60);
        player.addNotif(game.getCurrentPlayer(), game.getCurrentPlayer().getUsername() + " has hugged you!");
        return new Result(true, "You hugged " + username);
    }

    public Result flowerSomeone(String username) {
        Player currentPlayer = game.getCurrentPlayer();
        Player player = game.getUserByUsername(username).getPlayer();
        if (player == null) {
            return new Result(false, "Invalid username!");
        }
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near someone to give flower to them!");
        }

        Material flower = (Material) currentPlayer.getInventory().getItemByName("Bouquet");
        if (flower == null) {
            return new Result(false, "You don't have any flower!");
        }

        Friendship friendship = game.getFriendshipByPlayers(currentPlayer, player);
        if (friendship.getFriendshipPoints() < 300) {
            return new Result(false, "Your friendship points must at least be 300!");
        }

        if (player.equals(currentPlayer.getSpouse())) {
            player.addEnergy(50);
            currentPlayer.addEnergy(50);
        }

        currentPlayer.getInventory().removeItem(flower.getClass(), 1);
        Material newFlower = new Material(MaterialType.Bouquet, 1);
        player.getInventory().addItem(newFlower);

        friendship.setFriendshipLevel(3);
        player.addNotif(currentPlayer, currentPlayer.getUsername() + " has flowered you!");
        return new Result(true, "You have flowered " + username + "and your friendship level is now 3!");
    }

    public Result askMarriage(String username) {
        Player currentPlayer = game.getCurrentPlayer();
        Player player = game.getUserByUsername(username).getPlayer();
        if (player == null) {
            return new Result(false, "Invalid username!");
        }
        if (player.getSpouse() != null) {
            return new Result(false, "You can't ask marriage to someone who is already married!");
        }
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near someone to ask them!");
        }

        Friendship friendship = game.getFriendshipByPlayers(currentPlayer, player);
        if (friendship.getFriendshipLevel() < 3) {
            return new Result(false, "Your friendship level must at least be 3!");
        }
        if (friendship.getFriendshipPoints() < 400) {
            return new Result(false, "Your friendship points must at least be 400!");
        }

        Material ring = (Material) currentPlayer.getInventory().getItemByName("Wedding Ring");
        if (ring == null) {
            return new Result(false, "You need a ring to propose!");
        }

        if (currentPlayer.getGender().equals(player.getGender())) {
            return new Result(false, "You can't be GAY in this game!");
        }

        player.addNotif(currentPlayer, currentPlayer.getUsername() + " has proposed to you!");
        return new Result(true, "You have proposed! Wait for the answer!");
    }

    public Result respondToMarriage(String respond, String username) {
        Player player = game.getUserByUsername(username).getPlayer();
        if (player == null) {
            return new Result(false, "Invalid username!");
        }

        Friendship friendship = game.getFriendshipByPlayers(game.getCurrentPlayer(), player);
        if (respond.equals("accept")) {
            friendship.setFriendshipLevel(4);
            game.getCurrentPlayer().setSpouse(player);
            player.setSpouse(game.getCurrentPlayer());

            game.getCurrentPlayer().getBankAccount().deposit(player.getBankAccount().getBalance());
            player.setBankAccount(game.getCurrentPlayer().getBankAccount());
            player.addNotif(game.getCurrentPlayer(), "You are now married to " + game.getCurrentPlayer().getUsername());

            player.getInventory().removeItem(player.getInventory().getItemByName("Wedding Ring").getClass(), 1);
            Material newRing = new Material(MaterialType.Wedding_Ring, 1);
            player.getInventory().addItem(newRing);

            return new Result(true, "You are happily married!");
        }
        else {
            friendship.resetFriendship();
            player.addNotif(game.getCurrentPlayer(), "You have been dumped! Go hit the gym!");
            return new Result(true, "You have rejected the proposal!");
        }
    }

    public Result goToTradeMenu() {
        App.getInstance().setCurrentMenu(Menu.TradeMenu);
        return new Result(true, "You are now in trade menu!");
    }

    private boolean isPlayerNearSomething(int x, int y) {
        Player player = game.getCurrentPlayer();
        int playerX = player.currentX();
        int playerY = player.currentY();

        return Math.abs(playerX - x) <= 1 && Math.abs(playerY - y) <= 1;
    }

    public Result cheatSetEnergy(int value) {
        Player player = game.getCurrentPlayer();
        player.setEnergy(value);
        return new Result(true, player.getUsername() + "'s energy: is set to " + value);
    }

    public Result cheatUnlimitedEnergy(){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        player.setEnergy(Integer.MAX_VALUE);
        return new Result(true, player.getUsername() + "'s energy: is unlimited now! HA HA HA");
    }

    public Result showInventoryItems(){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        StringBuilder items = new StringBuilder();
        if(player.getInventory().getItems() == null || player.getInventory().getItems().isEmpty()){
            return new Result(false, "You have no items!");
        }
        for(Item item: player.getInventory().getItems()){
            items.append(item.getName() + " x" + item.getNumber() + "\n");
        }
        items.delete(items.length() - 1, items.length());
        return new Result(true, items.toString());
    }

    public Result removeItemFromInventory(String itemName, int quantity) {
        Player player = game.getCurrentPlayer();
        Item item = player.getInventory().getItemByName(itemName);

        if (item == null) {
            return new Result(false, "Item not found.");
        }
        if (item.getNumber() < quantity) {
            return new Result(false, "Not enough items to remove.");
        }
        if (item instanceof Tool) {
            return new Result(false, "Cannot trash a tool!");
        }

        player.getInventory().remove2(itemName, quantity);
        return new Result(true, quantity + "x " + itemName + " removed from inventory.");
    }

    public Result equipTool(String toolName){
        Game game = App.getInstance().getCurrentGame();
        Player player = game.getCurrentPlayer();
        Tool tool;
        if((tool = (Tool) findItem(toolName, player.getInventory().getItems())) == null){
            return new Result(false, "Tool not found in your inventory");
        }
        player.setCurrentTool(tool);
        return new Result(true, tool.getName() + "'s tool has been equipped");
    }

    public Result showCurrentTool(){
        Game game = App.getInstance().getCurrentGame();
        Tool tool = game.getCurrentPlayer().getCurrentTool();
        if(tool == null){
            return new Result(false, "There is no tool in your hand!");
        }
        return new Result(true, tool.getName() + " is your current tool");
    }

    public Result showAllTools(){
        Player player = game.getCurrentPlayer();
        ArrayList<Item> tools = player.getInventory().getItems();
        return new Result(true, toolListMaker(tools));
    }

    public Result useTool(String directionStr){
        GameMap map = game.getMap();
        Player player = game.getCurrentPlayer();
        Tile currrentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = getTargetTile(currrentTile,directionStr,map);
        return new Result(true, player.handleToolUse(targetTile).Message());
    }

    public Result craftInfo(String craftName) {
        CropType cropType;
        if((cropType = findCropType(craftName)) == null) {
            return new Result(false, "Crop not found");
        }
        StringBuilder info = new StringBuilder();
        info.append("Name: " + cropType.name()).append("\nSource: " + cropType.getSeed()).append("\nStages: ");

        for (Integer stage : cropType.getStages()){
            info.append(stage);
        }

        info.deleteCharAt(info.length() - 1);
        info.append("\nTotal Harvest Time: " + cropType.getTotalHarvestTime()).
            append("\nOne Time: " + cropType.isOneTimeHarvest()).
            append("\nRegrowth Time: " + cropType.getRegrowthTime()).
            append("\nBase Sell Price: " + cropType.getBaseSellPrice()).
            append("\nIs Edible: " + cropType.isEdible()).
            append("\nBase Energy: " + cropType.getEnergy()).
            append("\nBase Health: " + cropType.getBaseHealth()).
            append(("\nSeason: "));

        info.append(cropType.getSeasons());
        info.deleteCharAt(info.length() - 1);
        info.append("\nCan Become Giant: " + cropType.canBecomeGiant());
        return new Result(true, info.toString());
    }

    public Result treeInfo(String treeName) {
        TreeType treeType;
        if((treeType = findTreeType(treeName)) == null) {
            return new Result(false, "tree not found");
        }
        StringBuilder info = new StringBuilder();
        info.append("Name: " + treeType.name()).append("\nSource: " + treeType.getSource()).append("\nStages: ");
        for(Integer stage : treeType.getStages()){
            info.append(stage).append("-");
        }
        info.deleteCharAt(info.length() - 1);
        info.append("\nTotal Harvest Time: " + treeType.getTotalHarvestTime()).
            append("\nFruit: " + treeType.getProduct().getName()).
            append("\nHarvest cycle time: " + treeType.getHarvestCycle()).
            append("\nBase Sell Price: " + treeType.getBaseSellPrice()).
            append("\nIs Edible: " + treeType.isEdible()).
            append("\nBase Energy: " + treeType.getEnergy()).
            append(("\nSeason: "));
        for(Season season : treeType.getSeasons()){
            info.append(season.name()).append(", ");
        }
        info.deleteCharAt(info.length() - 1);
        return new Result(true, info.toString());
    }

    public Result plant(String seedName, String directionStr) {
        GameMap map = game.getMap();
        Player player = game.getCurrentPlayer();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = getTargetTile(currentTile, directionStr, map);

        Crop crop = findCropSeedInInventory(player.getInventory().getItems(), seedName);
        Fruit fruit = (crop == null) ? findFruitSeedInInventory(player.getInventory().getItems(), seedName) : null;
        Seed seed = findSeedInInventory(player.getInventory().getItems(), seedName);

        if (crop == null && fruit == null) {
            return new Result(false, "Seed not found in inventory (fruit and crop)");
        }

        if(seed == null) {
            return new Result(false, "Seed not found in inventory");
        }

        if (crop != null) {
            if (!targetTile.getType().equals(TileType.Shoveled) ||
                targetTile.getType().equals(TileType.Planted)) {
                return new Result(false, "Crops can only be planted on shoveled, unplanted soil");
            }

            targetTile.setType(TileType.Planted);
            targetTile.setPlant(crop);
            player.getInventory().getItems().remove(seed);
            player.getInventory().addNumOfItems(-1);
            return new Result(true, crop.getName() + " crop planted successfully");
        }
        else if (fruit != null) {
            if (!targetTile.getType().equals(TileType.Shoveled)) {
                return new Result(false, "Fruit trees can only be planted on shoveled");
            }

            targetTile.setType(TileType.Tree);
            targetTile.setPlant(fruit);
            player.getInventory().getItems().remove(seed);
            player.getInventory().addNumOfItems(-1);
            return new Result(true, fruit.getName() + " tree planted successfully");
        }

        return new Result(false, "You can not plant in this tile");
    }

    public Result showPlant(String xStr, String yStr){
        int x = Integer.parseInt(xStr);
        int y = Integer.parseInt(yStr);
        GameMap map = game.getMap();
        Tile targetTile = map.getTile(x,y);
        StringBuilder plantInfo = new StringBuilder();
        if(targetTile.getType().equals(TileType.Planted) || targetTile.getType().equals(TileType.Tree)) {
            if(targetTile.getPlant() instanceof Crop){
                plantInfo.append("Name: " + ((Crop) targetTile.getPlant()).getCropType().getName());
                plantInfo.append("\nRemaining time to harvest: " + ((Crop) targetTile.getPlant()).getDayRemaining());
                plantInfo.append("\nCurrent stage: " + ((Crop) targetTile.getPlant()).getCurrentStage());
                plantInfo.append("\nIs watered today? " + ((Crop) targetTile.getPlant()).isWateredToday());
                plantInfo.append("\nIs fertilized today? " + ((Crop) targetTile.getPlant()).isFertilizedToday());
                return new Result(true, plantInfo.toString());
            }
            else if(targetTile.getPlant() instanceof Fruit){
                plantInfo.append("Name: " + ((Fruit) targetTile.getPlant()).getFruitType().name());
                plantInfo.append("\nRemaining time to harvest: " + ((Fruit) targetTile.getPlant()).getDayRemaining());
                plantInfo.append("\nCurrent stage: " + ((Fruit) targetTile.getPlant()).getCurrentStage());
                plantInfo.append("\nIs watered today? " + ((Fruit) targetTile.getPlant()).isWateredToday());
                plantInfo.append("\nIs fertilized today? " + ((Fruit) targetTile.getPlant()).isFertilizedToday());
                return new Result(true, plantInfo.toString());
            }
        }
        return new Result(false, "Target tile does not have a plant");
    }

    public Result fertilize(String fertilizedName, String directionStr) {
        GameMap map = game.getMap();
        Player player = game.getCurrentPlayer();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = map.getTile(currentTile.getX(), currentTile.getY());
        Material fertilizer;

        if(!(targetTile.getType().equals(TileType.Shoveled) || targetTile.getType().equals(TileType.Planted) || targetTile.getType().equals(TileType.Tree))) {
            return new Result(false, "You can not fertilize this tile");
        }

        if((fertilizer = findFertilizerInInventory(player.getInventory().getItems(), fertilizedName)) == null) {
            return new Result(false, "Fertilizer not found in inventory");
        }

        if(fertilizer.getMaterialName().equals(MaterialType.Basic_Retaining_Soil.getName())){
            targetTile.getPlant().setFertilizedToday(true);
        }
        else if(fertilizer.getMaterialName().equals(MaterialType.Quality_Retaining_Soil.getName())){
            targetTile.getPlant().setFertilizedToday(true);
            targetTile.getPlant().growFaster();
        }
        else if(fertilizer.getMaterialName().equals(MaterialType.Deluxe_Retaining_Soil.getName())){
            targetTile.getPlant().setFertilizedToday(true);
            targetTile.getPlant().setWateredToday(true);
        }
        return new Result(true, "Fertilizer fertilized successfully");
    }

    public Result wateringCanFilled(){
        Player player = game.getCurrentPlayer();
        WateringCan wateringCan = findWateringCanInInventory(player.getInventory().getItems());
        if(wateringCan == null) {
            return new Result(false, "There is no watering can inventory 0o0");
        }
        return new Result(true, "There is " + wateringCan.getFilledCapacity() + "liter in watering can");
    }
    // todo : delete below functions
    public Result showCraftingRecipes(){
        Player player = game.getCurrentPlayer();
        ArrayList<CraftingRecipe> playerCraftingRecipes = player.getCraftingRecipe();
        StringBuilder recipeString = new StringBuilder();
        recipeString.append(player.getUsername() + "'s crafting recipes:\n");
        for(CraftingRecipe craftingRecipe : playerCraftingRecipes) {
            recipeString.append(craftingRecipe.getRecipeType().getName() + "\n");
        }
        recipeString.deleteCharAt(recipeString.length() - 1);
        return new Result(true, recipeString.toString());
    }

    public Result placeItem(String itemName, String directionStr) {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getMap();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = getTargetTile(currentTile, directionStr, map);
        if(targetTile.getItem() != null) {
            return new Result(false, "you can not put item on this tile, it has another item");
        }
        Item item = findItem(itemName, player.getInventory().getItems());
        if(item == null) {
            return new Result(false, "there is no item named " + itemName + " in your inventory");
        }
        targetTile.setItem(item);
        player.getInventory().getItems().remove(item);
        player.getInventory().addNumOfItems(-1);
        return new Result(true,item.getName() + " placed successfully");
    }

    public Result pickItem(String directionStr) {
        GameMap map = game.getMap();
        Player player = game.getCurrentPlayer();
        Tile currrentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = getTargetTile(currrentTile,directionStr,map);
        if(player.getCurrentTool() == null){
            return new Result(false, "please equip pickaxe");
        }
        if(!player.getCurrentTool().getToolType().name().endsWith("Pickaxe")){
            return new Result(false, "you can not pick an item, change your current tool to pickaxe");
        }
        if(targetTile.getItem() == null) {
            return new Result(false, "this tile doesn't have any item to pick");
        }
        if(player.getInventory().isFull()){
            return new Result(false, "your inventory is full");
        }
        player.getInventory().addItem(targetTile.getItem());
        Item pickedItem = targetTile.getItem();
        targetTile.setItem(null);
        player.getInventory().addNumOfItems(1);
        return new Result(true, pickedItem.getName() + " x" + pickedItem.getNumber() + " picked successfully");
    }

    public Result cheatAddItem(String itemName, String quantityStr) {
        try {
            Player player = game.getCurrentPlayer();
            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    return new Result(false, "Quantity must be positive");
                }
            } catch (NumberFormatException e) {
                return new Result(false, "Invalid quantity format");
            }
            Item item;
            try {
                item = ItemFactory.createItemOrThrow(itemName, quantity);
            } catch (IllegalArgumentException e) {
                return new Result(false, "Item '" + itemName + "' doesn't exist");
            }

            player.getInventory().addItem(item);
            player.getInventory().addNumOfItems(1);

            return new Result(true, item.getName() + " added successfully!");

        } catch (Exception e) {
            return new Result(false, "An unexpected error occurred");
        }
    }

    public Result useArtisanMachine(String machineName, String goodName) {
        Player player = game.getCurrentPlayer();
        CraftingMachineType machineType = findMachineType(machineName);
        if(machineType == null) {
            return new Result(false, "no such artisan machine");
        }
        CraftingMachine machine = findCraftingMachineInInventory(machineType,player.getInventory().getItems());
        if(machine == null) {
            return new Result(false, "you do not have this machine in your inventory");
        }
        if(player.getInventory().isFull()){
            return new Result(false, "your inventory is full");
        }
        Good newGood = createGood(goodName);
        if(newGood == null){
            return new Result(false, "oh shit good didn't created");
        }
        player.getInventory().getItems().add(newGood);
        return new Result(true, newGood.getName() + " will be added to your inventory");
    }

    public Result getArtisanProduct(String machineName) {
        Player player = game.getCurrentPlayer();
        Good good = findGoodInInventory(machineName);
        if(good == null) {
            return new Result(false, "you do not have this good");
        }
        if(!good.isReadyToUse()){
            return new Result(false,"The good is not ready to use");
        }
        player.getInventory().addNumOfItems(1);
        return new Result(true, good.getName() + " added to your inventory");
    }

    public Result cheatAddBalance(String amountStr) {
        int amount = Integer.parseInt(amountStr);
        Player player = game.getCurrentPlayer();
        player.getBankAccount().deposit(amount);
        return new Result(true, amount + " added successfully :)");
    }

    public Result showCookingRecipes() {
        Player player = game.getCurrentPlayer();
        if(player.getCookingRecipe() == null || player.getCookingRecipe().isEmpty()) {
            return new Result(false, "you do not have any cooking recipes");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("You have :\n");
        for(CookingRecipe recipe : player.getCookingRecipe()) {
            builder.append(recipe.getRecipeType().getName()).append("\n");
        }
        return new Result(true, builder.toString());
    }

    private User findUser(String username) {
        for(User user : App.getInstance().getUsers()){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    private CropType findCropType(String cropName) {
        for(CropType cropType : CropType.values()) {
            if (cropType.getName().equals(cropName)) {
                return cropType;
            }
        }
        return null;
    }

    private TreeType findTreeType(String treeName) {
        for (TreeType treeType : TreeType.values()) {
            if (treeType.name().equals(treeName)) {
                return treeType;
            }
        }
        return null;
    }

    private List<String> validateGameCommand(String command) throws IllegalArgumentException {

        // Check for -u flag
        if (GameMenuCommands.CheckFlagInNewGame.getMatcher(command) == null) {
            throw new IllegalArgumentException("Missing '-u' flag. Usage: 'game new -u <user1> <user2> <user3>'");
        }

        // Extract usernames
        Matcher usernameMatcher = GameMenuCommands.CreateNewGame.getMatcher(command);
        List<String> usernames = new ArrayList<>();

        usernames.add(usernameMatcher.group("username1"));
        usernames.add(usernameMatcher.group("username2"));
        usernames.add(usernameMatcher.group("username3"));
        for(String username : usernames){
            System.out.println(username);
        }

        // Validate username count
        if (usernames.size() < 3) {
            throw new IllegalArgumentException("Too few usernames. Expected 3, got " + usernames.size());
        }
        if (usernames.size() > 3) {
            throw new IllegalArgumentException("Too many usernames. Expected 3, got " + usernames.size());
        }

        return usernames;
    }

    private boolean isUserAvailable(User user) {
        for(Game game : App.getInstance().getGames()){
            if(game.getPlayers().contains(user)){
                return false;
            }
        }
        return true;
    }

    private Result cookingRefrigeratorPut(String materialName, String quantityStr) {
        Player player = game.getCurrentPlayer();
        House house  = player.getHouse();
        int quantity;
        try { quantity = Integer.parseInt(quantityStr); }
        catch (NumberFormatException e) {
            return Result.failure("The value must be an integer: " + quantityStr);
        }

        MaterialType material;
        try { material = MaterialType.valueOf(materialName); }
        catch (IllegalArgumentException e) {
            return Result.failure("Invalid raw material: " + materialName);
        }

        boolean ok = house.refrigerator().putMaterial(material, quantity);
        return ok
            ? Result.success(quantity + " × " + material + " It was placed in the refrigerator.")
            : Result.failure("Error in placing the material in the refrigerator.");
    }

    private Result cookingRefrigeratorPick(String materialName, String quantityStr) {
        Player player = game.getCurrentPlayer();
        House  house  = player.getHouse();
        int quantity;
        try { quantity = Integer.parseInt(quantityStr); }
        catch (NumberFormatException e) {
            return Result.failure("The value must be an integer: " + quantityStr);
        }

        MaterialType mat;
        try { mat = MaterialType.valueOf(materialName); }
        catch (IllegalArgumentException e) {
            return Result.failure("Invalid raw material: " + materialName);
        }

        boolean ok = house.refrigerator().pickMaterial(mat, quantity);
        return ok
            ? Result.success(quantity + " × " + mat + " Removed from the refrigerator.")
            : Result.failure("There is not enough " + mat + " in the refrigerator.");
    }

    private Result cookingShowRecipes() {
        StringBuilder sb = new StringBuilder("Cooking recipes:\n");
        for (CookingRecipeType r : CookingRecipeType.values()) {
            sb.append("- ").append(r.getDisplayName()).append("\n");
        }
        return Result.success(sb.toString());
    }

    public Result eat(String foodName) {
        Player player = game.getCurrentPlayer();
        Food food = (Food) player.getInventory().findItemByType(
            Arrays.stream(FoodType.values())
                .filter(f -> f.getName().equalsIgnoreCase(foodName))
                .findFirst()
                .orElse(null)
        );

        if (food == null) {
            return new Result(false, "You don't have this food to eat.");
        }

        player.getInventory().remove2(foodName, 1);
        player.addEnergy(food.getFoodType().getEnergy());

        StringBuilder buffMessage = new StringBuilder("Yummy!");
        FoodType foodType = food.getFoodType();

        // Handle Buffs
        if (foodType.isBuffMaxEnergy()) {
            ActiveBuff buff = new ActiveBuff(ActiveBuff.BuffType.MAX_ENERGY, null, foodType.getEffectiveTime());
            player.getActiveBuffs().add(buff);
            buffMessage.append("\nMax Energy buff activated for ").append(foodType.getEffectiveTime() * 10).append(" game minutes!");
        }
        if (foodType.getSkillBuff() != null) {
            Skills skillToBuff = foodType.getSkillBuff();
            player.getSkillData(skillToBuff).applyBuff(4); // Buff to level 4
            ActiveBuff buff = new ActiveBuff(ActiveBuff.BuffType.SKILL, skillToBuff, foodType.getEffectiveTime());
            player.getActiveBuffs().add(buff);
            buffMessage.append("\n").append(skillToBuff.name()).append(" buff activated for ").append(foodType.getEffectiveTime() * 10).append(" game minutes!");
        }

        return new Result(true, buffMessage.toString());
    }

    public Result fishing(String fishingPoleName) {
        Player player = game.getCurrentPlayer();
        Tool fishingPole = player.getInventory().getTool(fishingPoleName);
        if (fishingPole == null) {
            return new Result(false, "No fishing pole found");
        }
        // Ensure map reference is initialized
        if (map == null && game != null) {
            map = game.getMap();
        }
        if (map == null) {
            return new Result(false, "Map not loaded yet.");
        }
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        if (!isAdjacentToWater(currentTile)) {
            return new Result(false, "You must be next to water to fish.");
        }
        if (player.getInventory().isFull()) {
            return new Result(false, "Your inventory is full.");
        }

        // --- NEW LOGIC ---
        // Find a random fish that can be caught
        List<FishType> possibleFish = Arrays.stream(FishType.values())
            .filter(f -> f.getSeason() == game.getDate().getCurrentSeason() || f.getSeason() == Season.Special)
            .collect(Collectors.toList());

        if (possibleFish.isEmpty()) {
            return new Result(false, "There are no fish in season.");
        }

        Random rand = new Random();
        FishType caughtFish = possibleFish.get(rand.nextInt(possibleFish.size()));

        // Create and store the minigame instance
        this.activeMinigame = new FishingMinigame(caughtFish);

        // Signal success to the view, which will then start the minigame UI
        return new Result(true, "A fish has bitten!");
    }

    public Result cheatTileType(String direction) {
        Player player = game.getCurrentPlayer();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = getTargetTile(currentTile, direction, map);
        if(targetTile == null){
            return new Result(false, "No target tile found");
        }
        if(targetTile.getType() == null){
            return new Result(false, "tile type is null");
        }
        return new Result(true, "Type: " + targetTile.getType().name());
    }

    private void calculateEnergy(int amount) {
        Player player = game.getCurrentPlayer();
        player.setEnergy(player.getEnergy() + amount);
        if(player.getEnergy() >= 200){
            player.setEnergy(200);
        }
        if(player.getEnergy() <= 0){
            player.setFainted(true);
            switchTurn();
        }
    }

    private Item findItem(String itemName, ArrayList<Item> items) {
        for(Item item : items){
            if(item.getName().equals(itemName)){
                return item;
            }
        }
        return null;
    }

    private Item findItem(ItemType itemType, ArrayList<Item> items) {
        for(Item item : items){
            if(item.getItemType().equals(itemType)){
                return item;
            }
        }
        return null;
    }

    private String toolListMaker(ArrayList<Item> tools) {
        StringBuilder toolList = new StringBuilder();
        for (Item item : tools) {
            if (item instanceof Tool) {
                Tool tool = (Tool) item;
                toolList.append(tool.getToolType().name())
                    .append(" x").append(tool.getNumber())
                    .append("\n");
            }
        }
        return toolList.toString();
    }

    private Tile getTargetTile(Tile current, String directionInput, GameMap gameMap) {
        Direction direction = Direction.fromString(directionInput);

        int targetX = current.getX() + direction.dx;
        int targetY = current.getY() + direction.dy;

        Tile targetTile = gameMap.getTile(targetX, targetY);
        return targetTile;
    }

    private Fruit findFruitSeedInInventory(ArrayList<Item> items, String seedName) {
        for (Item item : items) {
            if (item instanceof Seed && item.getName().equals(seedName)) {
                FruitType fruitType = findFruitTypeBySeed(seedName);
                if (fruitType != null) {
                    return new Fruit(fruitType, 1);
                }
            }
        }
        return null;
    }

    private Crop findCropSeedInInventory(ArrayList<Item> items, String seedName) {
        for (Item item : items) {
            if (item instanceof Seed && item.getName().equals(seedName)) {
                CropType cropType = findCropTypeBySeed(seedName);
                if (cropType != null) {
                    return new Crop(cropType, 1);
                }
            }
        }
        return null;
    }

    private CropType findCropTypeBySeed(String seedName) {
        for(CropType cropType : CropType.values()) {
            if(cropType.getSeed().name().equals(seedName)) {
                return cropType;
            }
        }
        return null;
    }

    private FruitType findFruitTypeBySeed(String seedName) {
        for(FruitType fruitType : FruitType.values()) {
            if(fruitType.getName().equals(seedName)) {
                return fruitType;
            }
        }
        return null;
    }

    private Seed findSeedInInventory(ArrayList<Item> items, String seedName) {
        for (Item item : items) {
            if (item instanceof Seed && item.getName().equals(seedName)) {
                return (Seed) item;
            }
        }
        return null;
    }

    private Material findFertilizerInInventory(ArrayList<Item> items, String fertilizer) {
        for (Item item : items) {
            if (item instanceof Material && item.getName().equals(fertilizer)) {
                return (Material) item;
            }
        }
        return null;
    }

    private WateringCan findWateringCanInInventory(ArrayList<Item> items) {
        for (Item item : items) {
            if (item instanceof WateringCan) {
                return (WateringCan) item;
            }
        }
        return null;
    }

    private CraftingRecipes findCraftingRecipeType(String recipeName) {
        for(CraftingRecipes craftingRecipe : CraftingRecipes.values()) {
            if(craftingRecipe.name().equals(recipeName)) {
                return craftingRecipe;
            }
        }
        return null;
    }

    private CraftingRecipe findCraftingRecipeInInventory(ArrayList<CraftingRecipe> recipes, CraftingRecipes craftingRecipeType) {
        for(CraftingRecipe craftingRecipe : recipes) {
            if(craftingRecipe.getRecipeType().equals(craftingRecipeType)) {
                return craftingRecipe;
            }
        }
        return null;
    }

    private Result isInventoryReadyToCraft(CraftingRecipes craftingRecipe) {
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = craftingRecipe.getIngredients();
        ArrayList<Item> playerItems = player.getInventory().getItems();
        ItemType itemType;
        Integer quantity;
        Item item;
        Result result = isReadySecond(craftingRecipe);

        if(!result.isSuccessful()){
            return result;
        }

        for(Map.Entry<ItemType, Integer> entry : neededIngredients.entrySet()) {
            itemType = entry.getKey();
            quantity = entry.getValue();
            item = findItem(itemType,playerItems);
            item.setNumber(item.getNumber() - quantity);
        }
        return result;
    }

    private Result isReadySecond(CraftingRecipes craftingRecipe){
        Player player = game.getCurrentPlayer();
        Map<ItemType, Integer> neededIngredients = craftingRecipe.getIngredients();
        ArrayList<Item> playerItems = player.getInventory().getItems();
        ItemType itemType;
        Integer quantity;
        Item item;

        for(Map.Entry<ItemType, Integer> entry : neededIngredients.entrySet()) {
            itemType = entry.getKey();
            quantity = entry.getValue();
            item = findItem(itemType,playerItems);
            if(item == null) {
                return new Result(false, "Item not found in your inventory");
            }
            if(item.getNumber() < quantity){
                return new Result(false, "Not enough items in your inventory");
            }
        }
        return new Result(true, "All items in your inventory");
    }

    private CraftingMachineType findMachineType(String machineName) {
        for(CraftingMachineType craftingProductType : CraftingMachineType.values()) {
            if(craftingProductType.getName().equals(machineName)) {
                return craftingProductType;
            }
        }
        return null;
    }

    private CraftingMachine findCraftingMachineInInventory(CraftingMachineType craftingProductType, ArrayList<Item> items) {
        for(Item item : items) {
            if(item instanceof CraftingMachine) {
                if(item.getItemType().equals(craftingProductType)) {
                    return (CraftingMachine) item;
                }
            }
        }
        return null;
    }

    private Good createGood(String goodName) {
        for(ArtisanProductType productType : ArtisanProductType.values()) {
            if(productType.getName().equals(goodName)) {
                return new Good(productType, 10);
            }
        }
        return null;
    }

    private Good findGoodInInventory(String machineName) {
        for(Item item : game.getCurrentPlayer().getInventory().getItems()) {
            if(item instanceof Good) {
                if(((Good) item).getProductType().getMachine().getName().equals(machineName)) {
                    return (Good) item;
                }
            }
        }
        return null;
    }

    public Result showCurrentMenu() {
        return new Result(true, "Current menu: " + App.getInstance().getCurrentMenu().name());
    }

    public Result gotoHomeMenu() {
        Player player = game.getCurrentPlayer();
        Tile tile = map.getTile(player.currentX(), player.currentY());
        if(!tile.getType().equals(TileType.House)){
            return new Result(false, "To use home menu you should be at home.");
        }
        App.getInstance().setCurrentMenu(Menu.HomeMenu);
        return new Result(true, "you are in home menu");
    }

    public Result showFishingSkill() {
        Player player = game.getCurrentPlayer();
        return new Result(true, "your fishing skill is: " + player.getSkillLevel(Skills.Fishing));
    }

    public Result showFarmingSkill() {
        Player player = game.getCurrentPlayer();
        return new Result(true, "your farming skill is: " + player.getSkillLevel(Skills.Farming));
    }

    public Result showForagingSkill() {
        Player player = game.getCurrentPlayer();
        return new Result(true, "your foraging skill is: " + player.getSkillLevel(Skills.Foraging));
    }

    public Result showExtractionSkill() {
        Player player = game.getCurrentPlayer();
        return new Result(true, "your mining skill is: " + player.getSkillLevel(Skills.Mining));
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }

    private boolean isAdjacentToWater(Tile tile) {
        if (map == null && game != null) {
            map = game.getMap();
        }
        if (map == null) return false;
        Tile[][] tiles = map.getTiles();
        int x = tile.getX();
        int y = tile.getY();
        int rows = tiles.length;
        int cols = tiles[0].length;
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},          {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                if (tiles[newX][newY].getType().equals(TileType.Water)) {
                    return true;
                }
            }
        }
        return false;
    }

    private double seasonRate(){
        if(game.getTodayWeather().equals(Weather.Sunny)){
            return 1.5;
        }
        else if(game.getTodayWeather().equals(Weather.Rainy)){
            return 1.2;
        }
        else if(game.getTodayWeather().equals(Weather.Stormy)){
            return 0.5;
        }
        else {
            return 1;
        }
    }

    private double poleRate(String fishingPoleName){
        fishingPoleName = fishingPoleName.toLowerCase();
        if(fishingPoleName.startsWith("training")){
            return 0.1;
        }
        else if(fishingPoleName.startsWith("bamboo")){
            return 0.5;
        }
        else if(fishingPoleName.startsWith("fiberglass")){
            return 0.9;
        }
        else {
            return 1.2;
        }
    }

    private Food findFoodInInventory(String foodName) {
        Player player = game.getCurrentPlayer();
        for (Item item : player.getInventory().getItems()) {
            if (item.getName().equals(foodName)) {
                return (Food) item;
            }
        }
        return null;
    }
    public Result sell(String... inputs) {
        Player currentPlayer = game.getCurrentPlayer();
        if (!isPlayerNearSomething(currentPlayer.getTrashCanX(), currentPlayer.getTrashCanY())) {
            return new Result(false, "You aren't near your trash can!");
        }

        Item item = currentPlayer.getInventory().getItemByName(inputs[0]);
        if (item == null) {
            return new Result(false, "You don't have the entered item!");
        }
        if (item instanceof Tool) {
            return new Result(false, "You can't sell this item!");
        }

        if (inputs[1] == null) {
            switch (item.getItemType()) {
                case AnimalProductType animalProductType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(animalProductType.getPrice());
                    break;
                }
                case ArtisanProductType artisanProductType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(artisanProductType.getSellPrice());
                    break;
                }
                case CraftingMachineType craftingMachineType -> {
                    if (craftingMachineType.getPrice() == null) {
                        return new Result(false, "You can't sell this item!");
                    }
                    else {
                        currentPlayer.getBankAccount().setFardaeiDollar(craftingMachineType.getPrice());
                    }
                    break;
                }
                case CropType cropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(cropType.getBaseSellPrice());
                    break;
                }
                case FishType fishType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(fishType.getPrice());
                    break;
                }
                case FoodType foodType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foodType.getSellPrice());
                    break;
                }
                case ForagingCropType foragingCropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingCropType.getBaseSellPrice());
                    break;
                }
                case ForagingSeedType foragingSeedType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingSeedType.getPrice());
                    break;
                }
                case FruitType fruitType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(fruitType.getPrice());
                    break;
                }
                case MaterialType materialType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(materialType.getPrice());
                    break;
                }
                case MineralType mineralType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(mineralType.getPrice());
                    break;
                }
                default -> {
                    return new Result(false, "You can't sell this item!");
                }
            }

            currentPlayer.getInventory().removeItem(item.getClass(), item.getNumber());
        }
        else {
            int amount;
            try {
                amount = Integer.parseInt(inputs[1]);
            }
            catch (NumberFormatException e) {
                return new Result(false, "Invalid amount format!");
            }

            if (item.getNumber() < amount) {
                return new Result(false, "You don't have that much item!");
            }

            switch (item.getItemType()) {
                case AnimalProductType animalProductType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(animalProductType.getPrice());
                    break;
                }
                case ArtisanProductType artisanProductType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(artisanProductType.getSellPrice());
                    break;
                }
                case CraftingMachineType craftingMachineType -> {
                    if (craftingMachineType.getPrice() == null) {
                        return new Result(false, "You can't sell this item!");
                    }
                    else {
                        currentPlayer.getBankAccount().setFardaeiDollar(craftingMachineType.getPrice());
                    }
                    break;
                }
                case CropType cropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(cropType.getBaseSellPrice());
                    break;
                }
                case FishType fishType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(fishType.getPrice());
                    break;
                }
                case FoodType foodType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foodType.getSellPrice());
                    break;
                }
                case ForagingCropType foragingCropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingCropType.getBaseSellPrice());
                    break;
                }
                case ForagingSeedType foragingSeedType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingSeedType.getPrice());
                    break;
                }
                case FruitType fruitType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(fruitType.getPrice());
                    break;
                }
                case MaterialType materialType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(materialType.getPrice());
                    break;
                }
                case MineralType mineralType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(mineralType.getPrice());
                    break;
                }
                default -> {
                    return new Result(false, "You can't sell this item!");
                }
            }

            currentPlayer.getInventory().removeItem(item.getClass(), amount);
        }

        return new Result(true, "You will recieve your money tomorrow!");
    }
    public Result petAnimal(String name) {
        PurchasedAnimal animal = null;
        if (game.getCurrentPlayer() == null || game.getCurrentPlayer().getHousings() == null) {
            return new Result(false, "No housings available.");
        }
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                if (purchasedAnimal.getName().equals(name)) animal = purchasedAnimal;
            }
        }

        if (animal == null) {
            return new Result(false, "Invalid animal name!");
        }

        if (!isPlayerNearSomething(animal.getX(), animal.getY())) {
            return new Result(false, "You are not near the animal!");
        }

        animal.addFriendshipPoints(15);
        return new Result(true, "Animal satisfied!");
    }

    public Result cheatSetAnimalFriendship(String name, String amountString) {
        int amount = Integer.parseInt(amountString);
        PurchasedAnimal animal = null;
        if (game.getCurrentPlayer() == null || game.getCurrentPlayer().getHousings() == null) {
            return new Result(false, "No housings available.");
        }
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                if (purchasedAnimal.getName().equals(name)) animal = purchasedAnimal;
            }
        }

        if (animal == null) {
            return new Result(false, "Invalid animal name!");
        }

        animal.setFriendshipPoints(amount);
        return new Result(true, "Done!");
    }

    public Result showAnimals() {
        StringBuilder stringBuilder = new StringBuilder();
        if (game.getCurrentPlayer() == null || game.getCurrentPlayer().getHousings() == null) {
            return new Result(false, "No housings available.");
        }
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                stringBuilder.append(purchasedAnimal.toString());
            }
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result shepherdAnimal(String name, String xString, String yString) {
        int x, y;
        PurchasedAnimal animal = null;
        if (game.getCurrentPlayer() == null || game.getCurrentPlayer().getHousings() == null) {
            return new Result(false, "No housings available.");
        }
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                if (purchasedAnimal.getName().equals(name)) animal = purchasedAnimal;
            }
        }

        if (animal == null) {
            return new Result(false, "Invalid animal name!");
        }

        try {
            x = Integer.parseInt(xString);
            y = Integer.parseInt(yString);
        }
        catch (NumberFormatException e) {
            return new Result(false, "Invalid coordinates format!");
        }

        Tile tile = map.getTile(x, y);
        if (tile.getType().equals(TileType.Housing)) {
            animal.setInCage(true);
        }
        else {
            animal.setInCage(false);
        }

        animal.setX(x);
        animal.setY(y);
        animal.setFull(true);
        return new Result(true, "Done!");
    }

    public Result feedHay(String name) {
        PurchasedAnimal animal = null;
        if (game.getCurrentPlayer() == null || game.getCurrentPlayer().getHousings() == null) {
            return new Result(false, "No housings available.");
        }
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                if (purchasedAnimal.getName().equals(name)) animal = purchasedAnimal;
            }
        }

        if (animal == null) {
            return new Result(false, "Invalid animal name!");
        }

        if (!isPlayerNearSomething(animal.getX(), animal.getY())) {
            return new Result(false, "You are not near the animal!");
        }

        animal.addFriendshipPoints(10);
        animal.setFull(true);
        return new Result(true, "Done!");
    }

    public Result sellAnimal(String name) {
        PurchasedAnimal animal = null;
        Housing h = null;
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                if (purchasedAnimal.getName().equals(name)) {
                    animal = purchasedAnimal;
                    h = housing;
                }
            }
        }

        if (animal == null || h == null) {
            return new Result(false, "Invalid animal name!");
        }

        h.removeAnimal(animal);
        game.getCurrentPlayer().getBankAccount().deposit((int) animal.sellingPrice());
        return new Result(true, "Animal sold!");
    }

    public Result checkInventoryIntegrity() {
        StringBuilder report = new StringBuilder();
        report.append("Inventory Integrity Check:\n");

        ArrayList<Item> items = game.getCurrentPlayer().getInventory().getItems();
        boolean foundIssues = false;

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            try {
                String name = item.getName();
                ItemType type = item.getItemType();
                int number = item.getNumber();

                if (name == null) {
                    report.append("ISSUE: Item at index ").append(i).append(" has null name\n");
                    foundIssues = true;
                }
                if (type == null) {
                    report.append("ISSUE: Item at index ").append(i).append(" has null ItemType\n");
                    foundIssues = true;
                }
                if (number <= 0) {
                    report.append("WARNING: Item at index ").append(i).append(" has invalid quantity: ").append(number).append("\n");
                }

                if (item instanceof Material material) {
                    MaterialType matType = material.getMaterialType();
                    if (matType == null) {
                        report.append("ISSUE: Material '").append(name).append("' at index ").append(i).append(" has null MaterialType\n");
                        foundIssues = true;
                    }
                }

                report.append("OK: Item ").append(i).append(": ").append(name).append(" (").append(item.getClass().getSimpleName()).append(", qty: ").append(number).append(")\n");

            } catch (Exception e) {
                report.append("ERROR: Item at index ").append(i).append(" threw exception: ").append(e.getMessage()).append("\n");
                foundIssues = true;
            }
        }

        if (!foundIssues) {
            report.append("\nAll items are healthy!");
        } else {
            report.append("\nFound issues in inventory. Consider using 'cheat add item' to replace corrupted items.");
        }

        return new Result(true, report.toString());
    }

    public Result checkQuestIntegrity() {
        StringBuilder report = new StringBuilder();
        report.append("Quest and NPC Integrity Check:\n");

        boolean foundIssues = false;

        if (game.getNPCs() == null) {
            report.append("CRITICAL: Game NPCs list is null!\n");
            return new Result(true, report.toString());
        }

        for (int i = 0; i < game.getNPCs().size(); i++) {
            NPC npc = game.getNPCs().get(i);
            try {
                if (npc == null) {
                    report.append("ISSUE: NPC at index ").append(i).append(" is null\n");
                    foundIssues = true;
                    continue;
                }

                String npcName = npc.getType().name();
                HashMap<Quest, Boolean> quests = npc.getQuests();

                if (npcName == null) {
                    report.append("ISSUE: NPC at index ").append(i).append(" has null name\n");
                    foundIssues = true;
                }

                if (quests == null) {
                    report.append("ISSUE: NPC '").append(npcName != null ? npcName : "Unknown").append("' has null quest map\n");
                    foundIssues = true;
                    continue;
                }

                report.append("OK: NPC '").append(npcName).append("' has ").append(quests.size()).append(" quests\n");

                int questIndex = 0;
                for (Quest quest : quests.keySet()) {
                    try {
                        if (quest == null) {
                            report.append("  WARNING: Quest ").append(questIndex).append(" is null for NPC '").append(npcName).append("'\n");
                            foundIssues = true;
                        } else {
                            int questId = quest.getId();
                            ItemType demand = quest.getDemand();
                            ItemType reward = quest.getReward();
                            Boolean isCompleted = quests.get(quest);

                            if (demand == null) {
                                report.append("  ISSUE: Quest ").append(questId).append(" has null demand\n");
                                foundIssues = true;
                            }

                            if (isCompleted == null) {
                                report.append("  ISSUE: Quest ").append(questId).append(" has null completion status\n");
                                foundIssues = true;
                            }

                            report.append("  OK: Quest ID ").append(questId).append(", completed: ").append(isCompleted).append("\n");
                        }
                        questIndex++;
                    } catch (Exception e) {
                        report.append("  ERROR: Quest ").append(questIndex).append(" threw exception: ").append(e.getMessage()).append("\n");
                        foundIssues = true;
                    }
                }

            } catch (Exception e) {
                report.append("ERROR: NPC at index ").append(i).append(" threw exception: ").append(e.getMessage()).append("\n");
                foundIssues = true;
            }
        }

        if (!foundIssues) {
            report.append("\nAll NPCs and quests are healthy!");
        } else {
            report.append("\nFound issues with NPCs/quests. This might cause quest-related commands to fail.");
        }

        return new Result(true, report.toString());
    }

    public Result debugQuest(String idString) {
        StringBuilder debug = new StringBuilder();
        debug.append("=== QUEST DEBUG INFORMATION ===\n");

        debug.append("Game object: ").append(game != null ? "OK" : "NULL").append("\n");
        if (game == null) {
            return new Result(true, debug.toString());
        }

        debug.append("Current Player: ").append(game.getCurrentPlayer() != null ? "OK" : "NULL").append("\n");
        debug.append("NPCs list: ").append(game.getNPCs() != null ? "OK" : "NULL").append("\n");
        debug.append("Days passed: ").append(game.getDaysPassed()).append("\n");

        if (game.getNPCs() == null) {
            return new Result(true, debug.toString());
        }

        debug.append("Number of NPCs: ").append(game.getNPCs().size()).append("\n");

        int id;
        try {
            id = Integer.parseInt(idString);
            debug.append("Quest ID to find: ").append(id).append("\n");
        } catch (NumberFormatException e) {
            debug.append("ERROR: Invalid quest ID format: ").append(idString).append("\n");
            return new Result(true, debug.toString());
        }

        NPC targetNpc = null;
        Quest targetQuest = null;
        Boolean questCompleted = null;

        for (NPC npc : game.getNPCs()) {
            if (npc != null && npc.getQuests() != null) {
                for (Quest quest : npc.getQuests().keySet()) {
                    if (quest != null && quest.getId() == id) {
                        targetNpc = npc;
                        targetQuest = quest;
                        questCompleted = npc.getQuests().get(quest);
                        break;
                    }
                }
                if (targetQuest != null) break;
            }
        }

        debug.append("\n=== TARGET QUEST ANALYSIS ===\n");
        if (targetQuest == null) {
            debug.append("Quest not found!\n");
        } else {
            debug.append("Quest found!\n");
            debug.append("NPC: ").append(targetNpc.getType().name()).append("\n");
            debug.append("Quest ID: ").append(targetQuest.getId()).append("\n");
            debug.append("Completed: ").append(questCompleted).append("\n");

            if (targetQuest.getDemand() != null) {
                debug.append("Demand: ").append(targetQuest.getDemand().getName()).append(" x").append(targetQuest.getDemandAmount()).append("\n");

                Item demandedItem = game.getCurrentPlayer().getInventory().findItemByType(targetQuest.getDemand());
                if (demandedItem != null) {
                    debug.append("Player has: ").append(demandedItem.getNumber()).append(" ").append(demandedItem.getName()).append("\n");
                    debug.append("Enough items: ").append(demandedItem.getNumber() >= targetQuest.getDemandAmount() ? "YES" : "NO").append("\n");
                } else {
                    debug.append("Player has: 0 ").append(targetQuest.getDemand().getName()).append("\n");
                    debug.append("Enough items: NO\n");
                }
            } else {
                debug.append("Demand: NULL (ERROR!)\n");
            }

            if (targetQuest.getReward() != null) {
                debug.append("Reward: ").append(targetQuest.getReward().getName()).append(" x").append(targetQuest.getRewardAmount()).append("\n");
            } else {
                debug.append("Reward: Friendship level upgrade\n");
            }

            boolean isVisible = isQuestVisible(targetQuest, targetNpc);
            debug.append("Quest visible: ").append(isVisible ? "YES" : "NO").append("\n");

            if (!isVisible) {
                debug.append("Visibility requirements:\n");

                HashMap<Quest, Boolean> quests = targetNpc.getQuests();
                List<Quest> questsList = new ArrayList<>(quests.keySet());
                int questIndex = questsList.indexOf(targetQuest);

                debug.append("  Quest index: ").append(questIndex).append("\n");

                if (questIndex == 1) {
                    Integer friendshipLevel = targetNpc.getFriendShipLevelWith(game.getCurrentPlayer());
                    debug.append("  Needs friendship level >= 1, current: ").append(friendshipLevel != null ? friendshipLevel : "NULL").append("\n");
                } else if (questIndex == 2) {
                    debug.append("  NPC type: ").append(targetNpc.getType().name()).append("\n");
                    debug.append("  Days requirement: ");
                    switch (targetNpc.getType()) {
                        case Abigail -> debug.append("20, current: ").append(game.getDaysPassed()).append("\n");
                        case Harvey -> debug.append("10, current: ").append(game.getDaysPassed()).append("\n");
                        case Lia -> debug.append("15, current: ").append(game.getDaysPassed()).append("\n");
                        case Robin -> debug.append("25, current: ").append(game.getDaysPassed()).append("\n");
                        case Sebastian -> debug.append("15, current: ").append(game.getDaysPassed()).append("\n");
                        default -> debug.append("No requirement for this NPC type\n");
                    }
                }
            }
        }

        debug.append("\n=== ALL NPCS OVERVIEW ===\n");
        int npcIndex = 0;
        for (NPC npc : game.getNPCs()) {
            debug.append("NPC ").append(npcIndex).append(": ");

            if (npc == null) {
                debug.append("NULL\n");
                npcIndex++;
                continue;
            }

            debug.append(npc.getType() != null ? npc.getType().name() : "NULL").append(" - ");

            if (npc.getQuests() != null) {
                debug.append(npc.getQuests().size()).append(" quests: ");

                for (Quest quest : npc.getQuests().keySet()) {
                    if (quest != null) {
                        debug.append("ID").append(quest.getId());
                        if (quest.getId() == id) debug.append("(TARGET)");
                        debug.append(" ");
                    }
                }
                debug.append("\n");
            } else {
                debug.append("NULL quests\n");
            }
            npcIndex++;
        }

        return new Result(true, debug.toString());
    }

    public Result debugQuestVisibility() {
        StringBuilder debug = new StringBuilder();
        debug.append("=== QUEST VISIBILITY DEBUG ===\n");

        if (game.getNPCs() == null) {
            debug.append("No NPCs available!\n");
            return new Result(true, debug.toString());
        }

        for (NPC npc : game.getNPCs()) {
            if (npc == null || npc.getQuests() == null) {
                continue;
            }

            debug.append("NPC: ").append(npc.getType().name()).append("\n");
            HashMap<Quest, Boolean> quests = npc.getQuests();

            for (Quest quest : quests.keySet()) {
                if (quest != null) {
                    Boolean isCompleted = quests.get(quest);
                    boolean isVisible = isQuestVisible(quest, npc);
                    debug.append("  Quest ID ").append(quest.getId())
                        .append(": Completed=").append(isCompleted)
                        .append(", Visible=").append(isVisible).append("\n");
                }
            }
        }

        return new Result(true, debug.toString());
    }

    public Result debugTalkHistory() {
        StringBuilder debug = new StringBuilder();
        debug.append("=== TALK HISTORY DEBUG ===\n");

        Player currentPlayer = game.getCurrentPlayer();
        debug.append("Current player: ").append(currentPlayer.getUsername()).append("\n");
        debug.append("Number of talks in current player's list: ").append(currentPlayer.getTalks().size()).append("\n");

        for (int i = 0; i < currentPlayer.getTalks().size(); i++) {
            Talk talk = currentPlayer.getTalks().get(i);
            debug.append("Talk ").append(i).append(": ");
            if (talk.getSender() != null && talk.getReceiver() != null) {
                debug.append(talk.getSender().getUsername())
                    .append(" -> ").append(talk.getReceiver().getUsername())
                    .append(": ").append(talk.getMessage()).append("\n");
            } else {
                debug.append("NULL sender or receiver\n");
            }
        }

        return new Result(true, debug.toString());
    }

    public Result showBalance() {
        return new Result(true, "Your balance is " + game.getCurrentPlayer().getBankAccount().getBalance());
    }

    public Result goToStoreMenu() {
        App.getInstance().setCurrentMenu(Menu.StoreMenu);
        return new Result(true, "You are now in store menu!");
    }

    // In useTool(Tile targetTile)
    public Result useTool(Tile targetTile) {
        Player player = game.getCurrentPlayer();
        if (targetTile == null) {
            return new Result(false, "Invalid target tile.");
        }

        Tool currentTool = player.getCurrentTool();
        if (currentTool != null && currentTool.getToolType().name().contains("Axe") && targetTile.isPartOfGiantCrop()) {
            if (player.getInventory().isFull()) {
                return new Result(false, "Your inventory is full.");
            }

            int rootX = targetTile.getGiantCropRootX();
            int rootY = targetTile.getGiantCropRootY();
            Tile rootTile = game.getMap().getTile(rootX, rootY);
            Crop crop = (Crop) rootTile.getPlant();
            if (!(crop.getCropType() instanceof CropType)) {
                return new Result(false, "This giant plant is not a farm crop.");
            }
            CropType cropType = (CropType) crop.getCropType();

            // Give a larger, random yield (e.g., 15-21)
            int yield = 15 + new Random().nextInt(7);
            player.getInventory().addItem(new Crop(cropType, yield));

            // Clear all four tiles that made up the giant crop
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    Tile partOfCrop = game.getMap().getTile(rootX + i, rootY + j);
                    partOfCrop.resetGiantCropStatus();
                    partOfCrop.setPlant(null);
                    partOfCrop.setType(TileType.Shoveled);
                }
            }

            player.addSkillExperience(Skills.Farming, 50);
            return new Result(true, "You harvested a giant " + cropType.getName() + " and got " + yield + "!");
        }

        return player.handleToolUse(targetTile);
    }

    public Result plantItem(Item itemToPlant, Tile targetTile) {
        Player player = game.getCurrentPlayer();
        if (!(itemToPlant instanceof Seed)) {
            return new Result(false, "You can only plant seeds!");
        }

        if (targetTile.getType() != TileType.Shoveled || targetTile.getPlant() != null || targetTile.getSeed() != null) {
            return new Result(false, "Cannot plant here.");
        }

        Seed seed = (Seed) itemToPlant;
        ItemType plantable = ((ForagingSeedType) seed.getItemType()).getPlantType();

        if (plantable instanceof CropType) {
            Crop newCrop = new Crop(plantable, 1);
            newCrop.setCurrentStage(1);
            targetTile.setPlant(newCrop);
            targetTile.setType(TileType.Planted);
        } else if (plantable instanceof TreeType) {
            Fruit newFruit = new Fruit(((TreeType) plantable).getProduct(), 1);
            newFruit.setCurrentStage(1);
            targetTile.setPlant(newFruit);
            targetTile.setTree(new Tree((TreeType) plantable));
            targetTile.setType(TileType.Tree);
        } else {
            return new Result(false, "This seed cannot be planted.");
        }

        player.getInventory().remove2(itemToPlant.getName(), 1);
        return new Result(true, itemToPlant.getName() + " planted.");
    }

    public Result harvestFruit(Tile targetTile) {
        Player currentPlayer = game.getCurrentPlayer();
        if (targetTile == null || targetTile.getType() != TileType.Tree) {
            return new Result(false, "Nothing to harvest here.");
        }

        if (targetTile.getPlant() instanceof Fruit fruit && fruit.isReadyToHarvest()) {
            if (fruit.hasBeenHarvestedToday()) {
                return new Result(false, "You already harvested this today.");
            }
            if (currentPlayer.getInventory().isFull()) {
                return new Result(false, "Your inventory is full.");
            }

            int harvestAmount = fruit.getTreeType().getHarvestCycle();
            Fruit harvestedFruit = new Fruit(fruit.getFruitType(), harvestAmount);
            currentPlayer.getInventory().addItem(harvestedFruit);

            fruit.setHasBeenHarvestedToday(true);
            return new Result(true, "You harvested " + harvestAmount + " " + harvestedFruit.getName() + ".");
        }

        return new Result(false, "The fruit isn't ripe yet.");
    }

    public Result collectAnimalProduct(String animalName) {
        PurchasedAnimal animal = null;
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                if (purchasedAnimal.getName().equals(animalName)) animal = purchasedAnimal;
            }
        }

        if (animal == null) {
            return new Result(false, "Invalid animal name!");
        }

        if (!isPlayerNearSomething(animal.getX(), animal.getY())) {
            return new Result(false, "You are not near the animal!");
        }

        if (!animal.getWasFull()) {
            return new Result(false, "The animal was not fed and cannot produce products!");
        }

        if (animal.getCollected()) {
            return new Result(false, "You have already collected products from this animal today!");
        }

        animal.setCollected(true);

        if (animal.getProducedProductType()) {
            AnimalProductType firstProduct = animal.getType().getProducts().get(0);
            AnimalProductType secondProduct = animal.getType().getProducts().get(1);

            AnimalProduct firstProductInstance = new AnimalProduct(firstProduct, 1);
            AnimalProduct secondProductInstance = new AnimalProduct(secondProduct, 1);

            game.getCurrentPlayer().getInventory().addItem(firstProductInstance);
            game.getCurrentPlayer().getInventory().addItem(secondProductInstance);

            return new Result(true, "You collected " + firstProductInstance.getName() + " and " + secondProductInstance.getName() + " from " + animalName + "!");
        }

        AnimalProductType firstProduct = animal.getType().getProducts().get(0);
        AnimalProduct firstProductInstance = new AnimalProduct(firstProduct, 1);
        game.getCurrentPlayer().getInventory().addItem(firstProductInstance);

        return new Result(true, "You collected " + firstProductInstance.getName() + " from " + animalName + "!");
    }

    public Result showCollectableProducts(String animalName) {
        PurchasedAnimal animal = null;
        for (Housing housing : game.getCurrentPlayer().getHousings()) {
            for (PurchasedAnimal purchasedAnimal : housing.getOccupants()) {
                if (purchasedAnimal.getName().equals(animalName)) animal = purchasedAnimal;
            }
        }

        if (animal == null) {
            return new Result(false, "Invalid animal name!");
        }

        if (!animal.getWasFull()) {
            return new Result(false, "The animal was not fed and cannot produce products!");
        }

        if (animal.getCollected()) {
            return new Result(false, "You have already collected products from this animal today!");
        }

        if (animal.getProducedProductType()) {
            return new Result(true, "You can collect\n" + animal.getType().getProducts().get(0).getName() + "\n" + animal.getType().getProducts().get(1).getName() + "\nfrom " + animalName + "!");
        }

        return new Result(true, "You can collect\n" + animal.getType().getProducts().get(0).getName() + "\nfrom " + animalName + "!");
    }

    // main/controller/GameMenuController.java

    public Result craftItem(String recipeName) {
        Player player = game.getCurrentPlayer();

        // Find the recipe enum by its display name
        CraftingRecipes recipeType = Arrays.stream(CraftingRecipes.values())
            .filter(r -> r.getName().equalsIgnoreCase(recipeName))
            .findFirst()
            .orElse(null);

        if (recipeType == null) {
            return new Result(false, "No crafting recipe found with the name: " + recipeName);
        }

        // 1. Check if the player has learned this recipe
        boolean hasRecipe = player.getCraftingRecipe().stream()
            .anyMatch(playerRecipe -> playerRecipe.getRecipeType() == recipeType);

        if (!hasRecipe) {
            return new Result(false, "You have not learned this recipe yet.");
        }

        // 2. Check if the player's inventory is full
        if (player.getInventory().isFull()) {
            return new Result(false, "Your inventory is full. Make some space first.");
        }

        // 3. Check if the player has the required ingredients
        for (Map.Entry<ItemType, Integer> entry : recipeType.getIngredients().entrySet()) {
            ItemType requiredItemType = entry.getKey();
            int requiredAmount = entry.getValue();
            Item playerItem = player.getInventory().findItemByType(requiredItemType);

            if (playerItem == null || playerItem.getNumber() < requiredAmount) {
                return new Result(false, "You don't have enough " + requiredItemType.getName() + ". Need " + requiredAmount + ".");
            }
        }

        // 4. All checks passed: Deduct ingredients
        for (Map.Entry<ItemType, Integer> entry : recipeType.getIngredients().entrySet()) {
            player.getInventory().remove2(entry.getKey().getName(), entry.getValue());
        }

        // 5. Create and add the crafted item using ItemFactory
        ItemType productType = recipeType.getProduct();
        try {
            Item craftedItem = ItemFactory.createItemOrThrow(productType.getName(), 1);
            player.getInventory().addItem(craftedItem);
        } catch (Exception e) {
            // This will catch if ItemFactory doesn't know how to create the item
            return new Result(false, "Error creating crafted item: " + productType.getName());
        }

        return new Result(true, "Successfully crafted 1 " + productType.getName() + ".");
    }

    public Result cookFood(String recipeName) {
        Player player = game.getCurrentPlayer();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());

        if (currentTile.getType() != TileType.House) {
            return new Result(false, "You can only cook inside your house.");
        }

        CookingRecipeType recipeType = Arrays.stream(CookingRecipeType.values())
            .filter(r -> r.getDisplayName().equalsIgnoreCase(recipeName))
            .findFirst()
            .orElse(null);

        if (recipeType == null) {
            return new Result(false, "That recipe doesn't exist.");
        }

        boolean hasRecipe = player.getCookingRecipe().stream()
            .anyMatch(playerRecipe -> playerRecipe.getRecipeType() == recipeType);

        if (!hasRecipe) {
            return new Result(false, "You haven't learned this recipe yet.");
        }

        // --- NEW: Detailed Ingredient Check ---
        StringBuilder missingIngredients = new StringBuilder();
        Map<ItemType, Integer> totalIngredients = new HashMap<>();
        player.getInventory().getItems().forEach(item -> totalIngredients.merge(item.getItemType(), item.getNumber(), Integer::sum));
        player.getHouseRefrigerator().getItems().forEach(item -> totalIngredients.merge(item.getItemType(), item.getNumber(), Integer::sum));

        for (Map.Entry<ItemType, Integer> entry : recipeType.getIngredients().entrySet()) {
            int requiredAmount = entry.getValue();
            int availableAmount = totalIngredients.getOrDefault(entry.getKey(), 0);
            if (availableAmount < requiredAmount) {
                missingIngredients.append("\n- ").append(entry.getKey().getName()).append(" (Need: ").append(requiredAmount).append(", Have: ").append(availableAmount).append(")");
            }
        }

        if (missingIngredients.length() > 0) {
            return new Result(false, "You are missing ingredients:" + missingIngredients.toString());
        }
        // --- END: Detailed Ingredient Check ---


        // All checks passed, now deduct ingredients
        for (Map.Entry<ItemType, Integer> entry : recipeType.getIngredients().entrySet()) {
            int amountLeftToDeduct = entry.getValue();
            Item itemInInventory = player.getInventory().findItemByType(entry.getKey());
            if (itemInInventory != null) {
                int amountFromInventory = Math.min(amountLeftToDeduct, itemInInventory.getNumber());
                player.getInventory().remove2(itemInInventory.getName(), amountFromInventory);
                amountLeftToDeduct -= amountFromInventory;
            }
            if (amountLeftToDeduct > 0) {
                player.getHouseRefrigerator().remove(entry.getKey(), amountLeftToDeduct);
            }
        }

        // Create the food and store it temporarily
        this.lastCookedFood = new Food(recipeType.getFoodType(), 1);
        return new Result(true, "Successfully cooked " + lastCookedFood.getName());
    }

    public Food getJustCookedFood() {
        return this.lastCookedFood;
    }

    public Result placeCookedFood(String destination) {
        Player player = game.getCurrentPlayer();
        if (lastCookedFood == null) {
            return new Result(false, "There is no cooked food to place.");
        }

        if ("inventory".equalsIgnoreCase(destination)) {
            if (player.getInventory().isFull()) {
                return new Result(false, "Inventory is full!");
            }
            player.getInventory().addItem(lastCookedFood);
        } else if ("refrigerator".equalsIgnoreCase(destination)) {
            if (player.getHouseRefrigerator().isFull()) {
                return new Result(false, "Refrigerator is full!");
            }
            player.getHouseRefrigerator().putItem(lastCookedFood);
        } else {
            return new Result(false, "Invalid destination.");
        }

        String message = lastCookedFood.getName() + " placed in " + destination + ".";
        this.lastCookedFood = null; // Clear the temporary item
        return new Result(true, message);
    }

    public Result moveRandomFoodToRefrigerator() {
        Player player = game.getCurrentPlayer();
        List<Item> foodInInventory = player.getInventory().getItems().stream()
            .filter(item -> item instanceof Food)
            .collect(Collectors.toList());

        if (foodInInventory.isEmpty()) {
            return new Result(false, "You have no food in your inventory to move.");
        }

        if (player.getHouseRefrigerator().isFull()) {
            boolean canStack = false;
            for (Item food : foodInInventory) {
                if (player.getHouseRefrigerator().findItemByType(food.getItemType()) != null) {
                    canStack = true;
                    break;
                }
            }
            if (!canStack) {
                return new Result(false, "The refrigerator is full and cannot accept new items.");
            }
        }

        // Select a random food item from the list
        Random random = new Random();
        Item itemToMove = foodInInventory.get(random.nextInt(foodInInventory.size()));

        // Create a new food object for the refrigerator to avoid reference issues
        Food foodForFridge = new Food((FoodType) itemToMove.getItemType(), 1);
        player.getHouseRefrigerator().putItem(foodForFridge);

        // Remove one from the inventory
        player.getInventory().remove2(itemToMove.getName(), 1);

        return new Result(true, "Moved 1 " + itemToMove.getName() + " to the refrigerator.");
    }

    public Result cheatAddCraftingRecipe(String recipeName) {
        Player player = game.getCurrentPlayer();

        // Find the recipe enum by its display name (more user-friendly)
        CraftingRecipes recipeType = Arrays.stream(CraftingRecipes.values())
            .filter(r -> r.getName().equalsIgnoreCase(recipeName))
            .findFirst()
            .orElse(null);

        if (recipeType == null) {
            return new Result(false, "No crafting recipe found with that name.");
        }

        // Check if the player already knows the recipe
        boolean alreadyKnown = player.getCraftingRecipe().stream()
            .anyMatch(r -> r.getRecipeType() == recipeType);

        if (alreadyKnown) {
            return new Result(false, "You already know this recipe.");
        }

        player.addCraftingRecipe(new CraftingRecipe(recipeType, 1));
        return new Result(true, "Learned crafting recipe: " + recipeType.getName());
    }

    public Result cheatAddCookingRecipe(String recipeName) {
        Player player = game.getCurrentPlayer();

        // Find the recipe enum by its display name
        CookingRecipeType recipeType = Arrays.stream(CookingRecipeType.values())
            .filter(r -> r.getDisplayName().equalsIgnoreCase(recipeName))
            .findFirst()
            .orElse(null);

        if (recipeType == null) {
            return new Result(false, "No cooking recipe found with that name.");
        }

        // Check if the player already knows the recipe
        boolean alreadyKnown = player.getCookingRecipe().stream()
            .anyMatch(r -> r.getRecipeType() == recipeType);

        if (alreadyKnown) {
            return new Result(false, "You already know this recipe.");
        }

        player.addCookingRecipe(new CookingRecipe(recipeType));
        return new Result(true, "Learned cooking recipe: " + recipeType.getDisplayName());
    }

    public Result placeMachine(CraftingMachine machine, int tileX, int tileY) {
        if (map == null) {
            map = game != null ? game.getMap() : null;
        }
        if (map == null) {
            return new Result(false, "Map is still loading. Please wait.");
        }
        if (!map.inBounds(tileX, tileY)) {
            return new Result(false, "Cannot place outside the farm.");
        }
        Tile targetTile = map.getTile(tileX, tileY);
        if (targetTile.getPlacedMachine() != null || !targetTile.getType().isReachable()) {
            return new Result(false, "You cannot place a machine there.");
        }

        // Create the PlacedMachine state object
        PlacedMachine placedMachine = new PlacedMachine((CraftingMachineType) machine.getItemType());
        targetTile.setPlacedMachine(placedMachine);

        // Remove the item from inventory
        game.getCurrentPlayer().getInventory().remove2(machine.getName(), 1);

        return new Result(true, machine.getName() + " placed successfully.");
    }

    public Result startArtisanProcess(PlacedMachine machine, ArtisanProductType recipe) {
        Player player = game.getCurrentPlayer();
        Item ingredient = player.getInventory().findItemByType(recipe.getIngredients().keySet().iterator().next());

        if (ingredient == null || ingredient.getNumber() < 1) {
            return new Result(false, "You don't have the required ingredient in your inventory.");
        }

        player.getInventory().remove2(ingredient.getName(), 1);
        machine.startProcessing(ingredient, recipe);

        return new Result(true, "Processing started for " + recipe.getName());
    }

    public Result collectArtisanProduct(PlacedMachine machine) {
        Player player = game.getCurrentPlayer();
        if (player.getInventory().isFull()) {
            return new Result(false, "Your inventory is full.");
        }

        Good product = machine.collectProduct();
        if (product != null) {
            player.getInventory().addItem(product);
            return new Result(true, "Collected 1 " + product.getName());
        }
        return new Result(false, "Nothing to collect.");
    }

    public Result finishArtisanProcessNow(PlacedMachine machine) {
        Player player = game.getCurrentPlayer();
        if (player.getEnergy() < 100) {
            return new Result(false, "Not enough energy to finish instantly.");
        }
        if (!machine.isProcessing()) {
            return new Result(false, "Nothing is being processed.");
        }

        player.reduceEnergy(100);
        // Manually set progress to finish
        while (!machine.isDone()) {
            machine.updateProgress();
        }

        return new Result(true, "Process finished instantly!");
    }

    public Result cancelArtisanProcess(PlacedMachine machine) {
        if (!machine.isProcessing()) {
            return new Result(false, "Nothing to cancel.");
        }

        // Return the ingredient to the player's inventory
        Item ingredient = machine.getInput();
        if (ingredient != null) {
            game.getCurrentPlayer().getInventory().addItem(ingredient);
        }

        machine.cancelProcessing();
        return new Result(true, "Process cancelled.");
    }

    public void removePlayer(String username) {
        if (game != null) {
            System.out.println("[CONTROLLER] Removing player: " + username);
            game.removePlayer(username);
        } else {
            System.err.println("[CONTROLLER] Game is null. Cannot remove player.");
        }
    }
}
