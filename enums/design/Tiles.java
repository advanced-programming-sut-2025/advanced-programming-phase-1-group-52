package enums.design;

public enum Tiles {
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

    Tiles(boolean reachable) {
        this.reachable = reachable;
    }
}
