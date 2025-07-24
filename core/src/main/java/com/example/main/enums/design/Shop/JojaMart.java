package com.example.main.enums.design.Shop;

import com.example.main.enums.design.Season;
import com.example.main.enums.items.*;

public enum JojaMart implements ShopEntry {
    // Permanent Stock
    JojaCola_Product(null, "Joja Cola", "The flagship product of Joja corporation.",
        75, Integer.MAX_VALUE, MaterialType.Joja_Cola),
    Ancient_Seed_Product(null, "Ancient Seed", "Could these still grow?",
        500, 1, ForagingSeedType.Ancient_Seeds),
    Grass_Starter_Product(null, "Grass Starter", "Place this on your farm to start a new patch of grass.",
        125, Integer.MAX_VALUE, CraftingMachineType.Grass_Starter),
    Sugar_Product(null, "Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.",
        125, Integer.MAX_VALUE, MaterialType.Sugar),
    Wheat_Flour_Product(null, "Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.",
        125, Integer.MAX_VALUE, MaterialType.Wheat_Flour),
    Rice_Product(null, "Rice", "A basic grain often served under vegetables.",
        250, Integer.MAX_VALUE, MaterialType.Rice),

    // Spring Stock
    Parsnip_Seeds_Product(Season.Spring, "Parsnip Seeds", "Plant these in the spring. Takes 4 days to mature.",
        25, 5, ForagingSeedType.Parsnip_Seeds),
    Bean_Starter_Product(Season.Spring, "Bean Starter", "Plant these in the spring. Takes 10 days to mature, but keeps producing.",
        75, 5, ForagingSeedType.Bean_Starter),
    Cauliflower_Seeds_Product(Season.Spring, "Cauliflower Seeds", "Plant these in the spring. Takes 12 days.",
        100, 5, ForagingSeedType.Cauliflower_Seeds),
    Potato_Seeds_Product(Season.Spring, "Potato Seeds", "Plant these in the spring. Takes 6 days, may yield extra.",
        62, 5, ForagingSeedType.Potato_Seeds),
    Strawberry_Seeds_Product(Season.Spring, "Strawberry Seeds", "Plant in spring. Takes 8 days, keeps producing.",
        100, 5, ForagingSeedType.Strawberry_Seeds),
    Tulip_Bulb_Product(Season.Spring, "Tulip Bulb", "Plant in spring. Takes 6 days.",
        25, 5, ForagingSeedType.Tulip_Bulb),
    Kale_Seeds_Product(Season.Spring, "Kale Seeds", "Plant in spring. Takes 6 days. Harvest with scythe.",
        87, 5, ForagingSeedType.Kale_Seeds),
    Coffee_Beans_Spring_Product(Season.Spring, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
        200, 1, ForagingSeedType.Coffee_Bean),
    Carrot_Seeds_Product(Season.Spring, "Carrot Seeds", "Plant in spring. Takes 3 days.",
        5, 10, ForagingSeedType.Carrot_Seeds),
    Rhubarb_Seeds_Product(Season.Spring, "Rhubarb Seeds", "Plant in spring. Takes 13 days.",
        100, 5, ForagingSeedType.Rhubarb_Seeds),
    Jazz_Seeds_Product(Season.Spring, "Jazz Seeds", "Plant in spring. Takes 7 days.",
        37, 5, ForagingSeedType.Jazz_Seeds),

    // Summer Stock
    Tomato_Seeds_Product(Season.Summer, "Tomato Seeds", "Plant in summer. Takes 11 days, keeps producing.",
        62, 5, ForagingSeedType.Tomato_Seeds),
    Pepper_Seeds_Product(Season.Summer, "Pepper Seeds", "Plant in summer. Takes 5 days, keeps producing.",
        50, 5, ForagingSeedType.Pepper_Seeds),
    Wheat_Seeds_Summer_Product(Season.Summer, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
        12, 10, ForagingSeedType.Wheat_Seeds),
    Summer_Squash_Seeds_Product(Season.Summer, "Summer Squash Seeds", "Plant in summer. Takes 6 days, keeps producing.",
        10, 10, ForagingSeedType.Summer_Squash_Seeds),
    Radish_Seeds_Product(Season.Summer, "Radish Seeds", "Plant in summer. Takes 6 days.",
        50, 5, ForagingSeedType.Radish_Seeds),
    Melon_Seeds_Product(Season.Summer, "Melon Seeds", "Plant in summer. Takes 12 days.",
        100, 5, ForagingSeedType.Melon_Seeds),
    Hops_Starter_Product(Season.Summer, "Hops Starter", "Summer. Takes 11 days, keeps producing. Trellis.",
        75, 5, ForagingSeedType.Hops_Starter),
    Poppy_Seeds_Product(Season.Summer, "Poppy Seeds", "Summer. Bright red flower in 7 days.",
        125, 5, ForagingSeedType.Poppy_Seeds),
    Spangle_Seeds_Product(Season.Summer, "Spangle Seeds", "Summer. Tropical flower in 8 days.",
        62, 5, ForagingSeedType.Spangle_Seeds),
    Starfruit_Seeds_Product(Season.Summer, "Starfruit Seeds", "Summer. Takes 13 days.",
        400, 5, ForagingSeedType.Starfruit_Seeds),
    Coffee_Beans_Summer_Product(Season.Summer, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
        200, 1, ForagingSeedType.Coffee_Bean),
    Sunflower_Seeds_Summer_Product(Season.Summer, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
        125, 5, ForagingSeedType.Sunflower_Seeds),

    // Fall Stock
    Corn_Seeds_Product(Season.Fall, "Corn Seeds", "Summer/Fall. Takes 14 days, keeps producing.",
        187, 5, ForagingSeedType.Corn_Seeds),
    Eggplant_Seeds_Product(Season.Fall, "Eggplant Seeds", "Fall. Takes 5 days, keeps producing.",
        25, 5, ForagingSeedType.Eggplant_Seeds),
    Pumpkin_Seeds_Product(Season.Fall, "Pumpkin Seeds", "Fall. Takes 13 days.",
        125, 5, ForagingSeedType.Pumpkin_Seeds),
    Broccoli_Seeds_Product(Season.Fall, "Broccoli Seeds", "Fall. Takes 8 days, keeps producing.",
        15, 5, ForagingSeedType.Broccoli_Seeds),
    Amaranth_Seeds_Product(Season.Fall, "Amaranth Seeds", "Fall. Takes 7 days. Scythe harvest.",
        87, 5, ForagingSeedType.Amaranth_Seeds),
    Grape_Starter_Product(Season.Fall, "Grape Starter", "Fall. Takes 10 days, keeps producing. Trellis.",
        75, 5, ForagingSeedType.Grape_Starter),
    Beet_Seeds_Product(Season.Fall, "Beet Seeds", "Fall. Takes 6 days.",
        20, 5, ForagingSeedType.Beet_Seeds),
    Yam_Seeds_Product(Season.Fall, "Yam Seeds", "Fall. Takes 10 days.",
        75, 5, ForagingSeedType.Yam_Seeds),
    Bok_Choy_Seeds_Product(Season.Fall, "Bok Choy Seeds", "Fall. Takes 4 days.",
        62, 5, ForagingSeedType.Bok_Choy_Seeds),
    Cranberry_Seeds_Product(Season.Fall, "Cranberry Seeds", "Fall. Takes 7 days, keeps producing.",
        300, 5, ForagingSeedType.Cranberry_Seeds),
    Sunflower_Seeds_Fall_Product(Season.Fall, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
        125, 5, ForagingSeedType.Sunflower_Seeds),
    Fairy_Seeds_Product(Season.Fall, "Fairy Seeds", "Fall. Mysterious flower in 12 days.",
        250, 5, ForagingSeedType.Fairy_Seeds),
    Rare_Seed_Product(Season.Fall, "Rare Seed", "Fall. Takes all season to grow.",
        1000, 1, ForagingSeedType.Rare_Seed),
    Wheat_Seeds_Fall_Product(Season.Fall, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
        12, 5, ForagingSeedType.Wheat_Seeds),

    // Winter Stock
    Powdermelon_Seeds_Product(Season.Winter, "Powdermelon Seeds", "Winter. Takes 7 days to grow.",
        20, 10, ForagingSeedType.Powdermelon_Seeds);


    private final Season season;
    private final String displayName;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final ItemType itemType;

    JojaMart(Season season, String displayName, String description, int price, int dailyLimit, ItemType itemType) {
        this.season = season;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.itemType = itemType;
    }

    public Season getSeason() { return season; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }
    @Override
    public ItemType getItemType() { return itemType; }

    @Override
    public String toString() {
        return this.displayName + " - Price: " + this.price;
    }
}
