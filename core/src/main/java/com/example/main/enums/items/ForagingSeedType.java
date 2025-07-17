// In main/enums/items/ForagingSeedType.java

package com.example.main.enums.items;

import com.example.main.enums.design.Season;

public enum ForagingSeedType implements ItemType {
    Jazz_Seeds("jazz seeds", Season.Spring, true, CropType.BlueJazz, 18),
    Carrot_Seeds("carrot seeds", Season.Spring, true, CropType.Carrot, 2),
    Cauliflower_Seeds("cauliflower seeds", Season.Spring, true, CropType.Cauliflower, 50),
    Coffee_Bean("coffee bean", Season.Spring, true, CropType.CoffeeBean, 100),
    Garlic_Seeds("garlic seeds", Season.Spring, true, CropType.Garlic, 30),
    Bean_Starter("bean starter", Season.Spring, true, CropType.GreenBean, 37),
    Kale_Seeds("kale seeds", Season.Spring, true, CropType.Kale, 43),
    Parsnip_Seeds("parsnip seeds", Season.Spring, true, CropType.Parsnip, 12),
    Potato_Seeds("potato seeds", Season.Spring, true, CropType.Potato, 31),
    Rhubarb_Seeds("rhubarb seeds", Season.Spring, true, CropType.Rhubarb, 50),
    Strawberry_Seeds("strawberry seeds", Season.Spring, true, CropType.Strawberry, 50),
    Tulip_Bulb("tulip bulb", Season.Spring, true, CropType.Tulip, 12),
    Rice_Shoot("rice shoot", Season.Spring, true, CropType.UnmilledRice, 30),

    Blueberry_Seeds("blueberry seeds", Season.Summer, true, CropType.Blueberry, 60),
    Corn_Seeds("corn seeds", Season.Summer, true, CropType.Corn, 90),
    Hops_Starter("hops starter", Season.Summer, true, CropType.Hops, 37),
    Pepper_Seeds("pepper seeds", Season.Summer, true, CropType.HotPepper, 25),
    Melon_Seeds("melon seeds", Season.Summer, true, CropType.Melon, 50),
    Poppy_Seeds("poppy seeds", Season.Summer, true, CropType.Poppy, 62),
    Radish_Seeds("radish seeds", Season.Summer, true, CropType.Radish, 25),
    Red_Cabbage_Seeds("red cabbage seeds", Season.Summer, true, CropType.RedCabbage, 75),
    Starfruit_Seeds("starfruit seeds", Season.Summer, true, CropType.Starfruit, 200),
    Spangle_Seeds("spangle seeds", Season.Summer, true, CropType.SummerSpangle, 31),
    Summer_Squash_Seeds("summer squash seeds", Season.Summer, true, CropType.SummerSquash, 5),
    Sunflower_Seeds("sunflower seeds", Season.Summer, true, CropType.Sunflower, 62),
    Tomato_Seeds("tomato seeds", Season.Summer, true, CropType.Tomato, 31),
    Wheat_Seeds("wheat seeds", Season.Summer, true, CropType.WHEAT, 6),

    Amaranth_Seeds("amaranth seeds", Season.Fall, true, CropType.Amaranth, 43),
    Artichoke_Seeds("artichoke seeds", Season.Fall, true, CropType.Artichoke, 22),
    Beet_Seeds("beet seeds", Season.Fall, true, CropType.Beet, 10),
    Bok_Choy_Seeds("bok choy seeds", Season.Fall, true, CropType.BokChoy, 31),
    Broccoli_Seeds("broccoli seeds", Season.Fall, true, CropType.Broccoli, 7),
    Cranberry_Seeds("cranberry seeds", Season.Fall, true, CropType.Cranberries, 150),
    Eggplant_Seeds("eggplant seeds", Season.Fall, true, CropType.Eggplant, 12),
    Fairy_Seeds("fairy seeds", Season.Fall, true, CropType.FairyRose, 125),
    Grape_Starter("grape starter", Season.Fall, true, CropType.Grape, 36),
    Pumpkin_Seeds("pumpkin seeds", Season.Fall, true, CropType.Pumpkin, 62),
    Yam_Seeds("yam seeds", Season.Fall, true, CropType.Yam, 37),
    Rare_Seed("rare seed", Season.Fall, true, CropType.SweetGemBerry, 500),

    Powdermelon_Seeds("powder melon seeds", Season.Winter, true, CropType.PowderMelon, 10),

    Ancient_Seeds("ancient seeds", Season.Special, true, CropType.AncientFruit, 250),
    Mixed_Seeds("mixed seeds", Season.Special, false, null, 0),

    Apple_Sapling("apple sapling", Season.Special, false, TreeType.APPLE_TREE, 2000),
    Apricot_Sapling("apricot sapling", Season.Special, false, TreeType.APRICOT_TREE, 1000),
    Cherry_Sapling("cherry sapling", Season.Special, false, TreeType.CHERRY_TREE, 1700),
    Orange_Sapling("orange sapling", Season.Special, false, TreeType.ORANGE_TREE, 2000),
    Peach_Sapling("peach sapling", Season.Special, false, TreeType.PEACH_TREE, 3000),
    Pomegranate_Sapling("pomegranate sapling", Season.Special, false, TreeType.POMEGRANATE_TREE, 3000),
    Maple_Seed("maple seed", Season.Special, true, TreeType.MAPLE_TREE, 0),
    Pine_Cone("pine cone", Season.Special, true, TreeType.PINE_TREE, 0),
    Mahogany_Seed("mahogany seed", Season.Special, true, TreeType.MAHOGANY_TREE, 0),
    Mango_Sapling("mango sapling", Season.Special, false, TreeType.MANGO_TREE, 0),
    Banana_Sapling("banana sapling", Season.Special, false, TreeType.BANANA_TREE, 0),
    Acorn("acorn", Season.Special, true, TreeType.OAK_TREE, 0),
    Mushroom_Tree_Seed("mushroom tree seed", Season.Special, true, TreeType.MUSHROOM_TREE, 0),
    Mystic_Tree_Seed("mystic tree seed", Season.Special, false, TreeType.MYSTIC_TREE, 0);

    private final String name;
    private final Season season;
    private final boolean isForaging;
    private final ItemType plantType;
    private final int price;

    ForagingSeedType(String name, Season season, boolean isForaging, ItemType plantType, int price) {
        this.name = name;
        this.season = season;
        this.isForaging = isForaging;
        this.plantType = plantType;
        this.price = price;
    }

    public ItemType getPlantType() {
        return plantType;
    }

    public Season getSeason() {
        return season;
    }

    public boolean isForaging() {
        return isForaging;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    @Override
    public String getEnumName() {
        return name();
    }
}
