package com.example.main.models;

import com.example.main.enums.design.TileType;
import com.example.main.enums.items.Growable;
import com.example.main.enums.items.TreeType;
import com.example.main.models.building.Shop;
import com.example.main.models.item.Item;
import com.example.main.models.item.PlacedMachine;
import com.example.main.models.item.Seed;

import java.io.Serializable;


public class Tile implements Serializable {
    private final int x;
    private final int y;
    private TileType type;
    private Player owner;
    private Growable plant = null;
    private Tree tree ;
    private Seed seed = null;
    private Item item = null;
    private Shop shop = null;
    private PlacedMachine placedMachine = null;
    private boolean isPartOfGiantCrop = false;
    private int giantCropRootX = -1;
    private int giantCropRootY = -1;

    public Tile(int x, int y, TileType tileType, Player owner) {
        this.x = x;
        this.y = y;
        this.type = tileType;
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

    public Shop getShop() {
        return this.shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public PlacedMachine getPlacedMachine() {
        return placedMachine;
    }

    public void setPlacedMachine(PlacedMachine placedMachine) {
        this.placedMachine = placedMachine;
    }

    public boolean isPartOfGiantCrop() {
        return isPartOfGiantCrop;
    }

    public void setPartOfGiantCrop(boolean partOfGiantCrop) {
        isPartOfGiantCrop = partOfGiantCrop;
    }

    public int getGiantCropRootX() {
        return giantCropRootX;
    }

    public void setGiantCropRootX(int giantCropRootX) {
        this.giantCropRootX = giantCropRootX;
    }

    public int getGiantCropRootY() {
        return giantCropRootY;
    }

    public void setGiantCropRootY(int giantCropRootY) {
        this.giantCropRootY = giantCropRootY;
    }

    public void resetGiantCropStatus() {
        this.isPartOfGiantCrop = false;
        this.giantCropRootX = -1;
        this.giantCropRootY = -1;
    }

}
