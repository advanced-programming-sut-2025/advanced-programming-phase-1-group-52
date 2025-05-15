package enums.design;

import enums.items.AnimalProductType;
import enums.items.CookingRecipes;
import enums.items.CropType;
import enums.items.FishType;
import enums.items.FoodType;
import enums.items.ItemType;
import enums.items.MaterialType;
import enums.items.MineralType;
import enums.items.ToolType;
import java.util.ArrayList;
import models.Quest;

public enum NPCType {
    Sebastian(42, 32) {
        {
            this.favorites.add(AnimalProductType.SheepWool);
            this.favorites.add(FoodType.PumpkinPie);
            this.favorites.add(FoodType.Pizza);
        }
        {
            this.quests.add(new Quest(this, MaterialType.Iron, 50, MaterialType.Diamond, 2));
            this.quests.add(new Quest(this, FoodType.PumpkinPie, 1, MaterialType.GoldCoin, 5000));
            this.quests.add(new Quest(this, MaterialType.Stone, 150, MineralType.QUARTZ, 50));
        }
    },
    Abigail(52, 32) {
        {
            this.favorites.add(MaterialType.Stone);
            this.favorites.add(MaterialType.IronOre);
            this.favorites.add(MaterialType.Coffee);
        }
        {
            this.quests.add(new Quest(this, MaterialType.GoldBar, 1, null, null));
            this.quests.add(new Quest(this, MaterialType.Pumpkin, 1, MaterialType.GoldCoin, 500));
            this.quests.add(new Quest(this, MaterialType.Wheat, 50, ToolType.IridiumWateringCan, 1));
        }
    },
    Harvey(32, 42) {
        {
            this.favorites.add(MaterialType.Coffee);
            this.favorites.add() // todo: add torshi(Pickle) and wine
        }
        {
            this.quests.add(new Quest(this, , 12, MaterialType.GoldCoin, 750)); // todo: an random plant
            this.quests.add(new Quest(this, FishType.Salmon, 1, null, null));
            this.quests.add(new Quest(this, /*Wine Bottle */, 1, FoodType.Salad, 5));
        }
    },
    Lia(42, 42) {
        {
            this.favorites.add(FoodType.Salad);
            this.favorites.add(CropType.Grape); // todo: add grapes and wine
        }
        {
            this.quests.add(new Quest(this, MaterialType.HardWood, 10, MaterialType.GoldCoin, 500));
            this.quests.add(new Quest(this, FishType.Salmon, 1, CookingRecipes.SalmonDinner, 1));
            this.quests.add(new Quest(this, MaterialType.Wood, 200, ArtisanMachineProductType.DELUXE_SCARECROW, 3));

        }
    },
    Robin(52, 42) {
        {
            this.favorites.add(FoodType.Spaghetti);
            this.favorites.add(MaterialType.Wood);
            this.favorites.add(MaterialType.IronBar);
        }
        {
            this.quests.add(new Quest(this, MaterialType.Wood, 80, MaterialType.GoldCoin, 1000));
            this.quests.add(new Quest(this, MaterialType.IronBar, 10, ArtisanMachineProductType.BEE_HOUSE, 3));
            this.quests.add(new Quest(this, MaterialType.Wood, 1000, MaterialType.GoldCoin, 25000));
        }
    };

    public final ArrayList<ItemType> favorites = new ArrayList<>();
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

    public ArrayList<ItemType> getFavorites() {
        return favorites;
    }
}
