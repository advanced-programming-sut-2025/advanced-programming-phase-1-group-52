package com.example.main.models;

import com.example.main.enums.design.TileType;

import java.io.Serializable;

/**
 * A lightweight, serializable "blueprint" of the GameMap, safe for network transmission.
 * It contains only simple data types that GSON can easily handle.
 */
public class GameMapSnapshot implements Serializable {

    public TileType[][] tileTypes;
    public int[][] baseGroundMap;
    public int[][] grassVariantMap;
    public int[][] treeVariantMap;
    public int[][] stoneVariantMap;
    public int[][] waterVariantMap;
    public int[] playerHouseVariants;
    public int[] npcHouseVariants;

    // No-argument constructor is good practice for serialization libraries
    public GameMapSnapshot() {}
}
