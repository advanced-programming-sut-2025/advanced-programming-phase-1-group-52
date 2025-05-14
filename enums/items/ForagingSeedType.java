package enums.items;

import enums.design.Season;

public enum ForagingSeedType implements ItemType {
    JazzSeeds(Season.Spring, true),
    CarrotSeeds(Season.Spring, true),
    CauliflowerSeeds(Season.Spring, true),
    CoffeeBean(Season.Spring, true),
    GarlicSeeds(Season.Spring, true),
    BeanStarter(Season.Spring, true),
    KaleSeeds(Season.Spring, true),
    ParsnipSeeds(Season.Spring, true),
    PotatoSeeds(Season.Spring, true),
    RhubarbSeeds(Season.Spring, true),
    StrawberrySeeds(Season.Spring, true),
    TulipBulb(Season.Spring, true),
    RiceShoot(Season.Spring, true),

    BlueberrySeeds(Season.Summer, true),
    CornSeeds(Season.Summer, true),
    HopsStarter(Season.Summer, true),
    PepperSeeds(Season.Summer, true),
    MelonSeeds(Season.Summer, true),
    PoppySeeds(Season.Summer, true),
    RadishSeeds(Season.Summer, true),
    RedCabbageSeeds(Season.Summer, true),
    StarfruitSeeds(Season.Summer, true),
    SpangleSeeds(Season.Summer, true),
    SummerSquashSeeds(Season.Summer, true),
    SunflowerSeeds(Season.Summer, true),
    TomatoSeeds(Season.Summer, true),
    WheatSeeds(Season.Summer, true),

    AmaranthSeeds(Season.Fall, true),
    ArtichokeSeeds(Season.Fall, true),
    BeetSeeds(Season.Fall, true),
    BokChoySeeds(Season.Fall, true),
    BroccoliSeeds(Season.Fall, true),
    CranberrySeeds(Season.Fall, true),
    EggplantSeeds(Season.Fall, true),
    FairySeeds(Season.Fall, true),
    GrapeStarter(Season.Fall, true),
    PumpkinSeeds(Season.Fall, true),
    YamSeeds(Season.Fall, true),
    RareSeed(Season.Fall, true),

    PowderMelonSeeds(Season.Winter, true),

    AncientSeeds(Season.Special, true),
    MixedSeeds(Season.Special, true),

    // trees seed and pierre's products
    AppleSapling(Season.Special, false),
    ApricotSapling(Season.Special, false),
    CherrySapling(Season.Special, false),
    OrangeSapling(Season.Special, false),
    PeachSapling(Season.Special, false),
    PomegranateSapling(Season.Special, false),
    MapleSeed(Season.Special, true),
    PineCone(Season.Special, true),
    MahoganySeed(Season.Special, true),
    MangoSapling(Season.Special, false),
    BananaSapling(Season.Special, false),
    Acorn(Season.Special, true),
    MushroomTreeSeed(Season.Special, true),
    MysticTreeSeed(Season.Special, false);

    private final Season season;
    private final boolean isForaging;

    ForagingSeedType(Season season, boolean isForaging) {
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
}
