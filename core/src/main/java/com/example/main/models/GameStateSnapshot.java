package com.example.main.models;

import com.example.main.enums.design.FarmThemes;
import com.example.main.enums.player.Gender;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * A lightweight, serializable object for sending the initial game state
 * from the server to the clients. It contains only the data needed to
 * construct the Game object on the client side.
 */
public class GameStateSnapshot implements Serializable {

    private List<PlayerSnapshot> players;
    private List<FarmThemes> farmThemes;
    private String hostUsername;

    public GameStateSnapshot(List<PlayerSnapshot> players, List<FarmThemes> farmThemes, String hostUsername) {
        this.players = players;
        this.farmThemes = farmThemes;
        this.hostUsername = hostUsername;
    }

    // Getters for the client to use
    public List<PlayerSnapshot> getPlayers() {
        return players;
    }

    public List<FarmThemes> getFarmThemes() {
        return farmThemes;
    }

    public String getHostUsername() {
        return hostUsername;
    }

    /**
     * A nested static class to represent a player's initial state.
     */
    public static class PlayerSnapshot implements Serializable {
        private String username;
        private Gender gender;

        public PlayerSnapshot(String username, Gender gender) {
            this.username = username;
            this.gender = gender;
        }

        public String getUsername() {
            return username;
        }

        public Gender getGender() {
            return gender;
        }
    }
}
