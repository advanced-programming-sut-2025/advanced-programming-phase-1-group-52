package com.example.main.models;


public abstract class Trade {
    protected static int tradeCounter = 1;
    protected final Player buyer;
    protected final Player seller;
    protected final Player sender;
    protected final Player receiver;
    protected final int tradeId;
    protected boolean isAccepted = false;
    protected boolean isAnswered = false;

    public Trade(Player sender, Player receiver, Player buyer, Player seller) {
        this.sender = sender;
        this.receiver = receiver;
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

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
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
