package enums.items;

import enums.design.Season;
import java.util.List;

public enum ForagingSeedType implements ItemType {
    // Spring seeds
    JazzSeeds("jazz seeds", List.of(Season.Spring), true, CropType.BlueJazz),
    CarrotSeeds("carrot seeds", List.of(Season.Spring), true, CropType.Carrot),
    CauliflowerSeeds("cauliflower seeds", List.of(Season.Spring), true, CropType.Cauliflower),
    CoffeeBean("coffee bean", List.of(Season.Spring), true, CropType.CoffeeBean),
    GarlicSeeds("garlic seeds", List.of(Season.Spring), true, CropType.Garlic),
    BeanStarter("bean starter", List.of(Season.Spring), true, CropType.GreenBean),
    KaleSeeds("kale seeds", List.of(Season.Spring), true, CropType.Kale),
    ParsnipSeeds("parsnip seeds", List.of(Season.Spring), true, CropType.Parsnip),
    PotatoSeeds("potato seeds", List.of(Season.Spring), true, CropType.Potato),
    RhubarbSeeds("rhubarb seeds", List.of(Season.Spring), true, CropType.Rhubarb),
    StrawberrySeeds("strawberry seeds", List.of(Season.Spring), true, CropType.Strawberry),
    TulipBulb("tulip bulb", List.of(Season.Spring), true, CropType.Tulip),
    RiceShoot("rice shoot", List.of(Season.Spring), true, CropType.UnmilledRice),

    // Summer seeds
    BlueberrySeeds("blueberry seeds", List.of(Season.Summer), true, CropType.Blueberry),
    CornSeeds("corn seeds", List.of(Season.Summer), true, CropType.Corn),
    HopsStarter("hops starter", List.of(Season.Summer), true, CropType.Hops),
    PepperSeeds("pepper seeds", List.of(Season.Summer), true, CropType.HotPepper),
    MelonSeeds("melon seeds", List.of(Season.Summer), true, CropType.Melon),
    PoppySeeds("poppy seeds", List.of(Season.Summer), true, CropType.Poppy),
    RadishSeeds("radish seeds", List.of(Season.Summer), true, CropType.Radish),
    RedCabbageSeeds("red cabbage seeds", List.of(Season.Summer), true, CropType.RedCabbage),
    StarfruitSeeds("starfruit seeds", List.of(Season.Summer), true, CropType.Starfruit),
    SpangleSeeds("spangle seeds", List.of(Season.Summer), true, CropType.SummerSpangle),
    SummerSquashSeeds("summer squash seeds", List.of(Season.Summer), true, CropType.SummerSquash),
    SunflowerSeeds("sunflower seeds", List.of(Season.Summer, Season.Fall), true, CropType.Sunflower),
    TomatoSeeds("tomato seeds", List.of(Season.Summer), true, CropType.Tomato),
    WheatSeeds("wheat seeds", List.of(Season.Summer, Season.Fall), true, CropType.WHEAT),

    // Fall seeds
    AmaranthSeeds("amaranth seeds", List.of(Season.Fall), true, CropType.Amaranth),
    ArtichokeSeeds("artichoke seeds", List.of(Season.Fall), true, CropType.Artichoke),
    BeetSeeds("beet seeds", List.of(Season.Fall), true, CropType.Beet),
    BokChoySeeds("bok choy seeds", List.of(Season.Fall), true, CropType.BokChoy),
    BroccoliSeeds("broccoli seeds", List.of(Season.Fall), true, CropType.Broccoli),
    CranberrySeeds("cranberry seeds", List.of(Season.Fall), true, CropType.Cranberries),
    EggplantSeeds("eggplant seeds", List.of(Season.Fall), true, CropType.Eggplant),
    FairySeeds("fairy seeds", List.of(Season.Fall), true, CropType.FairyRose),
    GrapeStarter("grape starter", List.of(Season.Fall), true, CropType.Grape),
    PumpkinSeeds("pumpkin seeds", List.of(Season.Fall), true, CropType.Pumpkin),
    YamSeeds("yam seeds", List.of(Season.Fall), true, CropType.Yam),
    RareSeed("rare seed", List.of(Season.Fall), true, CropType.SweetGemBerry),

    // Winter seeds
    PowderMelonSeeds("powder melon seeds", List.of(Season.Winter), true, CropType.PowderMelon),

    // Special seeds (multiple seasons)
    AncientSeeds("ancient seeds", List.of(Season.Spring, Season.Summer, Season.Fall), true, CropType.AncientFruit),
    MixedSeeds("mixed seeds", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, null),

    // Tree saplings and seeds
    AppleSapling("apple sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.APPLE_TREE),
    ApricotSapling("apricot sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.APRICOT_TREE),
    CherrySapling("cherry sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.CHERRY_TREE),
    OrangeSapling("orange sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.ORANGE_TREE),
    PeachSapling("peach sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.PEACH_TREE),
    PomegranateSapling("pomegranate sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.POMEGRANATE_TREE),
    MapleSeed("maple seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true, TreeType.MAPLE_TREE),
    PineCone("pine cone", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true, TreeType.PINE_TREE),
    MahoganySeed("mahogany seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true, TreeType.MAHOGANY_TREE),
    MangoSapling("mango sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.MANGO_TREE),
    BananaSapling("banana sapling", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.BANANA_TREE),
    Acorn("acorn", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true, TreeType.OAK_TREE),
    MushroomTreeSeed("mushroom tree seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), true, TreeType.MUSHROOM_TREE),
    MysticTreeSeed("mystic tree seed", List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter), false, TreeType.MYSTIC_TREE),;

    private final String name;
    private final List<Season> seasons;
    private final boolean isForaging;
    private final ItemType plantType;

    ForagingSeedType(String name, List<Season> seasons, boolean isForaging, ItemType plantType) {
        this.name = name;
        this.seasons = seasons;
        this.isForaging = isForaging;
        this.plantType = plantType;
    }

    public ItemType getPlantType() {
        return plantType;
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
