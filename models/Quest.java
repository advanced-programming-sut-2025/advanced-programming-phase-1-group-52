package models;

import enums.design.NPCType;
import enums.items.ItemType;
import java.util.HashMap;

public class Quest {
    private final NPCType questGiver;
    private final HashMap<ItemType, Integer> demands;
    private final HashMap<ItemType, Integer> reward;
    private int rate = 0;

    public Quest(NPCType questGiver, ItemType demand, int demandAmount, ItemType reward, Integer rewardAmount) {
        this.questGiver = questGiver;
        this.demands = new HashMap<>();
        this.reward = new HashMap<>();

        this.demands.put(demand, demandAmount);
        this.reward.put(reward, rewardAmount);
    }

    public HashMap<ItemType, Integer> getDemands() {
        return demands;
    }

    public HashMap<ItemType, Integer> getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return this.questGiver.name() + "\'s quest: " + 
        this.demands.toString() + " for " + this.reward.toString()
        + "---------------------------\n";
    }

    public void rateGift(int rate) {
        this.rate = rate;
    }
}
