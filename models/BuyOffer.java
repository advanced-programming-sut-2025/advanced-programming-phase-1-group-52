package models;

public class BuyOffer extends Trade {
    private final String itemName;
    private final int amount;
    private final int price;

    public BuyOffer(Player buyer, Player seller, String itemName, int amount, int price) {
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
}
