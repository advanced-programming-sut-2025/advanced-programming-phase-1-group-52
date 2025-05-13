package enums.items;

import enums.design.Season;

import java.util.List;

public enum TreeType implements Growable {
    APRICOT_TREE(ForagingSeedType.ApricotSapling, List.of(7, 7, 7, 7), 28, FruitType.Apricot, 1, 59, true, 38, List.of(Season.Spring)),
    CHERRY_TREE(ForagingSeedType.CherrySapling, List.of(7, 7, 7, 7), 28, FruitType.Cherry, 1, 80, true, 38, List.of(Season.Spring)),
    BANANA_TREE(ForagingSeedType.BananaSapling, List.of(7, 7, 7, 7), 28, FruitType.Banana, 1, 150, true, 75, List.of(Season.Summer)),
    MANGO_TREE(ForagingSeedType.MangoSapling, List.of(7, 7, 7, 7), 28, FruitType.Mango, 1, 130, true, 100,  List.of(Season.Summer)),
    ORANGE_TREE(ForagingSeedType.OrangeSapling, List.of(7, 7, 7, 7), 28, FruitType.Orange, 1, 100, true, 38, List.of(Season.Summer)),
    PEACH_TREE(ForagingSeedType.PeachSapling, List.of(7, 7, 7, 7), 28, FruitType.Peach, 1, 140, true, 38, List.of(Season.Summer)),
    APPLE_TREE(ForagingSeedType.AppleSapling, List.of(7, 7, 7, 7), 28, FruitType.Apple, 1, 100, true, 38, List.of(Season.Fall)),
    POMEGRANATE_TREE(ForagingSeedType.PomegranateSapling, List.of(7, 7, 7, 7), 28, FruitType.Pomegranate, 1, 140, true, 38, List.of(Season.Fall)),
    OAK_TREE(ForagingSeedType.Acorn, List.of(7, 7, 7, 7), 28, FruitType.OakResin, 7, 150, false, -1, List.of(Season.Special)),
    MAPLE_TREE(ForagingSeedType.MapleSeed, List.of(7, 7, 7, 7), 28, FruitType.MapleSyrup, 9, 200, false,-1, List.of(Season.Special)),
    PINE_TREE(ForagingSeedType.PineCone, List.of(7, 7, 7, 7), 28, FruitType.PineTar, 5, 100, false, -1, List.of(Season.Special)),
    MAHOGANY_TREE(ForagingSeedType.MahoganySeed, List.of(7, 7, 7, 7), 28, FruitType.Sap, 1, 2, true, -2, List.of(Season.Special)),
    MUSHROOM_TREE(ForagingSeedType.MushroomTreeSeed, List.of(7, 7, 7, 7), 28, FruitType.CommonMushroom, 1, 40, true, 38, List.of(Season.Special)),
    MYSTIC_TREE(ForagingSeedType.MysticTreeSeed, List.of(7, 7, 7, 7), 28, FruitType.MysticSyrup, 7, 1000, true, 500, List.of(Season.Special));

    public final ForagingSeedType source;
    public final List<Integer> stages;
    public final int totalHarvestTime;
    public final FruitType product;
    public final int harvestCycle;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final List<Season> seasons;

    TreeType(ForagingSeedType source, List<Integer> stages, int totalHarvestTime, FruitType product,
             int harvestCycle, int baseSellPrice, boolean isEdible,
             int energy, List<Season> seasons) {
        this.source = source;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.product = product;
        this.harvestCycle = harvestCycle;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.seasons = seasons;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isEdible() {
        return isEdible;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public int getHarvestCycle() {
        return harvestCycle;
    }

    public FruitType getProduct() {
        return product;
    }

    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }

    public List<Integer> getStages() {
        return stages;
    }

    public ForagingSeedType getSource() {
        return source;
    }
}
