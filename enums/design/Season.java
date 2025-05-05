package enums.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum Season {
    Spring("Spring", new HashMap<Weather, Double>() {{
        put(Weather.Sunny, 0.5);
        put(Weather.Rainy, 0.4);
        put(Weather.Stormy, 0.1);
    }}),

    Summer("Summer", new HashMap<Weather, Double>() {{
        put(Weather.Sunny, 0.7);
        put(Weather.Rainy, 0.2);
        put(Weather.Stormy, 0.1);
    }}),

    Fall("Fall", new HashMap<Weather, Double>() {{
        put(Weather.Sunny, 0.4);
        put(Weather.Rainy, 0.4);
        put(Weather.Stormy, 0.2);
    }}),

    Winter("Winter", new HashMap<Weather, Double>() {{
        put(Weather.Sunny, 0.3);
        put(Weather.Snowy, 0.7);
    }}),

    Special("Special", new HashMap<Weather, Double>() {{
        put(Weather.Sunny, 0.3);
        put(Weather.Rainy, 0.25);
        put(Weather.Stormy, 0.25);
        put(Weather.Snowy, 0.2);
    }});

    private final String name;
    private final HashMap<Weather, Double> weathers;

    Season(String name, HashMap<Weather, Double> weathers) {
        this.name = name;
        this.weathers = weathers;
    }

    public Season getNextSeason() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Map<Weather, Double> weatherProbabilities() {
        return weathers;
    }
}
