package models;

import enums.items.Items;
import java.util.HashMap;

public class Quest {
    private final HashMap<Items, Integer> demands;
    private final HashMap<Items, Integer> reward;

    public Quest(Items demand, int demandAmount, Items reward, Integer rewardAmount) {
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
}
