package enums.design;

import models.Quest;
import models.item.Item;

import java.util.ArrayList;

public enum NPCType {
    Sebastian(42, 22),
    Abigail(52, 22),
    Harvey(32, 32),
    Lia(42, 32),
    Robin(52, 32),;

    private final ArrayList<Item> favorites = new ArrayList<>();
    private final ArrayList<Quest> quests = new ArrayList<>();
    private final int houseCornerX;
    private final int houseCornerY;

    NPCType(int houseCornerX, int houseCornerY) {
        this.houseCornerX = houseCornerX;
        this.houseCornerY = houseCornerY;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public int getHouseCornerX() {
        return houseCornerX;
    }

    public int getHouseCornerY() {
        return houseCornerY;
    }
}
