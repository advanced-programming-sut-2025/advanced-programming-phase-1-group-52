package enums.items;


public enum ToolType implements Items {
    // Hoes :
    PrimitiveHoe(5),
    CopperHoe(4),
    IronicHoe(3),
    GoldenHoe(2),
    IridiumHoe(1),


    // Pickaxes :
    PrimitivePickaxe(5),
    CopperPickaxe(4),
    IronicPickaxe(3),
    GoldenPickaxe(2),

    IridiumPickaxe(1),

    // Axes :
    PrimitiveAxe(5),
    CopperAxe(4),
    IronicAxe(3),
    GoldenAxe(2),
    IridiumAxe(1),

    // Watering cans :
    PrimitiveWateringCan(5),
    CopperWateringCan(4),
    IronicWateringCan(3),
    GoldenWateringCan(2),
    IridiumWateringCan(1),

    // Fishing poles :
    EducationalFishingPole(8),
    BambooFishingPole(8),
    FiberglassFishingPole(6),
    IridiumFishingPole(4),
    Seythe(2),
    MilkPale(4),
    Shear(4);

    private int energyConsumption;

    ToolType(int energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }
}