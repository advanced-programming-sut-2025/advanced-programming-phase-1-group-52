package enums.items;


public enum Tools implements Items {
    // Hoes :
    PrimitiveHoe("hoe",5),
    CopperHoe("hoe",4),
    IronicHoe("hoe",3),
    GoldenHoe("hoe",2),
    IridiumHoe("hoe",1),

    // Pickaxes :
    PrimitivePickaxe("pickaxe",5),
    CopperPickaxe("pickaxe",4),
    IronicPickaxe("pickaxe",3),
    GoldenPickaxe("pickaxe",2),
    IridiumPickaxe("pickaxe",1),

    // Axes :
    PrimitiveAxe("axe",5),
    CopperAxe("axe",4),
    IronicAxe("axe",3),
    GoldenAxe("axe",2),
    IridiumAxe("axe",1),

    // Watering cans :
    PrimitiveWateringCan("waterCan",5),
    CopperWateringCan("waterCan",4),
    IronicWateringCan("waterCan",3),
    GoldenWateringCan("waterCan",2),
    IridiumWateringCan("waterCan",1),

    // Fishing poles :
    EducationalFishingPole("fishingPole",8),
    BambooFishingPole("fishingPole",8),
    FiberglassFishingPole("fishingPole",6),
    IridiumFishingPole("fishingPole",4),
    // Others:
    Seythe("seythe",2),
    MilkPale("milkPale",4),
    Shear("shear",4);

    private int energyConsumption;
    private String typeName;

    Tools(String typeName, int energyConsumption) {
        this.energyConsumption = energyConsumption;
        this.typeName = typeName;
    }
}
