package com.example.main.models;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.main.enums.design.NPCType;

public class NPC {
    private int x;
    private int y;
    private final NPCType type;
    private final HashMap<Quest, Boolean> quests;
    private final ArrayList<NPCFriendship> friendships = new ArrayList<>();

    // --- THIS IS THE CORRECTED CONSTRUCTOR ---
    public NPC(NPCType type, ArrayList<Player> players) {
        this.type = type;
        this.quests = new HashMap<>();
        for (Quest quest : this.type.getQuests()) {
            quests.put(quest, false);
        }

        this.x = type.getHouseCornerX() + 1;
        this.y = type.getHouseCornerY() + 7;

        if (players != null) {
            for (Player player : players) {
                if (player != null) { // A final safety check
                    friendships.add(new NPCFriendship(this, player));
                }
            }
        }
    }

    public NPCType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HashMap<Quest, Boolean> getQuests() {
        return quests;
    }

    public ArrayList<NPCFriendship> getFriendships() {
        return friendships;
    }

    public Integer getFriendShipLevelWith(Player player) {
        for (NPCFriendship friendship : this.friendships) {
            if (friendship.getPlayer().equals(player)) return friendship.getFriendshipLevel();
        }

        return null;
    }

    public NPCFriendship getFriendShipWith(Player player) {
        for (NPCFriendship friendship : this.friendships) {
            if (friendship.getPlayer().equals(player)) return friendship;
        }

        return null;
    }

    public void finishQuest(Quest quest) {
        this.quests.put(quest, true);
    }
}
