package com.example.main.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.main.enums.design.FarmThemes;
import com.example.main.enums.design.NPCType;
import com.example.main.enums.design.ShopType;
import com.example.main.enums.design.TileType;
import com.example.main.enums.design.Weather;
import com.example.main.enums.items.CropType;
import com.example.main.enums.items.ForagingCropType;
import com.example.main.enums.items.ForagingSeedType;
import com.example.main.enums.items.ItemType;
import com.example.main.enums.items.MineralType;
import com.example.main.enums.items.TreeType;
import com.example.main.models.building.GreenHouse;
import com.example.main.models.building.House;
import com.example.main.models.building.NPCHouse;
import com.example.main.models.building.Shop;
import com.example.main.models.item.Crop;
import com.example.main.models.item.Fruit;
import com.example.main.models.item.Mineral;
import com.example.main.models.item.Seed;
import com.example.main.models.item.ItemFactory;

public class GameMap {
    private final Tile[][] tiles;
    private Weather currentWeather;
    private final ArrayList<Shop> shops;
    private final ArrayList<House> houses;
    private final ArrayList<NPCHouse> npcHouses;
    private final ArrayList<GreenHouse> greenHouses;
    private final ArrayList<Player> players;
    private final ArrayList<FarmThemes> themes;

    public GameMap(ArrayList<User> users, ArrayList<FarmThemes> themes) {
        this.tiles = new Tile[90][60];
        this.currentWeather = Weather.Sunny;
        this.shops = new ArrayList<>();
        this.houses = new ArrayList<>();
        this.npcHouses = new ArrayList<>();
        this.greenHouses = new ArrayList<>();
        this.themes = themes;
        this.players = new ArrayList<>();
        for (User user : users) {
            this.players.add(user.getPlayer());
        }

        generateBuilding(this.players, 0, TileType.House, 1, 8, 1, 8);
        generateBuilding(this.players, 0, TileType.BrokenGreenHouse, 22, 29, 1, 7);
        this.houses.add(new House(this.players.get(0), 1, 1));
        this.greenHouses.add(new GreenHouse(this.players.get(0), 22, 1));

        generateBuilding(this.players, 1, TileType.House, 81, 88, 1, 8);
        generateBuilding(this.players, 1, TileType.BrokenGreenHouse, 61, 68, 1, 7);
        this.houses.add(new House(this.players.get(1), 83, 1));
        this.greenHouses.add(new GreenHouse(this.players.get(1), 61, 1));

        generateBuilding(this.players, 2, TileType.House, 1, 8, 31, 38);
        generateBuilding(this.players, 2, TileType.BrokenGreenHouse, 22, 29, 32, 38);
        this.houses.add(new House(this.players.get(2), 1, 33));
        this.greenHouses.add(new GreenHouse(this.players.get(2), 22, 32));

        generateBuilding(this.players, 3, TileType.House, 81, 88, 31, 38);
        generateBuilding(this.players, 3, TileType.BrokenGreenHouse, 61, 68, 32, 38);
        this.houses.add(new House(this.players.get(3), 83, 33));
        this.greenHouses.add(new GreenHouse(this.players.get(3), 61, 32));

        generateFarm(this.players, themes);
        generateBushBorders(this.players);

        for (NPCType npc : NPCType.values()) {
            generateBuilding(
                null,
                4,
                TileType.NPCHouse,
                npc.getHouseCornerX(),
                npc.getHouseCornerX() + 4,
                npc.getHouseCornerY(),
                npc.getHouseCornerY() + 6
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

        for (int i = 1; i < 29; i++) {
            for (int j = 1; j < 29; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(20);
                    if (tileProb < 12) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(0));
                    }
                    else if (tileProb < 13) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(0));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(0));
                    }
                }
            }
        }
        
        for (int i = 61; i < 89; i++) {
            for (int j = 1; j < 29; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(20);
                    if (tileProb < 12) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(1));
                    }
                    else if (tileProb < 13) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(1));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(1));
                    }
                }
            }
        }

        for (int i = 1; i < 29; i++) {
            for (int j = 31; j < 59; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(20);
                    if (tileProb < 12) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(2));
                    }
                    else if (tileProb < 13) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(2));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(2));
                    }
                }
            }
        }

        for (int i = 61; i < 89; i++) {
            for (int j = 31; j < 59; j++) {
                if (tiles[i][j] == null) {
                    int tileProb = rand.nextInt(20);
                    if (tileProb < 12) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(3));
                    }
                    else if (tileProb < 13) {
                        tiles[i][j] = new Tile(i, j, TileType.Stone, players.get(3));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(3));
                    }
                }
            }
        }

        placeRandomForageables(this.players.get(0), 1, 1, 29, 29);
        placeRandomWildTrees(this.players.get(0), 1, 1, 29, 29);
        placeRandomForageables(this.players.get(1), 61, 1, 89, 29);
        placeRandomWildTrees(this.players.get(1), 61, 1, 89, 29);
        placeRandomForageables(this.players.get(2), 1, 31, 29, 59);
        placeRandomWildTrees(this.players.get(2), 1, 31, 29, 59);
        placeRandomForageables(this.players.get(3), 61, 31, 89, 59);
        placeRandomWildTrees(this.players.get(3), 61, 31, 89, 59);

        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                if (tiles[i][j] == null) {
                    if (i < 30 && j < 30) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(0));
                    }
                    else if (i < 30 && j >= 30) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(2));
                    }
                    else if (i >= 60 && j < 30) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(1));
                    }
                    else if (i >= 60 && j >= 30) {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, players.get(3));
                    }
                    else {
                        tiles[i][j] = new Tile(i, j, TileType.Earth, null);
                    }
                }
            }
        }

        for (ShopType shopType : ShopType.values()) {
            generateBuilding(
                null,
                4,
                TileType.Shop,
                shopType.getCornerX(),
                shopType.getCornerX() + 7,
                shopType.getCornerY(),
                shopType.getCornerY() + 7
            );

            Shop shop = new Shop(shopType);
            this.shops.add(shop);

            for (int i = shopType.getCornerX(); i < shopType.getCornerX() + 7; i++) {
                for (int j = shopType.getCornerY(); j < shopType.getCornerY() + 7; j++) {
                    tiles[i][j].setShop(shop);
                }
            }
        }
    }

    public void setCurrentWeather(Weather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public void lightning(int playerIndex) {
        Random rand = new Random();
        switch (playerIndex) {
            case 0 -> {
                for (int i = 0; i < 3; i++) {
                    int x = rand.nextInt(30);
                    int y = rand.nextInt(30);

                    Tile target = tiles[x][y];
                    if (target.getType().equals(TileType.Grass) || target.getType().equals(TileType.Planted) || target.getType().equals(TileType.Tree) || target.getType().equals(TileType.Shoveled)) {
                        target.setPlant(null);
                        target.setTree(null);
                        target.setSeed(null);
                        target.setType(TileType.Earth);
                    }
                }
            }
            case 1 -> {
                for (int i = 0; i < 3; i++) {
                    int x = rand.nextInt(30) + 60;
                    int y = rand.nextInt(30);

                    Tile target = tiles[x][y];
                    if (target.getType().equals(TileType.Grass) || target.getType().equals(TileType.Planted) || target.getType().equals(TileType.Tree) || target.getType().equals(TileType.Shoveled)) {
                        target.setPlant(null);
                        target.setTree(null);
                        target.setSeed(null);
                        target.setType(TileType.Earth);
                    }
                }
            }
            case 2 -> {
                for (int i = 0; i < 3; i++) {
                    int x = rand.nextInt(30);
                    int y = rand.nextInt(30) + 30;

                    Tile target = tiles[x][y];
                    if (target.getType().equals(TileType.Grass) || target.getType().equals(TileType.Planted) || target.getType().equals(TileType.Tree) || target.getType().equals(TileType.Shoveled)) {
                        target.setPlant(null);
                        target.setTree(null);
                        target.setSeed(null);
                        target.setType(TileType.Earth);
                    }
                }
            }
            case 3 -> {
                for (int i = 0; i < 3; i++) {
                    int x = rand.nextInt(30) + 60;
                    int y = rand.nextInt(30) + 30;

                    Tile target = tiles[x][y];
                    if (target.getType().equals(TileType.Grass) || target.getType().equals(TileType.Planted) || target.getType().equals(TileType.Tree) || target.getType().equals(TileType.Shoveled)) {
                        target.setPlant(null);
                        target.setTree(null);
                        target.setSeed(null);
                        target.setType(TileType.Earth);
                    }
                }
            }
            default -> {
            }
        }
    }

    public Result cheatLightning(int x, int y) {
        Tile target = tiles[x][y];
        if (target.getType().equals(TileType.Grass) || target.getType().equals(TileType.Planted) || target.getType().equals(TileType.Tree) || target.getType().equals(TileType.Shoveled)) {
            target.setPlant(null);
            target.setTree(null);
            target.setSeed(null);
            target.setType(TileType.Earth);
            return new Result(true, "Lightning hit!");
        }
        else {
            return new Result(false, "Lightning has no effect on that tile!");
        }
    }

    public void generateBuilding(
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

        if (buildingType == TileType.Quarry) {
            Random rand = new Random();
            List<MineralType> mineableItems = Arrays.asList(
                MineralType.Copper_Ore, MineralType.Iron_Ore, MineralType.Gold_Ore, MineralType.Iridium_Ore,
                MineralType.Quartz, MineralType.Earth_Crystal, MineralType.Frozen_Tear, MineralType.Fire_Quartz,
                MineralType.Amethyst, MineralType.Topaz
            );

            for (int i = xStart + 1; i < xEnd; i++) {
                for (int j = yStart + 1; j < yEnd; j++) {
                    MineralType chosenMineral = mineableItems.get(rand.nextInt(mineableItems.size()));
                    TileType stoneType;

                    switch (chosenMineral) {
                        case Copper_Ore:
                            stoneType = TileType.CopperStone;
                            break;
                        case Iron_Ore:
                            stoneType = TileType.IronStone;
                            break;
                        case Gold_Ore:
                            stoneType = TileType.GoldStone;
                            break;
                        case Iridium_Ore:
                            stoneType = TileType.IridiumStone;
                            break;
                        case Quartz, Earth_Crystal, Frozen_Tear, Fire_Quartz, Amethyst, Topaz:
                            stoneType = TileType.JewelStone;
                            break;
                        default:
                            stoneType = TileType.Stone;
                            break;
                    }

                    tiles[i][j] = new Tile(i, j, stoneType, owner);
                    tiles[i][j].setItem(ItemFactory.createItemOrThrow(chosenMineral.getName(), 1));
                }
            }
        }
        else {
            for (int i = xStart + 1; i < xEnd; i++) {
                for (int j = yStart + 1; j < yEnd; j++) {
                    tiles[i][j] = new Tile(i, j, buildingType, owner);
                }
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

    private void generateBushBorders(ArrayList<Player> players) {
        generateBushBorderForArea(players.get(0), 0, 29, 0, 29);
        generateBushBorderForArea(players.get(1), 60, 89, 0, 29);
        generateBushBorderForArea(players.get(2), 0, 29, 30, 59);
        generateBushBorderForArea(players.get(3), 60, 89, 30, 59);
    }

    private void generateBushBorderForArea(Player owner, int xStart, int xEnd, int yStart, int yEnd) {
        for (int x = xStart; x <= xEnd; x++) {
            if (x < xStart + 13 || x > xStart + 16) {
                if (tiles[x][yStart] == null || tiles[x][yStart].getType() == TileType.Earth) {
                    tiles[x][yStart] = new Tile(x, yStart, TileType.Bush, owner);
                }
            }
        }

        for (int x = xStart; x <= xEnd; x++) {
            if (x < xStart + 13 || x > xStart + 16) {
                if (tiles[x][yEnd] == null || tiles[x][yEnd].getType() == TileType.Earth) {
                    tiles[x][yEnd] = new Tile(x, yEnd, TileType.Bush, owner);
                }
            }
        }

        for (int y = yStart; y <= yEnd; y++) {
            if (y < yStart + 13 || y > yStart + 16) {
                if (tiles[xStart][y] == null || tiles[xStart][y].getType() == TileType.Earth) {
                    tiles[xStart][y] = new Tile(xStart, y, TileType.Bush, owner);
                }
            }
        }

        for (int y = yStart; y <= yEnd; y++) {
            if (y < yStart + 13 || y > yStart + 16) {
                if (tiles[xEnd][y] == null || tiles[xEnd][y].getType() == TileType.Earth) {
                    tiles[xEnd][y] = new Tile(xEnd, y, TileType.Bush, owner);
                }
            }
        }
    }

    public Tile[][] getTiles() {
        return this.tiles;
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

    public boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < tiles.length && y < tiles[0].length;
    }

    private boolean isWalkable(int x, int y) {
        App app = App.getInstance();
        Game game = app.getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        Tile tile = tiles[x][y];
        return tile.getType().isReachable() && ((tile.getOwner() == null ||  tile.getOwner().equals(currentPlayer)) ||
            (currentPlayer.getSpouse() != null && tile.getOwner().equals(currentPlayer.getSpouse())));
    }

    public String showMap(int x, int y, int size) {
        StringBuilder mapString = new StringBuilder();
        for (int i = y; i <= y + size; i++) {
            for (int j = x; j <= x + size; j++) {
                boolean player = false;
                for (User user : App.getInstance().getCurrentGame().getPlayers()) {
                    if (user.getPlayer().currentX() == i && user.getPlayer().currentY() == j) {
                        mapString.append(App.getInstance().getCurrentGame().getPlayers().indexOf(user) + 1);
                        player = true;
                    }
                }

                if (!player) {
                    mapString.append(tiles[j][i].getType().getSymbol());
                    if (j == 29 || j == 59) mapString.append('|');
                }
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

    public void generateRandomForagingSeeds() {
        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                Random rand = new Random();
                int prob = rand.nextInt(10);
                if (tiles[i][j].getType().equals(TileType.Shoveled) && prob == 0) {
                    Game game = App.getInstance().getCurrentGame();
                    List<ForagingSeedType> seeds = Arrays.stream(ForagingSeedType.values())
                        .filter(seed -> seed.isForaging() && seed.getSeason().equals(game.getDate().getCurrentSeason()))
                        .collect(Collectors.toList());
                    ForagingSeedType seedType = seeds.get(rand.nextInt(seeds.size()));

                    tiles[i][j].setSeed(new Seed(seedType, 1));
                }
            }
        }
    }

    public void generatePlantsFromSeeds() {
        for (int i = 0; i < 90; i++) {
            for (int j = 0; j < 60; j++) {
                Tile targetTile = tiles[i][j];
                if (targetTile.getSeed() != null) {
                    ItemType plantable = targetTile.getSeed().getForagingSeedType().getPlantType();

                    // --- THIS IS THE FIX ---
                    // Only proceed if the seed actually produces a plant
                    if (plantable != null) {
                        if (plantable instanceof TreeType treeType) {
                            targetTile.setType(TileType.Tree);
                            targetTile.setPlant(new Fruit(treeType.getProduct(), 1));
                        } else if (plantable instanceof CropType) {
                            targetTile.setType(TileType.Planted);
                            targetTile.setPlant(new Crop(plantable, 1));
                        }
                    }
                    // Always remove the seed after attempting to plant
                    targetTile.setSeed(null);
                }
            }
        }
    }

    public boolean isPlantThere(int xStart, int xEnd, int yStart, int yEnd) {
        for (int i = xStart; i <= xEnd; i++) {
            for (int j = yStart; j <= yEnd; j++) {
                if (tiles[i][j].getPlant() != null && tiles[i][j].getTree() == null) return false;
            }
        }

        return true;
    }

    private void placeRandomWildTrees(Player owner, int xStart, int yStart, int xEnd, int yEnd) {
        Random rand = new Random();
        int treeCount = 7 + rand.nextInt(4);

        List<TreeType> wildTrees = Arrays.asList(
            TreeType.Oak, TreeType.Maple, TreeType.Pine, TreeType.Mahogany
        );

        ArrayList<Tile> validTiles = new ArrayList<>();
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                if (tiles[i][j] == null || tiles[i][j].getType() == TileType.Earth) {
                    validTiles.add(new Tile(i, j, TileType.Earth, owner));
                }
            }
        }

        java.util.Collections.shuffle(validTiles);

        for (int i = 0; i < treeCount && !validTiles.isEmpty(); i++) {
            Tile targetTile = validTiles.remove(0);
            int x = targetTile.getX();
            int y = targetTile.getY();

            TreeType treeType = wildTrees.get(rand.nextInt(wildTrees.size()));

            targetTile.setType(TileType.Tree);
            targetTile.setTree(new Tree(treeType));

            // Remove this line, as Tree itself is now Growable
            // Fruit treeGrowthTracker = new Fruit(treeType.getProduct(), 1);
            // treeGrowthTracker.setCurrentStage(1);
            // targetTile.setPlant(treeGrowthTracker);
            targetTile.setPlant(new Tree(treeType)); // Set the Tree as the Growable on the tile

            this.tiles[x][y] = targetTile;
        }
    }

    private void placeRandomForageables(Player owner, int xStart, int yStart, int xEnd, int yEnd) {
        Random rand = new Random();
        int forageableCount = 10 + rand.nextInt(6);

        ArrayList<Tile> validTiles = new ArrayList<>();
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                if (tiles[i][j] == null || tiles[i][j].getType() == TileType.Earth) {
                    validTiles.add(new Tile(i, j, TileType.Earth, owner));
                }
            }
        }

        java.util.Collections.shuffle(validTiles);

        for (int i = 0; i < forageableCount && !validTiles.isEmpty(); i++) {
            Tile targetTile = validTiles.remove(0);
            int x = targetTile.getX();
            int y = targetTile.getY();

            double choice = rand.nextDouble();

            if (choice < 0.60) {
                List<ForagingCropType> crops = Arrays.asList(ForagingCropType.values());
                ForagingCropType forageableType = crops.get(rand.nextInt(crops.size()));

                // Forageable items are simply placed on the tile as an Item, not a growing plant
                targetTile.setType(TileType.Earth); // Or keep existing type if appropriate, e.g., Grass
                targetTile.setItem(ItemFactory.createItemOrThrow(forageableType.getName(), 1));

            } else {
                List<ForagingSeedType> seeds = Arrays.stream(ForagingSeedType.values())
                    .filter(ForagingSeedType::isForaging)
                    .collect(Collectors.toList());
                ForagingSeedType seedType = seeds.get(rand.nextInt(seeds.size()));

                targetTile.setType(TileType.Shoveled);
                targetTile.setSeed(new Seed(seedType, 1));
            }
            this.tiles[x][y] = targetTile;
        }
    }

    // In main/models/GameMap.java

    public void regenerateQuarries() {
        if (this.players == null || this.themes == null || this.players.isEmpty() || this.themes.isEmpty()) {
            return;
        }
        // This logic is moved from the old generateFarm method
        // It regenerates the quarry for each player based on their theme
        if (themes.size() > 0) {
            switch (themes.get(0)) {
                case Neutral -> generateBuilding(this.players, 0, TileType.Quarry, 1, 9, 24, 28);
                case Miner -> generateBuilding(this.players, 0, TileType.Quarry, 1, 19, 24, 28);
            }
        }
        if (themes.size() > 1) {
            switch (themes.get(1)) {
                case Neutral -> generateBuilding(this.players, 1, TileType.Quarry, 61, 69, 24, 28);
                case Miner -> generateBuilding(this.players, 1, TileType.Quarry, 61, 79, 24, 28);
            }
        }
        if (themes.size() > 2) {
            switch (themes.get(2)) {
                case Neutral -> generateBuilding(this.players, 2, TileType.Quarry, 1, 9, 54, 58);
                case Miner -> generateBuilding(this.players, 2, TileType.Quarry, 1, 19, 54, 58);
            }
        }
        if (themes.size() > 3) {
            switch (themes.get(3)) {
                case Neutral -> generateBuilding(this.players, 3, TileType.Quarry, 61, 69, 54, 58);
                case Miner -> generateBuilding(this.players, 3, TileType.Quarry, 61, 79, 54, 58);
            }
        }
    }
}
