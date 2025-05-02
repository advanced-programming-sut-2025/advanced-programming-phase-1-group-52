package models;

import enums.design.Season;
import enums.design.Weekday;

public class Date {
    private Season currentSeason = Season.Spring;
    private Weekday currentWeekday = Weekday.Sunday;
    private int currentDay = 1;

    public Date() {

    }

    public void dayPassed() {
        this.currentWeekday = this.currentWeekday.getNextDay();
        this.currentDay++;
    }

    public boolean isSeasonOver() {
        if (this.currentDay == 28) {
            this.seasonPassed();
            return true;
        }

        return false;
    }

    private void seasonPassed() {
        this.currentSeason = this.currentSeason.getNextSeason();
        this.currentDay = 1;
    }
}
