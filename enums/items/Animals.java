package enums.items;

import models.item.AnimalProduct;

import java.util.ArrayList;
import java.util.List;

public enum Animals {
    Chicken(800) {
        {
            products.add(AnimalProducts.ChickenEgg);
            products.add(AnimalProducts.BigChickenEgg);
        }
    },

    Duck(1200) {
        {
            products.add(AnimalProducts.DuckEgg);
            products.add(AnimalProducts.DuckFeather);
        }
    },

    Rabbit(1500) {
        {
            products.add(AnimalProducts.RabbitWool);
            products.add(AnimalProducts.RabbitLeg);
        }
    },

    Cow(5000) {
        {
            products.add(AnimalProducts.NormalCowMilk);
            products.add(AnimalProducts.LargeCowMilk);
        }
    },

    Goat(4000) {
        {
            products.add(AnimalProducts.NormalGoatMilk);
            products.add(AnimalProducts.LargeGoatMilk);
        }
    },

    Sheep(3000) {
        {
            products.add(AnimalProducts.SheepWool);
        }
    },

    Dinosaur(10000) {
        {
            products.add(AnimalProducts.DinosaurEgg);
        }
    },

    Pig(2500) {
        {
            products.add(AnimalProducts.Truffle);
        }
    };

    protected final ArrayList<AnimalProducts> products = new ArrayList<>();
    private final int price;

    private Animals(int price) {
        this.price = price;
    }


    public ArrayList<AnimalProducts> getProducts() {
        return new ArrayList<>(products); // Return defensive copy
    }

    public int getPrice() {
        return price;
    }
}
