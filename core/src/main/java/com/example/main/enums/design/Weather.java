package com.example.main.enums.design;

public enum Weather {
    Sunny,
    Rainy,
    Stormy,
    Snowy;
    public static Weather fromString(String weatherStr) {
        for (Weather weather : values()) {
            if (weather.name().equalsIgnoreCase(weatherStr)) {
                return weather;
            }
        }
        throw new IllegalArgumentException("Invalid weather: " + weatherStr);
    }
}
