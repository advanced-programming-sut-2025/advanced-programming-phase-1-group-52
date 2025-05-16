package enums.design.Shop;

import enums.items.AnimalType;
import enums.items.ItemType;
import enums.items.MaterialType;

public enum MarniesRanch implements ShopEntry{

    // Shop
    Hay(MaterialType.Hay, null, "Hay",
            "Dried grass used as animal food.",
            50, Integer.MAX_VALUE, null),
    MilkPail(MaterialType.MilkPail, null, "Milk Pail",
            "Gather milk from your animals.",
            1000, 1, null),
    Shears(MaterialType.Shears, null, "Shears",
            "Use this to collect wool from sheep",
            1000, 1, null),

    // PurchasedAnimal
    Chicken(null,AnimalType.Chicken, "Chicken",
            "Well cared-for chickens lay eggs every day. Lives in the coop.", 800, 2, "Coop"),
    Cow(null,AnimalType.Cow, "Cow",
            "Can be milked daily. A milk pail is required to harvest the milk. Lives in the barn.",
            1500, 2, "Barn"),
    Goat(null,AnimalType.Goat, "Goat",
            "Happy provide goat milk every other day. A milk pail is required to harvest the milk. Lives in the barn.",
            4000, 2, "Big Barn"),
    Duck(null,AnimalType.Duck, "Duck",
            "Happy lay duck eggs every other day. Lives in the coop.",
            1200, 2, "Big Coop"),
    Sheep(null,AnimalType.Sheep, "Sheep",
            "Can be shorn for wool. A pair of shears is required to harvest the wool. Lives in the barn.",
            8000, 2, "Deluxe Barn"),
    Rabbit(null,AnimalType.Rabbit, "Rabbit",
            "These are wooly rabbits! They shed precious wool every few days. Lives in the coop.",
            8000, 2, "Deluxe Coop"),
    Dinosaur(null,AnimalType.Dinosaur, "Dinosaur",
            "The Dinosaur is a farm animal that lives in a Big Coop",
            14000, 2, "Big Coop"),
    Pig(null,AnimalType.Pig, "Pig",
            "These pigs are trained to find truffles! Lives in the barn.",
            16000, 2, "Deluxe Barn");



    private final MaterialType materialType;
    private final AnimalType animalType;
    private final String name;
    private final String description;
    private final int price;
    private final int dailyLimit;
    private final String buildingRequired;

    MarniesRanch(MaterialType materialType, AnimalType animalType, String name, String description, int price, int dailyLimit, String buildingRequired) {

        this.materialType = materialType;
        this.animalType = animalType;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dailyLimit = dailyLimit;
        this.buildingRequired = buildingRequired;
    }

    public MaterialType getMaterialType() { return materialType; }
    public AnimalType getAnimalType() { return animalType; }
    @Override public String getDisplayName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getPrice() { return price; }
    @Override public int getDailyLimit() { return dailyLimit; }

    @Override
    public ItemType getItemType() {
        return null;
    }

    public String getBuildingRequired() { return buildingRequired; }
}
