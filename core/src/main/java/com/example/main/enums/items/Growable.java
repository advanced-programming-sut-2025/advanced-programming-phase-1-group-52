package com.example.main.enums.items;

public interface Growable {
    int getDayPassed();
    void setDayPassed(int dayPassed);

    int getDayRemaining();
    void setDayRemaining(int dayRemaining);

    boolean isWateredToday();
    void setWateredToday(boolean wateredToday);

    boolean isFertilizedToday();
    void setFertilizedToday(boolean fertilizedToday);

    boolean isNeedsWaterToday();
    void setNeedsWaterToday(boolean needsWaterToday);

    int getCurrentStage();
    void setCurrentStage(int currentStage);

    boolean isReadyToHarvest();
    void setReadyToHarvest(boolean readyToHarvest);

    void growFaster();

    boolean isNotWateredForTwoDays();

    void setNotWateredForTwoDays(boolean notWateredForTwoDays);
}
