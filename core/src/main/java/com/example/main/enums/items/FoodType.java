// In main/enums/items/FoodType.java

package com.example.main.enums.items;


import com.example.main.enums.player.Skills;
import com.example.main.models.item.Food;
import com.example.main.models.item.Item;

public enum FoodType implements ItemType {
    Fried_Egg("Fried egg", 50, 35, null, false, 0),
    Baked_Fish("Baked Fish", 75, 100, null, false, 0),
    Salad("Salad", 113, 110, null, false, 0),
    Omelet("Omelet", 100, 125, null, false, 0),
    Pumpkin_Pie("Pumpkin Pie", 225, 385, null, false, 0),
    Spaghetti("Spaghetti", 75, 120, null, false, 0),
    Pizza("Pizza", 150, 300, null, false, 0),
    Tortilla("Tortilla", 50, 50, null, false, 0),
    Maki_Roll("Maki Roll", 100, 220, null, false, 0),
    Triple_Shot_Espresso("Triple Shot Espresso", 200, 450, null, true, 5),
    Cookie("Cookie", 90, 140, null, false, 0),
    Hashbrowns("Hashbrowns", 90, 120, Skills.Farming, false, 5),
    Pancakes("Pancakes", 90, 80, Skills.Foraging, false, 11),
    Fruit_Salad("Fruit Salad", 263, 450, null, false, 0),
    Red_Plate("Red Plate", 240, 400, null, true, 3),
    Bread("Bread", 50, 60, null, false, 0),
    Salmon_Dinner("Salmon Dinner", 125, 300, null, false, 0),
    Vegetable_Medley("Vegetable Medley", 165, 120, null, false, 0),
    Farmers_Lunch("Farmer's Lunch", 200, 150, Skills.Farming, false, 5),
    Survival_Burger("Survival Burger", 125, 180, Skills.Foraging, false, 5),
    Dish_O_The_Sea("Dish O' the Sea", 150, 220, Skills.Fishing, false, 5),
    Seafoam_Pudding("Seafoam Pudding", 175, 300, Skills.Fishing, false, 10),
    Miners_Treat("Miner's Treat", 125, 200, Skills.Mining, false, 5),
    Beer("Beer", 100, 0, null, false, 0),
    Coffee("Coffee", 100, 0, null, false, 0);

    private final String name;
    private final int energy;
    private final int sellPrice;
    private final Skills skillBuff;
    private final boolean isBuffMaxEnergy;
    private final int effectiveTime;

    FoodType(String name, int energy, int sellPrice, Skills skillBuff, boolean isBuffMaxEnergy, int effectiveTime) {
        this.name = name;
        this.energy = energy;
        this.sellPrice = sellPrice;
        this.skillBuff = skillBuff;
        this.isBuffMaxEnergy = isBuffMaxEnergy;
        this.effectiveTime = effectiveTime;
    }

    @Override
    public String getName() { return name; }
    public int getEnergy() { return energy; }
    public int getSellPrice() { return sellPrice; }
    public Skills getSkillBuff() { return skillBuff; }
    public boolean isBuffMaxEnergy() { return isBuffMaxEnergy; }
    public int getEffectiveTime() { return effectiveTime; }

    @Override
    public boolean isTool() { return false; }

    public Item createItem(int count) {
        Food foodItem = new Food(this, count);
        foodItem.setNumber(count);
        return foodItem;
    }

    @Override
    public String getEnumName() {
        return name();
    }
}
