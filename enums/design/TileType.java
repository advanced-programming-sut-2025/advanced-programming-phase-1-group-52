package enums.design;

public enum TileType {
    Earth(true),
    Shoveled(true),
    Grass(true),
    Water(false),

    CopperStone(true),
    IronStone(true),
    GoldStone(true),
    IridiumStone(true),
    JewelStone(true),
    NormalStone(true),

    Branch(true),
    Wall(false),
    Door(true),
    House(true),
    GreenHouse(true),
    BrokenGreenHouse(false),
    Quarry(true),
    Tree(false),
    Shop(true),
    NPCHouse(true);

    private final boolean reachable;

    TileType(boolean reachable) {
        this.reachable = reachable;
    }
}
