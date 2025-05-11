package controllers;

import enums.design.Season;
import enums.items.CookingRecipes;
import enums.items.FoodType;
import enums.items.MaterialType;
import enums.regex.GameMenuCommands;
import models.App;
import models.Game;
import models.Result;
import models.User;
import models.*;
import models.building.House;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        User loggedInUser = App.getInstance().getCurrentUser();

        Player player1 = new Player(loggedInUser.getUsername());
        loggedInUser.setCurrentPlayer(player1);
        players.add(loggedInUser);

        Player player2 = new Player(user1.getUsername());
        user1.setCurrentPlayer(player2);
        players.add(user1);


        Player player3 = new Player(user2.getUsername());
        user2.setCurrentPlayer(player3);
        players.add(user2);


        Player player4 = new Player(user3.getUsername());
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
        if(App.getInstance().getCurrentUser().userGame() == null) {
            return new Result(false, "You are not in a game");
        }
        // todo : load game
        return new Result(true, "Your last game is loaded");
    }

    public Result exitGame() {
        Game game = App.getInstance().getCurrentGame();
        User loggedInUser = App.getInstance().getCurrentUser();
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
        Game game = App.getInstance().getCurrentGame();
        game.switchCurrentPlayer();
        return new Result(true, "Game switched to " + game.currentPlayer().username() + " ");
    }

    public Result showTime(){
        Game game = App.getInstance().getCurrentGame();
        return new Result(true, "It's " + game.time().hour() + "O'clock");
    }

    public Result showDate(){
        Game game = App.getInstance().getCurrentGame();
        return new Result(true,"Season: " + game.date().currentSeason().name() +
                "\nDay: " + game.date().currentDay());
    }

    public Result showDateAndTime(){
        return new Result(true, showTime().Message() + "\n" + showDate().Message());
    }

    public Result showDayOfWeek(){
        Game game = App.getInstance().getCurrentGame();
        return new Result(true,"It's " + game.date().currentWeekday().name());
    }

    public Result changeTime(int hours) {
        if (hours <= 0) {
            return new Result(false, "Hours must be positive");
        }
        Game game = App.getInstance().getCurrentGame();
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
        Game game = App.getInstance().getCurrentGame();
        Date date = game.date();
        int originalDay = date.currentDay();
        Season originalSeason = date.currentSeason();

        int seasonsPassed = date.addDays(days);

        game.time().setHour(Time.DAY_START);

        this.onSeasonChanged(seasonsPassed);

        return new Result(true,"date changed!");
    }

    public Result showSeason() {
        Game game = App.getInstance().getCurrentGame();
        return new Result(true,game.date().currentSeason().name());
    }

    public Result lightningHandling(){
        return new Result(true, "Lightning handling");
    }

    public Result cheatLightning(int x, int y){
        return new Result(true, "Lightning handling");
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

    private Result cookingRefrigeratorPut(String materialName, String quantityStr) {
        Player player = App.getInstance().currentPlayer();
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
        Player player = App.getInstance().currentPlayer();
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
        Player player = App.getInstance().currentPlayer();
        House  house  = player.getHouse();
        var    refrigerator = house.refrigerator();
        var    inventory    = player.inventory();

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

}
