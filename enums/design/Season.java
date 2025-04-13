package enums.design;

import java.util.ArrayList;

public enum Season {
    Spring("spring", new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Rainy);
        add(Weather.Stormy);
    }}),
    Summer("summer", new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Rainy);
        add(Weather.Stormy);
    }}),
    Fall("fall", new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Rainy);
        add(Weather.Stormy);
    }}),
    Winter("winter", new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Snowy);
    }}),
    Special("special", new ArrayList<Weather>(){{
        add(Weather.Sunny);
        add(Weather.Rainy);
        add(Weather.Stormy);
        add(Weather.Snowy);
    }});

    private final String name;
    private final ArrayList<Weather> weathers;

    Season(String name, ArrayList<Weather> weathers) {
        this.name = name;
        this.weathers = weathers;
    }
}
