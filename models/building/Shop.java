package models.building;

import enums.design.ShopType;
import models.item.Item;

import java.util.HashMap;

public class Shop extends Building {
    private final ShopType shopType;
    private final HashMap<Item, Integer> items;

    public Shop(ShopType shopType) {
        this.shopType = shopType;
        items = new HashMap<>();

        // todo: add items from shopType and set stock
    }
}