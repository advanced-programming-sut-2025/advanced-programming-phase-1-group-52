package enums.design.Shop;

import enums.items.ItemType;

public interface ShopEntry {
    String getDisplayName();
    String getDescription();
    int getPrice();
    int getDailyLimit();
    ItemType getItemType();
}
