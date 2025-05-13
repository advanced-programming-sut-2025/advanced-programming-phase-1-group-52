package enums.items;

import enums.design.Season;

public enum ForagingSeedType implements ItemType, Growable {
    JazzSeeds(Season.Spring),
    CarrotSeeds(Season.Spring),
    CauliflowerSeeds(Season.Spring),
    CoffeeBean(Season.Spring),
    GarlicSeeds(Season.Spring),
    BeanStarter(Season.Spring),
    KaleSeeds(Season.Spring),
    ParsnipSeeds(Season.Spring),
    PotatoSeeds(Season.Spring),
    RhubarbSeeds(Season.Spring),
    StrawberrySeeds(Season.Spring),
    TulipBulb(Season.Spring),
    RiceShoot(Season.Spring),

    BlueberrySeeds(Season.Summer),
    CornSeeds(Season.Summer),
    HopsStarter(Season.Summer),
    PepperSeeds(Season.Summer),
    MelonSeeds(Season.Summer),
    PoppySeeds(Season.Summer),
    RadishSeeds(Season.Summer),
    RedCabbageSeeds(Season.Summer),
    StarfruitSeeds(Season.Summer),
    SpangleSeeds(Season.Summer),
    SummerSquashSeeds(Season.Summer),
    SunflowerSeeds(Season.Summer),
    TomatoSeeds(Season.Summer),
    WheatSeeds(Season.Summer),

    AmaranthSeeds(Season.Fall),
    ArtichokeSeeds(Season.Fall),
    BeetSeeds(Season.Fall),
    BokChoySeeds(Season.Fall),
    BroccoliSeeds(Season.Fall),
    CranberrySeeds(Season.Fall),
    EggplantSeeds(Season.Fall),
    FairySeeds(Season.Fall),
    GrapeStarter(Season.Fall),
    PumpkinSeeds(Season.Fall),
    YamSeeds(Season.Fall),
    RareSeed(Season.Fall),

    PowderMelonSeeds(Season.Winter),

    AncientSeeds(Season.Special),
    MixedSeeds(Season.Special),

    // trees seed and pierre's products
    AppleSapling(Season.Special),
    ApricotSapling(Season.Special),
    CherrySapling(Season.Special),
    OrangeSapling(Season.Special),
    PeachSapling(Season.Special),
    PomegranateSapling(Season.Special),
    MapleSeed(Season.Special),
    PineCone(Season.Special),
    MahoganySeed(Season.Special),
    MangoSapling(Season.Special),
    BananaSapling(Season.Special),
    Acorn(Season.Special),
    MushroomTreeSeed(Season.Special),
    MysticTreeSeed(Season.Special);

    private final Season season;

    ForagingSeedType(Season season) {
        this.season = season;
    }

    public Season getSeason() {
        return season;
    }

    @Override
    public boolean isTool() {
        return false;
    }
}
