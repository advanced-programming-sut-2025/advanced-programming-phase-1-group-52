package enums.items;


public enum FoodType implements ItemType {
    FriedEgg("Fried egg", 50, null, "Starter", 35),
    BakedFish("Baked Fish",75, null, "Starter", 100),
    Salad("Salad", 113, null, "Starter", 110),

    // Stardrop Saloon Dishes
    Omelet("Omelet", 100, null, "Stardrop Saloon", 125),
    PumpkinPie("Pumpkin pie", 225, null, "Stardrop Saloon", 385),
    Spaghetti("Spaghetti", 75, null, "Stardrop Saloon", 120),
    Pizza("Pizza", 150, null, "Stardrop Saloon", 300),
    Tortilla("Tortilla", 50, null, "Stardrop Saloon", 50),
    MakiRoll("Maki Roll", 100, null, "Stardrop Saloon", 220),
    TripleShotEspresso("Triple Shot Espresso", 200, "Max Energy +100 (5 hours)", "Stardrop Saloon", 450),
    Cookie("Cookie", 90, null, "Stardrop Saloon", 140),
    HashBrowns("Hash browns", 90, "Farming (5 hours)", "Stardrop Saloon", 120),
    Pancakes("Pancakes", 90, "Foraging (11 hours)", "Stardrop Saloon", 80),
    FruitSalad("Fruit salad", 263, null, "Stardrop Saloon", 450),
    RedPlate("Red plate", 240, "Max Energy +50 (3 hours)", "Stardrop Saloon", 400),
    Bread("Bread", 50, null, "Stardrop Saloon", 60),

    // Special Dishes
    SalmonDinner("Salmon dinner", 125, null, "Leah reward", 300),
    VegetableMedley("Vegetable medley", 165, null, "Foraging Level 2", 120),
    FarmersLunch("Farmer's lunch", 200, "Farming (5 hours)", "Farming level 1", 150),
    SurvivalBurger("Survival burger", 125, "Foraging (5 hours)", "Foraging level 3", 180),
    DishOTheSea("Dish O' the Sea", 150, "Fishing (5 hours)", "Fishing level 2", 220),
    SeafoamPudding("Seafoam Pudding", 175, "Fishing (10 hours)", "Fishing level 3", 300),
    MinersTreat("Miner's treat", 125, "Mining (5 hours)", "Mining level 1", 200);

    private final String name;
    private final int energy;
    private final String buff;
    private final String source;
    private final int sellPrice;

    FoodType(String name, int energy, String buff, String source, int sellPrice) {
        this.name = name;
        this.energy = energy;
        this.buff = buff;
        this.source = source;
        this.sellPrice = sellPrice;
    }

    // Getters
    public String getName() { return name; }
    public int getEnergy() { return energy; }
    public String getBuff() { return buff; }
    public String getSource() { return source; }
    public int getSellPrice() { return sellPrice; }
}
