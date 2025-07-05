package com.example.main.enums.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HomeMenuCommands {
    CraftingShowRecipes("crafting show recipes"),
    Crafting("crafting craft (?<itemName>[\\S\\s]+)"),
    CookingRefrigerator("cooking refrigerator(\\s+)(?<action>(put|pick)) (?<item>[\\S\\s]+)(\\s*)"),
    CookingShowRecipes("^(\\s*)cooking(\\s+)show(\\s+)recipes(\\s*)$"),
    CookingPrepare("^(\\s*)cooking(\\s+)prepare(\\s+)(?<recipeName>[\\S\\s]+)(\\s*)$"),
    Back("^(\\s*)back"),
    CheatAddCraftingRecipe("cheat add crafting recipe -n (?<name>[\\S\\s]+)"),
    CheatAddCookingRecipe("cheat add cooking recipe -n (?<name>[\\S\\s]+)");

    private final String pattern;

    HomeMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);
        return matcher.matches() ? matcher : null;
    }
}
