package com.example.main.enums.design;

import com.example.main.models.ANSI;

import java.io.Serializable;

public enum TileType implements Serializable {
    Earth(true, ANSI.YELLOW + " " + ANSI.RESET),
    Grass(true, ANSI.GREEN + "^" + ANSI.RESET),
    Water(false, ANSI.BLUE + "~" + ANSI.RESET),
    Shoveled(true, ANSI.YELLOW + "~" + ANSI.RESET),
    Planted(true, ANSI.GREEN + "~" + ANSI.RESET),
    Branch(true, ANSI.BLUE + "~" + ANSI.RESET),
    CopperStone(false, ANSI.BLACK + "#" + ANSI.RESET),
    IronStone(false, ANSI.BLACK + "#" + ANSI.RESET),
    GoldStone(false, ANSI.BLACK + "#" + ANSI.RESET),
    IridiumStone(false, ANSI.BLACK + "#" + ANSI.RESET),
    JewelStone(false, ANSI.BLACK + "#" + ANSI.RESET),
    Stone(false, ANSI.BLACK + "#" + ANSI.RESET),
    Wall(false, ANSI.RED + "/" + ANSI.RESET),
    Door(true, ANSI.YELLOW + "%" + ANSI.RESET),
    House(true, ANSI.YELLOW + "@" + ANSI.RESET),
    GreenHouse(true, ANSI.GREEN + "G" + ANSI.RESET),
    BrokenGreenHouse(false, ANSI.GREEN + "B" + ANSI.RESET),
    Quarry(true, ANSI.BLACK + "Q" + ANSI.RESET),
    Tree(false, ANSI.GREEN + "T" + ANSI.RESET),
    Bush(false, ANSI.GREEN + "B" + ANSI.RESET),
    Shop(true, ANSI.BLUE + "$" + ANSI.RESET),
    NPCHouse(true, ANSI.CYAN + "H" + ANSI.RESET),
    Housing(true, ANSI.CYAN + "C" + ANSI.RESET);

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
