package com.example.main.models.building;

import com.example.main.models.Player;

public class GreenHouse extends Building {
    private final Player owner;
    private final int cornerX;
    private final int cornerY;
    private boolean isBroken = true;

    public GreenHouse(Player owner, int cornerX, int cornerY) {
        this.owner = owner;
        this.cornerX = cornerX;
        this.cornerY = cornerY;
    }

    public void repair() {
        this.isBroken = false;
    }
}
