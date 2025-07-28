package com.example.main.enums.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.main.enums.items.*;
import com.example.main.models.Quest;

public enum NPCType {
    Sebastian(42, 40) {{
        favorites.add(AnimalProductType.Wool);
        favorites.add(FoodType.Pumpkin_Pie);
        favorites.add(FoodType.Pizza);

        quests.add(new Quest(this, MineralType.Iron, 50, MineralType.Diamond, 2));
        quests.add(new Quest(this, FoodType.Pumpkin_Pie, 1, MaterialType.Gold_Coin, 5000));
        quests.add(new Quest(this, MaterialType.Stone, 150, MineralType.Quartz, 50));
    }},
    Abigail(52, 40) {{
        favorites.add(MaterialType.Stone);
        favorites.add(MineralType.Iron_Ore);
        favorites.add(FoodType.Coffee);

        quests.add(new Quest(this, MineralType.Gold_Bar, 1, MaterialType.Gold_Coin, 0));
        quests.add(new Quest(this, CropType.Pumpkin, 1, MaterialType.Gold_Coin, 500));
        quests.add(new Quest(this, CropType.Wheat, 50, ToolType.Iridium_Watering_Can, 1));
    }},
    Harvey(32, 50) {{
        favorites.add(FoodType.Coffee);
        favorites.add(ArtisanProductType.Wine);
        favorites.add(ArtisanProductType.Pickles);

        Random rand = new Random();
        CropType cropType = CropType.values()[rand.nextInt(CropType.values().length)];
        quests.add(new Quest(this, cropType, 12, MaterialType.Gold_Coin, 750));
        quests.add(new Quest(this, FishType.Salmon, 1, MaterialType.Gold_Coin, 0));
        quests.add(new Quest(this, ArtisanProductType.Wine, 1, FoodType.Salad, 5));
    }},
    Lia(42, 50) {{
        favorites.add(FoodType.Salad);
        favorites.add(CropType.Grape);
        favorites.add(ArtisanProductType.Wine);

        quests.add(new Quest(this, MaterialType.Hardwood, 10, MaterialType.Gold_Coin, 500));
        quests.add(new Quest(this, FishType.Salmon, 1, CookingRecipeType.Salmon_Dinner, 1));
        quests.add(new Quest(this, MaterialType.Wood, 200, CraftingMachineType.Deluxe_Scarecrow, 3));
    }},
    Robin(52, 50) {{
        favorites.add(FoodType.Spaghetti);
        favorites.add(MaterialType.Wood);
        favorites.add(MineralType.Iron_Bar);

        quests.add(new Quest(this, MaterialType.Wood, 80, MaterialType.Gold_Coin, 1000));
        quests.add(new Quest(this, MineralType  .Iron_Bar, 10, CraftingMachineType.Bee_House, 3));
        quests.add(new Quest(this, MaterialType.Wood, 1000, MaterialType.Gold_Coin, 25000));
    }};

    public final List<ItemType> favorites = new ArrayList<>();
    public final List<Quest>      quests    = new ArrayList<>();
    private final int houseCornerX;
    private final int houseCornerY;

    NPCType(int x, int y) {
        this.houseCornerX = x;
        this.houseCornerY = y;
    }

    public List<Quest> getQuests()          { return quests; }
    public List<ItemType> getFavorites()    { return favorites; }
    public int getHouseCornerX()            { return houseCornerX; }
    public int getHouseCornerY()            { return houseCornerY; }
}
