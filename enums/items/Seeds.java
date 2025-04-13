package enums.items;


import enums.design.Season;

public enum Seeds implements Items {
    // Spring Seeds
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

    // Summer Seeds
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

    // Fall Seeds
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

    // Winter/Special Seeds
    PowdermelonSeeds(Season.Winter),
    AncientSeeds(Season.Special),
    MixedSeeds(Season.Special);

    private final Season seasonType;

    Seeds(Season season) {
        this.seasonType = season;
    }

    public Season getSeasonType() {
        return seasonType;
    }
}
