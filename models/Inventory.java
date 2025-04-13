package models;

import enums.items.Backpacks;
import models.item.Item;

import java.util.ArrayList;

public class Inventory {
    private Backpacks backpack = Backpacks.PrimitiveBackpack;
    private ArrayList<Item> items = new ArrayList<>();
}
