package models;

public class Time {
    private int hour = 9;

    public void hourPassed() {
        this.hour++;
    }

    public boolean isDayOver() {
        if (this.hour == 22) {
            this.dayPassed();
            return true;
        }

        return false;
    }

    private void dayPassed() {
        this.hour = 9;
    }
}
