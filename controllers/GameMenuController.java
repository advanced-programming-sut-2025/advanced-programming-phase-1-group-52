package controllers;


import enums.Menu;
import enums.design.*;
import enums.design.Shop.CarpentersShop;
import enums.design.Shop.ShopEntry;
import enums.items.*;
import enums.player.Skills;
import enums.regex.GameMenuCommands;
import enums.regex.NPCDialogs;
import models.*;
import models.building.House;
import models.building.Shop;
import models.item.*;

import enums.design.Shop.MarniesRanch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import models.building.Housing;


public class GameMenuController {
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
        player1.setOriginX(2);
        player1.setOriginY(2);
        player1.setCurrentX(2);
        player1.setCurrentY(2);
        player1.setTrashCanX(0);
        player1.setTrashCanY(0);
        loggedInUser.setCurrentPlayer(player1);
        players.add(loggedInUser);

        Player player2 = new Player(user1.getUsername(), user1.getGender());
        player2.setOriginX(84);
        player2.setOriginY(2);
        player2.setCurrentX(84);
        player2.setCurrentY(2);
        player2.setTrashCanX(89);
        player2.setTrashCanY(0);
        user1.setCurrentPlayer(player2);
        players.add(user1);

        Player player3 = new Player(user2.getUsername(), user2.getGender());
        player3.setOriginX(2);
        player3.setOriginY(34);
        player3.setCurrentX(2);
        player3.setCurrentY(34);
        player3.setTrashCanX(0);
        player3.setTrashCanY(59);
        user2.setCurrentPlayer(player3);
        players.add(user2);

        Player player4 = new Player(user3.getUsername(), user3.getGender());
        player4.setOriginX(64);
        player4.setOriginY(34);
        player4.setCurrentX(64);
        player4.setCurrentY(34);
        player4.setTrashCanX(89);
        player4.setTrashCanY(59);
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
        return new Result(true, "It's " + game.getTime().hour() + " O'clock");
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

        int originalHour = time.hour();
        int daysPassed = time.addHours(hours);


        if (daysPassed > 0) {
            int seasonsPassed = date.addDays(daysPassed);
            this.onDayPassed(daysPassed);
        }

        return new Result(true, "time changed!");
    }

    public Result changeDate(String daysStr) {
        int days = Integer.parseInt(daysStr);
        if (days <= 0) {
            return new Result(false, "Days must be positive");
        }
        Date date = game.getDate();
        int originalDay = date.getCurrentDay();
        Season originalSeason = date.getCurrentSeason();

        int seasonsPassed = date.addDays(days);

        game.getTime().setHour(Time.DAY_START);

        this.onSeasonChanged(seasonsPassed);
        for(int i = 0; i < days; i++) {
            game.randomizeTomorrowWeather();
            game.updateCrops();
            game.getMap().generateRandomForagingSeeds();
            game.getMap().generatePlantsFromSeeds();
        }
        return new Result(true,"date changed!");
    }

    public Result showSeason() {
        return new Result(true,game.getDate().getCurrentSeason().name());
    }

    public Result lightningHandling() {
        return new Result(true, "Lightning handling");
    }

    public Result cheatLightning(String xString, String yString) {
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
//                        System.out.println("hh");
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
        ArrayList<Tile> path = map.findWalkPath(currentPlayer.currentX(), currentPlayer.currentY(), x, y);

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
        return new Result(true, npc.getType().toString() + " says: " + dialogs.get(rand.nextInt(dialogs.size())).getDialog());
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

    public Result finishQuest(String idString) {
        Player currentPlayer = game.getCurrentPlayer();
        int id;
        try {
            id = Integer.parseInt(idString);
        } 
        catch (NumberFormatException e) {
            return new Result(false, "Invalid id format!"); 
        }

        NPC npc = null;
        Quest quest = null;
        boolean isDone = false;
        for (NPC n : game.getNPCs()) {
            HashMap<Quest, Boolean> quests = npc.getQuests();
            for (Quest q : quests.keySet()) {
                if (q.getId() == id) {
                    npc = n;
                    quest = q;
                    isDone = quests.get(q);
                    break;
                }
            }

            if (quest != null) break;
        }

        if (quest == null) {
            return new Result(false, "No quest found with this id!");
        }
        if (isDone) {
            return new Result(false, "This quest is already done!");
        }

        Item item = game.getCurrentPlayer().getInventory().getItemByName(quest.getDemand().getName());
        if (item == null) {
            return new Result(false, "You don't have the demanding item!");
        }
        if (item.getNumber() < quest.getDemandAmount()) {
            return new Result(false, "You don't have enough items!");
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
            npc.getFriendShipWith(currentPlayer).upgradeLevel();
        }
        else {
            switch (quest.getReward()) {
                case MaterialType materialType -> {
                    Material material = new Material(materialType, quest.getRewardAmount());
                    currentPlayer.getInventory().addItem(material);
                }
                case MineralType mineralType -> {
                    Mineral mineral = new Mineral(mineralType, quest.getRewardAmount());
                    currentPlayer.getInventory().addItem(mineral);
                }
                case ToolType toolType -> {
                    Tool tool = new Tool(toolType, quest.getRewardAmount());
                    currentPlayer.getInventory().addItem(tool);
                }
                case FishType fishType -> {
                    Fish fish = new Fish(fishType, quest.getRewardAmount());
                    currentPlayer.getInventory().addItem(fish);
                }
                case FoodType foodType -> {
                    Food food = new Food(foodType, quest.getRewardAmount());
                    currentPlayer.getInventory().addItem(food);
                }
                case CookingRecipes cookingRecipes -> {
                    CookingRecipe cookingRecipe = new CookingRecipe(cookingRecipes);
                    if (!currentPlayer.getCookingRecipe().contains(cookingRecipe)) {
                        currentPlayer.getCookingRecipe().add(cookingRecipe);
                    }
                }
                case CraftingMachineType craftingMachineType -> {
                    CraftingMachine craftingMachine = new CraftingMachine(craftingMachineType, quest.getRewardAmount());
                    currentPlayer.getInventory().addItem(craftingMachine);
                }
                default -> {
                    return new Result(false, "Invalid item!");
                }
            }
        }

        npc.getQuests().put(quest, true);
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
                Material newMaterial = new Material(material.getMaterialType(), itemAmount);
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
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near someone to ask them!");
        }

        Material ring = (Material) currentPlayer.getInventory().getItemByName("Wedding Ring");
        if (ring == null) {
            return new Result(false, "You need a ring to propose!");
        }

        Friendship friendship = game.getFriendshipByPlayers(currentPlayer, player);
        if (friendship.getFriendshipPoints() < 400) {
            return new Result(false, "Your friendship points must at least be 400!");
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
            player.addNotif(game.getCurrentPlayer(), "You are now married to " + player.getUsername());

            player.getInventory().removeItem(player.getInventory().getItemByName("Wedding Ring").getClass(), 1);
            Material newRing = new Material(MaterialType.WeddingRing, 1);
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

    public Result cheatSetEnergy(String valueStr) {
        int value = Integer.parseInt(valueStr);
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
        info.append("Name: " + cropType.name()).append("\nSource: " + cropType.getSeed()).append("\nStages: ");

        info.append(cropType.getGrowthStages1());
        info.append(cropType.getGrowthStages2());
        info.append(cropType.getGrowthStages3());
        info.append(cropType.getGrowthStages4());

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

        if(fertilizer.getMaterialName().equals(MaterialType.BasicRetainingSoil.getName())){
            targetTile.getPlant().setFertilizedToday(true);
        }
        else if(fertilizer.getMaterialName().equals(MaterialType.QualityRetainingSoil.getName())){
            targetTile.getPlant().setFertilizedToday(true);
            targetTile.getPlant().growFaster();
        }
        else if(fertilizer.getMaterialName().equals(MaterialType.DeluxeRetainingSoil.getName())){
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
        GameMap map = game.getMap();
        Tile currentTile = map.getTile(player.currentX(), player.currentY());

        if(!currentTile.getType().equals(TileType.House)) {
            return new Result(false, "You should be at home to craft item");
        }

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

    public Result eat(String foodName) {
        return new Result(true, "eating");
    }

    public Result fishing(String fishingPoleName) {
        Player player = game.getCurrentPlayer();
        Tool fishingPole = player.getInventory().getTool(fishingPoleName);
        if(fishingPole == null){
            return new Result(false, "No fishing pole found");
        }
        Tile currentTile = map.getTile(player.currentX(), player.currentY());
        if(!isAdjacentToWater(currentTile)){
            return new Result(false, "You are not adjacent to water");
        }
        if(player.getInventory().isFull()){
            return new Result(false, "Your inventory is full");
        }
        List<Fish> possibleFish = new ArrayList<>();
        double r;
        double m;
        Random random = new Random();
        for (FishType fish : FishType.values()) {
            if (fish.getSeason().equals(game.getDate().getCurrentSeason()) && fish.getType().equals("Ordinary")) {
                do {
                    r = Math.random();
                } while (r == 0.0);
                m = seasonRate();
                int fishCount = (int) Math.ceil(2 + player.getSkillLevel(Skills.Fishing) * m * r);
                fishCount = Math.min(fishCount, 6);
                Fish newFish = new Fish(fish, fishCount);
                possibleFish.add(newFish);
            }
            if(fish.getSeason().equals(game.getDate().getCurrentSeason()) && fish.getType().equals("Legendary") && (player.getSkillLevel(Skills.Fishing) >= 4)){
                do {
                    r = Math.random();
                } while (r == 0.0);
                m = seasonRate();
                int fishCount = (int) Math.ceil(2 + player.getSkillLevel(Skills.Fishing) * m * r);
                fishCount = Math.min(fishCount, 6);
                Fish newFish = new Fish(fish, fishCount);
                possibleFish.add(newFish);
            }
        }
        do {
            r = Math.random();
        } while (r == 0.0);
        m = seasonRate();
        int index = random.nextInt(possibleFish.size());
        Fish fish = possibleFish.get(index);
        double quality = (r *  (player.getSkillLevel(Skills.Fishing) + 2) * poleRate(fishingPoleName));
        quality /= (7-m);
        fish.setQuality(quality);
        player.getInventory().addItem(fish);
        player.catchFish();
        return new Result(true, fish.getNumber() + "x of " + fish.getFishType().getName() + " added to your inventory");
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

    public Result buildBarnOrCoop(String buildingKey, int x, int y) {
        if (!map.inBounds(x, y)) {
            return new Result(false, "Coordinates are out of farm bounds.");
        }

        Shop carpShop = new Shop(ShopType.CarpentersShop);
        ShopEntry entry = carpShop.findEntry(buildingKey);
        if (!(entry instanceof CarpentersShop carpEnum) ||
                !(carpEnum.getDisplayName().contains("Barn") || carpEnum.getDisplayName().contains("Coop"))) {
            return new Result(false, "'" + buildingKey + "' is not a valid barn or coop building.");
        }

        Player player = game.getCurrentPlayer();
        Inventory inv = player.getInventory();
        var req1 = carpEnum.getMaterial1();
        var req2 = carpEnum.getMaterial2();

        for (var e1 : req1.entrySet()) {
            if (inv.getCount(e1.getKey()) < e1.getValue()) {
                return new Result(false, "Insufficient " + e1.getKey() + ".");
            }
        }
        for (var e2 : req2.entrySet()) {
            if (inv.getCount(e2.getKey()) < e2.getValue()) {
                return new Result(false, "Insufficient " + e2.getKey() + ".");
            }
        }

        req1.forEach((mat, amt) -> inv.remove(mat, amt));
        req2.forEach((mat, amt) -> inv.remove(mat, amt));

        try {
            var playersList = new ArrayList<Player>(game.getPlayers().size());
            for (var u : game.getPlayers()) playersList.add(u.getPlayer());
            int idx = playersList.indexOf(player);

            var tileType = TileType.valueOf(buildingKey);
            var size     = entry.getDisplayName().split("x");
            int width    = Integer.parseInt(size[0]);
            int height   = Integer.parseInt(size[1]);

            var gen = GameMap.class.getDeclaredMethod(
                    "generateBuilding",
                    ArrayList.class,
                    int.class,
                    TileType.class,
                    int.class, int.class, int.class, int.class
            );
            gen.setAccessible(true);
            gen.invoke(map, playersList, idx, tileType, x, x + width, y, y + height);
        } catch (Exception ex) {
            return new Result(false, "Error constructing building: " + ex.getMessage());
        }

        CageType cageType;
        if (carpEnum.getDisplayName().contains("Coop")) {
            cageType = CageType.NormalCage;
        } else {
            cageType = CageType.NormalBarn;
        }
        player.addHousing(cageType);
        return new Result(true,
                carpEnum.getDisplayName() + " successfully built at (" + x + "," + y + ")."
        );
    }

    public Result buyAnimal(String animalKey, int housingId, String givenName) {
        Shop ranch = new Shop(ShopType.MarniesRanch);
        ShopEntry entry = ranch.findEntry(animalKey);
        if (!(entry instanceof MarniesRanch ranchEnum)) {
            return Result.failure("'" + animalKey + "' is not sold at Marnie's Ranch.");
        }

        AnimalType animalType = ranchEnum.getAnimalType();
        if (animalType == null) {
            return Result.failure("'" + animalKey + "' is not an animal.");
        }

        Player player = game.getCurrentPlayer();
        BankAccount account = player.getBankAccount();
        int price = ranchEnum.getPrice();

        if (account.getBalance() < price) {
            return Result.failure("Insufficient funds to buy " + animalType.getName() + ".");
        }

        Housing target = null;
        for (Housing h : player.getHousings()) {
            if (h.getId() == housingId) {
                target = h;
                break;
            }
        }
        if (target == null) {
            return Result.failure("No housing found with ID " + housingId + ".");
        }

        String requiredBuilding = ranchEnum.getBuildingRequired();
        if (!target.getType().getName().toLowerCase().contains(requiredBuilding.toLowerCase())) {
            return Result.failure(animalType.getName() + " must live in a " + requiredBuilding + ".");
        }

        PurchasedAnimal newAnimal = new PurchasedAnimal(animalType, givenName);
        Result addResult = player.addAnimalToHousing(housingId, newAnimal);
        if (!addResult.isSuccessful()) {
            return addResult;
        }

        account.withdraw(price);
        return Result.success(
                animalType.getName() +
                        " named \"" + givenName + "\" purchased for " +
                        price + "g and assigned to " +
                        target.getType().getName() +
                        " #" + housingId + "."
        );
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
        return new Result(true, "your mining skill is: " + player.getSkillLevel(Skills.Extraction));
    }

    public void menuExit() {
        App.getInstance().setCurrentMenu(Menu.ExitMenu);
    }

    private boolean isAdjacentToWater(Tile tile) {
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
                }   
                case ArtisanProductType artisanProductType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(artisanProductType.getSellPrice());
                }
                case CraftingMachineType craftingMachineType -> {
                    if (craftingMachineType.getPrice() == null) {
                        return new Result(false, "You can't sell this item!");
                    }
                    else {
                        currentPlayer.getBankAccount().setFardaeiDollar(craftingMachineType.getPrice());
                    }
                }
                case CropType cropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(cropType.getBaseSellPrice());
                }
                case FishType fishType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(fishType.getPrice());
                }
                case FoodType foodType -> {
                    
                }
                case ForagingCropType foragingCropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingCropType.getBaseSellPrice());
                }
                case ForagingSeedType foragingSeedType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingSeedType.getPrice());
                }
                case FruitType fruitType -> {
                    
                }
                case MaterialType materialType -> {
                    
                }
                case MineralType mineralType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(mineralType.getPrice());
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
                }   
                case ArtisanProductType artisanProductType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(artisanProductType.getSellPrice());
                }
                case CraftingMachineType craftingMachineType -> {
                    if (craftingMachineType.getPrice() == null) {
                        return new Result(false, "You can't sell this item!");
                    }
                    else {
                        currentPlayer.getBankAccount().setFardaeiDollar(craftingMachineType.getPrice());
                    }
                }
                case CropType cropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(cropType.getBaseSellPrice());
                }
                case FishType fishType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(fishType.getPrice());
                }
                case FoodType foodType -> {
                    
                }
                case ForagingCropType foragingCropType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingCropType.getBaseSellPrice());
                }
                case ForagingSeedType foragingSeedType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(foragingSeedType.getPrice());
                }
                case FruitType fruitType -> {
                    
                }
                case MaterialType materialType -> {
                    
                }
                case MineralType mineralType -> {
                    currentPlayer.getBankAccount().setFardaeiDollar(mineralType.getPrice());
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
        return new Result(true, "You did it!");
    }

    public Result cheatSetAnimalFriendship(String name, String amountString) {
        int amount = Integer.parseInt(amountString);
        PurchasedAnimal animal = null;
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
        game.getCurrentPlayer().getBankAccount().deposit((int) (animal.getType().getPrice() * (animal.getFriendshipPoints() / 1000 + 0.3)));
        return new Result(true, "Animal sold!");
    }
}
