package com.example.main.network.common;

public enum MessageType {
    AUTHENTICATION,
    AUTH_SUCCESS,
    AUTH_FAILED,
    GAME_STATE,
    PLAYER_ACTION,
    PLAYER_JOIN,
    PLAYER_LEAVE,
    HEARTBEAT,
    DISCONNECT,
    ERROR,
    REGISTER,
    // Lobby messages
    LOBBY_JOIN,
    LOBBY_JOIN_FAILED,
    LOBBY_JOIN_SUCCESS,
    LOBBY_LEAVE,
    LOBBY_ADMIN_CHANGE,
    LOBBY_PLAYERS_UPDATE,
    LOBBY_READY,
    CREATE_LOBBY,
    LOBBY_START_GAME,
    // Online users messages
    REQUEST_ONLINE_USERS,
    ONLINE_USERS_UPDATE,
    // Lobby requests
    REQUEST_AVAILABLE_LOBBIES,
    AVAILABLE_LOBBIES_UPDATE,
    LOBBY_FIND_BY_ID,
    LOBBY_FIND_RESULT,
    // Game begins baby!
    NAVIGATE_TO_PREGAME,  // Server -> Client: Tells all players in the lobby to switch to the pre-game screen.
    SUBMIT_FARM_CHOICE,   // Client -> Server: A player sends their chosen farm type.
    GAME_SETUP_COMPLETE,
    PLAYER_MOVE,          // A message from a client to the server indicating they have moved.
    UPDATE_PLAYER_POSITIONS,
}
