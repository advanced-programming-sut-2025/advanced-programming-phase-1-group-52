package enums.items;

import java.util.ArrayList;

public enum Animals implements ItemType {
    Chicken("Chicken",800) {
        {
            products.add(AnimalProducts.ChickenEgg);
            products.add(AnimalProducts.BigChickenEgg);
        }
    },

    Duck("Duck", 1200) {
        {
            products.add(AnimalProducts.DuckEgg);
            products.add(AnimalProducts.DuckFeather);
        }
    },

    Rabbit("Rabbit", 1500) {
        {
            products.add(AnimalProducts.RabbitWool);
            products.add(AnimalProducts.RabbitLeg);
        }
    },

    Cow("Cow",5000) {
        {
            products.add(AnimalProducts.NormalCowMilk);
            products.add(AnimalProducts.LargeCowMilk);
        }
    },

    Goat("Goat",4000) {
        {
            products.add(AnimalProducts.NormalGoatMilk);
            products.add(AnimalProducts.LargeGoatMilk);
        }
    },

    Sheep("Sheep",3000) {
        {
            products.add(AnimalProducts.SheepWool);
        }
    },

    Dinosaur("Dinosaur",10000) {
        {
            products.add(AnimalProducts.DinosaurEgg);
        }
    },

    Pig("Pig",2500) {
        {
            products.add(AnimalProducts.Truffle);
        }
    };

    protected final ArrayList<AnimalProducts> products = new ArrayList<>();
    private final String name;
    private final int price;

    private Animals(String name, int price) {
        this.price = price;
        this.name = name;
    }


    public ArrayList<AnimalProducts> getProducts() {
        return new ArrayList<>(products); // Return defensive copy
    }

    public int getPrice() {
        return price;
    }
}
