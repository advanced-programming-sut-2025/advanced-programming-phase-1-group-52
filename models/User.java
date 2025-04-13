package models;

import enums.player.Gender;

import java.util.HashMap;

public class User {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private final Gender gender;
    private final HashMap<Game, Player> userPlayers;
    private int numPlayed;
    private int highScore;

    public User(String username, String password, String nickname, String email, Gender gender) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.userPlayers = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public Gender getGender() {
        return gender;
    }

    public Player getPlayer() {
        return this.userPlayers.get(App.getInstance().getCurrentGame());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
