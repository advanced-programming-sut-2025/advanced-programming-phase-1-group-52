package com.example.main.enums.items;

import com.example.main.enums.design.Season;

public enum ForagingSeedType implements ItemType {
    JazzSeeds("jazz seeds", Season.Spring, true, CropType.BlueJazz, 18),
    CarrotSeeds("carrot seeds", Season.Spring, true, CropType.Carrot, 2),
    CauliflowerSeeds("cauliflower seeds", Season.Spring, true, CropType.Cauliflower, 50),
    CoffeeBean("coffee bean", Season.Spring, true, CropType.CoffeeBean, 100),
    GarlicSeeds("garlic seeds", Season.Spring, true, CropType.Garlic, 30),
    BeanStarter("bean starter", Season.Spring, true, CropType.GreenBean, 37),
    KaleSeeds("kale seeds", Season.Spring, true, CropType.Kale, 43),
    ParsnipSeeds("parsnip seeds", Season.Spring, true, CropType.Parsnip, 12),
    PotatoSeeds("potato seeds", Season.Spring, true, CropType.Potato, 31),
    RhubarbSeeds("rhubarb seeds", Season.Spring, true, CropType.Rhubarb, 50),
    StrawberrySeeds("strawberry seeds", Season.Spring, true, CropType.Strawberry, 50),
    TulipBulb("tulip bulb", Season.Spring, true, CropType.Tulip, 12),
    RiceShoot("rice shoot", Season.Spring, true, CropType.UnmilledRice, 30),

    BlueberrySeeds("blueberry seeds", Season.Summer, true, CropType.Blueberry, 60),
    CornSeeds("corn seeds", Season.Summer, true, CropType.Corn, 90),
    HopsStarter("hops starter", Season.Summer, true, CropType.Hops, 37),
    PepperSeeds("pepper seeds", Season.Summer, true, CropType.HotPepper, 25),
    MelonSeeds("melon seeds", Season.Summer, true, CropType.Melon, 50),
    PoppySeeds("poppy seeds", Season.Summer, true, CropType.Poppy, 62),
    RadishSeeds("radish seeds", Season.Summer, true, CropType.Radish, 25),
    RedCabbageSeeds("red cabbage seeds", Season.Summer, true, CropType.RedCabbage, 75),
    StarfruitSeeds("starfruit seeds", Season.Summer, true, CropType.Starfruit, 200),
    SpangleSeeds("spangle seeds", Season.Summer, true, CropType.SummerSpangle, 31),
    SummerSquashSeeds("summer squash seeds", Season.Summer, true, CropType.SummerSquash, 5),
    SunflowerSeeds("sunflower seeds", Season.Summer, true, CropType.Sunflower, 62),
    TomatoSeeds("tomato seeds", Season.Summer, true, CropType.Tomato, 31),
    WheatSeeds("wheat seeds", Season.Summer, true, CropType.WHEAT, 6),

    AmaranthSeeds("amaranth seeds", Season.Fall, true, CropType.Amaranth, 43),
    ArtichokeSeeds("artichoke seeds", Season.Fall, true, CropType.Artichoke, 22),
    BeetSeeds("beet seeds", Season.Fall, true, CropType.Beet, 10),
    BokChoySeeds("bok choy seeds", Season.Fall, true, CropType.BokChoy, 31),
    BroccoliSeeds("broccoli seeds", Season.Fall, true, CropType.Broccoli, 7),
    CranberrySeeds("cranberry seeds", Season.Fall, true, CropType.Cranberries, 150),
    EggplantSeeds("eggplant seeds", Season.Fall, true, CropType.Eggplant, 12),
    FairySeeds("fairy seeds", Season.Fall, true, CropType.FairyRose, 125),
    GrapeStarter("grape starter", Season.Fall, true, CropType.Grape, 36),
    PumpkinSeeds("pumpkin seeds", Season.Fall, true, CropType.Pumpkin, 62),
    YamSeeds("yam seeds", Season.Fall, true, CropType.Yam, 37),
    RareSeed("rare seed", Season.Fall, true, CropType.SweetGemBerry, 500),

    PowderMelonSeeds("powder melon seeds", Season.Winter, true, CropType.PowderMelon, 10),

    AncientSeeds("ancient seeds", Season.Special, true, CropType.AncientFruit, 250),
    MixedSeeds("mixed seeds", Season.Special, false, null, 0),

    AppleSapling("apple sapling", Season.Special, false, TreeType.APPLE_TREE, 2000),
    ApricotSapling("apricot sapling", Season.Special, false, TreeType.APRICOT_TREE, 1000),
    CherrySapling("cherry sapling", Season.Special, false, TreeType.CHERRY_TREE, 1700),
    OrangeSapling("orange sapling", Season.Special, false, TreeType.ORANGE_TREE, 2000),
    PeachSapling("peach sapling", Season.Special, false, TreeType.PEACH_TREE, 3000),
    PomegranateSapling("pomegranate sapling", Season.Special, false, TreeType.POMEGRANATE_TREE, 3000),
    MapleSeed("maple seed", Season.Special, true, TreeType.MAPLE_TREE, 0),
    PineCone("pine cone", Season.Special, true, TreeType.PINE_TREE, 0),
    MahoganySeed("mahogany seed", Season.Special, true, TreeType.MAHOGANY_TREE, 0),
    MangoSapling("mango sapling", Season.Special, false, TreeType.MANGO_TREE, 0),
    BananaSapling("banana sapling", Season.Special, false, TreeType.BANANA_TREE, 0),
    Acorn("acorn", Season.Special, true, TreeType.OAK_TREE, 0),
    MushroomTreeSeed("mushroom tree seed", Season.Special, true, TreeType.MUSHROOM_TREE, 0),
    MysticTreeSeed("mystic tree seed", Season.Special, false, TreeType.MYSTIC_TREE, 0);

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

    @Override
    public String getEnumName() {
        return name();
    }

    public int getPrice() {
        return this.price;
    }
}
