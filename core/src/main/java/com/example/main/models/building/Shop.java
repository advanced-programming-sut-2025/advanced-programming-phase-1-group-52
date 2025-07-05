package com.example.main.models.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.main.enums.design.Shop.Blacksmith;
import com.example.main.enums.design.Shop.CarpentersShop;
import com.example.main.enums.design.Shop.FishShop;
import com.example.main.enums.design.Shop.JojaMart;
import com.example.main.enums.design.Shop.MarniesRanch;
import com.example.main.enums.design.Shop.PierresGeneralStore;
import com.example.main.enums.design.Shop.ShopEntry;
import com.example.main.enums.design.Shop.TheStardropSaloon;
import com.example.main.enums.design.ShopType;

public class Shop extends Building {
    private final ShopType shopType;
    private final ArrayList<ShopEntry> entries;
    private final Map<String, Integer> dailyLimits;
    private HashMap<String, Integer> availableStocks;

    public Shop(ShopType shopType) {
        this.shopType = shopType;
        this.entries = new ArrayList<>();
        this.dailyLimits = new HashMap<>();
        initializeStock();
        this.availableStocks = new HashMap<>(dailyLimits);
    }

    private void initializeStock() {
        ShopEntry[] catalog;
        catalog = switch (shopType) {
            case Blacksmith -> Blacksmith.values();
            case CarpentersShop -> CarpentersShop.values();
            case FishShop -> FishShop.values();
            case JojaMart -> JojaMart.values();
            case MarniesRanch -> MarniesRanch.values();
            case PierresGeneralStore -> PierresGeneralStore.values();
            case TheStardropSaloon -> TheStardropSaloon.values();
            default -> new ShopEntry[0];
        };
        for (ShopEntry e : catalog) {
            entries.add(e);
            dailyLimits.put(e.getDisplayName(), e.getDailyLimit());
        }
    }

    public HashMap<String, Integer> getStock() {
        return this.availableStocks;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public ArrayList<ShopEntry> getEntries() {
        return entries;
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
