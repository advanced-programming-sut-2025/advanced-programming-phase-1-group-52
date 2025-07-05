package com.example.main.enums.items;


public enum ToolType implements ItemType {
    // Hoes
    PrimitiveHoe("primitive hoe", 5, true),
    CopperHoe("copper hoe", 4, false),
    IronicHoe("iron hoe", 3, false),
    GoldenHoe("gold hoe", 2, false),
    IridiumHoe("iridium hoe", 1, false),

    // Pickaxes
    PrimitivePickaxe("primitive pickaxe", 5, true),
    CopperPickaxe("copper pickaxe", 4, false),
    IronicPickaxe("iron pickaxe", 3, false),
    GoldenPickaxe("gold pickaxe", 2, false),
    IridiumPickaxe("iridium pickaxe", 1, false),

    // Axes
    PrimitiveAxe("primitive axe", 5, true),
    CopperAxe("copper axe", 4, false),
    IronicAxe("iron axe", 3, false),
    GoldenAxe("gold axe", 2, false),
    IridiumAxe("iridium axe", 1, false),

    // Watering cans
    PrimitiveWateringCan("primitive watering can", 5, true),
    CopperWateringCan("copper watering can", 4, false),
    IronicWateringCan("iron watering can", 3, false),
    GoldenWateringCan("gold watering can", 2, false),
    IridiumWateringCan("iridium watering can", 1, false),

    // Fishing poles
    EducationalFishingPole("training fishing rod", 8, true),
    BambooFishingPole("bamboo pole", 8, false),
    FiberglassFishingPole("fiberglass rod", 6, false),
    IridiumFishingPole("iridium rod", 4, false),

    // Other tools
    Scythe("scythe", 2, true),
    MilkPail("milk pail", 4, true),
    Shear("shears", 4, true);

    private final String name;
    private final int energyConsumption;
    private final boolean isStarter;

    ToolType(String name, int energyConsumption, boolean isStarter) {
        this.name = name;
        this.energyConsumption = energyConsumption;
        this.isStarter = isStarter;
    }

    public boolean getIsStarter() {
        return isStarter;
    }

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }
}