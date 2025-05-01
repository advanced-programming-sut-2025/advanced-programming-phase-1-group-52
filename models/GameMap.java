package models;

import enums.design.Weather;

import java.util.ArrayList;

public class GameMap {
    private final ArrayList<Tile> tiles;
    private Weather currentWeather;

    public GameMap() {
        this.tiles = new ArrayList<>();
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void lightning(Tile tile1, Tile tile2, Tile tile3) {

    }
}
