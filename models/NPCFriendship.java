package models;

public class NPCFriendship {
    private final NPC npc;
    private final User player;
    private int friendshipLevel = 0;
    private int friendshipPoints = 0;

    public NPCFriendship(NPC npc, User player) {
        this.npc = npc;
        this.player = player;
    }

    public NPC getNpc() {
        return npc;
    }

    public User getPlayer() {
        return player;
    }

    public int getFriendshipLevel() {
        return friendshipLevel;
    }

    public int getFriendshipPoints() {
        return friendshipPoints;
    }

    public void addFriendshipPoints(int points) {
        if (this.friendshipPoints + points > 799) {
            this.friendshipPoints = 799;
        }
        else this.friendshipPoints += points;

        if (this.friendshipPoints < 200) {
            this.friendshipLevel = 0;
        } 
        else if (this.friendshipPoints < 400) {
            this.friendshipLevel = 1;
        } 
        else if (this.friendshipPoints < 600) {
            this.friendshipLevel = 2;
        }
        else if (this.friendshipPoints < 800) {
            this.friendshipLevel = 3;
        } 
    }

    @Override
    public String toString() {
        return npc.getType().toString() + ":\n" +
                "Friendship Level: " + friendshipLevel + "\n" +
                "Friendship XP: " + friendshipPoints + "\n"; 
    }
}
