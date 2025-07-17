package com.example.main.enums.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.main.enums.items.AnimalProductType;
import com.example.main.enums.items.ArtisanProductType;
import com.example.main.enums.items.CookingRecipeType;
import com.example.main.enums.items.CraftingMachineType;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.FishType;
import com.example.main.enums.items.FoodType;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.MaterialType;
import com.example.main.enums.items.MineralType;
import com.example.main.enums.items.ToolType;
import com.example.main.models.Quest;

public enum NPCType {
    Sebastian(42, 40) {{
        favorites.add(AnimalProductType.SheepWool);
        favorites.add(FoodType.PumpkinPie);
        favorites.add(FoodType.Pizza);

        quests.add(new Quest(this, MaterialType.Iron, 50, MaterialType.Diamond, 2));
        quests.add(new Quest(this, FoodType.PumpkinPie, 1, MaterialType.GoldCoin, 5000));
        quests.add(new Quest(this, MaterialType.Stone, 150, MineralType.QUARTZ, 50));
    }},
    Abigail(52, 40) {{
        favorites.add(MaterialType.Stone);
        favorites.add(MaterialType.IronOre);
        favorites.add(MaterialType.Coffee);

        quests.add(new Quest(this, MaterialType.GoldBar, 1, MaterialType.GoldCoin, 0));
        quests.add(new Quest(this, MaterialType.Pumpkin, 1, MaterialType.GoldCoin, 500));
        quests.add(new Quest(this, MaterialType.Wheat, 50, ToolType.Iridium_Watering_Can, 1));
    }},
    Harvey(32, 50) {{
        favorites.add(MaterialType.Coffee);
        favorites.add(ArtisanProductType.GRAPE_WINE);
        favorites.add(ArtisanProductType.PICKLES);

        Random rand = new Random();
        CropType cropType = CropType.values()[rand.nextInt(CropType.values().length)];
        quests.add(new Quest(this, cropType, 12, MaterialType.GoldCoin, 750));
        quests.add(new Quest(this, FishType.Salmon, 1, MaterialType.GoldCoin, 0));
        quests.add(new Quest(this, ArtisanProductType.GRAPE_WINE, 1, FoodType.Salad, 5));
    }},
    Lia(42, 50) {{
        favorites.add(FoodType.Salad);
        favorites.add(CropType.Grape);
        favorites.add(ArtisanProductType.GRAPE_WINE);

        quests.add(new Quest(this, MaterialType.HardWood, 10, MaterialType.GoldCoin, 500));
        quests.add(new Quest(this, FishType.Salmon, 1, CookingRecipeType.SalmonDinner, 1));
        quests.add(new Quest(this, MaterialType.Wood, 200, CraftingMachineType.DELUXE_SCARECROW, 3));
    }},
    Robin(52, 50) {{
        favorites.add(FoodType.Spaghetti);
        favorites.add(MaterialType.Wood);
        favorites.add(MaterialType.IronBar);

        quests.add(new Quest(this, MaterialType.Wood, 80, MaterialType.GoldCoin, 1000));
        quests.add(new Quest(this, MaterialType.IronBar, 10, CraftingMachineType.BEE_HOUSE, 3));
        quests.add(new Quest(this, MaterialType.Wood, 1000, MaterialType.GoldCoin, 25000));
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
