package com.example.main.models;

import java.util.ArrayList;
import java.util.List;

import com.example.main.enums.items.ToolType;
import com.example.main.models.item.Tool;
import com.example.main.models.item.WateringCan;

public class StarterKit {
    public static List<Tool> getStarterTools() {
        List<Tool> tools = new ArrayList<>();
        for (ToolType toolType : ToolType.values()) {
            if (toolType.getIsStarter()) {
                if (toolType.name().contains("Watering_Can")) {
                    tools.add(new WateringCan(toolType, 1));
                } else {
                    tools.add(new Tool(toolType, 1));
                }
            }
        }
        return tools;
    }
}
