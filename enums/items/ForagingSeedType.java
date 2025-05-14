package enums.items;

import enums.design.Season;

public enum ForagingSeedType implements ItemType {
    // Spring seeds
    JazzSeeds("jazz seeds", Season.Spring, true),
    CarrotSeeds("carrot seeds", Season.Spring, true),
    CauliflowerSeeds("cauliflower seeds", Season.Spring, true),
    CoffeeBean("coffee bean", Season.Spring, true),
    GarlicSeeds("garlic seeds", Season.Spring, true),
    BeanStarter("bean starter", Season.Spring, true),
    KaleSeeds("kale seeds", Season.Spring, true),
    ParsnipSeeds("parsnip seeds", Season.Spring, true),
    PotatoSeeds("potato seeds", Season.Spring, true),
    RhubarbSeeds("rhubarb seeds", Season.Spring, true),
    StrawberrySeeds("strawberry seeds", Season.Spring, true),
    TulipBulb("tulip bulb", Season.Spring, true),
    RiceShoot("rice shoot", Season.Spring, true),

    // Summer seeds
    BlueberrySeeds("blueberry seeds", Season.Summer, true),
    CornSeeds("corn seeds", Season.Summer, true),
    HopsStarter("hops starter", Season.Summer, true),
    PepperSeeds("pepper seeds", Season.Summer, true),
    MelonSeeds("melon seeds", Season.Summer, true),
    PoppySeeds("poppy seeds", Season.Summer, true),
    RadishSeeds("radish seeds", Season.Summer, true),
    RedCabbageSeeds("red cabbage seeds", Season.Summer, true),
    StarfruitSeeds("starfruit seeds", Season.Summer, true),
    SpangleSeeds("spangle seeds", Season.Summer, true),
    SummerSquashSeeds("summer squash seeds", Season.Summer, true),
    SunflowerSeeds("sunflower seeds", Season.Summer, true),
    TomatoSeeds("tomato seeds", Season.Summer, true),
    WheatSeeds("wheat seeds", Season.Summer, true),

    // Fall seeds
    AmaranthSeeds("amaranth seeds", Season.Fall, true),
    ArtichokeSeeds("artichoke seeds", Season.Fall, true),
    BeetSeeds("beet seeds", Season.Fall, true),
    BokChoySeeds("bok choy seeds", Season.Fall, true),
    BroccoliSeeds("broccoli seeds", Season.Fall, true),
    CranberrySeeds("cranberry seeds", Season.Fall, true),
    EggplantSeeds("eggplant seeds", Season.Fall, true),
    FairySeeds("fairy seeds", Season.Fall, true),
    GrapeStarter("grape starter", Season.Fall, true),
    PumpkinSeeds("pumpkin seeds", Season.Fall, true),
    YamSeeds("yam seeds", Season.Fall, true),
    RareSeed("rare seed", Season.Fall, true),

    // Winter seeds
    PowderMelonSeeds("powder melon seeds", Season.Winter, true),

    // Special seeds
    AncientSeeds("ancient seeds", Season.Special, true),
    MixedSeeds("mixed seeds", Season.Special, true),

    // Tree saplings and seeds
    AppleSapling("apple sapling", Season.Special, false),
    ApricotSapling("apricot sapling", Season.Special, false),
    CherrySapling("cherry sapling", Season.Special, false),
    OrangeSapling("orange sapling", Season.Special, false),
    PeachSapling("peach sapling", Season.Special, false),
    PomegranateSapling("pomegranate sapling", Season.Special, false),
    MapleSeed("maple seed", Season.Special, true),
    PineCone("pine cone", Season.Special, true),
    MahoganySeed("mahogany seed", Season.Special, true),
    MangoSapling("mango sapling", Season.Special, false),
    BananaSapling("banana sapling", Season.Special, false),
    Acorn("acorn", Season.Special, true),
    MushroomTreeSeed("mushroom tree seed", Season.Special, true),
    MysticTreeSeed("mystic tree seed", Season.Special, false);

    private final String name;
    private final Season season;
    private final boolean isForaging;

    ForagingSeedType(String name, Season season, boolean isForaging) {
        this.name = name;
        this.season = season;
        this.isForaging = isForaging;
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
}
