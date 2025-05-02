package models;

import enums.design.TileType;

public class Tile {
    private TileType type;
    private final Player owner;

    public Tile(TileType type, Player owner) {
        this.type = type;
        this.owner = owner;
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
