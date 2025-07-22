package com.example.main.enums.design.Shop;


import com.example.main.enums.design.Season;
import com.example.main.enums.items.Backpacks;
import com.example.main.enums.items.CraftingMachineType;
import com.example.main.enums.items.CraftingRecipes;
import com.example.main.enums.items.ForagingSeedType;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.MaterialType;


public enum PierresGeneralStore implements ShopEntry {
    // Year-Round Stock
    Rice(null, "Rice", "A basic grain often served under vegetables.",
            200, Integer.MAX_VALUE, MaterialType.Rice),
    Wheat_Flour(null, "Wheat Flour", "A common cooking ingredient made from crushed wheat seeds.",
            100, Integer.MAX_VALUE, MaterialType.Wheat_Flour),
    Bouquet(null, "Bouquet", "A gift that shows your romantic interest.",
            1000, 2, MaterialType.Bouquet),
    WeddingRing(null, "Wedding Ring", "It's used to ask for another farmer's hand in marriage.",
            10000, 2, MaterialType.Wedding_Ring),
    DehydratorRecipe(null, "Dehydrator (Recipe)", "A recipe to make Dehydrator",
            10000, 1, CraftingRecipes.DehydratorRecipe),
    GrassStarterRecipe(null, "Grass Starter (Recipe)", "A recipe to make Grass Starter",
            1000, 1, CraftingRecipes.GrassStarterRecipe),
    Sugar(null, "Sugar", "Adds sweetness to pastries and candies. Too much can be unhealthy.",
            100, Integer.MAX_VALUE, MaterialType.Sugar),
    Oil(null, "Oil", "All purpose cooking oil.",
            200, Integer.MAX_VALUE, MaterialType.Oil),
    Vinegar(null, "Vinegar", "An aged fermented liquid used in many cooking recipes.",
            200, Integer.MAX_VALUE, MaterialType.Vinegar),
    DeluxeRetainingSoil(null, "Deluxe Retaining Soil", "This soil has a 100% chance of staying watered overnight.",
            150, Integer.MAX_VALUE, MaterialType.Deluxe_Retaining_Soil),
    GrassStarter(null, "Grass Starter", "Place this on your farm to start a new patch of grass.",
            100, Integer.MAX_VALUE, CraftingMachineType.Grass_Starter),
    SpeedGro(null, "Speed-Gro", "Makes the plants grow 1 day earlier.",
            100, Integer.MAX_VALUE, MaterialType.Speed_Gro),
    AppleSapling(null, "Apple Sapling", "Bears fruit in fall. Needs 28 days to mature.",
            4000, Integer.MAX_VALUE, ForagingSeedType.Apple_Sapling),
    ApricotSapling(null, "Apricot Sapling", "Bears fruit in spring. Needs 28 days to mature.",
            2000, Integer.MAX_VALUE, ForagingSeedType.Apricot_Sapling),
    CherrySapling(null, "Cherry Sapling", "Bears fruit in spring. Needs 28 days to mature.",
            3400, Integer.MAX_VALUE, ForagingSeedType.Cherry_Sapling),
    OrangeSapling(null, "Orange Sapling", "Bears fruit in summer. Needs 28 days to mature.",
            4000, Integer.MAX_VALUE, ForagingSeedType.Orange_Sapling),
    PeachSapling(null, "Peach Sapling", "Bears fruit in summer. Needs 28 days to mature.",
            6000, Integer.MAX_VALUE, ForagingSeedType.Peach_Sapling),
    PomegranateSapling(null, "Pomegranate Sapling", "Bears fruit in fall. Needs 28 days to mature.",
            6000, Integer.MAX_VALUE, ForagingSeedType.Pomegranate_Sapling),
    BasicRetainingSoil(null, "Basic Retaining Soil", "Chance of staying watered overnight.",
            100, Integer.MAX_VALUE, MaterialType.Basic_Retaining_Soil),
    QualityRetainingSoil(null, "Quality Retaining Soil", "CraftingMachine chance of staying watered overnight.",
            150, Integer.MAX_VALUE, MaterialType.Quality_Retaining_Soil),

    // Backpacks
    LargePack(null, "Big Backpack", "Unlocks the 2nd row of inventory (12 more slots, total 24).",
            2000, 1, Backpacks.BigBackpack),
    DeluxePack(null, "Deluxe Backpack", "Unlocks the 3rd row of inventory (infinite slots).",
            10000, 1, Backpacks.DeluxeBackpack),

    // Seeds – Spring
    ParsnipSeeds(Season.Spring, "Parsnip Seeds", "Plant in spring. Takes 4 days to mature.",
            30, 5, ForagingSeedType.Parsnip_Seeds),
    BeanStarter(Season.Spring, "Bean Starter", "Plant in spring. Takes 10 days. Grows on a trellis.",
            90, 5, ForagingSeedType.Bean_Starter),
    CauliflowerSeeds(Season.Spring, "Cauliflower Seeds", "Plant in spring. Takes 12 days.",
            120, 5, ForagingSeedType.Cauliflower_Seeds),
    PotatoSeeds(Season.Spring, "Potato Seeds", "Plant in spring. Takes 6 days.",
            75, 5, ForagingSeedType.Potato_Seeds),
    TulipBulb(Season.Spring, "Tulip Bulb", "Spring flower. Takes 6 days.",
            30, 5, ForagingSeedType.Tulip_Bulb),
    KaleSeeds(Season.Spring, "Kale Seeds", "Spring crop. Takes 6 days.",
            105, 5, ForagingSeedType.Kale_Seeds),
    JazzSeeds(Season.Spring, "Jazz Seeds", "Spring flower. Takes 7 days.",
            45, 5, ForagingSeedType.Jazz_Seeds),
    GarlicSeeds(Season.Spring, "Garlic Seeds", "Plant in spring. Takes 4 days.",
            60, 5, ForagingSeedType.Garlic_Seeds),
    RiceShoot(Season.Spring, "Rice Shoot", "Plant in spring. Takes 8 days. Grows faster near water.",
            60, 5, ForagingSeedType.Rice_Shoot),

    // Seeds – Summer
    MelonSeeds(Season.Summer, "Melon Seeds", "Plant in summer. Takes 12 days.",
            120, 5, ForagingSeedType.Melon_Seeds),
    TomatoSeeds(Season.Summer, "Tomato Seeds", "Plant in summer. Takes 11 days.",
            75, 5, ForagingSeedType.Tomato_Seeds),
    BlueberrySeeds(Season.Summer, "Blueberry Seeds", "Plant in summer. Takes 13 days.",
            120, 5, ForagingSeedType.Blueberry_Seeds),
    PepperSeeds(Season.Summer, "Pepper Seeds", "Plant in summer. Takes 5 days.",
            60, 5, ForagingSeedType.Pepper_Seeds),
    WheatSeeds_Summer(Season.Summer, "Wheat Seeds", "Summer crop. Takes 4 days.",
            15, 5, ForagingSeedType.Wheat_Seeds),
    RadishSeeds(Season.Summer, "Radish Seeds", "Plant in summer. Takes 6 days.",
            60, 5, ForagingSeedType.Radish_Seeds),
    PoppySeeds(Season.Summer, "Poppy Seeds", "Summer flower. Takes 7 days.",
            150, 5, ForagingSeedType.Poppy_Seeds),
    SpangleSeeds(Season.Summer, "Spangle Seeds", "Summer flower. Takes 8 days.",
            75, 5, ForagingSeedType.Spangle_Seeds),
    HopsStarter(Season.Summer, "Hops Starter", "Summer crop. Takes 11 days. Grows on a trellis.",
            90, 5, ForagingSeedType.Hops_Starter),
    CornSeeds_Summer(Season.Summer, "Corn Seeds", "Summer crop. Takes 14 days.",
            225, 5, ForagingSeedType.Corn_Seeds),
    SunflowerSeeds_Summer(Season.Summer, "Sunflower Seeds", "Summer flower. Takes 8 days.",
            300, 5, ForagingSeedType.Sunflower_Seeds),
    RedCabbageSeeds(Season.Summer, "Red Cabbage Seeds", "Summer crop. Takes 9 days.",
            150, 5, ForagingSeedType.Red_Cabbage_Seeds),

    // Seeds – Fall
    EggplantSeeds(Season.Fall, "Eggplant Seeds", "Fall crop. Takes 5 days.",
            30, 5, ForagingSeedType.Eggplant_Seeds),
    CornSeeds_Fall(Season.Fall, "Corn Seeds", "Fall crop. Takes 14 days.",
            225, 5, ForagingSeedType.Corn_Seeds),
    PumpkinSeeds(Season.Fall, "Pumpkin Seeds", "Plant in fall. Takes 13 days.",
            150, 5, ForagingSeedType.Pumpkin_Seeds),
    BokChoySeeds(Season.Fall, "Bok Choy Seeds", "Fall crop. Takes 4 days.",
            75, 5, ForagingSeedType.Bok_Choy_Seeds),
    YamSeeds(Season.Fall, "Yam Seeds", "Fall crop. Takes 10 days.",
            90, 5, ForagingSeedType.Yam_Seeds),
    CranberrySeeds(Season.Fall, "Cranberry Seeds", "Fall crop. Takes 7 days.",
            360, 5, ForagingSeedType.Cranberry_Seeds),
    SunflowerSeeds_Fall(Season.Fall, "Sunflower Seeds", "Fall flower. Takes 8 days.",
            300, 5, ForagingSeedType.Sunflower_Seeds),
    FairySeeds(Season.Fall, "Fairy Seeds", "Fall flower. Takes 12 days.",
            300, 5, ForagingSeedType.Fairy_Seeds),
    AmaranthSeeds(Season.Fall, "Amaranth Seeds", "Fall crop. Takes 7 days.",
            105, 5, ForagingSeedType.Amaranth_Seeds),
    GrapeStarter(Season.Fall, "Grape Starter", "Fall crop. Takes 10 days. Grows on a trellis.",
            90, 5, ForagingSeedType.Grape_Starter),
    WheatSeeds_Fall(Season.Fall, "Wheat Seeds", "Fall crop. Takes 4 days.",
            15, 5, ForagingSeedType.Wheat_Seeds),
    ArtichokeSeeds(Season.Fall, "Artichoke Seeds", "Fall crop. Takes 8 days.",
            45, 5, ForagingSeedType.Artichoke_Seeds);


    private final Season season;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final ItemType itemType;

    PierresGeneralStore(Season season, String name, String description, int price, int dailyLimit, ItemType itemType) {
            this.season = season;
            this.name = name;
            this.description = description;
            this.price = price;
            this.dailyLimit = dailyLimit;
            this.itemType = itemType;
    }

    public Season getSeason() { return season; }
    @Override public String getDisplayName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }
    @Override
    public ItemType getItemType() { return itemType; }

    @Override
    public String toString() {
        return this.name + " - Price: " + this.price;
    }
}
