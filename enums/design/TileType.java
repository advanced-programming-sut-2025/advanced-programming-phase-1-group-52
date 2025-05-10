package enums.design;

import models.ANSI;

public enum TileType {
    Earth(true, ANSI.YELLOW + " " + ANSI.RESET),
    Grass(true, ANSI.GREEN + "^" + ANSI.RESET),
    Water(false, ANSI.BLUE + "~" + ANSI.RESET),
    Stone(false, ANSI.BLACK + "#" + ANSI.RESET),
    Wall(false, ANSI.RED + "/" + ANSI.RESET),
    Door(true, ANSI.YELLOW + "%" + ANSI.RESET),
    House(true, ANSI.YELLOW + "@" + ANSI.RESET),
    GreenHouse(true, ANSI.GREEN + "G" + ANSI.RESET),
    BrokenGreenHouse(false, ANSI.GREEN + "B" + ANSI.RESET),
    Quarry(true, ANSI.PURPLE + "Q" + ANSI.RESET),
    Tree(false, ANSI.GREEN + "T" + ANSI.RESET),
    Shop(true, ANSI.BLUE + "$" + ANSI.RESET),
    NPCHouse(true, ANSI.CYAN + "H" + ANSI.RESET);

    private final boolean reachable;
    private final String symbol;

    TileType(boolean reachable, String symbol) {
        this.reachable = reachable;
        this.symbol = symbol;
    }

    public boolean isReachable() {
        return this.reachable;
    }

    public String getSymbol() {
        return symbol;
    }
}
