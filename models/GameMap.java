package models;

import enums.design.FarmThemes;
import enums.design.TileType;
import enums.design.Weather;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {
    private final Tile[][] tiles;
    private Weather currentWeather;

    public GameMap(ArrayList<Player> players, ArrayList<FarmThemes> themes) {
        this.tiles = new Tile[90][40];
        this.currentWeather = Weather.Sunny;

        generateBuilding(players, 0, TileType.House, 1, 6, 1, 6);
        generateBuilding(players, 0, TileType.BrokenGreenHouse, 22, 29, 1, 7);

        generateBuilding(players, 1, TileType.House, 83, 88, 1, 6);
        generateBuilding(players, 1, TileType.BrokenGreenHouse, 81, 88, 1, 7);

        generateBuilding(players, 2, TileType.House, 1, 6, 33, 38);
        generateBuilding(players, 2, TileType.BrokenGreenHouse, 22, 29, 32, 38);

        generateBuilding(players, 3, TileType.House, 83, 88, 33, 38);
        generateBuilding(players, 3, TileType.BrokenGreenHouse, 81, 88, 32, 38);


    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void lightning(Tile tile1, Tile tile2, Tile tile3) {

    }

    private void generateBuilding(
            ArrayList<Player> players, int playerIndex, TileType buildingType,
            int xStart, int xEnd, int yStart, int yEnd
    ) {
        for (int i = xStart + 1; i < xEnd; i++) {
            for (int j = yStart + 1; j < yEnd; j++) {
                tiles[i][j] = new Tile(buildingType, players.get(playerIndex));
            }
        }

        for (int i = xStart; i < xEnd + 1; i++) {
            tiles[i][yStart] = new Tile(TileType.Wall, players.get(playerIndex));
            tiles[i][yEnd] = new Tile(TileType.Wall, players.get(playerIndex));
        }
        for (int i = yStart; i < yEnd + 1; i++) {
            tiles[xStart][i] = new Tile(TileType.Wall, players.get(playerIndex));
            tiles[xEnd][i] = new Tile(TileType.Wall, players.get(playerIndex));
        }

        tiles[xStart + 1][yEnd] = new Tile(TileType.Door, players.get(playerIndex));
    }

    private void generateFarm(ArrayList<Player> players, ArrayList<FarmThemes> themes, int themeIndex) {
        switch (themeIndex) {
            case 0:
                if (themes.get(0).equals(FarmThemes.Neutral)) {

                }
                else if (themes.get(0).equals(FarmThemes.Miner)) {

                }
                else if (themes.get(0).equals(FarmThemes.Fisher)) {

                }
                break;
            case 1:
                if (themes.get(1).equals(FarmThemes.Neutral)) {

                }
                else if (themes.get(1).equals(FarmThemes.Miner)) {

                }
                else if (themes.get(1).equals(FarmThemes.Fisher)) {

                }
                break;
            case 2:
                if (themes.get(2).equals(FarmThemes.Neutral)) {

                }
                else if (themes.get(2).equals(FarmThemes.Miner)) {

                }
                else if (themes.get(2).equals(FarmThemes.Fisher)) {

                }
                break;
            case 3:
                if (themes.get(3).equals(FarmThemes.Neutral)) {

                }
                else if (themes.get(3).equals(FarmThemes.Miner)) {

                }
                else if (themes.get(3).equals(FarmThemes.Fisher)) {

                }
                break;
        }
    }
}
