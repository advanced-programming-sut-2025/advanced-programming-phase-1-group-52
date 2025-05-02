package models.building;

import models.Player;

public class House extends Building {
    private final Player owner;
    private final int cornerX;
    private final int cornerY;

    public House(Player owner, int cornerX, int cornerY) {
        this.owner = owner;
        this.cornerX = cornerX;
        this.cornerY = cornerY;
    }
}
