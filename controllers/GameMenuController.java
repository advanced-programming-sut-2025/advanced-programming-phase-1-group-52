package controllers;

import enums.design.Season;
import enums.design.Weather;
import enums.items.ToolType;
import enums.regex.GameMenuCommands;
import models.App;
import models.Game;
import models.Result;
import models.User;
import models.*;
import models.item.Item;
import models.item.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


public class GameMenuController {


    public Result startNewGame(String input) {
        List<String> usernames;
        try {
            usernames = validateGameCommand(input);
        } catch (IllegalArgumentException e) {
            return new Result(false, e.getMessage());
        }

        User user1,user2,user3;

        if((user1 = findUser(usernames.get(0))) == null || (user2 = findUser(usernames.get(1))) == null || (user3 = findUser(usernames.get(2))) == null) {
            return new Result(false, "User not found, please try again");
        }

        if(!isUserAvailable(user1) || !isUserAvailable(user2) || !isUserAvailable(user3)) {
            return new Result(false, "Users are not available");
        }
        ArrayList<User> players = new ArrayList<>();
        User loggedInUser = App.getInstance().currentUser();

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
        return new Result(true, "Now Choose your map!");
    }

    public Result chooseMap(User user, String mapIdStr) {
        // todo : get each user map in game view
        // todo : print options for users in game view
        // todo : check the other error
        int mapId = Integer.parseInt(mapIdStr);
        if (mapId > 3 || mapId < 1) {
            return new Result(false, "Invalid map id, please try again");
        }
        Player userPlayer = user.currentPlayer();
        // todo : return player and mapId to be built
        return new Result(true,user.getUsername() + "'s map is " + mapId);
    }

    public Result loadMap() {
        if(App.getInstance().currentUser().userGame() == null) {
            return new Result(false, "You are not in a game");
        }
        // todo : load game
        return new Result(true, "Your last game is loaded");
    }

    public Result exitGame() {
        Game game = App.getInstance().currentGame();
        User loggedInUser = App.getInstance().currentUser();
        if(game.mainPlayer().equals(loggedInUser)){
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

    public Result switchTurn(){
        Game game = App.getInstance().currentGame();
        boolean isPlayerAvailable = game.switchCurrentPlayer();
        if(isPlayerAvailable){
            return new Result(true, "Game switched to " + game.currentPlayer().username() + " ");
        }
        return new Result(false, "you can not switch to other players");
    } 

    public Result showTime(){
        Game game = App.getInstance().currentGame();
        return new Result(true, "It's " + game.time().hour() + "O'clock");
    }

    public Result showDate(){
        Game game = App.getInstance().currentGame();
        return new Result(true,"Season: " + game.date().currentSeason().name() +
                "\nDay: " + game.date().currentDay());
    }

    public Result showDateAndTime(){
        return new Result(true, showTime().Message() + "\n" + showDate().Message());
    }

    public Result showDayOfWeek(){
        Game game = App.getInstance().currentGame();
        return new Result(true,"It's " + game.date().currentWeekday().name());
    }

    public Result changeTime(int hours) {
        if (hours <= 0) {
            return new Result(false, "Hours must be positive");
        }
        Game game = App.getInstance().currentGame();
        Time time = game.time();
        Date date = game.date();

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
        Game game = App.getInstance().currentGame();
        Date date = game.date();
        int originalDay = date.currentDay();
        Season originalSeason = date.currentSeason();

        int seasonsPassed = date.addDays(days);

        game.time().setHour(Time.DAY_START);

        this.onSeasonChanged(seasonsPassed);

        return new Result(true,"date changed!");
    }

    public Result showSeason() {
        Game game = App.getInstance().currentGame();
        return new Result(true,game.date().currentSeason().name());
    }

    public Result lightningHandling(){
        return new Result(true, "Lightning handling");
    }

    public Result cheatLightning(int x, int y){
        return new Result(true, "Lightning handling");
    }

    public Result showWeather(){
        Game game = App.getInstance().currentGame();
        return new Result(true, game.todayWeather().name());
    }

    public Result showTomorrowWeather(){
        Game game = App.getInstance().currentGame();
        return new Result(true, game.tomorrowWeather().name());
    }

    public Result changeTomorrowWeather(String weatherStr){
        Game game = App.getInstance().currentGame();
        try {
            Weather weather = Weather.fromString(weatherStr);
            game.setTomorrowWeather(weather);
        } catch (IllegalArgumentException e) {
            return new Result(false, "Invalid weather string");
        }
        return new Result(true, "Tomorrow weather changed to" + game.tomorrowWeather().name());
    }

    public Result buildGreenHouse(){
        Game game = App.getInstance().currentGame();
        return new Result(true, "Green house");
    }

    public Result walk(int x, int y){
        // todo : calculate closest way
        return new Result(true, "Walking");
    }

    public Result printMap(int x, int y,int size){
        Game game = App.getInstance().currentGame();
        return new Result(true, "Printing Map");
    }

    public Result mapInfo(){
        return new Result(true, "Map Info");
    }

    public Result energyShow(){
        Game game = App.getInstance().currentGame();
        int playerEnergy = game.currentPlayer().energy();
        return new Result(true, "Player energy: " + playerEnergy);
    }

    public Result cheatSetEnergy(int value){
        Game game = App.getInstance().currentGame();
        Player player = game.currentPlayer();
        player.setEnergy(value);
        return new Result(true, player.username() + "'s energy: is set to " + value);
    }

    public Result cheatUnlimitedEnergy(){
        Game game = App.getInstance().currentGame();
        Player player = game.currentPlayer();
        player.setEnergy(Integer.MAX_VALUE);
        return new Result(true, player.username() + "'s energy: is unlimited now! HA HA HA");
    }

    public Result showInventoryItems(){
        Game game = App.getInstance().currentGame();
        Player player = game.currentPlayer();
        StringBuilder items = new StringBuilder();
        for(Item item: player.inventory().getItems()){
            items.append(item.getName() + " x" + item.getNumber() + ", ");
        }
        items.delete(items.length() - 2, items.length());
        return new Result(true, items.toString());
    }

    public Result removeItemFromInventory(String itemName, String itemNumberStr){
        // todo : handle trim in view for now
        // todo : calculate return money
        Game game = App.getInstance().currentGame();
        Inventory inventory = game.currentPlayer().inventory();
        int itemNumber;
        Item item;
        if((item = findItem(itemName, inventory.getItems())) == null){
            return new Result(false, "Item not found");
        }

        if(itemNumberStr != null && !itemNumberStr.isEmpty()){
            itemNumber = Integer.parseInt(itemNumberStr);
            item.setNumber(item.getNumber() - itemNumber);
            return new Result(true,  "x" + itemNumber + item.getName() + " has been removed");
        }

        else{
            inventory.removeItem(item);
            return new Result(true, "Item removed from inventory");
        }
    }

    public Result equipTool(String toolName){
        Game game = App.getInstance().currentGame();
        Player player = game.currentPlayer();
        Tool tool;
        if((tool = (Tool) findItem(toolName, player.inventory().getItems())) == null){
            return new Result(false, "Tool not found in your inventory");
        }
        player.setCurrentTool(tool);
        return new Result(true, tool.getName() + "'s tool has been equipped");
    }

    public Result showCurrentTool(){
        Game game = App.getInstance().currentGame();
        Tool tool = game.currentPlayer().getCurrentTool();
        if(tool == null){
            return new Result(false, "There is no tool in your hand!");
        }
        return new Result(true, tool.getName() + " is your current tool");
    }

    public Result showAllTools(){
        Game game = App.getInstance().currentGame();
        Player player = game.currentPlayer();
        ArrayList<Item> tools = player.inventory().getItems();

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
        for(Game game : App.getInstance().getGames()){
            if(game.players().contains(user)){
                return false;
            }
        }
        return true;
    }

    private void calculateEnergy(int amount) {
        Player player = App.getInstance().currentGame().currentPlayer();
        player.setEnergy(player.energy() + amount);
        if(player.energy() >= 200){
            player.setEnergy(200);
        }
        if(player.energy() <= 0){
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

    private String toolListMaker(ArrayList<Item> tools) {
        StringBuilder toolList = new StringBuilder();
        for(Item item : tools){
            if(item.getItemType() == ToolType.PrimitiveHoe){}
        }
    }

}
