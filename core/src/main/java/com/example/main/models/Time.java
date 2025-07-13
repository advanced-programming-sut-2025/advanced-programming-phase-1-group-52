package com.example.main.models;

public class Time {
    public static final int DAY_START = 6; // Changed to 6 AM for a more standard start time
    public static final int DAY_END = 26; // Represents 2:00 AM the next day
    public static final int ACTIVE_HOURS = DAY_END - DAY_START;

    private int hour = DAY_START;
    private int minute = 0;

    public int addMinutes(int minutes) {
        if (minutes <= 0) return 0;

        int totalMinutes = (this.hour * 60 + this.minute) + minutes;

        this.hour = totalMinutes / 60;
        this.minute = totalMinutes % 60;

        int daysPassed = 0;
        if (this.hour >= DAY_END) {
            daysPassed = this.hour / DAY_END;
            this.hour = DAY_START + (this.hour % DAY_END);
        }

        return daysPassed;
    }

    public int addHours(int hours) {
        if (hours <= 0) return 0;
        return addMinutes(hours * 60);
    }

    public void advanceHour() {
        addHours(1);
    }

    public boolean isDayOver() {
        return this.hour >= DAY_END;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setHour(int hour) {
        if (hour < 0 || hour > 23) { // Allow full 24-hour range internally
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        this.hour = hour;
    }
}
