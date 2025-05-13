package models;

import java.util.ArrayList;

public class Friendship {
    private final ArrayList<Player> players = new ArrayList<>();
    private int friendshipLevel = 0;
    private int friendshipPoints = 0;

    public Friendship(Player firstPlayer, Player secondPlayer) {
        this.players.add(firstPlayer);
        this.players.add(secondPlayer);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getFriendshipLevel() {
        return friendshipLevel;
    }

    public int getFriendshipPoints() {
        return friendshipPoints;
    }

    public void addFriendshipPoints(int points) {
        this.friendshipPoints += points;
    }

    public void upgradeLevel() {
        this.friendshipLevel++;
    }
}
