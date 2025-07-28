package com.example.main.enums.items;

import com.example.main.enums.design.Season;

public enum ForagingSeedType implements ItemType {
    // Spring Crop Seeds
    Jazz_Seeds("jazz seeds", Season.Spring, true, "Blue_Jazz", 18),
    Carrot_Seeds("carrot seeds", Season.Spring, true, "Carrot", 2),
    Cauliflower_Seeds("cauliflower seeds", Season.Spring, true, "Cauliflower", 50),
    Coffee_Bean("coffee bean", Season.Spring, true, "Coffee_Bean", 100),
    Garlic_Seeds("garlic seeds", Season.Spring, true, "Garlic", 30),
    Bean_Starter("bean starter", Season.Spring, true, "Green_Bean", 37),
    Kale_Seeds("kale seeds", Season.Spring, true, "Kale", 43),
    Parsnip_Seeds("parsnip seeds", Season.Spring, true, "Parsnip", 12),
    Potato_Seeds("potato seeds", Season.Spring, true, "Potato", 31),
    Rhubarb_Seeds("rhubarb seeds", Season.Spring, true, "Rhubarb", 50),
    Strawberry_Seeds("strawberry seeds", Season.Spring, true, "Strawberry", 50),
    Tulip_Bulb("tulip bulb", Season.Spring, true, "Tulip", 12),
    Rice_Shoot("rice shoot", Season.Spring, true, "Unmilled_Rice", 30),

    // Summer Crop Seeds
    Blueberry_Seeds("blueberry seeds", Season.Summer, true, "Blueberry", 60),
    Corn_Seeds("corn seeds", Season.Summer, true, "Corn", 90),
    Hops_Starter("hops starter", Season.Summer, true, "Hops", 37),
    Pepper_Seeds("pepper seeds", Season.Summer, true, "Hot_Pepper", 25),
    Melon_Seeds("melon seeds", Season.Summer, true, "Melon", 50),
    Poppy_Seeds("poppy seeds", Season.Summer, true, "Poppy", 62),
    Radish_Seeds("radish seeds", Season.Summer, true, "Radish", 25),
    Red_Cabbage_Seeds("red cabbage seeds", Season.Summer, true, "Red_Cabbage", 75),
    Starfruit_Seeds("starfruit seeds", Season.Summer, true, "Starfruit", 200),
    Spangle_Seeds("spangle seeds", Season.Summer, true, "Summer_Spangle", 31),
    Summer_Squash_Seeds("summer squash seeds", Season.Summer, true, "Summer_Squash", 5),
    Sunflower_Seeds("sunflower seeds", Season.Summer, true, "Sunflower", 62),
    Tomato_Seeds("tomato seeds", Season.Summer, true, "Tomato", 31),
    Wheat_Seeds("wheat seeds", Season.Summer, true, "Wheat", 6),

    // Fall Crop Seeds
    Amaranth_Seeds("amaranth seeds", Season.Fall, true, "Amaranth", 43),
    Artichoke_Seeds("artichoke seeds", Season.Fall, true, "Artichoke", 22),
    Beet_Seeds("beet seeds", Season.Fall, true, "Beet", 10),
    Bok_Choy_Seeds("bok choy seeds", Season.Fall, true, "Bok_Choy", 31),
    Broccoli_Seeds("broccoli seeds", Season.Fall, true, "Broccoli", 7),
    Cranberry_Seeds("cranberry seeds", Season.Fall, true, "Cranberries", 150),
    Eggplant_Seeds("eggplant seeds", Season.Fall, true, "Eggplant", 12),
    Fairy_Seeds("fairy seeds", Season.Fall, true, "Fairy_Rose", 125),
    Grape_Starter("grape starter", Season.Fall, true, "Grape", 36),
    Pumpkin_Seeds("pumpkin seeds", Season.Fall, true, "Pumpkin", 62),
    Yam_Seeds("yam seeds", Season.Fall, true, "Yam", 37),
    Rare_Seed("rare seed", Season.Fall, true, "Sweet_Gem_Berry", 500),

    // Winter Crop Seeds
    Powdermelon_Seeds("powder melon seeds", Season.Winter, true, "PowderMelon", 10),

    // Special Seeds
    Ancient_Seeds("ancient seeds", Season.Special, true, "Ancient_Fruit", 250),
    Mixed_Seeds("mixed seeds", Season.Special, false, null, 0),

    // --- CORRECTED TREE SEEDS ---
    Apple_Sapling("apple sapling", Season.Special, false, "Apple", 2000),
    Apricot_Sapling("apricot sapling", Season.Special, false, "Apricot", 1000),
    Cherry_Sapling("cherry sapling", Season.Special, false, "Cherry", 1700),
    Orange_Sapling("orange sapling", Season.Special, false, "Orange", 2000),
    Peach_Sapling("peach sapling", Season.Special, false, "Peach", 3000),
    Pomegranate_Sapling("pomegranate sapling", Season.Special, false, "Pomegranate", 3000),
    Mango_Sapling("mango sapling", Season.Special, false, "Mango", 0),
    Banana_Sapling("banana sapling", Season.Special, false, "Banana", 0),
    Maple_Seed("maple seed", Season.Special, true, "Maple", 0),
    Pine_Cone("pine cone", Season.Special, true, "Pine", 0),
    Mahogany_Seed("mahogany seed", Season.Special, true, "Mahogany", 0),
    Acorn("acorn", Season.Special, true, "Oak", 0),
    Mushroom_Tree_Seed("mushroom tree seed", Season.Special, true, "Mushroom", 0),
    Mystic_Tree_Seed("mystic tree seed", Season.Special, false, "Mystic", 0);

    // ... (rest of the file remains the same) ...

    private final String name;
    private final Season season;
    private final boolean isForaging;
    private final String plantTypeName;
    private final int price;

    ForagingSeedType(String name, Season season, boolean isForaging, String plantTypeName, int price) {
        this.name = name;
        this.season = season;
        this.isForaging = isForaging;
        this.plantTypeName = plantTypeName;
        this.price = price;
    }

    public ItemType getPlantType() {
        if (plantTypeName == null) {
            return null;
        }
        try {
            return CropType.valueOf(plantTypeName);
        } catch (IllegalArgumentException e) {
            try {
                return TreeType.valueOf(plantTypeName);
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }

    public Season getSeason() { return season; }
    public boolean isForaging() { return isForaging; }
    @Override public boolean isTool() { return false; }
    @Override public String getName() { return this.name; }
    public int getPrice() { return this.price; }
    @Override public String getEnumName() { return name(); }
}
