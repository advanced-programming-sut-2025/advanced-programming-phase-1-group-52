package com.example.main.models.building;

import com.example.main.models.Player;
import com.example.main.models.Refrigerator;

public class House extends Building {
    private final Player owner;
    private final int cornerX;
    private final int cornerY;
    private final Refrigerator refrigerator;


    public House(Player owner, int cornerX, int cornerY) {
        this.owner = owner;
        this.cornerX = cornerX;
        this.cornerY = cornerY;
        this.refrigerator = new Refrigerator();
    }

    public Refrigerator refrigerator() {
        return refrigerator;
    }
}
