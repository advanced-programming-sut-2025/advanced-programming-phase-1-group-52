package com.example.main.enums.design;

public enum Weekday {
    Sunday,
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday;

    public Weekday getNextDay() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
