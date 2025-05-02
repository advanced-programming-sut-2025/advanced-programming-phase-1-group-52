package models;

import enums.design.FarmThemes;
import enums.design.NPCType;
import enums.design.ShopType;
import enums.design.TileType;
import enums.design.Weather;
import java.util.ArrayList;
import models.building.GreenHouse;
import models.building.House;
import models.building.NPCHouse;
import models.building.Shop;

public class GameMap {
    private final Tile[][] tiles;
    private Weather currentWeather;
    private final ArrayList<Shop> shops;
    private final ArrayList<House> houses;
    private final ArrayList<NPCHouse> npcHouses;
    private final ArrayList<GreenHouse> greenHouses;

    public GameMap(ArrayList<Player> players, ArrayList<FarmThemes> themes) {
        this.tiles = new Tile[90][40];
        this.currentWeather = Weather.Sunny;
        this.shops = new ArrayList<>();
        this.houses = new ArrayList<>();
        this.npcHouses = new ArrayList<>();
        this.greenHouses = new ArrayList<>();

        generateBuilding(players, 0, TileType.House, 1, 6, 1, 6);
        generateBuilding(players, 0, TileType.BrokenGreenHouse, 22, 29, 1, 7);
        this.houses.add(new House(players.get(0), 1, 1));
        this.greenHouses.add(new GreenHouse(players.get(0), 22, 1));

        generateBuilding(players, 1, TileType.House, 83, 88, 1, 6);
        generateBuilding(players, 1, TileType.BrokenGreenHouse, 81, 88, 1, 7);
        this.houses.add(new House(players.get(1), 83, 1));
        this.greenHouses.add(new GreenHouse(players.get(1), 81, 1));

        generateBuilding(players, 2, TileType.House, 1, 6, 33, 38);
        generateBuilding(players, 2, TileType.BrokenGreenHouse, 22, 29, 32, 38);
        this.houses.add(new House(players.get(2), 1, 33));
        this.greenHouses.add(new GreenHouse(players.get(2), 22, 33));

        generateBuilding(players, 3, TileType.House, 83, 88, 33, 38);
        generateBuilding(players, 3, TileType.BrokenGreenHouse, 81, 88, 32, 38);
        this.houses.add(new House(players.get(3), 83, 33));
        this.greenHouses.add(new GreenHouse(players.get(3), 81, 33));

        generateFarm(players, themes);

        for (ShopType shopType : ShopType.values()) {
            generateBuilding(
                    null,
                    0,
                    TileType.Shop,
                    shopType.getCornerX(),
                    shopType.getCornerX() + 4,
                    shopType.getCornerY(),
                    shopType.getCornerY() + 4
            );

            this.shops.add(new Shop(shopType));
        }

        for (NPCType npc : NPCType.values()) {
            generateBuilding(
                    null,
                    0,
                    TileType.NPCHouse,
                    npc.getHouseCornerX(),
                    npc.getHouseCornerX() + 4,
                    npc.getHouseCornerY(),
                    npc.getHouseCornerY() + 4
            );

            this.npcHouses.add(new NPCHouse(npc));
        }
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
        Player owner;
        try {
            owner = players.get(playerIndex);
        }
        catch (NullPointerException e) {
            owner = null;
        }

        for (int i = xStart + 1; i < xEnd; i++) {
            for (int j = yStart + 1; j < yEnd; j++) {
                tiles[i][j] = new Tile(buildingType, owner);
            }
        }

        for (int i = xStart; i < xEnd + 1; i++) {
            tiles[i][yStart] = new Tile(TileType.Wall, owner);
            tiles[i][yEnd] = new Tile(TileType.Wall, owner);
        }

        for (int i = yStart; i < yEnd + 1; i++) {
            tiles[xStart][i] = new Tile(TileType.Wall, owner);
            tiles[xEnd][i] = new Tile(TileType.Wall, owner);
        }

        tiles[xStart + 1][yEnd] = new Tile(TileType.Door, owner);
    }

    private void generateLake(Player owner, int xStart, int xEnd, int yStart, int yEnd) {
        for (int i = xStart; i < xEnd + 1; i++) {
            for (int j = yStart; j < yEnd + 1; j++) {
                tiles[i][j] = new Tile(TileType.Water, owner);
            }
        }
    }

    private void generateFarm(ArrayList<Player> players, ArrayList<FarmThemes> themes) {
        if (themes.get(0).equals(FarmThemes.Neutral)) {
            generateBuilding(players, 0, TileType.Quarry, 1, 9, 14, 18);
            generateLake(players.get(0), 23, 28, 14, 17);
        }
        else if (themes.get(0).equals(FarmThemes.Miner)) {
            generateBuilding(players, 0, TileType.Quarry, 1, 19, 14, 18);
        }
        else if (themes.get(0).equals(FarmThemes.Fisher)) {
            generateLake(players.get(0), 1, 19, 14, 17);
        }

        if (themes.get(1).equals(FarmThemes.Neutral)) {
            generateBuilding(players, 1, TileType.Quarry, 1, 9, 14, 18);
            generateLake(players.get(1), 23, 28, 14, 17);
        }
        else if (themes.get(1).equals(FarmThemes.Miner)) {
            generateBuilding(players, 1, TileType.Quarry, 1, 19, 14, 18);
        }
        else if (themes.get(1).equals(FarmThemes.Fisher)) {
            generateLake(players.get(1), 1, 19, 14, 17);
        }

        if (themes.get(2).equals(FarmThemes.Neutral)) {
            generateBuilding(players, 2, TileType.Quarry, 1, 9, 14, 18);
            generateLake(players.get(2), 23, 28, 14, 17);
        }
        else if (themes.get(2).equals(FarmThemes.Miner)) {
            generateBuilding(players, 2, TileType.Quarry, 1, 19, 14, 18);
        }
        else if (themes.get(2).equals(FarmThemes.Fisher)) {
            generateLake(players.get(2), 1, 19, 34, 38);
        }

        if (themes.get(3).equals(FarmThemes.Neutral)) {
            generateBuilding(players, 3, TileType.Quarry, 1, 9, 14, 18);
            generateLake(players.get(3), 23, 28, 14, 17);
        }
        else if (themes.get(3).equals(FarmThemes.Miner)) {
            generateBuilding(players, 3, TileType.Quarry, 1, 19, 14, 18);
        }
        else if (themes.get(3).equals(FarmThemes.Fisher)) {
            generateLake(players.get(3), 1, 19, 34, 38);
        }
    }
}
