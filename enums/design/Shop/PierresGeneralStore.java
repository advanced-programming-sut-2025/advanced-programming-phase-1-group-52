package enums.design.Shop;


import enums.design.Season;

public enum PierresGeneralStore implements ShopEntry {
    // Year-Round Stock
    Rice(null, "Rice", "A basic grain often served under vegetables.",
            200, Integer.MAX_VALUE),
    WheatFlour(null, "Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.",
            100, Integer.MAX_VALUE),
    Bouquet(null, "Bouquet", "A gift that shows your romantic interest.",
            1000, 2),
    WeddingRing(null, "Wedding Ring", "It's used to ask for another farmer's hand in marriage.",
            10000, 2),
    DehydratorRecipe(null, "Dehydrator (Recipe)", "A recipe to make Dehydrator",
            10000, 1),
    GrassStarterRecipe(null, "Grass Starter (Recipe)", "A recipe to make Grass Starter",
            1000, 1),
    Sugar(null, "Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.",
            100, Integer.MAX_VALUE),
    Oil(null, "Oil", "All purpose cooking oil.",
            200, Integer.MAX_VALUE),
    Vinegar(null, "Vinegar", "An aged fermented liquid used in many cooking recipes.",
            200, Integer.MAX_VALUE),
    DeluxeRetainingSoil(null, "Deluxe Retaining Soil", "This soil has a 100% chance of staying watered overnight.",
            150, Integer.MAX_VALUE),
    GrassStarter(null, "Grass Starter", "Place this on your farm to start a new patch of grass.",
            100, Integer.MAX_VALUE),
    SpeedGro(null, "Speed-Gro", "Makes the plants grow 1 day earlier.",
            100, Integer.MAX_VALUE),
    AppleSapling(null, "Apple Sapling", "Bears fruit in fall. Needs 28 days to mature.",
            4000, Integer.MAX_VALUE),
    ApricotSapling(null, "Apricot Sapling", "Bears fruit in spring. Needs 28 days to mature.",
            2000, Integer.MAX_VALUE),
    CherrySapling(null, "Cherry Sapling", "Bears fruit in spring. Needs 28 days to mature.",
            3400, Integer.MAX_VALUE),
    OrangeSapling(null, "Orange Sapling", "Bears fruit in summer. Needs 28 days to mature.",
            4000, Integer.MAX_VALUE),
    PeachSapling(null, "Peach Sapling", "Bears fruit in summer. Needs 28 days to mature.",
            6000, Integer.MAX_VALUE),
    PomegranateSapling(null, "Pomegranate Sapling", "Bears fruit in fall. Needs 28 days to mature.",
            6000, Integer.MAX_VALUE),
    BasicRetainingSoil(null, "Basic Retaining Soil", "Chance of staying watered overnight.",
            100, Integer.MAX_VALUE),
    QualityRetainingSoil(null, "Quality Retaining Soil", "ArtisanMachineProduct chance of staying watered overnight.",
            150, Integer.MAX_VALUE),

    // Backpacks
    LargePack(null, "Large Pack", "Unlocks the 2nd row of inventory (12 more slots, total 24).",
            2000, 1),
    DeluxePack(null, "Deluxe Pack", "Unlocks the 3rd row of inventory (infinite slots).",
            10000, 1),

    // Seeds – Spring
    ParsnipSeeds(Season.Spring, "Parsnip Seeds", "Plant in spring. Takes 4 days to mature.",
            30, 5),
    BeanStarter(Season.Spring, "Bean Starter", "Plant in spring. Takes 10 days. Grows on a trellis.",
            90, 5),
    CauliflowerSeeds(Season.Spring, "Cauliflower Seeds", "Plant in spring. Takes 12 days.",
            120, 5),
    PotatoSeeds(Season.Spring, "Potato Seeds", "Plant in spring. Takes 6 days.",
            75, 5),
    TulipBulb(Season.Spring, "Tulip Bulb", "Spring flower. Takes 6 days.",
            30, 5),
    KaleSeeds(Season.Spring, "Kale Seeds", "Spring crop. Takes 6 days.",
            105, 5),
    JazzSeeds(Season.Spring, "Jazz Seeds", "Spring flower. Takes 7 days.",
            45, 5),
    GarlicSeeds(Season.Spring, "Garlic Seeds", "Plant in spring. Takes 4 days.",
            60, 5),
    RiceShoot(Season.Spring, "Rice Shoot", "Plant in spring. Takes 8 days. Grows faster near water.",
            60, 5),

    // Seeds – Summer
    MelonSeeds(Season.Summer, "Melon Seeds", "Plant in summer. Takes 12 days.",
            120, 5),
    TomatoSeeds(Season.Summer, "Tomato Seeds", "Plant in summer. Takes 11 days.",
            75, 5),
    BlueberrySeeds(Season.Summer, "Blueberry Seeds", "Plant in summer. Takes 13 days.",
            120, 5),
    PepperSeeds(Season.Summer, "Pepper Seeds", "Plant in summer. Takes 5 days.",
            60, 5),
    WheatSeeds_Summer(Season.Summer, "Wheat Seeds", "Summer crop. Takes 4 days.",
            15, 5),
    RadishSeeds(Season.Summer, "Radish Seeds", "Plant in summer. Takes 6 days.",
            60, 5),
    PoppySeeds(Season.Summer, "Poppy Seeds", "Summer flower. Takes 7 days.",
            150, 5),
    SpangleSeeds(Season.Summer, "Spangle Seeds", "Summer flower. Takes 8 days.",
            75, 5),
    HopsStarter(Season.Summer, "Hops Starter", "Summer crop. Takes 11 days. Grows on a trellis.",
            90, 5),
    CornSeeds_Summer(Season.Summer, "Corn Seeds", "Summer crop. Takes 14 days.",
            225, 5),
    SunflowerSeeds_Summer(Season.Summer, "Sunflower Seeds", "Summer flower. Takes 8 days.",
            300, 5),
    RedCabbageSeeds(Season.Summer, "Red Cabbage Seeds", "Summer crop. Takes 9 days.",
            150, 5),

    // Seeds – Fall
    EggplantSeeds(Season.Fall, "Eggplant Seeds", "Fall crop. Takes 5 days.",
            30, 5),
    CornSeeds_Fall(Season.Fall, "Corn Seeds", "Fall crop. Takes 14 days.",
            225, 5),
    PumpkinSeeds(Season.Fall, "Pumpkin Seeds", "Plant in fall. Takes 13 days.",
            150, 5),
    BokChoySeeds(Season.Fall, "Bok Choy Seeds", "Fall crop. Takes 4 days.",
            75, 5),
    YamSeeds(Season.Fall, "Yam Seeds", "Fall crop. Takes 10 days.",
            90, 5),
    CranberrySeeds(Season.Fall, "Cranberry Seeds", "Fall crop. Takes 7 days.",
            360, 5),
    SunflowerSeeds_Fall(Season.Fall, "Sunflower Seeds", "Fall flower. Takes 8 days.",
            300, 5),
    FairySeeds(Season.Fall, "Fairy Seeds", "Fall flower. Takes 12 days.",
            300, 5),
    AmaranthSeeds(Season.Fall, "Amaranth Seeds", "Fall crop. Takes 7 days.",
            105, 5),
    GrapeStarter(Season.Fall, "Grape Starter", "Fall crop. Takes 10 days. Grows on a trellis.",
            90, 5),
    WheatSeeds_Fall(Season.Fall, "Wheat Seeds", "Fall crop. Takes 4 days.",
            15, 5),
    ArtichokeSeeds(Season.Fall, "Artichoke Seeds", "Fall crop. Takes 8 days.",
            45, 5),
    ;


    private final Season season;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;

    PierresGeneralStore(Season season, String name, String description, int price, int dailyLimit) {
        this.season = season;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    public Season getSeason() { return season; }
    @Override public String getDisplayName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }
}
