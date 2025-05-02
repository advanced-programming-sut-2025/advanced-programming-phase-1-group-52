package models;

public class Time {
    public static final int DAY_START = 9;
    public static final int DAY_END = 22;
    public static final int ACTIVE_HOURS = DAY_END - DAY_START;

    private int hour = DAY_START;

    public int addHours(int hours) {
        if (hours <= 0) return 0;

        int elapsedHours = (this.hour - DAY_START) + hours;

        int daysPassed = elapsedHours / ACTIVE_HOURS;
        int remainingHours = elapsedHours % ACTIVE_HOURS;
        this.hour = DAY_START + remainingHours;

        if (remainingHours == 0 && hours > 0) {
            this.hour = DAY_END;
        }

        return daysPassed;
    }

    public void advanceHour() {
        addHours(1);
    }

    public boolean isDayOver() {
        return this.hour >= DAY_END;
    }

    // Getters and setters
    public int hour() {
        return hour;
    }

    public void setHour(int hour) {
        if (hour < DAY_START || hour > DAY_END) {
            throw new IllegalArgumentException("Hour must be between 9 and 22");
        }
        this.hour = hour;
    }
}
