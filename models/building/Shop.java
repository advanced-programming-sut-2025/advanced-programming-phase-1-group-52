package models.building;

import enums.design.ShopType;
import enums.design.Shop.ShopEntry;
import enums.design.Shop.Blacksmith;
import enums.design.Shop.CarpentersShop;
import enums.design.Shop.FishShop;
import enums.design.Shop.JojaMart;
import enums.design.Shop.MarniesRanch;
import enums.design.Shop.PierresGeneralStore;
import enums.design.Shop.TheStardropSaloon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop extends Building {
    private final ShopType shopType;
    private final List<ShopEntry> entries;
    private final Map<String, Integer> dailyLimits;
    private Map<String, Integer> availableStocks;

    public Shop(ShopType shopType) {
        this.shopType = shopType;
        this.entries = new ArrayList<>();
        this.dailyLimits = new HashMap<>();
        initializeStock();
        this.availableStocks = new HashMap<>(dailyLimits);
    }

    private void initializeStock() {
        ShopEntry[] catalog;
        switch (shopType) {
            case Blacksmith:          catalog = Blacksmith.values();          break;
            case CarpentersShop:      catalog = CarpentersShop.values();      break;
            case FishShop:            catalog = FishShop.values();            break;
            case JojaMart:            catalog = JojaMart.values();            break;
            case MarniesRanch:        catalog = MarniesRanch.values();        break;
            case PierresGeneralStore: catalog = PierresGeneralStore.values(); break;
            case TheStardropSaloon:   catalog = TheStardropSaloon.values();   break;
            default:                  catalog = new ShopEntry[0];            break;
        }
        for (ShopEntry e : catalog) {
            entries.add(e);
            dailyLimits.put(e.getDisplayName(), e.getDailyLimit());
        }
    }

    public ShopType getShopType() {
        return shopType;
    }

    public List<ShopEntry> getEntries() {
        return List.copyOf(entries);
    }

    public boolean hasEntry(String name) {
        return dailyLimits.containsKey(name);
    }

    public ShopEntry findEntry(String name) {
        for (ShopEntry e : entries) {
            if (e.getDisplayName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    public int getDailyLimit(String name) {
        return dailyLimits.getOrDefault(name, 0);
    }

    public int getAvailableStock(String name) {
        return availableStocks.getOrDefault(name, 0);
    }

    public boolean purchase(String itemName, int quantity) {
        int available = getAvailableStock(itemName);
        if (available >= quantity) {
            availableStocks.put(itemName, available - quantity);
            return true;
        }
        return false;
    }

    public void resetStock() {
        this.availableStocks = new HashMap<>(dailyLimits);
    }
}
