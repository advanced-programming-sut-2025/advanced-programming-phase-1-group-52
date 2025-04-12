package enums.items;

public enum AnimalProducts {
    ChickenEgg(50),
    BigChickenEgg(95),
    DuckEgg(95),
    DuckFeather(250),
    RabbitWool(340),
    RabbitLeg(565),
    DinosaurEgg(350),
    NormalCowMilk(125),
    LargeCowMilk(190),
    NormalGoatMilk(225),
    LargeGoatMilk(345),
    SheepWool(340),
    Truffle(625);

    private int price;

    AnimalProducts(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }
}
