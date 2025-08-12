package com.example.main.events;

import com.example.main.models.GameMap;
import com.example.main.models.GameMapSnapshot;


public class GameMapSyncEvent implements AppEvent {
    private final GameMapSnapshot gameMapSnapshot;

    public GameMapSyncEvent(GameMapSnapshot snapshot) {
        this.gameMapSnapshot = snapshot;
    }

    public GameMapSnapshot getGameMapSnapshot() {
        return gameMapSnapshot;
    }
}
