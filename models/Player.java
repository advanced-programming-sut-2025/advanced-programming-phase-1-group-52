package models;

import enums.player.Skills;
import models.item.Item;
import models.item.Tool;

import java.util.ArrayList;
import java.util.HashMap;
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
    }

    public void addSkillExperience(Skills skill, int amount) {
        SkillData data = skills.get(skill);
        data.addExperience(amount);
        checkLevelUp(data);
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

    public void hoeHandler(){}
    public void shearHandler(){}
    public void pickaxeHandler(){}
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

    public int energy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public ArrayList<Trade> trades() {
        return trades;
    }

    public Inventory inventory() {
        return inventory;
    }

    public String username() {
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

    // todo: having private method for each tool functionality
}
