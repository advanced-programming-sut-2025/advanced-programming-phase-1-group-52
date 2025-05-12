package models;

import java.util.ArrayList;

public class Friendship {
    private final ArrayList<User> players = new ArrayList<>();
    private int friendshipLevel = 0;
    private int friendshipPoints = 0;

    public Friendship(User firstPlayer, User secondPlayer) {
        this.players.add(firstPlayer);
        this.players.add(secondPlayer);
    }

    public ArrayList<User> getPlayers() {
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
