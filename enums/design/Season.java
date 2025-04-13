package enums.design;

import java.util.ArrayList;

public enum Season {
    Spring(new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Rainy);
        add(Weather.Stormy);
    }}),
    Summer(new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Rainy);
        add(Weather.Stormy);
    }}),
    Autumn(new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Rainy);
        add(Weather.Stormy);
    }}),
    Winter(new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Snowy);
    }});

    private final ArrayList<Weather> weathers;

    Season(ArrayList<Weather> weathers) {
        this.weathers = weathers;
    }

    public Season getNextSeason() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
