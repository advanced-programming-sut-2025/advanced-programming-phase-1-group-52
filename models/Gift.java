package models;

import models.item.Item;

public class Gift {
    private final Player sender;
    private Player receiver;
    private NPC receiverNPC;
    private final Item item;
    private int amount;
    private final int rate;

    public Gift(Player sender, Player receiver, Item item, int amount, int rate) {
        this.sender = sender;
        this.receiver = receiver;
        this.item = item;
        this.amount = amount;
        this.rate = rate;
    }

    public Gift(Player sender, NPC receiverNPC, Item item, int amount, int rate) {
        this.sender = sender;
        this.receiverNPC = receiverNPC;
        this.item = item;
        this.rate = rate;
    }

    public Player getReceiver() {
        return receiver;
    }
}
