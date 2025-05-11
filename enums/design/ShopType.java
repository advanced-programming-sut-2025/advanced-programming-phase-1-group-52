package enums.design;

import enums.items.ItemType;

import java.util.ArrayList;

public enum ShopType {
    Blacksmith("Clint", 32, 2){
        {
            items.add();
        }
    },
    JojaMart("Morris", 42, 2){
        {
            items.add();
        }
    },
    PierresGeneralStore("Pierre", 52, 2){
        {
            items.add();
        }
    },
    ArpentersShop("Robin", 32, 12){
        {
            items.add();
        }
    },
    FishShop("Willy", 42, 12){
        {
            items.add();
        }
    },
    MarniesRanch("Marnie", 52, 12){
        {
            items.add();
        }
    },
    TheStardropSaloon("Gus", 32, 22){
        {
            items.add();
        }
    };

    private final String name;
    private final int cornerX;
    private final int cornerY;
    private final ArrayList<ItemType> items = new ArrayList<>();

    ShopType(String name, int cornerX, int cornerY) {
        this.name = name;
        this.cornerX = cornerX;
        this.cornerY = cornerY;
    }

    public String getName() {
        return name;
    }

    public int getCornerX() {
        return cornerX;
    }

    public int getCornerY() {
        return cornerY;
    }
}
