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
    ERROR
}