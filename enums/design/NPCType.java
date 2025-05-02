package enums.design;

import models.Quest;
import models.item.Item;

import java.util.ArrayList;

public enum NPCType {
    Sebastian(),
    Abigail(),
    Harvey(),
    Lia(),
    Robin();

    private final ArrayList<Item> favorites = new ArrayList<>();
    private final ArrayList<Quest> quests = new ArrayList<>();

    public ArrayList<Quest> getQuests() {
        return quests;
    }
}
