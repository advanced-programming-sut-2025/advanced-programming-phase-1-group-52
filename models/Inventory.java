package models;

import enums.items.Items;

import java.util.HashMap;

public class Inventory {
    private final HashMap<Items, Integer> items;

    public Inventory() {
        items = new HashMap<Items, Integer>();
    }
}
