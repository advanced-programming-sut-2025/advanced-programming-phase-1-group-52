package models.building;

import enums.design.NPCType;

public class NPCHouse extends Building {
    private final NPCType owner;
    private final int cornerX;
    private final int cornerY;

    public NPCHouse(NPCType owner) {
        this.owner = owner;
        this.cornerX = owner.getHouseCornerX();
        this.cornerY = owner.getHouseCornerY();
    }
}
