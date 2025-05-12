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
        if (friendshipPoints < 100 && friendshipPoints + points > 100) {
            friendshipLevel = 1;
        }
        else if (friendshipPoints < 200 && friendshipPoints + points > 200) {
            friendshipLevel = 2;
        } 
        else if (friendshipPoints < 300 && friendshipPoints + points > 300) {
            friendshipLevel = 3;
        } 
        else if (friendshipPoints < 400 && friendshipPoints + points > 400) {
            friendshipLevel = 4;
        } 

        this.friendshipPoints += points;
    }
}
