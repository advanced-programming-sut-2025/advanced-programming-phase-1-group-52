package models;

import enums.design.TileType;
import enums.items.Growable;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;
    private final Player owner;
    private Growable plant;

    public Tile(int x, int y, TileType type, Player owner) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.owner = owner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getType() {
        return type;
    }

    public Player getOwner() {
        return owner;
    }

    public void setType(TileType type) {
        this.type = type;
    }
}
