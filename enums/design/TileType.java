package enums.design;

public enum TileType {
    Earth(true),
    Grass(true),
    Water(false),
    Soil(true),
    Stone(false),
    Wall(false),
    Door(true),
    House(true),
    GreenHouse(true),
    BrokenGreenHouse(false),
    Quarry(true),
    Tree(false),
    Shop(true),
    NPCHouse(true),;

    private final boolean reachable;

    TileType(boolean reachable) {
        this.reachable = reachable;
    }
}
