package models.building;

import enums.design.ShopType;
import java.util.ArrayList;
import models.item.Item;

public class Shop extends Building {
    private final ShopType shopType;
    private final ArrayList<Item> items;

    public Shop(ShopType shopType) {
        this.shopType = shopType;
        items = new ArrayList<>();

        // todo: add items from shopType and set stock
    }
}