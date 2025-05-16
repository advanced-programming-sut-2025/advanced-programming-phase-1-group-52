package models;

import enums.design.NPCType;
import enums.items.ItemType;

public class Quest {
    public static int questCounter = 1;
    private final NPCType questGiver;
    private final ItemType demandingItem;
    private final int demandingAmount;
    private final ItemType rewardItem;
    private final int rewardAmount;
    private final int id;

    public Quest(NPCType questGiver, ItemType demand, int demandAmount, ItemType reward, Integer rewardAmount) {
        this.questGiver = questGiver;
        this.demandingItem = demand;
        this.demandingAmount = demandAmount;
        this.rewardItem = reward;
        this.rewardAmount = rewardAmount;
        this.id = questCounter++;
    }

    public NPCType getQuestGiver() {
        return this.questGiver;
    }

    public int getId() {
        return this.id;
    }

    public ItemType getDemand() {
        return this.demandingItem;
    }

    public int getDemandAmount() {
        return this.demandingAmount;
    }

    public ItemType getReward() {
        return this.rewardItem;
    }

    public int getRewardAmount() {
        return this.rewardAmount;
    }

    @Override
    public String toString() {
        return this.questGiver.name() + "\'s quest: " + 
        this.demandingItem.toString() + " x" + this.demandingAmount +
        " for " + this.rewardItem.toString() + " x" + this.rewardAmount +
        "\nId: " + this.id + "\n---------------------------\n";
    }
}
