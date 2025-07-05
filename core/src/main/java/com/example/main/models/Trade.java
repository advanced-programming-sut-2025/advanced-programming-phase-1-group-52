package com.example.main.models;


public abstract class Trade {
    protected static int tradeCounter = 1;
    protected final Player buyer;
    protected final Player seller;
    protected final int tradeId;
    protected boolean isAccepted = false;
    protected boolean isAnswered = false;

    public Trade(Player buyer, Player seller) {
        this.buyer = buyer;
        this.seller = seller;
        this.tradeId = tradeCounter++;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public Player getBuyer() {
        return buyer;
    }

    public Player getSeller() {
        return seller;
    }

    public int getTradeId() {
        return tradeId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }
}
