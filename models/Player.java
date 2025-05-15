package models;

import enums.items.CraftingRecipes;
import enums.items.MaterialType;
import enums.player.Skills;
import enums.design.TileType;
import enums.items.MineralType;
import enums.player.Gender;
import enums.player.Skills;
import models.building.House;
import models.item.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import models.building.House;
import models.item.Tool;

public class Player {
    private final String username;
    private final Inventory inventory;
    private final ArrayList<Trade> trades;
    private final ArrayList<Talk> talks;
    private ArrayList<CraftingRecipe> craftingRecipes;
    private ArrayList<CookingRecipe> cookingRecipes;
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
        this.craftingRecipes = new ArrayList<>();
        this.cookingRecipes = new ArrayList<>();
        for(Skills skill : Skills.values()){
            this.skills.put(skill, new SkillData());
        }
        giveStarterTools();
    }

    public Trade getTradeById(int id) {
        for (Trade trade : this.trades) {
            if (trade.getTradeId() == id) return trade;
        }

        return null;
    }

    public void addTrade(Trade trade) {
        this.trades.add(trade);
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
        Material newMaterial = null;
        Mineral newMineral = null;
        switch (tile.getType()){
            case Stone -> newMaterial = new Material(MaterialType.Stone,10);
            case CopperStone -> newMineral = new Mineral(MineralType.COPPER, 10);
            case GoldStone -> newMineral = new Mineral(MineralType.GOLD, 10);
            case IridiumStone -> newMineral = new Mineral(MineralType.IRIDIUM, 10);
            case JewelStone -> {
                Random rand = new Random();
                int prob = rand.nextInt(10);
                if (prob < 5) {
                    newMineral = new Mineral(MineralType.QUARTZ, 10);
                }
                else if (prob < 8) {
                    newMineral = new Mineral(MineralType.EMERALD, 10);
                }
                else {
                    newMineral = new Mineral(MineralType.DIAMOND, 10);
                }
            }
            case IronStone -> newMineral = new Mineral(MineralType.IRON, 10);
            case Shoveled -> tile.setType(TileType.Earth);
            default -> {
                return new Result(false, "Tile can not be mined!");
            }
        }
        this.inventory.addNumOfItems(1);
        if (newMineral != null) {
            this.inventory.addItem(newMineral);
        }
        else {
            this.inventory.addItem(newMaterial);
        }
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
            tile.setType(TileType.Earth);
            // wood sizes be checked!
            Material wood = new Material(MaterialType.Wood,20);
            if(this.inventory.addNumOfItems(1)){
                this.inventory.addItem(wood);
                return new Result(true, "Tree has been removed and you claimed 20 woods");
            }
            else{
                return new Result(false, "Tree has been removed, but you can not claim any woods, your inventory is full!");
            }
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
        else if(tile.getType().equals(TileType.Tree) || tile.getType().equals(TileType.Planted)){
            tile.getPlant().setWateredToday(true);
            wateringCan.useCan();
            return new Result(true, "Tile with X: " + tile.getX() + " Y: " + tile.getY() + " has been watered!");
        }
        return new Result(false, "You can not use watering on this tile!");
    }

    public Result fishingPoleHandler(){}

    public Result scytheHandler(Tile tile){
        int energyConsumption = currentTool.getToolType().getEnergyConsumption();
        if(this.energy <= energyConsumption){
            return new Result(false, "You don't have enough energy to use scythe!");
        }
        this.energy -= energyConsumption;

        switch (tile.getType()) {
            case Grass -> {
                tile.setType(TileType.Earth);
                return new Result(true, "tile with X: " + tile.getX() + " Y: " + tile.getY() + " has been changed to soil!");
            }
            case Tree -> {
                if(tile.getPlant().isReadyToHarvest()){
                    if(tile.getPlant() instanceof Fruit fruit){
                        Fruit newFruit = new Fruit(fruit.getFruitType(),1);
                        if(this.inventory.addNumOfItems(1)){
                            this.inventory.addItem(newFruit);
                            return new Result(true, "Fruit has been added to the inventory!");
                        }
                        else{
                            return new Result(false, "Fruit has not been added to the inventory!, your inventory is full!");
                        }
                    }
                    else{
                        return new Result(false, "some problem in harvesting (type casting) come to scytheHandler");
                    }
                }
                else{
                    return new Result(false,"This tree is not ready to harvest!");
                }
            }
            case Planted -> {
                if(tile.getPlant().isReadyToHarvest()){
                    if(tile.getPlant() instanceof Crop crop){
                        Crop newCrop = new Crop(crop.getCropType(),1);
                        if(this.inventory.addNumOfItems(1)){
                            this.inventory.addItem(newCrop);
                            return new Result(true, "Crop has been added to the inventory!");
                        }
                        else{
                            return new Result(false, "Crop has not been added to the inventory!, your inventory is full!");
                        }
                    }
                    else{
                        return new Result(false, "some problem in harvesting (type casting) come to scytheHandler");
                    }
                }
                else{
                    return new Result(false,"This seed is not ready to harvest!");
                }
            }
            default -> {
            }
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
            addForagingRecipes();
            addFarmingRecipes();
            addMiningRecipes();
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

    public void addCookingRecipe(CookingRecipe recipe) {
        this.cookingRecipes.add(recipe);
    }

    public void addCraftingRecipe(CraftingRecipe recipe) {
        this.craftingRecipes.add(recipe);
    }

    public ArrayList<CookingRecipe> getCookingRecipe() {
        return cookingRecipes;
    }

    public ArrayList<CraftingRecipe> getCraftingRecipe() {
        return craftingRecipes;
    }

    private void addMiningRecipes() {
        SkillData skillData = findSkillData(Skills.Extraction);
        int level = skillData.getLevel();

        if (level >= 1) {
            if (!hasRecipe(CraftingRecipes.CherryBombRecipe)) {
                craftingRecipes.add(new CraftingRecipe(CraftingRecipes.CherryBombRecipe));
            }
        }

        if (level >= 2) {
            if (!hasRecipe(CraftingRecipes.BombRecipe)) {
                craftingRecipes.add(new CraftingRecipe(CraftingRecipes.BombRecipe));
            }
        }

        if (level >= 3) {
            if (!hasRecipe(CraftingRecipes.MegaBombRecipe)) {
                craftingRecipes.add(new CraftingRecipe(CraftingRecipes.MegaBombRecipe));
            }
        }
    }

    private void addFarmingRecipes() {
        SkillData skillData = findSkillData(Skills.Farming);
        int level = skillData.getLevel();

        if (level >= 1) {
            if (!this.craftingRecipes.contains(CraftingRecipes.SprinklerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.SprinklerRecipe));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.BeeHouseRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.BeeHouseRecipe));
            }
        }

        else if (level >= 2) {
            if (!this.craftingRecipes.contains(CraftingRecipes.QualitySprinklerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.QualitySprinklerRecipe));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.CheesePressRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.CheesePressRecipe));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.PreservesJarRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.PreservesJarRecipe));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.DeluxeScarecrowRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.DeluxeScarecrowRecipe));
            }
        }

        else if (level >= 3) {
            if (!this.craftingRecipes.contains(CraftingRecipes.IridiumSprinklerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.IridiumSprinklerRecipe));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.KegRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.KegRecipe));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.LoomRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.LoomRecipe));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.OilMakerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.OilMakerRecipe));
            }
        }
    }

    private void addForagingRecipes() {
        SkillData skillData = findSkillData(Skills.Foraging);
        int level = skillData.getLevel();

        if (level >= 1) {
            if (!this.craftingRecipes.contains(CraftingRecipes.CharcoalKilnRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.CharcoalKilnRecipe));
            }
        }

        if (level >= 4) {
            if (!this.craftingRecipes.contains(CraftingRecipes.MysticTreeSeedRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.MysticTreeSeedRecipe));
            }
        }
    }

    private SkillData findSkillData(Skills skill) {
        SkillData data = this.skills.get(skill);
        if (data == null) {
            throw new IllegalArgumentException("Skill not found: " + skill);
        }
        return data;
    }

    private boolean hasRecipe(CraftingRecipes recipeType) {
        for (CraftingRecipe recipe : this.craftingRecipes) {
            if (recipe.getRecipeType() == recipeType) {
                return true;
            }
        }
        return false;
    }

}
