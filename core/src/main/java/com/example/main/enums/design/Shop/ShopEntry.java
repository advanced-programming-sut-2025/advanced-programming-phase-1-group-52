package com.example.main.enums.design.Shop;

import com.example.main.enums.items.ItemType;

public interface ShopEntry {
    String getDisplayName();
    String getDescription();
    int getPrice();
    int getDailyLimit();
    ItemType getItemType();
}
