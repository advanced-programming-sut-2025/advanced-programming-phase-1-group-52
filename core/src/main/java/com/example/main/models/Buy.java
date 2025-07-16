package com.example.main.models;

public class Buy extends Trade {
    private final String itemName;
    private final int amount;
    private final int price;

    public Buy(Player sender, Player receiver, Player buyer, Player seller, String itemName, int amount, int price) {
        super(sender, receiver, buyer, seller);
        this.itemName = itemName;
        this.amount = amount;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
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
               Buy:
               Sender:""" + this.sender.getUsername() + "\nReceiver: " + this.receiver.getUsername() +
            "\nBuyer: " + this.buyer.getUsername() + "\nSeller: " + this.seller.getUsername() +
            "\nGiving Item: " + this.itemName + "\nGiving Amount: " + this.amount + "\nPrice: " + this.price +
            "\nStatus: " + status + "\nId: " + this.tradeId + "\n-----------------------\n";
    }
}
