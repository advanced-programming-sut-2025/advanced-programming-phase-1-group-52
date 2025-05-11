package enums.design;

import enums.items.AnimalProductType;
import enums.items.CookingRecipes;
import enums.items.FishType;
import enums.items.FoodType;
import enums.items.Handicrafts;
import enums.items.Items;
import enums.items.MaterialType;
import enums.items.PlantType;
import enums.items.ToolType;
import java.util.ArrayList;

import models.NPCFriendship;
import models.Quest;
import models.item.Material;

public enum NPCType {
    Sebastian(42, 32) {
        {
            this.favorites.add(AnimalProductType.SheepWool);
            this.favorites.add(FoodType.PumpkinPie);
            this.favorites.add(FoodType.Pizza);
        }
        {
            this.quests.add(new Quest(MaterialType.Iron, 50, MaterialType.Diamond, 2));
            this.quests.add(new Quest(FoodType.PumpkinPie, 1, MaterialType.GoldCoin, 5000));
            this.quests.add(new Quest(MaterialType.Stone, 150, /*Quartz */, 50));
        }
    },
    Abigail(52, 32) {
        {
            this.favorites.add(MaterialType.Stone);
            this.favorites.add(MaterialType.IronOre);
            this.favorites.add(MaterialType.Coffee);
        }
        {
            this.quests.add(new Quest(MaterialType.GoldBar, 1, null, null));
            this.quests.add(new Quest(MaterialType.Pumpkin, 1, MaterialType.GoldCoin, 500));
            this.quests.add(new Quest(MaterialType.Wheat, 50, ToolType.IridiumWateringCan, 1));
        }
    },
    Harvey(32, 42) {
        {
            this.favorites.add(MaterialType.Coffee);
            this.favorites.add() // todo: add torshi(Pickle) and wine
        }
        {
            this.quests.add(new Quest(, 12, MaterialType.GoldCoin, 750)); // todo: an random plant
            this.quests.add(new Quest(FishType.Salmon, 1, null, null));
            this.quests.add(new Quest(/*Wine Bottle */, 1, FoodType.Salad, 5));
        }
    },
    Lia(42, 42) {
        {
            this.favorites.add(FoodType.Salad);
            this.favorites.add() // todo: add grapes and wine
        }
        {
            this.quests.add(new Quest(MaterialType.HardWood, 10, MaterialType.GoldCoin, 500));
            this.quests.add(new Quest(FishType.Salmon, 1, CookingRecipes.SalmonDinner, 1));
            this.quests.add(new Quest(MaterialType.Wood, 200, Handicrafts.DeluxeScarecrow, 3));

        }
    },
    Robin(52, 42) {
        {
            this.favorites.add(FoodType.Spaghetti);
            this.favorites.add(MaterialType.Wood);
            this.favorites.add(MaterialType.IronBar);
        }
        {
            this.quests.add(new Quest(MaterialType.Wood, 80, MaterialType.GoldCoin, 1000));
            this.quests.add(new Quest(MaterialType.IronBar, 10, Handicrafts.BeeHouse, 3));
            this.quests.add(new Quest(MaterialType.Wood, 1000, MaterialType.GoldCoin, 25000));
        }
    };

    public final ArrayList<Items> favorites = new ArrayList<>();
    public final ArrayList<Quest> quests = new ArrayList<>();
    private final int houseCornerX;
    private final int houseCornerY;

    NPCType(int houseCornerX, int houseCornerY) {
        this.houseCornerX = houseCornerX;
        this.houseCornerY = houseCornerY;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public int getHouseCornerX() {
        return houseCornerX;
    }

    public int getHouseCornerY() {
        return houseCornerY;
    }
}
