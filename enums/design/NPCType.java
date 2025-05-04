package enums.design;

import java.util.ArrayList;
import models.Quest;
import models.item.Item;

public enum NPCType {
    Sebastian(42, 32),
    Abigail(52, 32),
    Harvey(32, 42),
    Lia(42, 42),
    Robin(52, 42),;

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
