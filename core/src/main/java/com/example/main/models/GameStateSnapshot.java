    package com.example.main.models;

    import com.example.main.enums.design.FarmThemes;
    import com.example.main.enums.player.Gender;

    import java.io.Serializable;
    import java.util.List;
    import java.util.ArrayList;


    public class GameStateSnapshot implements Serializable {

        private List<PlayerSnapshot> players;
        private List<FarmThemes> farmThemes;
        private String hostUsername;

        public GameStateSnapshot(List<PlayerSnapshot> players, List<FarmThemes> farmThemes, String hostUsername) {
            this.players = players;
            this.farmThemes = farmThemes;
            this.hostUsername = hostUsername;
        }


        public List<PlayerSnapshot> getPlayers() {
            return players;
        }

        public List<FarmThemes> getFarmThemes() {
            return farmThemes;
        }

        public String getHostUsername() {
            return hostUsername;
        }

        public static class PlayerSnapshot implements Serializable {
            private String username;
            private Gender gender;
            private int playerIndex;

            public PlayerSnapshot(String username, Gender gender, int playerIndex) {
                this.username = username;
                this.gender = gender;
                this.playerIndex = playerIndex;
            }

            public String getUsername() {
                return username;
            }

            public Gender getGender() {
                return gender;
            }

            public int getPlayerIndex() {
                return playerIndex;
            }
        }
    }
