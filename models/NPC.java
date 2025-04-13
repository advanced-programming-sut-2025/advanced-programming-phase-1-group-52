package models;

import enums.design.NPCs;

import java.util.HashMap;

public class NPC {
    private final NPCs type;
    private int friendshipLevel = 0;
    private int friendshipPoints = 0;
    private final HashMap<Quest, Boolean> quests;

    public NPC(NPCs type) {
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
