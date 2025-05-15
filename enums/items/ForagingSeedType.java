package enums.items;

import enums.design.Season;

import java.util.List;

public enum ForagingSeedType implements ItemType {
    // Spring seeds
    JazzSeeds("jazz seeds", List.of(Season.Spring), true),
    CarrotSeeds("carrot seeds", List.of(Season.Spring), true),
    CauliflowerSeeds("cauliflower seeds", List.of(Season.Spring), true),
    CoffeeBean("coffee bean", List.of(Season.Spring), true),
    GarlicSeeds("garlic seeds", List.of(Season.Spring), true),
    BeanStarter("bean starter", List.of(Season.Spring), true),
    KaleSeeds("kale seeds", List.of(Season.Spring), true),
    ParsnipSeeds("parsnip seeds", List.of(Season.Spring), true),
    PotatoSeeds("potato seeds", List.of(Season.Spring), true),
    RhubarbSeeds("rhubarb seeds", List.of(Season.Spring), true),
    StrawberrySeeds("strawberry seeds", List.of(Season.Spring), true),
    TulipBulb("tulip bulb", List.of(Season.Spring), true),
    RiceShoot("rice shoot", List.of(Season.Spring), true),

    // Summer seeds
    BlueberrySeeds("blueberry seeds", List.of(Season.Summer), true),
    CornSeeds("corn seeds", List.of(Season.Summer), true),
    HopsStarter("hops starter", List.of(Season.Summer), true),
    PepperSeeds("pepper seeds", List.of(Season.Summer), true),
    MelonSeeds("melon seeds", List.of(Season.Summer), true),
    PoppySeeds("poppy seeds", List.of(Season.Summer), true),
    RadishSeeds("radish seeds", List.of(Season.Summer), true),
    RedCabbageSeeds("red cabbage seeds", List.of(Season.Summer), true),
    StarfruitSeeds("starfruit seeds", List.of(Season.Summer), true),
    SpangleSeeds("spangle seeds", List.of(Season.Summer), true),
    SummerSquashSeeds("summer squash seeds", List.of(Season.Summer), true),
    SunflowerSeeds("sunflower seeds", List.of(Season.Summer, Season.Fall), true),
    TomatoSeeds("tomato seeds", List.of(Season.Summer), true),
    WheatSeeds("wheat seeds", List.of(Season.Summer, Season.Fall), true),

    // Fall seeds
    AmaranthSeeds("amaranth seeds", List.of(Season.Fall), true),
    ArtichokeSeeds("artichoke seeds", List.of(Season.Fall), true),
    BeetSeeds("beet seeds", List.of(Season.Fall), true),
    BokChoySeeds("bok choy seeds", List.of(Season.Fall), true),
    BroccoliSeeds("broccoli seeds", List.of(Season.Fall), true),
    CranberrySeeds("cranberry seeds", List.of(Season.Fall), true),
    EggplantSeeds("eggplant seeds", List.of(Season.Fall), true),
    FairySeeds("fairy seeds", List.of(Season.Fall), true),
    GrapeStarter("grape starter", List.of(Season.Fall), true),
    PumpkinSeeds("pumpkin seeds", List.of(Season.Fall), true),
    YamSeeds("yam seeds", List.of(Season.Fall), true),
    RareSeed("rare seed", List.of(Season.Fall), true),

    // Winter seeds
    PowderMelonSeeds("powder melon seeds", List.of(Season.Winter), true),

    // Special seeds (multiple seasons)
    AncientSeeds("ancient seeds", List.of(Season.Spring, Season.Summer, Season.Fall), true),
    MixedSeeds("mixed seeds", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),

    // Tree saplings and seeds
    AppleSapling("apple sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    ApricotSapling("apricot sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    CherrySapling("cherry sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    OrangeSapling("orange sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    PeachSapling("peach sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    PomegranateSapling("pomegranate sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    MapleSeed("maple seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    PineCone("pine cone", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MahoganySeed("mahogany seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MangoSapling("mango sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    BananaSapling("banana sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false),
    Acorn("acorn", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MushroomTreeSeed("mushroom tree seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true),
    MysticTreeSeed("mystic tree seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false);

    private final String name;
    private final List<Season> seasons;
    private final boolean isForaging;

    ForagingSeedType(String name, List<Season> seasons, boolean isForaging) {
        this.name = name;
        this.seasons = seasons;
        this.isForaging = isForaging;
    }

    public List<Season> getSeasons() {
        return seasons;
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
