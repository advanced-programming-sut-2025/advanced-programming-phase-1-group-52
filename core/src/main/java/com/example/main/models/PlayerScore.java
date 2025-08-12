package com.example.main.models;

public class PlayerScore {
    private String playerName;
    private double score;
    private String scoreLabel;

    public PlayerScore(String playerName, double score, String scoreLabel) {
        this.playerName = playerName;
        this.score = score;
        this.scoreLabel = scoreLabel;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getScore() {
        return score;
    }

    public String getFormattedScore() {

        return (int)score + " " + scoreLabel;
    }
}
