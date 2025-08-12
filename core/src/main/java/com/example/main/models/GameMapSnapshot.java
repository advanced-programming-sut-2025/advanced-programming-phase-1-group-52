package com.example.main.models;

import com.example.main.enums.design.TileType;

import java.io.Serializable;


public class GameMapSnapshot implements Serializable {

    public TileType[][] tileTypes;
    public int[][] baseGroundMap;
    public int[][] grassVariantMap;
    public int[][] treeVariantMap;
    public int[][] stoneVariantMap;
    public int[][] waterVariantMap;
    public int[] playerHouseVariants;
    public int[] npcHouseVariants;


    public GameMapSnapshot() {}
}
