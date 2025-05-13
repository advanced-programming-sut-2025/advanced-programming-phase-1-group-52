package models;

public class TradeOffer extends Trade {
    private final String givingItemName;
    private final int givingAmount;
    private final String receivingItemName;
    private final int receivingAmount;

    public TradeOffer(Player buyer, Player seller, String givingItemName, int givingAmount, String receivingItemName, int receivingAmount) {
        super(buyer, seller);
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
}
