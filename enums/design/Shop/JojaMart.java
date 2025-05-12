package enums.design.Shop;

import enums.design.Season;
import enums.items.MaterialType;
import enums.items.SeedType;

public enum JojaMart {
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
            25, 5,SeedType.ParsnipSeeds),
    BeanStarter(Season.Spring, "Bean Starter", "Plant these in the spring. Takes 10 days to mature, but keeps producing.",
            75, 5,SeedType.BeanStarter),
    CauliflowerSeeds(Season.Spring, "Cauliflower Seeds", "Plant these in the spring. Takes 12 days.",
            100, 5,SeedType.CauliflowerSeeds),
    PotatoSeeds(Season.Spring, "Potato Seeds", "Plant these in the spring. Takes 6 days, may yield extra.",
            62, 5,SeedType.PotatoSeeds),
    StrawberrySeeds(Season.Spring, "Strawberry Seeds", "Plant in spring. Takes 8 days, keeps producing.",
            100, 5,SeedType.StrawberrySeeds),
    TulipBulb(Season.Spring, "Tulip Bulb", "Plant in spring. Takes 6 days.",
            25, 5,SeedType.TulipBulb),
    KaleSeeds(Season.Spring, "Kale Seeds", "Plant in spring. Takes 6 days. Harvest with scythe.",
            87, 5,SeedType.KaleSeeds),
    CoffeeBeansSpring(Season.Spring, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
            200, 1,null),
    CarrotSeeds(Season.Spring, "Carrot Seeds", "Plant in spring. Takes 3 days.",
            5, 10,SeedType.CarrotSeeds),
    RhubarbSeeds(Season.Spring, "Rhubarb Seeds", "Plant in spring. Takes 13 days.",
            100, 5,SeedType.RhubarbSeeds),
    JazzSeeds(Season.Spring, "Jazz Seeds", "Plant in spring. Takes 7 days.",
            37, 5,SeedType.JazzSeeds),

    // Summer Stock
    TomatoSeeds(Season.Summer, "Tomato Seeds", "Plant in summer. Takes 11 days, keeps producing.",
            62, 5,SeedType.TomatoSeeds),
    PepperSeeds(Season.Summer, "Pepper Seeds", "Plant in summer. Takes 5 days, keeps producing.",
            50, 5,SeedType.PepperSeeds),
    WheatSeedsSummer(Season.Summer, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
            12, 10,null),
    SummerSquashSeeds(Season.Summer, "Summer Squash Seeds", "Plant in summer. Takes 6 days, keeps producing.",
            10, 10,SeedType.SummerSquashSeeds),
    RadishSeeds(Season.Summer, "Radish Seeds", "Plant in summer. Takes 6 days.",
            50, 5,SeedType.RadishSeeds),
    MelonSeeds(Season.Summer, "Melon Seeds", "Plant in summer. Takes 12 days.",
            100, 5,SeedType.MelonSeeds),
    HopsStarter(Season.Summer, "Hops Starter", "Summer. Takes 11 days, keeps producing. Trellis.",
            75, 5,SeedType.HopsStarter),
    PoppySeeds(Season.Summer, "Poppy Seeds", "Summer. Bright red flower in 7 days.",
            125, 5,SeedType.PoppySeeds),
    SpangleSeeds(Season.Summer, "Spangle Seeds", "Summer. Tropical flower in 8 days.",
            62, 5,SeedType.SpangleSeeds),
    StarfruitSeeds(Season.Summer, "Starfruit Seeds", "Summer. Takes 13 days.",
            400, 5,SeedType.StarfruitSeeds),
    CoffeeBeansSummer(Season.Summer, "Coffee Beans", "Spring/Summer. Takes 10 days, then produces every 2 days.",
            200, 1,null),
    SunflowerSeedsSummer(Season.Summer, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
            125, 5,null),

    // Fall Stock
    CornSeeds(Season.Fall, "Corn Seeds", "Summer/Fall. Takes 14 days, keeps producing.",
            187, 5,SeedType.CornSeeds),
    EggplantSeeds(Season.Fall, "Eggplant Seeds", "Fall. Takes 5 days, keeps producing.",
            25, 5,SeedType.EggplantSeeds),
    PumpkinSeeds(Season.Fall, "Pumpkin Seeds", "Fall. Takes 13 days.",
            125, 5,SeedType.PumpkinSeeds),
    BroccoliSeeds(Season.Fall, "Broccoli Seeds", "Fall. Takes 8 days, keeps producing.",
            15, 5,SeedType.BroccoliSeeds),
    AmaranthSeeds(Season.Fall, "Amaranth Seeds", "Fall. Takes 7 days. Scythe harvest.",
            87, 5,SeedType.AmaranthSeeds),
    GrapeStarter(Season.Fall, "Grape Starter", "Fall. Takes 10 days, keeps producing. Trellis.",
            75, 5,SeedType.GrapeStarter),
    BeetSeeds(Season.Fall, "Beet Seeds", "Fall. Takes 6 days.",
            20, 5,SeedType.BeetSeeds),
    YamSeeds(Season.Fall, "Yam Seeds", "Fall. Takes 10 days.",
            75, 5,SeedType.YamSeeds),
    BokChoySeeds(Season.Fall, "Bok Choy Seeds", "Fall. Takes 4 days.",
            62, 5,SeedType.BokChoySeeds),
    CranberrySeeds(Season.Fall, "Cranberry Seeds", "Fall. Takes 7 days, keeps producing.",
            300, 5,SeedType.CranberrySeeds),
    SunflowerSeedsFall(Season.Fall, "Sunflower Seeds", "Summer/Fall. Takes 8 days. Yields extra seeds.",
            125, 5,SeedType.SunflowerSeeds),
    FairySeeds(Season.Fall, "Fairy Seeds", "Fall. Mysterious flower in 12 days.",
            250, 5,null),
    RareSeed(Season.Fall, "Rare Seed", "Fall. Takes all season to grow.",
            1000, 1,SeedType.RareSeed),
    WheatSeedsFall(Season.Fall, "Wheat Seeds", "Summer/Fall. Takes 4 days. Scythe harvest.",
            12, 5,SeedType.WheatSeeds),

    // Winter Stock
    PowdermelonSeeds(Season.Winter, "Powdermelon Seeds", "Winter. Takes 7 days to grow.",
            20, 10,SeedType.PowdermelonSeeds),;

    private final Season season;
    private final String displayName;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final SeedType seed;

    JojaMart(Season season, String displayName, String description, int price, int dailyLimit, SeedType seed) {
        this.season = season;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.seed = seed;
    }

    public Season getSeason() { return season; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getPrice() { return price; }
    public int getDailyLimit() { return dailyLimit; }
    public SeedType getSeed() { return seed; }
}
