package models;

import enums.design.Tiles;

public class Tile {
    private Tiles type;
    private final int x;
    private final int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setType(Tiles type) {
        this.type = type;
    }
}
