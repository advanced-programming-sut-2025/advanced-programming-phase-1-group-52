package models;

public class TradeRequest extends Trade {
    private final String givingItemName;
    private final int givingAmount;
    private final String receivingItemName;
    private final int receivingAmount;

    public TradeRequest(Player buyer, Player seller, String givingItemName, int givingAmount, String receivingItemName, int receivingAmount) {
        super(buyer, seller);
        this.givingItemName = givingItemName;
        this.givingAmount = givingAmount;
        this.receivingItemName = receivingItemName;
        this.receivingAmount = receivingAmount;
    }
}
