package com.example.main.models;

import com.example.main.models.item.Item;

public class Gift {
    private final Player sender;
    private Player receiver;
    private NPC receiverNPC;
    private final Item item;
    private int amount;
    private int rate = 0;
    private final int id;

    public Gift(Player sender, Player receiver, Item item, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.item = item;
        this.amount = amount;
        this.id = receiver.getGiftId();
    }

    public Gift(Player sender, NPC receiverNPC, Item item, int amount, int rate) {
        this.sender = sender;
        this.receiverNPC = receiverNPC;
        this.item = item;
        this.rate = rate;
        this.id = 0;
    }

    public Player getSender() {
        return this.sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public int getRate() {
        return this.rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Gift from " + sender.getUsername() + ":\n" + "Item: " + 
        item.getName() + "\n" + "Amount: " + amount + "\n" + "Rate: " + 
        rate + "\nID: " + id + "\n------------------------\n";
    }
}
