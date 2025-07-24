package com.example.main.enums.items;


public enum ToolType implements ItemType {
    // Hoes
    Hoe("primitive hoe", 5, true),
    Copper_Hoe("copper hoe", 4, false),
    Steel_Hoe("iron hoe", 3, false),
    Gold_Hoe("gold hoe", 2, false),
    Iridium_Hoe("iridium hoe", 1, false),

    // Pickaxes
    Pickaxe("primitive pickaxe", 5, true),
    Copper_Pickaxe("copper pickaxe", 4, false),
    Steel_Pickaxe("iron pickaxe", 3, false),
    Gold_Pickaxe("gold pickaxe", 2, false),
    Iridium_Pickaxe("iridium pickaxe", 1, false),

    // Axes
    Axe("primitive axe", 5, true),
    Copper_Axe("copper axe", 4, false),
    Steel_Axe("iron axe", 3, false),
    Gold_Axe("gold axe", 2, false),
    Iridium_Axe("iridium axe", 1, false),

    // Watering cans
    Watering_Can("primitive watering can", 5, true),
    Copper_Watering_Can("copper watering can", 4, false),
    Steel_Watering_Can("iron watering can", 3, false),
    Gold_Watering_Can("gold watering can", 2, false),
    Iridium_Watering_Can("iridium watering can", 1, false),

    // Fishing poles
    Training_Rod("training fishing rod", 8, true),
    Bamboo_Rod("bamboo rod", 8, false),
    Fiberglass_Rod("fiberglass rod", 6, false),
    Iridium_Rod("iridium rod", 4, false),

    // Other tools
    Scythe("scythe", 2, true),
    Milk_Pail("milk pail", 4, true),
    Shears("shears", 4, true);

    private final String toolName;
    private final int energyConsumption;
    private final boolean isStarter;

    ToolType(String toolName, int energyConsumption, boolean isStarter) {
        this.toolName = toolName;
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
        return toolName;
    }

    @Override
    public String getEnumName() {
        return name();
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }
}
