package enums.items;

public enum AnimalProductType implements Items {
    ChickenEgg("ChickenEgg",50),
    BigChickenEgg("BigChickenEgg",95),
    DuckEgg("DuckEgg",95),
    DuckFeather("DuckFeather",250),
    RabbitWool("RabbitWool",340),
    RabbitLeg("RabbitLeg",565),
    DinosaurEgg("DinosaurEgg",350),
    NormalCowMilk("NormalCowMilk",125),
    LargeCowMilk("LargeCowMilk",190),
    NormalGoatMilk("NormalGoatMilk",225),
    LargeGoatMilk("LargeGoatMilk",345),
    SheepWool("SheepWool",340),
    Truffle("Truffle",625);

    private String name;
    private int price;

    AnimalProductType(String name, int price) {
        this.name = name;
        this.price = price;
    }
    public int getPrice() {
        return price;
    }
}
