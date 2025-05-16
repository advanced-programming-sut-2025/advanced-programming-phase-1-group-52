package models;

import enums.design.TileType;
import enums.items.Growable;
import models.item.Item;
import models.item.Seed;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;
    private final Player owner;
    private Growable plant;
    private Tree tree = null;
    private Seed seed = null;
    private Item item = null;

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

    public Growable getPlant() {
        return plant;
    }

    public void setPlant(Growable plant) {
        this.plant = plant;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
