package enums.items;

import enums.design.Season;

public enum ForagingSeedType implements ItemType {
    JazzSeeds("jazz seeds", Season.Spring, true, CropType.BlueJazz),
    CarrotSeeds("carrot seeds", Season.Spring, true, CropType.Carrot),
    CauliflowerSeeds("cauliflower seeds", Season.Spring, true, CropType.Cauliflower),
    CoffeeBean("coffee bean", Season.Spring, true, CropType.CoffeeBean),
    GarlicSeeds("garlic seeds", Season.Spring, true, CropType.Garlic),
    BeanStarter("bean starter", Season.Spring, true, CropType.GreenBean),
    KaleSeeds("kale seeds", Season.Spring, true, CropType.Kale),
    ParsnipSeeds("parsnip seeds", Season.Spring, true, CropType.Parsnip),
    PotatoSeeds("potato seeds", Season.Spring, true, CropType.Potato),
    RhubarbSeeds("rhubarb seeds", Season.Spring, true, CropType.Rhubarb),
    StrawberrySeeds("strawberry seeds", Season.Spring, true, CropType.Strawberry),
    TulipBulb("tulip bulb", Season.Spring, true, CropType.Tulip),
    RiceShoot("rice shoot", Season.Spring, true, CropType.UnmilledRice),

    BlueberrySeeds("blueberry seeds", Season.Summer, true, CropType.Blueberry),
    CornSeeds("corn seeds", Season.Summer, true, CropType.Corn),
    HopsStarter("hops starter", Season.Summer, true, CropType.Hops),
    PepperSeeds("pepper seeds", Season.Summer, true, CropType.HotPepper),
    MelonSeeds("melon seeds", Season.Summer, true, CropType.Melon),
    PoppySeeds("poppy seeds", Season.Summer, true, CropType.Poppy),
    RadishSeeds("radish seeds", Season.Summer, true, CropType.Radish),
    RedCabbageSeeds("red cabbage seeds", Season.Summer, true, CropType.RedCabbage),
    StarfruitSeeds("starfruit seeds", Season.Summer, true, CropType.Starfruit),
    SpangleSeeds("spangle seeds", Season.Summer, true, CropType.SummerSpangle),
    SummerSquashSeeds("summer squash seeds", Season.Summer, true, CropType.SummerSquash),
    SunflowerSeeds("sunflower seeds", Season.Summer, true, CropType.Sunflower),
    TomatoSeeds("tomato seeds", Season.Summer, true, CropType.Tomato),
    WheatSeeds("wheat seeds", Season.Summer, true, CropType.WHEAT),

    AmaranthSeeds("amaranth seeds", Season.Fall, true, CropType.Amaranth),
    ArtichokeSeeds("artichoke seeds", Season.Fall, true, CropType.Artichoke),
    BeetSeeds("beet seeds", Season.Fall, true, CropType.Beet),
    BokChoySeeds("bok choy seeds", Season.Fall, true, CropType.BokChoy),
    BroccoliSeeds("broccoli seeds", Season.Fall, true, CropType.Broccoli),
    CranberrySeeds("cranberry seeds", Season.Fall, true, CropType.Cranberries),
    EggplantSeeds("eggplant seeds", Season.Fall, true, CropType.Eggplant),
    FairySeeds("fairy seeds", Season.Fall, true, CropType.FairyRose),
    GrapeStarter("grape starter", Season.Fall, true, CropType.Grape),
    PumpkinSeeds("pumpkin seeds", Season.Fall, true, CropType.Pumpkin),
    YamSeeds("yam seeds", Season.Fall, true, CropType.Yam),
    RareSeed("rare seed", Season.Fall, true, CropType.SweetGemBerry),

    PowderMelonSeeds("powder melon seeds", Season.Winter, true, CropType.PowderMelon),

    AncientSeeds("ancient seeds", Season.Special, true, CropType.AncientFruit),
    MixedSeeds("mixed seeds", Season.Special, false, null),

    AppleSapling("apple sapling", Season.Special, false, TreeType.APPLE_TREE),
    ApricotSapling("apricot sapling", Season.Special, false, TreeType.APRICOT_TREE),
    CherrySapling("cherry sapling", Season.Special, false, TreeType.CHERRY_TREE),
    OrangeSapling("orange sapling", Season.Special, false, TreeType.ORANGE_TREE),
    PeachSapling("peach sapling", Season.Special, false, TreeType.PEACH_TREE),
    PomegranateSapling("pomegranate sapling", Season.Special, false, TreeType.POMEGRANATE_TREE),
    MapleSeed("maple seed", Season.Special, true, TreeType.MAPLE_TREE),
    PineCone("pine cone", Season.Special, true, TreeType.PINE_TREE),
    MahoganySeed("mahogany seed", Season.Special, true, TreeType.MAHOGANY_TREE),
    MangoSapling("mango sapling", Season.Special, false, TreeType.MANGO_TREE),
    BananaSapling("banana sapling", Season.Special, false, TreeType.BANANA_TREE),
    Acorn("acorn", Season.Special, true, TreeType.OAK_TREE),
    MushroomTreeSeed("mushroom tree seed", Season.Special, true, TreeType.MUSHROOM_TREE),
    MysticTreeSeed("mystic tree seed", Season.Special, false, TreeType.MYSTIC_TREE);

    private final String name;
    private final Season season;
    private final boolean isForaging;
    private final ItemType plantType;

    ForagingSeedType(String name, Season season, boolean isForaging, ItemType plantType) {
        this.name = name;
        this.season = season;
        this.isForaging = isForaging;
        this.plantType = plantType;
    }

    public ItemType getPlantType() {
        return plantType;
    }

    public Season getSeasons() {
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
}
