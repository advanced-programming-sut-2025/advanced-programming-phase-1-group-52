package com.example.main.models;

import com.example.main.enums.design.FarmThemes; // <-- 1. IMPORT FARMTHEMES
import com.example.main.enums.player.Gender;
import com.example.main.enums.regex.SecurityQuestion;

import java.util.HashMap;

public class User {
    // --- Fields to be saved in JSON ---
    public String username;
    public String password;
    public String nickname;
    public String email;
    public Gender gender;
    public SecurityQuestion securityQuestion;
    private String securityAnswer;
    public int numPlayed = 0;
    public int highScore = 0;

    // --- Runtime fields (marked as transient to be ignored by JSON serializer) ---
    private transient HashMap<Game, Player> userPlayers = new HashMap<>();
    private transient Game userGame;
    private transient Player currentPlayer;
    private transient String clientId;
    private transient FarmThemes farmTheme; // <-- 2. ADD THE TRANSIENT FARMTHEME FIELD

    // No-argument constructor for JSON deserialization
    public User() {
        this.userPlayers = new HashMap<>();
    }

    public User(String username, String password, String nickname, String email, Gender gender) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.userPlayers = new HashMap<>();
    }

    // --- Getters and Setters ---

    // --- 3. ADD GETTER AND SETTER FOR FARMTHEME ---
    public FarmThemes getFarmTheme() {
        return farmTheme;
    }

    public void setFarmTheme(FarmThemes farmTheme) {
        this.farmTheme = farmTheme;
    }

    // --- (Rest of your existing getters and setters) ---
    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Gender getGender() { return gender; }

    public void setGender(Gender gender) { this.gender = gender; }

    public SecurityQuestion getSecurityQuestion() { return securityQuestion; }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }

    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public Player getPlayer() { return currentPlayer; }

    public Player currentPlayer() { return currentPlayer; }

    public void setCurrentPlayer(Player currentPlayer) { this.currentPlayer = currentPlayer; }

    public Game userGame() { return userGame; }

    public void setUserGame(Game userGame) { this.userGame = userGame; }

    public HashMap<Game, Player> userPlayers() { return userPlayers; }

    public void addUserPlayers(Game game, Player player) {
        if (this.userPlayers == null) {
            this.userPlayers = new HashMap<>();
        }
        this.userPlayers.put(game, player);
    }

    public int getNumPlayed() { return numPlayed; }

    public void addNumPlayed() {
        this.numPlayed++;
    }

    public int getHighScore() { return highScore; }

    public void setHighScore(int highScore) {
        if (highScore > this.highScore) {
            this.highScore = highScore;
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
