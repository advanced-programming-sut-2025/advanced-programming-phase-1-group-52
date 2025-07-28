package com.example.main.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.main.enums.design.TileType;
import com.example.main.enums.items.*;
import com.example.main.enums.player.Gender;
import com.example.main.enums.player.Skills;
import com.example.main.models.building.House;
import com.example.main.models.building.Housing;
import com.example.main.models.item.CookingRecipe;
import com.example.main.models.item.CraftingRecipe;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fruit;
import com.example.main.models.item.Item;
import com.example.main.models.item.Material;
import com.example.main.models.item.Mineral;
import com.example.main.models.item.PurchasedAnimal;
import com.example.main.models.item.Tool;
import com.example.main.models.item.TrashCan;
import com.example.main.models.item.WateringCan;

public class Player {
    private final String username;
    private final Inventory inventory;
    private final ArrayList<Trade> trades;
    private final ArrayList<Talk> talks;
    private ArrayList<CraftingRecipe> craftingRecipes;
    private ArrayList<CookingRecipe> cookingRecipes;
    private final ArrayList<ActiveBuff> activeBuffs = new ArrayList<>();
    private int energy;
    private House house;
    private HouseRefrigerator houseRefrigerator;
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
    private final List<Housing> housings = new ArrayList<>();
    private static int nextHousingId = 1;
    private int trashCanX;
    private int trashCanY;

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
        this.houseRefrigerator = new HouseRefrigerator();
        this.energy = 200;
        for(Skills skill : Skills.values()){
            this.skills.put(skill, new SkillData());
        }
        giveStarterTools();
        addTrashCan();
    }

    public Trade getTradeById(int id) {
        for (Trade trade : this.trades) {
            if (trade.getTradeId() == id) return trade;
        }

        return null;
    }

    public ArrayList<ActiveBuff> getActiveBuffs() {
        return activeBuffs;
    }

    public Map<Skills, SkillData> getSkills() {
        return this.skills;
    }

    public SkillData getSkillData(Skills skill) {
        return this.skills.get(skill);
    }

    public int getTrashCanX() {
        return this.trashCanX;
    }

    public int getTrashCanY() {
        return this.trashCanY;
    }

    public void setTrashCanX(int x) {
        this.trashCanX = x;
    }

    public void setTrashCanY(int y) {
        this.trashCanY = y;
    }

    public void addTrade(Trade trade) {
        this.trades.add(trade);
    }

    public void addEnergy(int amount) {
        if (this.energy + amount > 200) this.energy = 200;
        else this.energy += amount;
    }

    public void reduceEnergy(int amount) {
        this.energy -= amount;
        if (this.energy < 0) {
            this.energy = 0;
            this.isFainted = true;
        }
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
        this.spouse = player;
    }

    public void addNotif(Player sender, String message) {
        this.notifications.add(new Notification(sender, message));
    }

    public void showNotifs() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Notification notif : this.notifications) {
            stringBuilder.append(notif.getMessage()).append("\n--------------------------------\n");
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
        checkLevelUp(skill, data);
    }

    public void harvestCrop() {
        addSkillExperience(Skills.Farming, 5);
    }

    public void extract() {
        addSkillExperience(Skills.Mining, 10);
    }

    public void foraging() {
        addSkillExperience(Skills.Foraging, 10);
    }

    public void catchFish() {
        addSkillExperience(Skills.Fishing, 5);
    }

    public Result handleToolUse(Tile tile) {
        if (currentTool == null) {
            return new Result(false, "No tool equipped!");
        }
        if (tile == null) {
            return new Result(false, "Cannot use tool on an invalid tile.");
        }

        ToolType type = currentTool.getToolType();
        int energyConsumption = type.getEnergyConsumption();

        // Apply skill-based energy reduction
        if (type.name().contains("Hoe") && getSkillLevel(Skills.Farming) >= 4) energyConsumption--;
        if (type.name().contains("Pickaxe") && getSkillLevel(Skills.Mining) >= 4) energyConsumption--;
        if (type.name().contains("Axe") && getSkillLevel(Skills.Foraging) >= 4) energyConsumption--;
        if (type.name().contains("Watering_Can") && getSkillLevel(Skills.Farming) >= 4) energyConsumption--;

        if (this.energy < energyConsumption) {
            return new Result(false, "Not enough energy!");
        }

        // --- Tool Specific Logic ---
        if (type.name().contains("Pickaxe")) {
            if (tile.getItem() instanceof Mineral || tile.getType() == TileType.Stone) {
                if (inventory.isFull()) {
                    return new Result(false, "Your inventory is full.");
                }
                if (tile.getItem() instanceof Mineral) {
                    Mineral mineral = (Mineral) tile.getItem();
                    inventory.addItem(mineral);
                    addSkillExperience(Skills.Mining, 15);
                    tile.setItem(null);
                    tile.setType(TileType.Earth);
                    reduceEnergy(energyConsumption);
                    return new Result(true, "You found " + mineral.getName() + "!");
                }
                if (tile.getType() == TileType.Stone) {
                    inventory.addItem(new Material(MaterialType.Stone, 5));
                    addSkillExperience(Skills.Mining, 10);
                    tile.setType(TileType.Earth);
                    reduceEnergy(energyConsumption);
                    return new Result(true, "Broke a stone.");
                }
            }
            return new Result(false, "Nothing to break here.");
        }

        if (type.name().contains("Hoe")) {
            if (tile.getType() == TileType.Earth) {
                tile.setType(TileType.Shoveled);
                reduceEnergy(energyConsumption);
                return new Result(true, "Tilled the soil.");
            }
            return new Result(false, "Can only till dirt.");
        }

        if (type.name().contains("Axe")) {
            if (tile.getType() == TileType.Tree) {
                if (inventory.isFull()) {
                    return new Result(false, "Your inventory is full.");
                }
                tile.setType(TileType.Earth);
                tile.setPlant(null);
                tile.setTree(null);
                inventory.addItem(new Material(MaterialType.Wood, 10));
                addSkillExperience(Skills.Foraging, 12);
                reduceEnergy(energyConsumption);
                return new Result(true, "Chopped down a tree.");
            }
            return new Result(false, "Can't use the axe on that.");
        }

        if (type.name().contains("Watering_Can")) {
            WateringCan can = (WateringCan) currentTool;
            if (tile.getType() == TileType.Water) {
                can.fill();
                return new Result(true, "Watering can refilled.");
            }
            if (can.getFilledCapacity() <= 0) {
                return new Result(false, "Watering can is empty.");
            }
            if (tile.getPlant() != null && !tile.getPlant().isWateredToday()) {
                tile.getPlant().setWateredToday(true);
                can.useCan();
                reduceEnergy(energyConsumption);
                return new Result(true, "Watered the plant.");
            }
            return new Result(false, "Nothing to water here.");
        }

        if (type == ToolType.Scythe) {
            if (inventory.isFull()) {
                return new Result(false, "Your inventory is full.");
            }

            if (tile.getPlant() != null && tile.getPlant().isReadyToHarvest()) {
                if (tile.getPlant() instanceof Crop) {
                    Crop crop = (Crop) tile.getPlant();
                    ItemType itemType = crop.getCropType();
                    if (itemType instanceof CropType cropType) {
                        inventory.addItem(new Crop(cropType, 1));
                        addSkillExperience(Skills.Farming, 10);

                        if (cropType.isOneTimeHarvest()) {
                            tile.setPlant(null);
                            tile.setType(TileType.Shoveled);
                        } else {
                            int totalTime = cropType.getTotalHarvestTime();
                            int regrowthTime = cropType.getRegrowthTime() != null ? cropType.getRegrowthTime() : 0;
                            crop.setDayPassed(totalTime - regrowthTime);
                            crop.setReadyToHarvest(false);
                        }
                        reduceEnergy(energyConsumption);
                        return new Result(true, "Harvested a " + cropType.getName() + "!");

                    } else if (itemType instanceof ForagingCropType foragingCropType) {
                        inventory.addItem(new Crop(foragingCropType, 1));
                        addSkillExperience(Skills.Foraging, 7); // Foraging XP for these
                        tile.setPlant(null); // Wild forageables are always one-time harvests
                        tile.setType(TileType.Earth);
                        reduceEnergy(energyConsumption);
                        return new Result(true, "Harvested a " + foragingCropType.getName() + "!");
                    }
                }  else if (tile.getPlant() instanceof Fruit) {
                    Fruit fruitTree = (Fruit) tile.getPlant();
                    int harvestAmount = fruitTree.getTreeType().getHarvestCycle();
                    Fruit harvestedFruit = new Fruit(fruitTree.getFruitType(), harvestAmount);
                    inventory.addItem(harvestedFruit);
                    fruitTree.setHasBeenHarvestedToday(true); // Prevents re-harvesting with 'J' key

                    fruitTree.setReadyToHarvest(false);
                    fruitTree.setDayPassed(0);
                    fruitTree.setCurrentStage(1);

                    reduceEnergy(energyConsumption);
                    return new Result(true, "You harvested " + harvestAmount + " " + harvestedFruit.getName() + ".");
                }
            }

            if (tile.getType() == TileType.Grass) {
                inventory.addItem(new Material(MaterialType.Fiber, 1));
                tile.setType(TileType.Earth);
                reduceEnergy(energyConsumption);
                return new Result(true, "Cleared some grass.");
            }

            return new Result(false, "Nothing to harvest here.");
        }

        return new Result(false, "This tool can't be used here.");
    }


    public void backpackHandler(){}

    public void trashCanHandler(){}

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
        this.currentX = currentX;
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

    private void checkLevelUp(Skills skill, SkillData data) {
        int expNeeded = getExpForNextLevel(data.getLevel());

        if (data.getExperience() >= expNeeded) {
            data.deductExperience(expNeeded);
            data.levelUp();

            // Create and add the notification message
            String message = skill.name() + " skill increased to level " + data.getLevel() + "!";
            addSystemNotification(message);

            addForagingRecipes();
            addFarmingRecipes();
            addMiningRecipes();
            addFishingRecipes();
        }
    }

    // Add this new method to create system messages (without a sender)
    public void addSystemNotification(String message) {
        if (this.notifications == null) {
            this.notifications = new ArrayList<>();
        }
        this.notifications.add(new Notification(null, message));
    }

    private int getExpForNextLevel(int currentLevel) {
        return 100 * (currentLevel + 1) + 50;
    }

    private void giveStarterTools() {
        List<Tool> starterTools = StarterKit.getStarterTools();
        for (Tool tool : starterTools) {
            inventory.getItems().add(tool);
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
        SkillData skillData = findSkillData(Skills.Mining);
        int level = skillData.getLevel();

        if (level >= 1) {
            if (!hasRecipe(CraftingRecipes.CherryBombRecipe)) {
                craftingRecipes.add(new CraftingRecipe(CraftingRecipes.
                    CherryBombRecipe, 1));
            }
            if(!this.cookingRecipes.contains(CookingRecipeType.Miners_Treat)){
                this.cookingRecipes.add(new CookingRecipe(CookingRecipeType.Miners_Treat));
            }
        }

        if (level >= 2) {
            if (!hasRecipe(CraftingRecipes.BombRecipe)) {
                craftingRecipes.add(new CraftingRecipe(CraftingRecipes.BombRecipe,1));
            }
        }

        if (level >= 3) {
            if (!hasRecipe(CraftingRecipes.MegaBombRecipe)) {
                craftingRecipes.add(new CraftingRecipe(CraftingRecipes.MegaBombRecipe, 1));
            }
        }
    }

    private void addFishingRecipes() {
        SkillData skillData = findSkillData(Skills.Fishing);
        int level = skillData.getLevel();
        if(level >= 2){
            if(!this.cookingRecipes.contains(CookingRecipeType.Dish_O_The_Sea)){
                this.cookingRecipes.add(new CookingRecipe(CookingRecipeType.Dish_O_The_Sea));
            }
        }
        if (level >= 3) {
            if(!this.cookingRecipes.contains(CookingRecipeType.Seafoam_Pudding)){
                this.cookingRecipes.add(new CookingRecipe(CookingRecipeType.Seafoam_Pudding));
            }
        }
    }

    private void addFarmingRecipes() {
        SkillData skillData = findSkillData(Skills.Farming);
        int level = skillData.getLevel();

        if (level >= 1) {
            if (!this.craftingRecipes.contains(CraftingRecipes.SprinklerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.SprinklerRecipe, 1));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.BeeHouseRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.BeeHouseRecipe, 1));
            }
            if(!this.cookingRecipes.contains(CookingRecipeType.Farmers_Lunch)) {
                this.cookingRecipes.add(new CookingRecipe(CookingRecipeType.Farmers_Lunch));
            }
        }

        else if (level >= 2) {
            if (!this.craftingRecipes.contains(CraftingRecipes.QualitySprinklerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.QualitySprinklerRecipe,1));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.CheesePressRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.CheesePressRecipe,1));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.PreservesJarRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.PreservesJarRecipe,1));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.DeluxeScarecrowRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.DeluxeScarecrowRecipe,1));
            }
        }

        else if (level >= 3) {
            if (!this.craftingRecipes.contains(CraftingRecipes.IridiumSprinklerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.IridiumSprinklerRecipe,1));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.KegRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.KegRecipe,1));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.LoomRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.LoomRecipe,1));
            }
            if (!this.craftingRecipes.contains(CraftingRecipes.OilMakerRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.OilMakerRecipe,1));
            }
        }
    }

    private void addForagingRecipes() {
        SkillData skillData = findSkillData(Skills.Foraging);
        int level = skillData.getLevel();

        if (level >= 1) {
            if (!this.craftingRecipes.contains(CraftingRecipes.CharcoalKilnRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.CharcoalKilnRecipe,1));
            }
        }
        if (level >= 2) {
            if(!this.cookingRecipes.contains(CookingRecipeType.Vegetable_Medley)){
                this.cookingRecipes.add(new CookingRecipe(CookingRecipeType.Vegetable_Medley));
            }
        }
        if(level >= 3){
            if(!this.cookingRecipes.contains(CookingRecipeType.Survival_Burger)){
                this.cookingRecipes.add(new CookingRecipe(CookingRecipeType.Survival_Burger));
            }
        }
        if (level >= 4) {
            if (!this.craftingRecipes.contains(CraftingRecipes.MysticTreeSeedRecipe)) {
                this.craftingRecipes.add(new CraftingRecipe(CraftingRecipes.MysticTreeSeedRecipe,1));
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

    public void addHousing(CageType cageType, int x, int y) {
        Housing h = new Housing(nextHousingId++, cageType, x, y);
        housings.add(h);
    }

    public Result addAnimalToHousing(int housingId, PurchasedAnimal purchasedAnimal) {
        for (Housing h : housings) {
            if (h.getId() == housingId) {
                if (h.addAnimal(purchasedAnimal)) {
                    return new Result(true,
                        purchasedAnimal.getType().getName() +
                            " named \"" + purchasedAnimal.getName() +
                            "\" was successfully added to " +
                            h.getType().getName() +
                            " number " + housingId + "."
                    );
                } else {
                    return new Result(false,
                        "The capacity of " +
                            h.getType().getName() +
                            " number " + housingId +
                            " is full."
                    );
                }
            }
        }
        return new Result(false, "No housing found with ID " + housingId + ".");
    }

    public List<Housing> getHousings() {
        return housings;
    }

    private void addTrashCan(){
        inventory.getItems().add(new TrashCan(TrashCanType.Trash_Can,1));
    }

    public TrashCan getTrashCan() {
        for (Item item : inventory.getItems()) {
            if (item instanceof TrashCan) {
                return (TrashCan) item;
            }
        }
        return null;
    }

    public HouseRefrigerator getHouseRefrigerator() {
        return houseRefrigerator;
    }

    public void setHouseRefrigerator(HouseRefrigerator houseRefrigerator) {
        this.houseRefrigerator = houseRefrigerator;
    }

    public int getGiftId() {
        return giftId;
    }

    public ArrayList<Tool> getTools() {
        ArrayList<Tool> tools = new ArrayList<>();
        for (Item item : this.inventory.getItems()) {
            if (item instanceof Tool) {
                tools.add((Tool) item);
            }
        }
        return tools;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public int getNextHousingId() {
        return nextHousingId;
    }
}
