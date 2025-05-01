package models;

import enums.design.NPCType;

import java.util.HashMap;

public class NPC {
    private final NPCType type;
    private int friendshipLevel = 0;
    private int friendshipPoints = 0;
    private final HashMap<Quest, Boolean> quests;

    public NPC(NPCType type) {
        this.type = type;
        this.quests = new HashMap<>();
        for (Quest quest : this.type.getQuests()) {
            quests.put(quest, false);
        }
    }

    public void meetNPC() {

    }

    public void finishQuest(int id) {

    }
}
