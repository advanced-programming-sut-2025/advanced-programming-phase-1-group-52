package com.example.main.enums.design;

public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction fromString(String input) {
        return switch (input.toLowerCase()) {
            case "up" -> UP;
            case "down" -> DOWN;
            case "left" -> LEFT;
            case "right" -> RIGHT;
            case "up_left" -> UP_LEFT;
            case "up_right" -> UP_RIGHT;
            case "down_left" -> DOWN_LEFT;
            case "down_right" -> DOWN_RIGHT;
            default -> null;
        };
    }
}
