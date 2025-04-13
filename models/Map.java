package models;

import enums.design.Weather;

import java.util.ArrayList;

public class Map {
    private final ArrayList<Tile> tiles;
    private Weather currentWeather;

    public Map() {
        this.tiles = new ArrayList<>();
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void lightning(Tile tile1, Tile tile2, Tile tile3) {

    }
}
