package models;

import enums.design.TileType;
import enums.items.FruitType;
import enums.items.Growable;
import enums.items.TreeType;
import models.building.Shop;
import models.item.Item;
import models.item.Seed;

import java.util.Arrays;
import java.util.Random;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;
    private Player owner;
    private Growable plant = null;
    private Tree tree ;
    private Seed seed = null;
    private Item item = null;
    private Shop shop = null;

    public Tile(int x, int y, TileType tileType, Player owner) {
        this.x = x;
        this.y = y;
        this.type = tileType;
        this.owner = owner;
        if(tileType.equals(TileType.Tree)) {
            this.tree = new Tree(TreeType.APPLE_TREE);
        }
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

    public Shop getShop() {
        return this.shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
