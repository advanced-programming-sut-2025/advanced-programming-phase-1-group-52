package enums.items;


public enum ToolType implements ItemType {
    // Hoes :
    PrimitiveHoe(5, true),
    CopperHoe(4, false),
    IronicHoe(3, false),
    GoldenHoe(2, false),
    IridiumHoe(1, false),

    // Pickaxes :
    PrimitivePickaxe(5, true),
    CopperPickaxe(4, false),
    IronicPickaxe(3, false),
    GoldenPickaxe(2, false),
    IridiumPickaxe(1, false),

    // Axes :
    PrimitiveAxe(5, true),
    CopperAxe(4, false),
    IronicAxe(3, false),
    GoldenAxe(2, false),
    IridiumAxe(1, false),

    // Watering cans :
    PrimitiveWateringCan(5, true),
    CopperWateringCan(4, false),
    IronicWateringCan(3, false),
    GoldenWateringCan(2, false),
    IridiumWateringCan(1, false),

    // Fishing poles :
    EducationalFishingPole(8, true),
    BambooFishingPole(8, false),
    FiberglassFishingPole(6, false),
    IridiumFishingPole(4, false),

    Seythe(2, true),
    MilkPale(4, true),
    Shear(4, true);

    private int energyConsumption;
    private final boolean isStarter;

    ToolType(int energyConsumption, boolean isStarter) {
        this.energyConsumption = energyConsumption;
        this.isStarter = isStarter;
    }

    ToolType(int energyConsumption) {
        this(energyConsumption, false);
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public boolean getIsStarter() {
        return isStarter;
    }

    @Override
    public boolean isTool() {
        return true;
    }
}