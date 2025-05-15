package controllers;


import enums.Menu;
import enums.design.FarmThemes;
import enums.design.NPCType;
import enums.design.Season;
import enums.items.CookingRecipes;
import enums.items.FoodType;
import enums.items.MaterialType;
import enums.design.Weather;
import enums.items.ToolType;
import enums.design.*;
import enums.items.*;
import enums.regex.GameMenuCommands;
import models.App;
import models.Game;
import models.Result;
import models.User;
import models.*;
import models.building.House;
import models.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


public class GameMenuController {
    private final App app = App.getInstance();
    private Game game;
    private GameMap map;

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
        player1.setOriginX(10);
        player1.setOriginY(10);
        loggedInUser.setCurrentPlayer(player1);
        players.add(loggedInUser);

        Player player2 = new Player(user1.getUsername(), user1.getGender());
        player2.setOriginX(80);
        player2.setOriginY(10);
        user1.setCurrentPlayer(player2);
        players.add(user1);

        Player player3 = new Player(user2.getUsername(), user2.getGender());
        player3.setOriginX(10);
        player3.setOriginY(80);
        user2.setCurrentPlayer(player3);
        players.add(user2);

        Player player4 = new Player(user3.getUsername(), user3.getGender());
        player4.setOriginX(80);
        player4.setOriginY(80);
        user3.setCurrentPlayer(player4);
        players.add(user3);

        Game newGame = new Game(players);
        newGame.setMainPlayer(loggedInUser);
        App.getInstance().addGame(newGame);
        App.getInstance().setCurrentGame(newGame);
        this.setGame(game);
        return new Result(true, "Now Choose your map!");
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
            // todo : exit the game and go to game menu
            return new Result(true, "You are in game menu now");
        }
        else{
            return new Result(false, "You are not the creator of this game");
        }
    }

    public Result terminateGame() {
        return new Result(true, "Game terminated");
    }

    public Result switchTurn() {
        boolean isPlayerAvailable = game.switchCurrentPlayer();
        if(isPlayerAvailable){
            game.getCurrentPlayer().showNotifs();
            game.getCurrentPlayer().resetNotifs();
            return new Result(true, "Game switched to " + game.getCurrentPlayer().getUsername() + " ");
        }
        return new Result(false, "you can not switch to other players");
    } 

    public Result showTime() {
        return new Result(true, "It's " + game.getTime().hour() + "O'clock");
    }

    public Result showDate() {
        return new Result(true,"Season: " + game.getDate().currentSeason().name() +
                "\nDay: " + game.getDate().currentDay());
    }

    public Result showDateAndTime() {
        return new Result(true, showTime().Message() + "\n" + showDate().Message());
    }

    public Result showDayOfWeek() {
        return new Result(true,"It's " + game.getDate().currentWeekday().name());
    }

    public Result changeTime(int hours) {
        if (hours <= 0) {
            return new Result(false, "Hours must be positive");
        }
        Time time = game.getTime();
        Date date = game.getDate();

        int originalHour = time.hour();
        int daysPassed = time.addHours(hours);


        if (daysPassed > 0) {
            int seasonsPassed = date.addDays(daysPassed);
            this.onDayPassed(daysPassed);
        }

        return new Result(true, "time changed!");
    }

    public Result changeDate(int days) {
        if (days <= 0) {
            return new Result(false, "Days must be positive");
        }
        Date date = game.getDate();
        int originalDay = date.currentDay();
        Season originalSeason = date.currentSeason();

        int seasonsPassed = date.addDays(days);

        game.getTime().setHour(Time.DAY_START);

        this.onSeasonChanged(seasonsPassed);

        return new Result(true,"date changed!");
    }

    public Result showSeason() {
        return new Result(true,game.getDate().currentSeason().name());
    }

    public Result lightningHandling() {
        return new Result(true, "Lightning handling");
    }

    public Result cheatLightning(int x, int y) {
        return new Result(true, "Lightning handling");
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
        return new Result(true, "Tomorrow weather changed to" + game.getTomorrowWeather().name());
    }

    public Result buildGreenHouse() {
        return new Result(true, "Green house");
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
        ArrayList<Tile> path = map.findWalkPath(currentPlayer.currentX(), currentPlayer.currentY(), x, y);

        if (path == null) {
            return new Result(false, "You can not walk there!");
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

        return new Result(true, "You made it!");
    }

    public Result printMap(String xString, String yString, String sizeString) {
        int x = Integer.parseInt(xString);
        int y = Integer.parseInt(yString);
        int size = Integer.parseInt(sizeString);

        return new Result(true, map.showMap(x, y, size));
    }

    public Result mapInfo() {
        return new Result(true, "Map Info");
    }

    public Result energyShow() {
        int playerEnergy = game.getCurrentPlayer().getEnergy();
        return new Result(true, "Player energy: " + playerEnergy);
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

        return new Result(true, "You gifted " + itemName + " to " + NPCName);
    }

    public Result showQuests() {
        StringBuilder stringBuilder = new StringBuilder();
        for (NPC npc : game.getNPCs()) {
            java.util.HashMap<Quest, Boolean> quests = npc.getQuests();
            List<Quest> questsList = new ArrayList<>(quests.keySet());
            stringBuilder.append(questsList.getFirst().toString());
            if (npc.getFriendShipLevelWith(game.getCurrentPlayer()) >= 1) {
                stringBuilder.append(questsList.get(1).toString());
            }

            if (npc.getType() == NPCType.Abigail && game.getDaysPassed() >= 20) {
                stringBuilder.append(questsList.get(2).toString());
            }
            else if (npc.getType() == NPCType.Harvey && game.getDaysPassed() >= 10) {
                stringBuilder.append(questsList.get(2).toString());
            }
            else if (npc.getType() == NPCType.Lia && game.getDaysPassed() >= 15) {
                stringBuilder.append(questsList.get(2).toString());
            }
            else if (npc.getType() == NPCType.Robin && game.getDaysPassed() >= 25) {
                stringBuilder.append(questsList.get(2).toString());
            }
            else if (npc.getType() == NPCType.Sebastian && game.getDaysPassed() >= 15) {
                stringBuilder.append(questsList.get(2).toString());
            }
        }

        return new Result(true, stringBuilder.toString());
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

        Talk talk = new Talk(player, message);
        currentPlayer.addTalk(talk);

        Friendship friendship = game.getFriendshipByPlayers(currentPlayer, player);
        friendship.addFriendshipPoints(10);

        if (player.equals(currentPlayer.getSpouse())) {
            player.addEnergy(50);
            currentPlayer.addEnergy(50);
        }

        return new Result(true, game.getCurrentPlayer().getUsername() + " sent a message to " + player.getUsername() + ":\n" + message);
    }

    public Result talkHistory(String username) {
        Player player = game.getUserByUsername(username).getPlayer();
        if (player == null) {
            return new Result(false, "Player not found!");
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        for (Talk talk : player.getTalks()) {
            if (talk.getReceiver().equals(player)) stringBuilder.append(talk.toString());
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

        if (receiver.equals(game.getCurrentPlayer().getSpouse())) {
            receiver.addEnergy(50);
            game.getCurrentPlayer().addEnergy(50);
        }

        Gift gift = new Gift(game.getCurrentPlayer(), receiver, item, itemAmount);
        receiver.addGift(gift);
        // todo: remove from giver's inventory and add to receiver's
        receiver.addNotif(game.getCurrentPlayer(), game.getCurrentPlayer().getUsername() + " has gifted something to you!");
        return new Result(true, "You gifted " + itemName + " to " + username);
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
            if (gift.getSender().getUsername().equals(username)) {
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
        Player player = game.getUserByUsername(username).getPlayer();
        if (player == null) {
            return new Result(false, "Invalid username!");
        }
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near someone to give flower to them!");
        }

        Friendship friendship = game.getFriendshipByPlayers(game.getCurrentPlayer(), player);
        if (friendship.getFriendshipPoints() < 300) {
            return new Result(false, "Your friendship points must at least be 300!");
        }

        if (player.equals(game.getCurrentPlayer().getSpouse())) {
            player.addEnergy(50);
            game.getCurrentPlayer().addEnergy(50);
        }

        // todo: remove from giver's inventory and add to receiver's
        friendship.setFriendshipLevel(3);
        player.addNotif(game.getCurrentPlayer(), game.getCurrentPlayer().getUsername() + " has flowered you!");        
        return new Result(true, "You have flowered " + username + "and your friendship level is now 3!");
    }

    public Result askMarriage(String username) {
        Player player = game.getUserByUsername(username).getPlayer();
        if (player == null) {
            return new Result(false, "Invalid username!");
        }
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near someone to ask them!");
        }

        Friendship friendship = game.getFriendshipByPlayers(game.getCurrentPlayer(), player);
        if (friendship.getFriendshipPoints() < 400) {
            return new Result(false, "Your friendship points must at least be 400!");
        }
        if (game.getCurrentPlayer().getGender().equals(player.getGender())) {
            return new Result(false, "You can't be GAY in this game!");
        }

        // handle inventory
        player.addNotif(game.getCurrentPlayer(), game.getCurrentPlayer().getUsername() + " has proposed to you!");
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
            player.addNotif(game.getCurrentPlayer(), "You are now married to " + player.getUsername());

            return new Result(true, "You are happily married!");
        }
        else {
            friendship.resetFriendship();
            player.addNotif(game.getCurrentPlayer(), "You have been dumped! Go hit the gym!");
            return new Result(true, "You have rejected the proposal!");
        }
    }

    public Result goToTradeMenu() {
        app.setCurrentMenu(Menu.TradeMenu);
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
        for(Item item: player.getInventory().getItems()){
            items.append(item.getName() + " x" + item.getNumber() + ", ");
        }
        items.delete(items.length() - 2, items.length());
        return new Result(true, items.toString());
    }
    public Result removeItemFromInventory(String itemName, String itemNumberStr) {
        // todo : handle trim in view for now
        // todo : calculate return money
        Inventory inventory = game.getCurrentPlayer().getInventory();
        int itemNumber;
        Item item;
        if ((item = findItem(itemName, inventory.getItems())) == null) {
            return new Result(false, "Item not found");
        }

        if (itemNumberStr != null && !itemNumberStr.isEmpty()) {
            itemNumber = Integer.parseInt(itemNumberStr);
            item.setNumber(item.getNumber() - itemNumber);
            return new Result(true, "x" + itemNumber + item.getName() + " has been removed");
        } else {
            inventory.getItems().remove(item);
            inventory.addNumOfItems(-1);
            return new Result(true, "Item removed from inventory");
        }
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
        info.append("Name: " + cropType.name()).append("\nSource: " + cropType.getSeedSource()).append("\nStages: ");
        for(Integer stage : cropType.getGrowthStages()){
            info.append(stage).append("-");
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
        for(Season season : cropType.getSeasons()){
            info.append(season.name()).append(", ");
        }
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

    public void fishingAndDisplay(ToolType pole) {
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

    public Result showPlant(int x, int y){
        GameMap map = game.getMap();
        Tile targetTile = map.getTile(x,y);
        StringBuilder plantInfo = new StringBuilder();
        if(targetTile.getType().equals(TileType.Planted) || targetTile.getType().equals(TileType.Tree)) {
            if(targetTile.getPlant() instanceof Crop){
                plantInfo.append("Name: " + ((Crop) targetTile.getPlant()).getCropType().name());
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

        if(fertilizer.getMaterialName().equals(MaterialType.BasicRetainingSoil.getDisplayName())){
            targetTile.getPlant().setFertilizedToday(true);
        }
        else if(fertilizer.getMaterialName().equals(MaterialType.QualityRetainingSoil.getDisplayName())){
            targetTile.getPlant().setFertilizedToday(true);
            targetTile.getPlant().growFaster();
        }
        else if(fertilizer.getMaterialName().equals(MaterialType.DeluxeRetainingSoil.getDisplayName())){
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

    public Result showCraftingRecipes(){
        Player player = game.getCurrentPlayer();
        ArrayList<CraftingRecipe> playerCraftingRecipes = player.getCraftingRecipe();
        StringBuilder recipeString = new StringBuilder();
        recipeString.append(player.getUsername() + "'s crafting recipes:\n");
        for(CraftingRecipe craftingRecipe : playerCraftingRecipes) {
            recipeString.append(craftingRecipe.getRecipeType().getDisplayName() + "\n");
        }
        recipeString.deleteCharAt(recipeString.length() - 1);
        return new Result(true, recipeString.toString());
    }

    public Result showCookingRecipes(){
        Player player = game.getCurrentPlayer();
        ArrayList<CookingRecipe> playerCookingRecipes = player.getCookingRecipe();
        StringBuilder recipeString = new StringBuilder();
        recipeString.append(player.getUsername() + "'s cooking recipes:\n");
        for(CookingRecipe cookingRecipe : playerCookingRecipes) {
            recipeString.append(cookingRecipe.getRecipeType().getDisplayName() + "\n");
        }
        recipeString.deleteCharAt(recipeString.length() - 1);
        return new Result(true, recipeString.toString());
    }

    public Result craftItem(String itemName) {
        Player player = game.getCurrentPlayer();
        CraftingRecipes recipeType = findCraftingRecipeType(itemName);
        if(recipeType == null) {
            return new Result(false, "error");
        }

        CraftingRecipe recipe;

        if((recipe = findCraftingRecipeInInventory(player.getCraftingRecipe(), recipeType)) == null) {
            return new Result(false, "The crafting recipe is not in your inventory");
        }

        if(player.getInventory().isFull()){
            return new Result(false, "your inventory is full");
        }

        Result result = isInventoryReadyToCraft(recipeType);

        if(!result.isSuccessful()){
            return result;
        }

        CraftingMachine craftingProduct = new CraftingMachine(recipeType.getProduct(),10);
        player.getInventory().getItems().add(craftingProduct);
        player.getInventory().addNumOfItems(1);
        player.addEnergy(-2);
        return new Result(true,craftingProduct.getName() + " crafted successfully");
    }

    public Result placeItem(String itemName, String directionStr) {
        Player player = game.getCurrentPlayer();
        GameMap map = game.getMap();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = getTargetTile(currentTile, directionStr, map);
        if(targetTile.getItem() != null) {
            return new Result(false, "ypu can not put item on this tile, it has another item");
        }
        Item item = findItem(itemName, player.getInventory().getItems());
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
        targetTile.setItem(null);
        player.getInventory().addNumOfItems(1);
        return new Result(true, targetTile.getItem().getName() + " x" + targetTile.getItem().getNumber() + " picked successfully");
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

            if (player.getInventory().isFull()) {
                return new Result(false, "Your inventory is full");
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
        if(!good.canBeUsed()){
            return new Result(false,"The good is not ready to use");
        }
        player.getInventory().addNumOfItems(1);
        return new Result(true, good.getName() + " added to your inventory");
    }

    private void onDayPassed(int days) {
    }

    private void onSeasonChanged(int seasons) {
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
            if (cropType.name().equals(cropName)) {
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
        Matcher usernameMatcher = GameMenuCommands.ExtractUsernames.getMatcher(command);
        List<String> usernames = new ArrayList<>();

        while (usernameMatcher.find()) {
            usernames.add(usernameMatcher.group(1));
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
        for(Game game : app.getGames()){
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
        for (CookingRecipes r : CookingRecipes.values()) {
            sb.append("- ").append(r.getDisplayName()).append("\n");
        }
        return Result.success(sb.toString());
    }

    private Result cookingPrepare(String recipeName) {
        Player player = game.getCurrentPlayer();
        House  house  = player.getHouse();
        var    refrigerator = house.refrigerator();
        var    inventory    = player.getInventory();

        CookingRecipes recipe;
        try { recipe = CookingRecipes.valueOf(recipeName); }
        catch (IllegalArgumentException e) {
            return Result.failure("Invalid recipe: " + recipeName);
        }

        boolean ok = recipe.getIngredients().entrySet().stream()
                .allMatch(e -> refrigerator.hasMaterial(e.getKey(), e.getValue()));
        if (!ok) {
            return Result.failure("There are not enough ingredients in the refrigerator.");
        }

        for (Map.Entry<MaterialType, Integer> e : recipe.getIngredients().entrySet()) {
            refrigerator.pickMaterial(e.getKey(), e.getValue());
        }

        FoodType food;
        try { food = FoodType.valueOf(recipeName); }
        catch (IllegalArgumentException e) {
            return Result.failure("Error converting to FoodType.");
        }

        boolean added = inventory.addItem(food.createItem(1));
        if (!added) {
            for (Map.Entry<MaterialType, Integer> e : recipe.getIngredients().entrySet()) {
                refrigerator.putMaterial(e.getKey(), e.getValue());
            }
            return Result.failure("There is not enough space in the inventory.");
        }

        return Result.success(recipe.getDisplayName() + "Ready and added.");
    }

    private void eat(String foodName) {}

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
            if(cropType.getSeedSource().name().equals(seedName)) {
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
}
