package com.example.main.enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum StoreMenuCommands {
    BuildBarnOrCoop("^build -a (?<buildingName>.+?)" +
            "-l(?<x>\\d+),(?<y>\\d+)$"),
    BuyAnimal("^buy animal -a (?<animal>[\\S\\s]+) -id (?<id>\\d+) -n (?<name>\\S+)$"),
    ShowAllProducts("show all products"),
    ShowAvailableProducts("show all available products"),
    Purchase("purchase -n (?<productName>\\S+) -a (?<amount>\\S+)"),
    GoToGameMenu("menu enter GameMenu");

    private final String pattern;

    StoreMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
