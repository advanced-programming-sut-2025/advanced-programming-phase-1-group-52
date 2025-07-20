package com.example.main.enums.regex;

import com.example.main.enums.design.Season;
import com.example.main.enums.design.Weather;

public enum NPCDialogs {
    dialog1(null, 0, null, "Hello there farmer!"),
    dialog2(null, 0, null, "You new around here?"),
    dialog3(null, 0, Weather.Rainy, "The rain is heavy today, \nisn't it?"),
    dialog4(null, 0, Weather.Stormy, "I hope the storm doesn't \ncause any damage."),
    dialog5(null, 0, Weather.Snowy, "The snow is beautiful, \nbut it makes it hard to get around."),
    dialog6(Season.Spring, 0, null, "Spring is a time for \nnew beginnings."),
    dialog7(Season.Summer, 0, null, "Summer is the best time \nfor fishing."),
    dialog8(Season.Fall, 0, null, "Fall is a time for harvest."),
    dialog9(Season.Winter, 0, null, "Winter is a time for rest."),
    dialog10(null, 1, null, "Oh hey, \nI didn't see you there."),
    dialog11(null, 1, null, "How's the farm doing?"),
    dialog12(null, 1, Weather.Rainy, "I love the sound of rain \non the roof."),
    dialog13(null, 1, Weather.Stormy, "The storm sometimes brings out \nthe best in people."),
    dialog14(null, 1, Weather.Snowy, "I love the snow, \nit's so peaceful."),
    dialog15(Season.Spring, 1, null, "Spring is so beautiful \naround here."),
    dialog16(Season.Summer, 1, null, "Ah it's so hot \naround this time of the year."),
    dialog17(Season.Fall, 1, null, "The leaves are so colorful \nin the fall."),
    dialog18(Season.Winter, 1, null, "Winter is just snow and chill, \nain't it?"),
    dialog19(null, 2, null, "Oh hi! Wanna hang out?"),
    dialog20(null, 2, null, "I love spending time with friends."),
    dialog21(null, 2, Weather.Rainy, "Rainy days are perfect for \nstaying inside and reading."),
    dialog22(null, 2, Weather.Stormy, "Stormy weather is perfect for \ncuddling up with a good book."),
    dialog23(null, 2, Weather.Snowy, "Snowy days are perfect for \nbuilding snowmen."),
    dialog24(Season.Spring, 2, null, "Nice weather we're having, \nhuh?"),
    dialog25(Season.Summer, 2, null, "Up for a vacation?"),
    dialog26(Season.Fall, 2, null, "Fall is a great time for a hike."),
    dialog27(Season.Winter, 2, null, "How about a snowball fight?"),
    dialog28(null, 3, null, "I'm so glad we met."),
    dialog29(null, 3, null, "I love spending time with you."),
    dialog30(null, 3, Weather.Rainy, "Rainy days are perfect for staying \ninside and watching movies."),
    dialog31(null, 3, Weather.Stormy, "Maybe we should stay at \nmy place during the storm."),
    dialog32(null, 3, Weather.Snowy, "Hot chocolate at your place?"),
    dialog33(Season.Spring, 3, null, "Spring is a time for love."),
    dialog34(Season.Summer, 3, null, "Wanna go to the beach?"),
    dialog35(Season.Fall, 3, null, "It's Fall again, \ndon't you feel romantic?"),
    dialog36(Season.Winter, 3, null, "All you need is a warm blanket \nand a window during a snowy day.");
    
    private final Season season;
    private final int level;
    private final Weather weather;
    private final String dialog;

    NPCDialogs(Season season, int level, Weather weather, String dialog) {
        this.season = season;
        this.level = level;
        this.weather = weather;
        this.dialog = dialog;
    }

    public Season getSeason() {
        return season;
    }

    public int getLevel() {
        return level;
    }

    public Weather getWeather() {
        return weather;
    }

    public String getDialog() {
        return dialog;
    }
}
