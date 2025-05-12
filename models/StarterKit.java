package models;

import enums.items.ToolType;
import models.item.Tool;

import java.util.ArrayList;
import java.util.List;

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
