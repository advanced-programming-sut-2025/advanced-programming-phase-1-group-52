package com.example.main.network.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.example.main.models.User;

public class Lobby {
    private final String lobbyId;
    private final String name;
    private final String hostId;
    private final Map<String, User> players;
    private final Map<String, Boolean> playerReadyStatus;
    private boolean isPrivate;
    private String password;
    private boolean isVisible;
    private final int maxPlayers;
    private boolean gameStarted;
    private transient AtomicBoolean mapRequestSent = new AtomicBoolean(false);

    public Lobby(String lobbyId, String name, String hostId, boolean isPrivate, String password, boolean isVisible) {
        this.lobbyId = lobbyId;
        this.name = name;
        this.hostId = hostId;
        this.players = new ConcurrentHashMap<>();
        this.playerReadyStatus = new ConcurrentHashMap<>();
        this.isPrivate = isPrivate;
        this.password = password;
        this.isVisible = isVisible;
        this.maxPlayers = 4;
        this.gameStarted = false;
    }

    public boolean canStartGame() {
        // The check for allPlayersReady() has been removed.
        return players.size() >= 2 && players.size() <= maxPlayers;
    }
    public boolean allPlayersReady() {
        if (players.size() < 2) return false;
        return playerReadyStatus.values().stream().allMatch(ready -> ready);
    }

    public boolean checkPassword(String passwordAttempt) {
        if (!this.isPrivate) {
            return true; // Not private, no password needed
        }
        return this.password != null && this.password.equals(passwordAttempt);
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public String getName() {
        return name;
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
        playerReadyStatus.put(clientId, false);
        return true;
    }

    public boolean removePlayer(String clientId) {
        User removed = players.remove(clientId);
        playerReadyStatus.remove(clientId);
        return removed != null;
    }

    public boolean isPlayerInLobby(String clientId) {
        return players.containsKey(clientId);
    }

    public boolean setPlayerReady(String clientId, boolean ready) {
        if (players.containsKey(clientId)) {
            playerReadyStatus.put(clientId, ready);
            return true;
        }
        return false;
    }

    public boolean isPlayerReady(String clientId) {
        return playerReadyStatus.getOrDefault(clientId, false);
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
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
        return new HashMap<>(playerReadyStatus);
    }

    public boolean hasMapRequestBeenSentAndSet() {
        // This command atomically sets the value to 'true' and returns the PREVIOUS value.
        // So, it will only return 'false' on the very first time it is called for this lobby.
        return this.mapRequestSent.getAndSet(true);
    }
}
