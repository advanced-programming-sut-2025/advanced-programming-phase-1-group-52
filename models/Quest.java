package models;

import models.item.Item;

import java.util.HashMap;

public class Quest {
    private final HashMap<Item, Integer> demands;
    private final HashMap<Object, Integer> reward;

    public Quest(Item demand, int demandAmount, Object reward, int rewardAmount) {
        this.demands = new HashMap<>();
        this.reward = new HashMap<>();

        this.demands.put(demand, demandAmount);
        this.reward.put(reward, rewardAmount);
    }

    public HashMap<Item, Integer> getDemands() {
        return demands;
    }

    public HashMap<Object, Integer> getReward() {
        return reward;
    }
}
