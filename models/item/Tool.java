package models.item;

import enums.items.MaterialType;
import enums.items.ToolType;

public class Tool extends Item{

    public Tool(ToolType toolType,int number) {
        super(toolType, number);
    }

    @Override
    protected int calculateEnergyConsumption() {
        return ((ToolType)itemType).getEnergyConsumption();
    }

    public ToolType getToolType() {
        return (ToolType)itemType;
    }
}
