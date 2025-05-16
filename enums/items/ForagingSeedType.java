package enums.items;

import enums.design.Season;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ForagingSeedType implements ItemType {
    JazzSeeds("jazz seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.BlueJazz),
    CarrotSeeds("carrot seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Carrot),
    CauliflowerSeeds("cauliflower seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Cauliflower),
    CoffeeBean("coffee bean", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.CoffeeBean),
    GarlicSeeds("garlic seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Garlic),
    BeanStarter("bean starter", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.GreenBean),
    KaleSeeds("kale seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Kale),
    ParsnipSeeds("parsnip seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Parsnip),
    PotatoSeeds("potato seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Potato),
    RhubarbSeeds("rhubarb seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Rhubarb),
    StrawberrySeeds("strawberry seeds", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Strawberry),
    TulipBulb("tulip bulb", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.Tulip),
    RiceShoot("rice shoot", new ArrayList<>(Collections.singletonList(Season.Spring)), true, CropType.UnmilledRice),

    BlueberrySeeds("blueberry seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Blueberry),
    CornSeeds("corn seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Corn),
    HopsStarter("hops starter", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Hops),
    PepperSeeds("pepper seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.HotPepper),
    MelonSeeds("melon seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Melon),
    PoppySeeds("poppy seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Poppy),
    RadishSeeds("radish seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Radish),
    RedCabbageSeeds("red cabbage seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.RedCabbage),
    StarfruitSeeds("starfruit seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Starfruit),
    SpangleSeeds("spangle seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.SummerSpangle),
    SummerSquashSeeds("summer squash seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.SummerSquash),
    SunflowerSeeds("sunflower seeds", new ArrayList<>(List.of(Season.Summer, Season.Fall)), true, CropType.Sunflower),
    TomatoSeeds("tomato seeds", new ArrayList<>(Collections.singletonList(Season.Summer)), true, CropType.Tomato),
    WheatSeeds("wheat seeds", new ArrayList<>(List.of(Season.Summer, Season.Fall)), true, CropType.WHEAT),

    AmaranthSeeds("amaranth seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Amaranth),
    ArtichokeSeeds("artichoke seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Artichoke),
    BeetSeeds("beet seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Beet),
    BokChoySeeds("bok choy seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.BokChoy),
    BroccoliSeeds("broccoli seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Broccoli),
    CranberrySeeds("cranberry seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Cranberries),
    EggplantSeeds("eggplant seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Eggplant),
    FairySeeds("fairy seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.FairyRose),
    GrapeStarter("grape starter", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Grape),
    PumpkinSeeds("pumpkin seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Pumpkin),
    YamSeeds("yam seeds", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.Yam),
    RareSeed("rare seed", new ArrayList<>(Collections.singletonList(Season.Fall)), true, CropType.SweetGemBerry),

    PowderMelonSeeds("powder melon seeds", new ArrayList<>(Collections.singletonList(Season.Winter)), true, CropType.PowderMelon),

    AncientSeeds("ancient seeds", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall)), true, CropType.AncientFruit),
    MixedSeeds("mixed seeds", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, null),

    AppleSapling("apple sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.APPLE_TREE),
    ApricotSapling("apricot sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.APRICOT_TREE),
    CherrySapling("cherry sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.CHERRY_TREE),
    OrangeSapling("orange sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.ORANGE_TREE),
    PeachSapling("peach sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.PEACH_TREE),
    PomegranateSapling("pomegranate sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.POMEGRANATE_TREE),
    MapleSeed("maple seed", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), true, TreeType.MAPLE_TREE),
    PineCone("pine cone", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), true, TreeType.PINE_TREE),
    MahoganySeed("mahogany seed", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), true, TreeType.MAHOGANY_TREE),
    MangoSapling("mango sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.MANGO_TREE),
    BananaSapling("banana sapling", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.BANANA_TREE),
    Acorn("acorn", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), true, TreeType.OAK_TREE),
    MushroomTreeSeed("mushroom tree seed", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), true, TreeType.MUSHROOM_TREE),
    MysticTreeSeed("mystic tree seed", new ArrayList<>(List.of(Season.Spring, Season.Summer, Season.Fall, Season.Winter)), false, TreeType.MYSTIC_TREE);

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
