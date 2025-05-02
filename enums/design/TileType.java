package enums.design;

public enum TileType {
    Grass(true),
    Water(false),
    Stone(false),
    Wall(false),
    Door(true),
    House(true),
    GreenHouse(true),
    BrokenGreenHouse(false),
    Quarry(true),
    Tree(false);

    private final boolean reachable;

    TileType(boolean reachable) {
        this.reachable = reachable;
    }
}
