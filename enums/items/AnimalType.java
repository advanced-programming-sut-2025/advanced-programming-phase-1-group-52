package enums.items;

import java.util.ArrayList;

public enum AnimalType implements ItemType{
    Chicken("Chicken",800) {
        {
            products.add(AnimalProductType.ChickenEgg);
            products.add(AnimalProductType.BigChickenEgg);
        }
    },

    Duck("Duck", 1200) {
        {
            products.add(AnimalProductType.DuckEgg);
            products.add(AnimalProductType.DuckFeather);
        }
    },

    Rabbit("Rabbit", 1500) {
        {
            products.add(AnimalProductType.RabbitWool);
            products.add(AnimalProductType.RabbitLeg);
        }
    },

    Cow("Cow",5000) {
        {
            products.add(AnimalProductType.NormalCowMilk);
            products.add(AnimalProductType.LargeCowMilk);
        }
    },

    Goat("Goat",4000) {
        {
            products.add(AnimalProductType.NormalGoatMilk);
            products.add(AnimalProductType.LargeGoatMilk);
        }
    },

    Sheep("Sheep",3000) {
        {
            products.add(AnimalProductType.SheepWool);
        }
    },

    Dinosaur("Dinosaur",10000) {
        {
            products.add(AnimalProductType.DinosaurEgg);
        }
    },

    Pig("Pig",2500) {
        {
            products.add(AnimalProductType.Truffle);
        }
    };

    protected final ArrayList<AnimalProductType> products = new ArrayList<>();
    private final String name;
    private final int price;

    private AnimalType(String name, int price) {
        this.price = price;
        this.name = name;
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
