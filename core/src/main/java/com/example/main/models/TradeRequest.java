package com.example.main.models;

public class TradeRequest extends Trade {
    private final String givingItemName;
    private final int givingAmount;
    private final String receivingItemName;
    private final int receivingAmount;

    public TradeRequest(Player sender, Player receiver, Player buyer, Player seller, String givingItemName, int givingAmount, String receivingItemName, int receivingAmount) {
        super(sender, receiver, buyer, seller);
        this.givingItemName = givingItemName;
        this.givingAmount = givingAmount;
        this.receivingItemName = receivingItemName;
        this.receivingAmount = receivingAmount;
    }

    public String getGivingItemName() {
        return givingItemName;
    }

    public int getGivingAmount() {
        return givingAmount;
    }

    public String getReceivingItemName() {
        return receivingItemName;
    }

    public int getReceivingAmount() {
        return receivingAmount;
    }

    @Override
    public String toString() {
        String status = "PENDING";
        if (this.isAccepted) {
            status = "ACCEPTED";
        } else if (this.isAnswered && !this.isAccepted) {
            status = "REJECTED";
        }
        
        return """
               Trade Offer:
               Sender:""" + this.sender.getUsername() + "\nReceiver: " + this.receiver.getUsername() +
            "\nBuyer: " + this.buyer.getUsername() + "\nSeller: " + this.seller.getUsername() +
            "\nGiving Item: " + this.givingItemName + "\nGiving Amount: " + this.givingAmount +
            "\nReceiving Item: " + this.receivingItemName + "\nReceiving Amount: " + this.receivingAmount +
            "\nStatus: " + status + "\nId: " + this.tradeId + "\n-----------------------\n";
    }
}
