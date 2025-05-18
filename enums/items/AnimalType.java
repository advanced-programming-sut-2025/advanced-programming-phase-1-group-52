package enums.items;

import java.util.ArrayList;

public enum AnimalType implements ItemType{
    Chicken("Chicken",800, true) {
        {
            products.add(AnimalProductType.ChickenEgg);
            products.add(AnimalProductType.BigChickenEgg);
        }
    },

    Duck("Duck", 1200, true) {
        {
            products.add(AnimalProductType.DuckEgg);
            products.add(AnimalProductType.DuckFeather);
        }
    },

    Rabbit("Rabbit", 1500, true) {
        {
            products.add(AnimalProductType.RabbitWool);
            products.add(AnimalProductType.RabbitLeg);
        }
    },

    Cow("Cow",5000, false) {
        {
            products.add(AnimalProductType.NormalCowMilk);
            products.add(AnimalProductType.LargeCowMilk);
        }
    },

    Goat("Goat",4000, false) {
        {
            products.add(AnimalProductType.NormalGoatMilk);
            products.add(AnimalProductType.LargeGoatMilk);
        }
    },

    Sheep("Sheep",3000, false) {
        {
            products.add(AnimalProductType.SheepWool);
        }
    },

    Dinosaur("Dinosaur",10000, false) {
        {
            products.add(AnimalProductType.DinosaurEgg);
        }
    },

    Pig("Pig",2500, false) {
        {
            products.add(AnimalProductType.Truffle);
        }
    };

    protected final ArrayList<AnimalProductType> products = new ArrayList<>();
    private final String name;
    private final int price;
    private final boolean needsCage;

    private AnimalType(String name, int price, boolean needsCage) {
        this.price = price;
        this.name = name;
        this.needsCage = needsCage;
    }


    public ArrayList<AnimalProductType> getProducts() {
        return new ArrayList<>(products); // Return defensive copy
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean isTool() {
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
