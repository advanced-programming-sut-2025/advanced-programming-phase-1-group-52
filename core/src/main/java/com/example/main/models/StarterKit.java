package com.example.main.models;

import java.util.ArrayList;
import java.util.List;

import com.example.main.enums.items.ToolType;
import com.example.main.models.item.Tool;

public class StarterKit {
    public static List<Tool> getStarterTools() {
        List<Tool> tools = new ArrayList<>();
        for (ToolType toolType : ToolType.values()) {
            if (toolType.getIsStarter()) {
                tools.add(new Tool(toolType,1));
            }
        }
        return tools;
    }
}
