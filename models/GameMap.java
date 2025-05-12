package models;

import enums.design.FarmThemes;
import enums.design.NPCType;
import enums.design.ShopType;
import enums.design.TileType;
import enums.design.Weather;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
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

    public GameMap(ArrayList<User> users, ArrayList<FarmThemes> themes) {
        this.tiles = new Tile[90][60];
        this.currentWeather = Weather.Sunny;
        this.shops = new ArrayList<>();
        this.houses = new ArrayList<>();
        this.npcHouses = new ArrayList<>();
        this.greenHouses = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        for (User user : users) {
            players.add(user.getPlayer());
        }

        generateBuilding(players, 0, TileType.House, 1, 6, 1, 6);
        generateBuilding(players, 0, TileType.BrokenGreenHouse, 22, 29, 1, 7);
        this.houses.add(new House(players.get(0), 1, 1));
        this.greenHouses.add(new GreenHouse(players.get(0), 22, 1));

        generateBuilding(players, 1, TileType.House, 83, 88, 1, 6);
        generateBuilding(players, 1, TileType.BrokenGreenHouse, 61, 68, 1, 7);
        this.houses.add(new House(players.get(1), 83, 1));
        this.greenHouses.add(new GreenHouse(players.get(1), 61, 1));

        generateBuilding(players, 2, TileType.House, 1, 6, 33, 38);
        generateBuilding(players, 2, TileType.BrokenGreenHouse, 22, 29, 32, 38);
        this.houses.add(new House(players.get(2), 1, 33));
        this.greenHouses.add(new GreenHouse(players.get(2), 22, 32));

        generateBuilding(players, 3, TileType.House, 63, 68, 33, 38);
        generateBuilding(players, 3, TileType.BrokenGreenHouse, 61, 68, 32, 38);
        this.houses.add(new House(players.get(3), 63, 33));
        this.greenHouses.add(new GreenHouse(players.get(3), 61, 32));

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

        Random rand = new Random();
        int grassAmount = rand.nextInt(3);

        for (int i = 0; i < grassAmount + 2; i++) {
            int grassX = rand.nextInt(20) + 4;
            int grassY = rand.nextInt(12) + 8;
            int grassHeight = rand.nextInt(4) + 1;
            
            for (int j = grassY - grassHeight / 2; j < grassY + grassHeight / 2; j++) {
                for (int k = grassX - rand.nextInt(2) - 1; k < grassX + rand.nextInt(2); k++) {
                    tiles[k][j] = new Tile(k, j, TileType.Grass, players.get(0));
                } 
            }
        }

        grassAmount = rand.nextInt(3);
        for (int i = 0; i < grassAmount + 2; i++) {
            int grassX = rand.nextInt(20) + 64;
            int grassY = rand.nextInt(12) + 8;
            int grassHeight = rand.nextInt(4) + 1;

            for (int j = grassY - grassHeight / 2; j < grassY + grassHeight / 2; j++) {
                for (int k = grassX - rand.nextInt(2) - 1; k < grassX + rand.nextInt(2); k++) {
                    tiles[k][j] = new Tile(k, j, TileType.Grass, players.get(1));
                }
            }
        }

        grassAmount = rand.nextInt(3);
        for (int i = 0; i < grassAmount + 2; i++) {
            int grassX = rand.nextInt(20) + 4;
            int grassY = rand.nextInt(12) + 38;
            int grassHeight = rand.nextInt(4) + 1;

            for (int j = grassY - grassHeight / 2; j < grassY + grassHeight / 2; j++) {
                for (int k = grassX - rand.nextInt(2) - 1; k < grassX + rand.nextInt(2); k++) {
                    tiles[k][j] = new Tile(k, j, TileType.Grass, players.get(2));
                }
            }
        }

        grassAmount = rand.nextInt(3);
        for (int i = 0; i < grassAmount + 2; i++) {
            int grassX = rand.nextInt(20) + 64;
            int grassY = rand.nextInt(12) + 38;
            int grassHeight = rand.nextInt(4) + 1;

            for (int j = grassY - grassHeight / 2; j < grassY + grassHeight / 2; j++) {
                for (int k = grassX - rand.nextInt(2) - 1; k < grassX + rand.nextInt(2); k++) {
                    tiles[k][j] = new Tile(k, j, TileType.Grass, players.get(3));
                }
            }
        }

        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(10);
                    if (tileProb < 4) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(0));
                    }
                    else if (tileProb < 7) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(0));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Tree, players.get(0));
                    }
                }
            }
        }
        
        for (int i = 60; i < 90; i++) {
            for (int j = 0; j < 30; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(10);
                    if (tileProb < 4) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(1));
                    }
                    else if (tileProb < 7) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(1));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Tree, players.get(1));
                    }
                }
            }
        }

        for (int i = 0; i < 30; i++) {
            for (int j = 30; j < 60; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(10);
                    if (tileProb < 4) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(2));
                    }
                    else if (tileProb < 7) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(2));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Tree, players.get(2));
                    }
                }
            }
        }

        for (int i = 60; i < 90; i++) {
            for (int j = 30; j < 60; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(10);
                    if (tileProb < 4) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(3));
                    }
                    else if (tileProb < 7) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(3));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Tree, players.get(3));
                    }
                }
            }
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
                tiles[i][j] = new Tile(i, j, buildingType, owner);
            }
        }

        for (int i = xStart; i < xEnd + 1; i++) {
            tiles[i][yStart] = new Tile(i, yStart, TileType.Wall, owner);
            tiles[i][yEnd] = new Tile(i, yEnd, TileType.Wall, owner);
        }

        for (int i = yStart; i < yEnd + 1; i++) {
            tiles[xStart][i] = new Tile(xStart, i, TileType.Wall, owner);
            tiles[xEnd][i] = new Tile(xEnd, i, TileType.Wall, owner);
        }

        tiles[xStart + 1][yEnd] = new Tile(xStart + 1, yEnd, TileType.Door, owner);
    }

    private void generateLake(Player owner, int xStart, int xEnd, int yStart, int yEnd) {
        for (int i = xStart; i < xEnd + 1; i++) {
            for (int j = yStart; j < yEnd + 1; j++) {
                tiles[i][j] = new Tile(i, j, TileType.Water, owner);
            }
        }
    }

    private void generateFarm(ArrayList<Player> players, ArrayList<FarmThemes> themes) {
        switch (themes.get(0)) {
            case Neutral -> {
                generateBuilding(players, 0, TileType.Quarry, 1, 9, 24, 28);
                generateLake(players.get(0), 23, 28, 24, 27);
            }
            case Miner -> generateBuilding(players, 0, TileType.Quarry, 1, 19, 24, 28);
            case Fisher -> generateLake(players.get(0), 1, 19, 24, 27);
            default -> {
            }
        }

        switch (themes.get(1)) {
            case Neutral -> {
                generateBuilding(players, 1, TileType.Quarry, 61, 69, 24, 28);
                generateLake(players.get(1), 83, 88, 24, 27);
            }
            case Miner -> generateBuilding(players, 1, TileType.Quarry, 61, 79, 24, 28);
            case Fisher -> generateLake(players.get(1), 61, 79, 24, 27);
            default -> {
            }
        }

        switch (themes.get(2)) {
            case Neutral -> {
                generateBuilding(players, 2, TileType.Quarry, 1, 9, 54, 58);
                generateLake(players.get(2), 23, 28, 54, 57);
            }
            case Miner -> generateBuilding(players, 2, TileType.Quarry, 1, 19, 54, 58);
            case Fisher -> generateLake(players.get(2), 1, 19, 54, 58);
            default -> {
            }
        }

        switch (themes.get(3)) {
            case Neutral -> {
                generateBuilding(players, 3, TileType.Quarry, 61, 69, 54, 58);
                generateLake(players.get(3), 83, 88, 54, 57);
            }
            case Miner -> generateBuilding(players, 3, TileType.Quarry, 61, 79, 54, 58);
            case Fisher -> generateLake(players.get(3), 61, 79, 54, 58);
            default -> {
            }
        }
    }
    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }
    public ArrayList<Tile> findWalkPath(int startX, int startY, int destX, int destY) {
        boolean[][] visited = new boolean[tiles.length][tiles[0].length];
        Tile[][] previous = new Tile[tiles.length][tiles[0].length];
        Queue<Tile> queue = new LinkedList<>();

        queue.add(tiles[startX][startY]);
        visited[startX][startY] = true;
        
        int[] nearByX = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] nearByY = {-1, 0, 1, -1, 1, -1, 0, 1};

        while (!queue.isEmpty()) {
            Tile currentTile = queue.poll();
            if (currentTile.getX() == destX && currentTile.getY() == destY) {
                ArrayList<Tile> path = new ArrayList<>();
                Tile current = currentTile;
                while(current != null) {
                    path.addFirst(current);
                    current = previous[current.getX()][current.getY()];
                }

                return path;
            }

            for (int i = 0; i < 8; i++) {
                int nextX = currentTile.getX() + nearByX[i];
                int nextY = currentTile.getY() + nearByY[i];

                if (inBounds(nextX, nextY) && !visited[nextX][nextY] && isWalkable(nextX, nextY)) {
                    queue.add(tiles[nextX][nextY]);
                    visited[nextX][nextY] = true;
                    previous[nextX][nextY] = currentTile;
                }
            }
        }

        return null;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length;
    }

    private boolean isWalkable(int x, int y) {
        Tile tile = tiles[x][y];
        return tile.getType().isReachable();
    }

    public String showMap(int x, int y, int size) {
        StringBuilder mapString = new StringBuilder();
        for (int i = y; i <= y + size; i++) {
            for (int j = x; j <= x + size; j++) {
                mapString.append(tiles[j][i].getType().getSymbol());
                if (j == 29 || j == 59) mapString.append('|');
            }

            mapString.append('\n');
            if (i == 29) {
                for (int j = y; j <= y + size; j++) {
                    mapString.append('-');
                }

                mapString.append('\n');
            }
        }
        
        return mapString.toString();
    }
}
