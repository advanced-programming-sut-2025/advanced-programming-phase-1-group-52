package controllers;

import enums.design.Direction;
import enums.design.FarmThemes;
import enums.design.Season;
import enums.items.*;
import enums.design.Weather;
import enums.regex.GameMenuCommands;
import models.App;
import models.Game;
import models.Result;
import models.User;
import models.*;
import models.building.House;
import models.item.Item;
import models.item.Seed;
import models.item.Tool;
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

        User user1, user2, user3;

        if ((user1 = findUser(usernames.get(0))) == null || (user2 = findUser(usernames.get(1))) == null || (user3 = findUser(usernames.get(2))) == null) {
            return new Result(false, "User not found, please try again");
        }

        if (!isUserAvailable(user1) || !isUserAvailable(user2) || !isUserAvailable(user3)) {
            return new Result(false, "Users are not available");
        }
        ArrayList<User> players = new ArrayList<>();
        User loggedInUser = App.getInstance().getCurrentUser();

        Player player1 = new Player(loggedInUser.getUsername());
        player1.setOriginX(10);
        player1.setOriginY(10);
        loggedInUser.setCurrentPlayer(player1);
        players.add(loggedInUser);

        Player player2 = new Player(user1.getUsername());
        player2.setOriginX(80);
        player2.setOriginY(10);
        user1.setCurrentPlayer(player2);
        players.add(user1);

        Player player3 = new Player(user2.getUsername());
        player3.setOriginX(10);
        player3.setOriginY(80);
        user2.setCurrentPlayer(player3);
        players.add(user2);

        Player player4 = new Player(user3.getUsername());
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
        if (App.getInstance().getCurrentUser().userGame() == null) {
            return new Result(false, "You are not in a game");
        }
        // todo : load game
        return new Result(true, "Your last game is loaded");
    }

    public Result exitGame() {
        User loggedInUser = App.getInstance().getCurrentUser();
        if (game.getMainPlayer().equals(loggedInUser)) {
            // todo : exit the game and go to game menu
            return new Result(true, "You are in game menu now");
        } else {
            return new Result(false, "You are not the creator of this game");
        }
    }

    public Result terminateGame() {
        return new Result(true, "Game terminated");
    }

    public Result switchTurn() {
        boolean isPlayerAvailable = game.switchCurrentPlayer();
        if (isPlayerAvailable) {
            return new Result(true, "Game switched to " + game.getCurrentPlayer().getUsername() + " ");
        }
        return new Result(false, "you can not switch to other players");
    }

    public Result showTime() {
        return new Result(true, "It's " + game.getTime().hour() + "O'clock");
    }

    public Result showDate() {
        return new Result(true, "Season: " + game.getDate().currentSeason().name() +
                "\nDay: " + game.getDate().currentDay());
    }

    public Result showDateAndTime() {
        return new Result(true, showTime().Message() + "\n" + showDate().Message());
    }

    public Result showDayOfWeek() {
        return new Result(true, "It's " + game.getDate().currentWeekday().name());
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

        return new Result(true, "date changed!");
    }

    public Result showSeason() {
        return new Result(true, game.getDate().currentSeason().name());
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
        
        // todo : check if the item is giftable
        // todo : check if the item is thier favorite
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

            // todo: show third quest by time passed
        }

        return new Result(true, stringBuilder.toString());
    }

    public Result talk(String receiverName, String message) {
        Player player = game.getUserByUsername(receiverName).getPlayer();
        if (player == null) {
            return new Result(false, "Player not found!");
        }
        if (!isPlayerNearSomething(player.currentX(), player.currentY())) {
            return new Result(false, "You should be near" + receiverName);
        }

        Talk talk = new Talk(player, message);
        game.getCurrentPlayer().addTalk(talk);

        Friendship friendship = game.getFriendshipByPlayers(game.getCurrentPlayer(), player);
        friendship.addFriendshipPoints(10);
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

    public Result cheatUnlimitedEnergy() {
        Player player = game.getCurrentPlayer();
        player.setEnergy(Integer.MAX_VALUE);
        return new Result(true, player.getUsername() + "'s energy: is unlimited now! HA HA HA");
    }

    public Result showInventoryItems() {
        Player player = game.getCurrentPlayer();
        StringBuilder items = new StringBuilder();
        for (Item item : player.getInventory().getItems()) {
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
            return new Result(true, "Item removed from inventory");
        }
    }

    public Result equipTool(String toolName) {
        Player player = game.getCurrentPlayer();
        Tool tool;
        if ((tool = (Tool) findItem(toolName, player.getInventory().getItems())) == null) {
            return new Result(false, "Tool not found in your inventory");
        }
        player.setCurrentTool(tool);
        return new Result(true, tool.getName() + "'s tool has been equipped");
    }

    public Result showCurrentTool() {
        Tool tool = game.getCurrentPlayer().getCurrentTool();
        if (tool == null) {
            return new Result(false, "There is no tool in your hand!");
        }
        return new Result(true, tool.getName() + " is your current tool");
    }

    public Result showAllTools() {
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

    public void fishingAndDisplay(ToolType pole) {
    }

    public Result plant(String seedName, String directionStr){
        GameMap map = game.getMap();
        Player player = game.getCurrentPlayer();
        Tile currrentTile = map.getTile(player.currentX(), player.currentY());
        Tile targetTile = getTargetTile(currrentTile,directionStr,map);
        Seed seed;
        if((seed = findSeedInInventory(player.getInventory().getItems(),seedName)) == null) {
            return new Result(false, "Seed not found");
        }
        if()

    }

    private void onDayPassed(int days) {
    }

    private void onSeasonChanged(int seasons) {
    }

    private User findUser(String username) {
        for (User user : App.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
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
        for (Game game : app.getGames()) {
            if (game.getPlayers().contains(user)) {
                return false;
            }
        }
        return true;
    }

    private Result cookingRefrigeratorPut(String materialName, String quantityStr) {
        Player player = game.getCurrentPlayer();
        House house = player.getHouse();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return Result.failure("The value must be an integer: " + quantityStr);
        }

        MaterialType material;
        try {
            material = MaterialType.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            return Result.failure("Invalid raw material: " + materialName);
        }

        boolean ok = house.refrigerator().putMaterial(material, quantity);
        return ok
                ? Result.success(quantity + " × " + material + " It was placed in the refrigerator.")
                : Result.failure("Error in placing the material in the refrigerator.");
    }

    private Result cookingRefrigeratorPick(String materialName, String quantityStr) {
        Player player = game.getCurrentPlayer();
        House house = player.getHouse();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return Result.failure("The value must be an integer: " + quantityStr);
        }

        MaterialType mat;
        try {
            mat = MaterialType.valueOf(materialName);
        } catch (IllegalArgumentException e) {
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
        House house = player.getHouse();
        var refrigerator = house.refrigerator();
        var inventory = player.getInventory();

        CookingRecipes recipe;
        try {
            recipe = CookingRecipes.valueOf(recipeName);
        } catch (IllegalArgumentException e) {
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
        try {
            food = FoodType.valueOf(recipeName);
        } catch (IllegalArgumentException e) {
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

    private void eat(String foodName) {
    }

    private void calculateEnergy(int amount) {
        Player player = game.getCurrentPlayer();
        player.setEnergy(player.getEnergy() + amount);
        if (player.getEnergy() >= 200) {
            player.setEnergy(player.getEnergy() + amount);
            if (player.getEnergy() >= 200) {
                player.setEnergy(200);
            }
            if (player.getEnergy() <= 0) {
                player.setFainted(true);
                switchTurn();
            }
        }
    }

    private Item findItem(String itemName, ArrayList<Item> items) {
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
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

    private Seed findSeedInInventory(ArrayList<Item> items, String name) {
        for(Item item : items) {
            if(item instanceof Seed) {
                if(name.equals(((Seed) item).getName())) {
                    return (Seed) item;
                }
            }
        }
        return null;
    }
}