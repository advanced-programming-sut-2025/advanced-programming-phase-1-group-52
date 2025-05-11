package models;

import enums.design.TileType;
import enums.player.Skills;
import models.item.Item;
import models.item.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    private String username;
    private Inventory inventory;
    private ArrayList<Trade> trades;
    private int energy = 200;
    private int originX;
    private int originY;
    private int currentX;
    private int currentY;
    private boolean isFainted = false;
    private Map<Skills, SkillData> skills;
    private Tool currentTool = null;

    public Player(String username) {
        this.username = username;
        this.inventory = new Inventory();
        this.trades = new ArrayList<>();
        this.skills = new HashMap<>();
        for(Skills skill : Skills.values()){
            this.skills.put(skill, new SkillData());
        }
        giveStarterTools();
    }

    public void addSkillExperience(Skills skill, int amount) {
        SkillData data = skills.get(skill);
        data.addExperience(amount);
        checkLevelUp(data);
    }

    public void harvestCrop() {
        addSkillExperience(Skills.Farming, 5);
    }

    public void extract() {
        addSkillExperience(Skills.Extraction, 10);
    }

    public void foraging() {
        addSkillExperience(Skills.Foraging, 10);
    }

    public void catchFish() {
        addSkillExperience(Skills.Fishing, 5);
    }
    public Result handleToolUse(Tile tile) {
        if (currentTool == null) {
            return new Result(false, "No tool selected");
        }

        String typeName = currentTool.getToolType().name();

        if (typeName.endsWith("Hoe")) {
            return hoeHandler(tile);
        } else if (typeName.endsWith("Pickaxe")) {
            return pickaxeHandler(tile);
        } else if (typeName.endsWith("Axe")) {
            return axeHandler(tile);
        } else if (typeName.endsWith("WateringCan")) {
            return wateringCanHandler(tile);
        } else if (typeName.endsWith("FishingPole")) {
            return fishingPoleHandler(tile);
        } else if (typeName.equals("Seythe")) {
            return scytheHandler(tile);
        } else if (typeName.equals("MilkPale")) {
            return milkPailHandler(tile);
        } else if (typeName.equals("Shear")) {
            return shearHandler(tile);
        } else {
            return new Result(false, "Unknown tool type");
        }
    }

    public Result hoeHandler(Tile tile) {
        SkillData farmingData = skills.get(Skills.Farming);
        int energyConsumption = currentTool.getToolType().getEnergyConsumption();
        if(farmingData.getLevel() >= 4){
            energyConsumption -= 1;
        }
        if(this.energy <= energyConsumption){
            return new Result(false, "You don't have enough energy to farm");
        }
        this.energy -= energyConsumption;
        if(!tile.getType().equals(TileType.Earth)){
            return new Result(false, "you can not shovel this tile!");
        }
        else{
            tile.setType(TileType.Shoveled);
        }
        return new Result(true, "Tile with X: " + tile.getX() + " Y: " + tile.getY() + " has been shoved");
    }

    public void shearHandler(){}
    public Result pickaxeHandler(Tile tile) {
        SkillData extractionData = skills.get(Skills.Extraction);
        int energyConsumption = currentTool.getToolType().getEnergyConsumption();
        if(extractionData.getLevel() >= 4){
            energyConsumption -= 1;
        }
        if(this.energy <= energyConsumption){
            return new Result(false, "You don't have enough energy to mine!(extract)");
        }
        this.energy -= energyConsumption;
        switch (tile.getType()){
            case NormalStone:

        }
    }
    public void axeHandler(){}
    public void wateringCanHandler(){}
    public void fishingPoleHandler(){}
    public void seytheHandler(){}
    public void milkPaleHandler(){}
    public void backpackHandler(){}
    public void trashCanHandler(){}

    public int currentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    public int currentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = Player.this.currentX;
    }

    public boolean checkFaint (){
        return this.energy <= 0;
    }

    public boolean isFainted() {
        return isFainted;
    }

    public void setFainted(boolean fainted) {
        isFainted = fainted;
    }

    public int originY() {
        return originY;
    }

    public void setOriginY(int originY) {
        this.originY = originY;
    }

    public int originX() {
        return originX;
    }

    public void setOriginX(int originX) {
        this.originX = originX;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public ArrayList<Trade> getTrades() {
        return trades;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getUsername() {
        return username;
    }

    public int getSkillLevel(Skills skill) {
        return skills.get(skill).getLevel();
    }

    public int getSkillExperience(Skills skill) {
        return skills.get(skill).getExperience();
    }

    public Tool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(Tool currentTool) {
        this.currentTool = currentTool;
    }

    private void checkLevelUp(SkillData data) {
        int expNeeded = getExpForNextLevel(data.getLevel());

        if (data.getExperience() >= expNeeded) {
            data.deductExperience(expNeeded);
            data.levelUp();
            data.deductExperience(expNeeded);
        }
    }

    private int getExpForNextLevel(int currentLevel) {
        return 100 * (currentLevel + 1) + 50;
    }

    private void giveStarterTools() {
        List<Tool> starterTools = StarterKit.getStarterTools();
        for (Tool tool : starterTools) {
            inventory.addItem(tool);
        }
    }
}
