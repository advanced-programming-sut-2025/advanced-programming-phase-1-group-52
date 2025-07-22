package com.example.main.enums.design;

import com.example.main.enums.items.CropType;
import com.example.main.enums.items.ForagingSeedType;

import java.util.*;

public enum Season {
    Spring("Spring",
            new HashMap<Weather, Double>() {{
                put(Weather.Sunny, 0.5);
                put(Weather.Rainy, 0.4);
                put(Weather.Stormy, 0.1);
            }},
            Arrays.asList(CropType.Cauliflower, CropType.Parsnip, CropType.Potato, CropType.Blue_Jazz, CropType.Tulip)
    ),

    Summer("Summer",
            new HashMap<Weather, Double>() {{
                put(Weather.Sunny, 0.7);
                put(Weather.Rainy, 0.2);
                put(Weather.Stormy, 0.1);
            }},
            Arrays.asList(CropType.Corn, CropType.Hot_Pepper, CropType.Radish, CropType.Wheat, CropType.Poppy, CropType.Sunflower, CropType.Summer_Spangle)
    ),

    Fall("Fall",
            new HashMap<Weather, Double>() {{
                put(Weather.Sunny, 0.4);
                put(Weather.Rainy, 0.4);
                put(Weather.Stormy, 0.2);
            }},
            Arrays.asList(CropType.Artichoke, CropType.Corn, CropType.Eggplant, CropType.Pumpkin, CropType.Sunflower, CropType.Fairy_Rose)
    ),

    Winter("Winter",
            new HashMap<Weather, Double>() {{
                put(Weather.Sunny, 0.3);
                put(Weather.Snowy, 0.7);
            }},
            Arrays.asList(CropType.PowderMelon)
    ),

    Special("Special",
            new HashMap<Weather, Double>() {{
                put(Weather.Sunny, 0.3);
                put(Weather.Rainy, 0.25);
                put(Weather.Stormy, 0.25);
                put(Weather.Snowy, 0.2);
            }},
            new ArrayList<>()
    );

    private final String name;
    private final HashMap<Weather, Double> weathers;
    private final List<CropType> crops;

    Season(String name, HashMap<Weather, Double> weathers, List<CropType> crops) {
        this.name = name;
        this.weathers = weathers;
        this.crops = crops;
    }

    public Season getNextSeason() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Map<Weather, Double> weatherProbabilities() {
        return weathers;
    }

    public List<CropType> getPossibleCrops() {
        return crops;
    }
}
