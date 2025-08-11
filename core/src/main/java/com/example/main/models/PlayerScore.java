package com.example.main.models;

public class PlayerScore {
    private String playerName;
    private double score; // Use double to accommodate large numbers like balance or XP.
    private String scoreLabel; // A formatted string for display (e.g., "g", "XP")

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
        // Returns a nicely formatted string like "5000 g" or "1250 XP"
        return (int)score + " " + scoreLabel;
    }
}
