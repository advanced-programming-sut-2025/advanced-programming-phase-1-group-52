package enums.design.Shop;

import enums.design.Season;
import enums.design.ShopType;
import enums.items.ForagingSeedType;

public enum JojaMart implements ShopEntry{
    // Permanent Stock
    JojaCola(null, "Joja Cola", "The flagship product of Joja corporation.",
            75, Integer.MAX_VALUE,null),
    AncientSeed(null, "Ancient Seed", "Could these still grow?",
            500, 1,null),
    GrassStarter(null, "Grass Starter", "Place this on your farm to start a new patch of grass.",
            125, Integer.MAX_VALUE,null),

    Sugar(null, "Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.",
            125, Integer.MAX_VALUE,null),
    WheatFlour(null, "Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.",
            125, Integer.MAX_VALUE,null),
    Rice(null, "Rice", "A basic grain often served under vegetables.",
            250, Integer.MAX_VALUE,null),

    // Spring Stock
    ParsnipSeeds(Season.Spring, "Parsnip Seeds", "Plant these in the spring. Takes 4 days to mature.",
            25, 5, ForagingSeedType.ParsnipSeeds),
    BeanStarter(Season.Spring, "Bean Starter", "Plant these in the spring. Takes 10 days to mature, but keeps producing.",
            75, 5, ForagingSeedType.BeanStarter),
    CauliflowerSeeds(Season.Spring, "Cauliflower Seeds", "Plant these in the spring. Takes 12 days.",
            100, 5, ForagingSeedType.CauliflowerSeeds),
    PotatoSeeds(Season.Spring, "Potato Seeds", "Plant these in the spring. Takes 6 days, may yield extra.",
            62, 5, ForagingSeedType.PotatoSeeds),
    StrawberrySeeds(Season.Spring, "Strawberry Seeds", "Plant in spring. Takes 8 days, keeps producing.",
            100, 5, ForagingSeedType.StrawberrySeeds),
    TulipBulb(Season.Spring, "Tulip Bulb", "Plant in spring. Takes 6 days.",
            25, 5, ForagingSeedType.TulipBulb),
    KaleSeeds(Season.Spring, "Kale Seeds", "Plant in spring. Takes 6 days. Harvest with scythe.",
            87, 5, ForagingSeedType.KaleSeeds),
    CoffeeBeansSpring(Season.Spring, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
            200, 1,null),
    CarrotSeeds(Season.Spring, "Carrot Seeds", "Plant in spring. Takes 3 days.",
            5, 10, ForagingSeedType.CarrotSeeds),
    RhubarbSeeds(Season.Spring, "Rhubarb Seeds", "Plant in spring. Takes 13 days.",
            100, 5, ForagingSeedType.RhubarbSeeds),
    JazzSeeds(Season.Spring, "Jazz Seeds", "Plant in spring. Takes 7 days.",
            37, 5, ForagingSeedType.JazzSeeds),

    // Summer Stock
    TomatoSeeds(Season.Summer, "Tomato Seeds", "Plant in summer. Takes 11 days, keeps producing.",
            62, 5, ForagingSeedType.TomatoSeeds),
    PepperSeeds(Season.Summer, "Pepper Seeds", "Plant in summer. Takes 5 days, keeps producing.",
            50, 5, ForagingSeedType.PepperSeeds),
    WheatSeedsSummer(Season.Summer, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
            12, 10,null),
    SummerSquashSeeds(Season.Summer, "Summer Squash Seeds", "Plant in summer. Takes 6 days, keeps producing.",
            10, 10, ForagingSeedType.SummerSquashSeeds),
    RadishSeeds(Season.Summer, "Radish Seeds", "Plant in summer. Takes 6 days.",
            50, 5, ForagingSeedType.RadishSeeds),
    MelonSeeds(Season.Summer, "Melon Seeds", "Plant in summer. Takes 12 days.",
            100, 5, ForagingSeedType.MelonSeeds),
    HopsStarter(Season.Summer, "Hops Starter", "Summer. Takes 11 days, keeps producing. Trellis.",
            75, 5, ForagingSeedType.HopsStarter),
    PoppySeeds(Season.Summer, "Poppy Seeds", "Summer. Bright red flower in 7 days.",
            125, 5, ForagingSeedType.PoppySeeds),
    SpangleSeeds(Season.Summer, "Spangle Seeds", "Summer. Tropical flower in 8 days.",
            62, 5, ForagingSeedType.SpangleSeeds),
    StarfruitSeeds(Season.Summer, "Starfruit Seeds", "Summer. Takes 13 days.",
            400, 5, ForagingSeedType.StarfruitSeeds),
    CoffeeBeansSummer(Season.Summer, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
            200, 1,null),
    SunflowerSeedsSummer(Season.Summer, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
            125, 5,null),

    // Fall Stock
    CornSeeds(Season.Fall, "Corn Seeds", "Summer/Fall. Takes 14 days, keeps producing.",
            187, 5, ForagingSeedType.CornSeeds),
    EggplantSeeds(Season.Fall, "Eggplant Seeds", "Fall. Takes 5 days, keeps producing.",
            25, 5, ForagingSeedType.EggplantSeeds),
    PumpkinSeeds(Season.Fall, "Pumpkin Seeds", "Fall. Takes 13 days.",
            125, 5, ForagingSeedType.PumpkinSeeds),
    BroccoliSeeds(Season.Fall, "Broccoli Seeds", "Fall. Takes 8 days, keeps producing.",
            15, 5, ForagingSeedType.BroccoliSeeds),
    AmaranthSeeds(Season.Fall, "Amaranth Seeds", "Fall. Takes 7 days. Scythe harvest.",
            87, 5, ForagingSeedType.AmaranthSeeds),
    GrapeStarter(Season.Fall, "Grape Starter", "Fall. Takes 10 days, keeps producing. Trellis.",
            75, 5, ForagingSeedType.GrapeStarter),
    BeetSeeds(Season.Fall, "Beet Seeds", "Fall. Takes 6 days.",
            20, 5, ForagingSeedType.BeetSeeds),
    YamSeeds(Season.Fall, "Yam Seeds", "Fall. Takes 10 days.",
            75, 5, ForagingSeedType.YamSeeds),
    BokChoySeeds(Season.Fall, "Bok Choy Seeds", "Fall. Takes 4 days.",
            62, 5, ForagingSeedType.BokChoySeeds),
    CranberrySeeds(Season.Fall, "Cranberry Seeds", "Fall. Takes 7 days, keeps producing.",
            300, 5, ForagingSeedType.CranberrySeeds),
    SunflowerSeedsFall(Season.Fall, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
            125, 5, ForagingSeedType.SunflowerSeeds),
    FairySeeds(Season.Fall, "Fairy Seeds", "Fall. Mysterious flower in 12 days.",
            250, 5,null),
    RareSeed(Season.Fall, "Rare Seed", "Fall. Takes all season to grow.",
            1000, 1, ForagingSeedType.RareSeed),
    WheatSeedsFall(Season.Fall, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
            12, 5, ForagingSeedType.WheatSeeds),

    // Winter Stock
    PowdermelonSeeds(Season.Winter, "Powdermelon Seeds", "Winter. Takes 7 days to grow.",
            20, 10, ForagingSeedType.PowderMelonSeeds),;

    private final Season season;
    private final String displayName;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final ForagingSeedType seed;

    JojaMart(Season season, String displayName, String description, int price, int dailyLimit, ForagingSeedType seed) {
        this.season = season;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.seed = seed;
    }

    public Season getSeason() { return season; }
    @Override public String getDisplayName() { return displayName; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }
    public ForagingSeedType getSeed() { return seed; }
}
