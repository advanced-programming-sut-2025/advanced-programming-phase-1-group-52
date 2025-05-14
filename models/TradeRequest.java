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

    public String getGivingItemName() {
        return givingItemName;
    }

    public int getGivingAmount() {
        return givingAmount;
    }

    public String getReceivingItemName() {
        return receivingItemName;
    }

    public int getReceivingAMount() {
        return receivingAmount;
    }

    @Override
    public String toString() {
        return """
               Trade Request:
               Buyer: """ + this.buyer.getUsername() + "\nSeller: " + this.seller.getUsername() +
            "\nGiving Item: " + this.givingItemName + "\nGiving Amount: " + this.givingAmount +
            "\nReceiving Item: " + this.receivingItemName + "\nReceiving Amount: " + this.receivingAmount +
            "\nId: " + this.tradeId + "\n-----------------------\n";
    }
}
