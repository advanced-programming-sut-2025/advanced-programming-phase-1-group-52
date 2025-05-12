package models;

import enums.items.Backpacks;
import java.util.ArrayList;
import models.item.Item;

public class Inventory {
    private Backpacks backpack = Backpacks.PrimitiveBackpack;
    private ArrayList<Item> items = new ArrayList<>();
}
