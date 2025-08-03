package com.example.main.network.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.main.models.User;

public class Lobby {
    private final String lobbyId;
    private final String hostId;
    private final Map<String, User> players;
    private final Map<String, Boolean> playerReady;
    private final int maxPlayers;
    private boolean gameStarted;
    
    public Lobby(String lobbyId, String hostId) {
        this.lobbyId = lobbyId;
        this.hostId = hostId;
        this.players = new ConcurrentHashMap<>();
        this.playerReady = new ConcurrentHashMap<>();
        this.maxPlayers = 4;
        this.gameStarted = false;
    }
    
    public String getLobbyId() {
        return lobbyId;
    }
    
    public String getHostId() {
        return hostId;
    }
    
    public Map<String, User> getPlayers() {
        return new HashMap<>(players);
    }
    
    public List<String> getPlayerIds() {
        return new ArrayList<>(players.keySet());
    }
    
    public boolean addPlayer(String clientId, User user) {
        if (players.size() >= maxPlayers) {
            return false;
        }
        players.put(clientId, user);
        playerReady.put(clientId, false);
        return true;
    }
    
    public boolean removePlayer(String clientId) {
        User removed = players.remove(clientId);
        playerReady.remove(clientId);
        return removed != null;
    }
    
    public boolean isPlayerInLobby(String clientId) {
        return players.containsKey(clientId);
    }
    
    public boolean setPlayerReady(String clientId, boolean ready) {
        if (players.containsKey(clientId)) {
            playerReady.put(clientId, ready);
            return true;
        }
        return false;
    }
    
    public boolean isPlayerReady(String clientId) {
        return playerReady.getOrDefault(clientId, false);
    }
    
    public boolean allPlayersReady() {
        if (players.size() < 2) return false; // Need at least 2 players
        return playerReady.values().stream().allMatch(ready -> ready);
    }
    
    public boolean canStartGame() {
        return players.size() >= 2 && players.size() <= maxPlayers && allPlayersReady();
    }
    
    public boolean isFull() {
        return players.size() >= maxPlayers;
    }
    
    public int getPlayerCount() {
        return players.size();
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
    
    public User getPlayer(String clientId) {
        return players.get(clientId);
    }
    
    public String getHostUsername() {
        User host = players.get(hostId);
        return host != null ? host.getUsername() : "Unknown";
    }
    
    public Map<String, Boolean> getPlayerReadyStatus() {
        return new HashMap<>(playerReady);
    }
} 