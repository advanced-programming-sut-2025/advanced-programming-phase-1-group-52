package com.example.main.models.item;

import com.example.main.enums.items.ToolType;

public class Tool extends Item{
    private int level = 0;
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
    public void setToolType(ToolType toolType) {
        this.itemType = toolType;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public boolean isMax(){
        return this.level >= 4;
    }
}
