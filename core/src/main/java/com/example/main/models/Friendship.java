package com.example.main.models;

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

    public void setFriendshipLevel(int level) {
        this.friendshipLevel = level;
    }

    public void resetFriendship() {
        this.friendshipLevel = 0;
        this.friendshipPoints = 0;
    }

    public void addFriendshipPoints(int points) {
        this.friendshipPoints += points;
        
        if (friendshipPoints >= 200) {
            friendshipLevel = 2;
        } 
        else if (friendshipPoints >= 100) {
            friendshipLevel = 1;
        }
        else {
            friendshipLevel = 0;
        }
    }

    @Override
    public String toString() {
        return players.get(0).getUsername() + " and " + players.get(1).getUsername() +
                " friendship\nLevel: " + friendshipLevel +
                "\nPoints: " + friendshipPoints;
    }
}
