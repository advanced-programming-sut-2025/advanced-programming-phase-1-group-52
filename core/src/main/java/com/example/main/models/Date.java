package com.example.main.models;

import com.example.main.enums.design.Season;
import com.example.main.enums.design.Weekday;

public class Date {
    public static final int DAYS_PER_SEASON = 28;
    public static final int DAYS_PER_WEEK = 7;

    private Season currentSeason;
    private Weekday currentWeekday;
    private int currentDay;

    public Date() {
        this.currentSeason = Season.Spring;
        this.currentWeekday = Weekday.Sunday;
        this.currentDay = 1;
    }

    public int addDays(int days) {
        if (days <= 0) return 0;

        int seasonsPassed = 0;
        this.currentDay += days;

        while (this.currentDay > DAYS_PER_SEASON) {
            this.currentDay -= DAYS_PER_SEASON;
            this.currentSeason = this.currentSeason.getNextSeason();
            seasonsPassed++;
        }

        int weekdayAdvances = days % DAYS_PER_WEEK;
        for (int i = 0; i < weekdayAdvances; i++) {
            this.currentWeekday = this.currentWeekday.getNextDay();
        }

        return seasonsPassed;
    }

    public void advanceDay() {
        addDays(1);
    }

    public boolean isSeasonOver() {
        return currentDay == DAYS_PER_SEASON;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public Weekday getCurrentWeekday() {
        return currentWeekday;
    }

    public int getCurrentDay() {
        return currentDay;
    }

}
