package models;

import enums.design.NPCType;
import enums.items.Items;
import java.util.HashMap;

public class Quest {
    private final NPCType questGiver;
    private final HashMap<Items, Integer> demands;
    private final HashMap<Items, Integer> reward;

    public Quest(NPCType questGiver, Items demand, int demandAmount, Items reward, Integer rewardAmount) {
        this.questGiver = questGiver;
        this.demands = new HashMap<>();
        this.reward = new HashMap<>();

        this.demands.put(demand, demandAmount);
        this.reward.put(reward, rewardAmount);
    }

    public HashMap<Items, Integer> getDemands() {
        return demands;
    }

    public HashMap<Items, Integer> getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return this.questGiver.name() + "\'s quest: " + 
        this.demands.toString() + " for " + this.reward.toString()
        + "---------------------------\n";
    }
}
