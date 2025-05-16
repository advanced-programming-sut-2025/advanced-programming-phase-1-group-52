package enums.design;

public enum ShopType {
    Blacksmith("Clint", 32, 2),
    JojaMart("Morris", 42, 2),
    PierresGeneralStore("Pierre", 52, 2),
    CarpentersShop("Robin", 32, 12),
    FishShop("Willy", 42, 12),
    MarniesRanch("Marnie", 52, 12),
    TheStardropSaloon("Gus", 32, 22);

    private final String name;
    private final int cornerX;
    private final int cornerY;

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
