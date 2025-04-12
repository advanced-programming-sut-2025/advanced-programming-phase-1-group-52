package enums.design;

import enums.items.Items;

import java.util.ArrayList;

public enum Shops {
    Blacksmith("Clint"){
        {
            items.add();
        }
    },
    JojaMart("Morris"){
        {
            items.add();
        }
    },
    PierresGeneralStore("Pierre"){
        {
            items.add();
        }
    },
    arpentersShop("Robin"){
        {
            items.add();
        }
    },
    FishShop("Willy"){
        {
            items.add();
        }
    },
    MarniesRanch("Marnie"){
        {
            items.add();
        }
    },
    TheStardropSaloon("Gus"){
        {
            items.add();
        }
    };

    private final String name;

    public  ArrayList<Items> items = new ArrayList<>();


    Shops(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
