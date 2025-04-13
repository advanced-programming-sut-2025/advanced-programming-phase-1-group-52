package models;

import models.item.Item;

public class Trade {
    private static int id = 1;
    private final Player buyer;
    private final Player seller;
    private final Item toGiveItem;
    private final int toGiveAmount;
    private int price;
    private Item toGetItem;
    private int toGetAmount;
    private boolean isAccepted;
    private final int tradeId;

    public Trade(Player buyer, Player seller, Item toGiveItem, int toGiveAmount, int price) {
        this.buyer = buyer;
        this.seller = seller;
        this.toGiveItem = toGiveItem;
        this.toGiveAmount = toGiveAmount;
        this.price = price;
        this.tradeId = Trade.id;
        Trade.id++;
    }

    public Trade(Player buyer, Player seller, Item toGiveItem, int toGiveAmount, Item toGetItem, int toGetAmount) {
        this.buyer = buyer;
        this.seller = seller;
        this.toGiveItem = toGiveItem;
        this.toGiveAmount = toGiveAmount;
        this.toGetItem = toGetItem;
        this.toGetAmount = toGetAmount;
        this.tradeId = Trade.id;
        Trade.id++;
    }

    public void setResponse(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Player getBuyer() {
        return buyer;
    }
}
