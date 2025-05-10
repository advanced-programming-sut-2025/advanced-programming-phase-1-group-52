package controllers;

import enums.design.FarmThemes;
import enums.design.Season;
import enums.design.Weather;
import enums.regex.GameMenuCommands;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import models.*;


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
            return new Result(true, "Game switched to " + game.getCurrentPlayer().username() + " ");
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

        if (currentPlayer.energy() < requiredEnergy) {
            currentPlayer.setFainted(true);
            currentPlayer.setEnergy(0);
            Tile reachedTile = path.get(currentPlayer.energy() * 20 - 1);
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
        int playerEnergy = game.getCurrentPlayer().energy();
        return new Result(true, "Player energy: " + playerEnergy);
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
        for(Game game : app.getGames()){
            if(game.getPlayers().contains(user)){
                return false;
            }
        }
        return true;
    }

    private void calculateEnergy(int amount) {
        Player player = game.getCurrentPlayer();
        player.setEnergy(player.energy() + amount);
        if(player.energy() >= 200){
            player.setEnergy(200);
        }
        if(player.energy() <= 0){
            player.setFainted(true);
            switchTurn();
        }
    }
}
