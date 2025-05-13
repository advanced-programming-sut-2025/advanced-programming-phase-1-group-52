package models;

import enums.player.Skills;
import enums.design.TileType;
import enums.items.MineralType;
import enums.player.Gender;
import enums.player.Skills;
import models.building.House;
import models.item.Mineral;
import models.item.Tool;
import models.item.WateringCan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.building.House;
import models.item.Tool;

public class Player {
    private final String username;
    private final Inventory inventory;
    private final ArrayList<Trade> trades;
    private final ArrayList<Talk> talks;
    private int energy = 200;
    private House house;
    private int originX;
    private int originY;
    private int currentX;
    private int currentY;
    private boolean isFainted = false;
    private Map<Skills, SkillData> skills;
    private Tool currentTool = null;
    private final HashMap<Integer, Gift> gifts;
    private static int giftId = 1;
    private final Gender gender;
    private ArrayList<Notification> notifications;
    private BankAccount bankAccount;
    private Player spouse = null;

    public Player(String username, Gender gender) {
        this.username = username;
        this.gender = gender;
        this.inventory = new Inventory();
        this.trades = new ArrayList<>();
        this.talks = new ArrayList<>();
        this.skills = new HashMap<>();
        this.gifts = new HashMap<>();
        this.bankAccount = new BankAccount();
        this.notifications = new ArrayList<>();
        for(Skills skill : Skills.values()){
            this.skills.put(skill, new SkillData());
        }
        giveStarterTools();
    }

    public void addEnergy(int amount) {
        if (this.energy + amount > 200) this.energy = 200;
        else this.energy += amount;
    }

    public BankAccount getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Player getSpouse() {
        return this.spouse;
    }

    public void setSpouse(Player player) {
        this.spouse = spouse;
    }

    public void addNotif(Player sender, String message) {
        this.notifications.add(new Notification(sender, message));
    }

    public void showNotifs() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Notification notif : this.notifications) {
            stringBuilder.append(notif.getMessage());
        }

        System.out.println(stringBuilder.toString());
    }

    public void resetNotifs() {
        this.notifications = new ArrayList<>();
    }

    public Gender getGender() {
        return this.gender;
    }

    public void rateGift(int id, int rate) {
        this.gifts.get(id).setRate(rate);
    }

    public Gift getGiftById(int id) {
        return this.gifts.get(id);
    }

    public HashMap<Integer, Gift> getGifts() {
        return gifts;
    }

    public ArrayList<Talk> getTalks() {
        return this.talks;
    }

    public void addGift(Gift gift) {
        this.gifts.put(giftId, gift);
        giftId++;
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
            return fishingPoleHandler();
        } else if (typeName.equals("Scythe")) {
            return scytheHandler(tile);
        } else if (typeName.equals("MilkPale")) {
            return milkPaleHandler();
        } else if (typeName.equals("Shear")) {
            return shearHandler();
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

    public Result shearHandler(){}

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
        Mineral newMineral;
        switch (tile.getType()){
            case NormalStone:
                newMineral = new Mineral(MineralType.NormalStone,10);
                break;
                case CopperStone:
                    newMineral = new Mineral(MineralType.CopperStone, 10);
                    break;
                        case GoldStone:
                            newMineral = new Mineral(MineralType.GoldStone, 10);
                            break;
                                case IridiumStone:
                                    newMineral = new Mineral(MineralType.IridiumStone, 10);
                                    break;
                                    case JewelStone:
                                        newMineral = new Mineral(MineralType.JewelStone, 10);
                                        break;
                                        case IronStone:
                                            newMineral = new Mineral(MineralType.IronStone, 10);
                                            break;
                                        case Shoveled:
                                            default:
                                                return new Result(false, "Tile can not be mined!");
        }
        this.inventory.addNumOfItems(1);
        this.inventory.addItem(newMineral);
        return new Result(true, "Tile with X: " + tile.getX() + " Y: " + tile.getY() + " has been mined!");
    }

    public Result axeHandler(Tile tile) {
        SkillData foragingData = skills.get(Skills.Foraging);
        int energyConsumption = currentTool.getToolType().getEnergyConsumption();
        if(foragingData.getLevel() >= 4){
            energyConsumption -= 1;
        }
        if(this.energy <= energyConsumption){
            return new Result(false, "You don't have enough energy to mine!(extract)");
        }
        this.energy -= energyConsumption;
        if(tile.getType().equals(TileType.Branch)){
            tile.setType(TileType.Earth);
            return new Result(true, "Branches on Tile with X: " + tile.getX() + " Y: " + tile.getY() + " has been removed!");
        }
        else if(tile.getType().equals(TileType.Tree)){
            // todo : after completing trees and seeds write this section
        }
        return new Result(false, "you can not use axe on this tile!");
    }

    public Result wateringCanHandler(Tile tile){
        SkillData farmingData = skills.get(Skills.Farming);
        int energyConsumption = currentTool.getToolType().getEnergyConsumption();
        if(farmingData.getLevel() >= 4){
            energyConsumption -= 1;
        }
        if(this.energy <= energyConsumption){
            return new Result(false, "You don't have enough energy to use watering can!");
        }
        this.energy -= energyConsumption;
        WateringCan wateringCan = (WateringCan) this.currentTool;
        if(tile.getType().equals(TileType.Water)){
            wateringCan.fill();
            return new Result(true, "Water can is full now!");
        }
        else if(tile.getType().equals(TileType.Tree)){

        }
        // todo : after completing trees and seeds write this section
        return new Result(false, "You can not use watering on this tile!");
    }

    public Result fishingPoleHandler(){}

    public Result scytheHandler(Tile tile){
        int energyConsumption = currentTool.getToolType().getEnergyConsumption();
        if(this.energy <= energyConsumption){
            return new Result(false, "You don't have enough energy to use scythe!");
        }
        this.energy -= energyConsumption;

        if(tile.getType().equals(TileType.Grass)){
            tile.setType(TileType.Earth);
            return new Result(true, "tile with X: " + tile.getX() + " Y: " + tile.getY() + " has been changed to soil!");
        }
        else if(tile.getType().equals(TileType.Tree)){
            // todo : after completing trees and seeds write this section
        }
        return new Result(false, "you can not use scythe on this tile!");
    }

    public Result milkPaleHandler(){}

    public Result backpackHandler(){}

    public Result trashCanHandler(){}

    public ArrayList<Talk> talks() {
        return talks;
    }

    public void addTalk(Talk talk) {
        this.talks.add(talk);
    }

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

    public House getHouse() {
        return this.house;
    }
}
