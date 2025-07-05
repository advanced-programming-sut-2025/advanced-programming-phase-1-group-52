package com.example.main.models;

public class Buy extends Trade {
    private final String itemName;
    private final int amount;
    private final int price;

    public Buy(Player buyer, Player seller, String itemName, int amount, int price) {
        super(buyer, seller);
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
        return """
               Buy:
               Buyer: """ + this.buyer.getUsername() + "\nSeller: " + this.seller.getUsername() +
            "\nGiving Item: " + this.itemName + "\nGiving Amount: " + this.amount + "\nPrice: " + this.price +
            "\nId: " + this.tradeId + "\n-----------------------\n";
    }
}
